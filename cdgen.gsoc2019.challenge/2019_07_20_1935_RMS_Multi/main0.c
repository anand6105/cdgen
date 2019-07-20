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
	#define mainTask_ESSP5	( tskIDLE_PRIORITY +1 )
	#define mainTask_ESSP3	( tskIDLE_PRIORITY +2 )
	#define mainTask_ESSP9	( tskIDLE_PRIORITY +3 )
	#define mainTask_ESSP2	( tskIDLE_PRIORITY +4 )
	#define mainTask_ESSP8	( tskIDLE_PRIORITY +5 )

int main(void) 
{
	outbuf_init();
	AmaltheaTask AmalTk_Task_ESSP2 = createAmaltheaTask( vTask_ESSP2, cIN_Task_ESSP2, cOUT_Task_ESSP2, Timer_10MS, Timer_10MS, 1.46E11);
	AmaltheaTask AmalTk_Task_ESSP8 = createAmaltheaTask( vTask_ESSP8, cIN_Task_ESSP8, cOUT_Task_ESSP8, Timer_10MS, Timer_10MS, 2.19E11);
	AmaltheaTask AmalTk_Task_ESSP5 = createAmaltheaTask( vTask_ESSP5, cIN_Task_ESSP5, cOUT_Task_ESSP5, Timer_5MS, Timer_5MS, 8.06E11);
	AmaltheaTask AmalTk_Task_ESSP3 = createAmaltheaTask( vTask_ESSP3, cIN_Task_ESSP3, cOUT_Task_ESSP3, Timer_5MS, Timer_5MS, 3.66E11);
	AmaltheaTask AmalTk_Task_ESSP9 = createAmaltheaTask( vTask_ESSP9, cIN_Task_ESSP9, cOUT_Task_ESSP9, Timer_5MS, Timer_5MS, 2.19E11);
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP2", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP2, mainTask_ESSP2, NULL);
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP8", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP8, mainTask_ESSP8, NULL);
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP5", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP5, mainTask_ESSP5, NULL);
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP3", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP3, mainTask_ESSP3, NULL);
	xTaskCreate(generalizedRTOSTask , "AmalTk_Task_ESSP9", configMINIMAL_STACK_SIZE, &AmalTk_Task_ESSP9, mainTask_ESSP9, NULL);
	vTaskStartScheduler();
	return EXIT_SUCCESS;
}

