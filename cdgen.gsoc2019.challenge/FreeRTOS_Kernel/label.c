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




	//10ms

	uint8_t	VotedBrakePedalPosition_Task_10MS	=	255;
			uint8_t	BrakePedalPosition_Task_10MS	=	255;
			uint8_t	BrakeForceFeedback_Task_10MS	=	255;
			uint8_t	BrakeForce_Task_10MS	=	255;
			uint8_t	ArbitratedDiagnosisRequest_Task_10MS	=	255;
			uint8_t	MonitoredVehicleState_Task_10MS	=	255;
			uint8_t	BrakeSafetyLevel_Task_10MS	=	255;
			uint8_t	CalculatedBrakeForce_Task_10MS	=	255;
			uint8_t	BrakeMonitorLevel_Task_10MS	=	255;
			uint8_t	BrakeSafetyState_Task_10MS	=	255;
			uint8_t	ArbitratedBrakeForce_Task_10MS	=	255;
			uint8_t	ABSMode_Task_10MS	=	255;
			uint8_t	VotedVehicleSpeed_Task_10MS	=	255;
			uint8_t	VotedDecelerationRate_Task_10MS	=	255;
			uint8_t	ABSActivation_Task_10MS	=	255;
			uint8_t	VotedWheelSpeed_Task_10MS	=	255;
			uint8_t	BrakeForceCurrent_Task_10MS	=	255;
			uint8_t	CaliperPosition_Task_10MS	=	255;
			uint16_t	BrakeForceVoltage_Task_10MS	=	65535;
			uint8_t	BrakeApplication_Task_10MS	=	255;
			uint16_t	BrakePedalPositionVoltage2_Task_10MS	=	65535;
			uint16_t	BrakePedalPositionVoltage1_Task_10MS	=	65535;
			uint8_t	BrakePedalPosition2_Task_10MS	=	255;
			uint8_t	BrakePedalPosition1_Task_10MS	=	255;
			uint16_t	DecelerationVoltage2_Task_10MS	=	65535;
			uint16_t	DecelerationVoltage1_Task_10MS	=	65535;
			uint8_t	DecelerationRate2_Task_10MS	=	255;
			uint8_t	DecelerationRate1_Task_10MS	=	255;
			uint16_t	VehicleSpeedVoltage2_Task_10MS	=	65535;
			uint16_t	VehicleSpeedVoltage1_Task_10MS	=	65535;
			uint8_t	VehicleSpeed1_Task_10MS	=	255;
			uint8_t	VehicleSpeed2_Task_10MS	=	255;
			uint16_t	WheelSpeedVoltage2_Task_10MS	=	65535;
			uint8_t	WheelSpeed1_Task_10MS	=	255;
			uint8_t	WheelSpeed2_Task_10MS	=	255;
			uint16_t	WheelSpeedVoltage1_Task_10MS	=	65535;
			uint8_t	CylinderNumber_Task_10MS	=	255;
			uint8_t	TriggeredCylinderNumber_Task_10MS	=	255;
			uint8_t	VotedAPedPosition_Task_10MS	=	255;
			uint16_t	DesiredThrottlePosition_Task_10MS	=	65535;
			uint16_t	ThrottlePosition_Task_10MS	=	65535;
			uint8_t	APedPosition2_Task_10MS	=	255;
			uint8_t	APedPosition1_Task_10MS	=	255;
			uint8_t	MassAirFlow_Task_10MS	=	255;
			uint16_t	MAFRate_Task_10MS	=	65535;
			uint16_t	BaseFuelMassPerStroke_Task_10MS	=	65535;
			uint8_t	DesiredThrottlePositionVoltage_Task_10MS	=	255;
			uint8_t	TotalFuelMassPerStroke_Task_10MS	=	255;
			uint8_t	TransientFuelMassPerStroke_Task_10MS	=	255;
			uint16_t	InjectionTime2_Task_10MS	=	65535;
			uint16_t	InjectionTime7_Task_10MS	=	65535;
			uint16_t	InjectionTime6_Task_10MS	=	65535;
			uint16_t	InjectionTime8_Task_10MS	=	65535;
			uint16_t	InjectionTime4_Task_10MS	=	65535;
			uint16_t	InjectionTime5_Task_10MS	=	65535;
			uint16_t	InjectionTime3_Task_10MS	=	65535;
			uint16_t	InjectionTime1_Task_10MS	=	65535;
			uint8_t	IgnitionTime_Task_10MS	=	255;
			uint16_t	IgnitionTime5_Task_10MS	=	65535;
			uint16_t	IgnitionTime2_Task_10MS	=	65535;
			uint16_t	IgnitionTime7_Task_10MS	=	65535;
			uint16_t	IgnitionTime3_Task_10MS	=	65535;
			uint16_t	IgnitionTime4_Task_10MS	=	65535;
			uint16_t	IgnitionTime1_Task_10MS	=	65535;
			uint16_t	IgnitionTime8_Task_10MS	=	65535;
			uint16_t	IgnitionTime6_Task_10MS	=	65535;



			//5ms
			uint16_t	BrakeForceVoltage_Task_5MS	=	65535;
					uint8_t	BrakeApplication_Task_5MS	=	255;
					uint16_t	BrakePedalPositionVoltage2_Task_5MS	=	65535;
					uint16_t	BrakePedalPositionVoltage1_Task_5MS	=	65535;
					uint16_t	DecelerationVoltage2_Task_5MS	=	65535;
					uint16_t	DecelerationVoltage1_Task_5MS	=	65535;
					uint16_t	VehicleSpeedVoltage2_Task_5MS	=	65535;
					uint16_t	VehicleSpeedVoltage1_Task_5MS	=	65535;
					uint16_t	WheelSpeedVoltage2_Task_5MS	=	65535;
					uint16_t	WheelSpeedVoltage1_Task_5MS	=	65535;
					uint16_t	APedSensor1Voltage_Task_5MS	=	65535;
					uint8_t	APedPosition2_Task_5MS	=	255;
					uint16_t	APedSensor2Voltage_Task_5MS	=	65535;
					uint8_t	APedPosition1_Task_5MS	=	255;
					uint8_t	ThrottleSensor1Voltage_Task_5MS	=	255;
					uint8_t	ThrottleSensor2Voltage_Task_5MS	=	255;
					uint16_t	ThrottlePosition_Task_5MS	=	65535;
					uint8_t	MassAirFlow_Task_5MS	=	255;
					uint16_t	MAFSensorVoltage_Task_5MS	=	65535;

			//20ms

			uint8_t	ArbitratedBrakeForce_Task_20MS	=	255;
					uint8_t	CalculatedBrakeForce_Task_20MS	=	255;
					uint8_t	BrakeSafetyState_Task_20MS	=	255;


