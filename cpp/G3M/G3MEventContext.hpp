//
//  G3MEventContext.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/29/16.
//
//

#ifndef G3MEventContext_hpp
#define G3MEventContext_hpp

#include "G3MContext.hpp"

class Camera;

class G3MEventContext : public G3MContext {
public:

  const Camera*             _currentCamera;
  
  G3MEventContext(const IFactory*           factory,
                  const IStringUtils*       stringUtils,
                  const IThreadUtils*       threadUtils,
                  const ILogger*            logger,
                  const IMathUtils*         mathUtils,
                  const IJSONParser*        jsonParser,
                  const Planet*             planet,
                  IDownloader*              downloader,
                  EffectsScheduler*         scheduler,
                  IStorage*                 storage,
                  SurfaceElevationProvider* surfaceElevationProvider,
                  ViewMode                  viewMode,
                  const Camera*             currentCamera) :
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
  _currentCamera(currentCamera)
  {
  }

};

#endif
