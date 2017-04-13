package structures;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import kernel.Process;

/*
 *  ProcessQueue is our implementation of a waiting queue of processes.
 *  It takes a schedule and organizes it's queue according to this.
 *  The use of Java's BlockingQueue interface allows for race-free addition 
 *  and removal to and from the queues. They use locks internally to do this.
 *  It is used for any process queue (e.g. ready queue, wait queues, etc.)
 */

public class ProcessQueue extends Observable {
	private static int INIT = 11; // initial capacity of the queue (java default)
	// a blocking queue allows us to add and remove simultaneously
	// as internally it contains locks that solve the consumer/producer prob
	private BlockingQueue<Process> queue;
	private int schedule;
	
	public ProcessQueue(int schedule) {
		this.schedule = schedule;
		queue = getNewQueue(schedule);
	}
	
	// set to use a certain schedule
	public void setSchedule(int schedule){
		this.schedule = schedule;
		BlockingQueue<Process> newQueue = getNewQueue(schedule);
		newQueue.addAll(queue); // add from queue to new queue according to schedule
		queue = newQueue;	// replace with new queue
		setChanged();
		notifyObservers(Schedule.getName(schedule));
	}
	
	// gets the current schedule
	public int getSchedule(){ return schedule; }
	
	/*
	 * Queue methods
	 */
	
	// get the next job, according to schedule
	public Process next() throws InterruptedException {
		Process nextProcess = queue.take(); // blocks if there is nothing in queue
		setChanged();
		notifyObservers(); // notify view of change
		return nextProcess;
	}
	
	// add a job, according to schedule
	public void add(Process process) throws InterruptedException {
		queue.put(process);
		setChanged();
		notifyObservers();
	}
	
	public boolean hasNext(){ return queue.peek() != null; }	
	
	public int size(){ return queue.size(); }
	
	public boolean isEmpty(){ return queue.isEmpty(); }
	
	public void clear(){
		queue.clear();
		setChanged();
		notifyObservers();
	}
	
	// generates a Blocking Queue that sorts it's process' according to the schedule
	private BlockingQueue<Process> getNewQueue(int schedule){
		BlockingQueue<Process> queue = null;
		switch (schedule){
		case Schedule.FCFS:
			// First Come First Serve (regular queue)
			queue = new ArrayBlockingQueue<Process>(INIT);
			break;
		case Schedule.PRIORITY:
			// Priority Queue
			queue = new PriorityBlockingQueue<Process>(INIT, 
					new Schedule.PriorityComparator());
			break;
		case Schedule.SJF:
			// Shortest Total Time First
			queue = new PriorityBlockingQueue<Process>(INIT, 
					new Schedule.SJFComparator());
			break;
		case Schedule.SRT:
			// Shortest Remaining Time First
			queue = new PriorityBlockingQueue<Process>(INIT, 
					new Schedule.SRTComparator());
			break;
		}
		return queue;
	}
	
	// Iterator can be used to look through the list without destroying it
	// by making a copy as an array
	public Iterator<Process> iterator(){  return new ProcessIterator(); }

	private class ProcessIterator implements Iterator<Process>{
		Process[] processList;
		int current = 0;
		
		public ProcessIterator(){
			processList = new Process[queue.size()];
			queue.toArray(processList);
			if (schedule != Schedule.FCFS)
				Arrays.sort(processList, Schedule.getComparator(schedule));	// priority queue conversion need to be resorted			
	
			current = 0;
		}
		
		@Override
		public boolean hasNext() { return current < processList.length; }

		@Override
		public Process next() { return processList[current++]; }
		
		@SuppressWarnings("unused")
		public void reset() { current = 0; }
	}
}
