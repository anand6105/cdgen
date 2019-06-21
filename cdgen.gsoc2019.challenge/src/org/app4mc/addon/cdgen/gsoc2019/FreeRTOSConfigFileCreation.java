package org.app4mc.addon.cdgen.gsoc2019;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.app4mc.addon.cdgen.gsoc2019.utils.fileUtil;
import org.eclipse.app4mc.amalthea.model.Amalthea;

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
	 *            Amalthea Model
	 * @param path1
	 * @param pthreadFlag
	 * @param preemptionFlag
	 * @throws IOException
	 */

	public FreeRTOSConfigFileCreation(final Amalthea Model, String path1, boolean preemptionFlag) throws IOException {
		this.model = Model;
		System.out.println("FreeRTOSConfig File Creation Begins");
		fileCreate(model, path1, preemptionFlag);
		System.out.println("FreeRTOSConfig File Creation Ends");
	}

	public static void fileCreate(Amalthea model, String path1, boolean preemptionFlag) throws IOException {

		String fname = path1 + File.separator + "FreeRTOSConfig.h";
		File f2 = new File(path1);
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
			headerIncludesFreeRTOS(model, f1, preemptionFlag);

			// fileUtil.TaskDefinition(f1,tasks);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void headerIncludesFreeRTOS(Amalthea model, File f1, boolean preemptionFlag) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			int constval = 1;
			int taskSize = model.getSwModel().getTasks().size();
			fw.write("\t#ifndef FREERTOS_CONFIG_H\n");
			fw.write("\t#define FREERTOS_CONFIG_H\n");
			fw.write("//-----------------------------------------------------------\n");
			if (preemptionFlag == true) {
				fw.write("\t#define configUSE_PREEMPTION			1\n");
				fw.write("\t#define configUSE_TICK_HOOK				1\n");

			} else if (preemptionFlag == false) {
				fw.write("\t#define configUSE_PREEMPTION			0\n");
				fw.write("\t#define configUSE_TICK_HOOK				0\n");

			}
			fw.write("\t#define configTICK_RATE_HZ				( ( portTickType ) 1000 )\n");
			fw.write("\t#define configMINIMAL_STACK_SIZE		( ( unsigned portSHORT ) 4 )\n");
			fw.write("\t#define configTOTAL_HEAP_SIZE			( ( size_t ) ( 32 * 1024 ) )\n");
			fw.write("\t#define configMAX_TASK_NAME_LEN			( 16 )\n");
			fw.write("\t#define configUSE_TRACE_FACILITY    	1\n");
			fw.write("\t#define configUSE_16_BIT_TICKS      	0\n");
			fw.write("\t#define configIDLE_SHOULD_YIELD			1\n");
			fw.write("\t#define configUSE_CO_ROUTINES 			1\n");
			fw.write("\t#define configUSE_MUTEXES				1\n");
			fw.write("\t#define configUSE_COUNTING_SEMAPHORES	1\n");
			fw.write("\t#define configUSE_ALTERNATIVE_API		0\n");
			fw.write("\t#define configUSE_RECURSIVE_MUTEXES		1\n");
			fw.write("\t#define configCHECK_FOR_STACK_OVERFLOW	0 \n");
			fw.write("\t#define configUSE_APPLICATION_TASK_TAG	1\n");
			fw.write("\t#define configQUEUE_REGISTRY_SIZE		0\n");
			fw.write("\t#define configMAX_SYSCALL_INTERRUPT_PRIORITY	1\n");
			fw.write("\t#define configMAX_PRIORITIES		( ( unsigned portBASE_TYPE ) "+ (taskSize+constval)+" )\n");
			fw.write("\t#define configMAX_CO_ROUTINE_PRIORITIES ( 2 )\n");
			fw.write("//-----------------------------------------------------------\n");
			fw.write("\t#define INCLUDE_vTaskPrioritySet        	1\n");
			fw.write("\t#define INCLUDE_uxTaskPriorityGet       	1\n");
			fw.write("\t#define INCLUDE_vTaskDelete             	1\n");
			fw.write("\t#define INCLUDE_vTaskCleanUpResources   	1\n");
			fw.write("\t#define INCLUDE_vTaskSuspend            	1\n");
			fw.write("\t#define INCLUDE_vTaskDelayUntil				1\n");
			fw.write("\t#define INCLUDE_vTaskDelay					1\n");
			fw.write("\t#define INCLUDE_uxTaskGetStackHighWaterMark 0 \n");
			fw.write("\t#define INCLUDE_xTaskGetSchedulerState		1\n");
			fw.write("\textern void vMainQueueSendPassed( void );\n");
			fw.write("\t#define traceQUEUE_SEND( pxQueue ) vMainQueueSendPassed()\n");
			fw.write("\t#define configGENERATE_RUN_TIME_STATS		1\n");
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
