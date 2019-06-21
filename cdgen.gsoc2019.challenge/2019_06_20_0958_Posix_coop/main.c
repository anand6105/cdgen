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
*Title 		:   C File for Tasks Call
*Description	:	Main file in which scheduling is done 
******************************************************************
******************************************************************
******************************************************************/


/* Standard includes. */
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <string.h>

/* Scheduler includes. */
#include "taskDef.h"
#define NUM_THREADS	3

void sleepTimerMs(int ticks)
{
	int var;
	for (var = 0; var < ticks; ++var)
	{
		suspendMe();
		{
			usleep(1000);
		}
		resumeMe();
	}
}
int main(void) 
{
	pthread_t thread[NUM_THREADS];
	pthread_attr_t attr;

	long rtr0=0;
	long rtr1=1;
	long rtr2=2;
	long t;
	int rc;
	void *status;

	for(;;)
	{
		pthread_attr_init(&attr);
		pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_JOINABLE);

		pthread_create (&thread[0], &attr, vTask_10MS, (void *)rtr0);
		pthread_create (&thread[1], &attr, vTask_20MS, (void *)rtr1);
		pthread_create (&thread[2], &attr, vTask_5MS, (void *)rtr2);
		if (rc)
		{
			printf("ERROR; return code from pthread_join() is %d\n", rc);
			exit(-1);
		}

		pthread_attr_destroy(&attr);

		for(t=0; t<NUM_THREADS; t++)
		{
			rc = pthread_join(thread[t], &status);
			if (rc)
			{
				printf("ERROR; return code from pthread_join() is %d\n", rc);
				exit(-1);
			}
			printf("Main: completed join with thread %ld having a status of %ld\n",t,(long)status);
		}
		printf("\n");
	}
	printf("Main: program completed. Exiting.\n");
	pthread_exit(NULL);
}
