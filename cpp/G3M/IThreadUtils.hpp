//
//  IThreadUtils.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 30/08/12.
//
//

#ifndef __G3M__IThreadUtils__
#define __G3M__IThreadUtils__

class G3MContext;
class GTask;
class GAsyncTask;


class IThreadUtils {
private:
#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

protected:
  virtual void justInitialized() = 0;

  const bool isInitialized() const;

  const G3MContext* getContext() const;

public:
  IThreadUtils();

  virtual void onResume(const G3MContext* context) = 0;

  virtual void onPause(const G3MContext* context) = 0;

  virtual void onDestroy(const G3MContext* context) = 0;

  void initialize(const G3MContext* context);

  virtual ~IThreadUtils() {
  }

  virtual void invokeInRendererThread(GTask* task,
                                      bool autoDelete) const = 0;

  virtual void invokeInBackground(GTask* task,
                                  bool autoDelete) const = 0;

  void invokeAsyncTask(GAsyncTask* task,
                       bool autodelete) const;
  
};

#endif
