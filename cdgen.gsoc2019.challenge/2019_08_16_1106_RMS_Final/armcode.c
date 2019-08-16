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
*Title 		:   ArmCode Header
*Description	:	Header file for Deploy/Offloading of the task to EPI
******************************************************************
******************************************************************/


/* Standard includes. */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <e-hal.h>
#include <time.h>

/* Scheduler includes. */
#include "debugFlags.h"
#define READ_PRECISION_US 1000


int nsleep(long miliseconds){
	struct timespec req, rem;
	if(miliseconds > 999){
		req.tv_sec = (int)(miliseconds / 1000);
		req.tv_nsec = (miliseconds - ((long)req.tv_sec * 1000)) * 1000000;
	} else {
		req.tv_sec = 0;
		req.tv_nsec = miliseconds * 1000000;
	}
	return nanosleep(&req , &rem);
}
int main(){
	unsigned int shared_label_to_read;
	unsigned   row_loop,col_loop;
	e_platform_t epiphany;
	e_epiphany_t dev;
	int loop;
	int addr;
	e_mem_t emem;
	e_init(NULL);
	e_reset_system();
	e_get_platform_info(&epiphany);
	e_open(&dev,0,0,2,2);
	e_reset_group(&dev);
	e_return_stat_t	result0;
	e_return_stat_t	result1;
	e_return_stat_t	result2;
	e_return_stat_t	result3;
	e_return_stat_t	result4;
	unsigned int message0[9];
	unsigned int message1[9];
	unsigned int message2[9];
	unsigned int message3[9];
	unsigned int message4[9];
	result0=  e_load("main0.elf",&dev,0,0,E_FALSE);
	result1=  e_load("main1.elf",&dev,0,1,E_FALSE);
	result2=  e_load("main2.elf",&dev,0,2,E_FALSE);
	result3=  e_load("main3.elf",&dev,0,3,E_FALSE);
	result4=  e_load("main4.elf",&dev,1,0,E_FALSE);
	if (result0!=E_OK){
		fprintf(stderr,"Error Loading the Epiphany Application 0 %i\n", result0);
	}
	else if (result1!=E_OK){
		fprintf(stderr,"Error Loading the Epiphany Application 1 %i\n", result1);
	}
	else if (result2!=E_OK){
		fprintf(stderr,"Error Loading the Epiphany Application 2 %i\n", result2);
	}
	else if (result3!=E_OK){
		fprintf(stderr,"Error Loading the Epiphany Application 3 %i\n", result3);
	}
	else if (result4!=E_OK){
		fprintf(stderr,"Error Loading the Epiphany Application 4 %i\n", result4);
	}
	e_start_group(&dev);
	fprintf(stderr,"RMS Multicore on FreeRTOS started \n");
	addr = cnt_address;
	int pollLoopCounter = 0;
	int taskMessage;
	int prevtaskMessage;
	int prevpollLoopCounter = 0;
	for (pollLoopCounter=0;pollLoopCounter<=40;pollLoopCounter++){
		e_read(&dev,0,0,addr, &message0, sizeof(message0));
		fprintf(stderr, "tick1 %3d||",message0[8]+1);
		fprintf(stderr,"task holding core0 %2u||", message0[6]);
		e_read(&dev,0,1,addr, &message1, sizeof(message1));
		fprintf(stderr, "tick1 %3d||",message1[8]+1);
		fprintf(stderr,"task holding core1 %2u||", message1[6]);
		e_read(&dev,0,2,addr, &message2, sizeof(message2));
		fprintf(stderr, "tick1 %3d||",message2[8]+1);
		fprintf(stderr,"task holding core2 %2u||", message2[6]);
		e_read(&dev,0,3,addr, &message3, sizeof(message3));
		fprintf(stderr, "tick1 %3d||",message3[8]+1);
		fprintf(stderr,"task holding core3 %2u||", message3[6]);
		e_read(&dev,1,0,addr, &message4, sizeof(message4));
		fprintf(stderr, "tick1 %3d||",message4[8]+1);
		fprintf(stderr,"task holding core4 %2u||", message4[6]);
		fprintf(stderr,"\n");
		usleep(READ_PRECISION_US);
	}
	fprintf(stderr,"----------------------------------------------\n");
	e_close(&dev);
	e_finalize();
	fprintf(stderr,"RMS Multicore on FreeRTOS Complete \n ");
	return 0;
}
