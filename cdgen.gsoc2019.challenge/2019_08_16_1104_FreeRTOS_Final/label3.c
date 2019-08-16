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
#include "shared_comms.h"
#include "label3.h"

	uint8_t	BrakeForceFeedback 	=	 255;





 //local variable for Task_ESSP7
		uint8_t	BrakeForceFeedback_Task_ESSP7	=	255;

 //Shared label 
		uint8_t	BrakeForce_Task_ESSP7;
		uint16_t	VehicleSpeedVoltage2_Task_ESSP7;
		uint16_t	VehicleSpeedVoltage1_Task_ESSP7;

	void cIN_Task_ESSP7()
	{
		BrakeForce_Task_ESSP7	=	shared_label_8bit_read(4);
		VehicleSpeedVoltage2_Task_ESSP7	=	shared_label_16bit_read(0);
		VehicleSpeedVoltage1_Task_ESSP7	=	shared_label_16bit_read(9);
		BrakeForceFeedback_Task_ESSP7	=	BrakeForceFeedback;
	}

	void cOUT_Task_ESSP7()
	{
		shared_label_8bit_write(4,BrakeForce_Task_ESSP7 );
		shared_label_16bit_write(0,VehicleSpeedVoltage2_Task_ESSP7 );
		shared_label_16bit_write(9,VehicleSpeedVoltage1_Task_ESSP7 );
	}



 //local variable for Task_ESSP5
		uint8_t	BrakeForceFeedback_Task_ESSP5	=	255;

 //Shared label 
		uint8_t	MonitoredVehicleState_Task_ESSP5;
		uint8_t	ArbitratedDiagnosisRequest_Task_ESSP5;
		uint8_t	ArbitratedBrakeForce_Task_ESSP5;
		uint16_t	BrakePedalPositionVoltage1_Task_ESSP5;
		uint16_t	ThrottlePosition_Task_ESSP5;
		uint16_t	BrakePedalPositionVoltage2_Task_ESSP5;

	void cIN_Task_ESSP5()
	{
		MonitoredVehicleState_Task_ESSP5	=	shared_label_8bit_read(1);
		ArbitratedDiagnosisRequest_Task_ESSP5	=	shared_label_8bit_read(2);
		ArbitratedBrakeForce_Task_ESSP5	=	shared_label_8bit_read(3);
		BrakePedalPositionVoltage1_Task_ESSP5	=	shared_label_16bit_read(2);
		BrakePedalPositionVoltage2_Task_ESSP5	=	shared_label_16bit_read(8);
		BrakeForceFeedback_Task_ESSP5	=	BrakeForceFeedback;
	}

	void cOUT_Task_ESSP5()
	{
		shared_label_8bit_write(1,MonitoredVehicleState_Task_ESSP5 );
		shared_label_8bit_write(2,ArbitratedDiagnosisRequest_Task_ESSP5 );
		shared_label_8bit_write(3,ArbitratedBrakeForce_Task_ESSP5 );
		shared_label_16bit_write(2,BrakePedalPositionVoltage1_Task_ESSP5 );
		shared_label_16bit_write(8,BrakePedalPositionVoltage2_Task_ESSP5 );
	}


