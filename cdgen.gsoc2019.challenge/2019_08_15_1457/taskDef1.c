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


/* Standard includes. */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <stdint.h>

/* Scheduler includes. */
#include "FreeRTOS.h"
#include "queue.h"
#include "croutine.h"
#include "ParallellaUtils.h"
#include "label.c"
#include "task.h"
/* Task Counter Declaration. */
int taskCountTask_ESSP7	=	0;
int taskCountTask_ESSP1	=	0;
int taskCountTask_ESSP4	=	0;
int taskCountTask_ESSP6	=	0;
int taskCountTask_ESSP0	=	0;





	void vTask_ESSP7()
	{
	portTickType xLastWakeTime=xTaskGetTickCount();

		for( ;; )
		{
		updateDebugFlag(700);
		traceTaskPasses(1,1);
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP7();
			taskEXIT_CRITICAL ();

			/*Runnable calls */
			VehicleSpeedSensorDiagnosis();
			BrakeActuatorMonitor();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL ();
			cOUT_Task_ESSP7();
			taskEXIT_CRITICAL ();

			sleepTimerMs(1 , 11);

			taskCountTask_ESSP7++;
			traceTaskPasses(1, taskCountTask_ESSP7);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 10);
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


		uint8_t	APedPosition1;
		uint16_t	APedSensor1Voltage;
		uint8_t	APedPosition2;
		uint8_t	VotedAPedPosition;
		uint16_t	APedSensor2Voltage;
		uint8_t	DesiredThrottlePositionVoltage;
		uint16_t	DesiredThrottlePosition;


	void vTask_ESSP4()
	{
	portTickType xLastWakeTime=xTaskGetTickCount();

		for( ;; )
		{
		updateDebugFlag(700);
		traceTaskPasses(1,1);
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP4();
			taskEXIT_CRITICAL ();

			/*Runnable calls */
			APedSensor();
			APedVoter();
			ThrottleController();
			ThrottleActuator();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL ();
			cOUT_Task_ESSP4();
			taskEXIT_CRITICAL ();

			sleepTimerMs(1 , 13);

			taskCountTask_ESSP4++;
			traceTaskPasses(3, taskCountTask_ESSP4);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 5);
		}
	}


		uint8_t	DecelerationRate2;
		uint8_t	CaliperPosition;
		uint8_t	DecelerationRate1;
		uint8_t	VotedDecelerationRate;
		uint8_t	BrakeForceCurrent;
		uint8_t	ABSMode;
		uint8_t	ABSActivation;
		uint16_t	BrakeForceVoltage;


	void vTask_ESSP6()
	{
	portTickType xLastWakeTime=xTaskGetTickCount();

		for( ;; )
		{
		updateDebugFlag(700);
		traceTaskPasses(1,1);
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP6();
			taskEXIT_CRITICAL ();

			/*Runnable calls */
			VehicleStateMonitor();
			EcuDecelerationSensor();
			DecelerationSensorTranslation();
			DecelerationSensorVoter();
			ABSCalculation();
			BrakeForceActuation();
			CaliperPositionCalculation();
			BrakeActuator();
			EcuBrakeActuator();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL ();
			cOUT_Task_ESSP6();
			taskEXIT_CRITICAL ();

			sleepTimerMs(1 , 14);

			taskCountTask_ESSP6++;
			traceTaskPasses(4, taskCountTask_ESSP6);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 10);
		}
	}


		uint8_t	VehicleSpeed2;
		uint8_t	VehicleSpeed1;


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

			sleepTimerMs(1 , 15);

			taskCountTask_ESSP0++;
			traceTaskPasses(5, taskCountTask_ESSP0);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 5);
		}
	}

