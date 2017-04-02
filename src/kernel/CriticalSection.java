package kernel;

/*
 * The instructions state that we are to take a non-preemptive
 * kernel approach to simplify process synchronization.
 * 
 * The critical section problem can be solved simply by 
 * preventing a process from being interrupted while in it's 
 * critical section, and preemptive kernels do not interrupt
 * processes once they are running, so these problems don't 
 * happen in preemptive kernels.
 * 
 * Therefore, this code will not be used in the simulation of
 * process synchronization, but is included here to fulfill
 * the requirements of the project and to show our understanding
 * of the problem.
 */


// Simulating a critical section (shared variable) between two processes
public class CriticalSection implements Runnable{
	public static final int PROCESS_A = 0;
	public static final int PROCESS_B = 1;
	
	// Using Peterson's solution to the critical section problem
	private static boolean flag[] = {false, false};
	private static volatile int turn;
	private static int sharedResource = 0;
	
	private int csId; 			// process number 0 or 1
	private long timeInsideCS;	// amount of time spent inside of critical section
	
	// construct a process with a critical section
	public CriticalSection(int csId){ this.csId = csId; }
	
	public void setSecondsInCS(long time){ timeInsideCS = time; }
	
	// enter critical section in seperate thread, as busy wait will hold up entire virtual machine
	@Override
	public void run() {
		// for presentation, keep this while loop. It will cause process to keep trying to enter after it has exited
		while(true){ 
			int otherProcess = (csId + 1) % 2;
			
			flag[csId] = true; // process wants to enter
			turn = otherProcess; // give priority to other process
			
			boolean printout = true; // will be set to false within busy wait so that "waiting" message only appears once
			
			// attempt to enter the critical section (busy wait) 
			while (flag[otherProcess] && turn == otherProcess){
				if (printout) {
					System.out.println("Process"+(csId == PROCESS_A ? "A":"B")+" is waiting to enter critical section...");
					printout = false;
				}
			};
			
			// simulate work being done in the critical section by going to sleep, and then changing shared variable
			try {
				System.out.println("Process"+(csId == PROCESS_A ? "A":"B")+" is working inside critical section...");
				Thread.sleep(timeInsideCS * 1000);
				sharedResource++;
			} catch (InterruptedException e) {
				
			}

			System.out.println("Process"+(csId == PROCESS_A ? "A":"B")+ " setting shared variable: " + sharedResource);
			
			flag[csId] = false; // leave critical section
		}

		
	}
	
	// Testing the algorithm. It is necessary to use threads because each will be busy waiting.
	public static void main(String args[]){
		Thread processA = new Thread(new CriticalSection(0));
		Thread processB = new Thread(new CriticalSection(1));

		processA.start();
		processB.start();

	}
}
