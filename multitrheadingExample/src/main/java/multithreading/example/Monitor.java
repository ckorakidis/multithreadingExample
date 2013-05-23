package multithreading.example;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;

class Monitor implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(Monitor.class);
	
	private Storage storage;

    public Monitor(Storage storage) {

    	this.storage = storage;
    }

    public void run() {
        
    	while (!(storage.mustStop() && storage.isStepsCompletedEmpty())){
	         try{
	        	 
	        	String step = storage.getCompletedStep();
	        	if (step != null){
	        		logger.info(step);
	        	}
	        	
	         } catch (InterruptedException e) {
//	        	 System.out.println(e.getMessage());
	        	e.printStackTrace();
	            return;
	        }            

        }
    	ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
    	// Check for logback implementation of slf4j
    	if (loggerFactory instanceof LoggerContext) {
    	    LoggerContext context = (LoggerContext) loggerFactory;
    	    context.stop();
    	}    	
    	System.out.println("exiting monitor");
    }

}
