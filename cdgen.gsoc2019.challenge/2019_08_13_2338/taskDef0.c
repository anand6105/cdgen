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
int taskCountTask_ESSP8	=	0;
int taskCountTask_ESSP3	=	0;
int taskCountTask_ESSP2	=	0;
int taskCountTask_ESSP5	=	0;
int taskCountTask_ESSP9	=	0;



	void vTask_ESSP8()
	{


		uint16_t	IgnitionTime1_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime8_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime5_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime4_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime6_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime2_Task_ESSP8	=	65535;
		uint8_t	IgnitionTime_Task_ESSP8	=	255;
		uint16_t	IgnitionTime7_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime3_Task_ESSP8	=	65535;


		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			DiagnosisArbiter();
			IgnitionTiming();
			IgnitionTimeActuation();

			sleepTimerMs(1 , 11);

			taskCountTask_ESSP8++;
			traceTaskPasses(1, taskCountTask_ESSP8);
			traceRunningTask(0);
	}


	void vTask_ESSP3()
	{


		uint16_t	InjectionTime6_Task_ESSP3	=	65535;
		uint16_t	InjectionTime3_Task_ESSP3	=	65535;
		uint8_t	TransientFuelMassPerStroke_Task_ESSP3	=	255;
		uint8_t	MassAirFlow_Task_ESSP3	=	255;
		uint16_t	InjectionTime5_Task_ESSP3	=	65535;
		uint8_t	TotalFuelMassPerStroke_Task_ESSP3	=	255;
		uint16_t	InjectionTime8_Task_ESSP3	=	65535;
		uint16_t	InjectionTime1_Task_ESSP3	=	65535;
		uint16_t	InjectionTime4_Task_ESSP3	=	65535;
		uint16_t	MAFSensorVoltage_Task_ESSP3	=	65535;
		uint16_t	InjectionTime7_Task_ESSP3	=	65535;
		uint16_t	BaseFuelMassPerStroke_Task_ESSP3	=	65535;
		uint16_t	InjectionTime2_Task_ESSP3	=	65535;


		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			MassAirFlowSensor();
			BaseFuelMass();
			TransientFuelMass();
			TotalFuelMass();
			InjectionTimeActuation();

			sleepTimerMs(1 , 12);

			taskCountTask_ESSP3++;
			traceTaskPasses(2, taskCountTask_ESSP3);
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


	void vTask_ESSP5()
	{


		uint8_t	BrakeSafetyLevel_Task_ESSP5	=	255;
		uint8_t	CalculatedBrakeForce_Task_ESSP5	=	255;
		uint8_t	BrakeApplication_Task_ESSP5	=	255;
		uint8_t	ThrottleSensor2Voltage_Task_ESSP5	=	255;
		uint8_t	BrakeMonitorLevel_Task_ESSP5	=	255;
		uint8_t	BrakePedalPosition1_Task_ESSP5	=	255;
		uint8_t	BrakeSafetyState_Task_ESSP5	=	255;
		uint8_t	VotedBrakePedalPosition_Task_ESSP5	=	255;
		uint8_t	BrakePedalPosition2_Task_ESSP5	=	255;
		uint8_t	BrakePedalPosition_Task_ESSP5	=	255;
		uint8_t	ThrottleSensor1Voltage_Task_ESSP5	=	255;


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


	void vTask_ESSP9()
	{


		uint8_t	WheelSpeed1_Task_ESSP9	=	255;
		uint8_t	WheelSpeed2_Task_ESSP9	=	255;


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

