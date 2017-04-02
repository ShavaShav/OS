package structures;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import kernel.Process;

public class ProcessQueue {
	private static int INIT = 11; // initial capacity of the queue (java default)
	private Queue<Process> queue;
	
	public ProcessQueue(int schedule) {
		switch (schedule){
			case Schedule.FCFS:
				// First Come First Serve (regular queue)
				queue = new LinkedList<Process>();
				break;
			case Schedule.PRIORITY:
				// Priority Queue
				queue = new PriorityQueue<Process>(INIT, 
							new Schedule.PriorityComparator());
				break;
			case Schedule.SJF:
				// Shortest Total Time First
				queue = new PriorityQueue<Process>(INIT, 
						new Schedule.SJFComparator());
				break;
			case Schedule.SRT:
				// Shortest Remaining Time First
				queue = new PriorityQueue<Process>(INIT, 
						new Schedule.SRTComparator());
				break;
		}
	}
	
	// get the next job, according to schedule
	public Process getNext() {
		return queue.poll();
	}
	
	// add a job, according to schedule
	public void add(Process process) {
		queue.offer(process);
	}
	
}
