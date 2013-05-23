package multithreading.example;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Main {

	public static void main(String[] args) throws InterruptedException {		
		
		Map<Integer, Set<String>> steps = createStepsFor(2);
		
        Storage storage = new Storage(10);
        new StepsManager(storage, steps, 2);        
    }

	private static Map<Integer, Set<String>> createStepsFor(int clientsNumber) {
		
		Map<Integer, Set<String>> steps = new HashMap<Integer, Set<String>>();
		
		for (int i=0;i<clientsNumber; i++){
			
			Set<String> clientSteps = new LinkedHashSet<String>();

			clientSteps.add("2013-05-22 18:56:31");
			clientSteps.add("2013-05-22 18:56:35");
			clientSteps.add("2013-05-22 18:56:38");
			clientSteps.add("2013-05-22 18:56:40");
			
			
			clientSteps.add("2013-05-22 18:56:41");
			clientSteps.add("2013-05-22 18:56:45");
			clientSteps.add("2013-05-22 18:56:48");
			clientSteps.add("2013-05-22 18:56:50");
			
			
			clientSteps.add("2013-05-22 18:56:51");
			clientSteps.add("2013-05-22 18:56:55");
			clientSteps.add("2013-05-22 18:56:58");
			clientSteps.add("2013-05-22 18:57:00");
			clientSteps.add("2013-05-22 18:57:30");
			
			if (i%2 == 1){
				clientSteps.add("2013-05-22 18:57:33");
			}
			
			steps.put(i, clientSteps);
		}
		
		return steps;
	}

}
