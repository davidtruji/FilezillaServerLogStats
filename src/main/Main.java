package main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import logStats.Task;
import model.LogLine;
import model.UserStats;
import util.Utils;

public class Main {

	private JFrame frmFilezillaLogStats;
	private JTextField textField;
	private String logDirectoryPath;
	private JTextArea textArea;
	private JPanel panel_directory;
	private JComboBox<?> comboBox_user;
	private JButton btnSelect;
	private JButton btnApply;
	private List<LogLine> allLogLines;
	private List<String> allUserNames;
	private List<LogLine> filteredLogLines;
	private JTextField tfDateFrom;
	private JTextField tfDateTo;

	private String showDirectoryChooser() {

		String path = null;
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(null);
		chooser.setDialogTitle("Log directory selection");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(frmFilezillaLogStats) == JFileChooser.APPROVE_OPTION) {
			path = chooser.getSelectedFile().getAbsolutePath();
		}

		return path;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadLogLines() {
		textField.setText(logDirectoryPath);
		textArea.setText("Loading plase wait...");
		allLogLines = Task.loadAllLogLines(logDirectoryPath);
		allUserNames = Task.loadUserNames(allLogLines);
		allUserNames.add("All");
		List<UserStats> userStatsList = Task.generateUserStats(allLogLines);

		textArea.setText("");

		for (UserStats us : userStatsList)
			textArea.append(us.toString() + "\n\n");

		DefaultComboBoxModel model = new DefaultComboBoxModel((String[]) allUserNames.toArray(new String[0]));
		comboBox_user.setModel(model);
		comboBox_user.setSelectedItem("All");

		comboBox_user.setEnabled(true);
		tfDateFrom.setEnabled(true);
		tfDateTo.setEnabled(true);
		btnApply.setEnabled(true);
	}

	private void filterResults() {
		Date firstConnection = null, lastConnection = null;
		filteredLogLines = allLogLines;

		if (!comboBox_user.getSelectedItem().equals("All"))
			filteredLogLines = Task.filterLogLinesByUser(allLogLines, (String) comboBox_user.getSelectedItem());

		try {
			if (!tfDateFrom.getText().isEmpty())
				firstConnection = Utils.dateFormat2.parse(tfDateFrom.getText());
		} catch (ParseException e) {
			e.printStackTrace();

		}
		try {
			if (!tfDateTo.getText().isEmpty())
				lastConnection = Utils.dateFormat2.parse(tfDateTo.getText());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		filteredLogLines = Task.filterLogLinesByDate(filteredLogLines, firstConnection, lastConnection);

		List<UserStats> userStatsList = Task.generateUserStats(filteredLogLines);

		textArea.setText("");

		for (UserStats us : userStatsList)
			textArea.append(us.toString() + "\n\n");

	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFilezillaLogStats = new JFrame();
		frmFilezillaLogStats.setResizable(false);
		frmFilezillaLogStats.setTitle("Filezilla Server log stats - v1.0");
		frmFilezillaLogStats.setBounds(100, 100, 660, 370);
		frmFilezillaLogStats.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFilezillaLogStats.getContentPane().setLayout(null);

		panel_directory = new JPanel();
		panel_directory.setBounds(10, 10, 638, 60);
		panel_directory.setBorder(BorderFactory.createTitledBorder("Log directory"));
		frmFilezillaLogStats.getContentPane().add(panel_directory);
		panel_directory.setLayout(null);

		textField = new JTextField();
		textField.setText("...");
		textField.setToolTipText("");
		textField.setEditable(false);
		textField.setBounds(117, 22, 509, 25);
		panel_directory.add(textField);
		textField.setColumns(10);

		btnSelect = new JButton("Select...");
		btnSelect.setBounds(12, 22, 93, 25);
		btnSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logDirectoryPath = showDirectoryChooser();

				if (logDirectoryPath != null) {
					loadLogLines();
				}

			}
		});
		panel_directory.add(btnSelect);

		JPanel panel_output = new JPanel();
		panel_output.setBorder(new TitledBorder(null, "Output", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_output.setBounds(10, 82, 430, 246);
		frmFilezillaLogStats.getContentPane().add(panel_output);
		panel_output.setLayout(null);

		textArea = new JTextArea();
		textArea.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(12, 20, 406, 214);
		panel_output.add(scrollPane);

		JPanel panel_filters = new JPanel();
		panel_filters.setBorder(new TitledBorder(null, "Filters", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_filters.setBounds(452, 82, 196, 246);
		frmFilezillaLogStats.getContentPane().add(panel_filters);
		panel_filters.setLayout(null);

		JPanel panel_date = new JPanel();
		panel_date.setBorder(new TitledBorder(null, "Date", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_date.setBounds(12, 104, 172, 72);
		panel_filters.add(panel_date);
		panel_date.setLayout(null);

		JLabel lblFrom = new JLabel("from");
		lblFrom.setBounds(32, 24, 32, 15);
		panel_date.add(lblFrom);

		tfDateFrom = new JTextField();
		tfDateFrom.setEnabled(false);
		tfDateFrom.setBounds(68, 22, 92, 19);
		tfDateFrom.setText(Utils.dateFormat2.format(new Date()));
		panel_date.add(tfDateFrom);
		tfDateFrom.setColumns(8);

		JLabel lblTo = new JLabel("to");
		lblTo.setBounds(49, 48, 15, 15);
		panel_date.add(lblTo);

		tfDateTo = new JTextField();
		tfDateTo.setEnabled(false);
		tfDateTo.setBounds(68, 46, 92, 19);
		tfDateTo.setText(Utils.dateFormat2.format(new Date()));
		panel_date.add(tfDateTo);
		tfDateTo.setColumns(8);

		JPanel panel_user = new JPanel();
		panel_user.setBorder(new TitledBorder(null, "User", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_user.setBounds(12, 34, 172, 59);
		panel_filters.add(panel_user);
		panel_user.setLayout(null);

		comboBox_user = new JComboBox<String>();
		comboBox_user.setEnabled(false);
		comboBox_user.setBounds(12, 22, 148, 24);
		panel_user.add(comboBox_user);

		btnApply = new JButton("Apply");
		btnApply.setEnabled(false);
		btnApply.setBounds(111, 209, 73, 25);
		panel_filters.add(btnApply);
		btnApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filterResults();
			}
		});

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmFilezillaLogStats.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
