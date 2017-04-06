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
	
	public ProcessQueue(int schedule) {
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
	
	public static void main(String[] args){
		Process processA = new Process(25, true, 500, 25000);
		Process processB = new Process(35, false, 700, 15000);
		Process processC = new Process(65, true, 500, 10000);
		Process processD = new Process(50, true, 500, 5000);
		Process processE = new Process(100, false, 550, 100000);
		Process processF = new Process(12, false, 500, 7000);
		Process processG = new Process(19, true, 400, 6000);
		Process processH = new Process(46, false, 50, 500);
		
		ProcessQueue myQueue = new ProcessQueue(Schedule.SJF);
		try {
			myQueue.add(processA);
			myQueue.add(processB);
			myQueue.add(processC);
			myQueue.add(processD);
			myQueue.add(processE);
			myQueue.add(processF);
			myQueue.add(processG);
			myQueue.add(processH);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (myQueue.hasNext()){
			try {
				System.out.println(myQueue.next().fullDetails());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
