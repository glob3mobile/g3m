
#ifndef ThreadUtils_Wasm_hpp
#define ThreadUtils_Wasm_hpp

#include "IThreadUtils.hpp"


class ThreadUtils_Wasm : public IThreadUtils {
private:
  const int _delayMillis;
  
public:

  ThreadUtils_Wasm(const int delayMillis);

  ~ThreadUtils_Wasm();

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
