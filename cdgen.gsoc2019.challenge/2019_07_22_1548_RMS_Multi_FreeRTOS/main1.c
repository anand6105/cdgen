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
#include "ParallellaUtils.h"
#include "taskDef.h"

/* TaskStimuli. */
	#define Timer_10MS	10 
	#define Timer_20MS	20 
	#define Timer_5MS	5 

/* TaskPriorities. */
	#define mainTask_ESSP7	( tskIDLE_PRIORITY +5 )
	#define mainTask_ESSP6	( tskIDLE_PRIORITY +4 )
	#define mainTask_ESSP1	( tskIDLE_PRIORITY +3 )
	#define mainTask_ESSP0	( tskIDLE_PRIORITY +2 )
	#define mainTask_ESSP4	( tskIDLE_PRIORITY +1 )

int main(void) 
{
	outbuf_init();
	AmaltheaTask AmalTk_Task_ESSP1 = createAmaltheaTask( vTask_ESSP1, cIN_Task_ESSP1, cOUT_Task_ESSP1, Timer_10MS, Timer_10MS, 0);
	AmaltheaTask AmalTk_Task_ESSP4 = createAmaltheaTask( vTask_ESSP4, cIN_Task_ESSP4, cOUT_Task_ESSP4, Timer_5MS, Timer_5MS, 0);
	AmaltheaTask AmalTk_Task_ESSP0 = createAmaltheaTask( vTask_ESSP0, cIN_Task_ESSP0, cOUT_Task_ESSP0, Timer_5MS, Timer_5MS, 0);
	AmaltheaTask AmalTk_Task_ESSP6 = createAmaltheaTask( vTask_ESSP6, cIN_Task_ESSP6, cOUT_Task_ESSP6, Timer_10MS, Timer_10MS, 0);
	AmaltheaTask AmalTk_Task_ESSP7 = createAmaltheaTask( vTask_ESSP7, cIN_Task_ESSP7, cOUT_Task_ESSP7, Timer_10MS, Timer_10MS, 0);
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP1", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP1, mainTask_ESSP1, NULL);
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP4", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP4, mainTask_ESSP4, NULL);
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP0", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP0, mainTask_ESSP0, NULL);
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP6", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP6, mainTask_ESSP6, NULL);
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP7", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP7, mainTask_ESSP7, NULL);
	vTaskStartScheduler();
	return EXIT_SUCCESS;
}

