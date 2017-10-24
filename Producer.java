import java.util.concurrent.BlockingQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.concurrent.ArrayBlockingQueue;

public class Producer implements Runnable {
	private ArrayBlockingQueue<Event> my_q = null;
	private String file_name;
	private int events_no;
	private int id;

	Producer(ArrayBlockingQueue<Event> buffer, String file_name, int events_no, int id) {
		this.my_q      = buffer;			//we receive from main function the instatiated QUEUE 
		this.file_name = file_name;			//and also the name of the current processing file, which is like a PRODUCER ID
		this.events_no = events_no;			//number of events in each file; how many task a producer introduces in queue
		this.id 	   = id;				//thread id
	}

	
	public void run() {		
	 	
	    try {
	 
	        // Here BufferedInputStream is added for fast reading.
	        BufferedReader fis = new BufferedReader(new FileReader(file_name));
	 
	      	
		    for(int i = 0; i < events_no; i++ ){ 				
		 
				//variables to split the string lines in tokens
				String phrase = fis.readLine();				// this statement reads the line from the file 
				String delims = ",";
				String[] tokens = phrase.split(delims);

				int time       = Integer.parseInt(tokens[0]);
				Event ev = new Event( tokens[1], Integer.parseInt(tokens[2]));

				try {
					Thread.sleep(time);  					//wait the period of time before submiting the task in the queue               
					my_q.put(ev);							//we add the event in the queue 
				} catch(InterruptedException ex) {	
					Thread.currentThread().interrupt();
				}
			
		    }
	 	
	        fis.close();  
	 
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
		
	}

	

}
