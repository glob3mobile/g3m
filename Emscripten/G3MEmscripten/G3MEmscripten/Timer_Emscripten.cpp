

#include "Timer_Emscripten.hpp"

#include <emscripten/emscripten.h>
//#include <emscripten/html5.h>

//#include <emscripten/val.h>
#include <math.h>

//using namespace emscripten;

long long Timer_Emscripten::nowInMilliseconds() const {
//  emscripten_console_log("Timer_Emscripten::nowInMilliseconds() 1");
//  const val Date = val::global("Date");
//
//  emscripten_console_log("Timer_Emscripten::nowInMilliseconds() 2");
//  const val Date_now = Date["now"];
//  emscripten_console_log("Timer_Emscripten::nowInMilliseconds() 3");
//  if (Date_now.as<bool>()) {
////    val now = Date_now();
////    return now.as<long long>();
//    emscripten_console_log("Timer_Emscripten::nowInMilliseconds() 4");
//    return Date.call<long long>("now");
//  }
//  else {
//    emscripten_console_log("Timer_Emscripten::nowInMilliseconds() 5");
//    val date = Date.new_();
////    val time = date.call<val>("getTime");
////    return time.as<long long>();
//    emscripten_console_log("Timer_Emscripten::nowInMilliseconds() 6");
//    return date.call<long long>("getTime");
//  }
  return (long long) emscripten_get_now();
}

TimeInterval Timer_Emscripten::now() const {
  return TimeInterval::fromMilliseconds(nowInMilliseconds());
}

TimeInterval Timer_Emscripten::fromDaysFromNow(const double days) const {
  const long daysInMilliseconds = (long) round(days
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
