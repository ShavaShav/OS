package sim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import kernel.CPUScheduler;
import structures.Schedule;
import kernel.Process;
import machine.CPU;
import machine.RAM;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class SimConfigPanel extends JPanel {
	private static final long serialVersionUID = -6035621362856493608L;
	private JTextField textFieldSpeed;
	private JTextField textFieldRamSize;
	private JComboBox<String> comboBoxSchedule;
	private JButton btnSetSchedule, btnSetClockSpeed, btnSetRam;
	
	/**
	 * Create the panel.
	 */
	public SimConfigPanel() {
		super();
		setOpaque(true);
		setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		setBackground(new Color(0, 255, 204));
			
		GridBagLayout gbl_panelSimulation = new GridBagLayout();
		gbl_panelSimulation.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panelSimulation.rowHeights = new int[]{28, 22, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelSimulation.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelSimulation.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl_panelSimulation);
		
		JLabel label_4 = new JLabel("");
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.insets = new Insets(0, 0, 5, 5);
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 0;
		add(label_4, gbc_label_4);
		
		JPanel panelSimulationTitle = new JPanel();
		panelSimulationTitle.setOpaque(false);
		GridBagConstraints gbc_panelSimulationTitle = new GridBagConstraints();
		gbc_panelSimulationTitle.fill = GridBagConstraints.BOTH;
		gbc_panelSimulationTitle.insets = new Insets(0, 0, 5, 5);
		gbc_panelSimulationTitle.gridx = 1;
		gbc_panelSimulationTitle.gridy = 0;
		add(panelSimulationTitle, gbc_panelSimulationTitle);
		
		JLabel lblSettings = new JLabel("Simulation Settings");
		lblSettings.setFont(new Font("Verdana", Font.BOLD, 13));
		panelSimulationTitle.add(lblSettings);
		
		JLabel label_5 = new JLabel("");
		GridBagConstraints gbc_label_5 = new GridBagConstraints();
		gbc_label_5.insets = new Insets(0, 0, 5, 0);
		gbc_label_5.gridx = 2;
		gbc_label_5.gridy = 0;
		add(label_5, gbc_label_5);
		
		JPanel panelSpeed = new JPanel();
		panelSpeed.setOpaque(false);
		GridBagConstraints gbc_panelSpeed = new GridBagConstraints();
		gbc_panelSpeed.fill = GridBagConstraints.BOTH;
		gbc_panelSpeed.insets = new Insets(0, 0, 5, 5);
		gbc_panelSpeed.gridx = 1;
		gbc_panelSpeed.gridy = 1;
		add(panelSpeed, gbc_panelSpeed);
		panelSpeed.setLayout(new BorderLayout(0, 0));
		
		JLabel lblCpuSpeed = new JLabel("CPU Speed");
		panelSpeed.add(lblCpuSpeed, BorderLayout.NORTH);
		
		JPanel panelticksperSec = new JPanel();
		panelticksperSec.setOpaque(false);
		panelSpeed.add(panelticksperSec, BorderLayout.WEST);
		panelticksperSec.setLayout(new BorderLayout(0, 0));
		
		textFieldSpeed = new JTextField();
		panelticksperSec.add(textFieldSpeed, BorderLayout.WEST);
		textFieldSpeed.setText(String.valueOf(CPU.CLOCK_SPEED));
		textFieldSpeed.setColumns(10);
		
		JLabel lblTickssec = new JLabel(" ticks/sec");
		panelticksperSec.add(lblTickssec, BorderLayout.EAST);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panelSpeed.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		btnSetClockSpeed = new JButton("Set Clock Speed");
		GridBagConstraints gbc_btnSetClockSpeed = new GridBagConstraints();
		gbc_btnSetClockSpeed.insets = new Insets(0, 0, 5, 5);
		gbc_btnSetClockSpeed.gridx = 1;
		gbc_btnSetClockSpeed.gridy = 2;
		add(btnSetClockSpeed, gbc_btnSetClockSpeed);
		
		JPanel panelRamSize = new JPanel();
		panelRamSize.setOpaque(false);
		GridBagConstraints gbc_panelRamSize = new GridBagConstraints();
		gbc_panelRamSize.fill = GridBagConstraints.BOTH;
		gbc_panelRamSize.insets = new Insets(0, 0, 5, 5);
		gbc_panelRamSize.gridx = 1;
		gbc_panelRamSize.gridy = 3;
		add(panelRamSize, gbc_panelRamSize);
		panelRamSize.setLayout(new BorderLayout(0, 0));
		
		JLabel lblRamSize = new JLabel("RAM Size");
		panelRamSize.add(lblRamSize, BorderLayout.NORTH);
		
		JPanel panelRamGB = new JPanel();
		panelRamGB.setOpaque(false);
		panelRamSize.add(panelRamGB, BorderLayout.WEST);
		panelRamGB.setLayout(new BorderLayout(0, 0));
		
		textFieldRamSize = new JTextField();
		textFieldRamSize.setText(String.valueOf(RAM.CAPACITY/1000));
		panelRamGB.add(textFieldRamSize, BorderLayout.WEST);
		textFieldRamSize.setColumns(10);
		
		JLabel lblMb = new JLabel(" MB");
		panelRamGB.add(lblMb, BorderLayout.EAST);
		
		btnSetRam = new JButton("Set RAM Size");
		GridBagConstraints gbc_btnSetRam = new GridBagConstraints();
		gbc_btnSetRam.insets = new Insets(0, 0, 5, 5);
		gbc_btnSetRam.gridx = 1;
		gbc_btnSetRam.gridy = 4;
		add(btnSetRam, gbc_btnSetRam);
		
		JPanel panelSchedule_1 = new JPanel();
		panelSchedule_1.setOpaque(false);
		GridBagConstraints gbc_panelSchedule_1 = new GridBagConstraints();
		gbc_panelSchedule_1.insets = new Insets(0, 0, 5, 5);
		gbc_panelSchedule_1.fill = GridBagConstraints.BOTH;
		gbc_panelSchedule_1.gridx = 1;
		gbc_panelSchedule_1.gridy = 5;
		add(panelSchedule_1, gbc_panelSchedule_1);
		panelSchedule_1.setLayout(new BorderLayout(0, 0));
		
		String[] scheduleStrings = new String[Schedule.NUM_SCHEDULES];
		for (int i = 0; i < Schedule.NUM_SCHEDULES; i++)
			scheduleStrings[i] = Schedule.getName(i);
		
		JLabel lblSchedule = new JLabel("Schedule");
		panelSchedule_1.add(lblSchedule, BorderLayout.NORTH);
		comboBoxSchedule = new JComboBox(scheduleStrings);
		panelSchedule_1.add(comboBoxSchedule, BorderLayout.SOUTH);
		
		// initialize a new scheduler
		btnSetSchedule = new JButton("Set Schedule");
		GridBagConstraints gbc_btnSetSchedule = new GridBagConstraints();
		gbc_btnSetSchedule.insets = new Insets(0, 0, 5, 5);
		gbc_btnSetSchedule.gridx = 1;
		gbc_btnSetSchedule.gridy = 6;
		add(btnSetSchedule, gbc_btnSetSchedule);
		
		JLabel label_2 = new JLabel("");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.insets = new Insets(0, 0, 0, 5);
		gbc_label_2.gridx = 1;
		gbc_label_2.gridy = 7;
		add(label_2, gbc_label_2);

	}
	
	public void setFieldsEditable(boolean bool){
		textFieldSpeed.setEditable(bool);
		textFieldRamSize.setEditable(bool);
		comboBoxSchedule.setEditable(bool);
		btnSetSchedule.setEnabled(bool);
	}
	
	public void addClockSpeedController(ActionListener controller){
		btnSetClockSpeed.addActionListener(controller);
	}
	
	public void addScheduleController(ActionListener controller){
		btnSetSchedule.addActionListener(controller);
	}
	
	public void addRAMCapacityController(ActionListener controller){
		btnSetRam.addActionListener(controller);
	}
	
	public int getClockSpeed() { return Integer.parseInt(textFieldSpeed.getText()); }
	public int getRAMCapacity() { return Integer.parseInt(textFieldRamSize.getText()) * 1000; } // MB to KB conversion; }
	public int getSchedule() { return comboBoxSchedule.getSelectedIndex(); }

	public void addController(ActionListener controller){
		btnSetSchedule.addActionListener(controller);
	}
}
