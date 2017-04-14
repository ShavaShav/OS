package sim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import kernel.Process;

// a more efficient process pane then queuepane, can only add to it though (used for terminated processes)
public class ListPane extends JPanel {
	private static final long serialVersionUID = 3648497321370539853L;
	private JScrollPane scrollPane;
	private JLabel lblsched;
	private JPanel queueGrid;

	/**
	 * Create the panel.
	 */
	public ListPane(String title) {
		setLayout(new BorderLayout(0, 0));
		setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		JPanel titlePanel = new JPanel();
		add(titlePanel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel(title);
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 13));
		titlePanel.add(lblNewLabel);
		
		lblsched = new JLabel("List");
		lblsched.setFont(new Font("Verdana", Font.PLAIN, 11));
		titlePanel.add(lblsched);
		
		// set up grid
		queueGrid = new JPanel();
		queueGrid.setLayout(new BoxLayout(queueGrid, BoxLayout.Y_AXIS));
		
		scrollPane = new JScrollPane(queueGrid);

		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void setScheduleText(String scheduleName){
		lblsched.setText(scheduleName);
	}

	public void addToList(Process process){
		// add to grid column wise
		JPanel processPanel = QueuePane.generateProcessBox(process);
		processPanel.setMaximumSize( new Dimension(Integer.MAX_VALUE, processPanel.getPreferredSize().height));
		queueGrid.add(processPanel);
	}

}
