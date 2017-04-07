package structures;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import kernel.Process;

public class ProcessQueue {
	private static int INIT = 11; // initial capacity of the queue (java default)
	// a blocking queue allows us to add and remove simultaneously
	// as internally it contains locks that solve the consumer/producer prob
	private BlockingQueue<Process> queue;
	private int schedule;
	
	public ProcessQueue(int schedule) {
		this.schedule = schedule;
		switch (schedule){
			case Schedule.FCFS:
				// First Come First Serve (regular queue)
				queue = new LinkedBlockingQueue<Process>();
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
	}
	
	public int getSchedule(){
		return schedule;
	}
	
	public boolean hasNext(){
		return queue.peek() != null;
	}
	
	public int size(){
		return queue.size();
	}
	
	// get the next job, according to schedule
	public Process next() throws InterruptedException {
		return queue.take(); // blocks if there is nothing in queue
	}
	
	// add a job, according to schedule
	public void add(Process process) throws InterruptedException {
		queue.put(process);
	}
}
