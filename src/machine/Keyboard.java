package machine;

import structures.ProcessQueue;
import structures.Resource;
import structures.Schedule;

public class Keyboard extends Resource {
	// STDIN should only have one instance available
	private static final Keyboard instance = new Keyboard();
	public static Keyboard getInstance(){ return instance; }

	private Keyboard(){
		super();
		queue = new ProcessQueue(Schedule.FCFS); // first come first serve
		speed = SLOW_SPEED; // user input is considered the slowest input
	}
	
	@Override
	public String getName() {
		return "Keyboard";
	}
	
	@Override
	public String toString(){
		return id + ": Keyboard ("+ queue.size() + " processes in queue)";
	}
}
