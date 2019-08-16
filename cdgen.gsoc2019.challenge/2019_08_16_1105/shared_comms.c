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
*Title 		:   Shared Label Declaration
*Description	:	Declaration and Initialisation of Shared Label
******************************************************************
******************************************************************/


/* Standard includes. */
#include "shared_comms.h"
uint16_t 	*outbuf_shared16bit[10];

void shared_label_16bit_init(){
	outbuf_shared16bit[0] = (uint16_t *) shared_mem_section16bit;
	outbuf_shared16bit[1]=outbuf_shared16bit[1] + 1;
	outbuf_shared16bit[2]=outbuf_shared16bit[2] + 1;
	outbuf_shared16bit[3]=outbuf_shared16bit[3] + 1;
	outbuf_shared16bit[4]=outbuf_shared16bit[4] + 1;
	outbuf_shared16bit[5]=outbuf_shared16bit[5] + 1;
	outbuf_shared16bit[6]=outbuf_shared16bit[6] + 1;
	outbuf_shared16bit[7]=outbuf_shared16bit[7] + 1;
	outbuf_shared16bit[8]=outbuf_shared16bit[8] + 1;
	outbuf_shared16bit[9]=outbuf_shared16bit[9] + 1;
	for (int i=0;i<10;i++){
		*outbuf_shared16bit[i] =0;
	}
}

void shared_label_16bit_write(int label_indx,int payload){
	*outbuf_shared16bit[label_indx] = payload;
}

uint16_t shared_label_16bit_read(int label_indx){
	return *outbuf_shared16bit[label_indx];
}

uint8_t 	*outbuf_shared8bit[7];

void shared_label_8bit_init(){
	outbuf_shared8bit[0] = (uint8_t *) shared_mem_section8bit;
	outbuf_shared8bit[1]=outbuf_shared8bit[1] + 1;
	outbuf_shared8bit[2]=outbuf_shared8bit[2] + 1;
	outbuf_shared8bit[3]=outbuf_shared8bit[3] + 1;
	outbuf_shared8bit[4]=outbuf_shared8bit[4] + 1;
	outbuf_shared8bit[5]=outbuf_shared8bit[5] + 1;
	outbuf_shared8bit[6]=outbuf_shared8bit[6] + 1;
	for (int i=0;i<7;i++){
		*outbuf_shared8bit[i] =0;
	}
}

void shared_label_8bit_write(int label_indx,int payload){
	*outbuf_shared8bit[label_indx] = payload;
}

uint8_t shared_label_8bit_read(int label_indx){
	return *outbuf_shared8bit[label_indx];
}





