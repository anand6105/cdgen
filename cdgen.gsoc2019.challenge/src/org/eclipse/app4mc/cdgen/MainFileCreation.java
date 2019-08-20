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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.MappingModel;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.app4mc.amalthea.model.util.HardwareUtil;
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

public class MainFileCreation {
	final private Amalthea model;

	/**
	 * MainFileCreation Constructor
	 *
	 * @param Model
	 * @param srcPath
	 * @param configFlag
	 * @throws IOException
	 */
	public MainFileCreation(final Amalthea Model, final String srcPath, final int configFlag) throws IOException {
		this.model = Model;
		System.out.println("Main File Creation Begins");
		if (0x2000 == (configFlag & 0xF000)) {
			fileCreatePthread(this.model, srcPath);
		}
		System.out.println("Main File Creation Ends");
	}


	/**
	 * Shared Label definition and initialization structure.
	 *
	 * @param file
	 * @param labellist
	 */
	public static List<Label> SharedLabelCoreDefinition(final Amalthea model, final String srcPath) {
		final EList<Label> labellist = model.getSwModel().getLabels();
		final List<Label> SharedLabelListSortCore = new ArrayList<Label>();
		if (labellist.size() == 0) {
			System.out.println("Shared Label size 0");
		}
		else {
			final HashMap<Label, HashMap<Task, ProcessingUnit>> sharedLabelTaskMap = LabelFileCreation
					.LabelTaskMap(model, labellist);
			int i = 0, k = 0, j = 0;
			for (final Label share : labellist) {
				final HashMap<Task, ProcessingUnit> TaskMap = sharedLabelTaskMap.get(share);
				final Collection<ProcessingUnit> puList = TaskMap.values();
				final List<ProcessingUnit> puListUnique = puList.stream().distinct().collect(Collectors.toList());
				final Set<Task> TaskList = TaskMap.keySet();
				if (puListUnique.size() == 1 && TaskList.size() > 1) {
					SharedLabelListSortCore.add(share);
					i++;
				}
				else if (puListUnique.size() > 1) {
					j++;
				}
				else if (TaskList.size() == 1) {
					k++;
				}
			}
			System.out.println("Total Labels :" + sharedLabelTaskMap.keySet().size() + "=" + i + "+" + j + "+" + k + "="
					+ (i + j + k));
		}
		return SharedLabelListSortCore;
	}

	/**
	 * Main function for RMS Scheduler
	 *
	 * @param model
	 * @param file
	 * @param tasks
	 */
	private static void mainFucntionRMS(final Amalthea model, final File file, final Set<Task> tasks) {
		try {
			final File fn = file;
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
					final double sleepTime = taskTime.getValue().doubleValue();
					fw.write("\tAmaltheaTask AmalTk_" + task.getName() + " = createAmaltheaTask(" + task.getName()
							+ ", cIN_" + task.getName() + ", cOUT_" + task.getName() + ", "
							+ task.getStimuli().get(0).getName() + ", " + task.getStimuli().get(0).getName() + ", "
							+ sleepTime + ");\n");
				}
			}
			
