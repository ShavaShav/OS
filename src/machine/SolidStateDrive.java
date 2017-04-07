package machine;

import structures.IODevice;

public class SolidStateDrive extends IODevice {
	private static final SolidStateDrive instance = new SolidStateDrive();
	public static SolidStateDrive getInstance(){ return instance; }

	private SolidStateDrive(){
		super();
		speed = FAST_SPEED;
		name = "Solid State Drive";
	}
}
