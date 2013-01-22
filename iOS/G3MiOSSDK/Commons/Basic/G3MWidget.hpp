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
class GInitializationTask;
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
class IStorage;
class ITextUtils;

#include <vector>
#include <string>

#include "Color.hpp"
#include "Angle.hpp"

class G3MContext;
class GLState;

class WidgetUserData {
private:
  G3MWidget* _widget;

public:
  WidgetUserData() :
  _widget(NULL)
  {

  }

  virtual ~WidgetUserData() {

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

  static void initSingletons(ILogger*            logger,
                             IFactory*           factory,
                             const IStringUtils* stringUtils,
                             IStringBuilder*     stringBuilder,
                             IMathUtils*         mathUtils,
                             IJSONParser*        jsonParser,
                             ITextUtils*         textUtils);

  static G3MWidget* create(GL*                              gl,
                           IStorage*                        storage,
                           IDownloader*                     downloader,
                           IThreadUtils*                    threadUtils,
                           const Planet*                    planet,
                           std::vector<ICameraConstrainer*> cameraConstrainers,
                           CameraRenderer*                  cameraRenderer,
                           Renderer*                        mainRenderer,
                           Renderer*                        busyRenderer,
                           Color                            backgroundColor,
                           const bool                       logFPS,
                           const bool                       logDownloaderStatistics,
                           GInitializationTask*             initializationTask,
                           bool                             autoDeleteInitializationTask,
                           std::vector<PeriodicalTask*>     periodicalTasks);

  ~G3MWidget();

  void render(int width, int height);

  void onTouchEvent(const TouchEvent* myEvent);

  void onResizeViewportEvent(int width, int height);

  void onPause();

  void onResume();

  void onDestroy();

  GL* getGL() const {
    return _gl;
  }

  //  const Camera* getCurrentCamera() const {
  //    return _currentCamera;
  //  }

  Camera* getNextCamera() const {
    return _nextCamera;
  }

  void setUserData(WidgetUserData* userData) {
    delete _userData;

    _userData = userData;
    if (_userData != NULL) {
      _userData->setWidget(this);
    }
  }

  WidgetUserData* getUserData() const {
    return _userData;
  }

  void addPeriodicalTask(PeriodicalTask* periodicalTask);

  void addPeriodicalTask(const TimeInterval& interval,
                         GTask* task);


  void setCameraPosition(const Geodetic3D& position);

  void setCameraHeading(const Angle& angle);

  void setCameraPitch(const Angle& angle);

  void setAnimatedCameraPosition(const Geodetic3D& position);

  void setAnimatedCameraPosition(const Geodetic3D& position,
                                 const TimeInterval& interval);

  void resetCameraPosition();

  CameraRenderer* getCameraRenderer() const {
    return _cameraRenderer;
  }

  const G3MContext* getG3MContext() const {
    return _context;
  }

private:
  IStorage*           _storage;
  IDownloader*        _downloader;
  IThreadUtils*       _threadUtils;

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

  WidgetUserData* _userData;

  GInitializationTask* _initializationTask;
  bool                 _autoDeleteInitializationTask;

  std::vector<PeriodicalTask*> _periodicalTasks;

  int _width;
  int _height;

  void initializeGL();

  const G3MContext* _context;

  bool _paused;

  const GLState* _rootState;

  bool _initializationTaskWasRun;

  G3MWidget(GL*                              gl,
            IStorage*                        storage,
            IDownloader*                     downloader,
            IThreadUtils*                    threadUtils,
            const Planet*                    planet,
            std::vector<ICameraConstrainer*> cameraConstrainers,
            CameraRenderer*                  cameraRenderer,
            Renderer*                        mainRenderer,
            Renderer*                        busyRenderer,
            Color                            backgroundColor,
            const bool                       logFPS,
            const bool                       logDownloaderStatistics,
            GInitializationTask*             initializationTask,
            bool                             autoDeleteInitializationTask,
            std::vector<PeriodicalTask*>     periodicalTasks);
  
};

#endif
