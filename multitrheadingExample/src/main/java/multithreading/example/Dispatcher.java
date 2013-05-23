package multithreading.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;


public class Dispatcher implements Runnable {

	
	private final String stopTimeStr = "2013-05-22 18:57:00";
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
    private Storage storage;
    private ConcurrentMap<Integer,BlockingQueue<String>> actions;
    
    private Set<Integer> dispatchedClients = Collections.synchronizedSet(new LinkedHashSet<Integer>());

    public Dispatcher(Storage storage, ConcurrentMap<Integer,BlockingQueue<String>> actions) {
        this.storage = storage;
        this.actions = actions;
    }

    public void run() {
    	
    	if ((actions == null) || (actions.size() == 0) || (storage == null)){
    		return;
    	}
        
    	while (dispatchedClients.size() < actions.size()){
    		for (Integer key:actions.keySet()){
        	
	            try {
	            	String action = actions.get(key).poll();
	            	if (action == null){
	            		dispatchedClients.add(key);
	            	}else{	
	            		
	            		if ((!storage.mustStop()) && ( isAfterTheStopTime(action))){
	            			storage.stop();
	            		}
	            		
	            		if (!storage.mustStop()) {
	            			storage.addStep(action, key);
	            		}
	            		
	            	}
	
	            } catch (InterruptedException e) {
	
	                return;
	            }
    		}
    	}
    	storage.stop();
    	try {
			storage.addCompletedStep("exiting dispatcher ");
		} catch (InterruptedException e) {
		}    	
    }

	private boolean isAfterTheStopTime(String action) {
		
		try {
			
			Date stopTime = sdf.parse(stopTimeStr);
			Date actionTime = sdf.parse(action);
			
			return actionTime.after(stopTime);
		} catch (ParseException e) {
			return true;
		}
		
	}
}
