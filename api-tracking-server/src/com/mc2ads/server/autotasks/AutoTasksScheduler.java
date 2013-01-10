package com.mc2ads.server.autotasks;

import java.util.Collection;
import java.util.Timer;



public class AutoTasksScheduler {
	
	private static Collection<AutoTaskConfigs> autoTaskConfigs;
	
	public AutoTasksScheduler() {
		if(autoTaskConfigs == null){
			autoTaskConfigs = AutoTaskConfigs.loadFromFile("/auto-tasks-configs.json");		
			System.out.println("AutoTasksScheduler started with "+autoTaskConfigs.size() + " tasks in queue");
		}
	}
	
	public int startingAutoTasks(){
		int c = 0;
		Timer timer = new Timer();
		for (AutoTaskConfigs config : autoTaskConfigs) {
			if(config.getDelay() >= 0){
				try {
					System.out.println("#process autoTaskConfig:"+config);
					Class clazz = Class.forName(config.getClasspath());
					AutoTasks autoTasks = (AutoTasks) clazz.newInstance();	
					
					if(config.getPeriod() == 0){
						timer.schedule(autoTasks, config.getDelay());
					} else {
						timer.scheduleAtFixedRate(autoTasks, config.getDelay(), config.getPeriod());	
					}
					c++;				
				}  catch (Exception e) {				
					e.printStackTrace();
				}
			}
		}
		return c;
	}
}
