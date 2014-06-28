package de.tum.ziller.thesis.thrp.common.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.google.common.collect.TreeMultimap;
import com.rits.cloning.Cloner;

import de.tum.ziller.thesis.thrp.common.controller.Comparators;
import de.tum.ziller.thesis.thrp.common.entities.jobs.BreakJob;
import de.tum.ziller.thesis.thrp.common.entities.jobs.IdleJob;
import de.tum.ziller.thesis.thrp.common.entities.jobs.OutpatientJob;
import de.tum.ziller.thesis.thrp.common.entities.jobs.TreatmentJob;
import de.tum.ziller.thesis.thrp.common.entities.jobs.WardJob;
import de.tum.ziller.thesis.thrp.common.entities.rooms.TherapyCenter;
import de.tum.ziller.thesis.thrp.common.exceptions.RouteConstructionException;
import de.tum.ziller.thesis.thrp.common.utils.OutputUtil;
import de.tum.ziller.thesis.thrp.common.utils.TimeUtil;
import de.tum.ziller.thesis.thrp.heuristic.SolverConfiguration;

public @Getter
@Setter
class Solution implements Cloneable, Serializable {

	@AllArgsConstructor
	static class SolutionStatusWrapper implements Serializable{

		/**
		 * 
		 */
		private static final long	serialVersionUID	= 2973784589420087042L;
		public static SolutionStatusWrapper createNew() {
			return new SolutionStatusWrapper(TreeMultimap.create(Comparators.THERAPIST_ID_ASCENDING, Comparators.TIMESLOTS_ASCENDING_BY_START), new TreeMap<Therapist, Room[]>(
					Comparators.THERAPIST_ID_ASCENDING), TreeMultimap.create(Comparators.ROOM_ID_ASCENDING, Comparators.TIMESLOTS_ASCENDING_BY_START) 
					);
		}

		private transient TreeMultimap<Therapist, Timeslot>	P_availabilities	= TreeMultimap.create(Comparators.THERAPIST_ID_ASCENDING, Comparators.TIMESLOTS_ASCENDING_BY_START);
		private transient TreeMap<Therapist, Room[]>		P_locations			= new TreeMap<>(Comparators.THERAPIST_ID_ASCENDING);
		private transient TreeMultimap<Room, Timeslot>		R_availabilities	= TreeMultimap.create(Comparators.ROOM_ID_ASCENDING, Comparators.TIMESLOTS_ASCENDING_BY_START);

	}

	public void nullifyWrapper() {
		ssw = null;
	}

	private static final long						serialVersionUID	= 153274005962954988L;

	private transient Long							t					= 0L;
	private Double									costs				= 0.;
	private transient Cloner						cloner				= new Cloner();
	private transient Solution						predecessor			= null;
	private TreeMultimap<Therapist, Route>			routes				= TreeMultimap.create(Comparators.THERAPIST_ID_ASCENDING, Comparators.ROUTE_START_ASCENDING);
	private Long									t_s					= System.currentTimeMillis();
	private Long									t_S_init;
	private Long									t_S_compl;
	private transient String						uid					= UUID.randomUUID().toString();

	private transient SolutionStatusWrapper					ssw					= SolutionStatusWrapper.createNew();
	private Integer									minutesPerTimeslot;
	private Set<Job>								unscheduledJobs;
	private Instance								instance;
	private SolverConfiguration						config;
	private Insertion								i_last;
	private transient TreeMultimap<Therapist, Node>	P_routes			= TreeMultimap.create(Comparators.THERAPIST_ID_ASCENDING, Comparators.NODE_START_ASCENDING);
	private transient Set<Node>						N_all				= new TreeSet<>(Comparators.NODE_START_ASCENDING);


	public Solution(Instance is, SolverConfiguration sc) {
		instance = is;
		unscheduledJobs = is.getJobs();
		config = sc;
		minutesPerTimeslot = is.getI_conf().getMinutesPerTimeslot();
	}

