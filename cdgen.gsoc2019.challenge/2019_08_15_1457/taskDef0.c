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
int taskCountTask_ESSP2	=	0;
int taskCountTask_ESSP3	=	0;
int taskCountTask_ESSP5	=	0;
int taskCountTask_ESSP8	=	0;
int taskCountTask_ESSP9	=	0;





	void vTask_ESSP2()
	{
	portTickType xLastWakeTime=xTaskGetTickCount();

		for( ;; )
		{
		updateDebugFlag(700);
		traceTaskPasses(1,1);
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP2();
			taskEXIT_CRITICAL ();

			/*Runnable calls */
			WheelSpeedSensorDiagnosis();
			BrakePedalSensorDiagnosis();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL ();
			cOUT_Task_ESSP2();
			taskEXIT_CRITICAL ();

			sleepTimerMs(1 , 11);

			taskCountTask_ESSP2++;
			traceTaskPasses(1, taskCountTask_ESSP2);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 10);
		}
	}


		uint16_t	MAFSensorVoltage;
		uint16_t	InjectionTime1;
		uint16_t	InjectionTime5;
		uint8_t	TransientFuelMassPerStroke;
		uint16_t	InjectionTime8;
		uint16_t	BaseFuelMassPerStroke;
		uint16_t	InjectionTime3;
		uint16_t	InjectionTime2;
		uint8_t	MassAirFlow;
		uint16_t	InjectionTime6;
		uint16_t	InjectionTime7;
		uint8_t	TotalFuelMassPerStroke;
		uint16_t	InjectionTime4;


	void vTask_ESSP3()
	{
	portTickType xLastWakeTime=xTaskGetTickCount();

		for( ;; )
		{
		updateDebugFlag(700);
		traceTaskPasses(1,1);
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP3();
			taskEXIT_CRITICAL ();

			/*Runnable calls */
			MassAirFlowSensor();
			BaseFuelMass();
			TransientFuelMass();
			TotalFuelMass();
			InjectionTimeActuation();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL ();
			cOUT_Task_ESSP3();
			taskEXIT_CRITICAL ();

			sleepTimerMs(1 , 12);

			taskCountTask_ESSP3++;
			traceTaskPasses(2, taskCountTask_ESSP3);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 5);
		}
	}


		uint8_t	BrakeSafetyState;
		uint8_t	BrakePedalPosition1;
		uint8_t	ThrottleSensor2Voltage;
		uint8_t	VotedBrakePedalPosition;
		uint8_t	CalculatedBrakeForce;
		uint8_t	ThrottleSensor1Voltage;
		uint8_t	BrakeApplication;
		uint8_t	BrakeSafetyLevel;
		uint8_t	BrakePedalPosition2;
		uint8_t	BrakePedalPosition;
		uint8_t	BrakeMonitorLevel;


	void vTask_ESSP5()
	{
	portTickType xLastWakeTime=xTaskGetTickCount();

		for( ;; )
		{
		updateDebugFlag(700);
		traceTaskPasses(1,1);
			/*Cin - Create local variables and copy the actual variable to them */
			taskENTER_CRITICAL ();
			cIN_Task_ESSP5();
			taskEXIT_CRITICAL ();

			/*Runnable calls */
			ThrottleSensor();
			EcuBrakePedalSensor();
			BrakePedalSensorTranslation();
			BrakePedalSensorVoter();
			CheckPlausability();
			BrakeSafetyMonitor();
			BrakeForceCalculation();
			BrakeForceArbiter();
			StopLightActuator();
			EcuStopLightActuator();

			/*Cout - Write back the local variables back to the actual variables */
			taskENTER_CRITICAL ();
			cOUT_Task_ESSP5();
			taskEXIT_CRITICAL ();

			sleepTimerMs(1 , 13);

			taskCountTask_ESSP5++;
			traceTaskPasses(3, taskCountTask_ESSP5);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 5);
		}
	}


		uint16_t	IgnitionTime8;
		uint8_t	IgnitionTime;
		uint16_t	IgnitionTime3;
		uint16_t	IgnitionTime5;
		uint16_t	IgnitionTime6;
		uint16_t	IgnitionTime1;
		uint16_t	IgnitionTime2;
		uint16_t	IgnitionTime7;
		uint16_t	IgnitionTime4;


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

			sleepTimerMs(1 , 14);

			taskCountTask_ESSP8++;
			traceTaskPasses(4, taskCountTask_ESSP8);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 10);
		}
	}


		uint8_t	WheelSpeed1;
		uint8_t	WheelSpeed2;


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

			sleepTimerMs(1 , 15);

			taskCountTask_ESSP9++;
			traceTaskPasses(5, taskCountTask_ESSP9);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 5);
		}
	}

