/* System headers. */
#include <pthread.h>
#include "taskDef.h"
//#include "taskDef2.h"
#define NUM_THREADS 3


#define mainDEBUG_LOG_BUFFER_SIZE	( ( unsigned short ) 20480 )
#define DELAY_MULT 100
/* The number of flash co-routines to create. */
#define mainNUM_FLASH_CO_ROUTINES	( 8 )

/* The create delete Tasks routines are very expensive so they are
 * disabled unless required for testing. */
#define mainUSE_SUICIDAL_TASKS_DEMO		0

/* UDP Packet size to send/receive. */
#define mainUDP_SEND_ADDRESS		"127.0.0.1"
#define mainUDP_PORT				( 9999 )

/* Remove some of the CPU intensive tasks. */
#define mainCPU_INTENSIVE_TASKS		0


/* Just used to count the number of times the example task callback function is
called, and the number of times a queue send passes. */
static unsigned long uxQueueSendPassedCount = 0;
/*-----------------------------------------------------------*/
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

	//int rc0;
	long rtr0=0;
	//int rc1;
	long rtr1=1;
	//int rc2;
	long rtr2=2;
	long t;
	int rc;
	void *status;

	for(;;)
	{
		pthread_attr_init(&attr);
		pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_JOINABLE);

		pthread_create(&thread[0], &attr, vTask_10MS, (void *)rtr0);
		pthread_create(&thread[1], &attr, vTask_20MS, (void *)rtr1);
		pthread_create(&thread[2], &attr, vTask_5MS, (void *)rtr2);
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



/*-----------------------------------------------------------*/

void vMainQueueSendPassed( void )
{
	/* This is just an example implementation of the "queue send" trace hook. */
	uxQueueSendPassedCount++;
}
/*-----------------------------------------------------------*/


void vApplicationIdleHook( void )
{
	/* The co-routines are executed in the idle task using the idle task hook. */
	vCoRoutineSchedule();	/* Comment this out if not using Co-routines. */

#ifdef __GCC_POSIX__
	struct timespec xTimeToSleep, xTimeSlept;
		/* Makes the process more agreeable when using the Posix simulator. */
		xTimeToSleep.tv_sec = 1;
		xTimeToSleep.tv_nsec = 0;
		nanosleep( &xTimeToSleep, &xTimeSlept );
#endif
}
/*-----------------------------------------------------------*/
