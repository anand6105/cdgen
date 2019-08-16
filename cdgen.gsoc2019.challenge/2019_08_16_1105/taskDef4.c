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


#include "taskDef4.h"

/* Task Counter Declaration. */
int taskCountTask_ESSP9	=	0;
int taskCountTask_ESSP8	=	0;



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

			sleepTimerMs(1 , 110);

			taskCountTask_ESSP9++;
			traceTaskPasses(9, taskCountTask_ESSP9);
			traceRunningTask(0);
	}


	uint16_t	IgnitionTime4;
	uint16_t	IgnitionTime1;
	uint8_t	IgnitionTime;
	uint16_t	IgnitionTime2;
	uint16_t	IgnitionTime6;
	uint16_t	IgnitionTime3;
	uint16_t	IgnitionTime8;
	uint16_t	IgnitionTime5;
	uint16_t	IgnitionTime7;




	void vTask_ESSP8()
	{
		updateDebugFlag(700);
		traceTaskPasses(1,1);

			/*Runnable calls */
			DiagnosisArbiter();
			IgnitionTiming();
			IgnitionTimeActuation();

			sleepTimerMs(1 , 19);

			taskCountTask_ESSP8++;
			traceTaskPasses(8, taskCountTask_ESSP8);
			traceRunningTask(0);
	}

