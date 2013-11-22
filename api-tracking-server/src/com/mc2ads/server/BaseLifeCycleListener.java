package com.mc2ads.server;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.component.LifeCycle.Listener;

import com.mc2ads.server.autotasks.AutoTasksScheduler;

public class BaseLifeCycleListener implements Listener{
	
	static Logger logger = Logger.getLogger(BaseLifeCycleListener.class);
	
	
	public BaseLifeCycleListener(){		
	}

	@Override
	public void lifeCycleStopping(LifeCycle arg0) {
	}
	
	@Override
	public void lifeCycleStopped(LifeCycle arg0) {
		
	}
	
	@Override
	public void lifeCycleStarting(LifeCycle arg0) {				
		logger.debug("Master Node starting, loading init config ...");
	}
	
	@Override
	public void lifeCycleStarted(LifeCycle arg0) {				
		logger.debug("Master Node Started.");
		int c = new AutoTasksScheduler().startingAutoTasks();
		logger.info("Started: " + c + " AutoTasks");		
	}
	
	@Override
	public void lifeCycleFailure(LifeCycle arg0, Throwable arg1) {
	}

}
