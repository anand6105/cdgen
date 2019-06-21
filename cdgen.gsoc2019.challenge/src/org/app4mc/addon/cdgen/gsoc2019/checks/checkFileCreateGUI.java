package org.app4mc.addon.cdgen.gsoc2019.checks;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import org.app4mc.addon.cdgen.gsoc2019.*;
import org.app4mc.addon.cdgen.gsoc2019.identifiers.Constants;
import org.app4mc.addon.cdgen.gsoc2019.test.testTaskStructure;
import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.io.AmaltheaLoader;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.awt.event.ActionEvent;
import java.awt.Desktop;
import javax.swing.JSeparator;
import java.awt.Font;
import java.awt.Color;

/**
 * Implementation of GUI Design and Action on Button Click.
 * 
 * @author Ram Prasath Govindarajan
 *
 */

public class checkFileCreateGUI {
	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				checkFileCreateGUI window = null;
				try {
					window = new checkFileCreateGUI();
				} catch (IOException e) {
					e.printStackTrace();
				}
				window.frame.setVisible(true);
				window.frame.setTitle("CDGen");

			}
		});

	}

	public checkFileCreateGUI() throws IOException {
		initialize();
	}

	private void initialize() throws IOException {

		frame = new JFrame();
		frame.setBounds(100, 100, 864, 272);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JRadioButton cdgenFreeRTOS = new JRadioButton("FreeRTOS");
		cdgenFreeRTOS.setBounds(50, 90, 145, 25);
		frame.getContentPane().add(cdgenFreeRTOS);

		JRadioButton cdgenPosix = new JRadioButton("POSIX");
		cdgenPosix.setBounds(50, 120, 145, 25);
		frame.getContentPane().add(cdgenPosix);

		JRadioButton cdgenCustomSystem = new JRadioButton("Custom");
		cdgenCustomSystem.setBounds(50, 150, 145, 25);
		frame.getContentPane().add(cdgenCustomSystem);

		ButtonGroup group = new ButtonGroup();
		group.add(cdgenFreeRTOS);
		group.add(cdgenPosix);
		group.add(cdgenCustomSystem);

		JRadioButton cdgenrms = new JRadioButton("RMS(Rate Monotonic)");
		cdgenrms.setBounds(210, 90, 210, 25);
		frame.getContentPane().add(cdgenrms);

		JRadioButton cdgenedf = new JRadioButton("EDF(Earliest Deadline)");
		cdgenedf.setBounds(210, 120, 210, 25);
		frame.getContentPane().add(cdgenedf);

		JRadioButton cdgencustom = new JRadioButton("Non Custom");
		cdgencustom.setBounds(210, 150, 145, 25);
		frame.getContentPane().add(cdgencustom);

		ButtonGroup group2 = new ButtonGroup();
		group2.add(cdgenrms);
		group2.add(cdgenedf);
		group2.add(cdgencustom);

		JRadioButton cdgenCooperative = new JRadioButton("Cooperative");
		cdgenCooperative.setBounds(430, 90, 145, 25);
		frame.getContentPane().add(cdgenCooperative);

		JRadioButton cdgenPreemptive = new JRadioButton("Preemptive");
		cdgenPreemptive.setBounds(430, 120, 145, 25);
		frame.getContentPane().add(cdgenPreemptive);

		ButtonGroup group3 = new ButtonGroup();
		group3.add(cdgenCooperative);
		group3.add(cdgenPreemptive);

		JButton btnSelectTasks = new JButton("Generate Code");
		btnSelectTasks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Load File
				final File inputFile = new File(Constants.DEMOCAR);
				// final File inputFile = new File(Constants.WATERS2019_TEST);
				// final File inputFile = new File(Constants.WATERS2019_TEST);
				// final File inputFile = new File(Constants.WATERS2019_ANA);
				// final File inputFile = new File(Constants.WATERS2019_ANAMOD);
				final Amalthea model = AmaltheaLoader.loadFromFile(inputFile);
				if (model == null) {
					System.out.println("Error: No model loaded!");
					return;
				}
				String path = System.getProperty("user.dir");
				String timestamp = new Timestamp(System.currentTimeMillis()).toString();
				timestamp = timestamp.substring(0, timestamp.length() - 6).replaceAll(":", "");
				timestamp = timestamp.replaceAll("-", "_");
				timestamp = timestamp.replaceAll(" ", "_");
				File theDir = new File(timestamp);
				if (!theDir.exists()) {
					System.out.println("creating directory: " + theDir.getName());
					boolean result = false;

					try {
						theDir.mkdir();
						result = true;
					} catch (SecurityException se) {
					}
					if (result) {
						System.out.println("DIR created");
					}
				}
				String path1 = path + "/" + timestamp;
				String path2 = path1 + "/includes";
				boolean pthreadFlag, preemptionFlag;

				try {
					Path copied = Paths.get(path + "/ref/FreeRTOSConfig.h");
					Path originalPath = Paths.get(path1);
					Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				if (cdgenFreeRTOS.isSelected() & cdgencustom.isSelected()) {
					pthreadFlag = false;
					try {
						new MainFileCreation(model, path1, pthreadFlag);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						new RunFileCreation(model, path1, path2, pthreadFlag);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					try {
						new LabelFileCreation(model, path1);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						new FreeRTOSConfigFileCreation(model, path1, pthreadFlag);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					if (cdgenCooperative.isSelected()) {
						preemptionFlag = false;
						try {
							new FreeRTOSConfigFileCreation(model, path1, preemptionFlag);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						try {
							new TaskFileCreation(model, path1, path2, pthreadFlag, preemptionFlag);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						try {
							new testTaskStructure(model, path1, pthreadFlag, preemptionFlag);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else if (cdgenPreemptive.isSelected()) {
						preemptionFlag = true;
						try {
							new FreeRTOSConfigFileCreation(model, path1, preemptionFlag);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						try {
							new TaskFileCreation(model, path1, path2, pthreadFlag, preemptionFlag);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						try {
							new testTaskStructure(model, path1, pthreadFlag, preemptionFlag);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

					/*
					 * Path sourceDirectory = Paths.get(pathref); Path targetDirectory =
					 * Paths.get(path1); try {
					 * Files.copy(sourceDirectory,targetDirectory,StandardCopyOption.
					 * REPLACE_EXISTING); } catch (IOException e2) { block e2.printStackTrace(); }
					 */
					System.out.println("Generation completed, Check path 	" + path1);
					try {
						Desktop.getDesktop().open(new File(path1));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.exit(0);
				} else if (cdgenPosix.isSelected() & cdgencustom.isSelected()) {
					pthreadFlag = true;

					try {
						new MainFileCreation(model, path1, pthreadFlag);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						new RunFileCreation(model, path1, path2, pthreadFlag);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						new LabelFileCreation(model, path1);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					if (cdgenCooperative.isSelected()) {
						preemptionFlag = false;
						try {
							new TaskFileCreation(model, path1, path2, pthreadFlag, preemptionFlag);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						try {
							new testTaskStructure(model, path1, pthreadFlag, preemptionFlag);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else if (cdgenPreemptive.isSelected()) {
						preemptionFlag = true;
						try {
							new TaskFileCreation(model, path1, path2, pthreadFlag, preemptionFlag);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						try {
							new testTaskStructure(model, path1, pthreadFlag, preemptionFlag);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

					System.out.println("Generation completed, Check path 	" + path1);
					try {
						Desktop.getDesktop().open(new File(path1));
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					System.exit(0);

				} else {
					System.out.println("Configuration Not Defined!");
				}
			}

		});
		btnSelectTasks.setBounds(673, 95, 149, 25);
		frame.getContentPane().add(btnSelectTasks);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnClose.setBounds(673, 164, 149, 25);
		frame.getContentPane().add(btnClose);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnReset.setBounds(673, 129, 149, 25);
		frame.getContentPane().add(btnReset);

		JLabel lblAllTasks = new JLabel("OS Options");
		lblAllTasks.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblAllTasks.setBounds(50, 69, 110, 16);
		frame.getContentPane().add(lblAllTasks);

		JLabel lblSortedTasks = new JLabel("Scheduling Options");
		lblSortedTasks.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSortedTasks.setBounds(210, 69, 180, 16);
		frame.getContentPane().add(lblSortedTasks);

		JLabel lblResponsetime = new JLabel("Task Preemption");
		lblResponsetime.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblResponsetime.setBounds(430, 69, 150, 16);
		frame.getContentPane().add(lblResponsetime);

		JLabel lblPerformanceMetric = new JLabel("CDGen - Code Generator for APP4MC");
		lblPerformanceMetric.setForeground(Color.ORANGE);
		lblPerformanceMetric.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblPerformanceMetric.setBounds(27, 13, 450, 30);
		frame.getContentPane().add(lblPerformanceMetric);

		JSeparator separator = new JSeparator();
		separator.setBounds(12, 42, 822, 272);
		frame.getContentPane().add(separator);

	}
}