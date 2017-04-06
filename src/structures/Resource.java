package structures;

import java.util.Random;

import kernel.Process;

// resources can only be used by one process at a time, so subclasses
// will all follow the Singleton pattern. They will also run in separate
// threads so the scheduler can communicate with them asynchronously.
// Each resource will take operate at a certain speed.
public abstract class Resource implements Runnable {
	protected static double FAST_SPEED = 1; // seconds it takes to complete request
	protected static double MEDIUM_SPEED = 2;
	protected static double SLOW_SPEED = 7;
	private static int numResources = 0;
	private Random random;
	
	protected ProcessQueue queue;	// processes in line to use resource
	protected int id;				// id of resource
	protected double speed;			// time it takes to fulfill request (seconds)
	
	public Resource(){
		id = numResources++;
		speed = MEDIUM_SPEED; 	// medium second by default
		random = new Random();
	}
	
	public abstract String getName();
	public int getId(){ return id; }
	
	// returns true if there are processes in queue
	public boolean hasQueuedProcesses(){ return queue.hasNext(); }
	
	// number of processes in the queue
	public int numQueuedProcesses() { return queue.size(); }
	
	// add a process to the queue
	public void addToQueue(Process process) throws InterruptedException{
		System.out.println("adding");
		queue.add(process);
	}
	
	// gets the next process after it's request has been completed (simulated
	// by sleeping) - The queue blocks if empty
	public Process getNextCompletedProcess() throws InterruptedException{
		Thread.sleep((long) (getRandomTime(speed) * 1000));
		return queue.next();
	}
	
	// gets the next random time close to the speed, used by
	private double getRandomTime(double speed){
		double time = random.nextGaussian() * (speed/8) + speed;
		return time > 0.00 ? time : 0.00;
	}
	
	// runs in a separate thread due to busy waits
	@Override
	public void run() {
		// run until program is aborted
		while (true){
			try {
				// get the next process (might take a while)
				Process doneIO = getNextCompletedProcess();
				System.out.println(this + " -> Finished " + doneIO + "\n");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
