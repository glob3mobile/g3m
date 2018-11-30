//
//  IThreadUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 30/08/12.
//
//

#include "IThreadUtils.hpp"

#include "GTask.hpp"
#include "GAsyncTask.hpp"
#include "G3MContext.hpp"
#include "ErrorHandling.hpp"


class IThreadUtils_RendererTask : public GTask {
private:
  GAsyncTask* _task;
  const bool  _autodelete;

public:
  IThreadUtils_RendererTask(GAsyncTask* task,
                            bool autodelete) :
  _task(task),
  _autodelete(autodelete)
  {
  }

  void run(const G3MContext* context) {
    _task->onPostExecute(context);
  }

  ~IThreadUtils_RendererTask() {
    if (_autodelete) {
      delete _task;
    }
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
};

class IThreadUtils_BackgroundTask : public GTask {
private:
  GAsyncTask* _task;
  const bool  _autodelete;

public:
  IThreadUtils_BackgroundTask(GAsyncTask* task,
                              bool autodelete) :
  _task(task),
  _autodelete(autodelete)
  {
  }

  void run(const G3MContext* context) {
    _task->runInBackground(context);

    context->getThreadUtils()->invokeInRendererThread(new IThreadUtils_RendererTask(_task,
                                                                                    _autodelete),
                                                      true);
  }

};


IThreadUtils::IThreadUtils() :
_context(NULL)
{
}

void IThreadUtils::initialize(const G3MContext* context) {
  if (context == NULL) {
    THROW_EXCEPTION("context can't be NULL");
  }
  if (_context != NULL) {
    THROW_EXCEPTION("IThreadUtils already initialized");
  }
  _context = context;
  justInitialized();
}

const bool IThreadUtils::isInitialized() const {
  return (_context != NULL);
}

const G3MContext* IThreadUtils::getContext() const {
  if (_context == NULL) {
    THROW_EXCEPTION("IThreadUtils is not initialized");
  }
  return _context;
}

void IThreadUtils::invokeAsyncTask(GAsyncTask* task,
                                   bool autodelete) const {
  invokeInBackground(new IThreadUtils_BackgroundTask(task, autodelete),
                     true);
}
