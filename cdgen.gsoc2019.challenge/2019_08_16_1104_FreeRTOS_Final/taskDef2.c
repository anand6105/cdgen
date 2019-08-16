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


#include "taskDef2.h"

/* Task Counter Declaration. */
int taskCountTask_ESSP6	=	0;
int taskCountTask_ESSP4	=	0;



		uint8_t	ABSActivation;
		uint8_t	CaliperPosition;
		uint8_t	BrakeForceCurrent;
		uint8_t	ABSMode;
		uint16_t	BrakeForceVoltage;
		uint8_t	VotedDecelerationRate;
		uint8_t	DecelerationRate1;
		uint8_t	DecelerationRate2;


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

			sleepTimerMs(1 , 17);

			taskCountTask_ESSP6++;
			traceTaskPasses(7, taskCountTask_ESSP6);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 10);
		}
	}


		uint8_t	APedPosition1;
		uint16_t	DesiredThrottlePosition;
		uint16_t	APedSensor2Voltage;
		uint8_t	VotedAPedPosition;
		uint8_t	APedPosition2;
		uint16_t	APedSensor1Voltage;
		uint8_t	DesiredThrottlePositionVoltage;


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

			sleepTimerMs(1 , 15);

			taskCountTask_ESSP4++;
			traceTaskPasses(5, taskCountTask_ESSP4);
			traceRunningTask(0);
			vTaskDelayUntil(&xLastWakeTime, 5);
		}
	}

