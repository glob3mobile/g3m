

#include "Timer_Emscripten.hpp"

#include "emscripten/val.h"
#include <math.h>

using namespace emscripten;

long long currentTimeMillis() {
    val date = val::global("Date");
    val timeVal = date.call<val>("now");
    
    if(!timeVal.as<bool>()) {
        date = val::global("Date").new_();
        timeVal = date.call<val>("getTime");
    }
    
    return timeVal.as<long long>();
}

TimeInterval Timer_Emscripten::now() const {
    return TimeInterval::fromMilliseconds(currentTimeMillis());
}

TimeInterval Timer_Emscripten::fromDaysFromNow(const double days) const {
    const long daysInMilliseconds = round(days //
                                          * 24 /* hours */
                                          * 60 /* minutes */
                                          * 60 /* seconds */
                                          * 1000 /* milliseconds */ );
    
    const long milliseconds = currentTimeMillis() + daysInMilliseconds;
    
    return TimeInterval::fromMilliseconds(milliseconds);
}

long long Timer_Emscripten::nowInMilliseconds() const {
    return currentTimeMillis();
}

void Timer_Emscripten::start() {
    _startTimeInMilliseconds = currentTimeMillis();
}

TimeInterval Timer_Emscripten::elapsedTime() const {
    return TimeInterval::fromMilliseconds(currentTimeMillis() - _startTimeInMilliseconds);
}

long long Timer_Emscripten::elapsedTimeInMilliseconds() const {
    return currentTimeMillis() - _startTimeInMilliseconds;
}
