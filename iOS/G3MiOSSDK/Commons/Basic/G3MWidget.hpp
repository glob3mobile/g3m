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
class GL;
class TexturesHandler;
class Downloader;
class IDownloader;
class Camera;
class EffectsScheduler;
class IStringUtils;
class IThreadUtils;
class GTask;
class TimeInterval;
class IFactory;
class ITimer;
class PeriodicalTask;
class ICameraConstrainer;
class FrameTasksExecutor;
class TextureBuilder;
class G3MWidget;
class IStringBuilder;
class IMathUtils;
class IJSONParser;
class Geodetic3D;
class CameraRenderer;

#include <vector>
#include <string>

#include "Color.hpp"


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
  
  static void initSingletons(ILogger*             logger,
                             IFactory*            factory,
                             const IStringUtils*  stringUtils,
                             IThreadUtils*        threadUtils,
                             IStringBuilder*      stringBuilder,
                             IMathUtils*          mathUtils,
                             IJSONParser*         jsonParser);
  
  static G3MWidget* create(FrameTasksExecutor*              frameTasksExecutor,
                           GL*                              gl,
                           TexturesHandler*                 texturesHandler,
                           TextureBuilder*                  textureBuilder,
                           IDownloader*                     downloader,
                           const Planet*                    planet,
                           std::vector<ICameraConstrainer*> cameraConstrainers,
                           CameraRenderer*                  cameraRenderer,
                           Renderer*                        mainRenderer,
                           Renderer*                        busyRenderer,
                           EffectsScheduler*                effectsScheduler,
                           int                              width,
                           int                              height,
                           Color                            backgroundColor,
                           const bool                       logFPS,
                           const bool                       logDownloaderStatistics,
                           GTask*                           initializationTask,
                           bool                             autoDeleteInitializationTask,
                           std::vector<PeriodicalTask*>     periodicalTasks);
  
  ~G3MWidget();
  
  void render();
  
  void onTouchEvent(const TouchEvent* myEvent);
  
  void onResizeViewportEvent(int width, int height);
  
  void onPause();
  
  void onResume();
  
  GL* getGL() const {
    return _gl;
  }
  
  //  const Camera* getCurrentCamera() const {
  //    return _currentCamera;
  //  }
  
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
  
  void addPeriodicalTask(PeriodicalTask* periodicalTask);
  
  void addPeriodicalTask(const TimeInterval& interval,
                         GTask* task);

  void setAnimatedCameraPosition(const Geodetic3D& g,
                                 const TimeInterval& interval);

private:
  FrameTasksExecutor* _frameTasksExecutor;
  GL*                 _gl;
  const Planet*       _planet;

  CameraRenderer*     _cameraRenderer;
  Renderer*           _mainRenderer;
  Renderer*           _busyRenderer;
  bool                _mainRendererReady;
  Renderer*           _selectedRenderer;

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
  
  ITimer* _renderStatisticsTimer;
  
  UserData* _userData;
  
  GTask* _initializationTask;
  bool   _autoDeleteInitializationTask;
  
  std::vector<PeriodicalTask*> _periodicalTasks;
  
  void initializeGL();
  
  G3MWidget(FrameTasksExecutor*              frameTasksExecutor,
            GL*                              gl,
            TexturesHandler*                 texturesHandler,
            TextureBuilder*                  textureBuilder,
            IDownloader*                     downloader,
            const Planet*                    planet,
            std::vector<ICameraConstrainer*> cameraConstrainers,
            CameraRenderer*                  cameraRenderer,
            Renderer*                        mainRenderer,
            Renderer*                        busyRenderer,
            EffectsScheduler*                effectsScheduler,
            int                              width,
            int                              height,
            Color                            backgroundColor,
            const bool                       logFPS,
            const bool                       logDownloaderStatistics,
            GTask*                           initializationTask,
            bool                             autoDeleteInitializationTask,
            std::vector<PeriodicalTask*>     periodicalTasks);
  
};

#endif
