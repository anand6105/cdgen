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

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.amalthea.model.MappingModel;
import org.eclipse.app4mc.amalthea.model.Runnable;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.cdgen.utils.fileUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Implementation of Runnable Definition with Runnable specific delay.
 *
 */

public class ArmCodeFileCreation {
	final private Amalthea model;

	/**
	 * Constructor ArmCodeFileCreation.
	 *
	 * @param Model
	 * @param srcPath
	 * @param hdrPath
	 * @param configFlag
	 * @throws IOException
	 */
	public ArmCodeFileCreation(final Amalthea Model, final String srcPath, final String hdrPath, final int configFlag)
			throws IOException {
		this.model = Model;
		final EList<Task> tasks = this.model.getSwModel().getTasks();
		final EList<Runnable> runnables = this.model.getSwModel().getRunnables();
		System.out.println("Runnable File Creation Begins");
		fileCreate(this.model, srcPath, hdrPath, configFlag, tasks, runnables);
		System.out.println("Runnable File Creation Ends");

	}

	/**
	 * file creation and specification for armcode file
	 *
	 * @param model
	 * @param srcPath
	 * @param hdrPath
	 * @param configFlag
	 * @param tasks
	 * @param runnables
	 * @throws IOException
	 */
	private static void fileCreate(final Amalthea model, final String srcPath, final String hdrPath,
			final int configFlag, final EList<Task> tasks, final EList<Runnable> runnables) throws IOException {
		final String fname = srcPath + File.separator + "armcode.c";
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
		final FileWriter fw = new FileWriter(fn, true);
		try {
			if (0x3110 == (configFlag & 0xFFF0)) {
				fileUtil.fileMainHeader(f1);
				runFileHeader(f1);
				headerIncludesArmCode(f1);
				nsleep(f1);
				zynqmain(model, f1);
			}
			else {
				fileUtil.fileMainHeader(f1);
				runFileHeader(f1);
				headerIncludesArmCode(f1);
				nsleep(f1);
				zynqmain(model, f1);
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
	}

	/**
	 * ArmCode Header Title note.
	 *
	 * @param file
	 */
	private static void runFileHeader(final File file) {
		try {
			final File fn = file;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("*Title 		:   ArmCode Header\n");
			fw.write("*Description	:	Header file for Deploy/Offloading of the task to EPI\n");
			fw.write("******************************************************************\n");
			fw.write("******************************************************************/\n\n\n");

			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * nsleep - function call structure.
	 *
	 * @param file
	 */
	private static void nsleep(final File file) {
		try {
			final File fn = file;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("int nsleep(long miliseconds){\n");
			fw.write("\tstruct timespec req, rem;\n");
			fw.write("\tif(miliseconds > 999){\n");
			fw.write("\t\treq.tv_sec = (int)(miliseconds / 1000);\n");
			fw.write("\t\treq.tv_nsec = (miliseconds - ((long)req.tv_sec * 1000)) * 1000000;\n");
			fw.write("\t} else {\n");
			fw.write("\t\treq.tv_sec = 0;\n");
			fw.write("\t\treq.tv_nsec = miliseconds * 1000000;\n");
			fw.write("\t}\n");
			fw.write("\treturn nanosleep(&req , &rem);\n");
			fw.write("}\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * main function for the zynq in which we deploy the
	 *
	 * @param model
	 * @param file
	 */
	private static void zynqmain(final Amalthea model, final File file) {
		try {
			final File fn = file;
			final FileWriter fw = new FileWriter(fn, true);
			final MappingModel mappingModel = model.getMappingModel();
			if (mappingModel != null) {
				final EList<SchedulerAllocation> processingUnits = model.getMappingModel().getSchedulerAllocation();
				final ArrayList<SchedulerAllocation> localPU = new ArrayList<SchedulerAllocation>();
				localPU.addAll(processingUnits);
				final HashMap<SchedulerAllocation, Long> CoreMap = new HashMap<SchedulerAllocation, Long>();
				long count = 0;
				for (final SchedulerAllocation pu : localPU) {
					CoreMap.put(pu, count);
					count++;
				}
				fw.write("int main(){\n");
				fw.write("\tunsigned int shared_label_to_read;\n");
				fw.write("\tunsigned   row_loop,col_loop;\n");
				fw.write("\te_platform_t epiphany;\n");
				fw.write("\te_epiphany_t dev;\n");
				fw.write("\tint loop;\n");
				fw.write("\tint addr;\n");
				fw.write("\te_mem_t emem;\n");
				fw.write("\te_init(NULL);\n");
				fw.write("\te_reset_system();\n");
				fw.write("\te_get_platform_info(&epiphany);\n");
				int coreGroup = 0;
				if (processingUnits.size() == 1) {
					coreGroup = 0;
				}
				else if (processingUnits.size() > 1 && processingUnits.size() < 5) {
					coreGroup = 2;
				}
				else if (processingUnits.size() > 4 && processingUnits.size() < 10) {
					coreGroup = 3;
				}
				else if (processingUnits.size() > 9 && processingUnits.size() < 17) {
					coreGroup = 4;
				}
				fw.write("\te_open(&dev,0,0," + coreGroup + "," + coreGroup + ");\n");
				/* Core 1 */
				// 00
				/* Core 2 */
				// 00 10
				// 01 11
				/* Core 3 */
				// 00 10 20
				// 01 11 21
				// 02 12 22
				/* Core 4 */
				// 00 10 20 30
				// 01 11 21 31
				// 02 12 22 32
				// 03 13 23 33

				fw.write("\te_reset_group(&dev);\n");
				int k = 0;
				final ArrayList<String> result = new ArrayList<String>();
				for (int i = 0; i < localPU.size(); i++) {
					for (int j = 0; j < localPU.size(); j++) {
						if (k < localPU.size()) {
							fw.write("\te_return_stat_t\tresult" + k + ";\n");
							k++;
						}

					}
				}
				int k3 = 0;
				for (int rowCoreGroup = 0; rowCoreGroup < localPU.size(); rowCoreGroup++) {
					for (int columnCoreGroup = 0; columnCoreGroup < localPU.size(); columnCoreGroup++) {
						if (k3 < localPU.size()) {
							fw.write("\tunsigned int message" + k3 + "[9];\n");
							k3++;
						}
					}
				}
				int coreIndex = 0;
				for (int rowCoreGroup = 0; rowCoreGroup < coreGroup; rowCoreGroup++) {
					for (int columnCoreGroup = 0; columnCoreGroup < coreGroup; columnCoreGroup++) {
						if (coreIndex < localPU.size()) {
							fw.write("\tresult" + coreIndex + "=  e_load(\"main" + coreIndex + ".elf\",&dev,"
									+ rowCoreGroup + "," + columnCoreGroup + ",E_FALSE);\n");
							result.add("result" + coreIndex + "!=E_OK");
							coreIndex++;
						}
					}
				}
				String resultFinal = "";
				for (int k2 = 0; k2 < result.size(); k2++) {
					// System.out.println("Size ==> "+result.size());
					resultFinal = resultFinal + (result.get(k2) + "||");
					if (k2 == (result.size() - 2)) {
						k2++;
						resultFinal = resultFinal + result.get(k2);
						break;
					}
				}
				coreIndex = 0;
				for (int rowCoreGroup = 0; rowCoreGroup < localPU.size() & rowCoreGroup < 4; rowCoreGroup++) {
					for (int columnCore = 0; columnCore < localPU.size() & columnCore < 4; columnCore++) {
						if (coreIndex < localPU.size()) {
							if (coreIndex == 0) {
								fw.write("\tif (result" + coreIndex + "!=E_OK){\n");
							}
							else {
								fw.write("\telse if (result" + coreIndex + "!=E_OK){\n");
							}
							fw.write("\t\tfprintf(stderr,\"Error Loading the Epiphany Application " + coreIndex
									+ " %i\\n\", result" + coreIndex + ");");
							fw.write("\n\t}\n");
							coreIndex++;
						}
					}
				}
				fw.write("\te_start_group(&dev);\n");
				fw.write("\tfprintf(stderr,\"RMS Multicore on FreeRTOS started \\n\");\n");
				fw.write("\taddr = cnt_address;\n");
				fw.write("\tint pollLoopCounter = 0;\n");
				fw.write("\tint taskMessage;\n");
				fw.write("\tint prevtaskMessage;\n");
				fw.write("\tint prevpollLoopCounter = 0;\n");
				fw.write("\tfor (pollLoopCounter=0;pollLoopCounter<=40;pollLoopCounter++){\n");
				coreIndex = 0;
				for (int rowCoreGroup = 0; rowCoreGroup < coreGroup; rowCoreGroup++) {
					for (int columnCoreGroup = 0; columnCoreGroup < coreGroup; columnCoreGroup++) {
						if (coreIndex < localPU.size()) {
							fw.write("\t\te_read(&dev," + rowCoreGroup + "," + columnCoreGroup + ",addr, &message"
									+ coreIndex + ", sizeof(message" + coreIndex + "));\n");
							fw.write("\t\tfprintf(stderr, \"tick1 %3d||\",message" + coreIndex + "[8]+1);\n");
							fw.write("\t\tfprintf(stderr,\"task holding core" + coreIndex + " %2u||\", message"
									+ coreIndex + "[6]);\n");
							coreIndex++;
						}
					}
				}
				fw.write("\t\tfprintf(stderr,\"\\n\");\n");
				fw.write("\t\tusleep(READ_PRECISION_US);\n");
				fw.write("\t}\n");
				fw.write("\tfprintf(stderr,\"----------------------------------------------\\n\");\n");
				fw.write("\te_close(&dev);\n");
				fw.write("\te_finalize();\n");
				fw.write("\tfprintf(stderr,\"RMS Multicore on FreeRTOS Complete \\n \");\n");
				fw.write("\treturn 0;\n");
				fw.write("}\n");
				fw.close();
			}
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}


	/**
	 * header inclusion for armCode file
	 *
	 * @param file
	 */
	private static void headerIncludesArmCode(final File file) {
		try {
			final File fn = file;
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n");
			fw.write("#include <unistd.h>\n");
			fw.write("#include <e-hal.h>\n");
			fw.write("#include <time.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"debugFlags.h\"\n");
			fw.write("#define READ_PRECISION_US 1000\n\n\n");
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
