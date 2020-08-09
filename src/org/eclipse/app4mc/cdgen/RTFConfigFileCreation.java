/*******************************************************************************
 *   Copyright (c) 2020 Dortmund University of Applied Sciences and Arts and others.
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
import org.eclipse.app4mc.amalthea.model.ProcessingUnit;
import org.eclipse.app4mc.amalthea.model.SchedulerAllocation;
import org.eclipse.app4mc.amalthea.model.Task;
import org.eclipse.app4mc.amalthea.model.util.DeploymentUtil;
import org.eclipse.app4mc.cdgen.utils.fileUtil;
import org.eclipse.emf.common.util.EList;


/**
 * Implementation of RTF Parallella specific configuration and macros.
 *
 *
 */

public class RTFConfigFileCreation {
	
	final private Amalthea model;

	/**
	 * Constructor FreeRTOSConfigFileCreation
	 *
	 * @param Model
	 * @param srcPath
	 * @param configFlag
	 * @throws IOException
	 */
	public RTFConfigFileCreation(final Amalthea Model, final String srcPath, final int configFlag)
			throws IOException {
		this.model = Model;
		System.out.println("RTFParallellaConfig File Creation Begins");
		fileCreate(this.model, srcPath, configFlag);
		System.out.println("RTFParallellaConfig File Creation Ends");
	}
	
	
	/**
	 * FileCreation FreeRTOSConfigFileCreation
	 *
	 * @param model
	 * @param srcPath
	 * @param configFlag
	 * @throws IOException
	 */
	public static void fileCreate(final Amalthea model, final String srcpath, final int configFlag) throws IOException {
		final String fname = srcpath + File.separator + "RTFParallellaConfig.h";
		final File f2 = new File(srcpath);
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
			fileUtil.RTFConfigFileHeader(f1);
			headerIncludesRTFConfig(model, f1, configFlag);
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
	 * Macro framework and definition.
	 *
	 * @param model
	 * @param file
	 * @param configFlag
	 */
	public static void headerIncludesRTFConfig(final Amalthea model, final File file, final int configFlag) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			FileWriter fw = new FileWriter(fn, true);
			fw.write("#ifndef SRC_PARALLELLA_RTFPARALLELLACONFIG_H_\n");
			fw.write("#define SRC_PARALLELLA_RTFPARALLELLACONFIG_H_\n");
			fw.write("//-----------------------------------------------------------\n");
			fw.write("#define CDGEN_BTF_TRACE\n\n");
			fw.write("/* Shared DRAM start address*/\n" + 
					"#define SHARED_DRAM_START_ADDRESS			0x8E000000\n");
			fw.write("/* Shared dram start address offset corresponds to 0x8F000000 global address */\n" + 
					"#define SHARED_DRAM_START_OFFSET			0x01000000\n");
			fw.write("/* The Shared DRAM section as seen by the Epiphany core */\n" + 
					"#define SHARED_DRAM_SECTION				(SHARED_DRAM_START_ADDRESS + SHARED_DRAM_START_OFFSET)\n");
			fw.write("/* Allocate 4KB of shared DRAM for data exchange between host and epiphany cores */\n" + 
					"#define SHARED_DRAM_SIZE					0x00002000\n");
			fw.write("#define RTF_DEBUG_TRACE_COUNT				10\n");
			fw.write("/* First five address is used by FreeRTOS porting on Epiphany on shared dram see port.c file. */\n" + 
					"#define INPUT_TIMESCALE_OFFSET				20\n");
			fw.write("#define SHARED_BTF_DATA_OFFSET			(INPUT_TIMESCALE_OFFSET + 4)\n");
			fw.write("#define SHARED_LABEL_OFFSET				0x1000\n");
			fw.write("#define BTF_TRACE_BUFFER_SIZE				8\n");
			fw.write("#define GLOBAL_SHARED_LABEL_OFFSET		sizeof(btf_trace_info)\n");
			fw.write("/* Shared label count */\n" + 
					"#define SHM_LABEL_COUNT					10\n");
			fw.write("/* Start buffer address on epiphany core to store the RTF trace info. */\n" + 
					"#define ECORE_RTF_BUFFER_ADDR				0x7000\n");			
			fw.write("#define DSHM_LABEL_EPI_CORE_OFFSET		0x7040\n");
			fw.write("\n\n#define MUTEX_ROW        1\n");
			fw.write("#define MUTEX_COL        0\n");
			fw.write("#define RING_BUFFER_SIZE 6\n");
			fw.write("\n\nextern unsigned int execution_time_scale;\n");
			fw.write("\n\n/**\n" + 
					" * Structure to ensure proper synchronization between host and epiphany cores\n" + 
					" * and also within epiphany cores.\n" + 
					" */\n" + 
					"typedef struct btf_trace_info_t\n" + 
					"{\n" + 
					"    int length;                             /**< To ensure that the mutex is initialized */\n" + 
					"    unsigned int offset;                    /**< Mutex declaration. Unused on host  */\n" + 
					"    unsigned int core_id;                   /**< BTF trace data buffer size which is to be read */\n" + 
					"    unsigned int core_write;                /**< Read write operation between epiphany core and host */\n" + 
					"} btf_trace_info;\n");
			fw.write("\n\ntypedef enum entity_id_t\n" + 
					"{\n" + 
					"    /* 0 to 15 entity ID is reserved for TASKS. */\n" + 
					"    IDLE_TASK_ID = 0,\n");
			fw.close();
			final EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
			for (final SchedulerAllocation c : CoreNo) {
				final ProcessingUnit pu = c.getResponsibility().get(0);
				final Set<Task> tasks = DeploymentUtil.getTasksMappedToCore(pu, model);
				writeTaskEnum(model, file, tasks);
			}
			writeHwCoreEnum(model, file);
			fw = new FileWriter(fn, true);
			fw.write("\n\ntypedef enum {\n" + 
					"	UINT_8,\n" + 
					"	UINT_16,\n" + 
					"	UINT_32\n" + 
					"} TYPE;\n\n\n");
			fw.write("\n#endif\n\n\n");
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
				fw.write("    " + task.getName() + "_ID,\n");
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
		int core_id = 512;
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			fw.write("\n\n    /* 512 onwards reserved for HARDWARE */\n");
			final EList<SchedulerAllocation> CoreNo = model.getMappingModel().getSchedulerAllocation();
			for (final SchedulerAllocation c : CoreNo) {
				final ProcessingUnit pu = c.getResponsibility().get(0);
				fw.write("    " + pu.getName() + "_ID = " + core_id + ",\n");
				core_id++;
			}
			fw.write("} entity_id;\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

}
