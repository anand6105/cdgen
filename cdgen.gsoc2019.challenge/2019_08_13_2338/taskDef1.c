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
int taskCountTask_ESSP7	=	0;
int taskCountTask_ESSP0	=	0;
int taskCountTask_ESSP1	=	0;
int taskCountTask_ESSP4	=	0;
int taskCountTask_ESSP6	=	0;



	void vTask_ESSP7()
	{




		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			VehicleSpeedSensorDiagnosis();
			BrakeActuatorMonitor();

			sleepTimerMs(1 , 11);

			taskCountTask_ESSP7++;
			traceTaskPasses(1, taskCountTask_ESSP7);
			traceRunningTask(0);
	}


	void vTask_ESSP0()
	{


		uint8_t	VehicleSpeed1_Task_ESSP0	=	255;
		uint8_t	VehicleSpeed2_Task_ESSP0	=	255;


		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			EcuVehicleSpeedSensor();
			VehicleSpeedSensorTranslation();
			VehicleSpeedSensorVoter();

			sleepTimerMs(1 , 12);

			taskCountTask_ESSP0++;
			traceTaskPasses(2, taskCountTask_ESSP0);
			traceRunningTask(0);
	}


	void vTask_ESSP1()
	{


		uint8_t	CylinderNumber_Task_ESSP1	=	255;


		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			CylNumObserver();
			DecelerationSensorDiagnosis();

			sleepTimerMs(1 , 13);

			taskCountTask_ESSP1++;
			traceTaskPasses(3, taskCountTask_ESSP1);
			traceRunningTask(0);
	}


	void vTask_ESSP4()
	{


		uint8_t	APedPosition2_Task_ESSP4	=	255;
		uint16_t	APedSensor1Voltage_Task_ESSP4	=	65535;
		uint16_t	APedSensor2Voltage_Task_ESSP4	=	65535;
		uint8_t	DesiredThrottlePositionVoltage_Task_ESSP4	=	255;
		uint16_t	DesiredThrottlePosition_Task_ESSP4	=	65535;
		uint8_t	APedPosition1_Task_ESSP4	=	255;
		uint8_t	VotedAPedPosition_Task_ESSP4	=	255;


		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			APedSensor();
			APedVoter();
			ThrottleController();
			ThrottleActuator();

			sleepTimerMs(1 , 14);

			taskCountTask_ESSP4++;
			traceTaskPasses(4, taskCountTask_ESSP4);
			traceRunningTask(0);
	}


	void vTask_ESSP6()
	{


		uint8_t	DecelerationRate2_Task_ESSP6	=	255;
		uint16_t	BrakeForceVoltage_Task_ESSP6	=	65535;
		uint8_t	ABSMode_Task_ESSP6	=	255;
		uint8_t	BrakeForceCurrent_Task_ESSP6	=	255;
		uint8_t	VotedDecelerationRate_Task_ESSP6	=	255;
		uint8_t	ABSActivation_Task_ESSP6	=	255;
		uint8_t	CaliperPosition_Task_ESSP6	=	255;
		uint8_t	DecelerationRate1_Task_ESSP6	=	255;


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

			sleepTimerMs(1 , 15);

			taskCountTask_ESSP6++;
			traceTaskPasses(5, taskCountTask_ESSP6);
			traceRunningTask(0);
	}

