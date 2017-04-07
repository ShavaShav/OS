package machine;

import structures.IODevice;

public class HardDiskDrive extends IODevice {
	private static final HardDiskDrive instance = new HardDiskDrive();
	public static HardDiskDrive getInstance(){ return instance; }

	private HardDiskDrive(){
		super();
		speed = MEDIUM_SPEED;
		name = "Hard Disk Drive";
	}
}
