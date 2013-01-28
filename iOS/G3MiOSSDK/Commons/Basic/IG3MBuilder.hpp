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
#include "GL.hpp"
#include "IStorage.hpp"
#include "IDownloader.hpp"
#include "IThreadUtils.hpp"
#include "CameraRenderer.hpp"
#include "CameraConstraints.hpp"
#include "Color.hpp"
#include "GInitializationTask.hpp"
#include "PeriodicalTask.hpp"
#include "G3MWidget.hpp"
#include "TileRendererBuilder.hpp"



class IG3MBuilder {

private:
  GL* _gl;
  IDownloader* _downloader;
  IThreadUtils* _threadUtils;
  const Planet* _planet;
  std::vector<ICameraConstrainer*> _cameraConstraints;
  CameraRenderer* _cameraRenderer;
  Color* _backgroundColor;
  TileRendererBuilder* _tileRendererBuilder;
  Renderer* _busyRenderer;
  std::vector<Renderer*> _renderers;
  GInitializationTask* _initializationTask;
  bool _autoDeleteInitializationTask;
  std::vector<PeriodicalTask*> _periodicalTasks;
  bool _logFPS;
  bool _logDownloaderStatistics;
  WidgetUserData* _userData;

  std::vector<ICameraConstrainer*> createCameraConstraints();
  CameraRenderer* createCameraRenderer();

  void pvtSetInitializationTask(GInitializationTask* initializationTask,
                                const bool autoDeleteInitializationTask);


protected:
  IStorage* _storage;

  G3MWidget* create();

  virtual IThreadUtils* createThreadUtils() = 0;
  virtual IStorage*     createStorage()     = 0;
  virtual IDownloader*  createDownloader()  = 0;

public:
  IG3MBuilder();
  virtual ~IG3MBuilder();
  void setGL(GL* gl);
  void setStorage(IStorage* storage);
  void setDownloader(IDownloader* downloader);
  void setThreadUtils(IThreadUtils* threadUtils);
  void setPlanet(const Planet* planet);
  const Planet* getPlanet();
  void addCameraConstraint(ICameraConstrainer* cameraConstraint);
  void setCameraRenderer(CameraRenderer* cameraRenderer);
  void setBackgroundColor(Color* backgroundColor);
  TileRendererBuilder* getTileRendererBuilder();
  void setBusyRenderer(Renderer* busyRenderer);
  void addRenderer(Renderer* renderer);
  void addPeriodicalTask(PeriodicalTask* periodicalTask);
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

};

#endif /* defined(__G3MiOSSDK__IG3MBuilder__) */
