
#ifndef ThreadUtils_Emscripten_hpp
#define ThreadUtils_Emscripten_hpp

#include "G3M/IThreadUtils.hpp"


class ThreadUtils_Emscripten : public IThreadUtils {
private:
  const int _delayMillis;
  
public:
  
  ThreadUtils_Emscripten(const int delayMillis);
  
  ~ThreadUtils_Emscripten();
  
  void justInitialized();
  
  void invokeInRendererThread(GTask* task,
                              bool autoDelete) const;
  
  void invokeInBackground(GTask* task,
                          bool autoDelete) const;
  
  void onResume(const G3MContext* context);
  
  void onPause(const G3MContext* context);
  
  void onDestroy(const G3MContext* context);
  
};

#endif
