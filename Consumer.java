import java.util.concurrent.ArrayBlockingQueue;
import java.util.Collections;
import java.util.*;

public class Consumer implements Runnable {
	Event ev;
	
	Consumer( Event ev) {
		this.ev = ev;
		
	} 


	protected int factorial(int x){
		int facti = 1, factj = 1, i = 1;
		while(true){
			if(facti <= x && factj > x)
				return i-1;

			facti = facti * i;
			factj = facti * (i+1);
			i++;
		}

	}
	
	protected int square(int x){
		int i = 1;
		
		while(i*i <= x){
			i++;
		}
		return (i - 1);
	}
	
	protected int prime(int x){
		int flag = 0;
		
		do{
			for(int i=2; i <= Math.sqrt(x); ++i){
		        if(x % i == 0){
		            flag = 1;
		            break;
		        }
		    }
			if(flag == 1){
				x--; 
				flag = 0;
			}
			else
				break;
		}while(true);
		
		return x;
	}
	
	protected int fibonacci(int x){
	   int first = 0, second = 1, next, index = 0; 
	 
		do{
		   next = first + second;
		   first = second;
		   second = next;
		   index ++;
		      
		}while(second <= x);
		
		return index;
	}

	public void run() {
		
		if(ev.name.equals("FACT")){						//compare to see the type of event,calculate the result and writes it in the specific vector in main
			Main.fact_results.add(factorial(ev.N));
		}
		else if(ev.name.equals("FIB")){
			Main.fib_results.add(fibonacci(ev.N));
		}
		else if(ev.name.equals("SQUARE")){
			Main.square_results.add(square(ev.N));
		}
		else if(ev.name.equals("PRIME")){
			Main.prime_results.add(prime(ev.N));
		}
	}
}
