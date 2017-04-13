package machine;

import java.util.ArrayList;

import structures.IODevice;

public class Config {
	public static final boolean DEBUG = true;
	public static int CPU_UPDATE_SPEED = 500; // half a second
	
	
	public static final IODevice[] RESOURCES = new IODevice[]{
			HardDiskDrive.getInstance(),
			SolidStateDrive.getInstance(),
			Keyboard.getInstance(),
			Monitor.getInstance()
	};
	
	// gets a list of random resources available to system
	// used in process construction to generate their unique resource requirements
	public static ArrayList<IODevice> getRandomResources(){
		ArrayList<IODevice> resourceList = new ArrayList<IODevice>();
		
		if (Math.random() > 0.5)
			resourceList.add(RESOURCES[0]); // HDD medium-probability
		if (Math.random() > 0.5)
			resourceList.add(RESOURCES[1]); // SSD - medium probability
		if (Math.random() > 0.75)
			resourceList.add(RESOURCES[2]); // KEYBOARD - low probability
		if (Math.random() > 0.1)
			resourceList.add(RESOURCES[3]); // MONITOR - high probability
		
		return resourceList;
	}
	
}
