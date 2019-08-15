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
#include "taskDef0.h"

/* TaskStimuli. */
	#define Timer_10MS	10 
	#define Timer_20MS	20 
	#define Timer_5MS	5 

/* TaskPriorities. */
	#define mainTask_ESSP3	( tskIDLE_PRIORITY +5 )
	#define mainTask_ESSP5	( tskIDLE_PRIORITY +4 )
	#define mainTask_ESSP9	( tskIDLE_PRIORITY +3 )
	#define mainTask_ESSP2	( tskIDLE_PRIORITY +2 )
	#define mainTask_ESSP8	( tskIDLE_PRIORITY +1 )

int main(void) 
{
	outbuf_init();
	void shared_label_8bit_init();
	void shared_label_16bit_init();
	xTaskCreate(vTask_ESSP2 , "Task_ESSP2", configMINIMAL_STACK_SIZE, &Task_ESSP2, mainTask_ESSP2, NULL);
	xTaskCreate(vTask_ESSP3 , "Task_ESSP3", configMINIMAL_STACK_SIZE, &Task_ESSP3, mainTask_ESSP3, NULL);
	xTaskCreate(vTask_ESSP5 , "Task_ESSP5", configMINIMAL_STACK_SIZE, &Task_ESSP5, mainTask_ESSP5, NULL);
	xTaskCreate(vTask_ESSP8 , "Task_ESSP8", configMINIMAL_STACK_SIZE, &Task_ESSP8, mainTask_ESSP8, NULL);
	xTaskCreate(vTask_ESSP9 , "Task_ESSP9", configMINIMAL_STACK_SIZE, &Task_ESSP9, mainTask_ESSP9, NULL);
	vTaskStartScheduler();
	return EXIT_SUCCESS;
}

