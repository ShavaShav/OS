package machine;

import java.util.Observable;

import kernel.Process;

public class RAM extends Observable {

	public static int CAPACITY = 500000; // 500MB
	private static int currentUsage = 0; 		// amount of memory in use
	
	public static void loadProcess(Process process) throws Exception {
		if ((CAPACITY - currentUsage) < process.getSize())
			throw new Exception("RAM: Not enough space available.");
		
		currentUsage += process.getSize();	
	}
	
	public static void unloadProcess(Process process) {
		currentUsage -= process.getSize();
	}
	
	public static int getCurrentUsage(){
		return currentUsage;
	}
	
	public static double getPercentUsed(){
		return (currentUsage/(double)CAPACITY)*100.00;
	}
}
