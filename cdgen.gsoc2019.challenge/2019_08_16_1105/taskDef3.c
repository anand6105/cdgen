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
		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			VehicleSpeedSensorDiagnosis();
			BrakeActuatorMonitor();

			sleepTimerMs(1 , 18);

			taskCountTask_ESSP7++;
			traceTaskPasses(7, taskCountTask_ESSP7);
			traceRunningTask(0);
	}


	uint8_t	BrakePedalPosition;
	uint8_t	CalculatedBrakeForce;
	uint8_t	ThrottleSensor1Voltage;
	uint8_t	VotedBrakePedalPosition;
	uint8_t	ThrottleSensor2Voltage;
	uint8_t	BrakeMonitorLevel;
	uint8_t	BrakePedalPosition2;
	uint8_t	BrakeSafetyLevel;
	uint8_t	BrakePedalPosition1;
	uint8_t	BrakeApplication;
	uint8_t	BrakeSafetyState;




	void vTask_ESSP5()
	{
		updateDebugFlag(700);
		traceTaskPasses(1,1);

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

			sleepTimerMs(1 , 16);

			taskCountTask_ESSP5++;
			traceTaskPasses(5, taskCountTask_ESSP5);
			traceRunningTask(0);
	}

