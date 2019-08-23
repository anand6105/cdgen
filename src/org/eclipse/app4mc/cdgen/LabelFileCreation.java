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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.app4mc.amalthea.model.util.SoftwareUtil;
import org.eclipse.app4mc.cdgen.utils.fileUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Declaration of Labels with initial values .
 *
 *
 */

/**
 * @author rpras
 *
 */
public class LabelFileCreation {
	final private Amalthea model;

	/**
	 * Constructor LabelFileCreation
	 *
	 * @param Model
	 *            Amalthea Model
	 * @param srcPath
	 * @throws IOException
	 */
	public LabelFileCreation(final Amalthea Model, final String srcPath) throws IOException {
		this.model = Model;
		System.out.println("Label File Creation Begins");
		fileCreate(this.model, srcPath);
		System.out.println("Label File Creation Ends");
	}

	/**
	 * FileCreation LabelFileCreation
	 *
	 * @param model
	 * @param srcPath
	 * @throws IOException
	 */
	private static void fileCreate(final Amalthea model, final String srcPath) throws IOException {
		final EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
		int k = 0;
		for (final SchedulerAllocation c : CoreNo) {
			final ProcessingUnit pu = c.getResponsibility().get(0);
			final Set<Task> task = DeploymentUtil.getTasksMappedToCore(pu, model);
			final List<Task> tasks = new ArrayList<Task>(task);
			final List<Label> labelCoreCommonList = fileUtil.CoreSpecificLabel(model, tasks);
			final List<Label> sharedLabelList = fileUtil.SharedLabelDeclarationHead(model, tasks);
			final String fname1 = srcPath + File.separator + "label" + k + ".c";
			final String fname2 = srcPath + File.separator + "label" + k + ".h";
			final File f2 = new File(srcPath);
			final File f1 = new File(fname1);
			final File f3 = new File(fname2);
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
				labelFileHeader(f1);
				headerIncludesLabelHead(f1, k);
				LabelDeclaration(f1, tasks, labelCoreCommonList);
				LabelDeclarationLocal(f1, tasks, labelCoreCommonList, sharedLabelList);
			}
			finally {
				try {
					fw.close();
				}
				catch (final IOException e) {
					e.printStackTrace();
				}
			}

			try {
				fileUtil.fileMainHeader(f3);
				labelFileHeader(f3);
				headerIncludesLabel(f3);
				LabelDeclarationLocalHeader(f3, tasks, labelCoreCommonList);
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
	 * Title card - LabelFileCreation
	 *
	 * @param file
	 */
	private static void labelFileHeader(final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   Label Declaration\n");
			fw.write("*Description	:	Declaration and Initialisation of Label\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");

			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * Header inclusion - LabelFileCreation
	 *
	 * @param file
	 */
	private static void headerIncludesLabel(final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdint.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"shared_comms.h\"\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * Header inclusion - LabelFileCreation
	 *
	 * @param file
	 */
	private static void headerIncludesLabelHead(final File file, final int k) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdint.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"shared_comms.h\"\n");
			fw.write("#include \"label" + k + ".h\"\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}


	/**
	 * Label definition and initialization structure.
	 *
	 * @param file
	 * @param tasks
	 * @param labellist
	 */
	private static void LabelDeclaration(final File file, final List<Task> tasks, final List<Label> labellist) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			labellist.stream().distinct().collect(Collectors.toList());
			for (final Label label : labellist) {
				final String type = fileUtil.datatype(label.getSize().toString());
				final long init = fileUtil.intialisation(label.getSize().toString());
				fw.write("\t" + type + "	" + label.getName() + " \t=\t " + init + ";\n");
			}

			fw.write("\n\n\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	/**
	 * Local label definition and initialization structure. Task specific local
	 * labels are defined to perform Cin and Cout operation.
	 *
	 * @param file
	 * @param tasks
	 * @param sharedLabelList
	 */
	private static void LabelDeclarationLocal(final File file, final List<Task> tasks,
			final List<Label> labelCoreCommonList, final List<Label> sharedLabelList) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			for (final Task task : tasks) {
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(task, null);
				final ArrayList<Label> labellist1 = new ArrayList<Label>();
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				for (final Runnable run : runnablesOfTask) {
					final Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(run, null);
					labellist1.addAll(labellist);
				}
				final List<Label> listWithoutDuplicates2 = labellist1.stream().distinct().collect(Collectors.toList());
				final Set<Label> TaskLabel = SoftwareUtil.getAccessedLabelSet(task, null);
				fw.write("\n //local variable for " + task.getName() + "\n");
				for (final Label lab : listWithoutDuplicates2) {
					if ((TaskLabel.contains(lab)) && !(sharedLabelList.contains(lab))
							&& (labelCoreCommonList.contains(lab))) {
						final String type = fileUtil.datatype(lab.getSize().toString());
						final long init = fileUtil.intialisation(lab.getSize().toString());
						fw.write("\t\t" + type + "\t" + lab.getName() + "_" + task.getName() + "\t=\t" + init + ";\n");
					}
				}
				fw.write("\n //Shared label \n");
				for (final Label lab : sharedLabelList) {
					if (TaskLabel.contains(lab)) {
						final String type = fileUtil.datatype(lab.getSize().toString());
						final long init = fileUtil.intialisation(lab.getSize().toString());
						fw.write(
								"\t\t" + type + "\t" + lab.getName() + "_" + task.getName() + " \t=\t " + init + ";\n");
					}
				}
				final Set<Label> readLabels = SoftwareUtil.getReadLabelSet(task, null);
				final Set<Label> writeLabels = SoftwareUtil.getWriteLabelSet(task, null);
				fw.write("\n\tvoid cIN_" + task.getName() + "()\n\t{\n");
				List<String> readLabelType = new ArrayList<String>();
				final HashMap<String, HashMap<Label, Integer>> SharedLabelTypeMapIndexed = new HashMap<String, HashMap<Label, Integer>>();
				for (final Label share : sharedLabelList) {
					final String type = share.getSize().toString();
					readLabelType.add(type);
				}
				readLabelType = readLabelType.stream().distinct().collect(Collectors.toList());
				for (final String rLT : readLabelType) {

					final HashMap<Label, Integer> SharedLabelTypeMap = new HashMap<Label, Integer>();
					int k = 0;
					for (final Label share : sharedLabelList) {
						final String type = share.getSize().toString();
						if (type.equals(rLT)) {
							SharedLabelTypeMap.put(share, new Integer(k));
							k++;
						}
					}
					SharedLabelTypeMapIndexed.put(rLT, SharedLabelTypeMap);
				}
				HashMap<Label, Integer> LabelIndexedType = new HashMap<Label, Integer>();
				for (final String rLT : readLabelType) {
					LabelIndexedType = SharedLabelTypeMapIndexed.get(rLT);
					for (final Label share : sharedLabelList) {
						final String type = share.getSize().toString();
						if ((type.equals(rLT)) & (readLabels.contains(share))) {
							fw.write("\t\t" + share.getName() + "_" + task.getName() + "\t=\tshared_label_"
									+ type.replace(" ", "") + "_read(" + LabelIndexedType.get(share) + ");\n");
						}
					}
				}
				for (final Label lab : listWithoutDuplicates2) {
					if (labelCoreCommonList.contains(lab)) {
						fw.write("\t\t" + lab.getName() + "_" + task.getName() + "\t=\t" + lab.getName() + ";\n");
					}
				}
				fw.write("\t}\n");
				fw.write("\n\tvoid cOUT_" + task.getName() + "()\n\t{\n");
				final ArrayList<Label> labellist2 = new ArrayList<Label>();
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				for (final Runnable run : runnablesOfTask) {
					final Set<Label> labellist = SoftwareUtil.getWriteLabelSet(run, null);
					labellist2.addAll(labellist);
				}
				final List<Label> listWithoutDuplicates1 = labellist2.stream().distinct().collect(Collectors.toList());
				for (final Label lab : listWithoutDuplicates1) {
					if (labelCoreCommonList.contains(lab) & (writeLabels.contains(lab))) {
						fw.write("\t\t" + lab.getName() + "\t=\t" + lab.getName() + "_" + task.getName() + ";\n");
					}
				}
				HashMap<Label, Integer> LabelWriteIndexedType = new HashMap<Label, Integer>();
				for (final String rLT : readLabelType) {
					LabelWriteIndexedType = SharedLabelTypeMapIndexed.get(rLT);
					for (final Label share : sharedLabelList) {
						final String type = share.getSize().toString();
						if ((type.equals(rLT)) & (writeLabels.contains(share))) {
							fw.write("\t\t" + share.getName() + "_" + task.getName() + "++;\n");
							fw.write("\t\tshared_label_" + type.replace(" ", "") + "_write("
									+ LabelWriteIndexedType.get(share) + "," + share.getName() + "_" + task.getName()
									+ " );\n");
						}
					}
				}
				fw.write("\t}\n");
				fw.write("\n\n");
			}
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void LabelDeclarationLocalHeader(final File file, final List<Task> tasks,
			final List<Label> labelList) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			for (final Task task : tasks) {
				List<Runnable> runnablesOfTask = SoftwareUtil.getRunnableList(task, null);
				final ArrayList<Label> labellist1 = new ArrayList<Label>();
				runnablesOfTask = runnablesOfTask.stream().distinct().collect(Collectors.toList());
				for (final Runnable run : runnablesOfTask) {
					final Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(run, null);
					labellist1.addAll(labellist);
				}
				fw.write("\n\tvoid cIN_" + task.getName() + "();");
				fw.write("\n\tvoid cOUT_" + task.getName() + "();");
			}
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	public static List<Label> SharedLabelFinder(final Amalthea model) {
		final EList<Task> tasks = model.getSwModel().getTasks();
		final ArrayList<Label> labelCombined = new ArrayList<Label>();
		final ArrayList<Label> labelOhneDuplicate = new ArrayList<Label>();
		for (final Task task : tasks) {
			final Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(task, null);
			labelCombined.addAll(labellist);
		}
		final Set<Label> uniques = new HashSet<>();
		for (final Label t : labelCombined) {
			if (!uniques.add(t)) {
				labelOhneDuplicate.add(t);
			}
		}
		final List<Label> sharedLabelList = labelOhneDuplicate.stream().distinct().collect(Collectors.toList());
		return sharedLabelList;
	}

	@SuppressWarnings("null")
	public static HashMap<Label, HashMap<Task, ProcessingUnit>> LabelTaskMap(final Amalthea model,
			final List<Label> labelList) {
		final EList<Task> tasks = model.getSwModel().getTasks();
		final HashMap<Label, HashMap<Task, ProcessingUnit>> localLabelAllocation = new HashMap<Label, HashMap<Task, ProcessingUnit>>();

		for (final Label label : labelList) {
			final HashMap<Task, ProcessingUnit> localAllocation = new HashMap<Task, ProcessingUnit>();
			for (final Task task : tasks) {
				final ProcessingUnit pu = DeploymentUtil.getAssignedCoreForProcess(task, model).iterator().next();
				final ArrayList<Label> labelListLocalTask = new ArrayList<Label>(
						SoftwareUtil.getAccessedLabelSet(task, null));
				if (labelListLocalTask.contains(label)) {
					localAllocation.put(task, pu);
				}
			}
			if (localAllocation != null) {
				localLabelAllocation.put(label, localAllocation);
			}
		}
		return localLabelAllocation;
	}
}
