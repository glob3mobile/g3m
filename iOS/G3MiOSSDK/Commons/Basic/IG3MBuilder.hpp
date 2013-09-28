//
//  IG3MBuilder.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 20/11/12.
//
//

#ifndef __G3MiOSSDK__IG3MBuilder__
#define __G3MiOSSDK__IG3MBuilder__

#include <vector>
class GL;
class IStorage;
class IDownloader;
class IThreadUtils;
class ICameraActivityListener;
class CameraRenderer;
class ICameraConstrainer;
class Color;
class GInitializationTask;
class PeriodicalTask;
class G3MWidget;
class PlanetRendererBuilder;
class Planet;
class Renderer;
class WidgetUserData;
class GPUProgramSources;
class GPUProgramManager;
class SceneLighting;
class Sector;
class GEORenderer;
class GEOSymbolizer;
class MeshRenderer;
class ShapesRenderer;
class MarksRenderer;
class ErrorRenderer;


class IG3MBuilder {
private:
  GL*                               _gl;
  IDownloader*                      _downloader;
  IThreadUtils*                     _threadUtils;
  ICameraActivityListener*          _cameraActivityListener;
  const Planet*                     _planet;
  std::vector<ICameraConstrainer*>* _cameraConstraints;
  CameraRenderer*                   _cameraRenderer;
  Color*                            _backgroundColor;
  PlanetRendererBuilder*            _planetRendererBuilder;
  Renderer*                         _busyRenderer;
  ErrorRenderer*                    _errorRenderer;
  std::vector<Renderer*>*           _renderers;
  GInitializationTask*              _initializationTask;
  bool                              _autoDeleteInitializationTask;
  std::vector<PeriodicalTask*>*     _periodicalTasks;
  bool                              _logFPS;
  bool                              _logDownloaderStatistics;
  WidgetUserData*                   _userData;
  std::vector<GPUProgramSources>    _sources;
  SceneLighting*                    _sceneLighting;
  Sector*                           _shownSector;

  GL*                               getGL();
  IDownloader*                      getDownloader();
  IThreadUtils*                     getThreadUtils();
  ICameraActivityListener*          getCameraActivityListener();
  std::vector<ICameraConstrainer*>* getCameraConstraints();
  CameraRenderer*                   getCameraRenderer();
  Renderer*                         getBusyRenderer();
  ErrorRenderer*                    getErrorRenderer();
  Color*                            getBackgroundColor();
  std::vector<Renderer*>*           getRenderers();
  bool                              getLogFPS();
  bool                              getLogDownloaderStatistics();
  GInitializationTask*              getInitializationTask();
  bool                              getAutoDeleteInitializationTask();
  std::vector<PeriodicalTask*>*     getPeriodicalTasks();
  WidgetUserData*                   getUserData();
  GPUProgramManager*                getGPUProgramManager();
  std::vector<ICameraConstrainer*>* createDefaultCameraConstraints();
  CameraRenderer*                   createDefaultCameraRenderer();
  std::vector<Renderer*>*           createDefaultRenderers();
  std::vector<PeriodicalTask*>*     createDefaultPeriodicalTasks();
  Sector                            getShownSector() const;

  void pvtSetInitializationTask(GInitializationTask* initializationTask,
                                const bool autoDeleteInitializationTask);

  bool containsPlanetRenderer(std::vector<Renderer*> renderers);


protected:
  IStorage* _storage;

  IStorage* getStorage();

  G3MWidget* create();

  virtual IThreadUtils* createDefaultThreadUtils() = 0;
  virtual IStorage*     createDefaultStorage()     = 0;
  virtual IDownloader*  createDefaultDownloader()  = 0;


public:
  IG3MBuilder();

  virtual ~IG3MBuilder();

  void setGL(GL* gl);

  void setStorage(IStorage* storage);

  void setDownloader(IDownloader* downloader);

  void setThreadUtils(IThreadUtils* threadUtils);

  void setCameraActivityListener(ICameraActivityListener* cameraActivityListener);

  void setPlanet(const Planet* planet);

  void addCameraConstraint(ICameraConstrainer* cameraConstraint);

  void setCameraConstrainsts(std::vector<ICameraConstrainer*> cameraConstraints);

  void setCameraRenderer(CameraRenderer* cameraRenderer);

  void setBackgroundColor(Color* backgroundColor);

  void setBusyRenderer(Renderer* busyRenderer);

  void setErrorRenderer(ErrorRenderer* errorRenderer);

  void addRenderer(Renderer* renderer);

  void setRenderers(std::vector<Renderer*> renderers);

  void addPeriodicalTask(PeriodicalTask* periodicalTask);

  void setPeriodicalTasks(std::vector<PeriodicalTask*> periodicalTasks);

  void setLogFPS(const bool logFPS);

  void setLogDownloaderStatistics(const bool logDownloaderStatistics);

  void setUserData(WidgetUserData* userData);

#ifdef C_CODE
  void setInitializationTask(GInitializationTask* initializationTask,
                             const bool autoDeleteInitializationTask) {
    pvtSetInitializationTask(initializationTask,
                             autoDeleteInitializationTask);
  }
#endif

#ifdef JAVA_CODE
  public final void setInitializationTask(GInitializationTask initializationTask) {
    pvtSetInitializationTask(initializationTask,
                             true /* parameter ignored in Java code */);
  }
#endif

  const Planet* getPlanet();
  PlanetRendererBuilder* getPlanetRendererBuilder();

  void addGPUProgramSources(const GPUProgramSources& s);

  void setSceneLighting(SceneLighting* sceneLighting);
  SceneLighting* getSceneLighting();

  void setShownSector(const Sector& sector);

  GEORenderer* createGEORenderer(GEOSymbolizer* symbolizer) {
    const bool createMeshRenderer      = true;
    const bool createShapesRenderer    = true;
    const bool createMarksRenderer     = true;
    const bool createGEOTileRasterizer = true;

    return createGEORenderer(symbolizer,
                             createMeshRenderer,
                             createShapesRenderer,
                             createMarksRenderer,
                             createGEOTileRasterizer);
  }

  GEORenderer* createGEORenderer(GEOSymbolizer* symbolizer,
                                 bool createMeshRenderer,
                                 bool createShapesRenderer,
                                 bool createMarksRenderer,
                                 bool createGEOTileRasterizer);

  MeshRenderer* createMeshRenderer();

  ShapesRenderer* createShapesRenderer();

  MarksRenderer* createMarksRenderer();
  
};

#endif
