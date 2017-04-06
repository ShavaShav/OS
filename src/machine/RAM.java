package machine;

import kernel.Process;

public class RAM {

	public static final int CAPACITY = 4194304; // 4GB
	private static int currentUsage = 0; 		// amount of memory in use
	
	public static void loadProcess(Process process) {
		currentUsage += process.getSize();
	}
	
	public static void unloadProcess(Process process) {
		currentUsage -= process.getSize();
	}
	
	public static int getCurrentUsage(){
		return currentUsage;
	}
	
	public static double getPercentUsed(){
		return (currentUsage/CAPACITY)*100;
	}
}
