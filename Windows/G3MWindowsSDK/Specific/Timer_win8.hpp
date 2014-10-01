//
//  Timer_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 02/06/14.
//  NOTE: almost exact copy of Timer_iOS.hpp
//

#ifndef __G3MWindowsSDK_Timer_win8__
#define __G3MWindowsSDK_Timer_win8__

#include "ITimer.hpp"

#include <Windows.h>

class Timer_win8 : public ITimer {
private:
	double _startedInSeconds;



	double nowInSeconds() const {
		ULONGLONG ticks = GetTickCount64();
		double seconds = ticks / 1000.0;
		return seconds;
		//return 1;
	}

public:

	Timer_win8() {
		start();
	}

	TimeInterval now() const {
		return TimeInterval::fromSeconds(nowInSeconds());
	}

	long long nowInMilliseconds() const{
		return GetTickCount64();
	}

	void start() {
		_startedInSeconds = nowInSeconds();
	}

	TimeInterval elapsedTime() const {
		return TimeInterval::fromSeconds(nowInSeconds() - _startedInSeconds);
	}

	long long elapsedTimeInMilliseconds() const {
		return (long long)((nowInSeconds() - _startedInSeconds) * 1000.0);
	}

};

#endif