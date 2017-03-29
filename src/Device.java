
public interface Device {
	public Device getInstance(); // returns the single instance of the device
	public void addJob(Job job); // add job to device's wait queue
	public void setSchedule(ScheduleAlgorithm schedule);
}
