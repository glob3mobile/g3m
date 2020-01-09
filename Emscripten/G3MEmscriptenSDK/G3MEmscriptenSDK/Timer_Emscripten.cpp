

#include "Timer_Emscripten.hpp"

#include "emscripten/val.h"
#include <math.h>

using namespace emscripten;

long long Timer_Emscripten::nowInMilliseconds() const {
  val Date = val::global("Date");

  val Date_now = Date["now"];
  if(Date_now.as<bool>()) {
    val now = Date_now();
    return now.as<long long>();
  }
  else {
    val date = Date.new_();
    val time = date.call<val>("getTime");
    return time.as<long long>();
  }
}

TimeInterval Timer_Emscripten::now() const {
  return TimeInterval::fromMilliseconds(nowInMilliseconds());
}

TimeInterval Timer_Emscripten::fromDaysFromNow(const double days) const {
  const long daysInMilliseconds = round(days
                                        * 24   /* hours        */
                                        * 60   /* minutes      */
                                        * 60   /* seconds      */
                                        * 1000 /* milliseconds */ );
  
  const long milliseconds = nowInMilliseconds() + daysInMilliseconds;
  
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
