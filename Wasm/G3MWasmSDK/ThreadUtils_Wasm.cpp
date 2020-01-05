

#include "ThreadUtils_Wasm.hpp"

#include <emscripten/html5.h>


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


class TaskActivation {
private:
  GTask* _task;
  const bool _autoDelete;
  G3MContext _context;

public:
  TaskActivation(GTask*     task,
		 const bool autoDelete,
		 G3MContext context) :
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

void __activateTask(void *userData) {
  TaskActivation* taskActivation = (TaskActivation*) userData;
  taskActivation->doIt();
  delete taskActivation;
}

void ThreadUtils_Wasm::invokeInBackground(GTask* task,
					  bool autoDelete) const  {
  emscripten_set_timeout(__activateTask,
			 _delayMillis,
			 (void*) new TaskActivation(task, autoDelete, getContext()));
}
