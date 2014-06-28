package de.tum.ziller.thesis.thrp.heuristic.helper;

import de.tum.ziller.thesis.thrp.common.entities.Insertion;
import de.tum.ziller.thesis.thrp.common.entities.Solution;
import de.tum.ziller.thesis.thrp.heuristic.strategies.alns.ALNSAbstractOperation;

public class ALNSStrategieVisualizationManager implements IStrategyVisualizer {
	
	public final IStrategyVisualizer CONSOLE = new ConsolePrintVisualizer();
	public final IStrategyVisualizer GUI = new GUIVisualizer();
	public final IStrategyVisualizer NONE = new DoNothingVisualizer();
	
	private IStrategyVisualizer ON_JOB_PLANNED = CONSOLE;
	
	public void setOnJobPlanned(IStrategyVisualizer iv){
		ON_JOB_PLANNED = iv;
	}

	@Override
	public void onJobPlanned(ALNSAbstractOperation a,Insertion i, Solution s) {
		ON_JOB_PLANNED.onJobPlanned(a, i, s);
		
	}
	
	public void all(IStrategyVisualizer iv){
		ON_JOB_PLANNED = iv;
	}
	
	public void disableAll(){
		ON_JOB_PLANNED = NONE;
	}
	
	

	

}