//
//  EAGLView.m
//  Prueba Opengl iPad
//
//  Created by Agustín Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#import "Glob3.h"

#import "ES2Renderer.h"

#include "G3Widget.h"
#include "CompositeRenderer.h"
#include "Planet.hpp"

@interface Glob3 ()
@property(nonatomic, getter=isAnimating) BOOL animating;
@end


@implementation Glob3

@synthesize animating, animationFrameInterval, displayLink, animationTimer, renderer /*, multipleTouchEnabled*/, g3Widget;


// You must implement this method
+ (Class)layerClass {
    return [CAEAGLLayer class];
}

//The EAGL view is stored in the nib file. When it's unarchived it's sent -initWithCoder:
- (id)initWithCoder:(NSCoder *)coder {
    self = [super initWithCoder:coder];

    if (self) {
        // Get the layer
        CAEAGLLayer *eaglLayer = (CAEAGLLayer *) self.layer;

        eaglLayer.opaque = TRUE;
        eaglLayer.drawableProperties = [NSDictionary dictionaryWithObjectsAndKeys:
                [NSNumber numberWithBool:FALSE], kEAGLDrawablePropertyRetainedBacking, kEAGLColorFormatRGBA8, kEAGLDrawablePropertyColorFormat, nil];

        // create IGL object
        renderer = nil;
        
        renderer = [[ES2Renderer alloc] init];
        if (!renderer) {
            printf("**** ERROR: Glob3 Mobile needs Opengl ES 2.0\n");
        }
        else {
            printf("*** Using Opengl ES 2.0\n\n");
            glver = OpenGL_2;
        }

        // create GLOB3M WIDGET
        //int w = [self frame].size.width;
        //int h = [self frame].size.height;
        
        g3Widget = new G3Widget();
        G3Widget *g3W = (G3Widget*) [self g3Widget]; 
        
        CompositeRenderer *comp = new CompositeRenderer();
        Planet * p = Planet::createEarth();
        g3W->create(p, comp);

        
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        // rest of initialization
        animating = FALSE;
        displayLinkSupported = FALSE;
        animationFrameInterval = 1;
        displayLink = nil;
        animationTimer = nil;

        self.multipleTouchEnabled = YES; //NECESSARY FOR PROPER PINCH EVENT

        // A system version of 3.1 or greater is required to use CADisplayLink. The NSTimer
        // class is used as fallback when it isn't available.
        NSString *reqSysVer = @"3.1";
        NSString *currSysVer = [[UIDevice currentDevice] systemVersion];
        if ([currSysVer compare:reqSysVer options:NSNumericSearch] != NSOrderedAscending)
            displayLinkSupported = TRUE;

        //Detecting LongPress
        UILongPressGestureRecognizer *longPressRecognizer = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(handleLongPress:)];
        [self addGestureRecognizer:longPressRecognizer];
    }
    return self;
}

- (IBAction)handleLongPress:(UIGestureRecognizer *)sender {

    if (sender.state == UIGestureRecognizerStateEnded) {
        NSLog(@"LONG PRESS");
    }

}


- (void)drawView:(id)sender {
    if (animating) {
        [renderer render: (G3Widget*)[self g3Widget]];
    }
}

- (void)layoutSubviews {
    int w = [self frame].size.width;
    int h = [self frame].size.height;
    //SceneController::GetInstance()->ResizeCanvas(w, h);
    printf("RESIZING CANVAS: %d, %d\n", w, h);
    //SceneController::GetInstance()->ForceRedrawNextFrame();

    [renderer resizeFromLayer:(CAEAGLLayer *) self.layer];
    [self drawView:nil];
}

- (NSInteger)animationFrameInterval {
    return animationFrameInterval;
}

- (void)setAnimationFrameInterval:(NSInteger)frameInterval {
    // Frame interval defines how many display frames must pass between each time the
    // display link fires. The display link will only fire 30 times a second when the
    // frame internal is two on a display that refreshes 60 times a second. The default
    // frame interval setting of one will fire 60 times a second when the display refreshes
    // at 60 times a second. A frame interval setting of less than one results in undefined
    // behavior.
    if (frameInterval >= 1) {
        animationFrameInterval = frameInterval;

        if (animating) {
            [self stopAnimation];
            [self startAnimation];
        }
    }
}

- (void)startAnimation {
    if (!animating) {
        if (displayLinkSupported) {
            // CADisplayLink is API new to iPhone SDK 3.1. Compiling against earlier versions will result in a warning, but can be dismissed
            // if the system version runtime check for CADisplayLink exists in -initWithCoder:. The runtime check ensures this code will
            // not be called in system versions earlier than 3.1.

            self.displayLink = [NSClassFromString(@"CADisplayLink") displayLinkWithTarget:self selector:@selector(drawView:)];
            [displayLink setFrameInterval:animationFrameInterval];
            [displayLink addToRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];
        }
        else
            self.animationTimer = [NSTimer scheduledTimerWithTimeInterval:(NSTimeInterval) ((1.0 / 60.0) * animationFrameInterval) target:self selector:@selector(drawView:) userInfo:nil repeats:TRUE];

        self.animating = TRUE;
    }
}

- (void)stopAnimation {
    if (animating) {
        if (displayLinkSupported) {
            [displayLink invalidate];
            self.displayLink = nil;
        }
        else {
            [animationTimer invalidate];
            self.animationTimer = nil;
        }

        self.animating = FALSE;
    }
}


- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
//    UITouch *touch = [touches anyObject];
//    CGPoint current = [touch locationInView:self];
//    CGPoint previous = [touch previousLocationInView:self];
}


- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
//    NSSet *allTouches = [event allTouches];
}


- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
//    UITouch *touch = [touches anyObject];
//    CGPoint previous = [touch previousLocationInView:self];
//    CGPoint current = [touch locationInView:self];
}

- (void)dealloc {
}

@end
