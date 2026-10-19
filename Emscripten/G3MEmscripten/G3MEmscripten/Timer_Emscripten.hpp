
#ifndef Timer_Emscripten_hpp
#define Timer_Emscripten_hpp

#include "G3M/ITimer.hpp"


class Timer_Emscripten : public ITimer {
private:
  long long _startTimeInMilliseconds;
  
public:
  Timer_Emscripten();

  TimeInterval now() const;
  
  TimeInterval fromDaysFromNow(const double days) const;
  
  long long nowInMilliseconds() const;
  
  void start();
  
  TimeInterval elapsedTime() const;
  
  long long elapsedTimeInMilliseconds() const;
  
};

#endif
