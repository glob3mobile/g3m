//
//  G3MMarkerRenderer.m
//  G3MApp
//
//  Created by Mari Luz Mateo on 25/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import "G3MMarkerRenderer.hpp"

#import <G3MiOSSDK/MarksRenderer.hpp>
#import <G3MiOSSDK/MarkTouchListener.hpp>
#import <G3MiOSSDK/URL.hpp>
#import "G3MViewController.h"
#import "G3MMarkerUserData.hpp"

class TestMarkTouchListener : public MarkTouchListener {
private:
  G3MViewController* _vc;
public:
  TestMarkTouchListener(G3MViewController* vc) {
    _vc = vc;
  }
  bool touchedMark(Mark* mark) {
    NSString* message = [NSString stringWithFormat: @"%s", ((G3MMarkerUserData*) mark->getUserData())->getTitle().c_str()];
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle: @"glob3 mobile"
                                                    message: message
                                                   delegate: _vc
                                          cancelButtonTitle: @"OK"
                                          otherButtonTitles: @"Learn more...",nil];
    
    URL markUrl = ((G3MMarkerUserData*) mark->getUserData())->getUrl();
    [_vc setValue: [NSString stringWithCString: markUrl.getPath().c_str()
                                      encoding: NSUTF8StringEncoding]
           forKey: @"urlMarkString"];
    [alert show];
    
    return true;
  }
};


MarksRenderer* G3MMarkerRenderer::createMarkerRenderer(G3MViewController* vc) {
  const bool readyWhenMarksReady = false;
  MarksRenderer* marksRenderer = new MarksRenderer(readyWhenMarksReady);
  
  marksRenderer->setMarkTouchListener(new TestMarkTouchListener(vc), true);
  
  return marksRenderer;
}