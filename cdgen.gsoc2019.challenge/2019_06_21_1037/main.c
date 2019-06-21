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
	#define mainorg.eclipse.app4mc.amalthea.model.impl.TaskImpl@3a0a765a (name: Task_ESSP5) (preemption: _undefined_, multipleTaskActivationLimit: 0)	( tskIDLE_PRIORITY + 0 )
	#define mainorg.eclipse.app4mc.amalthea.model.impl.TaskImpl@45375994 (name: Task_ESSP3) (preemption: _undefined_, multipleTaskActivationLimit: 0)	( tskIDLE_PRIORITY + 1 )
	#define mainorg.eclipse.app4mc.amalthea.model.impl.TaskImpl@680890a (name: Task_ESSP0) (preemption: _undefined_, multipleTaskActivationLimit: 0)	( tskIDLE_PRIORITY + 2 )
	#define mainorg.eclipse.app4mc.amalthea.model.impl.TaskImpl@739c4316 (name: Task_ESSP9) (preemption: _undefined_, multipleTaskActivationLimit: 0)	( tskIDLE_PRIORITY + 3 )
	#define mainorg.eclipse.app4mc.amalthea.model.impl.TaskImpl@477d40e1 (name: Task_ESSP4) (preemption: _undefined_, multipleTaskActivationLimit: 0)	( tskIDLE_PRIORITY + 4 )
	#define mainorg.eclipse.app4mc.amalthea.model.impl.TaskImpl@371a6ae9 (name: Task_ESSP7) (preemption: _undefined_, multipleTaskActivationLimit: 0)	( tskIDLE_PRIORITY + 5 )
	#define mainorg.eclipse.app4mc.amalthea.model.impl.TaskImpl@481e6187 (name: Task_ESSP6) (preemption: _undefined_, multipleTaskActivationLimit: 0)	( tskIDLE_PRIORITY + 6 )
	#define mainorg.eclipse.app4mc.amalthea.model.impl.TaskImpl@7b5126ea (name: Task_ESSP2) (preemption: _undefined_, multipleTaskActivationLimit: 0)	( tskIDLE_PRIORITY + 7 )
	#define mainorg.eclipse.app4mc.amalthea.model.impl.TaskImpl@14ad24f0 (name: Task_ESSP1) (preemption: _undefined_, multipleTaskActivationLimit: 0)	( tskIDLE_PRIORITY + 8 )
	#define mainorg.eclipse.app4mc.amalthea.model.impl.TaskImpl@1a4d4ea3 (name: Task_ESSP8) (preemption: _undefined_, multipleTaskActivationLimit: 0)	( tskIDLE_PRIORITY + 9 )

int main(void) 
{
	xTaskCreate( vTask_ESSP0, "TASK_ESSP0", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP0, NULL );
	xTaskCreate( vTask_ESSP1, "TASK_ESSP1", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP1, NULL );
	xTaskCreate( vTask_ESSP2, "TASK_ESSP2", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP2, NULL );
	xTaskCreate( vTask_ESSP3, "TASK_ESSP3", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP3, NULL );
	xTaskCreate( vTask_ESSP4, "TASK_ESSP4", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP4, NULL );
	xTaskCreate( vTask_ESSP5, "TASK_ESSP5", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP5, NULL );
	xTaskCreate( vTask_ESSP6, "TASK_ESSP6", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP6, NULL );
	xTaskCreate( vTask_ESSP7, "TASK_ESSP7", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP7, NULL );
	xTaskCreate( vTask_ESSP8, "TASK_ESSP8", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP8, NULL );
	xTaskCreate( vTask_ESSP9, "TASK_ESSP9", configMINIMAL_STACK_SIZE, NULL, mainTask_ESSP9, NULL );
	vTaskStartScheduler();
	return 0;
}
