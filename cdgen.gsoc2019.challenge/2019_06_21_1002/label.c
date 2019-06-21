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
*Title 		:   Label Declaration
*Description	:	Declaration and Initialisation of Label
******************************************************************
******************************************************************/


/* Standard includes. */
#include <stdio.h>
#include <stdint.h>
#include <string.h>

/* Scheduler includes. */
	uint8_t	ABSActivation	=	255;
	uint8_t	ABSMode	=	255;
	uint8_t	APedPosition1	=	255;
	uint8_t	APedPosition2	=	255;
	uint16_t	APedSensor1Voltage	=	65535;
	uint16_t	APedSensor2Voltage	=	65535;
	uint8_t	ArbitratedBrakeForce	=	255;
	uint8_t	ArbitratedDiagnosisRequest	=	255;
	uint16_t	BaseFuelMassPerStroke	=	65535;
	uint8_t	BrakeApplication	=	255;
	uint8_t	BrakeForce	=	255;
	uint8_t	BrakeForceCurrent	=	255;
	uint8_t	BrakeForceFeedback	=	255;
	uint16_t	BrakeForceVoltage	=	65535;
	uint8_t	BrakeMonitorLevel	=	255;
	uint8_t	BrakePedalPosition	=	255;
	uint8_t	BrakePedalPosition1	=	255;
	uint8_t	BrakePedalPosition2	=	255;
	uint16_t	BrakePedalPositionVoltage1	=	65535;
	uint16_t	BrakePedalPositionVoltage2	=	65535;
	uint8_t	BrakeSafetyLevel	=	255;
	uint8_t	BrakeSafetyState	=	255;
	uint8_t	CalculatedBrakeForce	=	255;
	uint8_t	CaliperPosition	=	255;
	uint8_t	CylinderNumber	=	255;
	uint8_t	DecelerationRate1	=	255;
	uint8_t	DecelerationRate2	=	255;
	uint16_t	DecelerationVoltage1	=	65535;
	uint16_t	DecelerationVoltage2	=	65535;
	uint16_t	DesiredThrottlePosition	=	65535;
	uint8_t	DesiredThrottlePositionVoltage	=	255;
	uint8_t	IgnitionTime	=	255;
	uint16_t	IgnitionTime1	=	65535;
	uint16_t	IgnitionTime2	=	65535;
	uint16_t	IgnitionTime3	=	65535;
	uint16_t	IgnitionTime4	=	65535;
	uint16_t	IgnitionTime5	=	65535;
	uint16_t	IgnitionTime6	=	65535;
	uint16_t	IgnitionTime7	=	65535;
	uint16_t	IgnitionTime8	=	65535;
	uint16_t	InjectionTime1	=	65535;
	uint16_t	InjectionTime2	=	65535;
	uint16_t	InjectionTime3	=	65535;
	uint16_t	InjectionTime4	=	65535;
	uint16_t	InjectionTime5	=	65535;
	uint16_t	InjectionTime6	=	65535;
	uint16_t	InjectionTime7	=	65535;
	uint16_t	InjectionTime8	=	65535;
	uint16_t	MAFRate	=	65535;
	uint16_t	MAFSensorVoltage	=	65535;
	uint8_t	MassAirFlow	=	255;
	uint8_t	MonitoredVehicleState	=	255;
	uint16_t	ThrottlePosition	=	65535;
	uint8_t	ThrottleSensor1Voltage	=	255;
	uint8_t	ThrottleSensor2Voltage	=	255;
	uint8_t	TotalFuelMassPerStroke	=	255;
	uint8_t	TransientFuelMassPerStroke	=	255;
	uint8_t	TriggeredCylinderNumber	=	255;
	uint8_t	VehicleSpeed1	=	255;
	uint8_t	VehicleSpeed2	=	255;
	uint16_t	VehicleSpeedVoltage1	=	65535;
	uint16_t	VehicleSpeedVoltage2	=	65535;
	uint8_t	VotedAPedPosition	=	255;
	uint8_t	VotedBrakePedalPosition	=	255;
	uint8_t	VotedDecelerationRate	=	255;
	uint8_t	VotedVehicleSpeed	=	255;
	uint8_t	VotedWheelSpeed	=	255;
	uint8_t	WheelSpeed1	=	255;
	uint8_t	WheelSpeed2	=	255;
	uint16_t	WheelSpeedVoltage1	=	65535;
	uint16_t	WheelSpeedVoltage2	=	65535;





 //local variable for Task_ESSP0
		uint16_t	VehicleSpeedVoltage2_Task_ESSP0	=	65535;
		uint16_t	VehicleSpeedVoltage1_Task_ESSP0	=	65535;
		uint8_t	VehicleSpeed2_Task_ESSP0	=	255;
		uint8_t	VehicleSpeed1_Task_ESSP0	=	255;
		uint8_t	VotedVehicleSpeed_Task_ESSP0	=	255;



 //local variable for Task_ESSP1
		uint8_t	TriggeredCylinderNumber_Task_ESSP1	=	255;
		uint8_t	CylinderNumber_Task_ESSP1	=	255;
		uint16_t	DecelerationVoltage1_Task_ESSP1	=	65535;
		uint16_t	DecelerationVoltage2_Task_ESSP1	=	65535;



 //local variable for Task_ESSP2
		uint16_t	WheelSpeedVoltage1_Task_ESSP2	=	65535;
		uint16_t	WheelSpeedVoltage2_Task_ESSP2	=	65535;
		uint16_t	BrakePedalPositionVoltage2_Task_ESSP2	=	65535;
		uint16_t	BrakePedalPositionVoltage1_Task_ESSP2	=	65535;



 //local variable for Task_ESSP3
		uint8_t	MassAirFlow_Task_ESSP3	=	255;
		uint16_t	MAFSensorVoltage_Task_ESSP3	=	65535;
		uint16_t	MAFRate_Task_ESSP3	=	65535;
		uint16_t	BaseFuelMassPerStroke_Task_ESSP3	=	65535;
		uint8_t	TransientFuelMassPerStroke_Task_ESSP3	=	255;
		uint8_t	TotalFuelMassPerStroke_Task_ESSP3	=	255;
		uint16_t	InjectionTime4_Task_ESSP3	=	65535;
		uint16_t	InjectionTime3_Task_ESSP3	=	65535;
		uint16_t	InjectionTime6_Task_ESSP3	=	65535;
		uint16_t	InjectionTime8_Task_ESSP3	=	65535;
		uint8_t	TriggeredCylinderNumber_Task_ESSP3	=	255;
		uint16_t	InjectionTime5_Task_ESSP3	=	65535;
		uint16_t	InjectionTime2_Task_ESSP3	=	65535;
		uint16_t	InjectionTime1_Task_ESSP3	=	65535;
		uint16_t	InjectionTime7_Task_ESSP3	=	65535;



 //local variable for Task_ESSP4
		uint16_t	APedSensor2Voltage_Task_ESSP4	=	65535;
		uint8_t	APedPosition1_Task_ESSP4	=	255;
		uint8_t	APedPosition2_Task_ESSP4	=	255;
		uint16_t	APedSensor1Voltage_Task_ESSP4	=	65535;
		uint8_t	VotedAPedPosition_Task_ESSP4	=	255;
		uint16_t	DesiredThrottlePosition_Task_ESSP4	=	65535;
		uint16_t	ThrottlePosition_Task_ESSP4	=	65535;
		uint8_t	DesiredThrottlePositionVoltage_Task_ESSP4	=	255;



 //local variable for Task_ESSP5
		uint8_t	ThrottleSensor2Voltage_Task_ESSP5	=	255;
		uint8_t	ThrottleSensor1Voltage_Task_ESSP5	=	255;
		uint16_t	ThrottlePosition_Task_ESSP5	=	65535;
		uint16_t	BrakePedalPositionVoltage2_Task_ESSP5	=	65535;
		uint16_t	BrakePedalPositionVoltage1_Task_ESSP5	=	65535;
		uint8_t	BrakePedalPosition2_Task_ESSP5	=	255;
		uint8_t	BrakePedalPosition1_Task_ESSP5	=	255;
		uint8_t	VotedBrakePedalPosition_Task_ESSP5	=	255;
		uint8_t	BrakePedalPosition_Task_ESSP5	=	255;
		uint8_t	BrakeForceFeedback_Task_ESSP5	=	255;
		uint8_t	ArbitratedDiagnosisRequest_Task_ESSP5	=	255;
		uint8_t	MonitoredVehicleState_Task_ESSP5	=	255;
		uint8_t	BrakeMonitorLevel_Task_ESSP5	=	255;
		uint8_t	BrakeSafetyLevel_Task_ESSP5	=	255;
		uint8_t	BrakeSafetyState_Task_ESSP5	=	255;
		uint8_t	CalculatedBrakeForce_Task_ESSP5	=	255;
		uint8_t	ArbitratedBrakeForce_Task_ESSP5	=	255;
		uint8_t	BrakeApplication_Task_ESSP5	=	255;



 //local variable for Task_ESSP6
		uint8_t	MonitoredVehicleState_Task_ESSP6	=	255;
		uint16_t	DecelerationVoltage1_Task_ESSP6	=	65535;
		uint16_t	DecelerationVoltage2_Task_ESSP6	=	65535;
		uint8_t	DecelerationRate1_Task_ESSP6	=	255;
		uint8_t	DecelerationRate2_Task_ESSP6	=	255;
		uint8_t	VotedDecelerationRate_Task_ESSP6	=	255;
		uint8_t	VotedWheelSpeed_Task_ESSP6	=	255;
		uint8_t	ArbitratedBrakeForce_Task_ESSP6	=	255;
		uint8_t	ABSMode_Task_ESSP6	=	255;
		uint8_t	ABSActivation_Task_ESSP6	=	255;
		uint8_t	VotedVehicleSpeed_Task_ESSP6	=	255;
		uint8_t	BrakeForce_Task_ESSP6	=	255;
		uint8_t	BrakeForceCurrent_Task_ESSP6	=	255;
		uint8_t	CaliperPosition_Task_ESSP6	=	255;
		uint16_t	BrakeForceVoltage_Task_ESSP6	=	65535;



 //local variable for Task_ESSP7
		uint16_t	VehicleSpeedVoltage2_Task_ESSP7	=	65535;
		uint16_t	VehicleSpeedVoltage1_Task_ESSP7	=	65535;
		uint8_t	BrakeForceFeedback_Task_ESSP7	=	255;
		uint8_t	BrakeForce_Task_ESSP7	=	255;



 //local variable for Task_ESSP8
		uint8_t	ArbitratedDiagnosisRequest_Task_ESSP8	=	255;
		uint8_t	IgnitionTime_Task_ESSP8	=	255;
		uint16_t	MAFRate_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime3_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime1_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime4_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime8_Task_ESSP8	=	65535;
		uint8_t	TriggeredCylinderNumber_Task_ESSP8	=	255;
		uint16_t	IgnitionTime6_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime5_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime7_Task_ESSP8	=	65535;
		uint16_t	IgnitionTime2_Task_ESSP8	=	65535;



 //local variable for Task_ESSP9
		uint16_t	WheelSpeedVoltage1_Task_ESSP9	=	65535;
		uint16_t	WheelSpeedVoltage2_Task_ESSP9	=	65535;
		uint8_t	WheelSpeed1_Task_ESSP9	=	255;
		uint8_t	WheelSpeed2_Task_ESSP9	=	255;
		uint8_t	VotedWheelSpeed_Task_ESSP9	=	255;


