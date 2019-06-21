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
#include "FreeRTOS.h"

 void vABSCalculation(void);
 void vAPedSensor(void);
 void vAPedVoter(void);
 void vBaseFuelMass(void);
 void vBrakeActuator(void);
 void vBrakeActuatorMonitor(void);
 void vBrakeForceActuation(void);
 void vBrakeForceArbiter(void);
 void vBrakeForceCalculation(void);
 void vBrakePedalSensorDiagnosis(void);
 void vBrakePedalSensorTranslation(void);
 void vBrakePedalSensorVoter(void);
 void vBrakeSafetyMonitor(void);
 void vCaliperPositionCalculation(void);
 void vCheckPlausability(void);
 void vCylNumObserver(void);
 void vDecelerationSensorDiagnosis(void);
 void vDecelerationSensorTranslation(void);
 void vDecelerationSensorVoter(void);
 void vDiagnosisArbiter(void);
 void vEcuBrakeActuator(void);
 void vEcuBrakePedalSensor(void);
 void vEcuDecelerationSensor(void);
 void vEcuStopLightActuator(void);
 void vEcuVehicleSpeedSensor(void);
 void vEcuWheelSpeedSensor(void);
 void vIgnitionTimeActuation(void);
 void vIgnitionTiming(void);
 void vInjectionTimeActuation(void);
 void vMassAirFlowSensor(void);
 void vStopLightActuator(void);
 void vThrottleActuator(void);
 void vThrottleController(void);
 void vThrottleSensor(void);
 void vTotalFuelMass(void);
 void vTransientFuelMass(void);
 void vVehicleSpeedSensorDiagnosis(void);
 void vVehicleSpeedSensorTranslation(void);
 void vVehicleSpeedSensorVoter(void);
 void vVehicleStateMonitor(void);
 void vWheelSpeedSensorDiagnosis(void);
 void vWheelSpeedSensorTranslation(void);
 void vWheelSpeedSensorVoter(void);
