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
#include "taskDef1.h"
#include "shared_comms.h"

#include "label1.h"
/* TaskStimuli. */
	#define Timer_10MS	10 
	#define Timer_20MS	20 
	#define Timer_5MS	5 

/* TaskPriorities. */
	#define mainTask_ESSP3	( tskIDLE_PRIORITY +2 )
	#define mainTask_ESSP2	( tskIDLE_PRIORITY +1 )

int main(void) 
{
	outbuf_init();
	void shared_label_16bit_init();
	void shared_label_8bit_init();
	AmaltheaTask AmalTk_Task_ESSP3 = createAmaltheaTask( vTask_ESSP3, cIN_Task_ESSP3, cOUT_Task_ESSP3, Timer_5MS, Timer_5MS, 1);
	AmaltheaTask AmalTk_Task_ESSP2 = createAmaltheaTask( vTask_ESSP2, cIN_Task_ESSP2, cOUT_Task_ESSP2, Timer_10MS, Timer_10MS, 1);
	createRTOSTask( &AmalTk_Task_ESSP3, mainTask_ESSP3, 2,uint16_t, 11, uint8_t, 15);
	createRTOSTask( &AmalTk_Task_ESSP2, mainTask_ESSP2, 1,uint16_t, 4);
	vTaskStartScheduler();
	return EXIT_SUCCESS;
}

