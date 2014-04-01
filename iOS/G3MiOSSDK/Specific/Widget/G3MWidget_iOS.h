//
//  EAGLView.h
//  Prueba Opengl iPad
//
//  Created by Agustin Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>

#import "TouchEvent.hpp"
#import "GL.hpp"

//class G3MWidget;

@class ES2Renderer;

class IStorage;
class IDownloader;
class IThreadUtils;
class ICameraActivityListener;
class Planet;
class Color;
class CameraRenderer;
class LayerSet;
class ICameraConstrainer;
class Renderer;
class ProtoRenderer;
class WidgetUserData;
class PlanetRenderer;
class TilesRenderParameters;
class G3MWidget;
class PeriodicalTask;
class GInitializationTask;
class TimeInterval;
class ErrorRenderer;

// This class wraps the CAEAGLLayer from CoreAnimation into a convenient UIView subclass.
// The view content is basically an EAGL surface you render your OpenGL scene into.
// Note that setting the view non-opaque will only work if the EAGL surface has an alpha channel.
@interface G3MWidget_iOS : UIView {
@private
  BOOL _displayLinkSupported;

  TouchEvent* _lastTouchEvent;

  void* _widgetVP;
}

@property(readonly, nonatomic, getter=isAnimating) BOOL         animating;
@property(nonatomic)                               NSInteger    animationFrameInterval;
@property(nonatomic, strong)                       id           displayLink;
@property(nonatomic, weak)                         NSTimer*     animationTimer;
@property(nonatomic, retain)                       ES2Renderer* renderer;


- (void)startAnimation;

- (void)stopAnimation;

- (void)drawView: (id)sender;

- (void)          initWidget: (IStorage*) storage
                  downloader: (IDownloader*) downloader
                 threadUtils: (IThreadUtils*) threadUtils
      cameraActivityListener: (ICameraActivityListener*) cameraActivityListener
                      planet: (const Planet*) planet
           cameraConstraints: (std::vector<ICameraConstrainer*>) cameraConstrainers
              cameraRenderer: (CameraRenderer*) cameraRenderer
                mainRenderer: (Renderer*) mainRenderer
                busyRenderer: (ProtoRenderer*) busyRenderer
               errorRenderer: (ErrorRenderer*) errorRenderer
                 hudRenderer: (Renderer*) hudRenderer
             backgroundColor: (Color) backgroundColor
                      logFPS: (bool) logFPS
     logDownloaderStatistics: (bool) logDownloaderStatistics
          initializationTask: (GInitializationTask*) initializationTask
autoDeleteInitializationTask: (bool) autoDeleteInitializationTask
             periodicalTasks: (std::vector<PeriodicalTask*>) periodicalTasks
                    userData: (WidgetUserData*) userData
       initialCameraPosition: (Geodetic3D) initialCameraPosition;

- (GL*)getGL;

- (void)setWidget: (G3MWidget*) widget;

- (G3MWidget*) widget;

- (void)initSingletons;

- (CameraRenderer*)getCameraRenderer;

- (void)setAnimatedCameraPosition: (const Geodetic3D&) position
                     timeInterval: (const TimeInterval&)interval;

- (void)setAnimatedCameraPosition: (const Geodetic3D&) position;

- (void)setCameraPosition: (const Geodetic3D&) position;

- (void)setCameraHeading: (const Angle&) angle;

- (void)setCameraPitch: (const Angle&) angle;

- (void)cancelCameraAnimation;

- (WidgetUserData*) userData;

@end
