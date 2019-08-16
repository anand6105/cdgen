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
int taskCountTask_ESSP3	=	0;
int taskCountTask_ESSP2	=	0;



	uint16_t	InjectionTime3;
	uint16_t	InjectionTime6;
	uint16_t	MAFSensorVoltage;
	uint16_t	InjectionTime1;
	uint16_t	InjectionTime4;
	uint16_t	BaseFuelMassPerStroke;
	uint16_t	InjectionTime8;
	uint16_t	InjectionTime5;
	uint16_t	InjectionTime2;
	uint16_t	InjectionTime7;
	uint8_t	TotalFuelMassPerStroke;
	uint8_t	TransientFuelMassPerStroke;
	uint8_t	MassAirFlow;




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

			sleepTimerMs(1 , 14);

			taskCountTask_ESSP3++;
			traceTaskPasses(3, taskCountTask_ESSP3);
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
			traceTaskPasses(2, taskCountTask_ESSP2);
			traceRunningTask(0);
	}

