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


#include "taskDef3.h"

/* Task Counter Declaration. */
int taskCountTask_ESSP7	=	0;
int taskCountTask_ESSP5	=	0;





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

			sleepTimerMs(1 , 18);

			taskCountTask_ESSP7++;
			traceTaskPasses(8, taskCountTask_ESSP7);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 10);
		}
	}


		uint8_t	BrakeSafetyLevel;
		uint8_t	BrakePedalPosition1;
		uint8_t	BrakeApplication;
		uint8_t	ThrottleSensor2Voltage;
		uint8_t	CalculatedBrakeForce;
		uint8_t	BrakePedalPosition;
		uint8_t	ThrottleSensor1Voltage;
		uint8_t	BrakePedalPosition2;
		uint8_t	BrakeSafetyState;
		uint8_t	VotedBrakePedalPosition;
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

			sleepTimerMs(1 , 16);

			taskCountTask_ESSP5++;
			traceTaskPasses(6, taskCountTask_ESSP5);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 5);
		}
	}

