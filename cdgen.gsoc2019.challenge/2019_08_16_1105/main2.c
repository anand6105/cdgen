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
#include "taskDef2.h"
#include "shared_comms.h"

#include "label2.h"
/* TaskStimuli. */
	#define Timer_10MS	10 
	#define Timer_20MS	20 
	#define Timer_5MS	5 

/* TaskPriorities. */
	#define mainTask_ESSP4	( tskIDLE_PRIORITY +2 )
	#define mainTask_ESSP6	( tskIDLE_PRIORITY +1 )

int main(void) 
{
	outbuf_init();
	void shared_label_16bit_init();
	void shared_label_8bit_init();
	AmaltheaTask AmalTk_Task_ESSP6 = createAmaltheaTask( vTask_ESSP6, cIN_Task_ESSP6, cOUT_Task_ESSP6, Timer_10MS, Timer_10MS, 1);
	AmaltheaTask AmalTk_Task_ESSP4 = createAmaltheaTask( vTask_ESSP4, cIN_Task_ESSP4, cOUT_Task_ESSP4, Timer_5MS, Timer_5MS, 1);
	createRTOSTask( &AmalTk_Task_ESSP6, mainTask_ESSP6, 2,uint8_t, 12, uint16_t, 15);
	createRTOSTask( &AmalTk_Task_ESSP4, mainTask_ESSP4, 2,uint16_t, 4, uint8_t, 8);
	vTaskStartScheduler();
	return EXIT_SUCCESS;
}

