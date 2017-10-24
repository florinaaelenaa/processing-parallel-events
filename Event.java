public class Event{
	
	String name;
	int N;
	
	Event(){}									//constructor wihtout parameters

	Event( String event, int N){		        //constructor with parameters			
		this.name = event;						//tipe of event PRIME. SQUARE, FIB, FACT
		this.N     = N;							//the input for the event functions
	}


}