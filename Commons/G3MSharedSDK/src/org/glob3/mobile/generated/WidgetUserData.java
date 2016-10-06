package org.glob3.mobile.generated; 
//
//  G3MWidget.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//

//
//  G3MWidget.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//



//class IMathUtils;
//class ILogger;
//class G3MWidget;
//class IFactory;
//class IStringUtils;
//class IStringBuilder;
//class IJSONParser;
//class ITextUtils;
//class IDeviceAttitude;
//class IDeviceLocation;
//class ICameraActivityListener;
//class GL;
//class IStorage;
//class IDownloader;
//class IThreadUtils;
//class Planet;
//class ICameraConstrainer;
//class CameraRenderer;
//class ProtoRenderer;
//class ErrorRenderer;
//class Color;
//class GInitializationTask;
//class PeriodicalTask;
//class GPUProgramManager;
//class SceneLighting;
//class InitialCameraPositionProvider;
//class TouchEvent;
//class EffectsScheduler;
//class Camera;
//class TimeInterval;
//class GTask;
//class Geodetic3D;
//class G3MContext;
//class PlanetRenderer;
//class Sector;
//class FrameTasksExecutor;
//class TexturesHandler;
//class ITimer;
//class G3MRenderContext;
//class SurfaceElevationProvider;
//class GLState;
//class G3MEventContext;


public class WidgetUserData
{
  private G3MWidget _widget;

  public WidgetUserData()
  {
     _widget = null;

  }

  public void dispose()
  {

  }

  public final void setWidget(G3MWidget widget)
  {
    _widget = widget;
  }

  public final G3MWidget getWidget()
  {
    return _widget;
  }
}