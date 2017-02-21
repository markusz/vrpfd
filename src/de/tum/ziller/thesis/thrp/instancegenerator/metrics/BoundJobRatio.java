package de.tum.ziller.thesis.thrp.instancegenerator.metrics;

import de.tum.ziller.thesis.thrp.common.entities.Instance;
import de.tum.ziller.thesis.thrp.common.entities.Job;
import lombok.SneakyThrows;

public class BoundJobRatio implements IMetric {

	@Override
	public String getAbbreviation() {
		// TODO Auto-generated method stub
		return "BJR";
	}

	@Override
	@SneakyThrows
	public Double compute(Instance i) {
		
		Double cnt = 0.;
		
		for(Job j : i.getJobs()){
			if(i.getProficientTherapists(j).size() == 1){
				cnt++;
			}
		}
		// TODO Auto-generated method stub
		return cnt / new Double(i.getJobs().size());
	}

}
