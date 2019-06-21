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
*Title 		:   Runnable Definition
*Description	:	Runnable Definition with delay time
******************************************************************
******************************************************************/


/* Standard includes. */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* Scheduler includes. */
#include "runnable.h"
#include "FreeRTOS.h"

void ABSCalculation(void)	{
	vDisplayMessage("Runnable Execution ABSCalculation\n");
	usleep(1000);
}
void APedSensor(void)	{
	vDisplayMessage("Runnable Execution APedSensor\n");
	usleep(1000);
}
void APedVoter(void)	{
	vDisplayMessage("Runnable Execution APedVoter\n");
	usleep(1000);
}
void BaseFuelMass(void)	{
	vDisplayMessage("Runnable Execution BaseFuelMass\n");
	usleep(1000);
}
void BrakeActuator(void)	{
	vDisplayMessage("Runnable Execution BrakeActuator\n");
	usleep(1000);
}
void BrakeActuatorMonitor(void)	{
	vDisplayMessage("Runnable Execution BrakeActuatorMonitor\n");
	usleep(1000);
}
void BrakeForceActuation(void)	{
	vDisplayMessage("Runnable Execution BrakeForceActuation\n");
	usleep(1000);
}
void BrakeForceArbiter(void)	{
	vDisplayMessage("Runnable Execution BrakeForceArbiter\n");
	usleep(1000);
}
void BrakeForceCalculation(void)	{
	vDisplayMessage("Runnable Execution BrakeForceCalculation\n");
	usleep(1000);
}
void BrakePedalSensorDiagnosis(void)	{
	vDisplayMessage("Runnable Execution BrakePedalSensorDiagnosis\n");
	usleep(1000);
}
void BrakePedalSensorTranslation(void)	{
	vDisplayMessage("Runnable Execution BrakePedalSensorTranslation\n");
	usleep(1000);
}
void BrakePedalSensorVoter(void)	{
	vDisplayMessage("Runnable Execution BrakePedalSensorVoter\n");
	usleep(1000);
}
void BrakeSafetyMonitor(void)	{
	vDisplayMessage("Runnable Execution BrakeSafetyMonitor\n");
	usleep(1000);
}
void CaliperPositionCalculation(void)	{
	vDisplayMessage("Runnable Execution CaliperPositionCalculation\n");
	usleep(1000);
}
void CheckPlausability(void)	{
	vDisplayMessage("Runnable Execution CheckPlausability\n");
	usleep(1000);
}
void CylNumObserver(void)	{
	vDisplayMessage("Runnable Execution CylNumObserver\n");
	usleep(1000);
}
void DecelerationSensorDiagnosis(void)	{
	vDisplayMessage("Runnable Execution DecelerationSensorDiagnosis\n");
	usleep(1000);
}
void DecelerationSensorTranslation(void)	{
	vDisplayMessage("Runnable Execution DecelerationSensorTranslation\n");
	usleep(1000);
}
void DecelerationSensorVoter(void)	{
	vDisplayMessage("Runnable Execution DecelerationSensorVoter\n");
	usleep(1000);
}
void DiagnosisArbiter(void)	{
	vDisplayMessage("Runnable Execution DiagnosisArbiter\n");
	usleep(1000);
}
void EcuBrakeActuator(void)	{
	vDisplayMessage("Runnable Execution EcuBrakeActuator\n");
	usleep(1000);
}
void EcuBrakePedalSensor(void)	{
	vDisplayMessage("Runnable Execution EcuBrakePedalSensor\n");
	usleep(1000);
}
void EcuDecelerationSensor(void)	{
	vDisplayMessage("Runnable Execution EcuDecelerationSensor\n");
	usleep(1000);
}
void EcuStopLightActuator(void)	{
	vDisplayMessage("Runnable Execution EcuStopLightActuator\n");
	usleep(1000);
}
void EcuVehicleSpeedSensor(void)	{
	vDisplayMessage("Runnable Execution EcuVehicleSpeedSensor\n");
	usleep(1000);
}
void EcuWheelSpeedSensor(void)	{
	vDisplayMessage("Runnable Execution EcuWheelSpeedSensor\n");
	usleep(1000);
}
void IgnitionTimeActuation(void)	{
	vDisplayMessage("Runnable Execution IgnitionTimeActuation\n");
	usleep(1000);
}
void IgnitionTiming(void)	{
	vDisplayMessage("Runnable Execution IgnitionTiming\n");
	usleep(1000);
}
void InjectionTimeActuation(void)	{
	vDisplayMessage("Runnable Execution InjectionTimeActuation\n");
	usleep(1000);
}
void MassAirFlowSensor(void)	{
	vDisplayMessage("Runnable Execution MassAirFlowSensor\n");
	usleep(1000);
}
void StopLightActuator(void)	{
	vDisplayMessage("Runnable Execution StopLightActuator\n");
	usleep(1000);
}
void ThrottleActuator(void)	{
	vDisplayMessage("Runnable Execution ThrottleActuator\n");
	usleep(1000);
}
void ThrottleController(void)	{
	vDisplayMessage("Runnable Execution ThrottleController\n");
	usleep(1000);
}
void ThrottleSensor(void)	{
	vDisplayMessage("Runnable Execution ThrottleSensor\n");
	usleep(1000);
}
void TotalFuelMass(void)	{
	vDisplayMessage("Runnable Execution TotalFuelMass\n");
	usleep(1000);
}
void TransientFuelMass(void)	{
	vDisplayMessage("Runnable Execution TransientFuelMass\n");
	usleep(1000);
}
void VehicleSpeedSensorDiagnosis(void)	{
	vDisplayMessage("Runnable Execution VehicleSpeedSensorDiagnosis\n");
	usleep(1000);
}
void VehicleSpeedSensorTranslation(void)	{
	vDisplayMessage("Runnable Execution VehicleSpeedSensorTranslation\n");
	usleep(1000);
}
void VehicleSpeedSensorVoter(void)	{
	vDisplayMessage("Runnable Execution VehicleSpeedSensorVoter\n");
	usleep(1000);
}
void VehicleStateMonitor(void)	{
	vDisplayMessage("Runnable Execution VehicleStateMonitor\n");
	usleep(1000);
}
void WheelSpeedSensorDiagnosis(void)	{
	vDisplayMessage("Runnable Execution WheelSpeedSensorDiagnosis\n");
	usleep(1000);
}
void WheelSpeedSensorTranslation(void)	{
	vDisplayMessage("Runnable Execution WheelSpeedSensorTranslation\n");
	usleep(1000);
}
void WheelSpeedSensorVoter(void)	{
	vDisplayMessage("Runnable Execution WheelSpeedSensorVoter\n");
	usleep(1000);
}
