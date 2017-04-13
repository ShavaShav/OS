package sim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import kernel.CPUScheduler;
import kernel.Process;
import machine.CPU;
import machine.Config;
import machine.RAM;
import structures.IODevice;
import structures.Schedule;

public class SimController {
	private SimWindow simWindow;
	private SimPanel simPanel;
	private NewProcessPanel npPanel;
	private SimConfigPanel scPanel;
	private CPUScheduler scheduler;
	
	public SimController(SimWindow simWindow, CPUScheduler scheduler) {
		this.simWindow = simWindow;
		this.scheduler = scheduler;
		npPanel = new NewProcessPanel();
		scPanel = new SimConfigPanel();
		simPanel = new SimPanel(scheduler); // model only needs to be used in simPanel
		
		simWindow.addSimulationConfigPanel(scPanel);
		simWindow.addNewProcessPanel(npPanel);
		simWindow.addSimulationPanel(simPanel);

		npPanel.addController(new AddProcessListener());
		scPanel.addClockSpeedController(new ClockSpeedListener());
		scPanel.addRAMCapacityController(new RAMCapacityListener());
		scPanel.addScheduleController(new ScheduleListener());
	}
	
	// called when add process button is pressed
	private class AddProcessListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Add Process pressed");
			// parse the input and create a process
			int size = npPanel.getProcessSize();
			boolean hiPriority = npPanel.getHighPriority();
			ArrayList<IODevice> resources = npPanel.getResources();
			int totalTicks = npPanel.getTotalTicks();
			Process newProcess = new Process(size, hiPriority, totalTicks, resources);
			try {
				scheduler.scheduleProcess(newProcess);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(simWindow, e1.getMessage(), "Error!", JOptionPane.WARNING_MESSAGE);
			}
			
			if (!scheduler.isRunning()) {
				// start the device threads
				for (IODevice device : Config.RESOURCES)
					new Thread(device).start();
				// start the scheduling algorithm
				scheduler.start();
			}
			simPanel.updateDiagnostics();
		}
	}
	
	// called when new clock speed button is pressed
	private class ClockSpeedListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			CPU.CLOCK_SPEED = scPanel.getClockSpeed();
		}
	}
	
	// called when new ram size button is pressed
	private class RAMCapacityListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			RAM.CAPACITY = scPanel.getRAMCapacity(); // MB to KB conversion
		}
	}
	
	// called when new schedule button is pressed
	private class ScheduleListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			scheduler.setSchedule(scPanel.getSchedule());
			simPanel.setReadyQueueScheduleText(Schedule.getName(scPanel.getSchedule()));
		}
	}
	
}
