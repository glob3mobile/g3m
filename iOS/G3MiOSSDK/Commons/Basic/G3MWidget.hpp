//
//  G3MWidget.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_G3MWidget_h
#define G3MiOSSDK_G3MWidget_h

class Renderer;
class TouchEvent;
class Planet;
class ILogger;
class IFactory;
class GL;
class TexturesHandler;
class Downloader;
class IDownloader;
class Camera;
class ITimer;
class EffectsScheduler;
class IStringUtils;
class IThreadUtils;

#include <vector>
#include <string>

#include "Color.hpp"

class ICameraConstrainer;
class FrameTasksExecutor;
class TextureBuilder;

class G3MWidget;

class UserData {
private:
  G3MWidget* _widget;
  
public:
  virtual ~UserData() {
    
  }
  
  void setWidget(G3MWidget* widget) {
    _widget = widget;
  }
  
  G3MWidget* getWidget() const {
    return _widget;
  }
};


class G3MWidget {
public:
  
  static G3MWidget* create(FrameTasksExecutor* frameTasksExecutor,
                           IFactory*           factory,
                           const IStringUtils* stringUtils,
                           IThreadUtils*       threadUtils,
                           ILogger*            logger,
                           GL*                 gl,
                           TexturesHandler*    texturesHandler,
                           TextureBuilder*     textureBuilder,
                           IDownloader*        downloader,
                           const Planet*       planet,
                           std::vector<ICameraConstrainer*> cameraConstraint,
                           Renderer*           renderer,
                           Renderer*           busyRenderer,
                           EffectsScheduler*   scheduler,
                           int                 width,
                           int                 height,
                           Color               backgroundColor,
                           const bool          logFPS,
                           const bool          logDownloaderStatistics);
  
  ~G3MWidget();
  
  void render();
  
  void onTouchEvent(const TouchEvent* myEvent);
  
  void onResizeViewportEvent(int width, int height);
  
  GL* getGL() const {
    return _gl;
  }
  
  /*  const Camera* getCurrentCamera() const {
   return _currentCamera;
   }*/
  
  Camera* getNextCamera() const {
    return _nextCamera;
  }
  
  void setUserData(UserData* userData) {
    if (_userData != NULL) {
      delete _userData;
    }
    _userData = userData;
    if (_userData != NULL) {
      _userData->setWidget(this);
    }
  }
  
  UserData* getUserData() const {
    return _userData;
  }
  
private:
  FrameTasksExecutor* _frameTasksExecutor;
  IFactory*           _factory;
  const IStringUtils* _stringUtils;
  IThreadUtils*       _threadUtils;
  ILogger*            _logger;
  GL*                 _gl;
  const Planet*       _planet;
  Renderer*           _renderer;
  Renderer*           _busyRenderer;
  EffectsScheduler*   _effectsScheduler;
  
  std::vector<ICameraConstrainer*> _cameraConstrainers;
  
  Camera*          _currentCamera;
  Camera*          _nextCamera;
  IDownloader*     _downloader;
  TexturesHandler* _texturesHandler;
  TextureBuilder*  _textureBuilder;
  const Color      _backgroundColor;
  
  ITimer*          _timer;
  long             _renderCounter;
  long             _totalRenderTime;
  const bool       _logFPS;
  const bool       _logDownloaderStatistics;
  std::string      _lastCacheStatistics;
  
  bool      _rendererReady;
  Renderer* _selectedRenderer;
  
  ITimer* _renderStatisticsTimer;
  
  UserData* _userData;
  
  void initializeGL();
  
  G3MWidget(FrameTasksExecutor* frameTasksExecutor,
            IFactory*           factory,
            const IStringUtils* stringUtils,
            IThreadUtils*       threadUtils,
            ILogger*            logger,
            GL*                 gl,
            TexturesHandler*    texturesHandler,
            TextureBuilder*     textureBuilder,
            IDownloader*        downloader,
            const Planet*       planet,
            std::vector<ICameraConstrainer*> cameraConstraint,
            Renderer*           renderer,
            Renderer*           busyRenderer,
            EffectsScheduler*   scheduler,
            int                 width,
            int                 height,
            Color               backgroundColor,
            const bool          logFPS,
            const bool          logDownloaderStatistics);
  
};

#endif
