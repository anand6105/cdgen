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
#include "FreeRTOS.h"

void ABSCalculation (void);
void APedSensor (void);
void APedVoter (void);
void BaseFuelMass (void);
void BrakeActuator (void);
void BrakeActuatorMonitor (void);
void BrakeForceActuation (void);
void BrakeForceArbiter (void);
void BrakeForceCalculation (void);
void BrakePedalSensorDiagnosis (void);
void BrakePedalSensorTranslation (void);
void BrakePedalSensorVoter (void);
void BrakeSafetyMonitor (void);
void CaliperPositionCalculation (void);
void CheckPlausability (void);
void CylNumObserver (void);
void DecelerationSensorDiagnosis (void);
void DecelerationSensorTranslation (void);
void DecelerationSensorVoter (void);
void DiagnosisArbiter (void);
void EcuBrakeActuator (void);
void EcuBrakePedalSensor (void);
void EcuDecelerationSensor (void);
void EcuStopLightActuator (void);
void EcuVehicleSpeedSensor (void);
void EcuWheelSpeedSensor (void);
void IgnitionTimeActuation (void);
void IgnitionTiming (void);
void InjectionTimeActuation (void);
void MassAirFlowSensor (void);
void StopLightActuator (void);
void ThrottleActuator (void);
void ThrottleController (void);
void ThrottleSensor (void);
void TotalFuelMass (void);
void TransientFuelMass (void);
void VehicleSpeedSensorDiagnosis (void);
void VehicleSpeedSensorTranslation (void);
void VehicleSpeedSensorVoter (void);
void VehicleStateMonitor (void);
void WheelSpeedSensorDiagnosis (void);
void WheelSpeedSensorTranslation (void);
void WheelSpeedSensorVoter (void);
