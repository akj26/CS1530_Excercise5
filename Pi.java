import java.util.concurrent.atomic.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.*;


public class Pi {

    
    public static AtomicLong threadCount = new AtomicLong(0);//represents the sum of all threads running on the system

    public Thread[] _threads;

    
    public void calculate(long amountOfThreads, long totalAmountofIterations) {

	_threads = new Thread[(int) amountOfThreads];//this will hold all of our threads so that we can perform the necessary calculations

	//this will give us the overall amount of iterations that will need need to perform base on the threads
	
	final long overallAmountofIterations = totalAmountofIterations / amountOfThreads;

	
	for (int j = 0; j < _threads.length; j++) {//now we need to make the threads 

	    _threads[j] = new Thread(() -> {
		    int currentIteration = 0;//represents the iteration that we are on 
		    int localCount = 0;//represents the count
		    while (currentIteration++ < overallAmountofIterations) {
			// with x and y both somewhere between 0.000 and 1.000
			double x = ThreadLocalRandom.current().nextDouble(1);
			double y = ThreadLocalRandom.current().nextDouble(1);

			
			//cacuculates if we ar inside the circle: If x^2 + y^2 < 1, then that point is "inside" the circle
			if ((x * x + y * y) < 1) {
			    localCount++;

			} else {
			   //if we are not in the circle, the directions tell us that we dont have to do anything 

			}
		    }
		    
		    //this will add the thread count to ur local count 
		    threadCount.addAndGet(localCount); 
		    
		});
	}

	
	//this will help us to start the threads 
	
	for (int j = 0; j < _threads.length; j++) {
	    _threads[j].start();
	}

	//we need to joung the threads bc before we continue we want them all to be complete.
	
	for (int j = 0; j < _threads.length; j++) {
	    try {
		_threads[j].join();
	    } catch (Exception ex) { }
	}

	

	//gets the total # of vals that are in the circle 
	
	long circlePoints = threadCount.get();

	//gets the total # of interations 
	long totalIterations = totalAmountofIterations;

	
	//ratio between how many points were inside the circle and the total number of points.
	double ratio = ((double) circlePoints / totalIterations);

	
	//printing the results

	System.out.println("**Here are the Results**"); 	
	
	System.out.println("Total  = " + totalIterations);
	System.out.println("Inside = " + circlePoints);
	System.out.println("Ratio  = " + ratio);

	//This should be multiplied by four (since we were looking at a "quarter" of the circle). This will allow us to calculate an estimate of pi.
	System.out.println("Pi     = " + (ratio * 4));


    }
    
    public static void main(String[] args) {

	//we need to get the amount of threads and the number of iterations from the
    	//cmd line 
	long amountOfThreads = Long.parseLong(args[0]);
	long totalAmountofIterations = Long.parseLong(args[1]);
	
	Pi pi = new Pi();
	
	pi.calculate(amountOfThreads, totalAmountofIterations);
    }
}