package de.tum.ziller.thesis.thrp.common.controller;

import java.util.Comparator;

import de.tum.ziller.thesis.thrp.common.entities.Job;
import de.tum.ziller.thesis.thrp.common.entities.Node;
import de.tum.ziller.thesis.thrp.common.entities.Room;
import de.tum.ziller.thesis.thrp.common.entities.Route;
import de.tum.ziller.thesis.thrp.common.entities.Therapist;
import de.tum.ziller.thesis.thrp.common.entities.Timeslot;

public class Comparators {

	public static Comparator<Therapist> THERAPIST_ID_ASCENDING = new Comparator<Therapist>() {

		@Override
		public int compare(Therapist t1, Therapist t2) {
			if (t1.getId() > t2.getId()) {
				return 1;
			}
			if (t1.getId() < t2.getId()) {
				return -1;
			}
			return 0;
		}
	};
	
	
	public static Comparator<Job> JOB_PRIORITY_SCORE_DESCENDING = new Comparator<Job>() {

		@Override
		public int compare(Job i, Job j) {
			if (i.getSchedulingPriority() < j.getSchedulingPriority()) {
				return 1;
			}
			if (i.getSchedulingPriority() > j.getSchedulingPriority()) {
				return -1;
			}
			return 0;
		}
	};
	
	public static Comparator<Room> ROOM_ID_ASCENDING = new Comparator<Room>() {

		@Override
		public int compare(Room i, Room j) {
			if (i.getId() < j.getId()) {
				return 1;
			}
			if (i.getId() > j.getId()) {
				return -1;
			}
			return 0;
		}
	};

	public static Comparator<Node> NODE_START_ASCENDING = new Comparator<Node>() {

		@Override
		public int compare(Node t1, Node t2) {
			return t1.compareTo(t2);
		}
	};

	public static Comparator<Route> ROUTE_START_ASCENDING = new Comparator<Route>() {

		@Override
		public int compare(Route t1, Route t2) {
			return t1.compareTo(t2);
		}
	};

	public static Comparator<Timeslot> TIMESLOTS_ASCENDING_BY_START = new Comparator<Timeslot>() {

		@Override
		public int compare(Timeslot t1, Timeslot t2) {
			if (t1.getStart() > t2.getStart()) {
				return 1;
			}
			return -1;
		}
	};

	public static Comparator<Integer> INTEGER_DESCENDING = new Comparator<Integer>() {

		@Override
		public int compare(Integer t1, Integer t2) {
			if (t1 > t2) {
				return -1;
			}
			if (t1 < t2) {
				return 1;
			} else
				return 0;
		}
	};
	
	public static Comparator<Node> NODE_START_ASCENDING_ONLY_TIME_CONSIDERED = new Comparator<Node>() {
		@Override
		
		public int compare(Node i, Node j) {
		if (i.getTime().getStart() > j.getTime().getStart()) {
			return 1;
		}
		if (i.getTime().getStart() < j.getTime().getStart()) {
			return -1;
		} else {
			return 0;
		}
		}
	};
	
//	public static Comparator<Node> NODE_START_ASCENDING_FULL = new Comparator<Node>() {
//
//		@Override
//		public int compare(Node n, Node m) {
//			
//			if(n == m){
//				return 0;
//			}
//			
//			if(!(this instanceof TreatmentJob) && !(this instanceof TreatmentJob)){
//				if(this.getFromTo().getStart() == this.getFromTo().getEnd()){
//					return 0;
//				}
//				if (this.getFromTo().getStart() > o.getFromTo().getStart()) {
//					return 1;
//				}
//				return -1;
//			}
//			
//			if(this.getJob().getId() == o.getJob().getId()){
//				return 0;
//			}else{
//				
//				if (this.getFromTo().getStart() > o.getFromTo().getStart()) {
//					return 1;
//				}
//				return -1;
//			}
//			
//			
//		}
//	};
}