			fw.write("\n\tvDisplayMessage(\"created RMS sched task\\n\");\n");
			int count = 0;
			for (final Task task : tasks) {
				fw.write("\txTaskCreate(generalizedRTOSTask , \"AmalTk_" + task.getName()
						+ "\", configMINIMAL_STACK_SIZE, &AmalTk_" + task.getName() + ", main" + task.getName()
						+ ", NULL);\n");
				count++;
			}
			fw.write("\tvDisplayMessage(\"created other tasks\\n\");\n");
			fw.write("\tvTaskStartScheduler();\n");
			fw.write("\t" + "return EXIT_SUCCESS;\n");
			fw.write("}\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void sleepTimerMsPthread(final File f1) {
		try {
			final File fn = f1;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("void sleepTimerMs(int ticks)\n");
			fw.write("{\n");
			fw.write("\tint var;\n");
			fw.write("\tfor (var = 0; var < ticks; ++var)\n");
			fw.write("\t{\n");
			fw.write("\t\tsuspendMe();\n");
			fw.write("\t\t{\n");
			fw.write("\t\t\tusleep(1000);\n");
			fw.write("\t\t}\n");
			fw.write("\t\tresumeMe();\n");
			fw.write("\t}\n");
			fw.write("}\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void sleepTimerMs(final File f1) {
		try {
			final File fn = f1;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("void sleepTimerMs(int ticks)\n");
			fw.write("{\n");
			fw.write("\tint var;\n");
			fw.write("\tfor (var = 0; var < ticks; ++var)\n");
			fw.write("\t{\n");
			fw.write("\t\tvTaskSuspendAll();\n");
			fw.write("\t\t{\n");
			fw.write("\t\t\tusleep(1000);\n");
			fw.write("\t\t}\n");
			fw.write("\t\txTaskResumeAll();\n");
			fw.write("\t}\n");
			fw.write("}\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void fileCreatePthread(final Amalthea model, final String path1) throws IOException {
		final EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		int k = 0;
		for (final SchedulerAllocation c : CoreNo) {
			final ProcessingUnit pu = c.getResponsibility().get(0);
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
				headerIncludesMainPthread(f1, model, k, tasks);
				sleepTimerMsPthread(f1);
				mainFucntionPthread(f1, tasks);
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

	private static void mainFucntionPthread(final File f1, final Set<Task> tasks) {
		try {
			final File fn = f1;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("int main(void) \n{\n");
			fw.write("\tpthread_t thread[NUM_THREADS];\n");
			fw.write("\tpthread_attr_t attr;\n\n");
			final int tasksize = tasks.size();
			int init = 0;
			while (init < tasksize) {
				fw.write("\tlong rtr" + init + "=" + init + ";\n");
				init++;
			}
			fw.write("\tlong t;\n");
			fw.write("\tint rc;\n");
			fw.write("\tvoid *status;\n\n");
			fw.write("\tfor(;;)\n\t{\n");
			fw.write("\t\tpthread_attr_init(&attr);\n");
			fw.write("\t\tpthread_attr_setdetachstate(&attr, PTHREAD_CREATE_JOINABLE);\n\n");
			int init1 = 0;
			while (init1 < tasksize) {
				fw.write("\t\tpthread_create (&thread[" + init1 + "], &attr, v" + tasks.iterator().next().getName()
						+ ", (void *)rtr" + init1 + ");\n");
				init1++;
			}
			fw.write("\t\tif (rc)\n");
			fw.write("\t\t{\n");
			fw.write("\t\t\tprintf(\"ERROR; return code from pthread_join() is %d\\n\", rc);\n");
			fw.write("\t\t\texit(-1);\n");
			fw.write("\t\t}\n");

			fw.write("\n\t\tpthread_attr_destroy(&attr);\n");
			fw.write("\n\t\tfor(t=0; t<NUM_THREADS; t++)\n");
			fw.write("\t\t{\n");
			fw.write("\t\t\trc = pthread_join(thread[t], &status);\n");
			fw.write("\t\t\tif (rc)\n");
			fw.write("\t\t\t{\n");
			fw.write("\t\t\t\tprintf(\"ERROR; return code from pthread_join() is %d\\n\", rc);\n");
			fw.write("\t\t\t\texit(-1);\n");
			fw.write("\t\t\t}\n");
			fw.write(
					"\t\t\tprintf(\"Main: completed join with thread %ld having a status of %ld\\n\",t,(long)status);\n");
			fw.write("\t\t}\n");
			fw.write("\t\tprintf(\"\\n\");\n");
			fw.write("\t}\n");
			fw.write("\tprintf(\"Main: program completed. Exiting.\\n\");\n");
			fw.write("\tpthread_exit(NULL);\n");
			fw.write("}\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void headerIncludesMainPthread(final File f1, final Amalthea model2, final int k,
			final Set<Task> tasks) {
		try {
			final File fn = f1;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <pthread.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"taskDef" + k + ".h\"\n");
			fw.write("#define NUM_THREADS\t" + tasks.size() + "\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void mainFucntionMulticore(final Amalthea model, final File f1, final List<Task> tasks) {
		try {
			final File fn = f1;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("int main(void) \n{\n");
			for (final Task task : tasks) {
				final MappingModel mappingModel = model.getMappingModel();
				if (mappingModel != null) {
					final List<ProcessingUnit> processingUnits = HardwareUtil
							.getModulesFromHwModel(ProcessingUnit.class, model);
					final ArrayList<ProcessingUnit> localPU = new ArrayList<ProcessingUnit>();
					localPU.addAll(processingUnits);
					final HashMap<ProcessingUnit, Long> CoreMap = new HashMap<ProcessingUnit, Long>();
					long count = 0;
					for (final ProcessingUnit p : localPU) {
						CoreMap.put(p, count);
						count++;
					}
					final ProcessingUnit pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();

					final Long coreID = CoreMap.get(pu);
					fw.write("\txTaskCreate( " + coreID + ", v" + task.getName() + ", \"" + task.getName().toUpperCase()
							+ "\", configMINIMAL_STACK_SIZE, NULL, main" + task.getName() + ", NULL );\n");
				}
				else {
					fw.write("\txTaskCreate( v" + task.getName() + ", \"" + task.getName().toUpperCase()
							+ "\", configMINIMAL_STACK_SIZE, NULL, main" + task.getName() + ", NULL );\n");
				}
			}
			fw.write("\tvTaskStartScheduler();\n");
			fw.write("\t" + "return 0;\n");
			fw.write("}\n");
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

	private static void mainFucntion(final File f1, final EList<Task> tasks) {
		try {
			final File fn = f1;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("int main(void) \n{\n");
			for (final Task task : tasks) {
				fw.write("\txTaskCreate( v" + task.getName() + ", \"" + task.getName().toUpperCase()
						+ "\", configMINIMAL_STACK_SIZE, NULL, main" + task.getName() + ", NULL );\n");
			}
			fw.write("\tvTaskStartScheduler();\n");
			fw.write("\t" + "return 0;\n");
			fw.write("}\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	// TODO Read paper send by lukas
	private static void mainTaskPriority(final File f1, final List<Task> tasks) {
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


	private static void mainTaskStimuli(final Amalthea model, final File f1, final List<Task> tasks) {
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