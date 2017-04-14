package structures;

import java.util.Observable;
import java.util.Random;

import kernel.Process;

/*
 *  Our resources can only be instantiated once, so subclasses must
 *  all use the Singleton pattern. They will all use the ProcessQueue
 *  class for their waiting queues, utilizing the FCFS schedule.
 *  They can easily use a different schedule but I think FCFS makes sense.
 *  Each resource will take operate at a certain predefined speed.
 *  Each resource operates in a separate thread, so the scheduler 
 *  can communicate with them asynchronously.
 */

public abstract class IODevice extends Observable implements Runnable {
	public static double FAST_SPEED = 2; // seconds it takes to complete request
	public static double MEDIUM_SPEED = 4;
	public static double SLOW_SPEED = 7;
	private static int numResources = 0;
	
	private Random random;
	private boolean isRunning = false;
	
	protected int id;				// id of resource
	protected String name;			// human-readable name of device
	protected double speed;			// time it takes to fulfill request (seconds)
	
	protected ProcessQueue queue;	// processes in line to use resource -> I/O Waiting queue
	
	
	// constructor to be used by subclasses (specific I/o devices)
	protected IODevice(){
		id = numResources++;	// give IODevice the unique ID
		speed = MEDIUM_SPEED; 	// medium second by default
		name = "IODevice";		// default name
		random = new Random();	// random number generator used in getNextCompletedProcess()
		queue = new ProcessQueue(Schedule.FCFS); // first come first serve for ALL I/O devices
	}
	
	// can use this generically to make any device (ended up not using in GUI, but works for console UI)
	public IODevice(String name, double speed){
		this();
		this.name = name;
		this.speed = speed;
	}
	
	public String getName() { return name; }
	public int getId() { return id; }
	public ProcessQueue getQueue(){ return queue; }	
	public void stop(){ isRunning = false; }
	public void reset(){ queue.clear(); }

	@Override
	public String toString() { return id + ": " + getName() + " ("+ queue.size() + " processes in queue)"; }
	
	// returns true if there are processes in queue
	public boolean hasQueuedProcesses() { return queue.hasNext(); }
	
	// number of processes in the queue
	public int numQueuedProcesses() { return queue.size(); }
	
	// add a process to the queue
	public synchronized void addToQueue(Process process) throws InterruptedException{
		queue.add(process);
	}
	
	// gets the next process after it's request has been completed (simulated
	// by sleeping for a random time close to the "speed") 
	private void serveIORequest() throws InterruptedException{
		// select random amount of seconds using gaussian dist
		double time = random.nextGaussian() * (speed/8) + speed;
		time = time > 0.50 ? time : 0.50; // cap the lower bound at half a second
		long randTime = (long) (time * 1000);
		Thread.sleep(randTime);
	}
	
	// runs in a separate thread due to busy waiting (while looping)
	@Override
	public void run() {
		isRunning = true;
		// run until program is aborted
		while (true){
			try {
				// don't try to get process if queue is empty. Even though queue will block,
				// as soon as something is available it will be gotten. Better to wait.
				if (hasQueuedProcesses()){
					
					// simulate work being done to complete IO request for process
					serveIORequest();
					
					// get "completed" process. queue blocks so if we are adding, it will wait.
					Process doneIO = queue.next(); 
					
					// if user has requested to stop the thread
					if (!isRunning)
						return;
					
					// pass the completed process back to the scheduler to put in Ready Queue
					setChanged();
					notifyObservers(doneIO);
				}					
			} catch (InterruptedException e) {
				System.out.println(this.getName() + " is unable to serve request.");
				e.printStackTrace();
			}
		}
	}
}
