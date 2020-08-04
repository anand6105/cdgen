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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.MappingModel;
import org.eclipse.app4mc.amalthea.model.PeriodicStimulus;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Stimulus;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.Time;
import org.eclipse.app4mc.amalthea.model.TimeUnit;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.app4mc.amalthea.model.util.HardwareUtil;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil.TimeType;
import org.eclipse.app4mc.amalthea.model.util.SoftwareUtil;
import org.eclipse.app4mc.amalthea.model.util.TimeUtil;
import org.eclipse.app4mc.cdgen.utils.fileUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of Task Definition with Cin and Cout Calls.
 *
 *
 */

public class TaskFileCreation {
	final private Amalthea model;

	/**
	 * Constructor TaskFileCreation
	 *
	 * @param Model
	 *            - Amalthea Model
	 * @param srcPath
	 * @param pthreadFlag
	 * @param preemptionFlag
	 * @throws IOException
	 */
	public TaskFileCreation(final Amalthea Model, final String srcPath, final String path2, final int configFlag)
			throws IOException {
		this.model = Model;

		if ((0x3100 == (0xFF00 & configFlag)) || (0x1300 == (0xFF00 & configFlag))) {
			System.out.println("Task File Creation Begins");
			fileCreate(this.model, srcPath, path2, configFlag);
			System.out.println("Task File Creation Ends");
		}
		else {
			System.out.println("Task File Creation Begins");
			fileCreatePthread(this.model, srcPath, path2, configFlag);
			System.out.println("Task File Creation Ends");
		}

	}


