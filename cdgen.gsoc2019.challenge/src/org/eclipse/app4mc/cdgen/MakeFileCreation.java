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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.MappingModel;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil.TimeType;
import org.eclipse.app4mc.cdgen.utils.fileUtil;
import org.eclipse.app4mc.amalthea.model.util.TimeUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of Main function in which scheduling is done.
 *
 * @author Ram Prasath Govindarajan
 *
 */

public class MakeFileCreation {
	final private Amalthea model;

	public MakeFileCreation(final Amalthea Model, final String path1, final int configFlag) throws IOException {
		this.model = Model;
		System.out.println("Main File Creation Begins");
		fileCreate(this.model, path1, configFlag);
		System.out.println("Main File Creation Ends");
	}

	private static void fileCreate(final Amalthea model, final String path1, final int configFlag) throws IOException {
		model.getMappingModel().getTaskAllocation();
		final EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		int k = 0;
		for (final SchedulerAllocation c : CoreNo) {
			final ProcessingUnit pu = c.getResponsibility().get(0);
			System.out.println("Core ==> " + pu);
			final Set<Task> tasks = DeploymentUtil.getTasksMappedToCore(pu, model);
			final String fname = path1 + File.separator + "main" + k + ".c";
			final File f2 = new File(path1);
			final File f1 = new File(fname);
			f2.mkdirs();
			try {
				f1.createNewFile();
			}
			catch (final IOException e) {
				e.printStackTrace();
			}
			final File fn = f1;
			final FileWriter fw = new FileWriter(fn, true);
			try {
				fileUtil.fileMainHeader(f1);
				mainFileHeader(f1);
				if ((0x0100 == (0x0F00 & configFlag)) & (0x3000 == (0xF000 & configFlag))) {
					headerIncludesMainRMS(f1);
					mainTaskStimuli(model, f1, tasks);
					mainTaskPriority(f1, tasks);
					mainFucntionRMS(model, f1, tasks);
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
			k++;
		}
	}

	private static void mainFucntionRMS(final Amalthea model, final File f1, final Set<Task> tasks) {
		try {
			final File fn = f1;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("int main(void) \n{\n");
			fw.write("\toutbuf_init();\n");
			for (final Task task : tasks) {
				final MappingModel mappingModel = model.getMappingModel();
				ProcessingUnit pu = null;
				if (mappingModel != null) {
					pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();
					Time taskTime = RuntimeUtil.getExecutionTimeForProcess(task, pu, null, TimeType.WCET);
					taskTime = TimeUtil.convertToTimeUnit(taskTime, TimeUnit.US);
					final BigInteger sleepTime = taskTime.getValue();
					fw.write("\tAmaltheaTask AmalTk_" + task.getName() + " = createAmaltheaTask( v" + task.getName()
							+ ", cIN_" + task.getName() + ", cOUT_" + task.getName() + ", "
							+ task.getStimuli().get(0).getName() + ", " + task.getStimuli().get(0).getName() + ", "
							+ sleepTime + ");\n");
				}
			}

			int count = 0;
			for (final Task task : tasks) {
				fw.write("\txTaskCreate(generalizedRTOSTask , \"AmalTk_" + task.getName()
						+ "\", configMINIMAL_STACK_SIZE, &AmalTk_" + task.getName() + ", main" + task.getName()
						+ ", NULL);\n");
				count++;
			}
			fw.write("\tvTaskStartScheduler();\n");
			fw.write("\t" + "return EXIT_SUCCESS;\n");
			fw.write("}\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void taskHandleRMS(final File f1, final EList<Task> tasks) {
		try {
			final File fn = f1;
			final FileWriter fw = new FileWriter(fn, true);
			final List<Task> localTaskPriority = new ArrayList<Task>();
			localTaskPriority.addAll(tasks);
			fw.write("/* TaskHandler. */\n");
			for (final Task task : tasks) {
				fw.write("\txTaskHandle\t\ttaskHandle" + task.getName() + ";\n");
				// TODO merge this constval with the value used in time period
				// in FreeRTOS config File - Issue001
			}
			fw.write("\tAmaltheaTask taskList[];\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void mainFileHeader(final File f1) {
		try {
			final File fn = f1;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   C File for Tasks Call\n");
			fw.write("*Description	:	Main file in which scheduling is done \n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void headerIncludesMain(final File f1) {
		try {
			final File fn = f1;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"taskDef.h\"\n");
			fw.write("#include \"FreeRTOS.h\"\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void headerIncludesMainRMS(final File f1) {
		try {
			final File fn = f1;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n");
			fw.write("#include <e_lib.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"FreeRTOS.h\"\n");
			fw.write("#include \"task.h\"\n");
			fw.write("#include \"queue.h\"\n");
			fw.write("#include \"AmaltheaConverter.h\"\n");
			fw.write("#include \"debugFlags.h\"\n");
			fw.write("#include \"taskDef.h\"\n\n");
			fw.write("#define READ_PRECISION_US 1000\n\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	// TODO Read paper send by lukas
	private static void mainTaskPriority(final File f1, final Set<Task> tasks) {
		try {
			final File fn = f1;
			final List<Task> localTaskPriority = new ArrayList<Task>();
			localTaskPriority.addAll(tasks);
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* TaskPriorities. */\n");
			final HashMap<Task, Long> periodMap = new HashMap<Task, Long>();
			for (final Task task : tasks) {
				final long period = fileUtil.getRecurrence(task).getValue().longValue();
				periodMap.put(task, period);
			}
			final Map<Task, Long> periodMapSorted = fileUtil.sortByValue(periodMap);
			for (int i = 0; i < periodMapSorted.size(); i++) {
				final Task task = (Task) periodMapSorted.keySet().toArray()[i];
				fw.write("\t#define main" + task.getName() + "\t( tskIDLE_PRIORITY +" + (i + 1) + " )\n");// TODO
																											// merge
																											// this
																											// constval
																											// with
																											// the
																											// value
																											// used
																											// in
																											// time
																											// period
																											// in
																											// FreeRTOS
																											// config
																											// File
																											// -
																											// Issue001
			}
			fw.write("\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void mainTaskStimuli(final Amalthea model, final File f1, final Set<Task> tasks) {
		try {
			final File fn = f1;

			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* TaskStimuli. */\n");
			final EList<Stimulus> Stimuli = model.getStimuliModel().getStimuli();
			for (final Stimulus s : Stimuli) {
				if (s instanceof PeriodicStimulus) {
					fw.write("\t#define " + s.getName() + "\t" + ((PeriodicStimulus) s).getRecurrence().getValue()
							+ " \n");
				}
			}
			fw.write("\n");
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
