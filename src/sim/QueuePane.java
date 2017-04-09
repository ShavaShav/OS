package sim;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

import kernel.Process;
import machine.Config;
import machine.Monitor;
import structures.ProcessQueue;
import structures.Schedule;

import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class QueuePane extends JPanel implements Observer{
	private volatile JScrollPane scrollPane;
	private JLabel lblsched;
	/**
	 * Create the panel.
	 */
	public QueuePane(ProcessQueue queue, String title) {
		queue.addObserver(this);
		setLayout(new BorderLayout(0, 0));
		setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		
		JPanel titlePanel = new JPanel();
		add(titlePanel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel(title);
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 13));
		titlePanel.add(lblNewLabel);
		
		lblsched = new JLabel(Schedule.getName(queue.getSchedule()));
		lblsched.setFont(new Font("Verdana", Font.PLAIN, 11));
		titlePanel.add(lblsched);
		
		JPanel queueGrid = generateQueueGrid(queue);
		
		scrollPane = new JScrollPane(queueGrid);

		add(scrollPane, BorderLayout.CENTER);
	}
	
	public static QueuePane getTestPane(){
		QueuePane qPane = new QueuePane(ProcessQueue.getTestQueue(), "Test Queue");
		return qPane;
	}

	// when queue changes (add or remove) update the pane
	@Override
	public void update(Observable obs, Object o) {
		if (obs instanceof ProcessQueue){
			synchronized(this){ // synching solves producer/consumer prob here (dont let gui remove process from queue while adding it)
				ProcessQueue queue = (ProcessQueue) obs;
				JPanel queueGrid = generateQueueGrid(queue);
				scrollPane.setViewportView(queueGrid);				
			}
		}
	}
	
	public void setScheduleText(String scheduleName){
		lblsched.setText(scheduleName);
	}

	// very inefficient, try to think of a better way...
	public JPanel generateQueueGrid(ProcessQueue queue){
		// set up grid
		JPanel queueGrid = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		gb.columnWidths = new int[]{325, 0};
		gb.rowHeights = new int[] {0};
		gb.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gb.rowWeights = new double[]{0.0};
		queueGrid.setLayout(gb);

		// go through processes, add to grid column wise
		Iterator<Process> it = queue.iterator();
		int procRow = 0;
		while (it.hasNext()){
			JPanel processPanel = generateProcessBox(it.next());
			GridBagConstraints gbc_processPanel = new GridBagConstraints();
			gbc_processPanel.anchor = GridBagConstraints.NORTHWEST;
			gbc_processPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_processPanel.gridx = 0;
			gbc_processPanel.gridy = procRow++;
			gbc_processPanel.weighty = 0;
			queueGrid.add(processPanel, gbc_processPanel);
		}
		
		// padding panel
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = procRow;
		gbc_panel.weighty = 1;

		queueGrid.add(panel, gbc_panel);
		
		return queueGrid;	
	}
	
	public static synchronized JPanel generateProcessBox(Process process){
		JPanel processPanel = new JPanel();
		if (process != null) {
			processPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
			
			if (process.isHighPriority())
				processPanel.setBackground(new Color(153, 255, 255));
			
			JLabel lblProcess = new JLabel(process.toString());
			lblProcess.setFont(new Font("Verdana", Font.PLAIN, 13));
			processPanel.add(lblProcess);
			
			JProgressBar progressBar = new JProgressBar();
			progressBar.setFont(new Font("Verdana", Font.PLAIN, 11));
			progressBar.setStringPainted(true);
			progressBar.setString(process.getTicksRemaining() + " / " + process.getTotalTicks() + " ticks");
			progressBar.setValue(process.getPercentageDone());
			processPanel.add(progressBar);
		}
		processPanel.setAlignmentY(CENTER_ALIGNMENT);
		return processPanel;
	}
	
}
