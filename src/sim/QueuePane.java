package sim;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.LineBorder;

import kernel.Process;
import structures.IODevice;
import structures.ProcessQueue;
import structures.Schedule;
import structures.State;

import java.awt.Font;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class QueuePane extends JPanel implements Observer{
	private static final long serialVersionUID = -8290089352246145892L;
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

	// This method is sort of inefficient, as it generates the whole list
	// everytime it is changed. This is necessary as processes added
	// to queue in different ways (according to schedule)
	// If I were to do this again, I would use JTables that observe the queues
	public JPanel generateQueueGrid(ProcessQueue queue){
		// set up grid
		JPanel queueGrid = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		gb.columnWidths = new int[]{325, 0};
		gb.rowHeights = new int[] {0};
		gb.columnWeights = new double[]{1.0};
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
	
	public static JPanel generateProcessBox(Process process){
		JPanel processPanel = new JPanel();
		if (process != null) {
			processPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
			
			if (process.isHighPriority())
				processPanel.setBackground(new Color(137, 215, 255));
			else
				processPanel.setBackground(new Color(188, 232, 255));
			
			JLabel lblProcess = new JLabel(String.format("%-7s", "PID " + process.getPID()));
			lblProcess.setFont(new Font("Verdana", Font.BOLD, 13));
			processPanel.add(lblProcess);
			
			JLabel lblSize = new JLabel(String.format("%-9s", process.getSize() + "kb"));
			lblSize.setFont(new Font("Verdana", Font.PLAIN, 13));
			processPanel.add(lblSize);
			
			JProgressBar progressBar = new JProgressBar();
			progressBar.setFont(new Font("Verdana", Font.PLAIN, 11));
			progressBar.setStringPainted(true);
			progressBar.setString(process.getTicksRemaining() + " / " + process.getTotalTicks() + " ticks left");
			progressBar.setValue(process.getPercentageDone());
			progressBar.setForeground(new Color(74, 145, 68));
			processPanel.add(progressBar);

			JLabel lblState = new JLabel(State.getName(process.getState()));
			lblState.setFont(new Font("Verdana", Font.ITALIC, 13));
			processPanel.add(lblState);
			
			if (process.getState() == State.RUNNING){
				JLabel lblResourceTitle = new JLabel(String.format("%25s", "Resources Required: "));
				lblResourceTitle.setFont(new Font("Verdana", Font.BOLD, 13));
				processPanel.add(lblResourceTitle);
				ArrayList<IODevice> resources = process.getResources();
				if (resources.isEmpty()){
					JLabel lblResource = new JLabel("None, bursting to end!");
					lblResource.setFont(new Font("Verdana", Font.PLAIN, 13));
					processPanel.add(lblResource);
				} else {
					for (int i = 0; i < resources.size(); i++){
						IODevice resource = resources.get(i);
						JLabel lblResource;
						if (i == 0)
							lblResource = new JLabel(resource.getName());
						else 
							lblResource = new JLabel(", " + resource.getName());
						
						lblResource.setFont(new Font("Verdana", Font.PLAIN, 13));
						processPanel.add(lblResource);
					}					
				}
			}
		}
		processPanel.setAlignmentY(CENTER_ALIGNMENT);
		processPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		return processPanel;
	}
	
}
