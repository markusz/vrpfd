package de.tum.ziller.thesis.thrp.instancegenerator;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import de.tum.ziller.thesis.thrp.common.entities.jobs.ICUJob;
import de.tum.ziller.thesis.thrp.common.entities.jobs.OutpatientJob;
import de.tum.ziller.thesis.thrp.common.entities.jobs.WardJob;
import de.tum.ziller.thesis.thrp.common.entities.rooms.ICU;
import de.tum.ziller.thesis.thrp.common.entities.rooms.TherapyCenter;
import de.tum.ziller.thesis.thrp.common.entities.rooms.Ward;
import de.tum.ziller.thesis.thrp.common.exceptions.InstanceConfigurationException;
import de.tum.ziller.thesis.thrp.instancegenerator.random.EqualDistribution;
import de.tum.ziller.thesis.thrp.instancegenerator.random.GaussDistribution;
import de.tum.ziller.thesis.thrp.instancegenerator.random.IProbabilityDistribution;
import de.tum.ziller.thesis.thrp.instancegenerator.random.PoissonDistribution;

import java.io.Serializable;
import java.util.logging.Logger;

public class InstanceConfiguration implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7862267106160181806L;


	private final static Logger log = Logger.getLogger(InstanceConfiguration.class.getName());
	

	private Integer qualificationsQuantity = 4;
	private Integer therapistsQuantity = 22;

	private Integer totalRoomsQuantity = 50;
	private Integer totalJobsQuantity = 120;

	// Rooms
	private Integer ICURoomsQuantity;
	private Integer WardRoomsQuantity;
	private Integer TCRoomsQuantity;

	private Double ICURoomsRatio = 0.16;
	private Double WardRoomsRatio = 0.5;
	private Double TCRoomsRatio = 0.34;

	private Double costPerCell = 1.0;
	
	private Double routingCostFactor = 1.;
	private Double patientTransportCostFactor = .0;

	// Jobs
	private Integer ICUJobsQuantity;
	private Integer WardJobsQuantity;
	private Integer OutpatientJobsQuantity;

	private Double ICUJobsRatio = 0.1;
	private Double WardJobsRatio = 0.5;
	private Double OutpatientJobsRatio = 0.4;

	private Double avgJobsPerWard = (WardJobsRatio * totalJobsQuantity) / (WardRoomsRatio * totalRoomsQuantity);
	private Double avgJobsPerICU = (ICUJobsRatio * totalJobsQuantity) / (ICURoomsRatio * totalRoomsQuantity);
	private Double avgJobsPerTC = (OutpatientJobsRatio * totalJobsQuantity) / (TCRoomsRatio * totalRoomsQuantity);

	

	/**
	 * in minutes
	 */
	private Integer minutesPerTimeslot = 15;
	private Integer minutesPerPlanningHorizon = 24 * 60;

	private Integer numberOfTimeSlots = minutesPerPlanningHorizon / minutesPerTimeslot;

    @java.beans.ConstructorProperties({"qualificationsQuantity", "therapistsQuantity", "totalRoomsQuantity", "totalJobsQuantity", "ICURoomsQuantity", "WardRoomsQuantity", "TCRoomsQuantity", "ICURoomsRatio", "WardRoomsRatio", "TCRoomsRatio", "costPerCell", "routingCostFactor", "patientTransportCostFactor", "ICUJobsQuantity", "WardJobsQuantity", "OutpatientJobsQuantity", "ICUJobsRatio", "WardJobsRatio", "OutpatientJobsRatio", "avgJobsPerWard", "avgJobsPerICU", "avgJobsPerTC", "minutesPerTimeslot", "minutesPerPlanningHorizon", "numberOfTimeSlots", "firstBreakLength_MINUTES", "firstBreakFlexibility_MINUTES", "secondBreakLength_MINUTES", "secondBreakFlexibility_MINUTES", "JobMinLengths_MINUTES", "JobAvgLengths_MINUTES", "JobMaxLengths_MINUTES", "firstBreakLength", "firstBreakFlexibility", "secondBreakLength", "secondBreakFlexibility", "JobMinLengths_TS", "JobAvgLengths_TS", "JobMaxLengths_TS", "possibleShiftStartingSlots", "possibleShiftLengths", "qualificationToTherapistAssignment", "qualificationToJobAssignment", "timeslotToJobAssignment", "shiftStartAssignment", "shiftDurationAssignment", "roomToJobAssignment", "useQ"})
    public InstanceConfiguration(Integer qualificationsQuantity, Integer therapistsQuantity, Integer totalRoomsQuantity, Integer totalJobsQuantity, Integer ICURoomsQuantity, Integer WardRoomsQuantity, Integer TCRoomsQuantity, Double ICURoomsRatio, Double WardRoomsRatio, Double TCRoomsRatio, Double costPerCell, Double routingCostFactor, Double patientTransportCostFactor, Integer ICUJobsQuantity, Integer WardJobsQuantity, Integer OutpatientJobsQuantity, Double ICUJobsRatio, Double WardJobsRatio, Double OutpatientJobsRatio, Double avgJobsPerWard, Double avgJobsPerICU, Double avgJobsPerTC, Integer minutesPerTimeslot, Integer minutesPerPlanningHorizon, Integer numberOfTimeSlots, Integer firstBreakLength_MINUTES, Integer firstBreakFlexibility_MINUTES, Integer secondBreakLength_MINUTES, Integer secondBreakFlexibility_MINUTES, Integer[] JobMinLengths_MINUTES, Integer[] JobAvgLengths_MINUTES, Integer[] JobMaxLengths_MINUTES, Integer firstBreakLength, Integer firstBreakFlexibility, Integer secondBreakLength, Integer secondBreakFlexibility, Integer[] JobMinLengths_TS, Integer[] JobAvgLengths_TS, Integer[] JobMaxLengths_TS, Integer[] possibleShiftStartingSlots, Integer[] possibleShiftLengths, IProbabilityDistribution qualificationToTherapistAssignment, IProbabilityDistribution qualificationToJobAssignment, IProbabilityDistribution timeslotToJobAssignment, IProbabilityDistribution shiftStartAssignment, IProbabilityDistribution shiftDurationAssignment, Multimap<Class, Class> roomToJobAssignment, boolean useQ) {
        this.qualificationsQuantity = qualificationsQuantity;
        this.therapistsQuantity = therapistsQuantity;
        this.totalRoomsQuantity = totalRoomsQuantity;
        this.totalJobsQuantity = totalJobsQuantity;
        this.ICURoomsQuantity = ICURoomsQuantity;
        this.WardRoomsQuantity = WardRoomsQuantity;
        this.TCRoomsQuantity = TCRoomsQuantity;
        this.ICURoomsRatio = ICURoomsRatio;
        this.WardRoomsRatio = WardRoomsRatio;
        this.TCRoomsRatio = TCRoomsRatio;
        this.costPerCell = costPerCell;
        this.routingCostFactor = routingCostFactor;
        this.patientTransportCostFactor = patientTransportCostFactor;
        this.ICUJobsQuantity = ICUJobsQuantity;
        this.WardJobsQuantity = WardJobsQuantity;
        this.OutpatientJobsQuantity = OutpatientJobsQuantity;
        this.ICUJobsRatio = ICUJobsRatio;
        this.WardJobsRatio = WardJobsRatio;
        this.OutpatientJobsRatio = OutpatientJobsRatio;
        this.avgJobsPerWard = avgJobsPerWard;
        this.avgJobsPerICU = avgJobsPerICU;
        this.avgJobsPerTC = avgJobsPerTC;
        this.minutesPerTimeslot = minutesPerTimeslot;
        this.minutesPerPlanningHorizon = minutesPerPlanningHorizon;
        this.numberOfTimeSlots = numberOfTimeSlots;
        this.firstBreakLength_MINUTES = firstBreakLength_MINUTES;
        this.firstBreakFlexibility_MINUTES = firstBreakFlexibility_MINUTES;
        this.secondBreakLength_MINUTES = secondBreakLength_MINUTES;
        this.secondBreakFlexibility_MINUTES = secondBreakFlexibility_MINUTES;
        this.JobMinLengths_MINUTES = JobMinLengths_MINUTES;
        this.JobAvgLengths_MINUTES = JobAvgLengths_MINUTES;
        this.JobMaxLengths_MINUTES = JobMaxLengths_MINUTES;
        this.firstBreakLength = firstBreakLength;
        this.firstBreakFlexibility = firstBreakFlexibility;
        this.secondBreakLength = secondBreakLength;
        this.secondBreakFlexibility = secondBreakFlexibility;
        this.JobMinLengths_TS = JobMinLengths_TS;
        this.JobAvgLengths_TS = JobAvgLengths_TS;
        this.JobMaxLengths_TS = JobMaxLengths_TS;
        this.possibleShiftStartingSlots = possibleShiftStartingSlots;
        this.possibleShiftLengths = possibleShiftLengths;
        this.qualificationToTherapistAssignment = qualificationToTherapistAssignment;
        this.qualificationToJobAssignment = qualificationToJobAssignment;
        this.timeslotToJobAssignment = timeslotToJobAssignment;
        this.shiftStartAssignment = shiftStartAssignment;
        this.shiftDurationAssignment = shiftDurationAssignment;
        this.roomToJobAssignment = roomToJobAssignment;
        this.useQ = useQ;
    }

    public InstanceConfiguration() {
    }

    public Integer getIndexOfLastTimeSlot() {
		return numberOfTimeSlots - 1;
	}
	
	private Integer firstBreakLength_MINUTES = 45;
	private Integer firstBreakFlexibility_MINUTES = 30;
	private Integer secondBreakLength_MINUTES = 15;
	private Integer secondBreakFlexibility_MINUTES = 30;
	
	/**
	 * Min, Avg. Max Jobl�ngen in Minuten pro Job
	 * [0] = ICU, [1] = Ward, [2] = Outpatient
	 */
	private Integer[] JobMinLengths_MINUTES = new Integer[]{Math.max(10, minutesPerTimeslot),Math.max(10, minutesPerTimeslot),Math.max(10, minutesPerTimeslot)};
	private Integer[] JobAvgLengths_MINUTES = new Integer[]{20,20,20};
	private Integer[] JobMaxLengths_MINUTES = new Integer[]{45,45,45};
	
	private Integer firstBreakLength = (int) Math.floor(firstBreakLength_MINUTES.doubleValue() / minutesPerTimeslot.doubleValue());
	private Integer firstBreakFlexibility = (int) Math.floor(firstBreakFlexibility_MINUTES.doubleValue() / minutesPerTimeslot.doubleValue());
	private Integer secondBreakLength = (int) Math.floor(secondBreakLength_MINUTES.doubleValue() / minutesPerTimeslot.doubleValue());
	private Integer secondBreakFlexibility = (int) Math.floor(secondBreakFlexibility_MINUTES.doubleValue() / minutesPerTimeslot.doubleValue());
	
	
	/**
	 * Min, Avg. Max Jobl�ngen in Timeslots pro Job
	 * [0] = ICU, [1] = Ward, [2] = Outpatient
	 */
	private Integer[] JobMinLengths_TS = new Integer[]{
			(int) Math.floor(JobMinLengths_MINUTES[0].doubleValue() / minutesPerTimeslot.doubleValue()),
			(int) Math.floor(JobMinLengths_MINUTES[1].doubleValue() / minutesPerTimeslot.doubleValue()),
			(int) Math.floor(JobMinLengths_MINUTES[2].doubleValue() / minutesPerTimeslot.doubleValue())};
	
	private Integer[] JobAvgLengths_TS = new Integer[]{
			(int) Math.floor(JobAvgLengths_MINUTES[0].doubleValue() / minutesPerTimeslot.doubleValue()),
			(int) Math.floor(JobAvgLengths_MINUTES[1].doubleValue() / minutesPerTimeslot.doubleValue()),
			(int) Math.floor(JobAvgLengths_MINUTES[2].doubleValue() / minutesPerTimeslot.doubleValue())};
	
	private Integer[] JobMaxLengths_TS = new Integer[]{
			(int) Math.floor(JobMaxLengths_MINUTES[0].doubleValue() / minutesPerTimeslot.doubleValue()),
			(int) Math.floor(JobMaxLengths_MINUTES[1].doubleValue() / minutesPerTimeslot.doubleValue()),
			(int) Math.floor(JobMaxLengths_MINUTES[2].doubleValue() / minutesPerTimeslot.doubleValue())};
	
	
	
	
