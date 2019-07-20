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
	#define mainTask_ESSP4	( tskIDLE_PRIORITY +1 )
	#define mainTask_ESSP0	( tskIDLE_PRIORITY +2 )
	#define mainTask_ESSP3	( tskIDLE_PRIORITY +3 )
	#define mainTask_ESSP5	( tskIDLE_PRIORITY +4 )
	#define mainTask_ESSP9	( tskIDLE_PRIORITY +5 )
	#define mainTask_ESSP1	( tskIDLE_PRIORITY +6 )
	#define mainTask_ESSP8	( tskIDLE_PRIORITY +7 )
	#define mainTask_ESSP2	( tskIDLE_PRIORITY +8 )
	#define mainTask_ESSP7	( tskIDLE_PRIORITY +9 )
	#define mainTask_ESSP6	( tskIDLE_PRIORITY +10 )

int main(void) 
{
	xTaskCreate(0, vTask_ESSP0, "TASK_ESSP0", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP0, NULL );
	xTaskCreate(1, vTask_ESSP1, "TASK_ESSP1", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP1, NULL );
	xTaskCreate(0, vTask_ESSP2, "TASK_ESSP2", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP2, NULL );
	xTaskCreate(0, vTask_ESSP3, "TASK_ESSP3", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP3, NULL );
	xTaskCreate(1, vTask_ESSP4, "TASK_ESSP4", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP4, NULL );
	xTaskCreate(0, vTask_ESSP5, "TASK_ESSP5", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP5, NULL );
	xTaskCreate(1, vTask_ESSP6, "TASK_ESSP6", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP6, NULL );
	xTaskCreate(1, vTask_ESSP7, "TASK_ESSP7", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP7, NULL );
	xTaskCreate(0, vTask_ESSP8, "TASK_ESSP8", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP8, NULL );
	xTaskCreate(0, vTask_ESSP9, "TASK_ESSP9", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP9, NULL );
	vTaskStartScheduler();
	return 0;
}
