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

public class FreeRTOSConfigPreeFileCreation {
	final private Amalthea model;

	/**
	 * Constructor FreeRTOSConfigPreeFileCreation
	 *
	 * @param Model
	 *            Amalthea Model
	 * @param path1
	 * @throws IOException
	 */
	public FreeRTOSConfigPreeFileCreation(final Amalthea Model, String path1) throws IOException {
		this.model = Model;
		System.out.println("FreeRTOSConfig File Creation Begins");
		fileCreate(model, path1);
		System.out.println("FreeRTOSConfig File Creation Ends");
	}

	private static void fileCreate(Amalthea model, String path1) throws IOException {

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
			headerIncludesFreeRTOSPree(f1);

			// fileUtil.TaskDefinition(f1,tasks);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

 private static void headerIncludesFreeRTOSPree(File f1) {
		try {
			File fn = f1;
			FileWriter fw = new FileWriter(fn, true);
			fw.write("#ifndef FREERTOS_CONFIG_H\n");
			fw.write("#define FREERTOS_CONFIG_H\n");
			fw.write("##include <avr/io.h>\n\n");
			fw.write("/* Standard includes. */\n");
			fw.write("#include <stdio.h>\n");
			fw.write("#include <stdlib.h>\n");
			fw.write("#include <string.h>\n\n");
			fw.write("/* Scheduler includes. */\n");
			fw.write("#include \"runnable.h\"\n");
			fw.write("#include \"taskDef.h\"\n");
			fw.write("#include \"FreeRTOS.h\"\n\n");
			fw.write("/******************************************************************/\n\n");
			fw.write("#define configUSE_PREEMPTION		1\n");
			fw.write("#define configUSE_IDLE_HOOK			1\n");
			fw.write("#define configUSE_TICK_HOOK			0\n");
			fw.write("#define configCPU_CLOCK_HZ			( ( unsigned long ) " + 8000000 + " )\n");
			fw.write("#define configTICK_RATE_HZ			( ( TickType_t ) 1000 )\n");
			fw.write("#define configMAX_PRIORITIES		( 4 )\n");
			fw.write("#define configUSE_PREEMPTION		1\n");
			fw.write("#define configMINIMAL_STACK_SIZE	( ( unsigned short ) 85 )\n");
			fw.write("#define configTOTAL_HEAP_SIZE		( (size_t ) ( 1500 ) )\n");
			fw.write("#define configMAX_TASK_NAME_LEN		( 8 )\n");
			fw.write("#define configUSE_TRACE_FACILITY	0\n");
			fw.write("#define configUSE_16_BIT_TICKS		1\n");
			fw.write("#define configIDLE_SHOULD_YIELD		1\n");
			fw.write("#define configQUEUE_REGISTRY_SIZE	0\n\n");
			fw.write("/* Co-routine definitions. */\n\n");
			fw.write("#define configUSE_CO_ROUTINES 		1\n");
			fw.write("#define configMAX_CO_ROUTINE_PRIORITIES ( 2 )\n\n");
			fw.write("/* Set the following definitions to 1 to include the API function, or zero\r\n"
					+ "to exclude the API function. */\n\n");
			fw.write("#define INCLUDE_vTaskPrioritySet		0\n");
			fw.write("#define INCLUDE_uxTaskPriorityGet		0\n");
			fw.write("#define INCLUDE_vTaskDelete				1\n");
			fw.write("#define INCLUDE_vTaskCleanUpResources	0\n");
			fw.write("#define INCLUDE_vTaskSuspend			0\n");
			fw.write("#define INCLUDE_vTaskDelayUntil			1\n");
			fw.write("#define INCLUDE_vTaskDelay				1\n");
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
