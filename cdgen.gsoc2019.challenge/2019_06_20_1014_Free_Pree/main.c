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

/* Scheduler includes. */
#include "taskDef.h"
#include "FreeRTOS.h"

void sleepTimerMs(int ticks)
{
	int var;
	for (var = 0; var < ticks; ++var)
	{
		vTaskSuspendAll();
		{
			usleep(1000);
		}
		xTaskResumeAll();
	}
}
/* TaskPriorities. */
	#define mainTask_10MS	( tskIDLE_PRIORITY + 10 )
	#define mainTask_20MS	( tskIDLE_PRIORITY + 10 )
	#define mainTask_5MS	( tskIDLE_PRIORITY + 10 )

int main(void) 
{
	xTaskCreate( vTask_10MS, "TASK_10MS", configMINIMAL_STACK_SIZE, NULL, mainTask_10MS, NULL );
	xTaskCreate( vTask_20MS, "TASK_20MS", configMINIMAL_STACK_SIZE, NULL, mainTask_20MS, NULL );
	xTaskCreate( vTask_5MS, "TASK_5MS", configMINIMAL_STACK_SIZE, NULL, mainTask_5MS, NULL );
	vTaskStartScheduler();
	return 0;
}
