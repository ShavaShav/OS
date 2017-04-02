package structures;

// I/O devices have buffers/schedules, and they are considered a resource
public abstract class IODevice extends Resource {
	public abstract IODevice getInstance(); // returns the single instance of the device
	
}
