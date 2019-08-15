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
#include "taskDef1.h"

/* TaskStimuli. */
	#define Timer_10MS	10 
	#define Timer_20MS	20 
	#define Timer_5MS	5 

/* TaskPriorities. */
	#define mainTask_ESSP4	( tskIDLE_PRIORITY +5 )
	#define mainTask_ESSP0	( tskIDLE_PRIORITY +4 )
	#define mainTask_ESSP7	( tskIDLE_PRIORITY +3 )
	#define mainTask_ESSP1	( tskIDLE_PRIORITY +2 )
	#define mainTask_ESSP6	( tskIDLE_PRIORITY +1 )

int main(void) 
{
	outbuf_init();
	void shared_label_8bit_init();
	void shared_label_16bit_init();
	xTaskCreate(vTask_ESSP7 , "Task_ESSP7", configMINIMAL_STACK_SIZE, &Task_ESSP7, mainTask_ESSP7, NULL);
	xTaskCreate(vTask_ESSP1 , "Task_ESSP1", configMINIMAL_STACK_SIZE, &Task_ESSP1, mainTask_ESSP1, NULL);
	xTaskCreate(vTask_ESSP4 , "Task_ESSP4", configMINIMAL_STACK_SIZE, &Task_ESSP4, mainTask_ESSP4, NULL);
	xTaskCreate(vTask_ESSP6 , "Task_ESSP6", configMINIMAL_STACK_SIZE, &Task_ESSP6, mainTask_ESSP6, NULL);
	xTaskCreate(vTask_ESSP0 , "Task_ESSP0", configMINIMAL_STACK_SIZE, &Task_ESSP0, mainTask_ESSP0, NULL);
	vTaskStartScheduler();
	return EXIT_SUCCESS;
}

