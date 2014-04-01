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
class ProtoRenderer;
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
class G3MWidget;
class IStringBuilder;
class IMathUtils;
class IJSONParser;
class Geodetic3D;
class CameraRenderer;
class IStorage;
class ITextUtils;
class G3MEventContext;
class SurfaceElevationProvider;
class GPUProgram;
class GPUProgramManager;
class ICameraActivityListener;
class GLState;
class PlanetRenderer;
class ErrorRenderer;
class G3MRenderContext;

#include <vector>
#include <string>

#include "Color.hpp"
#include "Angle.hpp"
#include "InitialCameraPositionProvider.hpp"
#include "RenderState.hpp"

class G3MContext;
class GLGlobalState;
class SceneLighting;

class Sector;

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

  static G3MWidget* create(GL*                                  gl,
                           IStorage*                            storage,
                           IDownloader*                         downloader,
                           IThreadUtils*                        threadUtils,
                           ICameraActivityListener*             cameraActivityListener,
                           const Planet*                        planet,
                           std::vector<ICameraConstrainer*>     cameraConstrainers,
                           CameraRenderer*                      cameraRenderer,
                           Renderer*                            mainRenderer,
                           ProtoRenderer*                       busyRenderer,
                           ErrorRenderer*                       errorRenderer,
                           Renderer*                            hudRenderer,
                           const Color&                         backgroundColor,
                           const bool                           logFPS,
                           const bool                           logDownloaderStatistics,
                           GInitializationTask*                 initializationTask,
                           bool                                 autoDeleteInitializationTask,
                           std::vector<PeriodicalTask*>         periodicalTasks,
                           GPUProgramManager*                   gpuProgramManager,
                           SceneLighting*                       sceneLighting,
                           const InitialCameraPositionProvider* initialCameraPositionProvider);

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

  EffectsScheduler* getEffectsScheduler() const {
    return _effectsScheduler;
  }

  const Camera* getCurrentCamera() const {
    return _currentCamera;
  }

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

  void resetPeriodicalTasksTimeouts();

  void setCameraPosition(const Geodetic3D& position);

  void setCameraHeading(const Angle& heading);

  void setCameraPitch(const Angle& pitch);

  void setCameraRoll(const Angle& roll);

  void setCameraHeadingPitchRoll(const Angle& heading,
                                 const Angle& pitch,
                                 const Angle& roll);

  void setAnimatedCameraPosition(const Geodetic3D& position,
                                 const Angle& heading = Angle::zero(),
                                 const Angle& pitch   = Angle::fromDegrees(-90));

  void setAnimatedCameraPosition(const TimeInterval& interval,
                                 const Geodetic3D& position,
                                 const Angle& heading = Angle::zero(),
                                 const Angle& pitch   = Angle::fromDegrees(-90),
                                 const bool linearTiming = false,
                                 const bool linearHeight = false);

  void setAnimatedCameraPosition(const TimeInterval& interval,
                                 const Geodetic3D& fromPosition,
                                 const Geodetic3D& toPosition,
                                 const Angle& fromHeading,
                                 const Angle& toHeading,
                                 const Angle& fromPitch,
                                 const Angle& toPitch,
                                 const bool linearTiming  = false,
                                 const bool linearHeight  = false);

  void cancelCameraAnimation();

  void cancelAllEffects();

  CameraRenderer* getCameraRenderer() const {
    return _cameraRenderer;
  }

  Renderer* getHUDRenderer() const {
    return _hudRenderer;
  }

  const G3MContext* getG3MContext() const {
    return _context;
  }

  void setBackgroundColor(const Color& backgroundColor);

  PlanetRenderer* getPlanetRenderer();

  bool setRenderedSector(const Sector& sector);

  void setForceBusyRenderer(bool forceBusyRenderer) {
    _forceBusyRenderer = forceBusyRenderer;
  }
  
private:
  IStorage*                _storage;
  IDownloader*             _downloader;
  IThreadUtils*            _threadUtils;
  ICameraActivityListener* _cameraActivityListener;

  FrameTasksExecutor* _frameTasksExecutor;
  GL*                 _gl;
  const Planet*       _planet;

  CameraRenderer*     _cameraRenderer;
  Renderer*           _mainRenderer;
  ProtoRenderer*      _busyRenderer;
  ErrorRenderer*      _errorRenderer;
  Renderer*           _hudRenderer;
  RenderState*        _rendererState;
  ProtoRenderer*           _selectedRenderer;

  EffectsScheduler*   _effectsScheduler;

  std::vector<ICameraConstrainer*> _cameraConstrainers;

  Camera*          _currentCamera;
  Camera*          _nextCamera;
  TexturesHandler* _texturesHandler;

  Color*           _backgroundColor;

  ITimer*          _timer;
  long             _renderCounter;
  long             _totalRenderTime;
  const bool       _logFPS;
  const bool       _logDownloaderStatistics;
  std::string      _lastCacheStatistics;
  const int        _nFramesBeetweenProgramsCleanUp;

  ITimer* _renderStatisticsTimer;

  WidgetUserData* _userData;

  GInitializationTask* _initializationTask;
  bool                 _autoDeleteInitializationTask;

  std::vector<PeriodicalTask*> _periodicalTasks;

  int _width;
  int _height;

  const G3MContext* _context;

  bool _paused;
  bool _initializationTaskWasRun;
  bool _initializationTaskReady;

  bool _clickOnProcess;

  GPUProgramManager* _gpuProgramManager;

  SurfaceElevationProvider* _surfaceElevationProvider;

  SceneLighting*            _sceneLighting;
  GLState*                  _rootState;

  const InitialCameraPositionProvider* _initialCameraPositionProvider;
  bool _initialCameraPositionHasBeenSet;

  G3MRenderContext* _renderContext;

  bool _forceBusyRenderer;

  G3MWidget(GL*                              gl,
            IStorage*                        storage,
            IDownloader*                     downloader,
            IThreadUtils*                    threadUtils,
            ICameraActivityListener*         cameraActivityListener,
            const Planet*                    planet,
            std::vector<ICameraConstrainer*> cameraConstrainers,
            CameraRenderer*                  cameraRenderer,
            Renderer*                        mainRenderer,
            ProtoRenderer*                   busyRenderer,
            ErrorRenderer*                   errorRenderer,
            Renderer*                        hudRenderer,
            const Color&                     backgroundColor,
            const bool                       logFPS,
            const bool                       logDownloaderStatistics,
            GInitializationTask*             initializationTask,
            bool                             autoDeleteInitializationTask,
            std::vector<PeriodicalTask*>     periodicalTasks,
            GPUProgramManager*               gpuProgramManager,
            SceneLighting*                   sceneLighting,
            const InitialCameraPositionProvider* initialCameraPositionProvider);

  void notifyTouchEvent(const G3MEventContext &ec,
                        const TouchEvent* touchEvent) const;

  RenderState calculateRendererState();
  
  void setSelectedRenderer(ProtoRenderer* selectedRenderer);
  
};

#endif
