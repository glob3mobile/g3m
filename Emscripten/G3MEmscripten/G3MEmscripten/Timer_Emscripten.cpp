

#include "Timer_Emscripten.hpp"

#include <emscripten/emscripten.h>
#include <emscripten/html5.h>

//#include <emscripten/val.h>
#include <math.h>

//using namespace emscripten;

Timer_Emscripten::Timer_Emscripten() {
  start();
}

// milliseconds since 1970, as System.currentTimeMillis() in WebGL
long long Timer_Emscripten::nowInMilliseconds() const {
  return (long long) emscripten_date_now();
}

TimeInterval Timer_Emscripten::now() const {
  return TimeInterval::fromMilliseconds(nowInMilliseconds());
}

TimeInterval Timer_Emscripten::fromDaysFromNow(const double days) const {
  const long long daysInMilliseconds = (long long) round(days
                                                         * 24   /* hours        */
                                                         * 60   /* minutes      */
                                                         * 60   /* seconds      */
                                                         * 1000 /* milliseconds */ );
  
  const long long milliseconds = nowInMilliseconds() + daysInMilliseconds;
  
  return TimeInterval::fromMilliseconds(milliseconds);
}

void Timer_Emscripten::start() {
  _startTimeInMilliseconds = nowInMilliseconds();
}

TimeInterval Timer_Emscripten::elapsedTime() const {
  return TimeInterval::fromMilliseconds(nowInMilliseconds() - _startTimeInMilliseconds);
}

long long Timer_Emscripten::elapsedTimeInMilliseconds() const {
  return nowInMilliseconds() - _startTimeInMilliseconds;
}
