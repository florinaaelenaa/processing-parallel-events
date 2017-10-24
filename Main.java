import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ArrayBlockingQueue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

import java.util.concurrent.TimeUnit;

import java.util.*;


public class Main {
	
	public static Vector<Integer> fact_results   = new Vector<>();
	public static Vector<Integer> fib_results    = new Vector<>();
	public static Vector<Integer> square_results = new Vector<>();
	public static Vector<Integer> prime_results  = new Vector<>();
	public static  int total_events;

	public static boolean forever = true;

	public static void main(String[] args) {
		
		int files_no   = args.length - 2; 				//number of files which is equal with PRODUCERS NUMBER 
														//I extract 2 because the first 2 args are the queue dimension and the events number
		int queue_dim  = Integer.parseInt(args[0]);		//first argument, represented by index 0, is the queue dimension
		int events_no  = Integer.parseInt(args[1]);		//the next arguments is the number of events in each file
		String[] files = new String[files_no]; 			//an array of string to remember the files' names
		
		for(int i = 2; i < args.length; i++)
			files[i - 2] = args[i];

		total_events = events_no * files_no;			//the total number of events in each file

		ExecutorService s = Executors.newFixedThreadPool(4);			//variable for pool thread

		ArrayBlockingQueue<Event> event_queue = new ArrayBlockingQueue<Event>(queue_dim);	//I create a queue for events and I'll send it to the constructor of Producer and Consumer	

		Thread producer_threads[] = new Thread[files_no];
		
		for (int i = 0; i < files_no; i++)
			producer_threads[i] = new Thread(new Producer(event_queue, files[i], events_no, i));
		for (int i = 0; i < files_no; i++)
			producer_threads[i].start();
	
		do{
			if(total_events == 0 && event_queue.isEmpty()) {
				s.shutdown();								//Initiates an orderly shutdown in which previously submitted 
				
				try {
					s.awaitTermination(1, TimeUnit.SECONDS); //wait to finish all tasks
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			
				break;		   	 							//tasks are executed, but no new tasks will be accepted.
			}
			else{

				Event el = new Event();
				try {
					el = event_queue.take();
					total_events--;
					s.submit(new Consumer(el));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
		}while(forever);
	

		for (int i = 0; i < files_no; i++) {
			try {
				producer_threads[i].join(); 			//wait for this thread to die
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try{
		    PrintWriter writer_fib    = new PrintWriter("FIB.out", "UTF-8");
		    PrintWriter writer_fact   = new PrintWriter("FACT.out", "UTF-8");
		    PrintWriter writer_prime  = new PrintWriter("PRIME.out", "UTF-8");
		    PrintWriter writer_square = new PrintWriter("SQUARE.out", "UTF-8");
		    
		    Collections.sort(fib_results);					//sorting the elements in vectors
		    Collections.sort(fact_results);
		    Collections.sort(prime_results);
		    Collections.sort(square_results);

		    for(int i = 0; i < fib_results.size(); i++)
		    	writer_fib.println(fib_results.get(i));		//writing the elements in specific files
		  	
		  	for(int i = 0; i < fact_results.size(); i++)
		    	writer_fact.println(fact_results.get(i));
		    
		    for(int i = 0; i < prime_results.size(); i++)
		    	writer_prime.println(prime_results.get(i));
		    
		    for(int i = 0; i < square_results.size(); i++)
		    	writer_square.println(square_results.get(i));

		    writer_fib.close();
		    writer_fact.close();
		    writer_square.close();
		    writer_prime.close();

		} catch (IOException e) {
		   // do something
		}
		
			
	}

}
