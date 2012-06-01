//
//  EAGLView.h
//  Prueba Opengl iPad
//
//  Created by Agustín Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>

@class ES2Renderer;

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
//    ES2Renderer *renderer;

//    BOOL animating;
    BOOL _displayLinkSupported;
//    NSInteger animationFrameInterval;
    // Use of the CADisplayLink class is the preferred method for controlling your animation timing.
    // CADisplayLink will link to the main display and fire every vsync when added to a given run-loop.
    // The NSTimer class is used only as fallback when running on a pre 3.1 device where CADisplayLink
    // isn't available.
//    id displayLink;
//    NSTimer *__weak animationTimer;

    //BOOL multipleTouchEnabled;

    enum GL_version glver;
}

@property(readonly, nonatomic, getter=isAnimating) BOOL animating;
@property(nonatomic) NSInteger animationFrameInterval;
@property(nonatomic, strong) id displayLink;
@property(nonatomic, weak) NSTimer *animationTimer;
//@property(nonatomic, getter=isMultipleTouchEnabled) BOOL multipleTouchEnabled;
@property(nonatomic, retain) ES2Renderer *renderer;

@property(readonly, nonatomic) void *widget;


- (void)startAnimation;

- (void)stopAnimation;

- (void)drawView:(id)sender;


@end
