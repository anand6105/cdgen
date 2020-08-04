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
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil;
import org.eclipse.app4mc.amalthea.model.util.RuntimeUtil.TimeType;
import org.eclipse.app4mc.amalthea.model.util.SoftwareUtil;
import org.eclipse.app4mc.amalthea.model.util.TimeUtil;
import org.eclipse.app4mc.cdgen.utils.fileUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of Main function in which scheduling is done. Specific to RMS
 * Scheduler
 *
 *
 */

public class MainRMSFileCreation {
	final private Amalthea model;

	/**
	 * MainRMSFileCreation Constructor
	 *
	 * @param Model
	 * @param srcPath
	 * @param configFlag
	 * @throws IOException
	 */
	public MainRMSFileCreation(final Amalthea Model, final String srcPath, final int configFlag) throws IOException {
		this.model = Model;
		System.out.println("Main File Creation Begins");
		fileCreate(this.model, srcPath, configFlag);
		System.out.println("Main File Creation Ends");
	}

	/**
	 * MainRMSFileCreation - File Creation
	 *
	 * @param model
	 * @param srcPath
	 * @param configFlag
	 * @throws IOException
	 */
	private static void fileCreate(final Amalthea model, final String srcPath, final int configFlag)
			throws IOException {
		final EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		int k = 0;
		for (final SchedulerAllocation c : CoreNo) {
			final ProcessingUnit pu = c.getResponsibility().get(0);
			final Set<Task> tasks = DeploymentUtil.getTasksMappedToCore(pu, model);
			final String fname = srcPath + File.separator + "main" + k + ".c";
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
				mainFileHeader(f1);
				if ((0x0100 == (0x0F00 & configFlag)) & (0x3000 == (0xF000 & configFlag))) {
					if (0x0001 == (0x0001 & configFlag)) {
						headerIncludesMainRMS(f1, k, true);
						mainTaskStimuli(model, f1, tasks);
						mainTaskPriority(f1, tasks);
						mainFucntionRMS(model, f1, tasks, true);
						// SharedLabelDeclarationHead(f1, model);
					}
					else {
						headerIncludesMainRMS(f1, k, false);
						mainTaskStimuli(model, f1, tasks);
						mainTaskPriority(f1, tasks);
						mainFucntionRMS(model, f1, tasks, false);
						// SharedLabelDeclarationHead(f1, model);
					}

				}
				else {
					headerIncludesMainFreeRTOS(f1, k);
					mainTaskStimuli(model, f1, tasks);
					mainTaskPriority(f1, tasks);
					mainFucntionFreeRTOS(model, f1, tasks);
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

	/**
	 * Main function in Main file of RMS specific scheduler
	 *
	 * @param model
	 * @param file
	 * @param tasks
	 */
	private static void mainFucntionRMS(final Amalthea model, final File file, final Set<Task> tasks, final boolean btfEnable) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("int main(void) \n{\n");
			if (btfEnable == true) {
				fw.write("\tinit_btf_mem_section();\n");
				fw.write("\tinit_task_trace_buffer();\n");
				fw.write("\tint ts = get_time_scale_factor();\n");
			}
			else {
				fw.write("\toutbuf_init();\n");
			}

			final List<Label> SharedLabelList = LabelFileCreation.SharedLabelFinder(model);
			final List<Label> SharedLabelListSortCore = new ArrayList<Label>();
			if (SharedLabelList.size() == 0) {
				System.out.println("Shared Label size 0");
			}
			else {
				final HashMap<Label, HashMap<Task, ProcessingUnit>> sharedLabelTaskMap = LabelFileCreation
						.LabelTaskMap(model, SharedLabelList);
				for (final Label share : SharedLabelList) {
					final HashMap<Task, ProcessingUnit> TaskMap = sharedLabelTaskMap.get(share);
					final Collection<ProcessingUnit> puList = TaskMap.values();
					final List<ProcessingUnit> puListUnique = puList.stream().distinct().collect(Collectors.toList());
					if (puListUnique.size() > 1) {
						SharedLabelListSortCore.add(share);
					}
				}
			}

			final HashMap<Label, String> SharedLabelTypeMap = new HashMap<Label, String>();
			for (final Label share : SharedLabelListSortCore) {
				SharedLabelTypeMap.put(share, share.getSize().toString());
			}
			final List<String> SharedTypeMapList = new ArrayList<>(
					SharedLabelTypeMap.values().stream().distinct().collect(Collectors.toList()));
			final List<Label> SharedLabelMapList = new ArrayList<Label>(SharedLabelTypeMap.keySet());
			for (int k = 0; k < SharedTypeMapList.size(); k++) {
				final List<Label> SharedLabel = new ArrayList<Label>();
				final String sh = SharedTypeMapList.get(k);
				for (final Label s : SharedLabelMapList) {
					final String ShTy = SharedLabelTypeMap.get(s);
					if (sh.equals(ShTy)) {
						SharedLabel.add(s);
					}
				}
				int SharedLabelCounter = SharedLabel.size();
				if (SharedLabelCounter != 0) {
					fw.write("\tshared_label_" + sh.toString().replace(" ", "") + "_init();\n");
				}
				SharedLabelCounter = 0;
			}

			for (final Task task : tasks) {
				final MappingModel mappingModel = model.getMappingModel();
				ProcessingUnit pu = null;
				if (mappingModel != null) {
					pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();
					Time taskTime = RuntimeUtil.getExecutionTimeForProcess(task, pu, null, TimeType.WCET);
					taskTime = TimeUtil.convertToTimeUnit(taskTime, TimeUnit.MS);
					final BigInteger sleepTime = taskTime.getValue();
					final BigInteger b2 = new BigInteger("1000");
					final int comparevalue = sleepTime.compareTo(b2);
					if (btfEnable == true)
					{
						if (comparevalue < 0) {
							fw.write("\tAmaltheaTask AmalTk_" + task.getName() + " = createAmaltheaTask( v" + task.getName()
									+ ", cIN_" + task.getName() + ", cOUT_" + task.getName() + ",\n\t\t\t "
									+ task.getStimuli().get(0).getName() + "*ts, " + task.getStimuli().get(0).getName()
									+ "*ts, 1*ts, 0, 0, "+ pu.getName()+"_ID, 0);\n");
						}
						else {
							fw.write("\tAmaltheaTask AmalTk_" + task.getName() + " = createAmaltheaTask( v" + task.getName()
									+ ", cIN_" + task.getName() + ", cOUT_" + task.getName() + ",\n\t\t\t "
									+ task.getStimuli().get(0).getName() + "*ts, " + task.getStimuli().get(0).getName() + "*ts, "
									+ sleepTime + "*ts, 0, 0, "+ pu.getName()+"_ID, 0);\n");
						}
					}
					else {
						if (comparevalue < 0) {
							fw.write("\tAmaltheaTask AmalTk_" + task.getName() + " = createAmaltheaTask( v" + task.getName()
									+ ", cIN_" + task.getName() + ", cOUT_" + task.getName() + ", "
									+ task.getStimuli().get(0).getName() + ", " + task.getStimuli().get(0).getName()
									+ ", 1);\n");
						}
						else {
							fw.write("\tAmaltheaTask AmalTk_" + task.getName() + " = createAmaltheaTask( v" + task.getName()
									+ ", cIN_" + task.getName() + ", cOUT_" + task.getName() + ", "
									+ task.getStimuli().get(0).getName() + ", " + task.getStimuli().get(0).getName() + ", "
									+ sleepTime + ");\n");
						}
					}
				}
			}
			
			if (btfEnable == true) {
				final List<Task> localTaskPriority = new ArrayList<Task>();
				localTaskPriority.addAll(tasks);
				@SuppressWarnings("resource")
				final HashMap<Task, Long> periodMap = new HashMap<Task, Long>();
				for (final Task task : tasks) {
					final long period = fileUtil.getRecurrence(task).getValue().longValue();
					periodMap.put(task, period);
				}
				final Map<Task, Long> periodMapSorted = fileUtil.sortByValue(periodMap);
				for (int i = (periodMapSorted.size()), k = 0; i > 0; i--, k++) {
					final Task task = (Task) periodMapSorted.keySet().toArray()[k];
					final Set<Label> taskLabel = SoftwareUtil.getAccessedLabelSet(task, null);
					final List<Label> taskLabelList = new ArrayList<>(taskLabel);
					final HashMap<Label, String> LabelTypeMap = new HashMap<Label, String>();
					for (final Label tl : taskLabelList) {
						LabelTypeMap.put(tl, tl.getSize().toString());
					}
					final List<String> TypeList = new ArrayList<>(
							LabelTypeMap.values().stream().distinct().collect(Collectors.toList()));
					final List<Label> LabelList = new ArrayList<>(
							LabelTypeMap.keySet().stream().distinct().collect(Collectors.toList()));
					fw.write("\tcreateRTOSTask( &AmalTk_" + task.getName() + ", main" + task.getName() + ", "
							+ TypeList.size() + ",");
					final List<Label> dataTypeList = new ArrayList<Label>();
					int j = 0;
					for (final String tl : TypeList) {
						fw.write(fileUtil.datatypeSize(tl) + ", ");
						for (final Label La : LabelList) {
							if (LabelTypeMap.get(La).contains(tl) && (SharedLabelListSortCore.contains(La))) {
								dataTypeList.add(La);
							}
						}
						fw.write("" + dataTypeList.size() + "");
						j++;
						if (j < TypeList.size()) {
							fw.write(", ");
						}
					}
					fw.write(");\n");
				}
			}
			else {
				for (final Task task : tasks) {
					final Set<Label> taskLabel = SoftwareUtil.getAccessedLabelSet(task, null);
					final List<Label> taskLabelList = new ArrayList<>(taskLabel);
					final HashMap<Label, String> LabelTypeMap = new HashMap<Label, String>();
					for (final Label tl : taskLabelList) {
						LabelTypeMap.put(tl, tl.getSize().toString());
					}
					final List<String> TypeList = new ArrayList<>(
							LabelTypeMap.values().stream().distinct().collect(Collectors.toList()));
					final List<Label> LabelList = new ArrayList<>(
							LabelTypeMap.keySet().stream().distinct().collect(Collectors.toList()));
					fw.write("\tcreateRTOSTask( &AmalTk_" + task.getName() + ", main" + task.getName() + ", "
							+ TypeList.size() + ",");
					final List<Label> dataTypeList = new ArrayList<Label>();
					int k = 0;
					for (final String tl : TypeList) {
						fw.write(fileUtil.datatypeSize(tl) + ", ");
						for (final Label La : LabelList) {
							if (LabelTypeMap.get(La).contains(tl) && (SharedLabelListSortCore.contains(La))) {
								dataTypeList.add(La);
							}
						}
						fw.write("" + dataTypeList.size() + "");
						k++;
						if (k < TypeList.size()) {
							fw.write(", ");
						}
					}
					fw.write(");\n");
				}
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


	/**
	 * Main function in Main file of RMS specific scheduler
	 *
	 * @param model
	 * @param file
	 * @param tasks
	 */
	private static void mainFucntionFreeRTOS(final Amalthea model, final File file, final Set<Task> tasks) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("int main(void) \n{\n");
			fw.write("\toutbuf_init();\n");
			final List<Label> SharedLabelList = LabelFileCreation.SharedLabelFinder(model);
			final List<Label> SharedLabelListSortCore = new ArrayList<Label>();
			if (SharedLabelList.size() == 0) {
				System.out.println("Shared Label size 0");
			}
			else {
				// System.out.println("Shared Label size
				// "+SharedLabelList.size());
				final HashMap<Label, HashMap<Task, ProcessingUnit>> sharedLabelTaskMap = LabelFileCreation
						.LabelTaskMap(model, SharedLabelList);
				for (final Label share : SharedLabelList) {
					final HashMap<Task, ProcessingUnit> TaskMap = sharedLabelTaskMap.get(share);
					final Collection<ProcessingUnit> puList = TaskMap.values();
					final List<ProcessingUnit> puListUnique = puList.stream().distinct().collect(Collectors.toList());
					if (puListUnique.size() > 1) {
						SharedLabelListSortCore.add(share);
					}
				}
			}

			final HashMap<Label, String> SharedLabelTypeMap = new HashMap<Label, String>();
			for (final Label share : SharedLabelListSortCore) {
				SharedLabelTypeMap.put(share, share.getSize().toString());
			}
			final List<String> SharedTypeMapList = new ArrayList<>(
					SharedLabelTypeMap.values().stream().distinct().collect(Collectors.toList()));
			final List<Label> SharedLabelMapList = new ArrayList<Label>(SharedLabelTypeMap.keySet());
			for (int k = 0; k < SharedTypeMapList.size(); k++) {
				final List<Label> SharedLabel = new ArrayList<Label>();
				final String sh = SharedTypeMapList.get(k);
				for (final Label s : SharedLabelMapList) {
					final String ShTy = SharedLabelTypeMap.get(s);
					if (sh.equals(ShTy)) {
						SharedLabel.add(s);
					}
				}
				int SharedLabelCounter = SharedLabel.size();
				if (SharedLabelCounter != 0) {
					fw.write("\tshared_label_" + sh.toString().replace(" ", "") + "_init();\n");
					// fw.write("void shared_label_"+sh.toString().replace(" ",
					// "")+"_init_core();\n");

				}
				SharedLabelCounter = 0;
			}

			for (final Task task : tasks) {
				fw.write("\txTaskCreate( v" + task.getName() + " , \"" + task.getName()
						+ "\", configMINIMAL_STACK_SIZE, &v" + task.getName() + ", main" + task.getName()
						+ ", NULL);\n");
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


	/**
	 * Title Card of MainRMSFileCreation
	 *
	 * @param file
	 */
	private static void mainFileHeader(final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
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


	/**
	 * MainRMSFileCreation Header inclusion
	 *
	 * @param file
	 */
	private static void headerIncludesMainRMS(final File file, final int k, final boolean btfEnable) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
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
			fw.write("#include \"ParallellaUtils.h\"\n");
			fw.write("#include \"taskDef" + k + ".h\"\n");
			fw.write("#include \"shared_comms.h\"\n\n");
			fw.write("#include \"label" + k + ".h\"\n");
			if (btfEnable ==  true) {
				fw.write("#include \"RTFParallellaConfig.h\"\n");
			}
			// fw.write("#include \"c2c.h\"\n\n");
			// fw.write("#define READ_PRECISION_US 1000\n\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * Assign Priority to task in RMS
	 *
	 * @param file
	 * @param tasks
	 */
	private static void mainTaskPriority(final File file, final Set<Task> tasks) {
		try {
			final File fn = file;
			final List<Task> localTaskPriority = new ArrayList<Task>();
			localTaskPriority.addAll(tasks);
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* TaskPriorities. */\n");
			final HashMap<Task, Long> periodMap = new HashMap<Task, Long>();
			for (final Task task : tasks) {
				final long period = fileUtil.getRecurrence(task).getValue().longValue();
				periodMap.put(task, period);
			}
			final Map<Task, Long> periodMapSorted = fileUtil.sortByValue(periodMap);
			// System.out.println("periodMapSorted Size "+
			// periodMapSorted.size());
			// for (int i=0;i<(periodMapSorted.size());i++) {
			for (int i = (periodMapSorted.size()), k = 0; i > 0; i--, k++) {
				final Task task = (Task) periodMapSorted.keySet().toArray()[k];
				fw.write("\t#define main" + task.getName() + "\t( tskIDLE_PRIORITY +" + (i) + " )\n");
			}
			fw.write("\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * MainRMSFileCreation Header inclusion
	 *
	 * @param file
	 */
	private static void headerIncludesMainFreeRTOS(final File file, final int k) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
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
			fw.write("#include \"ParallellaUtils.h\"\n");
			fw.write("#include \"taskDef" + k + ".h\"\n");
			fw.write("#include \"shared_comms.h\"\n\n");
			fw.write("#include \"label" + k + ".h\"\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * Macro for Stimuli in the model
	 *
	 * @param model
	 * @param file
	 * @param tasks
	 */
	private static void mainTaskStimuli(final Amalthea model, final File f1, final Set<Task> tasks) {
		try {
			final File fn = f1;

			@SuppressWarnings("resource")
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
}
