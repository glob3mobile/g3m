//
//  EAGLView.h
//  Prueba Opengl iPad
//
//  Created by Agustin Trujillo Pino on 12/01/11.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>


@class ES2Renderer;

class TouchEvent;
class GL;
class G3MWidget;
class CameraRenderer;
class Geodetic3D;
class TimeInterval;
class Angle;
class WidgetUserData;


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