	/**
	 * F�gt einen Job-Raum Kombination in den Graph ein. Gibt die Route auf der die Aktion eingef�gt wurde zur�ck
	 * 
	 * @author Markus Z.
	 * @date 21.12.2013
	 * @param i
	 * @return
	 * @throws RouteConstructionException
	 * 
	 */
	public Route apply(Insertion i) throws RouteConstructionException {

		Set<Route> R = routes.get(i.getTherapist());
		for (Route r : R) {
			if (r.getStartTime() <= i.getStart() && r.getEndTime() >= i.getEnd()) {
				r.insert(i.getNode());
				r.cleanup();
				update();
				
				costs = getSolutionCosts();
				i_last = i;
				return r;
			}
		}
		throw new RouteConstructionException("No fitting Pathway of Therapist " + i.getTherapist().getName() + " has been found for the PNI");
	}

	/**
	 * 
	 * @author Markus Z.
	 * @date 27.12.2013
	 * @param n
	 * @throws RouteConstructionException
	 * 
	 */
	public void remove(Node n) throws RouteConstructionException {
		for (Therapist t : routes.keySet()) {
			Set<Route> R = routes.get(t);
			for (Route r : R) {
				if (r.contains(n)) {
					r.remove(n);
					unscheduledJobs.add(n.getJob());
					update();

					costs = getSolutionCosts();
					return;
				}
			}
		}

	}
	
	/**
	 * 
	 * @author Markus Z.
	 * @date 27.12.2013
	 * @param n
	 * @throws RouteConstructionException
	 * 
	 */
	public void remove(Node n, Route r) throws RouteConstructionException {
		
		if(!r.contains(n)){
			System.err.println("Node not on Route");
		}

		if (r.contains(n)) {
			r.remove(n);
			unscheduledJobs.add(n.getJob());
			update();
			costs = getSolutionCosts();
			return;
		}

	}


	public Set<Node> getAllNodes() {
		return N_all;
	}

	public Set<Node> getAllNodes(Therapist t) {
		return P_routes.get(t);
	}

	public List<Timeslot> getIdleTimeForTherapist(Therapist t) {
		return new ArrayList<>(ssw.P_availabilities.get(t));
	}

	public Room getLocation(Integer t, Therapist tp) throws RouteConstructionException {
		return ssw.P_locations.get(tp)[t];
	}

	public TreeMultimap<Room, Timeslot> getVacanciesForAllRooms() {
		return ssw.R_availabilities;
	}

	/**
	 * 
	 * @author Markus Z.
	 * @date 27.12.2013
	 * 
	 */
	public void update() {

		updateAllNodes();
		updateTherapistLocations();
		updateTherapistAvailabilities();
		updateRoomAvailabilities();
	}

	private void updateAllNodes() {
		P_routes.clear();
		N_all.clear();

		for (Therapist t : routes.keySet()) {
			for (Route r : routes.get(t)) {
				Set<Node> nodes = r.getN();
				P_routes.putAll(t, nodes);
				N_all.addAll(r.getN());
			}
		}
	}

	private void updateTherapistLocations() {
		ssw.P_locations.clear();
		Set<Therapist> t = instance.getTherapists();
		int r_size = instance.getI_conf().getNumberOfTimeSlots() + 1;

		for (Therapist th : t) {
			Room[] rr = new Room[r_size];
			for (Route r : routes.get(th)) {
				for (Node n : r.getN()) {
					for (int i = n.getStart(); i <= n.getEnd(); i++) {
						rr[i] = n.getRoom();
					}
				}
			}
			ssw.P_locations.put(th, rr);
		}

	}

	private void updateTherapistAvailabilities() {
		ssw.P_availabilities.clear();
		Set<Therapist> t = instance.getTherapists();

		for (Therapist th : t) {
			for (Route r : routes.get(th)) {
				for (Node n : r.getN()) {
					if (n.getJob() instanceof IdleJob) {
						ssw.P_availabilities.put(th, n.getTime());
					}
				}

			}
		}

	}

