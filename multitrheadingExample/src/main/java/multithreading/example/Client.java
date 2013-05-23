package multithreading.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

class Client implements Runnable {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
	private Storage storage;
    private final Integer id;
    private String previousStep;

    public Client(Storage storage, Integer id) {
        this.storage = storage;
        this.id = id;
    }

    public void run() {
    	while(!storage.mustStop()) {
            try {
            	String step = storage.getNextStep(id);
            	
                if ((StringUtils.isEmpty(previousStep)) && (step != null)){
                	storage.addCompletedStep(id + ": starting the procedure");
                }else{
                	storage.addCompletedStep(id + " completed the step " + step);
                }
                
                if ((previousStep != null) && (step != null)){
                	long timeslot = timeslotBetween(previousStep, step);

                }
            	
                previousStep = step;

            } catch (InterruptedException e) {
                return;
            }
        }
    	
    	try {
			storage.addCompletedStep("exiting client " + id);
		} catch (InterruptedException e) {
		}
    }

	private long timeslotBetween(String first, String second){
		
		Date timeOfFirst;
		try {
			timeOfFirst = sdf.parse(first);
			Date timeOfSecond = sdf.parse(second);
			return Math.abs(timeOfFirst.getTime() - timeOfSecond.getTime());
		} catch (ParseException e) {
			return 0;
		}
		
	}
}
