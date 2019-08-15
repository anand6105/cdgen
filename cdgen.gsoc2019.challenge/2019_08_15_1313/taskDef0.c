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
int taskCountTask_ESSP3	=	0;
int taskCountTask_ESSP8	=	0;
int taskCountTask_ESSP2	=	0;
int taskCountTask_ESSP5	=	0;
int taskCountTask_ESSP9	=	0;



	uint16_t	InjectionTime5;
	uint16_t	InjectionTime2;
	uint16_t	BaseFuelMassPerStroke;
	uint16_t	InjectionTime3;
	uint16_t	MAFSensorVoltage;
	uint8_t	TransientFuelMassPerStroke;
	uint16_t	InjectionTime1;
	uint8_t	MassAirFlow;
	uint16_t	InjectionTime4;
	uint8_t	TotalFuelMassPerStroke;
	uint16_t	InjectionTime7;
	uint16_t	InjectionTime6;
	uint16_t	InjectionTime8;




	void vTask_ESSP3()
	{
		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			MassAirFlowSensor();
			BaseFuelMass();
			TransientFuelMass();
			TotalFuelMass();
			InjectionTimeActuation();

			sleepTimerMs(1 , 11);

			taskCountTask_ESSP3++;
			traceTaskPasses(1, taskCountTask_ESSP3);
			traceRunningTask(0);
	}


	uint16_t	IgnitionTime7;
	uint16_t	IgnitionTime3;
	uint8_t	IgnitionTime;
	uint16_t	IgnitionTime5;
	uint16_t	IgnitionTime4;
	uint16_t	IgnitionTime1;
	uint16_t	IgnitionTime6;
	uint16_t	IgnitionTime8;
	uint16_t	IgnitionTime2;




	void vTask_ESSP8()
	{
		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			DiagnosisArbiter();
			IgnitionTiming();
			IgnitionTimeActuation();

			sleepTimerMs(1 , 12);

			taskCountTask_ESSP8++;
			traceTaskPasses(2, taskCountTask_ESSP8);
			traceRunningTask(0);
	}






	void vTask_ESSP2()
	{
		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			WheelSpeedSensorDiagnosis();
			BrakePedalSensorDiagnosis();

			sleepTimerMs(1 , 13);

			taskCountTask_ESSP2++;
			traceTaskPasses(3, taskCountTask_ESSP2);
			traceRunningTask(0);
	}


	uint8_t	ThrottleSensor1Voltage;
	uint8_t	BrakeSafetyState;
	uint8_t	ThrottleSensor2Voltage;
	uint8_t	VotedBrakePedalPosition;
	uint8_t	CalculatedBrakeForce;
	uint8_t	BrakeSafetyLevel;
	uint8_t	BrakePedalPosition2;
	uint8_t	BrakeApplication;
	uint8_t	BrakePedalPosition1;
	uint8_t	BrakePedalPosition;
	uint8_t	BrakeMonitorLevel;




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

			sleepTimerMs(1 , 14);

			taskCountTask_ESSP5++;
			traceTaskPasses(4, taskCountTask_ESSP5);
			traceRunningTask(0);
	}


	uint8_t	WheelSpeed2;
	uint8_t	WheelSpeed1;




	void vTask_ESSP9()
	{
		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			EcuWheelSpeedSensor();
			WheelSpeedSensorTranslation();
			WheelSpeedSensorVoter();

			sleepTimerMs(1 , 15);

			taskCountTask_ESSP9++;
			traceTaskPasses(5, taskCountTask_ESSP9);
			traceRunningTask(0);
	}

