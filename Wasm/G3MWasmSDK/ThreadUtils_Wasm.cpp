

#include "ThreadUtils_Wasm.hpp"


delayMillis

ThreadUtils_Wasm::ThreadUtils_Wasm(const int delayMillis) :
  _delayMillis(delayMillis)
{
}

ThreadUtils_Wasm::~ThreadUtils_Wasm() {
}

void ThreadUtils_Wasm::justInitialized()  {
  // do nothing
}

void ThreadUtils_Wasm::onResume(const G3MContext* context) {
  // do nothing
}

void ThreadUtils_Wasm::onPause(const G3MContext* context) {
  // do nothing
}

void ThreadUtils_Wasm::onDestroy(const G3MContext* context) {
  // do nothing
}

void ThreadUtils_Wasm::invokeInRendererThread(GTask* task,
					      bool autoDelete) const {
  task->run(getContext());
  if (autoDelete) {
    delete task;
  }
}

void ThreadUtils_Wasm::invokeInBackground(GTask* task,
					  bool autoDelete) const  {
#error TODO
}
