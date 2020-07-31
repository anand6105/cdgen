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

import org.eclipse.app4mc.amalthea.model.Amalthea;
import org.eclipse.app4mc.cdgen.utils.fileUtil;

/**
 * Implementation of FreeRTOS specific configuration and macros.
 *
 *
 */
public class FreeRTOSConfigFileCreation {
	final private Amalthea model;

	/**
	 * Constructor FreeRTOSConfigFileCreation
	 *
	 * @param Model
	 * @param srcPath
	 * @param configFlag
	 * @throws IOException
	 */
	public FreeRTOSConfigFileCreation(final Amalthea Model, final String srcPath, final int configFlag)
			throws IOException {
		this.model = Model;
		System.out.println("FreeRTOSConfig File Creation Begins");
		fileCreate(this.model, srcPath, configFlag);
		System.out.println("FreeRTOSConfig File Creation Ends");
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
		final String fname = srcpath + File.separator + "FreeRTOSConfig.h";
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
			fileUtil.FreeRTOSConfigFileHeader(f1);
			headerIncludesFreeRTOS(model, f1, configFlag);
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
	public static void headerIncludesFreeRTOS(final Amalthea model, final File file, final int configFlag) {
		try {
			final File fn = file;
			@SuppressWarnings("resource")
			final FileWriter fw = new FileWriter(fn, true);
			final int constval = 1;
			final int taskSize = model.getSwModel().getTasks().size();
			fw.write("\t#ifndef FREERTOS_CONFIG_H\n");
			fw.write("\t#define FREERTOS_CONFIG_H\n");
			fw.write("//-----------------------------------------------------------\n");
			fw.write("\t#define configCALL_STACK_SIZE			0x50\n");
			if (0x0020 == (configFlag & 0x00F0)) {
				fw.write("\t#define configUSE_PREEMPTION		1\n");
			}
			else {
				fw.write("\t#define configUSE_PREEMPTION		0\n");
			}
			fw.write("\t#define configUSE_TIME_SLICING			0\n");
			fw.write("\t#define configUSE_IDLE_HOOK				0\n");
			fw.write("\t#define configUSE_TICK_HOOK				0\n");
			fw.write("\t#define configCPU_CLOCK_HZ				( ( unsigned long ) 700000000 )\n");
			fw.write("\t#define configTICK_RATE_HZ				( ( TickType_t ) 1000 )\n");
			fw.write("\t#define configMAX_PRIORITIES			( ( unsigned portBASE_TYPE ) " + (taskSize + constval)
					+ " )\n");
			if (0x0001 == (0x0001 & configFlag)) {
				fw.write("\t#define configMINIMAL_STACK_SIZE		( ( unsigned short ) 0x112) //274 words\n");
				fw.write("\t#define configTOTAL_HEAP_SIZE			( ( size_t ) ( 10240 ) )\n");
			}
			else {
				fw.write("\t#define configMINIMAL_STACK_SIZE		( ( unsigned short ) 0x200) //512 words\n");
				fw.write("\t#define configTOTAL_HEAP_SIZE			( ( size_t ) ( 10450 ) )\n");
			}
			fw.write("\t#define configMAX_TASK_NAME_LEN			( 128 )\n");
			fw.write("\t#define configUSE_TRACE_FACILITY    	0\n");
			fw.write("\t#define configUSE_16_BIT_TICKS      	1\n");
			fw.write("\t#define configIDLE_SHOULD_YIELD			0\n");
			fw.write("\t#define configUSE_ALTERNATIVE_API		0\n");
			fw.write("//-----------------------------------------------------------\n");
			fw.write("\t#define configUSE_CO_ROUTINES			0\n");
			fw.write("\t#define configMAX_CO_ROUTINE_PRIORITIES ( 2 )\n");
			fw.write("//-----------------------------------------------------------\n");
			fw.write("\t#define INCLUDE_vTaskPrioritySet        	0\n");
			fw.write("\t#define INCLUDE_uxTaskPriorityGet       	0\n");
			fw.write("\t#define INCLUDE_vTaskDelete             	0\n");
			fw.write("\t#define INCLUDE_vTaskCleanUpResources   	0\n");
			fw.write("\t#define INCLUDE_vTaskSuspend            	1\n");
			fw.write("\t#define INCLUDE_vTaskDelayUntil				1\n");
			fw.write("\t#define INCLUDE_vTaskDelay					1\n");
			fw.write("\t#define INCLUDE_xTaskGetCurrentTaskHandle	1\n");
			fw.write("\t#define INCLUDE_pcTaskGetTaskName 			1\n");
			fw.write("\n\t#define C2C_MSG_TYPE int		\n");
			fw.write("\t#endif\n");
			fw.close();
		}
		catch (final IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}
}
