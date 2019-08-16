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
*Description	:	Header file for Declaration and Initialisation of Shared Label
******************************************************************
******************************************************************/


#ifndef DEMO_PARALLELLA_SHARED_COMMS_H_
#define DEMO_PARALLELLA_SHARED_COMMS_H_

/* Standard includes. */
#include <stdlib.h>
#include <stdint.h>


#define shared_mem_section16bit	0x01000000

void shared_label_16bit_init();
void shared_label_16bit_write(int label_indx,int payload);
uint16_t shared_label_16bit_read(int label_indx);

#define shared_mem_section8bit	0x02000000

void shared_label_8bit_init();
void shared_label_8bit_write(int label_indx,int payload);
uint8_t shared_label_8bit_read(int label_indx);


#endif