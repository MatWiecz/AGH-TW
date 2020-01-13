#include <algorithm>
#include <array>
#include <atomic>
#include <chrono>
//We are using only standard library, so snprintf instead of Boost::Format
#include <cstdio>
#include <iostream>
#include <mutex>
#include <random>
#include <string>
#include <thread>
#include <zconf.h>
#include <sys/time.h>

//#define NAIVE_SOLUTION
//#define STARVE_SOLUTION
//#define ASYMMETRIC_SOLUTION
#define WAITER_SOLUTION

inline static unsigned long
timeval_diff_usec(const struct timeval *start, const struct timeval *end)
{ // calculate time difference in microseconds
	return (end->tv_sec - start->tv_sec) * 1e6L +
		   (end->tv_usec - start->tv_usec);
}

const int philsNum = 5;

std::mutex cout_mutex;
std::mutex waiter_mutex;
int waiter = 0;

struct Fork
{
	std::mutex mutex;
};


struct Dinner
{
	std::atomic <bool> ready {false};
	std::array <Fork, philsNum> forks;
	
	~Dinner()
	{
		//std::cout << "Dinner is over";
	}
};

Dinner gDinner;

class Philosopher
{
	static int nextId;
	int id;
	std::mt19937 rng {std::random_device {}()};
	
	const std::string name;
	const Dinner &dinner;
	Fork &left;
	Fork &right;
	std::thread worker;
	int measuresDoneNum;
	unsigned long totalTime;
	
	void live();
	
	void dine();
	
	void ponder();
	
	public:
	Philosopher():
		id(nextId),
		name(std::to_string(nextId)),
		dinner(gDinner),
		left(gDinner.forks[nextId % philsNum]),
		right(gDinner.forks[(nextId + 1) % philsNum]),
		worker(&Philosopher::live, this)
	{
		nextId++;
		measuresDoneNum = 0;
		totalTime = 0;
	}
	
	Philosopher(std::string name_, const Dinner &dinn, Fork &l, Fork &r)
		: name(std::move(name_)),
		  dinner(dinn),
		  left(l),
		  right(r),
		  worker(&Philosopher::live, this)
	{
	}
	
	~Philosopher()
	{
		worker.join();
	}
	
	void getAvgTime();
};

int Philosopher::nextId = 0;

void Philosopher::getAvgTime()
{
	std::cout << name << ", " <<
			  ((double) totalTime) / measuresDoneNum << "," << std::endl;
	//std::cout << name << ", " <<
	//		  totalTime<< "," << std::endl;
	//std::cout << name << ", " <<
	//						  measuresDoneNum<< "," << std::endl;
}

void Philosopher::live()
{
	while (not dinner.ready); //You spin me right round, baby, right round...
	do
	{//Aquire forks first
		//lock uses deadlock prevention mechanism to acquire mutexes safely
		dine(); //Dine adopts lock on forks and releases them
		if (not dinner.ready)
			break;
		ponder();
	}
	while (dinner.ready);
}

void Philosopher::dine()
{
	struct timeval time_start, time_end;
	gettimeofday(&time_start, NULL);
#ifdef NAIVE_SOLUTION
	left.mutex.lock();
	right.mutex.lock();
#endif
#ifdef STARVE_SOLUTION
	std::lock(left.mutex, right.mutex);
#endif
#ifdef ASYMMETRIC_SOLUTION
	if(id%2 == 1)
	{
		right.mutex.lock();
		left.mutex.lock();
	}
	else
	{
		left.mutex.lock();
		right.mutex.lock();
	}
#endif
#ifdef WAITER_SOLUTION
	bool wait = true;
	while (wait)
	{
		waiter_mutex.lock();
		if (waiter < 4)
		{
			wait = false;
			waiter++;
		}
		waiter_mutex.unlock();
	}
	left.mutex.lock();
	right.mutex.lock();
#endif
	gettimeofday(&time_end, NULL);
	{
		unsigned long diff = timeval_diff_usec(&time_start, &time_end);
		measuresDoneNum++;
		totalTime += diff;
		cout_mutex.lock();
		std::cout << name << ", " << diff << ", " << std::endl;
		cout_mutex.unlock();
	}
	thread_local std::uniform_int_distribution <> dist(1, 6);
	
	if (not dinner.ready)
		return;
	usleep(rand() % 10000);
	left.mutex.unlock();
	right.mutex.unlock();
#ifdef WAITER_SOLUTION
	waiter_mutex.lock();
	waiter--;
	waiter_mutex.unlock();
#endif
}

void Philosopher::ponder()
{
	thread_local std::uniform_int_distribution <> wait(1, 6);
	if (not dinner.ready)
		return;
	usleep(rand() % 10000);
}

int main()
{
	srand((unsigned int) time(0));
	std::array <Philosopher, philsNum> philosophers;
	std::cout << "Philosopher, time of waiting for dining," << std::endl;
	std::this_thread::sleep_for(std::chrono::seconds(1));
	gDinner.ready = true;
	std::this_thread::sleep_for(std::chrono::seconds(5));
	gDinner.ready = false;
	//for (int i = 0; i < philsNum; i++)
	//	philosophers[i].getAvgTime();
}