	private static void fileCreate(final Amalthea model, final String srcPath, final String path2, final int configFlag)
			throws IOException {
		boolean preemptionFlag = false;
		if (0x0020 == (0x00F0 & configFlag)) {
			preemptionFlag = true;
		}
		else {
			preemptionFlag = false;
		}
		final EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		int k = 0;
		for (final SchedulerAllocation c : CoreNo) {
			final ProcessingUnit pu = c.getResponsibility().get(0);
			final Set<Task> task = DeploymentUtil.getTasksMappedToCore(pu, model);
			final List<Task> tasks = new ArrayList<Task>(task);
			final String fname = srcPath + File.separator + "taskDef" + k + ".c";
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
				fileUtil.fileMainHeader(f1);
				taskFileHeader(f1);
				if ((0x3000 == (0xF000 & configFlag)) & (0x0100 == (0x0F00 & configFlag))) {
					TaskCounter(f1, tasks);
					if (0x0001 == (0x0001 & configFlag)) {
						headerIncludesTaskHeadRMS(f1, k, true);
						TaskDefinitionRMS(f1, model, tasks, preemptionFlag, true);
					}
					else {
						headerIncludesTaskHeadRMS(f1, k, false);
						TaskDefinitionRMS(f1, model, tasks, preemptionFlag, false);
					}

				}
				else if ((0x1000 == (0xF000 & configFlag)) & (0x0300 == (0x0F00 & configFlag))) {
					headerIncludesTaskHeadRMS(f1, k, false);
					TaskCounter(f1, tasks);
					TaskDefinitionFreeRTOS(f1, model, tasks, preemptionFlag);
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
			final String fname2 = srcPath + File.separator + "taskDef" + k + ".h";
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
				fileUtil.fileMainHeader(f3);
				taskFileHeader(f3);
				headerIncludesTaskRMSHead(f3, k);
				mainStaticTaskDef(f3, tasks);

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


	private static void TaskCounter(final File f3, final List<Task> tasks) {
		try {
			final File fn = f3;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true); // the true will
															// append the new
															// data
			fw.write("/* Task Counter Declaration. */\n");
			for (final Task ta : tasks) {
				fw.write("int taskCount" + ta.getName() + "\t=\t0;\n");
			}
			fw.write("\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}


	private static void fileCreatePthread(final Amalthea model, final String path1, final String path2,
			final int configFlag) throws IOException {
		boolean preemptionFlag = false;
		if (0x0020 == (0x00F0 & configFlag)) {
			preemptionFlag = true;
		}
		else {
			preemptionFlag = false;
		}
		final EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		int k = 0;
		for (final SchedulerAllocation c : CoreNo) {
			final ProcessingUnit pu = c.getResponsibility().get(0);
			final Set<Task> task = DeploymentUtil.getTasksMappedToCore(pu, model);
			final List<Task> tasks = new ArrayList<Task>(task);
			final String fname = path1 + File.separator + "taskDef" + k + ".c";
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
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			try {
				fileUtil.fileMainHeader(f1);
				taskFileHeader(f1);
				headerIncludesTaskPthreadHead(f1);
				TaskDefinitionPthread(f1, tasks, model, preemptionFlag);
			}
			finally {
				try {
					fw.close();
				}
				catch (final IOException e) {
					e.printStackTrace();
				}
			}
			final String fname2 = path1 + File.separator + "taskDef" + k + ".h";
			final File f4 = new File(path1);
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
				fileUtil.fileMainHeader(f3);
				taskFileHeader(f3);
				headerIncludesPthreadTask(f3, k);
				mainStaticTaskPthreadDef(f3, tasks);
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

	private static void headerIncludesPthreadTask(final File f3, final int k) {
		try {
			final File fn = f3;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true); // the true will
															// append the new
															// data
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("#include <stdint.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"runnable" + k + ".h\"\n");
			fw.write("#include \"label" + k + ".h\"\n");

			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void mainStaticTaskPthreadDef(final File f3, final List<Task> tasks) {
		try {
			final File fn = f3;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Static definition of the tasks. */\n");
			for (final Task task : tasks) {
				fw.write("void v" + task.getName() + "( void *t );\n");
			}
			fw.write("\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	private static void TaskDefinitionPthread(final File f1, final List<Task> tasks, final Amalthea model,
			final boolean preemptionFlag) {
		try {
			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			for (final Task task : tasks) {
				final List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(task, null);
				final ArrayList<Label> labellist1 = new ArrayList<Label>();
				for (final Runnable run : runnablesOfTask) {
					final Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(run, null);
					labellist1.addAll(labellist);
				}
				final List<Label> listWithoutDuplicates2 = labellist1.stream().distinct().collect(Collectors.toList());
				for (final Label lab : listWithoutDuplicates2) {
					final String type = fileUtil.datatype(lab.getSize().toString());
					fw.write("\t\textern\t" + type + "\t" + lab.getName() + ";\n");
				}
				fw.write("\n\n\n");
				for (final Label lab : listWithoutDuplicates2) {
					final String type = fileUtil.datatype(lab.getSize().toString());
					fw.write("\t\textern\t" + type + "\t" + lab.getName() + "_" + task.getName() + ";\n");
				}
				fw.write("\n\n\n");

				fw.write("\n\tvoid cIN_" + task.getName() + "()\n\t{\n");
				for (final Label lab : listWithoutDuplicates2) {
					fw.write("\t\t" + lab.getName() + "_" + task.getName() + "\t=\t" + lab.getName() + ";\n");
				}
				fw.write("\t}\n");
				fw.write("\n\tvoid cOUT_" + task.getName() + "()\n\t{\n");
				final ArrayList<Label> labellist2 = new ArrayList<Label>();
				for (final Runnable run : runnablesOfTask) {
					final Set<Label> labellist = SoftwareUtil.getWriteLabelSet(run, null);
					labellist2.addAll(labellist);
				}
				final List<Label> listWithoutDuplicates1 = labellist2.stream().distinct().collect(Collectors.toList());

				for (final Label lab : listWithoutDuplicates1) {
					fw.write("\t\t" + lab.getName() + "\t=\t" + lab.getName() + "_" + task.getName() + ";\n");
				}
				fw.write("\t}\n");
				fw.write("\n\tvoid v" + task.getName() + "( void *t )" + "\n\t{\n");
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
					fw.write("\t\tcpu_set_t cpuset;\n");
					fw.write("\t\tint cpu = " + coreID + ";\n");
					fw.write("\t\tCPU_ZERO(&cpuset);\n");
					fw.write("\t\tCPU_SET( cpu , &cpuset);\n");
					final Set<ProcessingUnit> procUniSet = DeploymentUtil.getAssignedCoreForProcess(task, model);
					List<ProcessingUnit> procUniList = null;
					if (procUniSet != null) {
						procUniList = new ArrayList<ProcessingUnit>(procUniSet);
					}
					if (procUniList != null) {
						procUniList.get(0);
					}
					fw.write("\t\tsched_setaffinity(0, sizeof(cpuset), &cpuset);\n\n");
				}
				fw.write("\t\tfor( ;; )\n\t\t{\n");
				fw.write("\t\t\tsuspendMe ();\n");
				fw.write("\t\tprint_affinity();\n");
				fw.write("\t\t\tvDisplayMessagePthread( \"" + task.getName() + " is running\\r\\n\" );\n");
				fw.write("\t\t\t/* Cin - Create local variables and copy the actual variable to them */\n");
				fw.write("\t\t\tcIN_" + task.getName() + "();\n");
				if (preemptionFlag == true) {
					fw.write("\t\t\tresumeMe ();\n");
				}
				fw.write("\n\t\t\t/*Runnable calls */\n");
				for (final Runnable run : runnablesOfTask) {
					fw.write("\t\t\t" + run.getName() + "();\n");
				}
				fw.write("\n\t\t\t/* Cout - Write back the local variables back to the actual variables */\n");
				if (preemptionFlag == true) {
					fw.write("\t\t\tsuspendMe ();\n");
				}
				fw.write("\t\t\tcOUT_" + task.getName() + "();\n");
				fw.write("\t\t\tresumeMe ();\n");
				fw.write("\t\t\tsleepTimerMs(DELAY_MULT*NULL);\n");
				fw.write("\t\t\tpthread_exit((void*) t);\n");
				final Time tasktime = fileUtil.getRecurrence(task);
				if (tasktime != null) {
					// TimeUtil.getAsTimeUnit(fileUtil.getRecurrence(task),
					// null);

				}
				fw.write("\t\t}\n");
				fw.write("\t}\n\n");
			}
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void taskFileHeader(final File f1) {
		try {
			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   Task Definition\n");
			fw.write("*Description	:	Task Definition with Task Structure\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void headerIncludesTaskPthreadHead(final File f1) {
		try {
			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("#define _GNU_SOURCE\n\n");
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n");
			fw.write("#include <pthread.h>\n");
			fw.write("#include <sched.h>\n");
			fw.write("#include <stdint.h>\n\n");

			// fw.write("#define DELAY_MULT 100\n\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void headerIncludesTaskRMSHead(final File f1, final int k) {
		try {
			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n");
			fw.write("#include <stdint.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"FreeRTOS.h\"\n");
			fw.write("#include \"queue.h\"\n");
			fw.write("#include \"croutine.h\"\n");
			fw.write("#include \"debugFlags.h\"\n");
			fw.write("#include \"task.h\"\n");
			fw.write("#include \"ParallellaUtils.h\"\n");
			fw.write("#include \"label" + k + ".h\"\n");
			fw.write("#include \"runnable" + k + ".h\"\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void headerIncludesTaskHeadRMS(final File f1, final int k, final boolean btfEnable) {
		try {
			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("#include \"taskDef" + k + ".h\"\n\n");
			if (btfEnable == true) {
				fw.write("#include \"debugFlags.h\"\n" + 
						"#include \"RTFParallellaConfig.h\"\n" + 
						"#include \"trace_utils_BTF.h\"\n\n\n");
			}
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	public static void mainStaticTaskDef(final File f1, final List<Task> tasks) {
		try {
			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Static definition of the tasks. */\n");
			for (final Task task : tasks) {
				fw.write("void v" + task.getName() + "( );\n");
			}
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	public static HashMap<Task, Integer> TaskIndexMapping(final Amalthea model) {
		final EList<Task> tasks = model.getSwModel().getTasks();
		final HashMap<Task, Integer> TaskMapping = new HashMap<Task, Integer>();
		int i = 0;
		for (final Task Ta : tasks) {
			TaskMapping.put(Ta, i);
			i++;
		}
		return TaskMapping;
	}


	private static void TaskDefinitionRMS(final File f1, final Amalthea model, final List<Task> tasks,
			final boolean preemptionFlag, final boolean btfEnable) {
		try {
			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			final HashMap<Task, Integer> taskIndex = TaskIndexMapping(model);
			for (final Task task : tasks) {
				final Integer taskCount = taskIndex.get(task);
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(task, null);
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				fw.write("\n");
				final List<Label> taskLabelList = TaskSpecificLabel(model, task);
				for (final Label lab : taskLabelList) {
					final String type = fileUtil.datatype(lab.getSize().toString());
					fw.write("\t" + type + "\t" + lab.getName() + ";\n");
				}
				fw.write("\n");
				fw.write("\n\n");
				if (btfEnable == true) {
					fw.write("\n\tvoid v" + task.getName() + "(int src_id, int src_instance)" + "\n\t{\n");
				}
				else {
					fw.write("\n\tvoid v" + task.getName() + "()" + "\n\t{\n");
				}


				// fw.write("\n\n");
				fw.write("\t\tupdateDebugFlag(700);\n");
				if (btfEnable == false) {
					fw.write("\t\ttraceTaskPasses(1,1);\n");
				}
				fw.write("\n\t\t\t/*Runnable calls */\n");
				for (final Runnable run : runnablesOfTask) {
					fw.write("\t\t\t" + run.getName() + "();\n");
				}
				final MappingModel mappingModel = model.getMappingModel();
				ProcessingUnit pu = null;
				if (mappingModel != null) {
					pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();
					Time taskTime = RuntimeUtil.getExecutionTimeForProcess(task, pu, null, TimeType.WCET);
					taskTime = TimeUtil.convertToTimeUnit(taskTime, TimeUnit.MS);
					final BigInteger sleepTime = taskTime.getValue();
					final BigInteger b2 = new BigInteger("1000");
					final int comparevalue = sleepTime.compareTo(b2);
					if (btfEnable == true) {
						fw.write("\t\t\ttraceTaskEvent(src_id, src_instance, TASK_EVENT," + task.getName() + "_ID,\n" + 
								"\t\t\t\t\t taskCount"+ task.getName()+", PROCESS_START, 0);\n");
						if (comparevalue < 0) {
							fw.write("\n\t\t\tsleepTimerMs(1 , " + task.getName() + "_ID);\n");
						}
						else {
							fw.write("\n\t\t\tsleepTimerMs(" + sleepTime + ", " + task.getName() + "_ID);\n");
						}
					}
					else {
						if (comparevalue < 0) {
							fw.write("\n\t\t\tsleepTimerMs(1 , 1" + (taskCount + 1) + ");\n");
						}
						else {
							fw.write("\n\t\t\tsleepTimerMs(" + sleepTime + ", " + taskCount + 1 + ");\n");
						}
						
					}
					if (btfEnable == true) {
						fw.write("\t\t\ttraceTaskEvent(src_id, src_instance, TASK_EVENT," + task.getName() + "_ID,\n" + 
								"\t\t\t\t\t taskCount"+ task.getName()+", PROCESS_TERMINATE, 0);\n");
					}

				}
				fw.write("\n\t\t\ttaskCount" + task.getName() + "++;");
				if (btfEnable == false) {
					fw.write("\n\t\t\ttraceTaskPasses(" + taskCount + ", taskCount" + task.getName() + ");");
					fw.write("\n\t\t\ttraceRunningTask(0);\n");
				}
				fw.write("\t}\n\n");
			}
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void TaskDefinitionFreeRTOS(final File f1, final Amalthea model, final List<Task> tasks,
			final boolean preemptionFlag) {
		try {
			final File fn = f1;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			final HashMap<Task, Integer> taskIndex = TaskIndexMapping(model);
			for (final Task task : tasks) {
				Integer taskCount = taskIndex.get(task);
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(task, null);
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				final List<Label> taskLabelList = TaskSpecificLabel(model, task);
				fw.write("\n");
				for (final Label lab : taskLabelList) {
					final String type = fileUtil.datatype(lab.getSize().toString());
					fw.write("\t\t" + type + "\t" + lab.getName() + ";\n");
				}
				fw.write("\n");
				fw.write("\n\tvoid v" + task.getName() + "()" + "\n\t{\n");
				fw.write("\tportTickType xLastWakeTime=xTaskGetTickCount();\n");
				fw.write("\n\t\tfor( ;; )\n\t\t{\n");
				fw.write("\t\tupdateDebugFlag(700);\n");
				fw.write("\t\ttraceTaskPasses(1,1);\n");
				fw.write("\t\t\t/* Cin - Create local variables and copy the actual variable to them */\n");
				fw.write("\t\t\ttaskENTER_CRITICAL ();\n");
				fw.write("\t\t\tcIN_" + task.getName() + "();\n");
				fw.write("\t\t\ttaskEXIT_CRITICAL ();\n");
				fw.write("\n\t\t\t/*Runnable calls */\n");
				for (final Runnable run : runnablesOfTask) {
					fw.write("\t\t\t" + run.getName() + "();\n");
				}
				fw.write("\n\t\t\t/* Cout - Write back the local variables back to the actual variables */\n");
				fw.write("\t\t\ttaskENTER_CRITICAL ();\n");
				fw.write("\t\t\tcOUT_" + task.getName() + "();\n");
				fw.write("\t\t\ttaskEXIT_CRITICAL ();\n");
				final MappingModel mappingModel = model.getMappingModel();
				ProcessingUnit pu = null;
				if (mappingModel != null) {
					pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();
					Time taskTime = RuntimeUtil.getExecutionTimeForProcess(task, pu, null, TimeType.WCET);
					taskTime = TimeUtil.convertToTimeUnit(taskTime, TimeUnit.MS);
					final BigInteger sleepTime = taskTime.getValue();
					final BigInteger b2 = new BigInteger("1000");
					final int comparevalue = sleepTime.compareTo(b2);
					if (comparevalue < 0) {
						fw.write("\n\t\t\tsleepTimerMs(1 , 1" + (taskCount + 1) + ");\n");
					}
					else {
						fw.write("\n\t\t\tsleepTimerMs(" + sleepTime + ", " + taskCount + 1 + ");\n");
					}
					taskCount++;
				}
				fw.write("\n\t\t\ttaskCount" + task.getName() + "++;");
				fw.write("\n\t\t\ttraceTaskPasses(" + taskCount + ", taskCount" + task.getName() + ");");
				fw.write("\n\t\t\ttraceRunningTask(0);\n");
				final EList<Stimulus> Stimuli = model.getStimuliModel().getStimuli();
				for (final Stimulus s : Stimuli) {
					if (s instanceof PeriodicStimulus) {
						if (task.getStimuli().get(0) == s) {
							fw.write("\t\t\tvTaskDelayUntil(&xLastWakeTime, "
									+ ((PeriodicStimulus) s).getRecurrence().getValue() + ");\n");
						}
					}
				}
				fw.write("\t\t}\n");
				fw.write("\t}\n\n");
			}
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * Shared Label definition and initialization structure.
	 *
	 * @param file
	 * @param labellist
	 */
	public static List<Label> TaskSpecificLabel(final Amalthea model, final Task tasks) {
		List<Label> SharedLabelList = new ArrayList<Label>(SoftwareUtil.getAccessedLabelSet(tasks, null));
		SharedLabelList = SharedLabelList.stream().distinct().collect(Collectors.toList());
		final List<Label> SharedLabelListSortCore = new ArrayList<Label>();
		if (SharedLabelList.size() == 0) {
			// System.out.println("Shared Label size 0");
		}
		else {
			final HashMap<Label, HashMap<Task, ProcessingUnit>> sharedLabelTaskMap = LabelFileCreation
					.LabelTaskMap(model, SharedLabelList);
			for (final Label share : SharedLabelList) {
				final HashMap<Task, ProcessingUnit> TaskMap = sharedLabelTaskMap.get(share);
				final Set<Task> taskList = TaskMap.keySet();
				final Collection<ProcessingUnit> puList = TaskMap.values();
				final List<ProcessingUnit> puListUnique = puList.stream().distinct().collect(Collectors.toList());
				if (!((puListUnique.size() == 1 && taskList.size() > 1) || (puListUnique.size() > 1))) {
					SharedLabelListSortCore.add(share);
				}
			}
		}
		return SharedLabelListSortCore;
	}

	/**
	 * helper function to get the Amalthea Model
	 *
	 */
	public Amalthea getModel() {
		return this.model;
	}
}
