package sim;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

import kernel.CPUScheduler;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;

public class SimPanel extends JPanel {
	QueuePane panelReadyQueue;
	/**
	 * Create the panel.
	 */
	public SimPanel(CPUScheduler scheduler) {
		System.out.println("Simpanel constructed");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 52, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 39, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		panelReadyQueue = new QueuePane(scheduler.getReadyQueue(), "Ready Queue");
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 1;
		add(panelReadyQueue, gbc_panel);
		
		JLabel lblNewLabel = new JLabel("Running");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 3;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);
		
		QueuePane panel_1 = QueuePane.getTestPane();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 5;
		gbc_panel_1.gridy = 1;
		add(panel_1, gbc_panel_1);

	}
	
	public void setReadyQueueScheduleText(String scheduleName){
		panelReadyQueue.setScheduleText(scheduleName);
	}

	public void addController(ActionListener controller){
		
	}
}
