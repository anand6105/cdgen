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
 * @author Ram Prasath Govindarajan
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
	public FreeRTOSConfigFileCreation(final Amalthea Model, String srcPath, int configFlag) throws IOException {
		this.model = Model;
		System.out.println("FreeRTOSConfig File Creation Begins");
		fileCreate(model, srcPath, configFlag);
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
	public static void fileCreate(Amalthea model, String srcpath, int configFlag) throws IOException {
		String fname = srcpath + File.separator + "FreeRTOSConfig.h";
		File f2 = new File(srcpath);
		File f1 = new File(fname);
		f2.mkdirs();
		try {
			f1.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File fn = f1;
		FileWriter fw = new FileWriter(fn, true);
		try {
			fileUtil.fileMainHeader(f1);
			fileUtil.FreeRTOSConfigFileHeader(f1);
			headerIncludesFreeRTOS(model, f1, configFlag);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
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
	public static void headerIncludesFreeRTOS(Amalthea model, File file, int configFlag) {
		try {
			File fn = file;
			FileWriter fw = new FileWriter(fn, true);
			int constval = 1;//TODO merge this constval with the value used in time period in MAIN File - Issue001
			int taskSize = model.getSwModel().getTasks().size();
			fw.write("\t#ifndef FREERTOS_CONFIG_H\n");
			fw.write("\t#define FREERTOS_CONFIG_H\n");
			fw.write("//-----------------------------------------------------------\n");
			fw.write("\t#define configCALL_STACK_SIZE			0x50\n");
			if (0x0020 == (configFlag & 0x00F0)) {
				fw.write("\t#define configUSE_PREEMPTION		1\n");
			} else{
				fw.write("\t#define configUSE_PREEMPTION		0\n");
			}
			fw.write("\t#define configUSE_TIME_SLICING			0\n");
			fw.write("\t#define configUSE_IDLE_HOOK				0\n");
			fw.write("\t#define configUSE_TICK_HOOK				0\n");
			fw.write("\t#define configCPU_CLOCK_HZ				( ( unsigned long ) 700000000 )\n");
			fw.write("\t#define configTICK_RATE_HZ				( ( TickType_t ) 1000 )\n");
			fw.write("\t#define configMAX_PRIORITIES			( ( unsigned portBASE_TYPE ) "+ (taskSize+constval)+" )\n");
			fw.write("\t#define configMINIMAL_STACK_SIZE		( ( unsigned short ) 0x200) //512 words\n");
			fw.write("\t#define configTOTAL_HEAP_SIZE			( ( size_t ) ( 10450 ) )\n");
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
		} catch (IOException ioe) {
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
