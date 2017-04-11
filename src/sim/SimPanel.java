package sim;

import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import kernel.CPUScheduler;
import kernel.Process;
import machine.CPU;
import machine.Config;
import machine.RAM;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JProgressBar;
import java.awt.Font;
import java.awt.CardLayout;
import javax.swing.border.LineBorder;

public class SimPanel extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1876860546817802467L;
	QueuePane panelReadyQueue;
	JPanel panelRunning, panelRunningProcessBox;
	JPanel panelCPULight;
	private ListPane panelTerminated;
	private JProgressBar progressRAM;
	private JPanel panelIOQueue1;
	private JPanel panelIOQueue2;
	private JPanel panelIOQueue4;
	private JPanel panelIOQueue3;
	private Timer cpuTimer;
	
	private int PANE_HEIGHT = 400;
	/**
	 * Create the panel.
	 */
	public SimPanel(CPUScheduler scheduler) {
		CPU.getInstance().getProcessObservable().addObserver(new RunningProcessObserver());
		scheduler.addObserver(this);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 35, 19, 12, 34, 0, 0, 28, 15, 20, 0, 0, 45, 16, 83, 0};
		gridBagLayout.rowHeights = new int[]{39, 22, 131, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		panelReadyQueue = new QueuePane(scheduler.getReadyQueue(), "Ready Queue");
		panelReadyQueue.setPreferredSize(new Dimension(0, PANE_HEIGHT));
		GridBagConstraints gbc_panelReadyQueue = new GridBagConstraints();
		gbc_panelReadyQueue.gridwidth = 4;
		gbc_panelReadyQueue.insets = new Insets(0, 0, 5, 5);
		gbc_panelReadyQueue.fill = GridBagConstraints.BOTH;
		gbc_panelReadyQueue.gridx = 1;
		gbc_panelReadyQueue.gridy = 0;
		add(panelReadyQueue, gbc_panelReadyQueue);
		
		JPanel panelRunningDiagnostics = new JPanel();
		GridBagConstraints gbc_panelRunningDiagnostics = new GridBagConstraints();
		gbc_panelRunningDiagnostics.gridwidth = 9;
		gbc_panelRunningDiagnostics.insets = new Insets(0, 0, 5, 5);
		gbc_panelRunningDiagnostics.fill = GridBagConstraints.BOTH;
		gbc_panelRunningDiagnostics.gridx = 6;
		gbc_panelRunningDiagnostics.gridy = 0;
		add(panelRunningDiagnostics, gbc_panelRunningDiagnostics);
		GridBagLayout gbl_panelRunningDiagnostics = new GridBagLayout();
		gbl_panelRunningDiagnostics.columnWidths = new int[]{105, 0};
		gbl_panelRunningDiagnostics.rowHeights = new int[]{0, 0, 0, 0, 0, 33, 0};
		gbl_panelRunningDiagnostics.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelRunningDiagnostics.rowWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		panelRunningDiagnostics.setLayout(gbl_panelRunningDiagnostics);
		
		JLabel lblRunning = new JLabel("Running");
		GridBagConstraints gbc_lblRunning = new GridBagConstraints();
		gbc_lblRunning.insets = new Insets(0, 0, 5, 0);
		gbc_lblRunning.gridx = 0;
		gbc_lblRunning.gridy = 1;
		panelRunningDiagnostics.add(lblRunning, gbc_lblRunning);
		lblRunning.setFont(new Font("Verdana", Font.BOLD, 15));
		
		panelRunning = new JPanel();
		GridBagConstraints gbc_panelRunning = new GridBagConstraints();
		gbc_panelRunning.insets = new Insets(0, 0, 5, 0);
		gbc_panelRunning.fill = GridBagConstraints.BOTH;
		gbc_panelRunning.gridx = 0;
		gbc_panelRunning.gridy = 2;
		panelRunningDiagnostics.add(panelRunning, gbc_panelRunning);
		panelRunning.setLayout(new CardLayout(0, 0));
		panelRunning.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		
		panelRunningProcessBox = new JPanel();
		panelRunning.add(panelRunningProcessBox);
		panelRunningProcessBox.setPreferredSize(new Dimension(170,50));
		
		JPanel panelDiagnostics = new JPanel();
		GridBagConstraints gbc_panelDiagnostics = new GridBagConstraints();
		gbc_panelDiagnostics.insets = new Insets(0, 0, 5, 0);
		gbc_panelDiagnostics.fill = GridBagConstraints.BOTH;
		gbc_panelDiagnostics.gridx = 0;
		gbc_panelDiagnostics.gridy = 4;
		panelRunningDiagnostics.add(panelDiagnostics, gbc_panelDiagnostics);
		GridBagLayout gbl_panelDiagnostics = new GridBagLayout();
		gbl_panelDiagnostics.columnWidths = new int[]{31, 85, 19, 0};
		gbl_panelDiagnostics.rowHeights = new int[]{0, 34, 0, 0, 0, 0};
		gbl_panelDiagnostics.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panelDiagnostics.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panelDiagnostics.setLayout(gbl_panelDiagnostics);
		
		JLabel lblCpu = new JLabel("CPU Activity");
		lblCpu.setFont(new Font("Verdana", Font.BOLD, 13));
		GridBagConstraints gbc_lblCpu = new GridBagConstraints();
		gbc_lblCpu.gridwidth = 3;
		gbc_lblCpu.insets = new Insets(0, 0, 5, 0);
		gbc_lblCpu.gridx = 0;
		gbc_lblCpu.gridy = 0;
		panelDiagnostics.add(lblCpu, gbc_lblCpu);
		
		panelCPULight = new JPanel();
		panelCPULight.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		panelCPULight.setBackground(new Color(204, 153, 153));
		GridBagConstraints gbc_panelCPULight = new GridBagConstraints();
		gbc_panelCPULight.insets = new Insets(0, 0, 5, 5);
		gbc_panelCPULight.fill = GridBagConstraints.BOTH;
		gbc_panelCPULight.gridx = 1;
		gbc_panelCPULight.gridy = 1;
		panelDiagnostics.add(panelCPULight, gbc_panelCPULight);
		
		JLabel lblNewLabel = new JLabel("RAM Usage");
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 13));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 3;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		panelDiagnostics.add(lblNewLabel, gbc_lblNewLabel);
		
		progressRAM = new JProgressBar(0, 100);
		progressRAM.setStringPainted(true);
		progressRAM.setString("0 / " + RAM.CAPACITY / 1000 + " MB");
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.insets = new Insets(0, 0, 5, 0);
		gbc_progressBar.gridwidth = 3;
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 3;
		panelDiagnostics.add(progressRAM, gbc_progressBar);
		
		panelTerminated = new ListPane("Terminated");
		panelTerminated.setPreferredSize(new Dimension(0, PANE_HEIGHT));
		GridBagConstraints gbc_panelTerminated = new GridBagConstraints();
		gbc_panelTerminated.insets = new Insets(0, 0, 5, 0);
		gbc_panelTerminated.fill = GridBagConstraints.BOTH;
		gbc_panelTerminated.gridx = 16;
		gbc_panelTerminated.gridy = 0;
		add(panelTerminated, gbc_panelTerminated);
		
		panelIOQueue1 = new QueuePane(Config.RESOURCES[2].getQueue(), Config.RESOURCES[2].getName());
		panelIOQueue1.setPreferredSize(new Dimension(0, PANE_HEIGHT));
		GridBagConstraints gbc_panelIOQueue1 = new GridBagConstraints();
		gbc_panelIOQueue1.gridwidth = 4;
		gbc_panelIOQueue1.insets = new Insets(0, 0, 0, 5);
		gbc_panelIOQueue1.fill = GridBagConstraints.BOTH;
		gbc_panelIOQueue1.gridx = 1;
		gbc_panelIOQueue1.gridy = 2;
		add(panelIOQueue1, gbc_panelIOQueue1);
		
		panelIOQueue2 = new QueuePane(Config.RESOURCES[3].getQueue(), Config.RESOURCES[3].getName());
		panelIOQueue2.setPreferredSize(new Dimension(0, PANE_HEIGHT));
		GridBagConstraints gbc_panelIOQueue2 = new GridBagConstraints();
		gbc_panelIOQueue2.gridwidth = 4;
		gbc_panelIOQueue2.insets = new Insets(0, 0, 0, 5);
		gbc_panelIOQueue2.fill = GridBagConstraints.BOTH;
		gbc_panelIOQueue2.gridx = 6;
		gbc_panelIOQueue2.gridy = 2;
		add(panelIOQueue2, gbc_panelIOQueue2);
		
		panelIOQueue3 = new QueuePane(Config.RESOURCES[0].getQueue(), Config.RESOURCES[0].getName());
		panelIOQueue3.setPreferredSize(new Dimension(0, PANE_HEIGHT));
		GridBagConstraints gbc_panelIOQueue3 = new GridBagConstraints();
		gbc_panelIOQueue3.gridwidth = 4;
		gbc_panelIOQueue3.insets = new Insets(0, 0, 0, 5);
		gbc_panelIOQueue3.fill = GridBagConstraints.BOTH;
		gbc_panelIOQueue3.gridx = 11;
		gbc_panelIOQueue3.gridy = 2;
		add(panelIOQueue3, gbc_panelIOQueue3);
		
		panelIOQueue4 = new QueuePane(Config.RESOURCES[1].getQueue(), Config.RESOURCES[1].getName());
		panelIOQueue4.setPreferredSize(new Dimension(0, PANE_HEIGHT));
		GridBagConstraints gbc_panelIOQueue4 = new GridBagConstraints();
		gbc_panelIOQueue4.fill = GridBagConstraints.BOTH;
		gbc_panelIOQueue4.gridx = 16;
		gbc_panelIOQueue4.gridy = 2;
		add(panelIOQueue4, gbc_panelIOQueue4);
	}
	
	public void setReadyQueueScheduleText(String scheduleName){
		panelReadyQueue.setScheduleText(scheduleName);
	}
	
	public class RunningProcessObserver implements Observer{
		@Override
		public void update(Observable o, Object arg) {
			
//			// CPU has received interrupt, show process change in Running section
			Process process = CPU.getInstance().getCurrentProcess();
			synchronized (this) {
				if (process != null){
						panelRunning.remove(panelRunningProcessBox);
						panelRunningProcessBox = QueuePane.generateProcessBox(process);
						panelRunningProcessBox.setPreferredSize(new Dimension(170,50));
						panelRunning.add(panelRunningProcessBox, BorderLayout.CENTER);
						panelRunning.revalidate();
						panelRunning.repaint();
						
						cpuTimer = new Timer();
						cpuTimer.schedule(new UpdateCPUWorkBar(process), 0, CPU.CLOCK_SPEED/2);
						
						panelCPULight.setBackground(new Color(0, 255, 0));

				} else {
	
						cpuTimer.cancel();
						panelRunning.remove(panelRunningProcessBox);
						panelCPULight.setBackground(new Color(204, 153, 153));

				}
			}
		}		
	}

	// called by scheduler on process termination
	@Override
	public void update(Observable obs, Object o) {
		if (o instanceof Process){
			Process process = (Process) o;
			panelTerminated.addToList(process);
			updateDiagnostics();
		}
	}
	
	public void updateDiagnostics(){
		progressRAM.setValue((int) RAM.getPercentUsed());
		progressRAM.setString(RAM.getCurrentUsage()/100 + " / " + RAM.CAPACITY/100 + " MB");
	}
	
	private class UpdateCPUWorkBar extends TimerTask {
		private Process process;
		public UpdateCPUWorkBar(Process process){
			this.process = process;
		}
	    public void run() {
	    	// update the progress bar
			panelRunning.remove(panelRunningProcessBox);
			panelRunningProcessBox = QueuePane.generateProcessBox(process);
			panelRunningProcessBox.setPreferredSize(new Dimension(170,50));
			panelRunning.add(panelRunningProcessBox, BorderLayout.CENTER);
			panelRunning.revalidate();
			panelRunning.repaint();
	    }
	  }
}