	private void updateRoomAvailabilities() {
		Set<Node> nodes = N_all;
		ssw.R_availabilities.clear();
		Comparator<Room> roomComp = new Comparator<Room>() {

			@Override
			public int compare(Room o1, Room o2) {
				if (o1.getId() < o2.getId()) {
					return 1;
				}
				if (o1.getId() == o2.getId()) {
					return 0;
				}
				return -1;
			}
		};

		// TreeMultimap<Room, Timeslot> vac = TreeMultimap.create(roomComp, Comparators.TIMESLOTS_ASCENDING_BY_START);
		TreeMultimap<Room, Timeslot> occ = TreeMultimap.create(roomComp, Comparators.TIMESLOTS_ASCENDING_BY_START);

		for (Node n : nodes) {
			if (n.isTreatment()) {
				occ.put(n.getRoom(), n.getTime());
				// TODO Kapazit�t!!
			}
		}

		for (Room r_ : instance.getRooms()) {
			Timeslot full = new Timeslot(0, instance.getI_conf().getNumberOfTimeSlots());
			// NavigableSet<Room> sss = occ.keySet();
			if (occ.containsKey(r_)) {

				List<Timeslot> vacant = TimeUtil.subtract(full, new LinkedList<>(occ.get(r_)));
				ssw.R_availabilities.removeAll(r_);
				ssw.R_availabilities.putAll(r_, vacant);
			} else {
				ssw.R_availabilities.put(r_, full);
			}
		}
	}

	public void renewUID() {
		uid = UUID.randomUUID().toString();
	}

	public void refreshWrapper() {
		ssw = SolutionStatusWrapper.createNew();
	}

	public TreeMultimap<Therapist, Route> addRoute(Therapist t, Route p) {
		routes.put(t, p);
		return routes;
	}

	public Integer[] planningBounds() {

		Integer[] bounds = new Integer[] { Integer.MAX_VALUE, Integer.MIN_VALUE };

		for (Therapist t : routes.keySet()) {
			Timeslot ts = getShiftBoundsForTherapist(t);
			if (ts.getStart() < bounds[0]) {
				bounds[0] = ts.getStart();
			}

			if (ts.getEnd() > bounds[1]) {
				bounds[1] = ts.getEnd();
			}

		}

		return bounds;
	}

	public Timeslot getShiftBoundsForTherapist(Therapist t) {
		Integer start = routes.get(t).first().getStartTime();
		Integer end = routes.get(t).last().getEndTime();

		return new Timeslot(start, end);
	}

	@Override
	public String toString() {
		return routes.toString();
	}

	public Double unscheduledJobsAverageLength() {
		Integer sum = 0;
		for (Job j : unscheduledJobs) {
			sum += j.getDurationSlots();
		}

		return new Double(sum) / new Double(unscheduledJobs.size());
	}

	/**
	 * clone() methode.
	 * 
	 * @author Markus Z.
	 * @date 30.06.2013
	 * @param b
	 *            true: detailierter Log, false: kein log
	 * @return
	 * 
	 */
	@Override
	public Solution clone() {
		if (cloner == null) {
			cloner = new Cloner();
		}

		cloner.setDumpClonedClasses(false);
		cloner.dontCloneInstanceOf(Job.class, Therapist.class, Room.class, Qualification.class, Instance.class, SolverConfiguration.class, SolutionStatusWrapper.class);

		Solution clone = cloner.deepClone(this);

		return clone;
	}

	public Solution cloneIncludingWrapper() {
		if (cloner == null) {
			cloner = new Cloner();
		}

		cloner.setDumpClonedClasses(false);
		cloner.dontCloneInstanceOf(Job.class, Therapist.class, Room.class, Qualification.class, Instance.class, SolverConfiguration.class);

		Solution clone = cloner.deepClone(this);

		return clone;
	}

	/**
	 * 
	 * @author Markus Z.
	 * @date 27.12.2013
	 * @return
	 * 
	 */
	public boolean isComplete() {
		return unscheduledJobs.size() == 0;
	}


