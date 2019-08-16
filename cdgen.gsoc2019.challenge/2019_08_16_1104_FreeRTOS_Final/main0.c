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
#include "taskDef0.h"
#include "shared_comms.h"

#include "label0.h"
/* TaskStimuli. */
	#define Timer_10MS	10 
	#define Timer_20MS	20 
	#define Timer_5MS	5 

/* TaskPriorities. */
	#define mainTask_ESSP0	( tskIDLE_PRIORITY +2 )
	#define mainTask_ESSP1	( tskIDLE_PRIORITY +1 )

int main(void) 
{
	outbuf_init();
	void shared_label_8bit_init();
	void shared_label_16bit_init();
	xTaskCreate(vTask_ESSP0 , "Task_ESSP0", configMINIMAL_STACK_SIZE, &vTask_ESSP0, mainTask_ESSP0, NULL);
	xTaskCreate(vTask_ESSP1 , "Task_ESSP1", configMINIMAL_STACK_SIZE, &vTask_ESSP1, mainTask_ESSP1, NULL);
	vTaskStartScheduler();
	return EXIT_SUCCESS;
}

