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
*Title 		:   Runnable Header
*Description	:	Runnable Definition with Runnable delay
******************************************************************
******************************************************************/


/* Standard includes. */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* Scheduler includes. */
#include "runnable.h"
void CheckPlausability(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		CheckPlausability\n");
	usleep(10000);
}
void BrakeActuatorMonitor(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		BrakeActuatorMonitor\n");
	usleep(10000);
}
void DiagnosisArbiter(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		DiagnosisArbiter\n");
	usleep(10000);
}
void VehicleStateMonitor(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		VehicleStateMonitor\n");
	usleep(10000);
}
void BrakeForceCalculation(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		BrakeForceCalculation\n");
	usleep(10000);
}
void BrakeSafetyMonitor(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		BrakeSafetyMonitor\n");
	usleep(10000);
}
void ABSCalculation(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		ABSCalculation\n");
	usleep(10000);
}
void BrakeForceActuation(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		BrakeForceActuation\n");
	usleep(10000);
}
void CaliperPositionCalculation(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		CaliperPositionCalculation\n");
	usleep(10000);
}
void BrakeActuator(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		BrakeActuator\n");
	usleep(10000);
}
void StopLightActuator(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		StopLightActuator\n");
	usleep(10000);
}
void BrakePedalSensorDiagnosis(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		BrakePedalSensorDiagnosis\n");
	usleep(10000);
}
void BrakePedalSensorTranslation(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		BrakePedalSensorTranslation\n");
	usleep(10000);
}
void BrakePedalSensorVoter(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		BrakePedalSensorVoter\n");
	usleep(10000);
}
void DecelerationSensorDiagnosis(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		DecelerationSensorDiagnosis\n");
	usleep(10000);
}
void DecelerationSensorTranslation(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		DecelerationSensorTranslation\n");
	usleep(10000);
}
void VehicleSpeedSensorDiagnosis(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		VehicleSpeedSensorDiagnosis\n");
	usleep(10000);
}
void DecelerationSensorVoter(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		DecelerationSensorVoter\n");
	usleep(10000);
}
void VehicleSpeedSensorVoter(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		VehicleSpeedSensorVoter\n");
	usleep(10000);
}
void VehicleSpeedSensorTranslation(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		VehicleSpeedSensorTranslation\n");
	usleep(10000);
}
void WheelSpeedSensorTranslation(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		WheelSpeedSensorTranslation\n");
	usleep(10000);
}
void WheelSpeedSensorDiagnosis(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		WheelSpeedSensorDiagnosis\n");
	usleep(10000);
}
void CylNumObserver(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		CylNumObserver\n");
	usleep(10000);
}
void WheelSpeedSensorVoter(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		WheelSpeedSensorVoter\n");
	usleep(10000);
}
void ThrottleController(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		ThrottleController\n");
	usleep(10000);
}
void APedVoter(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		APedVoter\n");
	usleep(10000);
}
void BaseFuelMass(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		BaseFuelMass\n");
	usleep(10000);
}
void ThrottleActuator(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		ThrottleActuator\n");
	usleep(10000);
}
void TotalFuelMass(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		TotalFuelMass\n");
	usleep(10000);
}
void TransientFuelMass(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		TransientFuelMass\n");
	usleep(10000);
}
void InjectionTimeActuation(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		InjectionTimeActuation\n");
	usleep(10000);
}
void IgnitionTiming(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		IgnitionTiming\n");
	usleep(10000);
}
void IgnitionTimeActuation(void)	{
	vDisplayMessagePthread(" Task_10MS 	Runnable Execution		IgnitionTimeActuation\n");
	usleep(10000);
}
void BrakeForceArbiter(void)	{
	vDisplayMessagePthread(" Task_20MS 	Runnable Execution		BrakeForceArbiter\n");
	usleep(10000);
}
void EcuBrakeActuator(void)	{
	vDisplayMessagePthread(" Task_5MS 	Runnable Execution		EcuBrakeActuator\n");
	usleep(10000);
}
void EcuStopLightActuator(void)	{
	vDisplayMessagePthread(" Task_5MS 	Runnable Execution		EcuStopLightActuator\n");
	usleep(10000);
}
void EcuBrakePedalSensor(void)	{
	vDisplayMessagePthread(" Task_5MS 	Runnable Execution		EcuBrakePedalSensor\n");
	usleep(10000);
}
void EcuDecelerationSensor(void)	{
	vDisplayMessagePthread(" Task_5MS 	Runnable Execution		EcuDecelerationSensor\n");
	usleep(10000);
}
void EcuVehicleSpeedSensor(void)	{
	vDisplayMessagePthread(" Task_5MS 	Runnable Execution		EcuVehicleSpeedSensor\n");
	usleep(10000);
}
void EcuWheelSpeedSensor(void)	{
	vDisplayMessagePthread(" Task_5MS 	Runnable Execution		EcuWheelSpeedSensor\n");
	usleep(10000);
}
void APedSensor(void)	{
	vDisplayMessagePthread(" Task_5MS 	Runnable Execution		APedSensor\n");
	usleep(10000);
}
void ThrottleSensor(void)	{
	vDisplayMessagePthread(" Task_5MS 	Runnable Execution		ThrottleSensor\n");
	usleep(10000);
}
void MassAirFlowSensor(void)	{
	vDisplayMessagePthread(" Task_5MS 	Runnable Execution		MassAirFlowSensor\n");
	usleep(10000);
}
