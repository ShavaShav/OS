package sim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import kernel.Process;

public class ListPane extends JPanel {

	private JScrollPane scrollPane;
	private JLabel lblsched;
	private ArrayList<Process> queue;
	private JPanel queueGrid;
	
	private static int procRow = 0;
	/**
	 * Create the panel.
	 */
	public ListPane(String title) {
		queue = new ArrayList<Process>();
		setLayout(new BorderLayout(0, 0));
		setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		JPanel titlePanel = new JPanel();
		add(titlePanel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel(title);
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 13));
		titlePanel.add(lblNewLabel);
		
		lblsched = new JLabel("FCFS"); // lists are first come first serve
		lblsched.setFont(new Font("Verdana", Font.PLAIN, 11));
		titlePanel.add(lblsched);
		
		// set up grid
		queueGrid = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		gb.columnWidths = new int[]{325, 0};
		gb.rowHeights = new int[] {0};
		gb.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gb.rowWeights = new double[]{0.0};
		queueGrid.setLayout(gb);
		
		scrollPane = new JScrollPane(queueGrid);

		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void setScheduleText(String scheduleName){
		lblsched.setText(scheduleName);
	}

	// very inefficient, try to think of a better way...
	public void addToList(Process process){
		// go through processes, add to grid column wise
		JPanel processPanel = QueuePane.generateProcessBox(process);
		GridBagConstraints gbc_processPanel = new GridBagConstraints();
		gbc_processPanel.anchor = GridBagConstraints.NORTHWEST;
		gbc_processPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_processPanel.gridx = 0;
		gbc_processPanel.gridy = procRow++;
		gbc_processPanel.weighty = 0;
		queueGrid.add(processPanel, gbc_processPanel);
	}

}
