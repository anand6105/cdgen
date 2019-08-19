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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.util.SoftwareUtil;
import org.eclipse.app4mc.cdgen.utils.fileUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Declaration of Labels with initial values .
 *
 * @author Ram Prasath Govindarajan
 *
 */

/**
 * @author rpras
 *
 */
public class SharedLabelsFileCreation {
	final private Amalthea model;

	/**
	 * Constructor LabelFileCreation
	 *
	 * @param Model
	 *            Amalthea Model
	 * @param srcPath
	 * @throws IOException
	 */
	public SharedLabelsFileCreation(final Amalthea Model, final String srcPath) throws IOException {
		this.model = Model;
		System.out.println("Shared Label File Creation Begins");
		fileCreate(this.model, srcPath);
		System.out.println("Shared Label File Creation Ends");
	}

	/**
	 * FileCreation LabelFileCreation
	 *
	 * @param model
	 * @param srcPath
	 * @throws IOException
	 */
	private static void fileCreate(final Amalthea model, final String srcPath) throws IOException {
		final EList<Task> tasks = model.getSwModel().getTasks();
		final EList<Label> labellist = model.getSwModel().getLabels();
		final String fname1 = srcPath + File.separator + "shared_comms.c";
		final String fname2 = srcPath + File.separator + "shared_comms.h";
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

		final File fn1 = f1;
		final File fn2 = f3;

		final FileWriter fw = new FileWriter(fn1, true);
		try {
			fileUtil.fileMainHeader(f1);
			sharedLabelFileHeader(f1);
			headerIncludesSharedLabel(f1);
			SharedLabelDeclaration(f1, model, labellist);
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
			sharedLabelFileHeaderHead(f3);
			headerIncludesSharedLabelHead(f3);
			SharedLabelDeclarationHead(f3, model, labellist);
		}
		finally {
			try {
				fw.close();
			}
			catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Title card - LabelFileCreation
	 *
	 * @param file
	 */
	private static void sharedLabelFileHeader(final File file) {
		try {
			final File fn = file;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   Shared Label Declaration\n");
			fw.write("*Description	:	Declaration and Initialisation of Shared Label\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");

			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * Title card - LabelFileCreation
	 *
	 * @param file
	 */
	private static void sharedLabelFileHeaderHead(final File file) {
		try {
			final File fn = file;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   Shared Label Declaration\n");
			fw.write("*Description	:	Header file for Declaration and Initialisation of Shared Label\n");
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
	private static void headerIncludesSharedLabelHead(final File file) {
		try {
			final File fn = file;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("#ifndef DEMO_PARALLELLA_SHARED_COMMS_H_\n");
			fw.write("#define DEMO_PARALLELLA_SHARED_COMMS_H_\n\n");

			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <stdint.h>\n\n");
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
	private static void headerIncludesSharedLabel(final File file) {
		try {
			final File fn = file;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include \"shared_comms.h\"\n");
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
	private static void SharedLabelDeclaration(final File file, final Amalthea model, final EList<Label> labellist) {
		try {
			final File fn = file;
			final FileWriter fw = new FileWriter(fn, true);
			final List<Label> SharedLabelList = LabelFileCreation.SharedLabelFinder(model);
			final List<Label> SharedLabelListSortCore = new ArrayList<Label>();
			if (SharedLabelList.size() == 0) {
				// System.out.println("Shared Label size 0");
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


					fw.write(fileUtil.datatype(sh.toString()) + " 	*outbuf_shared" + sh.toString().replace(" ", "")
							+ "[" + SharedLabelCounter + "];\n\n");
					fw.write("void shared_label_" + sh.toString().replace(" ", "") + "_init(){\n");
					fw.write("\toutbuf_shared" + sh.toString().replace(" ", "") + "[0] = ("
							+ fileUtil.datatype(sh.toString()) + " *) shared_mem_section"
							+ sh.toString().replace(" ", "") + ";\n");
					for (int i = 1; i < SharedLabelCounter; i++) {
						fw.write("\toutbuf_shared" + sh.toString().replace(" ", "") + "[" + i + "]=outbuf_shared"
								+ sh.toString().replace(" ", "") + "[" + i + "] + 1;\n");
					}
					fw.write("\tfor (int i=0;i<" + SharedLabelCounter + ";i++){\n");
					fw.write("\t\t*outbuf_shared" + sh.toString().replace(" ", "") + "[i] =0;\n");
					fw.write("\t}\n");
					fw.write("}\n\n");
					fw.write("void shared_label_" + sh.toString().replace(" ", "")
							+ "_write(int label_indx,int payload){\n");
					// fw.write("\t"+fileUtil.datatype(sh.toString())+"
					// retval=NULL;\n");
					fw.write("\t*outbuf_shared" + sh.toString().replace(" ", "") + "[label_indx] = payload;\n");
					// fw.write("\treturn retval;\n\n");
					fw.write("}\n\n");
					fw.write("" + fileUtil.datatype(sh.toString()) + " shared_label_" + sh.toString().replace(" ", "")
							+ "_read(int label_indx){\n");
					fw.write("\treturn *outbuf_shared" + sh.toString().replace(" ", "") + "[label_indx];\n");
					fw.write("}\n\n");
				}
				SharedLabelCounter = 0;
			}
			fw.write("\n\n\n\n");
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
	private static void SharedLabelDeclarationHead(final File file, final Amalthea model,
			final EList<Label> labellist) {
		try {
			final File fn = file;
			final FileWriter fw = new FileWriter(fn, true);
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
					fw.write("\n#define shared_mem_section" + sh.toString().replace(" ", "") + "	0x0" + (k + 1)
							+ "000000\n\n");
					fw.write("void shared_label_" + sh.toString().replace(" ", "") + "_init();\n");
					fw.write("void shared_label_" + sh.toString().replace(" ", "")
							+ "_write(int label_indx,int payload);\n");
					fw.write("" + fileUtil.datatype(sh.toString()) + " shared_label_" + sh.toString().replace(" ", "")
							+ "_read(int label_indx);\n");
				}
				SharedLabelCounter = 0;
			}
			fw.write("\n\n");
			fw.write("#endif");
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
	 */
	private static void LabelDeclarationLocal(final File file, final EList<Task> tasks) {
		try {
			final File fn = file;
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
				fw.write("\n //local variable for " + task.getName() + "\n");
				for (final Label lab : listWithoutDuplicates2) {
					final String type = fileUtil.datatype(lab.getSize().toString());
					final long init = fileUtil.intialisation(lab.getSize().toString());
					fw.write("\t\t" + type + "\t" + lab.getName() + "\t=\t" + init + ";\n");
				}
				fw.write("\n\n");
			}
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	private static void SharedLabelFinder(final File file, final EList<Task> tasks) {
		final ArrayList<Label> labelCombined = new ArrayList<Label>();
		final ArrayList<Label> labelOhneDuplicate = new ArrayList<Label>();
		List<Label> labelNoDup = new ArrayList<Label>();
		for (final Task task : tasks) {
			final Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(task, null);
			labelCombined.addAll(labellist);
		}

		// Set of label types
		final List<String> labeltype = new ArrayList<String>();
		labelNoDup = labelCombined.stream().distinct().collect(Collectors.toList());
		final HashMap<Label, String> LabelTypeMap = new HashMap<Label, String>();
		for (final Label La : labelNoDup) {
			final String LaType = La.getSize().toString();
			labeltype.add(La.getSize().toString());
			LabelTypeMap.put(La, LaType);
		}
		final List<String> labeltype1 = labeltype.stream().distinct().collect(Collectors.toList());
		for (final String l : labeltype1) {
			// System.out.println("\n==>"+l+labeltype1.size());
		}
		System.out.println("Key Set " + LabelTypeMap.size());
		// Categories based on type
		// List<String> labelDatatypeList = new ArrayList<String>();
		final HashMap<String, List<Label>> LabelType = new HashMap<String, List<Label>>();
		// Map<String, <List>String> doubleBraceMap = new HashMap<String,
		// <List>String>() {
		final Set<String> labelDatatypeList = LabelType.keySet();
		final int n = labelDatatypeList.size();
		final ArrayList<Label>[] al = new ArrayList[n];
		for (final Label La : labelNoDup) {
			final String LaType = La.getSize().toString();
			// LabelType.add(La.getSize().toString());
			// LabelType.put(LaType,La);
		}
		final Iterator<String> iterator = labelDatatypeList.iterator();
		// initializing
		int i = 0;
		while (iterator.hasNext()) {
			final String element = iterator.next();
			al[i] = new ArrayList<Label>();
			for (final Label La : labelNoDup) {
				if (La.getDataType().toString() == element) {
					al[i].add(La);
				}
			}
			i++;
		}
		for (int j = 0; j < LabelType.size(); j++) {
			System.out.println("\nDataType " + j + " " + al[j].size());
		}
		// Shared labels list
		final Set<Label> uniques = new HashSet<>();
		for (final Label t : labelCombined) {
			if (!uniques.add(t)) {
				labelOhneDuplicate.add(t);
				// System.out.println("\n==>"+t.getName());
			}
		}
		final List<Label> sharedLabelList = labelOhneDuplicate.stream().distinct().collect(Collectors.toList());
		for (final Label l : sharedLabelList) {
			// System.out.println("\n==>"+l.getName());
		}
	}

	private static void SharedLabelFinder1(final File file, final EList<Task> tasks) {
		final ArrayList<Label> labelCombined = new ArrayList<Label>();
		// ArrayList<Label> labelTypeList = new ArrayList<Label>();
		List<Label> labelNoDup = new ArrayList<Label>();
		for (final Task task : tasks) {
			final Set<Label> labellist = SoftwareUtil.getAccessedLabelSet(task, null);
			labelCombined.addAll(labellist);
		}
		labelNoDup = labelCombined.stream().distinct().collect(Collectors.toList());
		final HashMap<String, Label> LabelType1 = new HashMap<String, Label>();
		for (final Label la : labelNoDup) {
			final String latype = la.getSize().toString();
			LabelType1.put(latype, la);
		}
		final HashMap<String, List<Label>> LabelTypeMap = new HashMap<String, List<Label>>();
		final Set<String> regex = LabelType1.keySet();
		final Iterator<String> itr = regex.iterator();
		while (itr.hasNext()) {
			final ArrayList<Label> labelTypeList = new ArrayList<Label>();
			final String maptypeset = itr.next();
			for (final Label la : labelNoDup) {
				final String latype = la.getSize().toString();
				if (latype.equals(maptypeset)) {
					labelTypeList.add(la);
				}

			}
			LabelTypeMap.put(itr.next(), labelTypeList);
		}
		/*
		 * for(int k=0; k<LabelType.size();k++) {
		 * 
		 * }
		 */

	}


	/**
	 * helper function to get the Amalthea Model
	 *
	 */
	public Amalthea getModel() {
		return this.model;
	}
}