//	private Integer ICUJobMinLength_MINUTES = 10;
//	private Integer ICUJobMaxLength_MINUTES = 45;
//	private Integer WardJobMinLength_MINUTES = 10;
//	private Integer WardJobMaxLength_MINUTES = 45;
//	private Integer OutpatientJobMinLength_MINUTES = 10;
//	private Integer OutpatientJobMaxLength_MINUTES = 45;
//	private Integer ICUJobMinLength_TS = (int) Math.floor(ICUJobMinLength_MINUTES.doubleValue() / minutesPerTimeslot.doubleValue());;
//	private Integer ICUJobMaxLength_TS = (int) Math.floor(ICUJobMaxLength_MINUTES.doubleValue() / minutesPerTimeslot.doubleValue());
//	private Integer OutpatientJobMinLength_TS = (int) Math.floor(OutpatientJobMinLength_MINUTES.doubleValue() / minutesPerTimeslot.doubleValue());;
//	private Integer OutpatientJobMaxLength_TS = (int) Math.floor(OutpatientJobMaxLength_MINUTES.doubleValue() / minutesPerTimeslot.doubleValue());
//	private Integer WardJobMinLength_TS = (int) Math.floor(WardJobMinLength_MINUTES.doubleValue() / minutesPerTimeslot.doubleValue());;
//	private Integer WardJobMaxLength_TS = (int) Math.floor(WardJobMaxLength_MINUTES.doubleValue() / minutesPerTimeslot.doubleValue());

	private Integer[] possibleShiftStartingSlots = new Integer[] { 0 * 60 / minutesPerTimeslot, 4 * 60 / minutesPerTimeslot, 8 * 60 / minutesPerTimeslot, 12 * 60 / minutesPerTimeslot, 16 * 60 / minutesPerTimeslot };
	private Integer[] possibleShiftLengths = new Integer[] { 4 * 60 / minutesPerTimeslot, 8 * 60 / minutesPerTimeslot };
	
	private transient IProbabilityDistribution qualificationToTherapistAssignment = new PoissonDistribution(1.5);
	private transient IProbabilityDistribution qualificationToJobAssignment = new PoissonDistribution(1.0);
	private transient IProbabilityDistribution timeslotToJobAssignment = new PoissonDistribution(2. + Math.random() * 2.);
	private transient IProbabilityDistribution shiftStartAssignment = new EqualDistribution();
	private transient IProbabilityDistribution shiftDurationAssignment = new GaussDistribution(0.0, 1.3);

	@SuppressWarnings("rawtypes")
	Multimap<Class, Class> roomToJobAssignment = initRoomToJobAssignment();

	public void validateConfiguration() throws InstanceConfigurationException {
		if (ICURoomsRatio + TCRoomsRatio + WardRoomsRatio < 0.999999 || ICURoomsRatio + TCRoomsRatio + WardRoomsRatio > 1.00000001) {
			throw new InstanceConfigurationException("Room-Ratios don't sum up to 1");
		}
		
		if (ICUJobsRatio + OutpatientJobsRatio + WardJobsRatio < 0.999999 || ICUJobsRatio + OutpatientJobsRatio + WardJobsRatio > 1.00000001) {
			throw new InstanceConfigurationException("Job-Ratios don't sum up to 1");
		}
		
		if(totalJobsQuantity > 3000){
			throw new InstanceConfigurationException("More than 3000 Jobs are not supported");
		}
		
		if(totalRoomsQuantity > 3000){
			throw new InstanceConfigurationException("More than 3000 Rooms are not supported");
		}
		
		if(totalRoomsQuantity > 3000){
			throw new InstanceConfigurationException("More than 3000 Rooms are not supported");
		}
		
		for (int i = 0; i < JobMinLengths_MINUTES.length; i++) {
			if(JobMinLengths_MINUTES[i] < minutesPerTimeslot){
				throw new InstanceConfigurationException("Min Length of Jobs is smaller than Timeslot size. This causes problems. Please fix");
			}
		}

		for (int i = 0; i < JobAvgLengths_MINUTES.length; i++) {
			if(JobAvgLengths_MINUTES[i] < minutesPerTimeslot){
				throw new InstanceConfigurationException("Avg Length of Jobs is smaller than Timeslot size. This causes problems. Please fix");
			}
		}

		for (int i = 0; i < JobMaxLengths_MINUTES.length; i++) {
			if(JobMaxLengths_MINUTES[i] < minutesPerTimeslot){
				throw new InstanceConfigurationException("Max Length of Jobs is smaller than Timeslot size. This causes problems. Please fix");
			}
		}
		
		if(avgJobsPerWard < 1.0 || avgJobsPerWard > 4.0 ){
			log.warning("Average Jobs per Ward is "+avgJobsPerWard+". Typically this metric should be between 1 and 4.");
		}
		
		if(avgJobsPerTC < 8.0){
			log.warning("Average Jobs per TC is "+avgJobsPerTC+". Typically this metric should be > 8.");
		}
		
		if(avgJobsPerICU > 1.5){
			log.warning("Average Jobs per ICU is "+avgJobsPerICU+". Typically this metric should be < 1,5.");
		}
		
		if((ICUJobsRatio * totalJobsQuantity) > (ICURoomsRatio * totalRoomsQuantity)){
			log.warning("Not enough ICU-Rooms ("+Math.floor(ICURoomsRatio * totalRoomsQuantity)+") for "+Math.floor(ICUJobsRatio * totalJobsQuantity)+" ICU Jobs");
//			throw new InstanceConfigurationException("Not enough ICU-Rooms ("+Math.floor(ICURoomsRatio * totalRoomsQuantity)+") for "+Math.floor(ICUJobsRatio * totalJobsQuantity)+" ICU Jobs");
		}
	}

	@SuppressWarnings("rawtypes")
	private Multimap<Class, Class> initRoomToJobAssignment() {
		Multimap<Class, Class> map = HashMultimap.create();

		map.put(WardJob.class, Ward.class);
		map.put(WardJob.class, TherapyCenter.class);
		map.put(ICUJob.class, ICU.class);
		map.put(OutpatientJob.class, TherapyCenter.class);

		return map;
	}
	
	
	/**
	 * *********************************
	 * 
	 * IMPORT SETTINGS
	 * 
	 * *********************************
	 */
	
