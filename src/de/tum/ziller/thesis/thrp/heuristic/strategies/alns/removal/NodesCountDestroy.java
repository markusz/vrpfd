package de.tum.ziller.thesis.thrp.heuristic.strategies.alns.removal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.TreeMultimap;

import de.tum.ziller.thesis.thrp.common.entities.Node;
import de.tum.ziller.thesis.thrp.common.entities.Route;
import de.tum.ziller.thesis.thrp.common.entities.Solution;
import de.tum.ziller.thesis.thrp.common.entities.Therapist;
import de.tum.ziller.thesis.thrp.heuristic.strategies.alns.ALNSAbstractOperation;

/**
 * Entfernt die Routen mit den wenigsten/meisten Knoten, jedoch nie mehr als q Knoten. Es werden nur ganzen Routen entfernt
 * 
 * @param highestFirst
 *            true = l�ngste Routen zuerst, false = k�rzeste zuerst
 */
public class NodesCountDestroy extends ALNSAbstractOperation implements IALNSDestroy {
	int	c_g		= 1;
	int	c_l		= -1;

	public NodesCountDestroy(boolean highestFirst) {
		int modifier = highestFirst ? -1 : 1;
		c_g *= modifier;
		c_l *= modifier;
	}

	@Override
	public Solution destroy(final Solution from, int q) throws Exception {
		TreeMultimap<Therapist, Route> map = from.getRoutes();
		TreeSet<Route> routes = new TreeSet<>(new Comparator<Route>() {
			@Override
			public int compare(Route o1, Route o2) {
				if (o1.noOfTreatmentJobs() > o2.noOfTreatmentJobs()) {
					return c_g;
				}
				if (o1.noOfTreatmentJobs() < o2.noOfTreatmentJobs()) {
					return c_l;
				}
				return 0;
			}
		});
		ArrayList<Removal> removals = new ArrayList<>();
		for (Therapist t : map.keySet()) {
			Set<Route> R = map.get(t);
			for (Route r : R) {
				if (r.noOfTreatmentJobs() > 0) {
					routes.add(r);
				}
			}
		}
		for (Route r : routes) {
			if (r.noOfTreatmentJobs() <= q) {
				if (q < 1) {
					break;
				}
				for (Node m : r.getN()) {
					if (q > 0) {
						if (m.isTreatment()) {
							removals.add(new Removal(m, r));
							q--;
						}
					} else {
						break;
					}
				}
			}
		}
		for (Removal w : removals) {
			from.remove(w.n, w.r);
		}
		return from;
	}
}