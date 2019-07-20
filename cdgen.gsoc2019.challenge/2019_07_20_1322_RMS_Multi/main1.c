/******************************************************************
******************************************************************
**************#####**####*****#####**######*###****##*************
*************##******#***##**##******##*****##*#***##*************
*************#*******#****#**#****##*######*##**#**##*************
*************##******#***##**##***##*##*****##***#*##*************
**************#####**####*****######*######*##****###*************
******************************************************************
******************************************************************
*Author		:	Ram Prasath Govindarajan
*Tool 		:	CDGen_GSoC
*Version 	:	V1.0.0
*Title 		:   C File for Tasks Call
*Description	:	Main file in which scheduling is done 
******************************************************************
******************************************************************
******************************************************************/


/* Standard includes. */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <e_lib.h>

/* Scheduler includes. */
#include "FreeRTOS.h"
#include "task.h"
#include "queue.h"
#include "AmaltheaConverter.h"
#include "debugFlags.h"
#include "taskDef.h"

#define READ_PRECISION_US 1000


/* TaskStimuli. */
	#define Timer_10MS	10 
	#define Timer_20MS	20 
	#define Timer_5MS	5 

/* TaskPriorities. */
	#define mainTask_ESSP4	( tskIDLE_PRIORITY +1 )
	#define mainTask_ESSP0	( tskIDLE_PRIORITY +2 )
	#define mainTask_ESSP1	( tskIDLE_PRIORITY +3 )
	#define mainTask_ESSP6	( tskIDLE_PRIORITY +4 )
	#define mainTask_ESSP7	( tskIDLE_PRIORITY +5 )

int main(void) 
{
	outbuf_init();
	AmaltheaTask AmalTk_Task_ESSP1 = createAmaltheaTask(Task_ESSP1, cIN_Task_ESSP1, cOUT_Task_ESSP1, Timer_10MS, Timer_10MS, 1.46E11);
	AmaltheaTask AmalTk_Task_ESSP4 = createAmaltheaTask(Task_ESSP4, cIN_Task_ESSP4, cOUT_Task_ESSP4, Timer_5MS, Timer_5MS, 2.93E11);
	AmaltheaTask AmalTk_Task_ESSP6 = createAmaltheaTask(Task_ESSP6, cIN_Task_ESSP6, cOUT_Task_ESSP6, Timer_10MS, Timer_10MS, 7.33E11);
	AmaltheaTask AmalTk_Task_ESSP7 = createAmaltheaTask(Task_ESSP7, cIN_Task_ESSP7, cOUT_Task_ESSP7, Timer_10MS, Timer_10MS, 1.46E11);
	AmaltheaTask AmalTk_Task_ESSP0 = createAmaltheaTask(Task_ESSP0, cIN_Task_ESSP0, cOUT_Task_ESSP0, Timer_5MS, Timer_5MS, 2.19E11);

	vDisplayMessage("created RMS sched task\n");
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP1", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP1, mainTask_ESSP1, NULL);
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP4", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP4, mainTask_ESSP4, NULL);
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP6", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP6, mainTask_ESSP6, NULL);
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP7", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP7, mainTask_ESSP7, NULL);
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP0", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP0, mainTask_ESSP0, NULL);
	vDisplayMessage("created other tasks\n");
	vTaskStartScheduler();
	return EXIT_SUCCESS;
}

