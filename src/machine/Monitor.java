package machine;

import structures.ProcessQueue;
import structures.Resource;
import structures.Schedule;

public class Monitor extends Resource {
	// STDOUT should only have one instance available
	private static final Monitor instance = new Monitor();
	public static Monitor getInstance(){ return instance; }

	private Monitor(){
		super();
		queue = new ProcessQueue(Schedule.FCFS); // first come first serve
		speed = FAST_SPEED; // we are taking monitor output to be the fastest i/o device
	}
	
	@Override
	public String getName() {
		return "Monitor";
	}
	
	@Override
	public String toString(){
		return id + ": Monitor ("+ queue.size() + " processes in queue)";
	}
}