//	private double gamma = 1.0;
	private boolean useQ = false;

    public Integer getQualificationsQuantity() {
        return this.qualificationsQuantity;
    }

    public Integer getTherapistsQuantity() {
        return this.therapistsQuantity;
    }

    public Integer getTotalRoomsQuantity() {
        return this.totalRoomsQuantity;
    }

    public Integer getTotalJobsQuantity() {
        return this.totalJobsQuantity;
    }

    public Integer getICURoomsQuantity() {
        return this.ICURoomsQuantity;
    }

    public Integer getWardRoomsQuantity() {
        return this.WardRoomsQuantity;
    }

    public Integer getTCRoomsQuantity() {
        return this.TCRoomsQuantity;
    }

    public Double getICURoomsRatio() {
        return this.ICURoomsRatio;
    }

    public Double getWardRoomsRatio() {
        return this.WardRoomsRatio;
    }

    public Double getTCRoomsRatio() {
        return this.TCRoomsRatio;
    }

    public Double getCostPerCell() {
        return this.costPerCell;
    }

    public Double getRoutingCostFactor() {
        return this.routingCostFactor;
    }

    public Double getPatientTransportCostFactor() {
        return this.patientTransportCostFactor;
    }

    public Integer getICUJobsQuantity() {
        return this.ICUJobsQuantity;
    }

    public Integer getWardJobsQuantity() {
        return this.WardJobsQuantity;
    }

    public Integer getOutpatientJobsQuantity() {
        return this.OutpatientJobsQuantity;
    }

    public Double getICUJobsRatio() {
        return this.ICUJobsRatio;
    }

    public Double getWardJobsRatio() {
        return this.WardJobsRatio;
    }

    public Double getOutpatientJobsRatio() {
        return this.OutpatientJobsRatio;
    }

    public Double getAvgJobsPerWard() {
        return this.avgJobsPerWard;
    }

    public Double getAvgJobsPerICU() {
        return this.avgJobsPerICU;
    }

    public Double getAvgJobsPerTC() {
        return this.avgJobsPerTC;
    }

    public Integer getMinutesPerTimeslot() {
        return this.minutesPerTimeslot;
    }

    public Integer getMinutesPerPlanningHorizon() {
        return this.minutesPerPlanningHorizon;
    }

    public Integer getNumberOfTimeSlots() {
        return this.numberOfTimeSlots;
    }

    public Integer getFirstBreakLength_MINUTES() {
        return this.firstBreakLength_MINUTES;
    }

    public Integer getFirstBreakFlexibility_MINUTES() {
        return this.firstBreakFlexibility_MINUTES;
    }

    public Integer getSecondBreakLength_MINUTES() {
        return this.secondBreakLength_MINUTES;
    }

    public Integer getSecondBreakFlexibility_MINUTES() {
        return this.secondBreakFlexibility_MINUTES;
    }

    public Integer[] getJobMinLengths_MINUTES() {
        return this.JobMinLengths_MINUTES;
    }

    public Integer[] getJobAvgLengths_MINUTES() {
        return this.JobAvgLengths_MINUTES;
    }

    public Integer[] getJobMaxLengths_MINUTES() {
        return this.JobMaxLengths_MINUTES;
    }

    public Integer getFirstBreakLength() {
        return this.firstBreakLength;
    }

    public Integer getFirstBreakFlexibility() {
        return this.firstBreakFlexibility;
    }

    public Integer getSecondBreakLength() {
        return this.secondBreakLength;
    }

    public Integer getSecondBreakFlexibility() {
        return this.secondBreakFlexibility;
    }

    public Integer[] getJobMinLengths_TS() {
        return this.JobMinLengths_TS;
    }

    public Integer[] getJobAvgLengths_TS() {
        return this.JobAvgLengths_TS;
    }

    public Integer[] getJobMaxLengths_TS() {
        return this.JobMaxLengths_TS;
    }

    public Integer[] getPossibleShiftStartingSlots() {
        return this.possibleShiftStartingSlots;
    }

    public Integer[] getPossibleShiftLengths() {
        return this.possibleShiftLengths;
    }

    public IProbabilityDistribution getQualificationToTherapistAssignment() {
        return this.qualificationToTherapistAssignment;
    }

    public IProbabilityDistribution getQualificationToJobAssignment() {
        return this.qualificationToJobAssignment;
    }

    public IProbabilityDistribution getTimeslotToJobAssignment() {
        return this.timeslotToJobAssignment;
    }

    public IProbabilityDistribution getShiftStartAssignment() {
        return this.shiftStartAssignment;
    }

    public IProbabilityDistribution getShiftDurationAssignment() {
        return this.shiftDurationAssignment;
    }

    public Multimap<Class, Class> getRoomToJobAssignment() {
        return this.roomToJobAssignment;
    }

    public boolean isUseQ() {
        return this.useQ;
    }

    public void setQualificationsQuantity(Integer qualificationsQuantity) {
        this.qualificationsQuantity = qualificationsQuantity;
    }

    public void setTherapistsQuantity(Integer therapistsQuantity) {
        this.therapistsQuantity = therapistsQuantity;
    }

    public void setTotalRoomsQuantity(Integer totalRoomsQuantity) {
        this.totalRoomsQuantity = totalRoomsQuantity;
    }

    public void setTotalJobsQuantity(Integer totalJobsQuantity) {
        this.totalJobsQuantity = totalJobsQuantity;
    }

    public void setICURoomsQuantity(Integer ICURoomsQuantity) {
        this.ICURoomsQuantity = ICURoomsQuantity;
    }

    public void setWardRoomsQuantity(Integer WardRoomsQuantity) {
        this.WardRoomsQuantity = WardRoomsQuantity;
    }

    public void setTCRoomsQuantity(Integer TCRoomsQuantity) {
        this.TCRoomsQuantity = TCRoomsQuantity;
    }

    public void setICURoomsRatio(Double ICURoomsRatio) {
        this.ICURoomsRatio = ICURoomsRatio;
    }

    public void setWardRoomsRatio(Double WardRoomsRatio) {
        this.WardRoomsRatio = WardRoomsRatio;
    }

    public void setTCRoomsRatio(Double TCRoomsRatio) {
        this.TCRoomsRatio = TCRoomsRatio;
    }

    public void setCostPerCell(Double costPerCell) {
        this.costPerCell = costPerCell;
    }

    public void setRoutingCostFactor(Double routingCostFactor) {
        this.routingCostFactor = routingCostFactor;
    }

    public void setPatientTransportCostFactor(Double patientTransportCostFactor) {
        this.patientTransportCostFactor = patientTransportCostFactor;
    }

    public void setICUJobsQuantity(Integer ICUJobsQuantity) {
        this.ICUJobsQuantity = ICUJobsQuantity;
    }

    public void setWardJobsQuantity(Integer WardJobsQuantity) {
        this.WardJobsQuantity = WardJobsQuantity;
    }

    public void setOutpatientJobsQuantity(Integer OutpatientJobsQuantity) {
        this.OutpatientJobsQuantity = OutpatientJobsQuantity;
    }

    public void setICUJobsRatio(Double ICUJobsRatio) {
        this.ICUJobsRatio = ICUJobsRatio;
    }

    public void setWardJobsRatio(Double WardJobsRatio) {
        this.WardJobsRatio = WardJobsRatio;
    }

    public void setOutpatientJobsRatio(Double OutpatientJobsRatio) {
        this.OutpatientJobsRatio = OutpatientJobsRatio;
    }

    public void setAvgJobsPerWard(Double avgJobsPerWard) {
        this.avgJobsPerWard = avgJobsPerWard;
    }

    public void setAvgJobsPerICU(Double avgJobsPerICU) {
        this.avgJobsPerICU = avgJobsPerICU;
    }

    public void setAvgJobsPerTC(Double avgJobsPerTC) {
        this.avgJobsPerTC = avgJobsPerTC;
    }

    public void setMinutesPerTimeslot(Integer minutesPerTimeslot) {
        this.minutesPerTimeslot = minutesPerTimeslot;
    }

    public void setMinutesPerPlanningHorizon(Integer minutesPerPlanningHorizon) {
        this.minutesPerPlanningHorizon = minutesPerPlanningHorizon;
    }

    public void setNumberOfTimeSlots(Integer numberOfTimeSlots) {
        this.numberOfTimeSlots = numberOfTimeSlots;
    }

    public void setFirstBreakLength_MINUTES(Integer firstBreakLength_MINUTES) {
        this.firstBreakLength_MINUTES = firstBreakLength_MINUTES;
    }

    public void setFirstBreakFlexibility_MINUTES(Integer firstBreakFlexibility_MINUTES) {
        this.firstBreakFlexibility_MINUTES = firstBreakFlexibility_MINUTES;
    }

    public void setSecondBreakLength_MINUTES(Integer secondBreakLength_MINUTES) {
        this.secondBreakLength_MINUTES = secondBreakLength_MINUTES;
    }

    public void setSecondBreakFlexibility_MINUTES(Integer secondBreakFlexibility_MINUTES) {
        this.secondBreakFlexibility_MINUTES = secondBreakFlexibility_MINUTES;
    }

    public void setJobMinLengths_MINUTES(Integer[] JobMinLengths_MINUTES) {
        this.JobMinLengths_MINUTES = JobMinLengths_MINUTES;
    }

    public void setJobAvgLengths_MINUTES(Integer[] JobAvgLengths_MINUTES) {
        this.JobAvgLengths_MINUTES = JobAvgLengths_MINUTES;
    }

    public void setJobMaxLengths_MINUTES(Integer[] JobMaxLengths_MINUTES) {
        this.JobMaxLengths_MINUTES = JobMaxLengths_MINUTES;
    }

    public void setFirstBreakLength(Integer firstBreakLength) {
        this.firstBreakLength = firstBreakLength;
    }

    public void setFirstBreakFlexibility(Integer firstBreakFlexibility) {
        this.firstBreakFlexibility = firstBreakFlexibility;
    }

    public void setSecondBreakLength(Integer secondBreakLength) {
        this.secondBreakLength = secondBreakLength;
    }

    public void setSecondBreakFlexibility(Integer secondBreakFlexibility) {
        this.secondBreakFlexibility = secondBreakFlexibility;
    }

    public void setJobMinLengths_TS(Integer[] JobMinLengths_TS) {
        this.JobMinLengths_TS = JobMinLengths_TS;
    }

    public void setJobAvgLengths_TS(Integer[] JobAvgLengths_TS) {
        this.JobAvgLengths_TS = JobAvgLengths_TS;
    }

    public void setJobMaxLengths_TS(Integer[] JobMaxLengths_TS) {
        this.JobMaxLengths_TS = JobMaxLengths_TS;
    }

    public void setPossibleShiftStartingSlots(Integer[] possibleShiftStartingSlots) {
        this.possibleShiftStartingSlots = possibleShiftStartingSlots;
    }

    public void setPossibleShiftLengths(Integer[] possibleShiftLengths) {
        this.possibleShiftLengths = possibleShiftLengths;
    }

    public void setQualificationToTherapistAssignment(IProbabilityDistribution qualificationToTherapistAssignment) {
        this.qualificationToTherapistAssignment = qualificationToTherapistAssignment;
    }

    public void setQualificationToJobAssignment(IProbabilityDistribution qualificationToJobAssignment) {
        this.qualificationToJobAssignment = qualificationToJobAssignment;
    }

    public void setTimeslotToJobAssignment(IProbabilityDistribution timeslotToJobAssignment) {
        this.timeslotToJobAssignment = timeslotToJobAssignment;
    }

    public void setShiftStartAssignment(IProbabilityDistribution shiftStartAssignment) {
        this.shiftStartAssignment = shiftStartAssignment;
    }

    public void setShiftDurationAssignment(IProbabilityDistribution shiftDurationAssignment) {
        this.shiftDurationAssignment = shiftDurationAssignment;
    }

    public void setRoomToJobAssignment(Multimap<Class, Class> roomToJobAssignment) {
        this.roomToJobAssignment = roomToJobAssignment;
    }

    public void setUseQ(boolean useQ) {
        this.useQ = useQ;
    }

    public String toString() {
        return "de.tum.ziller.thesis.thrp.instancegenerator.InstanceConfiguration(qualificationsQuantity=" + this.getQualificationsQuantity() + ", therapistsQuantity=" + this.getTherapistsQuantity() + ", totalRoomsQuantity=" + this.getTotalRoomsQuantity() + ", totalJobsQuantity=" + this.getTotalJobsQuantity() + ", ICURoomsQuantity=" + this.getICURoomsQuantity() + ", WardRoomsQuantity=" + this.getWardRoomsQuantity() + ", TCRoomsQuantity=" + this.getTCRoomsQuantity() + ", ICURoomsRatio=" + this.getICURoomsRatio() + ", WardRoomsRatio=" + this.getWardRoomsRatio() + ", TCRoomsRatio=" + this.getTCRoomsRatio() + ", costPerCell=" + this.getCostPerCell() + ", routingCostFactor=" + this.getRoutingCostFactor() + ", patientTransportCostFactor=" + this.getPatientTransportCostFactor() + ", ICUJobsQuantity=" + this.getICUJobsQuantity() + ", WardJobsQuantity=" + this.getWardJobsQuantity() + ", OutpatientJobsQuantity=" + this.getOutpatientJobsQuantity() + ", ICUJobsRatio=" + this.getICUJobsRatio() + ", WardJobsRatio=" + this.getWardJobsRatio() + ", OutpatientJobsRatio=" + this.getOutpatientJobsRatio() + ", avgJobsPerWard=" + this.getAvgJobsPerWard() + ", avgJobsPerICU=" + this.getAvgJobsPerICU() + ", avgJobsPerTC=" + this.getAvgJobsPerTC() + ", minutesPerTimeslot=" + this.getMinutesPerTimeslot() + ", minutesPerPlanningHorizon=" + this.getMinutesPerPlanningHorizon() + ", numberOfTimeSlots=" + this.getNumberOfTimeSlots() + ", firstBreakLength_MINUTES=" + this.getFirstBreakLength_MINUTES() + ", firstBreakFlexibility_MINUTES=" + this.getFirstBreakFlexibility_MINUTES() + ", secondBreakLength_MINUTES=" + this.getSecondBreakLength_MINUTES() + ", secondBreakFlexibility_MINUTES=" + this.getSecondBreakFlexibility_MINUTES() + ", JobMinLengths_MINUTES=" + java.util.Arrays.deepToString(this.getJobMinLengths_MINUTES()) + ", JobAvgLengths_MINUTES=" + java.util.Arrays.deepToString(this.getJobAvgLengths_MINUTES()) + ", JobMaxLengths_MINUTES=" + java.util.Arrays.deepToString(this.getJobMaxLengths_MINUTES()) + ", firstBreakLength=" + this.getFirstBreakLength() + ", firstBreakFlexibility=" + this.getFirstBreakFlexibility() + ", secondBreakLength=" + this.getSecondBreakLength() + ", secondBreakFlexibility=" + this.getSecondBreakFlexibility() + ", JobMinLengths_TS=" + java.util.Arrays.deepToString(this.getJobMinLengths_TS()) + ", JobAvgLengths_TS=" + java.util.Arrays.deepToString(this.getJobAvgLengths_TS()) + ", JobMaxLengths_TS=" + java.util.Arrays.deepToString(this.getJobMaxLengths_TS()) + ", possibleShiftStartingSlots=" + java.util.Arrays.deepToString(this.getPossibleShiftStartingSlots()) + ", possibleShiftLengths=" + java.util.Arrays.deepToString(this.getPossibleShiftLengths()) + ", qualificationToTherapistAssignment=" + this.getQualificationToTherapistAssignment() + ", qualificationToJobAssignment=" + this.getQualificationToJobAssignment() + ", timeslotToJobAssignment=" + this.getTimeslotToJobAssignment() + ", shiftStartAssignment=" + this.getShiftStartAssignment() + ", shiftDurationAssignment=" + this.getShiftDurationAssignment() + ", roomToJobAssignment=" + this.getRoomToJobAssignment() + ", useQ=" + this.isUseQ() + ")";
    }
}
