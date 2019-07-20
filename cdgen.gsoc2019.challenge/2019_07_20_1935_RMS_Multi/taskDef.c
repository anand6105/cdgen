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


/* Standard includes. */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <stdint.h>

/* Scheduler includes. */
#include "FreeRTOS.h"
#include "queue.h"
#include "croutine.h"
#include "task.h"
#include "runnable.h"

/* Task Counter Declaration. */
int taskCountTask_ESSP0	=	0;
int taskCountTask_ESSP1	=	0;
int taskCountTask_ESSP2	=	0;
int taskCountTask_ESSP3	=	0;
int taskCountTask_ESSP4	=	0;
int taskCountTask_ESSP5	=	0;
int taskCountTask_ESSP6	=	0;
int taskCountTask_ESSP7	=	0;
int taskCountTask_ESSP8	=	0;
int taskCountTask_ESSP9	=	0;


		extern	uint16_t	VehicleSpeedVoltage2;
		extern	uint16_t	VehicleSpeedVoltage1;
		extern	uint8_t	VehicleSpeed2;
		extern	uint8_t	VehicleSpeed1;
		extern	uint8_t	VotedVehicleSpeed;



		extern	uint16_t	VehicleSpeedVoltage2_Task_ESSP0;
		extern	uint16_t	VehicleSpeedVoltage1_Task_ESSP0;
		extern	uint8_t	VehicleSpeed2_Task_ESSP0;
		extern	uint8_t	VehicleSpeed1_Task_ESSP0;
		extern	uint8_t	VotedVehicleSpeed_Task_ESSP0;




	void cIN_Task_ESSP0()
	{
		VehicleSpeedVoltage2_Task_ESSP0	=	VehicleSpeedVoltage2;
		VehicleSpeedVoltage1_Task_ESSP0	=	VehicleSpeedVoltage1;
		VehicleSpeed2_Task_ESSP0	=	VehicleSpeed2;
		VehicleSpeed1_Task_ESSP0	=	VehicleSpeed1;
		VotedVehicleSpeed_Task_ESSP0	=	VotedVehicleSpeed;
	}

	void cOUT_Task_ESSP0()
	{
		VehicleSpeedVoltage2	=	VehicleSpeedVoltage2_Task_ESSP0;
		VehicleSpeedVoltage1	=	VehicleSpeedVoltage1_Task_ESSP0;
		VehicleSpeed2	=	VehicleSpeed2_Task_ESSP0;
		VehicleSpeed1	=	VehicleSpeed1_Task_ESSP0;
		VotedVehicleSpeed	=	VotedVehicleSpeed_Task_ESSP0;
	}

	void vTask_ESSP0( void *pvParameters )
	{

	updateDebugFlag(700);
	/*Runnable calls */
		EcuVehicleSpeedSensor();
		VehicleSpeedSensorTranslation();
		VehicleSpeedSensorVoter();

	taskCountTask_ESSP0++;
	traceTaskPasses(0, taskCountTask_ESSP0);
	traceRunningTask(0);
	}

		extern	uint8_t	CylinderNumber;
		extern	uint8_t	TriggeredCylinderNumber;
		extern	uint16_t	DecelerationVoltage2;
		extern	uint16_t	DecelerationVoltage1;



		extern	uint8_t	CylinderNumber_Task_ESSP1;
		extern	uint8_t	TriggeredCylinderNumber_Task_ESSP1;
		extern	uint16_t	DecelerationVoltage2_Task_ESSP1;
		extern	uint16_t	DecelerationVoltage1_Task_ESSP1;




	void cIN_Task_ESSP1()
	{
		CylinderNumber_Task_ESSP1	=	CylinderNumber;
		TriggeredCylinderNumber_Task_ESSP1	=	TriggeredCylinderNumber;
		DecelerationVoltage2_Task_ESSP1	=	DecelerationVoltage2;
		DecelerationVoltage1_Task_ESSP1	=	DecelerationVoltage1;
	}

	void cOUT_Task_ESSP1()
	{
		TriggeredCylinderNumber	=	TriggeredCylinderNumber_Task_ESSP1;
	}

	void vTask_ESSP1( void *pvParameters )
	{

	updateDebugFlag(700);
	/*Runnable calls */
		CylNumObserver();
		DecelerationSensorDiagnosis();

	taskCountTask_ESSP1++;
	traceTaskPasses(1, taskCountTask_ESSP1);
	traceRunningTask(0);
	}

		extern	uint16_t	WheelSpeedVoltage1;
		extern	uint16_t	WheelSpeedVoltage2;
		extern	uint16_t	BrakePedalPositionVoltage1;
		extern	uint16_t	BrakePedalPositionVoltage2;



		extern	uint16_t	WheelSpeedVoltage1_Task_ESSP2;
		extern	uint16_t	WheelSpeedVoltage2_Task_ESSP2;
		extern	uint16_t	BrakePedalPositionVoltage1_Task_ESSP2;
		extern	uint16_t	BrakePedalPositionVoltage2_Task_ESSP2;




	void cIN_Task_ESSP2()
	{
		WheelSpeedVoltage1_Task_ESSP2	=	WheelSpeedVoltage1;
		WheelSpeedVoltage2_Task_ESSP2	=	WheelSpeedVoltage2;
		BrakePedalPositionVoltage1_Task_ESSP2	=	BrakePedalPositionVoltage1;
		BrakePedalPositionVoltage2_Task_ESSP2	=	BrakePedalPositionVoltage2;
	}

	void cOUT_Task_ESSP2()
	{
	}

	void vTask_ESSP2( void *pvParameters )
	{

	updateDebugFlag(700);
	/*Runnable calls */
		WheelSpeedSensorDiagnosis();
		BrakePedalSensorDiagnosis();

	taskCountTask_ESSP2++;
	traceTaskPasses(2, taskCountTask_ESSP2);
	traceRunningTask(0);
	}

		extern	uint8_t	MassAirFlow;
		extern	uint16_t	MAFSensorVoltage;
		extern	uint16_t	MAFRate;
		extern	uint16_t	BaseFuelMassPerStroke;
		extern	uint8_t	TransientFuelMassPerStroke;
		extern	uint8_t	TotalFuelMassPerStroke;
		extern	uint16_t	InjectionTime8;
		extern	uint16_t	InjectionTime3;
		extern	uint16_t	InjectionTime7;
		extern	uint16_t	InjectionTime2;
		extern	uint16_t	InjectionTime5;
		extern	uint16_t	InjectionTime6;
		extern	uint8_t	TriggeredCylinderNumber;
		extern	uint16_t	InjectionTime4;
		extern	uint16_t	InjectionTime1;



		extern	uint8_t	MassAirFlow_Task_ESSP3;
		extern	uint16_t	MAFSensorVoltage_Task_ESSP3;
		extern	uint16_t	MAFRate_Task_ESSP3;
		extern	uint16_t	BaseFuelMassPerStroke_Task_ESSP3;
		extern	uint8_t	TransientFuelMassPerStroke_Task_ESSP3;
		extern	uint8_t	TotalFuelMassPerStroke_Task_ESSP3;
		extern	uint16_t	InjectionTime8_Task_ESSP3;
		extern	uint16_t	InjectionTime3_Task_ESSP3;
		extern	uint16_t	InjectionTime7_Task_ESSP3;
		extern	uint16_t	InjectionTime2_Task_ESSP3;
		extern	uint16_t	InjectionTime5_Task_ESSP3;
		extern	uint16_t	InjectionTime6_Task_ESSP3;
		extern	uint8_t	TriggeredCylinderNumber_Task_ESSP3;
		extern	uint16_t	InjectionTime4_Task_ESSP3;
		extern	uint16_t	InjectionTime1_Task_ESSP3;




	void cIN_Task_ESSP3()
	{
		MassAirFlow_Task_ESSP3	=	MassAirFlow;
		MAFSensorVoltage_Task_ESSP3	=	MAFSensorVoltage;
		MAFRate_Task_ESSP3	=	MAFRate;
		BaseFuelMassPerStroke_Task_ESSP3	=	BaseFuelMassPerStroke;
		TransientFuelMassPerStroke_Task_ESSP3	=	TransientFuelMassPerStroke;
		TotalFuelMassPerStroke_Task_ESSP3	=	TotalFuelMassPerStroke;
		InjectionTime8_Task_ESSP3	=	InjectionTime8;
		InjectionTime3_Task_ESSP3	=	InjectionTime3;
		InjectionTime7_Task_ESSP3	=	InjectionTime7;
		InjectionTime2_Task_ESSP3	=	InjectionTime2;
		InjectionTime5_Task_ESSP3	=	InjectionTime5;
		InjectionTime6_Task_ESSP3	=	InjectionTime6;
		TriggeredCylinderNumber_Task_ESSP3	=	TriggeredCylinderNumber;
		InjectionTime4_Task_ESSP3	=	InjectionTime4;
		InjectionTime1_Task_ESSP3	=	InjectionTime1;
	}

	void cOUT_Task_ESSP3()
	{
		MassAirFlow	=	MassAirFlow_Task_ESSP3;
		MAFRate	=	MAFRate_Task_ESSP3;
		BaseFuelMassPerStroke	=	BaseFuelMassPerStroke_Task_ESSP3;
		TransientFuelMassPerStroke	=	TransientFuelMassPerStroke_Task_ESSP3;
		TotalFuelMassPerStroke	=	TotalFuelMassPerStroke_Task_ESSP3;
	}

	void vTask_ESSP3( void *pvParameters )
	{

	updateDebugFlag(700);
	/*Runnable calls */
		MassAirFlowSensor();
		BaseFuelMass();
		TransientFuelMass();
		TotalFuelMass();
		InjectionTimeActuation();

	taskCountTask_ESSP3++;
	traceTaskPasses(3, taskCountTask_ESSP3);
	traceRunningTask(0);
	}

		extern	uint16_t	APedSensor1Voltage;
		extern	uint16_t	APedSensor2Voltage;
		extern	uint8_t	APedPosition2;
		extern	uint8_t	APedPosition1;
		extern	uint8_t	VotedAPedPosition;
		extern	uint16_t	ThrottlePosition;
		extern	uint16_t	DesiredThrottlePosition;
		extern	uint8_t	DesiredThrottlePositionVoltage;



		extern	uint16_t	APedSensor1Voltage_Task_ESSP4;
		extern	uint16_t	APedSensor2Voltage_Task_ESSP4;
		extern	uint8_t	APedPosition2_Task_ESSP4;
		extern	uint8_t	APedPosition1_Task_ESSP4;
		extern	uint8_t	VotedAPedPosition_Task_ESSP4;
		extern	uint16_t	ThrottlePosition_Task_ESSP4;
		extern	uint16_t	DesiredThrottlePosition_Task_ESSP4;
		extern	uint8_t	DesiredThrottlePositionVoltage_Task_ESSP4;




	void cIN_Task_ESSP4()
	{
		APedSensor1Voltage_Task_ESSP4	=	APedSensor1Voltage;
		APedSensor2Voltage_Task_ESSP4	=	APedSensor2Voltage;
		APedPosition2_Task_ESSP4	=	APedPosition2;
		APedPosition1_Task_ESSP4	=	APedPosition1;
		VotedAPedPosition_Task_ESSP4	=	VotedAPedPosition;
		ThrottlePosition_Task_ESSP4	=	ThrottlePosition;
		DesiredThrottlePosition_Task_ESSP4	=	DesiredThrottlePosition;
		DesiredThrottlePositionVoltage_Task_ESSP4	=	DesiredThrottlePositionVoltage;
	}

	void cOUT_Task_ESSP4()
	{
		APedPosition2	=	APedPosition2_Task_ESSP4;
		APedPosition1	=	APedPosition1_Task_ESSP4;
		VotedAPedPosition	=	VotedAPedPosition_Task_ESSP4;
		DesiredThrottlePosition	=	DesiredThrottlePosition_Task_ESSP4;
		DesiredThrottlePositionVoltage	=	DesiredThrottlePositionVoltage_Task_ESSP4;
	}

	void vTask_ESSP4( void *pvParameters )
	{

	updateDebugFlag(700);
	/*Runnable calls */
		APedSensor();
		APedVoter();
		ThrottleController();
		ThrottleActuator();

	taskCountTask_ESSP4++;
	traceTaskPasses(4, taskCountTask_ESSP4);
	traceRunningTask(0);
	}

		extern	uint8_t	ThrottleSensor1Voltage;
		extern	uint16_t	ThrottlePosition;
		extern	uint8_t	ThrottleSensor2Voltage;
		extern	uint16_t	BrakePedalPositionVoltage1;
		extern	uint16_t	BrakePedalPositionVoltage2;
		extern	uint8_t	BrakePedalPosition1;
		extern	uint8_t	BrakePedalPosition2;
		extern	uint8_t	VotedBrakePedalPosition;
		extern	uint8_t	BrakePedalPosition;
		extern	uint8_t	BrakeMonitorLevel;
		extern	uint8_t	ArbitratedDiagnosisRequest;
		extern	uint8_t	BrakeSafetyLevel;
		extern	uint8_t	MonitoredVehicleState;
		extern	uint8_t	BrakeForceFeedback;
		extern	uint8_t	BrakeSafetyState;
		extern	uint8_t	CalculatedBrakeForce;
		extern	uint8_t	ArbitratedBrakeForce;
		extern	uint8_t	BrakeApplication;



		extern	uint8_t	ThrottleSensor1Voltage_Task_ESSP5;
		extern	uint16_t	ThrottlePosition_Task_ESSP5;
		extern	uint8_t	ThrottleSensor2Voltage_Task_ESSP5;
		extern	uint16_t	BrakePedalPositionVoltage1_Task_ESSP5;
		extern	uint16_t	BrakePedalPositionVoltage2_Task_ESSP5;
		extern	uint8_t	BrakePedalPosition1_Task_ESSP5;
		extern	uint8_t	BrakePedalPosition2_Task_ESSP5;
		extern	uint8_t	VotedBrakePedalPosition_Task_ESSP5;
		extern	uint8_t	BrakePedalPosition_Task_ESSP5;
		extern	uint8_t	BrakeMonitorLevel_Task_ESSP5;
		extern	uint8_t	ArbitratedDiagnosisRequest_Task_ESSP5;
		extern	uint8_t	BrakeSafetyLevel_Task_ESSP5;
		extern	uint8_t	MonitoredVehicleState_Task_ESSP5;
		extern	uint8_t	BrakeForceFeedback_Task_ESSP5;
		extern	uint8_t	BrakeSafetyState_Task_ESSP5;
		extern	uint8_t	CalculatedBrakeForce_Task_ESSP5;
		extern	uint8_t	ArbitratedBrakeForce_Task_ESSP5;
		extern	uint8_t	BrakeApplication_Task_ESSP5;




	void cIN_Task_ESSP5()
	{
		ThrottleSensor1Voltage_Task_ESSP5	=	ThrottleSensor1Voltage;
		ThrottlePosition_Task_ESSP5	=	ThrottlePosition;
		ThrottleSensor2Voltage_Task_ESSP5	=	ThrottleSensor2Voltage;
		BrakePedalPositionVoltage1_Task_ESSP5	=	BrakePedalPositionVoltage1;
		BrakePedalPositionVoltage2_Task_ESSP5	=	BrakePedalPositionVoltage2;
		BrakePedalPosition1_Task_ESSP5	=	BrakePedalPosition1;
		BrakePedalPosition2_Task_ESSP5	=	BrakePedalPosition2;
		VotedBrakePedalPosition_Task_ESSP5	=	VotedBrakePedalPosition;
		BrakePedalPosition_Task_ESSP5	=	BrakePedalPosition;
		BrakeMonitorLevel_Task_ESSP5	=	BrakeMonitorLevel;
		ArbitratedDiagnosisRequest_Task_ESSP5	=	ArbitratedDiagnosisRequest;
		BrakeSafetyLevel_Task_ESSP5	=	BrakeSafetyLevel;
		MonitoredVehicleState_Task_ESSP5	=	MonitoredVehicleState;
		BrakeForceFeedback_Task_ESSP5	=	BrakeForceFeedback;
		BrakeSafetyState_Task_ESSP5	=	BrakeSafetyState;
		CalculatedBrakeForce_Task_ESSP5	=	CalculatedBrakeForce;
		ArbitratedBrakeForce_Task_ESSP5	=	ArbitratedBrakeForce;
		BrakeApplication_Task_ESSP5	=	BrakeApplication;
	}

	void cOUT_Task_ESSP5()
	{
		ThrottlePosition	=	ThrottlePosition_Task_ESSP5;
		BrakePedalPositionVoltage1	=	BrakePedalPositionVoltage1_Task_ESSP5;
		BrakePedalPositionVoltage2	=	BrakePedalPositionVoltage2_Task_ESSP5;
		BrakePedalPosition1	=	BrakePedalPosition1_Task_ESSP5;
		BrakePedalPosition2	=	BrakePedalPosition2_Task_ESSP5;
		VotedBrakePedalPosition	=	VotedBrakePedalPosition_Task_ESSP5;
		BrakePedalPosition	=	BrakePedalPosition_Task_ESSP5;
		BrakeSafetyLevel	=	BrakeSafetyLevel_Task_ESSP5;
		BrakeSafetyState	=	BrakeSafetyState_Task_ESSP5;
		BrakeMonitorLevel	=	BrakeMonitorLevel_Task_ESSP5;
		CalculatedBrakeForce	=	CalculatedBrakeForce_Task_ESSP5;
		ArbitratedBrakeForce	=	ArbitratedBrakeForce_Task_ESSP5;
		BrakeApplication	=	BrakeApplication_Task_ESSP5;
	}

	void vTask_ESSP5( void *pvParameters )
	{

	updateDebugFlag(700);
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

	taskCountTask_ESSP5++;
	traceTaskPasses(5, taskCountTask_ESSP5);
	traceRunningTask(0);
	}

		extern	uint8_t	MonitoredVehicleState;
		extern	uint16_t	DecelerationVoltage2;
		extern	uint16_t	DecelerationVoltage1;
		extern	uint8_t	DecelerationRate2;
		extern	uint8_t	DecelerationRate1;
		extern	uint8_t	VotedDecelerationRate;
		extern	uint8_t	VotedVehicleSpeed;
		extern	uint8_t	ABSActivation;
		extern	uint8_t	ABSMode;
		extern	uint8_t	VotedWheelSpeed;
		extern	uint8_t	ArbitratedBrakeForce;
		extern	uint8_t	BrakeForceCurrent;
		extern	uint8_t	BrakeForce;
		extern	uint8_t	CaliperPosition;
		extern	uint16_t	BrakeForceVoltage;



		extern	uint8_t	MonitoredVehicleState_Task_ESSP6;
		extern	uint16_t	DecelerationVoltage2_Task_ESSP6;
		extern	uint16_t	DecelerationVoltage1_Task_ESSP6;
		extern	uint8_t	DecelerationRate2_Task_ESSP6;
		extern	uint8_t	DecelerationRate1_Task_ESSP6;
		extern	uint8_t	VotedDecelerationRate_Task_ESSP6;
		extern	uint8_t	VotedVehicleSpeed_Task_ESSP6;
		extern	uint8_t	ABSActivation_Task_ESSP6;
		extern	uint8_t	ABSMode_Task_ESSP6;
		extern	uint8_t	VotedWheelSpeed_Task_ESSP6;
		extern	uint8_t	ArbitratedBrakeForce_Task_ESSP6;
		extern	uint8_t	BrakeForceCurrent_Task_ESSP6;
		extern	uint8_t	BrakeForce_Task_ESSP6;
		extern	uint8_t	CaliperPosition_Task_ESSP6;
		extern	uint16_t	BrakeForceVoltage_Task_ESSP6;




	void cIN_Task_ESSP6()
	{
		MonitoredVehicleState_Task_ESSP6	=	MonitoredVehicleState;
		DecelerationVoltage2_Task_ESSP6	=	DecelerationVoltage2;
		DecelerationVoltage1_Task_ESSP6	=	DecelerationVoltage1;
		DecelerationRate2_Task_ESSP6	=	DecelerationRate2;
		DecelerationRate1_Task_ESSP6	=	DecelerationRate1;
		VotedDecelerationRate_Task_ESSP6	=	VotedDecelerationRate;
		VotedVehicleSpeed_Task_ESSP6	=	VotedVehicleSpeed;
		ABSActivation_Task_ESSP6	=	ABSActivation;
		ABSMode_Task_ESSP6	=	ABSMode;
		VotedWheelSpeed_Task_ESSP6	=	VotedWheelSpeed;
		ArbitratedBrakeForce_Task_ESSP6	=	ArbitratedBrakeForce;
		BrakeForceCurrent_Task_ESSP6	=	BrakeForceCurrent;
		BrakeForce_Task_ESSP6	=	BrakeForce;
		CaliperPosition_Task_ESSP6	=	CaliperPosition;
		BrakeForceVoltage_Task_ESSP6	=	BrakeForceVoltage;
	}

	void cOUT_Task_ESSP6()
	{
		MonitoredVehicleState	=	MonitoredVehicleState_Task_ESSP6;
		DecelerationVoltage2	=	DecelerationVoltage2_Task_ESSP6;
		DecelerationVoltage1	=	DecelerationVoltage1_Task_ESSP6;
		DecelerationRate2	=	DecelerationRate2_Task_ESSP6;
		DecelerationRate1	=	DecelerationRate1_Task_ESSP6;
		VotedDecelerationRate	=	VotedDecelerationRate_Task_ESSP6;
		ABSActivation	=	ABSActivation_Task_ESSP6;
		ABSMode	=	ABSMode_Task_ESSP6;
		BrakeForceCurrent	=	BrakeForceCurrent_Task_ESSP6;
		BrakeForce	=	BrakeForce_Task_ESSP6;
		CaliperPosition	=	CaliperPosition_Task_ESSP6;
		BrakeForceVoltage	=	BrakeForceVoltage_Task_ESSP6;
	}

	void vTask_ESSP6( void *pvParameters )
	{

	updateDebugFlag(700);
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

	taskCountTask_ESSP6++;
	traceTaskPasses(6, taskCountTask_ESSP6);
	traceRunningTask(0);
	}

		extern	uint16_t	VehicleSpeedVoltage2;
		extern	uint16_t	VehicleSpeedVoltage1;
		extern	uint8_t	BrakeForceFeedback;
		extern	uint8_t	BrakeForce;



		extern	uint16_t	VehicleSpeedVoltage2_Task_ESSP7;
		extern	uint16_t	VehicleSpeedVoltage1_Task_ESSP7;
		extern	uint8_t	BrakeForceFeedback_Task_ESSP7;
		extern	uint8_t	BrakeForce_Task_ESSP7;




	void cIN_Task_ESSP7()
	{
		VehicleSpeedVoltage2_Task_ESSP7	=	VehicleSpeedVoltage2;
		VehicleSpeedVoltage1_Task_ESSP7	=	VehicleSpeedVoltage1;
		BrakeForceFeedback_Task_ESSP7	=	BrakeForceFeedback;
		BrakeForce_Task_ESSP7	=	BrakeForce;
	}

	void cOUT_Task_ESSP7()
	{
	}

	void vTask_ESSP7( void *pvParameters )
	{

	updateDebugFlag(700);
	/*Runnable calls */
		VehicleSpeedSensorDiagnosis();
		BrakeActuatorMonitor();

	taskCountTask_ESSP7++;
	traceTaskPasses(7, taskCountTask_ESSP7);
	traceRunningTask(0);
	}

		extern	uint8_t	ArbitratedDiagnosisRequest;
		extern	uint8_t	IgnitionTime;
		extern	uint16_t	MAFRate;
		extern	uint16_t	IgnitionTime8;
		extern	uint16_t	IgnitionTime1;
		extern	uint16_t	IgnitionTime6;
		extern	uint16_t	IgnitionTime5;
		extern	uint16_t	IgnitionTime4;
		extern	uint16_t	IgnitionTime3;
		extern	uint8_t	TriggeredCylinderNumber;
		extern	uint16_t	IgnitionTime2;
		extern	uint16_t	IgnitionTime7;



		extern	uint8_t	ArbitratedDiagnosisRequest_Task_ESSP8;
		extern	uint8_t	IgnitionTime_Task_ESSP8;
		extern	uint16_t	MAFRate_Task_ESSP8;
		extern	uint16_t	IgnitionTime8_Task_ESSP8;
		extern	uint16_t	IgnitionTime1_Task_ESSP8;
		extern	uint16_t	IgnitionTime6_Task_ESSP8;
		extern	uint16_t	IgnitionTime5_Task_ESSP8;
		extern	uint16_t	IgnitionTime4_Task_ESSP8;
		extern	uint16_t	IgnitionTime3_Task_ESSP8;
		extern	uint8_t	TriggeredCylinderNumber_Task_ESSP8;
		extern	uint16_t	IgnitionTime2_Task_ESSP8;
		extern	uint16_t	IgnitionTime7_Task_ESSP8;




	void cIN_Task_ESSP8()
	{
		ArbitratedDiagnosisRequest_Task_ESSP8	=	ArbitratedDiagnosisRequest;
		IgnitionTime_Task_ESSP8	=	IgnitionTime;
		MAFRate_Task_ESSP8	=	MAFRate;
		IgnitionTime8_Task_ESSP8	=	IgnitionTime8;
		IgnitionTime1_Task_ESSP8	=	IgnitionTime1;
		IgnitionTime6_Task_ESSP8	=	IgnitionTime6;
		IgnitionTime5_Task_ESSP8	=	IgnitionTime5;
		IgnitionTime4_Task_ESSP8	=	IgnitionTime4;
		IgnitionTime3_Task_ESSP8	=	IgnitionTime3;
		TriggeredCylinderNumber_Task_ESSP8	=	TriggeredCylinderNumber;
		IgnitionTime2_Task_ESSP8	=	IgnitionTime2;
		IgnitionTime7_Task_ESSP8	=	IgnitionTime7;
	}

	void cOUT_Task_ESSP8()
	{
		IgnitionTime	=	IgnitionTime_Task_ESSP8;
		IgnitionTime8	=	IgnitionTime8_Task_ESSP8;
		IgnitionTime7	=	IgnitionTime7_Task_ESSP8;
	}

	void vTask_ESSP8( void *pvParameters )
	{

	updateDebugFlag(700);
	/*Runnable calls */
		DiagnosisArbiter();
		IgnitionTiming();
		IgnitionTimeActuation();

	taskCountTask_ESSP8++;
	traceTaskPasses(8, taskCountTask_ESSP8);
	traceRunningTask(0);
	}

		extern	uint16_t	WheelSpeedVoltage1;
		extern	uint16_t	WheelSpeedVoltage2;
		extern	uint8_t	WheelSpeed1;
		extern	uint8_t	WheelSpeed2;
		extern	uint8_t	VotedWheelSpeed;



		extern	uint16_t	WheelSpeedVoltage1_Task_ESSP9;
		extern	uint16_t	WheelSpeedVoltage2_Task_ESSP9;
		extern	uint8_t	WheelSpeed1_Task_ESSP9;
		extern	uint8_t	WheelSpeed2_Task_ESSP9;
		extern	uint8_t	VotedWheelSpeed_Task_ESSP9;




	void cIN_Task_ESSP9()
	{
		WheelSpeedVoltage1_Task_ESSP9	=	WheelSpeedVoltage1;
		WheelSpeedVoltage2_Task_ESSP9	=	WheelSpeedVoltage2;
		WheelSpeed1_Task_ESSP9	=	WheelSpeed1;
		WheelSpeed2_Task_ESSP9	=	WheelSpeed2;
		VotedWheelSpeed_Task_ESSP9	=	VotedWheelSpeed;
	}

	void cOUT_Task_ESSP9()
	{
		WheelSpeedVoltage1	=	WheelSpeedVoltage1_Task_ESSP9;
		WheelSpeedVoltage2	=	WheelSpeedVoltage2_Task_ESSP9;
		WheelSpeed1	=	WheelSpeed1_Task_ESSP9;
		WheelSpeed2	=	WheelSpeed2_Task_ESSP9;
		VotedWheelSpeed	=	VotedWheelSpeed_Task_ESSP9;
	}

	void vTask_ESSP9( void *pvParameters )
	{

	updateDebugFlag(700);
	/*Runnable calls */
		EcuWheelSpeedSensor();
		WheelSpeedSensorTranslation();
		WheelSpeedSensorVoter();

	taskCountTask_ESSP9++;
	traceTaskPasses(9, taskCountTask_ESSP9);
	traceRunningTask(0);
	}