	public boolean isActive(Therapist t) {
		Set<Route> R = routes.get(t);
		for (Route r : R) {
			for (Node n : r.getN()) {
				if (n.getJob() instanceof TreatmentJob) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @author Markus Z.
	 * @date 27.12.2013
	 * @return
	 * 
	 */
	public int getNoOfScheduledJobs() {
		int cnt = 0;

		for(Therapist t : instance.getTherapists()){
			for(Node n : P_routes.get(t)){
				if (!(n.getJob() instanceof BreakJob || n.getJob() instanceof IdleJob)) {
					cnt++;
				}
			}
		}

		return cnt;
	}

	/**
	 * 
	 * @author Markus Z.
	 * @date 27.12.2013
	 * @return
	 * 
	 */
	private Double getSolutionCosts() {
		double c = 0.;
		for (Therapist t : instance.getTherapists()) {
			Set<Route> R = routes.get(t);
			for (Route r : R) {
				Double d = getRouteCosts(r);
				c += d;
			}
		}

		return c;
	}

	public double getRouteCosts(Route r) {
		Node last = null;
		double c_t = 0.;
		double c_r = 0.;
		for (Node n : r.getN()) {
			Room s = n.getRoom();
			Job j = n.getJob();

			if (j instanceof WardJob && s instanceof TherapyCenter) {

				WardJob wj = (WardJob) j;
				c_t += instance.getTransportCosts(wj.getRoom(), s);
				c_t += instance.getTransportCosts(wj.getRoom(), s);
			}
			
			if (j instanceof OutpatientJob) {

				c_t += instance.getTransportCosts(instance.getBreakroom(), s);
				c_t += instance.getTransportCosts(s, instance.getBreakroom());
			}

			if (last != null) {

				Room ro = last.getRoom();
				Double d = instance.getRouteCosts(ro, s);
				c_r += d;
			}
			last = n;
		}
		return c_r + c_t;
	}

	public void removeHistory() {
		predecessor = null;

	}

	public double getFitness() {
		double a = 0.9999;
		double b = 0.0001;

		double active_v = activeVehicles();
		double c_uns = instance.getC_max() * 2 + 1;
		double m = active_v + (getUnscheduledJobs().size() * instance.getTherapists().size() + 1);
		double d = getCosts() + getUnscheduledJobs().size() * c_uns;

		return a*m + b*d;
	}
	
	public double getCostFitness() {
		double c_uns = instance.getC_max() * 2 + 1;
		return getCosts() + getUnscheduledJobs().size() * c_uns;
	}
	
	public int getVehicleFitness() {
		int active_v = activeVehicles();
		return active_v + (getUnscheduledJobs().size() * instance.getTherapists().size()+1);
	}

	public void complete() {
		t_S_compl = System.currentTimeMillis() - t_s;
	}

	public int activeVehicles() {
		int i = 0;
		for (Therapist t : instance.getTherapists()) {
			if (isActive(t)) {
				i++;
			}
		}
		return i;
	}

	/**
	 * 
	 * @author Markus Z.
	 * @date 27.12.2013
	 * @return
	 * 
	 */
	public String getGraphicalOutput() {
		return OutputUtil.graphicalOutput(this);
	}

	public void evaluateConsitency() {
		Set<Job> jobs = new HashSet<>(instance.getJobs());
		Set<Job> in_s = new HashSet<>();

		Set<Job> unscheduled = new HashSet<>(unscheduledJobs);

		for (Therapist t : instance.getTherapists()) {
			Set<Route> R = routes.get(t);
			for (Route r : R) {
				Node[] N = r.getN().toArray(new Node[0]);

				for (int i = 0; i < N.length - 1; i++) {

					Node n = N[i];

					if (in_s.contains(n.getJob())) {
						System.err.println("Job " + n.getJob() + " doppelt geplant");
					}
					if (n.getJob() instanceof TreatmentJob) {
						in_s.add(n.getJob());
					}

					if (!n.isIdle()) {

						for (int j = i; j < N.length; j++) {

							Node m = N[j];

							if (n.getRoom() != m.getRoom()) {

								int t_nm = instance.getTravelTime(n.getRoom(), m.getRoom());
								if (n.getEnd() + t_nm > m.getStart()) {
									System.err.println("traveltime: " + t_nm + " but " + n + " ends at " + n.getEnd() + " and " + m + " starts at " + m.getStart());
								}
								break;
							}
						}
					}
				}

				if (N[N.length - 1].getJob() instanceof TreatmentJob) {
					in_s.add(N[N.length - 1].getJob());
				}

			}
		}

		jobs.removeAll(unscheduled);
		jobs.removeAll(in_s);
		if (jobs.size() > 0) {
			System.out.println("Scheduled Nodes in solution: " + in_s.size());
			System.out.println("Unscheduled Nodes in solution: " + unscheduled.size());
			System.out.println("Instance Jobs \\scheduled \\uns: " + jobs.size());
		}
	}
}