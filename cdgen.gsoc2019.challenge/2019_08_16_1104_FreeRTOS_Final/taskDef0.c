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


#include "taskDef0.h"

/* Task Counter Declaration. */
int taskCountTask_ESSP0	=	0;
int taskCountTask_ESSP1	=	0;



		uint8_t	VehicleSpeed1;
		uint8_t	VehicleSpeed2;


	void vTask_ESSP0()
	{
	portTickType xLastWakeTime=xTaskGetTickCount();

		for( ;; )
		{
		updateDebugFlag(700);
		traceTaskPasses(1,1);
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP0();
			taskEXIT_CRITICAL ();

			/*Runnable calls */
			EcuVehicleSpeedSensor();
			VehicleSpeedSensorTranslation();
			VehicleSpeedSensorVoter();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL ();
			cOUT_Task_ESSP0();
			taskEXIT_CRITICAL ();

			sleepTimerMs(1 , 11);

			taskCountTask_ESSP0++;
			traceTaskPasses(1, taskCountTask_ESSP0);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 5);
		}
	}


		uint8_t	CylinderNumber;


	void vTask_ESSP1()
	{
	portTickType xLastWakeTime=xTaskGetTickCount();

		for( ;; )
		{
		updateDebugFlag(700);
		traceTaskPasses(1,1);
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP1();
			taskEXIT_CRITICAL ();

			/*Runnable calls */
			CylNumObserver();
			DecelerationSensorDiagnosis();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL ();
			cOUT_Task_ESSP1();
			taskEXIT_CRITICAL ();

			sleepTimerMs(1 , 12);

			taskCountTask_ESSP1++;
			traceTaskPasses(2, taskCountTask_ESSP1);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 10);
		}
	}

