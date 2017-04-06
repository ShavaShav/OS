package machine;

import structures.ProcessQueue;
import structures.Resource;
import structures.Schedule;

public class DiskDrive extends Resource {
	private static final DiskDrive instance = new DiskDrive();
	public static DiskDrive getInstance(){ return instance; }

	private DiskDrive(){
		super();
		queue = new ProcessQueue(Schedule.FCFS); // first come first serve
		speed = MEDIUM_SPEED;
	}

	@Override
	public String getName() {
		return "DiskDrive";
	}
	
	@Override
	public String toString(){
		return id + ": DiskDrive ("+ queue.size() + " processes in queue)";
	}
}
