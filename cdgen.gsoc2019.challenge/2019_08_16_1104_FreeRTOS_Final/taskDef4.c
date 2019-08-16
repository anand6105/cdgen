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
*Title 		:   Task Definition
*Description	:	Task Definition with Task Structure
******************************************************************
******************************************************************/


#include "taskDef4.h"

/* Task Counter Declaration. */
int taskCountTask_ESSP9	=	0;
int taskCountTask_ESSP8	=	0;



		uint8_t	WheelSpeed2;
		uint8_t	WheelSpeed1;


	void vTask_ESSP9()
	{
	portTickType xLastWakeTime=xTaskGetTickCount();

		for( ;; )
		{
		updateDebugFlag(700);
		traceTaskPasses(1,1);
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP9();
			taskEXIT_CRITICAL ();

			/*Runnable calls */
			EcuWheelSpeedSensor();
			WheelSpeedSensorTranslation();
			WheelSpeedSensorVoter();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL ();
			cOUT_Task_ESSP9();
			taskEXIT_CRITICAL ();

			sleepTimerMs(1 , 110);

			taskCountTask_ESSP9++;
			traceTaskPasses(10, taskCountTask_ESSP9);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 5);
		}
	}


		uint16_t	IgnitionTime3;
		uint16_t	IgnitionTime4;
		uint16_t	IgnitionTime5;
		uint8_t	IgnitionTime;
		uint16_t	IgnitionTime2;
		uint16_t	IgnitionTime8;
		uint16_t	IgnitionTime1;
		uint16_t	IgnitionTime7;
		uint16_t	IgnitionTime6;


	void vTask_ESSP8()
	{
	portTickType xLastWakeTime=xTaskGetTickCount();

		for( ;; )
		{
		updateDebugFlag(700);
		traceTaskPasses(1,1);
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP8();
			taskEXIT_CRITICAL ();

			/*Runnable calls */
			DiagnosisArbiter();
			IgnitionTiming();
			IgnitionTimeActuation();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL ();
			cOUT_Task_ESSP8();
			taskEXIT_CRITICAL ();

			sleepTimerMs(1 , 19);

			taskCountTask_ESSP8++;
			traceTaskPasses(9, taskCountTask_ESSP8);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 10);
		}
	}

