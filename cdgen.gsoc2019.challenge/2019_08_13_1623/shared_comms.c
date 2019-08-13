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
unsigned int *outbuf_shared8bit[5];

void shared_label_8bit_init(){
	outbuf_shared8bit[0] = (unsigned int *) shared_mem_section8bit;
	outbuf_shared8bit[1]=outbuf_shared8bit[1] + 1;
	outbuf_shared8bit[2]=outbuf_shared8bit[2] + 1;
	outbuf_shared8bit[3]=outbuf_shared8bit[3] + 1;
	outbuf_shared8bit[4]=outbuf_shared8bit[4] + 1;
	for (int i=0;i<5;i++){
		*outbuf_shared8bit[i] =0;
	}
}

uint8_t shared_label_8bit_write(int label_indx,int payload){
	uint8_t retval=NULL;
	*outbuf_shared8bit[label_indx] = payload;
	return retval;

}

unsigned int shared_label_8bit_read(int label_indx){
	return *outbuf_shared8bit[label_indx];
}

unsigned int *outbuf_shared16bit[1];

void shared_label_16bit_init(){
	outbuf_shared16bit[0] = (unsigned int *) shared_mem_section16bit;
	for (int i=0;i<1;i++){
		*outbuf_shared16bit[i] =0;
	}
}

uint16_t shared_label_16bit_write(int label_indx,int payload){
	uint16_t retval=NULL;
	*outbuf_shared16bit[label_indx] = payload;
	return retval;

}

unsigned int shared_label_16bit_read(int label_indx){
	return *outbuf_shared16bit[label_indx];
}





