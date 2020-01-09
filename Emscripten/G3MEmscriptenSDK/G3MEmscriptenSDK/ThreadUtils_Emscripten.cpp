

#include "ThreadUtils_Emscripten.hpp"

#include <stddef.h>

#include <emscripten/html5.h>

#include "GTask.hpp"



ThreadUtils_Emscripten::ThreadUtils_Emscripten(const int delayMillis) :
  _delayMillis(delayMillis)
{
}

ThreadUtils_Emscripten::~ThreadUtils_Emscripten() {
}

void ThreadUtils_Emscripten::justInitialized()  {
  // do nothing
}

void ThreadUtils_Emscripten::onResume(const G3MContext* context) {
  // do nothing
}

void ThreadUtils_Emscripten::onPause(const G3MContext* context) {
  // do nothing
}

void ThreadUtils_Emscripten::onDestroy(const G3MContext* context) {
  // do nothing
}

void ThreadUtils_Emscripten::invokeInRendererThread(GTask* task,
					      bool autoDelete) const {
  task->run(getContext());
  if (autoDelete) {
    delete task;
  }
}


class TaskActivation {
private:
  GTask* _task;
  const bool _autoDelete;
  const G3MContext* _context;

public:
  TaskActivation(GTask*     task,
		 const bool autoDelete,
		 const G3MContext* context) :
    _task(task),
    _autoDelete(autoDelete),
    _context(context)
  {
  }

  void doIt() {
    _task->run( _context );
    if (_autoDelete) {
      delete _task;
      _task = NULL;
    }
  }
};

void __activateTask(void* userData) {
  TaskActivation* taskActivation = (TaskActivation*) userData;
  taskActivation->doIt();
  delete taskActivation;
}

void ThreadUtils_Emscripten::invokeInBackground(GTask* task,
					  bool autoDelete) const  {
#warning TODO: research emscripten_async_queue_XXX
  
  emscripten_set_timeout(__activateTask,
			 _delayMillis,
			 (void*) new TaskActivation(task, autoDelete, getContext()));
}
