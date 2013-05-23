package multithreading.example;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class StepsManager {

    public StepsManager(
            Storage storage,
            Map<Integer, Set<String>> steps, int maxStorageItemsPerClient) throws InterruptedException {
    	
    	ExecutorService executor = Executors.newFixedThreadPool(steps.size()+2);
    	
    	executor.execute(new Dispatcher(storage, concurrent(steps)));        
        for (Integer id:steps.keySet()){

    		executor.execute(new Client(storage, id));
        }

        executor.execute(new Monitor(storage));
        executor.shutdown();        
    }

    private ConcurrentMap<Integer, BlockingQueue<String>> concurrent(Map<Integer, Set<String>> steps) {
    	
    	ConcurrentMap<Integer, BlockingQueue<String>> stepsResult = new ConcurrentHashMap<Integer, BlockingQueue<String>>();
    	
    	for (Integer key:steps.keySet()){
    		stepsResult.put(key, new LinkedBlockingQueue<String>(steps.get(key)));
    	}
    	
    	return stepsResult;
	}
}

