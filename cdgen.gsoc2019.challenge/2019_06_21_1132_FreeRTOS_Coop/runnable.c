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
void EcuVehicleSpeedSensor (void)	{
	vDisplayMessage(" Task_ESSP0 	Runnable Execution		EcuVehicleSpeedSensor\n");
	//usleep(1000);
}
void VehicleSpeedSensorTranslation (void)	{
	vDisplayMessage(" Task_ESSP0 	Runnable Execution		VehicleSpeedSensorTranslation\n");
	//usleep(1000);
}
void VehicleSpeedSensorVoter (void)	{
	vDisplayMessage(" Task_ESSP0 	Runnable Execution		VehicleSpeedSensorVoter\n");
	//usleep(1000);
}
void CylNumObserver (void)	{
	vDisplayMessage(" Task_ESSP1 	Runnable Execution		CylNumObserver\n");
	//usleep(1000);
}
void DecelerationSensorDiagnosis (void)	{
	vDisplayMessage(" Task_ESSP1 	Runnable Execution		DecelerationSensorDiagnosis\n");
	//usleep(1000);
}
void WheelSpeedSensorDiagnosis (void)	{
	vDisplayMessage(" Task_ESSP2 	Runnable Execution		WheelSpeedSensorDiagnosis\n");
	//usleep(1000);
}
void BrakePedalSensorDiagnosis (void)	{
	vDisplayMessage(" Task_ESSP2 	Runnable Execution		BrakePedalSensorDiagnosis\n");
	//usleep(1000);
}
void MassAirFlowSensor (void)	{
	vDisplayMessage(" Task_ESSP3 	Runnable Execution		MassAirFlowSensor\n");
	//usleep(1000);
}
void BaseFuelMass (void)	{
	vDisplayMessage(" Task_ESSP3 	Runnable Execution		BaseFuelMass\n");
	//usleep(1000);
}
void TransientFuelMass (void)	{
	vDisplayMessage(" Task_ESSP3 	Runnable Execution		TransientFuelMass\n");
	//usleep(1000);
}
void TotalFuelMass (void)	{
	vDisplayMessage(" Task_ESSP3 	Runnable Execution		TotalFuelMass\n");
	//usleep(1000);
}
void InjectionTimeActuation (void)	{
	vDisplayMessage(" Task_ESSP3 	Runnable Execution		InjectionTimeActuation\n");
	//usleep(1000);
}
void APedSensor (void)	{
	vDisplayMessage(" Task_ESSP4 	Runnable Execution		APedSensor\n");
	//usleep(1000);
}
void APedVoter (void)	{
	vDisplayMessage(" Task_ESSP4 	Runnable Execution		APedVoter\n");
	//usleep(1000);
}
void ThrottleController (void)	{
	vDisplayMessage(" Task_ESSP4 	Runnable Execution		ThrottleController\n");
	//usleep(1000);
}
void ThrottleActuator (void)	{
	vDisplayMessage(" Task_ESSP4 	Runnable Execution		ThrottleActuator\n");
	//usleep(1000);
}
void ThrottleSensor (void)	{
	vDisplayMessage(" Task_ESSP5 	Runnable Execution		ThrottleSensor\n");
	//usleep(1000);
}
void EcuBrakePedalSensor (void)	{
	vDisplayMessage(" Task_ESSP5 	Runnable Execution		EcuBrakePedalSensor\n");
	//usleep(1000);
}
void BrakePedalSensorTranslation (void)	{
	vDisplayMessage(" Task_ESSP5 	Runnable Execution		BrakePedalSensorTranslation\n");
	//usleep(1000);
}
void BrakePedalSensorVoter (void)	{
	vDisplayMessage(" Task_ESSP5 	Runnable Execution		BrakePedalSensorVoter\n");
	//usleep(1000);
}
void CheckPlausability (void)	{
	vDisplayMessage(" Task_ESSP5 	Runnable Execution		CheckPlausability\n");
	//usleep(1000);
}
void BrakeSafetyMonitor (void)	{
	vDisplayMessage(" Task_ESSP5 	Runnable Execution		BrakeSafetyMonitor\n");
	//usleep(1000);
}
void BrakeForceCalculation (void)	{
	vDisplayMessage(" Task_ESSP5 	Runnable Execution		BrakeForceCalculation\n");
	//usleep(1000);
}
void BrakeForceArbiter (void)	{
	vDisplayMessage(" Task_ESSP5 	Runnable Execution		BrakeForceArbiter\n");
	//usleep(1000);
}
void StopLightActuator (void)	{
	vDisplayMessage(" Task_ESSP5 	Runnable Execution		StopLightActuator\n");
	//usleep(1000);
}
void EcuStopLightActuator (void)	{
	vDisplayMessage(" Task_ESSP5 	Runnable Execution		EcuStopLightActuator\n");
	//usleep(1000);
}
void VehicleStateMonitor (void)	{
	vDisplayMessage(" Task_ESSP6 	Runnable Execution		VehicleStateMonitor\n");
	//usleep(1000);
}
void EcuDecelerationSensor (void)	{
	vDisplayMessage(" Task_ESSP6 	Runnable Execution		EcuDecelerationSensor\n");
	//usleep(1000);
}
void DecelerationSensorTranslation (void)	{
	vDisplayMessage(" Task_ESSP6 	Runnable Execution		DecelerationSensorTranslation\n");
	//usleep(1000);
}
void DecelerationSensorVoter (void)	{
	vDisplayMessage(" Task_ESSP6 	Runnable Execution		DecelerationSensorVoter\n");
	//usleep(1000);
}
void ABSCalculation (void)	{
	vDisplayMessage(" Task_ESSP6 	Runnable Execution		ABSCalculation\n");
	//usleep(1000);
}
void BrakeForceActuation (void)	{
	vDisplayMessage(" Task_ESSP6 	Runnable Execution		BrakeForceActuation\n");
	//usleep(1000);
}
void CaliperPositionCalculation (void)	{
	vDisplayMessage(" Task_ESSP6 	Runnable Execution		CaliperPositionCalculation\n");
	//usleep(1000);
}
void BrakeActuator (void)	{
	vDisplayMessage(" Task_ESSP6 	Runnable Execution		BrakeActuator\n");
	//usleep(1000);
}
void EcuBrakeActuator (void)	{
	vDisplayMessage(" Task_ESSP6 	Runnable Execution		EcuBrakeActuator\n");
	//usleep(1000);
}
void VehicleSpeedSensorDiagnosis (void)	{
	vDisplayMessage(" Task_ESSP7 	Runnable Execution		VehicleSpeedSensorDiagnosis\n");
	//usleep(1000);
}
void BrakeActuatorMonitor (void)	{
	vDisplayMessage(" Task_ESSP7 	Runnable Execution		BrakeActuatorMonitor\n");
	//usleep(1000);
}
void DiagnosisArbiter (void)	{
	vDisplayMessage(" Task_ESSP8 	Runnable Execution		DiagnosisArbiter\n");
	//usleep(1000);
}
void IgnitionTiming (void)	{
	vDisplayMessage(" Task_ESSP8 	Runnable Execution		IgnitionTiming\n");
	//usleep(1000);
}
void IgnitionTimeActuation (void)	{
	vDisplayMessage(" Task_ESSP8 	Runnable Execution		IgnitionTimeActuation\n");
	//usleep(1000);
}
void EcuWheelSpeedSensor (void)	{
	vDisplayMessage(" Task_ESSP9 	Runnable Execution		EcuWheelSpeedSensor\n");
	//usleep(1000);
}
void WheelSpeedSensorTranslation (void)	{
	vDisplayMessage(" Task_ESSP9 	Runnable Execution		WheelSpeedSensorTranslation\n");
	//usleep(1000);
}
void WheelSpeedSensorVoter (void)	{
	vDisplayMessage(" Task_ESSP9 	Runnable Execution		WheelSpeedSensorVoter\n");
	//usleep(1000);
}
