package multithreading.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

final class Storage {

	private Integer limit;
    private BlockingQueue<String> stepsCompleted;
    private ConcurrentMap<Integer,BlockingQueue<String>> steps;
    private volatile boolean stop = false;

    public Storage(Integer limit) {
    	this.limit = limit;
    	this.steps = new ConcurrentHashMap<Integer,BlockingQueue<String>>();
        this.stepsCompleted = new LinkedBlockingQueue<String>();
    }

	public synchronized void addStep(String step, Integer id)  throws InterruptedException {
		
        while (isStepsFull(id)) {
            wait();
        }

        BlockingQueue<String> stepsOfClient = steps.get(id);
        if (stepsOfClient == null){
        	stepsOfClient = new LinkedBlockingQueue<String>(limit);
        	steps.put(id, stepsOfClient);
        }
        
        stepsOfClient.put(step);
        notify();		
	}    

    public synchronized String getNextStep(Integer id) throws InterruptedException {

        while(isStepsEmpty(id)) {
            wait();
        }

        BlockingQueue<String> stepsOfClient = steps.get(id);
        if (stepsOfClient == null){
        	return null;
        }
        
        String step = stepsOfClient.poll();
        notify();
        return step;
    }	
    	
	public synchronized void addCompletedStep(String step)  throws InterruptedException {

		stepsCompleted.put(step);
	}
   
	public synchronized String getCompletedStep() throws InterruptedException{
    	
        while(isStepsCompletedEmpty() && !stop) {
            wait();
        }
        
        String message = stepsCompleted.poll();
        notify();
        return message;
    }
	
	public boolean mustStop(){

		return stop;
	}
	
	public void stop(){

		stop = true;
	}
	
    public synchronized boolean isStepsCompletedEmpty() {
		
    	return stepsCompleted.size() == 0;
	}
    
    public synchronized boolean isStepsFull(Integer id) {
    	
    	BlockingQueue<String> stepsOfClient = steps.get(id);
    	
    	if (stepsOfClient == null) { 
    	
    		return false; 
    	}
    	
        return stepsOfClient.size() >= limit;
    }
    
	public synchronized boolean isStepsEmpty(Integer id) {
    	
    	BlockingQueue<String> stepsOfClient = steps.get(id);
    	
    	if (stepsOfClient == null) { 
    	
    		return true; 
    	}
    	
        return stepsOfClient.size() == 0;
    }
}
