//
//  G3MRenderContext.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//

#ifndef G3MRenderContext_hpp
#define G3MRenderContext_hpp

#include "G3MContext.hpp"

class OrderedRenderable;
class FrameTasksExecutor;
class GL;
class Camera;
class TexturesHandler;
class ITimer;
class GPUProgramManager;

#include <vector>


#ifdef C_CODE
bool MyDataSortPredicate(const OrderedRenderable* or1,
                         const OrderedRenderable* or2);
#endif

class G3MRenderContext : public G3MContext {
private:
  FrameTasksExecutor* _frameTasksExecutor;
  GL*                 _gl;
  const Camera*       _currentCamera;
  Camera*             _nextCamera;
  TexturesHandler*    _texturesHandler;
  ITimer*             _frameStartTimer;
  GPUProgramManager*  _gpuProgramManager;


  mutable std::vector<OrderedRenderable*>* _orderedRenderables;

public:
  G3MRenderContext(FrameTasksExecutor*       frameTasksExecutor,
                   const IFactory*           factory,
                   const IStringUtils*       stringUtils,
                   const IThreadUtils*       threadUtils,
                   const ILogger*            logger,
                   const IMathUtils*         mathUtils,
                   const IJSONParser*        jsonParser,
                   const Planet*             planet,
                   GL*                       gl,
                   const Camera*             currentCamera,
                   Camera*                   nextCamera,
                   TexturesHandler*          texturesHandler,
                   IDownloader*              downloader,
                   EffectsScheduler*         scheduler,
                   ITimer*                   frameStartTimer,
                   IStorage*                 storage,
                   GPUProgramManager*        gpuProgramManager,
                   SurfaceElevationProvider* surfaceElevationProvider,
                   ViewMode                  viewMode) :
  G3MContext(factory,
             stringUtils,
             threadUtils,
             logger,
             mathUtils,
             jsonParser,
             planet,
             downloader,
             scheduler,
             storage,
             surfaceElevationProvider,
             viewMode),
  _frameTasksExecutor(frameTasksExecutor),
  _gl(gl),
  _currentCamera(currentCamera),
  _nextCamera(nextCamera),
  _texturesHandler(texturesHandler),
  _frameStartTimer(frameStartTimer),
  _orderedRenderables(NULL),
  _gpuProgramManager(gpuProgramManager)
  {

  }

  void clear();

  GL* getGL() const {
    return _gl;
  }

  const Camera* getCurrentCamera() const {
    return _currentCamera;
  }

  Camera* getNextCamera() const {
    return _nextCamera;
  }

  TexturesHandler* getTexturesHandler() const {
    return _texturesHandler;
  }

  const ITimer* getFrameStartTimer() const {
    return _frameStartTimer;
  }

  FrameTasksExecutor* getFrameTasksExecutor() const {
    return _frameTasksExecutor;
  }

  GPUProgramManager* getGPUProgramManager() const {
    return _gpuProgramManager;
  }

  virtual ~G3MRenderContext();

  /*
   Get the OrderedRenderables, sorted by distanceFromEye()
   */
  std::vector<OrderedRenderable*>* getSortedOrderedRenderables() const;

  void addOrderedRenderable(OrderedRenderable* orderedRenderable) const;
    
    void clearOrderedRenderables();
  
};

#endif
