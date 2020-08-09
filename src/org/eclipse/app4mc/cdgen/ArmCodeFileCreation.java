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
		@SuppressWarnings("resource")
		final FileWriter fw = new FileWriter(fn, true);
		try {
			fileUtil.fileMainHeader(f1);
			runFileHeader(f1);
			if (0x3110 == (configFlag & 0xFFF0)) {
				if (0x0001 == (0x0001 & configFlag)) {
					headerIncludesArmCodeforBTF(f1);
					constructTraceHeader(f1);
					parseTraceData(f1);
					zynqmainBTF(model, f1);
				} 
				else {
					headerIncludesArmCode(f1);
					nsleep(f1);
					zynqmain(model, f1);
				}

			}
			else {
				if (0x0001 == (0x0001 & configFlag)) {
					headerIncludesArmCodeforBTF(f1);
					constructTraceHeader(f1);
					parseTraceData(f1);
					zynqmainBTF(model, f1);
				}
				else {
					headerIncludesArmCode(f1);
					nsleep(f1);
					zynqmain(model, f1);
				}
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
			@SuppressWarnings("resource")
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
			@SuppressWarnings("resource")
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
			@SuppressWarnings("resource")
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
	 * main function for the zynq in which we deploy the
	 *
	 * @param model
	 * @param file
	 */
	private static void zynqmainBTF(final Amalthea model, final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
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
				fw.write("int main(int argc, char *argv[])\n{\n");
				fw.write("\te_platform_t epiphany;\n");
				fw.write("\te_epiphany_t dev;\n");
				fw.write("\te_mem_t emem;\n");
				fw.write("\tint index = 0;\n");
				fw.write("\tbtf_trace_info trace_info;\n");
				fw.write("\ttrace_info.length = 0;\n");
				fw.write("\ttrace_info.core_write = 0;\n");
				fw.write("\ttrace_info.offset = 0;\n");
				fw.write("\ttrace_info.core_id = 0;\n");
				fw.write("\t/* File pointer to store the BTF data which will be used for\n" + 
						"\tfurther processing. The file generated will be deleted after the processing is done */\n" + 
						"\tFILE *fp_temp = NULL;\n");
				fw.write("\t/* File pointer to store the  BTF trace file */\n" + 
						"\tFILE *fp_to_trace = NULL;\n");
				fw.write("\tint scale_factor = parse_btf_trace_arguments(argc, argv);\n");
				fw.write("\tchar trace_file_path[512] = {0};\n");
				fw.write("\tget_btf_trace_file_path(trace_file_path);\n");
				fw.write("\tif (strlen((const char *)trace_file_path) != 0)\n" + 
						"\t{\n" + 
						"\t\tfp_to_trace = fopen((const char *)trace_file_path, \"w+\");\n" + 
						"\t\tif (fp_to_trace == NULL)\n" + 
						"\t\t{\n" + 
						"\t\t\tfprintf(stderr,\"Output redirected to stderr\\n\");\n" + 
						"\t\t\tfp_to_trace = stderr;\n" + 
						"\t\t}\n" + 
						"\t}\n" + 
						"\telse\n" + 
						"\t{\n" + 
						"\t\tfprintf(stderr,\"Output redirected to stderr\\n\");\n" + 
						"\t\tfp_to_trace = stderr;\n" + 
						"\t}\n");
				fw.write("\tconstruct_btf_trace_header(fp_to_trace);\n");
				fw.write("\tfp_temp = fopen((const char *)\"temp.txt\", \"w+\");\n");
				fw.write("\tif (fp_temp == NULL)\n");
				fw.write("\t{\n");
				fw.write("\t\texit(0);\n");
				fw.write("\t}\n");
				fw.write("\te_init(NULL);\n");
				fw.write("\t/* Reserve the memory for the data in the shared dram region to be shared between\n" + 
						"\t * host and epiphany core. The dram offset starts at 0x01000000 which corresponds\n" + 
						"\t * to the global address as 0x8F000000. */\n" + 
						"\tif (E_OK != e_alloc(&emem, (unsigned int)shared_dram_start_address , SHARED_DRAM_SIZE))\n" + 
						"\t{\n" + 
						"\t\tfprintf(stderr, \"Error in reserving the shared dram buffer\\n\");\n" + 
						"\t}\n");
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
							fw.write("\tunsigned int ecore" + k3 + "[RTF_DEBUG_TRACE_COUNT];\n");
							k3++;
						}
					}
				}
				int coreIndex = 0;
				for (int rowCoreGroup = 0; rowCoreGroup < coreGroup; rowCoreGroup++) {
					for (int columnCoreGroup = 0; columnCoreGroup < coreGroup; columnCoreGroup++) {
						if (coreIndex < localPU.size()) {
							fw.write("\tresult" + coreIndex + "=  e_load(\"main" + coreIndex + ".elf\",&dev,"
									+ columnCoreGroup + "," + rowCoreGroup + ",E_FALSE);\n");
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
				fw.write("\t/* Write the time unit used for the configuration of the clock cycle per tick */\n" + 
						"\tif (sizeof(int) != e_write(&emem, 0, 0, INPUT_TIMESCALE_OFFSET, &scale_factor, sizeof(int)))\n" + 
						"\t{\n" + 
						"\t\tfprintf(stderr, \"Error in writing to the shared dram buffer\\n\");\n" + 
						"\t}\n");
				fw.write("\t/* Write the initialized trace buffer values to the shared memory */\n" + 
						"\tif (sizeof(btf_trace_info) != e_write(&emem, 0, 0, SHARED_BTF_DATA_OFFSET, &trace_info,\n" + 
						"\t\t\tsizeof(btf_trace_info)))\n" + 
						"\t{\n" + 
						"\t\tfprintf(stderr, \"Error in writing to the shared dram buffer\\n\");\n" + 
						"\t}\n");
				
				fw.write("\te_start_group(&dev);\n");
				fw.write("\tfprintf(stderr,\"RMS Multicore on FreeRTOS started \\n\");\n");
				fw.write("\tint pollLoopCounter = 0;\n");
				fw.write("\tunsigned int btf_trace[BTF_TRACE_BUFFER_SIZE * 6] = {0};\n");
				fw.write("\tunsigned int core_id = 0;\n");
				fw.write("\tunsigned char btf_data_index = 0;\n");
				fw.write("\tunsigned int btf_data_start_offset = (SHARED_BTF_DATA_OFFSET + sizeof(btf_trace_info));\n");
				fw.write("\tfor (pollLoopCounter=0;pollLoopCounter<=100000;pollLoopCounter++){\n");
				fw.write("\t\te_read(&emem, 0, 0, SHARED_BTF_DATA_OFFSET , &trace_info, sizeof(btf_trace_info));\n");
				fw.write("\t\tif (trace_info.core_write == 1)\r\n\t\t{\n");
				fw.write("\t\t\te_read(&emem, 0, 0, SHARED_BTF_DATA_OFFSET , &trace_info, sizeof(btf_trace_info));\n");
				fw.write("\t\t\te_read(&emem, 0, 0, (btf_data_start_offset + (trace_info.offset * sizeof(int))),\n" + 
						"\t\t\t\t\t\t\t\t&btf_trace, BTF_TRACE_BUFFER_SIZE * sizeof(int) * trace_info.length);\n");
				fw.write("\t\t\tfor(btf_data_index = 0; btf_data_index < trace_info.length; btf_data_index++)\n" + 
						"\t\t\t{\n" + 
						"\t\t\t\tuint16_t offset = btf_data_index * BTF_TRACE_BUFFER_SIZE;\n" + 
						"\t\t\t\tbuffer_count += sprintf( &file_buffer[buffer_count], \"%d %d %d %d %d %d %d %d %d\\n\",\n" + 
						"\t\t\t\t\t\ttrace_info.core_id, btf_trace[offset],\n" + 
						"\t\t\t\t\t\tbtf_trace[offset + 1], btf_trace[offset+2], btf_trace[offset+3],\n" + 
						"\t\t\t\t\t\tbtf_trace[offset+4], btf_trace[offset+5], btf_trace[offset+6],\n" + 
						"\t\t\t\t\t\tbtf_trace[offset+7]);\n" + 
						"\t\t\t}\n\n");
				fw.write("\t\t\tif( buffer_count >= CHUNK_SIZE )\n" + 
						"\t\t\t{\n" + 
						"\t\t\t\tfwrite( file_buffer, buffer_count, 1, fp_temp ) ;\n" + 
						"\t\t\t\tbuffer_count = 0 ;\n" + 
						"\t\t\t}\n\n");
				fw.write("\t\t\ttrace_info.core_write = 0;\n");
				fw.write("\t\t\te_write(&emem, 0, 0, SHARED_BTF_DATA_OFFSET + offsetof(btf_trace_info, core_write),\n" + 
						"\t\t\t\t\t&trace_info.core_write, sizeof(int));\n");
				fw.write("\t\t}\n");
				fw.write("\t}\n");
				fw.write("\t// Write remainder\n" + 
						"\tif( buffer_count > 0 )\n" + 
						"\t{\n" + 
						"\t\tfwrite( file_buffer, buffer_count, 1, fp_temp );\n" + 
						"\t}\n");
				fw.write("\tfprintf(stderr,\"----------------------------------------------\\n\");\n");
				fw.write("\tif (fp_temp != NULL)\n" + 
						"\t{\n" + 
						"\t\tfclose(fp_temp);\n" + 
						"\t\tfp_temp = NULL;\n" + 
						"\t}\n");
				fw.write("\t/* Parse the trace data and store the trace file */\n" + 
						"\tparse_trace_data(fp_to_trace);\n");
				fw.write("\tif (fp_to_trace != NULL)\n" + 
						"\t{\n" + 
						"\t\tfclose(fp_to_trace);\n" + 
						"\t\tfp_to_trace = NULL;\n" + 
						"\t}\n");
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
	private static void parseTraceData(final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("static void parse_trace_data(FILE *trace)\n" + 
					"{\n" + 
					"    FILE *fp_temp = NULL;\n" + 
					"    unsigned int trace_data[BTF_TRACE_BUFFER_SIZE];\n" + 
					"    unsigned int active_row;\n" + 
					"    if (trace == NULL )\n" + 
					"    {\n" + 
					"        return;\n" + 
					"    }\n" + 
					"    fp_temp = fopen((const char *)\"temp.txt\", \"r\");\n" + 
					"    if (fp_temp == NULL)\n" + 
					"    {\n" + 
					"        exit(1);\n" + 
					"    }\n" + 
					"    while( fscanf(fp_temp, \"%d %d %d %d %d %d %d %d %d\\n\"\n" + 
					"                    , &active_row, &trace_data[0], &trace_data[1], &trace_data[2],\n" + 
					"                    &trace_data[3], &trace_data[4], &trace_data[5], &trace_data[6],\n" + 
					"                    &trace_data[7]) != EOF )\n" + 
					"    {\n" + 
					"        write_btf_trace_data(trace, active_row, trace_data);\n" + 
					"    }\n" + 
					"    if (fp_temp != NULL)\n" + 
					"    {\n" + 
					"        fclose(fp_temp);\n" + 
					"        fp_temp = NULL;\n" + 
					"    }\n" + 
					"    remove(\"temp.txt\");\n" + 
					"}\n\n\n");
			fw.close();
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
	private static void constructTraceHeader(final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("static void construct_btf_trace_header(FILE *stream)\n" + 
					"{\n" + 
					"    write_btf_trace_header_config(stream);\n" + 
					"    write_btf_trace_header_entity_type(stream, TASK_EVENT);\n" + 
					"    write_btf_trace_header_entity_type(stream, RUNNABLE_EVENT);\n" + 
					"    write_btf_trace_header_entity_type(stream, SIGNAL_EVENT);\n" + 
					"    generate_task_entity_table();\n" + 
					"    generate_runnable_entity_table();\n" + 
					"    generate_signal_entity_table();\n" + 
					"    generate_hw_entity_table();\n" + 
					"    write_btf_trace_header_entity_table(stream);\n" + 
					"    write_btf_trace_header_entity_type_table(stream);\n" + 
					"}\n\n\n");
			fw.close();
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
	private static void headerIncludesArmCodeforBTF(final File file) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n");
			fw.write("#include <stddef.h>\n");
			fw.write("#include <unistd.h>\n");
			fw.write("#include <e-hal.h>\n");
			fw.write("#include <e-loader.h>\n");
			fw.write("#include <time.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"RTFParallellaConfig.h\"\n");
			fw.write("#include \"host_utils.h\"\n");
			fw.write("#include \"model_enumerations.h\"\n");
			fw.write("#include \"trace_utils_BTF.h\"\n");
			fw.write("\n\n\n");
			fw.write("#define CHUNK_SIZE             4096\n");
			fw.write("char file_buffer[CHUNK_SIZE + 256];\n");
			fw.write("static int buffer_count = 0;\n\n\n");
			fw.write("unsigned int shared_dram_start_address = SHARED_DRAM_START_OFFSET;\n\n\n");
			fw.close();
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
			@SuppressWarnings("resource")
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
}
