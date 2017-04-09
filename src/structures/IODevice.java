package structures;

import java.util.Observable;
import java.util.Random;

import kernel.Process;

// resources can only be used by one process at a time, so subclasses
// will all follow the Singleton pattern. They will also run in separate
// threads so the scheduler can communicate with them asynchronously.
// Each resource will take operate at a certain speed. The use of Java's
// BlockingQueue interface allows for race-free addition and removal from waiting queues
public abstract class IODevice extends Observable implements Runnable {
	public static double FAST_SPEED = 3000; // ticks it takes to complete request
	public static double MEDIUM_SPEED = 5000;
	public static double SLOW_SPEED = 7000;
	private static int numResources = 0;
	private Random random;
	private boolean isRunning = false;
	protected ProcessQueue queue;	// processes in line to use resource -> I/O Waiting queue
	
	protected int id;				// id of resource
	protected String name;			// human-readable name of device
	protected double speed;			// time it takes to fulfill request (seconds)
	
	// constructor to be used by subclasses (specific I/o devices)
	protected IODevice(){
		id = numResources++;	// give every IODevice the next ID
		speed = MEDIUM_SPEED; 	// medium second by default
		name = "IODevice";		// default name
		random = new Random();
		queue = new ProcessQueue(Schedule.FCFS); // first come first serve for ALL I/O devices
	}
	
	// can use this generically to make any device
	public IODevice(String name, double speed){
		this();
		this.name = name;
		this.speed = speed;
	}
	
	public String getName() { return name; }
	public int getId() { return id; }
	
	// returns true if there are processes in queue
	public boolean hasQueuedProcesses() { return queue.hasNext(); }
	
	// number of processes in the queue
	public int numQueuedProcesses() { return queue.size(); }
	
	// add a process to the queue
	public void addToQueue(Process process) throws InterruptedException{
		queue.add(process);
	}
	
	// gets the next process after it's request has been completed (simulated
	// by sleeping) - The queue blocks if empty
	public Process getNextCompletedProcess() throws InterruptedException{
		long randTime = (long) (getRandomTime(speed));
		Thread.sleep((long) speed);
		return queue.next();
	}
	
	// gets the next random time close to the speed, used by
	private double getRandomTime(double speed) {
		double time = random.nextGaussian() * (speed/8) + speed;
		return time > 250 ? time : 250;
	}
	
	// runs in a separate thread due to busy waits
	@Override
	public void run() {
		isRunning = true;
		// run until program is aborted
		while (true){
			try {
				// get the next process (might take a while)
				Process doneIO = getNextCompletedProcess();
				if (!isRunning)
					return;
				// pass the completed process back to the scheduler
				setChanged();
				notifyObservers(doneIO);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public ProcessQueue getQueue(){
		return queue;
	}
	
	@Override
	public String toString() {
		return id + ": " + getName() + " ("+ queue.size() + " processes in queue)";
	}
	
	public void reset(){
		queue.clear();
	}
}
