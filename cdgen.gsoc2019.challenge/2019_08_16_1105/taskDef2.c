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



	uint8_t	BrakeForceCurrent;
	uint8_t	VotedDecelerationRate;
	uint8_t	DecelerationRate2;
	uint8_t	DecelerationRate1;
	uint8_t	ABSMode;
	uint8_t	ABSActivation;
	uint8_t	CaliperPosition;
	uint16_t	BrakeForceVoltage;




	void vTask_ESSP6()
	{
		updateDebugFlag(700);
		traceTaskPasses(1,1);

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

			sleepTimerMs(1 , 17);

			taskCountTask_ESSP6++;
			traceTaskPasses(6, taskCountTask_ESSP6);
			traceRunningTask(0);
	}


	uint16_t	APedSensor1Voltage;
	uint8_t	DesiredThrottlePositionVoltage;
	uint8_t	APedPosition1;
	uint8_t	VotedAPedPosition;
	uint16_t	DesiredThrottlePosition;
	uint16_t	APedSensor2Voltage;
	uint8_t	APedPosition2;




	void vTask_ESSP4()
	{
		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			APedSensor();
			APedVoter();
			ThrottleController();
			ThrottleActuator();

			sleepTimerMs(1 , 15);

			taskCountTask_ESSP4++;
			traceTaskPasses(4, taskCountTask_ESSP4);
			traceRunningTask(0);
	}

