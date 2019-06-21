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
	#define mainTask_ESSP0	( tskIDLE_PRIORITY + 0 )
	#define mainTask_ESSP1	( tskIDLE_PRIORITY + 0 )
	#define mainTask_ESSP2	( tskIDLE_PRIORITY + 0 )
	#define mainTask_ESSP3	( tskIDLE_PRIORITY + 0 )
	#define mainTask_ESSP4	( tskIDLE_PRIORITY + 0 )
	#define mainTask_ESSP5	( tskIDLE_PRIORITY + 0 )
	#define mainTask_ESSP6	( tskIDLE_PRIORITY + 0 )
	#define mainTask_ESSP7	( tskIDLE_PRIORITY + 0 )
	#define mainTask_ESSP8	( tskIDLE_PRIORITY + 0 )
	#define mainTask_ESSP9	( tskIDLE_PRIORITY + 0 )

