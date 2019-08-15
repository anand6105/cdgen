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


#include "taskDef1.h"

/* Task Counter Declaration. */
int taskCountTask_ESSP6	=	0;
int taskCountTask_ESSP7	=	0;
int taskCountTask_ESSP4	=	0;
int taskCountTask_ESSP0	=	0;
int taskCountTask_ESSP1	=	0;



	uint8_t	BrakeForceCurrent;
	uint8_t	ABSActivation;
	uint8_t	ABSMode;
	uint8_t	VotedDecelerationRate;
	uint16_t	BrakeForceVoltage;
	uint8_t	DecelerationRate1;
	uint8_t	CaliperPosition;
	uint8_t	DecelerationRate2;




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

			sleepTimerMs(1 , 11);

			taskCountTask_ESSP6++;
			traceTaskPasses(1, taskCountTask_ESSP6);
			traceRunningTask(0);
	}






	void vTask_ESSP7()
	{
		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			VehicleSpeedSensorDiagnosis();
			BrakeActuatorMonitor();

			sleepTimerMs(1 , 12);

			taskCountTask_ESSP7++;
			traceTaskPasses(2, taskCountTask_ESSP7);
			traceRunningTask(0);
	}


	uint16_t	DesiredThrottlePosition;
	uint8_t	APedPosition1;
	uint16_t	APedSensor2Voltage;
	uint8_t	VotedAPedPosition;
	uint16_t	APedSensor1Voltage;
	uint8_t	DesiredThrottlePositionVoltage;
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

			sleepTimerMs(1 , 13);

			taskCountTask_ESSP4++;
			traceTaskPasses(3, taskCountTask_ESSP4);
			traceRunningTask(0);
	}


	uint8_t	VehicleSpeed2;
	uint8_t	VehicleSpeed1;




	void vTask_ESSP0()
	{
		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			EcuVehicleSpeedSensor();
			VehicleSpeedSensorTranslation();
			VehicleSpeedSensorVoter();

			sleepTimerMs(1 , 14);

			taskCountTask_ESSP0++;
			traceTaskPasses(4, taskCountTask_ESSP0);
			traceRunningTask(0);
	}


	uint8_t	CylinderNumber;




	void vTask_ESSP1()
	{
		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			CylNumObserver();
			DecelerationSensorDiagnosis();

			sleepTimerMs(1 , 15);

			taskCountTask_ESSP1++;
			traceTaskPasses(5, taskCountTask_ESSP1);
			traceRunningTask(0);
	}

