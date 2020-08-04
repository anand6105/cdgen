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
import java.util.stream.Collectors;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.Task;
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
	public SharedLabelsFileCreation(final Amalthea Model, final String srcPath, final int configFlag) throws IOException {
		this.model = Model;
		System.out.println("Shared Label File Creation Begins");
		fileCreate(this.model, srcPath, configFlag);
		System.out.println("Shared Label File Creation Ends");
	}

	/**
	 * FileCreation LabelFileCreation
	 *
	 * @param model
	 * @param srcPath
	 * @throws IOException
	 */
	private static void fileCreate(final Amalthea model, final String srcPath, final int configFlag) throws IOException {
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
		@SuppressWarnings("resource")
		final FileWriter fw = new FileWriter(fn1, true);
		try {
			fileUtil.fileMainHeader(f1);
			sharedLabelFileHeader(f1);
			headerIncludesSharedLabel(f1);
			SharedLabelDeclaration(f1, model, labellist, configFlag);
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
			headerIncludesSharedLabelHead(f3, configFlag);
			SharedLabelDeclarationHead(f3, model, labellist, configFlag);
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
			@SuppressWarnings("resource")
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
			@SuppressWarnings("resource")
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
	private static void headerIncludesSharedLabelHead(final File file, final int configFlag) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("#ifndef DEMO_PARALLELLA_SHARED_COMMS_H_\n");
			fw.write("#define DEMO_PARALLELLA_SHARED_COMMS_H_\n\n");

			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <stdint.h>\n\n");
			if (0x0001 == (0x0001 & configFlag)) {
				
			}
			fw.write("#include \"RTFParallellaConfig.h\"\n");
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
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include \"shared_comms.h\"\n\n\n");
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
	private static void SharedLabelDeclaration(final File file, final Amalthea model,
							final EList<Label> labellist, final int configFlag) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			if (0x0001 == (0x0001 & configFlag)) {
				fw.write("unsigned int *allocate_shared_memory(unsigned int offset)\n" + 
						"{\n" + 
						"\tunsigned int *dram_addr = 0;\n" + 
						"\tunsigned int *shdram_start_addr = (unsigned int *)SHARED_DRAM_SECTION;\n" + 
						"\t/* Add offset to get the address */\n" + 
						"\tdram_addr = (shdram_start_addr + offset);\n" + 
						"\treturn (unsigned int *)dram_addr;\n" + 
						"}\n\n\n");
			}
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
			final EList<Label> labellist, final int configFlag) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("unsigned int *allocate_shared_memory(unsigned int offset);\n\n");
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
					if (0x0001 == (0x0001 & configFlag)) {
						fw.write("\n#define shared_mem_section" + sh.toString().replace(" ", "") + "	0x8F001" + k + "00\n\n");
					}
					else {
						fw.write("\n#define shared_mem_section" + sh.toString().replace(" ", "") + "	0x0" + (k + 1)
								+ "000000\n\n");
					}

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
	 * helper function to get the Amalthea Model
	 *
	 */
	public Amalthea getModel() {
		return this.model;
	}
}
