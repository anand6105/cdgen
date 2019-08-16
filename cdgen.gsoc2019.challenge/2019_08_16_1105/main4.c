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
#include "taskDef4.h"
#include "shared_comms.h"

#include "label4.h"
/* TaskStimuli. */
	#define Timer_10MS	10 
	#define Timer_20MS	20 
	#define Timer_5MS	5 

/* TaskPriorities. */
	#define mainTask_ESSP9	( tskIDLE_PRIORITY +2 )
	#define mainTask_ESSP8	( tskIDLE_PRIORITY +1 )

int main(void) 
{
	outbuf_init();
	void shared_label_16bit_init();
	void shared_label_8bit_init();
	AmaltheaTask AmalTk_Task_ESSP9 = createAmaltheaTask( vTask_ESSP9, cIN_Task_ESSP9, cOUT_Task_ESSP9, Timer_5MS, Timer_5MS, 1);
	AmaltheaTask AmalTk_Task_ESSP8 = createAmaltheaTask( vTask_ESSP8, cIN_Task_ESSP8, cOUT_Task_ESSP8, Timer_10MS, Timer_10MS, 1);
	createRTOSTask( &AmalTk_Task_ESSP9, mainTask_ESSP9, 2,uint8_t, 3, uint16_t, 5);
	createRTOSTask( &AmalTk_Task_ESSP8, mainTask_ESSP8, 2,uint16_t, 9, uint8_t, 12);
	vTaskStartScheduler();
	return EXIT_SUCCESS;
}

