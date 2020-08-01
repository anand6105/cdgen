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
import java.util.Set;

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.Label;
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.app4mc.cdgen.utils.fileUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Declaration of Labels with initial values .
 *
 *
 */

/**
 * @author anand prakash
 *
 */
public class ModelEnumFileCreation {
	final private Amalthea model;

	/**
	 * Constructor ModelFileCreation
	 *
	 * @param Model
	 *            Amalthea Model
	 * @param srcPath
	 * @throws IOException
	 */
	public ModelEnumFileCreation(final Amalthea Model, final String srcPath) throws IOException {
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
		final EList<Label> labellist = model.getSwModel().getLabels();
		final String fname1 = srcPath + File.separator + "model_enumerations.c";
		final String fname2 = srcPath + File.separator + "model_enumerations.h";
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
			modelEnumFileHeader(f1);
			headerIncludesModelEnum(f1);
			modelEnumDeclaration(f1, model, labellist);
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
			modelEnumFileHeaderHead(f3);
			headerIncludesModelEnumHead(f3);
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
	private static void modelEnumFileHeader(final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   Model Enumaration Declaration\n");
			fw.write("*Description	:	Declaration of model containing tasks and hardware interface\n");
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
	private static void modelEnumFileHeaderHead(final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   Model Enum Declaration\n");
			fw.write("*Description	:	Header file for Declaration of Amalthea model\n");
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
	private static void headerIncludesModelEnumHead(final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("#ifndef SRC_PARALLELLA_MODEL_ENUMERATIONS_H_\n");
			fw.write("#define SRC_PARALLELLA_MODEL_ENUMERATIONS_H_\n\n");

			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <stdint.h>\n\n\n\n");
			fw.write("#define SHM_LABEL_COUNT                   10\n\n");
			fw.write("#define DSHM_LABEL_COUNT                  10\n\n");
			fw.write("#define LABEL_STRLEN                      32\n\n");
			fw.write("#define EXEC_TASK_COUNT                    5\n\n");
			fw.write("#define EXEC_CORE_COUNT                    2\n\n");
			fw.write("#define SHM_VISIBLE_LABEL_COUNT            2\n\n");
			fw.write("#define DSHM_VISIBLE_LABEL_COUNT           2\n\n");
			fw.write("void generate_task_entity_table(void);\n\n");
			fw.write("void generate_runnable_entity_table(void);\n\n");
			fw.write("void generate_signal_entity_table(void);\n\n");
			fw.write("void generate_hw_entity_table(void);\n\n");
			fw.write("#endif\n\n");
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
	private static void headerIncludesModelEnum(final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include \"model_enumerations.h\"\n");
			fw.write("#include \"RTFParallellaConfig.h\"\n");
			fw.write("#include \"trace_utils_BTF.h\"\n");
			fw.write("\n\n\n");
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
	private static void modelEnumDeclaration(final File file, final Amalthea model, final EList<Label> labellist) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			FileWriter fw = new FileWriter(fn, true);
			fw.write("static const char task_enum [][LABEL_STRLEN] =\n");
			fw.write("{\n");
			fw.write("\t\"[idle]\",\n");
			fw.close();
			final EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
			for (final SchedulerAllocation c : CoreNo) {
				final ProcessingUnit pu = c.getResponsibility().get(0);
				final Set<Task> tasks = DeploymentUtil.getTasksMappedToCore(pu, model);
				writeTaskEnum(model, file, tasks);
			}
			fw = new FileWriter(fn, true);
			fw.write("};\n\n");
			fw.write("static const char hw_enum[][LABEL_STRLEN] =\n");
			fw.write("{\n");
			fw.close();
			writeHwCoreEnum(model, file);
			fw = new FileWriter(fn, true);
			fw.write("};\n\n");
			fw.write("void generate_runnable_entity_table(void)\n");
			fw.write("{\n");
			fw.write("}\n\n");
			fw.write("void generate_signal_entity_table(void)\n");
			fw.write("{\n");
			fw.write("}\n\n");
			fw.write("void generate_task_entity_table(void)\n");
			fw.write("{\n");
			fw.write("\tstore_entity_entry(IDLE_TASK_ID, TASK_EVENT, task_enum[0]);\n");
			fw.write("\tint index = 1;\n");
			fw.write("\tint task_count = sizeof(task_enum)/sizeof(task_enum[0]);\n" + 
					"\tfor(index = 1; index <= task_count; index++) {\n");
			fw.write("\t\tstore_entity_entry(index , TASK_EVENT, task_enum[index]);\n");
			fw.write("\t}\n}\n\n\n");
			fw.write("void generate_hw_entity_table(void)\n");
			fw.write("{\n");
			fw.write("\tint index = 0;\n");
			for (final SchedulerAllocation c : CoreNo) {
				final ProcessingUnit pu = c.getResponsibility().get(0);
				fw.write("\tint core_id = " + pu.getName() + "_ID;\n");
				break;
			}
			fw.write("\tint hw_count = sizeof(hw_enum)/sizeof(hw_enum[0]);\n" + 
					"\tfor(index = 0; index < hw_count; index++) {\n");
			fw.write("\tstore_entity_entry(core_id , TASK_EVENT, hw_enum[index]);\n");
			fw.write("\tcore_id++;\n");
			fw.write("\t}\n}\n\n\n");
			fw.write("\n\n\n\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	
	/**
	 * Function to write the tasks
	 *
	 * @param model
	 * @param file
	 * @param tasks
	 */
	private static void writeTaskEnum(final Amalthea model, final File file, final Set<Task> tasks) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			for (final Task task : tasks) {
				fw.write("\t\"" + task.getName() + "\",\n");
			}
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}
	
	
	
	
	
	/**
	 * Function to write the hardware cores
	 *
	 * @param model
	 * @param file
	 * @param tasks
	 */
	private static void writeHwCoreEnum(final Amalthea model, final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			final EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
			for (final SchedulerAllocation c : CoreNo) {
				final ProcessingUnit pu = c.getResponsibility().get(0);
				fw.write("\t\"" + pu.getName() + "\",\n");
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

