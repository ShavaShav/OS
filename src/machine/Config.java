package machine;

import java.util.ArrayList;

import structures.IODevice;

public class Config {
	public static IODevice[] resources = new IODevice[]{
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
			resourceList.add(resources[0]); // HDD medium-probability
		if (Math.random() > 0.75)
			resourceList.add(resources[1]); // SSD - low probability
		if (Math.random() > 0.25)
			resourceList.add(resources[2]); // KEYBOARD - medium-high probability
		if (Math.random() > 0.1)
			resourceList.add(resources[3]); // MONITOR - high probability
		
		return resourceList;
	}
}
