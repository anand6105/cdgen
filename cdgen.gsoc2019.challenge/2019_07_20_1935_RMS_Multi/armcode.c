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
#include "runnable.h"
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
	e_return_stat_t result;
	unsigned int message[9];
	unsigned int message2[9];
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
	result0=  e_load("main0.elf",&dev,0,0,E_FALSE);
	result1=  e_load("main1.elf",&dev,0,1,E_FALSE);
	if (result0!=E_OK||result1!=E_OK){
		fprintf(stderr,"Error Loading the Epiphany Application 1 %i\n", result);	}
	e_start_group(&dev);
	fprintf(stderr,"FreeRTOS started \n");
	addr = cnt_address;
	int pollLoopCounter = 0;
	int taskMessage;
	int prevtaskMessage;
	int prevpollLoopCounter = 0;
	for (pollLoopCounter=0;pollLoopCounter<=40;pollLoopCounter++){
		fprintf(stderr,"\n");
		usleep(READ_PRECISION_US);
	}
	fprintf(stderr,"----------------------------------------------\n");
	e_close(&dev);
	e_finalize();
	fprintf(stderr,"demo complete \n ");
	return 0;
}
