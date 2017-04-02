package structures;

import java.util.Comparator;

import kernel.Process;

public interface Schedule {
	public static final int FCFS = 1; // first-come first serve
	public static final int SJF = 2; // shortest job first
	public static final int PRIORITY = 3; // highest priority first
	public static final int SRT = 4; // shortest remaining time
	
	public class PriorityComparator implements Comparator<Process> {
		@Override
		public int compare(Process p1, Process p2) {
			return -Boolean.compare(p1.isHighPriority(), p2.isHighPriority());
		}
	}
	
	public class SJFComparator implements Comparator<Process> {
		@Override
		public int compare(Process p1, Process p2) {
			return Integer.compare(p1.getTotalTicks(), p2.getTotalTicks());
		}
	}
	
	public class SRTComparator implements Comparator<Process> {
		@Override
		public int compare(Process p1, Process p2) {
			return Integer.compare(p1.getTicksRemaining(), p2.getTicksRemaining());
		}
	}
}
