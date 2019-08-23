/*******************************************************************************
 *   Copyright (c) 2019 Dortmund University of Applied Sciences and Arts and others.
 *
 *   This program and the accompanying materials are made
 *   available under the terms of the Eclipse Public License 2.0
 *   which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 *   SPDX-License-Identifier: EPL-2.0
 *
 *   Contributors:
 *       Dortmund University of Applied Sciences and Arts - initial API and implementation
 *******************************************************************************/
package org.eclipse.app4mc.cdgen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Process;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil.TimeType;
import org.eclipse.app4mc.amalthea.model.util.SoftwareUtil;
import org.eclipse.app4mc.amalthea.model.util.TimeUtil;
import org.eclipse.app4mc.cdgen.utils.fileUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of Runnable Definition with Runnable specific delay.
 *
 *
 */

public class RunFileCreation {
	final private Amalthea model;

	/**
	 * Constructor RunFileCreation
	 *
	 * @param Model
	 *            Amalthea Model
	 * @param srcPath
	 * @param pthreadFlag
	 * @throws IOException
	 */
	public RunFileCreation(final Amalthea Model, final String srcPath, final String path2, final int configFlag)
			throws IOException {
		this.model = Model;
		System.out.println("Runnable File Creation Begins");
		fileCreate(this.model, srcPath, path2, configFlag);
		System.out.println("Runnable File Creation Ends");

	}


	/**
	 * Runnable File Creation
	 *
	 * @param model
	 * @param srcPath
	 * @param path2
	 * @param configFlag
	 * @param tasks
	 * @param runnables
	 * @throws IOException
	 */
	private static void fileCreate(final Amalthea model, final String srcPath, final String path2, final int configFlag)
			throws IOException {
		final EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		int k = 0;
		for (final SchedulerAllocation c : CoreNo) {
			final ProcessingUnit pu = c.getResponsibility().get(0);
			final Set<Task> task = DeploymentUtil.getTasksMappedToCore(pu, model);
			final List<Task> tasks = new ArrayList<Task>(task);
			final String fname = srcPath + File.separator + "runnable" + k + ".c";
			final File f2 = new File(srcPath);
			final File f1 = new File(fname);
			f2.mkdirs();
			try {
				f1.createNewFile();
			}
			catch (final IOException e) {
				e.printStackTrace();
			}

			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			try {
				if (0x2000 == (configFlag & 0xF000)) {
					fileUtil.fileMainHeader(f1);
					runFileHeader(f1);
					headerIncludesRun(f1, k);
					runnablePthreadDefinition(f1, tasks, model);
				}
				else {
					fileUtil.fileMainHeader(f1);
					runFileHeader(f1);
					headerIncludesRun(f1, k);
					runnableDefinition(f1, tasks, model);
				}

			}
			finally {
				try {
					fw.close();
				}
				catch (final IOException e) {
					e.printStackTrace();
				}
			}
			final String fname2 = srcPath + File.separator + "runnable" + k + ".h";
			final File f4 = new File(srcPath);
			final File f3 = new File(fname2);
			f4.mkdirs();
			try {
				f1.createNewFile();
			}
			catch (final IOException e) {
				e.printStackTrace();
			}

			final File fn1 = f3;
			@SuppressWarnings("resource")
			final FileWriter fw1 = new FileWriter(fn1, true);
			try {
				if (0x2000 == (configFlag & 0xF000)) {
					fileUtil.fileMainHeader(f3);
					runFileHeader(f3);
					headerIncludesRunPthreadHead(f3);
					runnableDeclaration(f3, tasks);
				}
				else {
					fileUtil.fileMainHeader(f3);
					runFileHeader(f3);
					headerIncludesRunHead(f3);
					runnableDeclaration(f3, tasks);
				}

			}
			finally {
				try {
					fw1.close();
				}
				catch (final IOException e) {
					e.printStackTrace();
				}
			}
			k++;
		}
	}

	/**
	 * Title Card for RunFileCreation
	 *
	 * @param file
	 */
	private static void runFileHeader(final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   Runnable Header\n");
			fw.write("*Description	:	Runnable Definition with Runnable delay\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");

			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * Header inclusion for the Runnable.c file
	 *
	 * @param file
	 */
	private static void headerIncludesRun(final File file, final int k) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"runnable" + k + ".h\"\n");

			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * RunFileCreation - Header inclusion for runnable.h
	 *
	 * @param file
	 */
	private static void headerIncludesRunHead(final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			// fw.write("#include \"runnable.h\"\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * RunFileCreation - Header inclusion for runnable.h pthread specific
	 *
	 * @param file
	 */
	private static void headerIncludesRunPthreadHead(final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * RunFileCreation - Runnable Definition Pthread specific
	 *
	 * @param file
	 * @param tasks
	 * @param model
	 */
	private static void runnablePthreadDefinition(final File file, final List<Task> tasks, final Amalthea model) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			// int taskCounter =1;
			for (final Task t : tasks) {
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(t, null);
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				// int runnableCounter =1;
				for (final Runnable Run : runnablesOfTask) {
					fw.write("void " + Run.getName() + " (void)\t{\n");
					/*
					 * fw.write("\tvDisplayMessagePthread(\" " + t.getName() +
					 * " \tRunnable Execution	" + "\t" + Run.getName() +
					 * "\\n\");\n");
					 */
					final Process RunTaskName = SoftwareUtil.getCallingProcesses(Run, null).get(0);
					final Set<ProcessingUnit> pu = DeploymentUtil.getAssignedCoreForProcess(RunTaskName, model);
					if (pu != null) {
						for (final ProcessingUnit p : pu) {
							Time RunTime1 = RuntimeUtil.getExecutionTimeForRunnable(Run, p, null, TimeType.WCET);
							RunTime1 = TimeUtil.convertToTimeUnit(RunTime1, TimeUnit.US);

							final double sleepTime = RunTime1.getValue().doubleValue();

							fw.write("\tusleep(" + sleepTime + ");\n");
							break;
						}
					}
					fw.write("}\n");
					// runnableCounter++;
				}
				// taskCounter++;
			}
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * Runnable Definition(Generic)
	 *
	 * @param file
	 * @param tasks
	 * @param model
	 */
	private static void runnableDefinition(final File file, final List<Task> tasks, final Amalthea model) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			for (final Task t : tasks) {
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(t, null);
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				for (final Runnable Run : runnablesOfTask) {
					fw.write("void " + Run.getName() + " \t(void)\t{\n");
					final Process RunTaskName = SoftwareUtil.getCallingProcesses(Run, null).get(0);
					final Set<ProcessingUnit> pu = DeploymentUtil.getAssignedCoreForProcess(RunTaskName, model);
					for (final ProcessingUnit p : pu) {
						Time RunTime1 = RuntimeUtil.getExecutionTimeForRunnable(Run, p, null, TimeType.WCET);
						RunTime1 = TimeUtil.convertToTimeUnit(RunTime1, TimeUnit.US);
						break;
					}
					fw.write("}\n");
				}
			}
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * Runnable Declaration in runnable.h
	 *
	 * @param file
	 * @param runnables
	 */
	private static void runnableDeclaration(final File file, final List<Task> tasks) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			final List<Runnable> runnables = new ArrayList<Runnable>();
			for (final Task ta : tasks) {
				runnables.addAll(SoftwareUtil.getRunnableList(ta, null));
			}
			for (final Runnable Run : runnables) {
				fw.write("void " + Run.getName() + " (void);\n");
			}
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * helper function to get the Amalthea Model
	 *
	 */
	public Amalthea getModel() {
		return this.model;
	}

}
