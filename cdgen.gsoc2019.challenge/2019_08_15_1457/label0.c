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
#include "label0.h"

	uint16_t	WheelSpeedVoltage2 	=	 65535;
	uint16_t	WheelSpeedVoltage1 	=	 65535;
	uint16_t	BrakePedalPositionVoltage1 	=	 65535;
	uint16_t	BrakePedalPositionVoltage2 	=	 65535;
	uint8_t	ArbitratedDiagnosisRequest 	=	 255;
	uint16_t	MAFRate 	=	 65535;





 //local variable for Task_ESSP2
		uint16_t	WheelSpeedVoltage2_Task_ESSP2	=	65535;
		uint16_t	WheelSpeedVoltage1_Task_ESSP2	=	65535;
		uint16_t	BrakePedalPositionVoltage1_Task_ESSP2	=	65535;
		uint16_t	BrakePedalPositionVoltage2_Task_ESSP2	=	65535;

 //Shared label 

	void cIN_Task_ESSP2()
	{
		WheelSpeedVoltage2_Task_ESSP2	=	WheelSpeedVoltage2;
		WheelSpeedVoltage1_Task_ESSP2	=	WheelSpeedVoltage1;
		BrakePedalPositionVoltage1_Task_ESSP2	=	BrakePedalPositionVoltage1;
		BrakePedalPositionVoltage2_Task_ESSP2	=	BrakePedalPositionVoltage2;
	}

	void cOUT_Task_ESSP2()
	{
	}



 //local variable for Task_ESSP3
		uint16_t	MAFRate_Task_ESSP3	=	65535;

 //Shared label 
		uint8_t	TriggeredCylinderNumber_Task_ESSP3;

	void cIN_Task_ESSP3()
	{
		MAFRate_Task_ESSP3	=	MAFRate;
		TriggeredCylinderNumber_Task_ESSP3	=	shared_label_8bit_read(2);
	}

	void cOUT_Task_ESSP3()
	{
		MAFRate	=	MAFRate_Task_ESSP3;
		shared_label_8bit_write(2,TriggeredCylinderNumber_Task_ESSP3 );
	}



 //local variable for Task_ESSP5
		uint16_t	BrakePedalPositionVoltage1_Task_ESSP5	=	65535;
		uint16_t	BrakePedalPositionVoltage2_Task_ESSP5	=	65535;
		uint8_t	ArbitratedDiagnosisRequest_Task_ESSP5	=	255;

 //Shared label 
		uint8_t	BrakeForceFeedback_Task_ESSP5;
		uint8_t	MonitoredVehicleState_Task_ESSP5;
		uint8_t	ArbitratedBrakeForce_Task_ESSP5;
		uint16_t	ThrottlePosition_Task_ESSP5;

	void cIN_Task_ESSP5()
	{
		BrakePedalPositionVoltage1_Task_ESSP5	=	BrakePedalPositionVoltage1;
		BrakePedalPositionVoltage2_Task_ESSP5	=	BrakePedalPositionVoltage2;
		ArbitratedDiagnosisRequest_Task_ESSP5	=	ArbitratedDiagnosisRequest;
		BrakeForceFeedback_Task_ESSP5	=	shared_label_8bit_read(1);
		MonitoredVehicleState_Task_ESSP5	=	shared_label_8bit_read(3);
		ArbitratedBrakeForce_Task_ESSP5	=	shared_label_8bit_read(4);
	}

	void cOUT_Task_ESSP5()
	{
		BrakePedalPositionVoltage1	=	BrakePedalPositionVoltage1_Task_ESSP5;
		BrakePedalPositionVoltage2	=	BrakePedalPositionVoltage2_Task_ESSP5;
		shared_label_16bit_write(5,ThrottlePosition_Task_ESSP5 );
		shared_label_8bit_write(1,BrakeForceFeedback_Task_ESSP5 );
		shared_label_8bit_write(3,MonitoredVehicleState_Task_ESSP5 );
		shared_label_8bit_write(4,ArbitratedBrakeForce_Task_ESSP5 );
	}



 //local variable for Task_ESSP8
		uint8_t	ArbitratedDiagnosisRequest_Task_ESSP8	=	255;
		uint16_t	MAFRate_Task_ESSP8	=	65535;

 //Shared label 
		uint8_t	TriggeredCylinderNumber_Task_ESSP8;

	void cIN_Task_ESSP8()
	{
		ArbitratedDiagnosisRequest_Task_ESSP8	=	ArbitratedDiagnosisRequest;
		MAFRate_Task_ESSP8	=	MAFRate;
		TriggeredCylinderNumber_Task_ESSP8	=	shared_label_8bit_read(2);
	}

	void cOUT_Task_ESSP8()
	{
		shared_label_8bit_write(2,TriggeredCylinderNumber_Task_ESSP8 );
	}



 //local variable for Task_ESSP9
		uint16_t	WheelSpeedVoltage2_Task_ESSP9	=	65535;
		uint16_t	WheelSpeedVoltage1_Task_ESSP9	=	65535;

 //Shared label 
		uint8_t	VotedWheelSpeed_Task_ESSP9;

	void cIN_Task_ESSP9()
	{
		WheelSpeedVoltage2_Task_ESSP9	=	WheelSpeedVoltage2;
		WheelSpeedVoltage1_Task_ESSP9	=	WheelSpeedVoltage1;
	}

	void cOUT_Task_ESSP9()
	{
		WheelSpeedVoltage2	=	WheelSpeedVoltage2_Task_ESSP9;
		WheelSpeedVoltage1	=	WheelSpeedVoltage1_Task_ESSP9;
		shared_label_8bit_write(0,VotedWheelSpeed_Task_ESSP9 );
	}


