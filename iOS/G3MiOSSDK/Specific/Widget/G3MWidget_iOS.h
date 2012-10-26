//
//  EAGLView.h
//  Prueba Opengl iPad
//
//  Created by Agustín Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>

#import "TouchEvent.hpp"

//class G3MWidget;

@class ES2Renderer;

class CameraRenderer;
class LayerSet;
class ICameraConstrainer;
class Renderer;
class UserData;
class TilesRenderParameters;
class G3MWidget;
class PeriodicalTask;
class GTask;

// opengl versions value
enum GL_version {
  OpenGL_1,   //obsolete
  OpenGL_2
};

// This class wraps the CAEAGLLayer from CoreAnimation into a convenient UIView subclass.
// The view content is basically an EAGL surface you render your OpenGL scene into.
// Note that setting the view non-opaque will only work if the EAGL surface has an alpha channel.
@interface G3MWidget_iOS : UIView {
@private
  BOOL _displayLinkSupported;
  
  enum GL_version glver;
  
  TouchEvent *lastTouchEvent;
  
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

- (void)initWidgetWithCameraConstraints: (std::vector<ICameraConstrainer*>) cameraConstraints
                               layerSet: (LayerSet*) layerSet
                 incrementalTileQuality: (bool) incrementalTileQuality
                              renderers: (std::vector<Renderer*>) renderers
                               userData: (UserData*) userData
                     initializationTask: (GTask*) initializationTask
                        periodicalTasks: (std::vector<PeriodicalTask*>) periodicalTasks;

- (void)initWidgetWithCameraRenderer: (CameraRenderer*) cameraRenderer
                   cameraConstraints: (std::vector<ICameraConstrainer*>) cameraConstraints
                            layerSet: (LayerSet*) layerSet
               tilesRenderParameters: (TilesRenderParameters*) parameters
                           renderers: (std::vector<Renderer*>) renderers
                            userData: (UserData*) userData
                  initializationTask: (GTask*) initializationTask
                     periodicalTasks: (std::vector<PeriodicalTask*>) periodicalTasks;

- (G3MWidget*) widget;

- (void)initSingletons;

@end
