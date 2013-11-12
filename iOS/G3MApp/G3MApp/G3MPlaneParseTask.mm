//
//  G3MPlaneParseTask.m
//  G3MApp
//
//  Created by Mari Luz Mateo on 22/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import "G3MPlaneParseTask.hpp"

#import <G3MiOSSDK/G3MWidget_iOS.h>
#import <G3MiOSSDK/ShapesRenderer.hpp>
#import <G3MiOSSDK/SceneJSShapesParser.hpp>
#import <G3MiOSSDK/ByteBuffer_iOS.hpp>
#import <G3MiOSSDK/IThreadUtils.hpp>
#import <G3MiOSSDK/URL.hpp>
#import "G3MAppUserData.hpp"
#import <G3MiOSSDK/SGShape.hpp>


class G3MAddShapeTask : public GTask {
  
private:
  G3MWidget_iOS* _widget;
  
public:
  G3MAddShapeTask(G3MWidget_iOS* widget) {
    _widget = widget;
  }
  
  void run(const G3MContext* context) {
    ShapesRenderer* shapeRenderer = ((G3MAppUserData*) [_widget userData])->getShapeRenderer();
    shapeRenderer->addShape(((G3MAppUserData*) [_widget userData])->getPlane());
  }
};


G3MPlaneParseTask::G3MPlaneParseTask(G3MWidget_iOS* widget) {
  _widget = widget;
}

void G3MPlaneParseTask::run(const G3MContext* context) {
  NSString *bsonFilePath = [[NSBundle mainBundle] pathForResource: @"A320"
                                                           ofType: @"bson"];
  if (bsonFilePath) {
    NSData* data = [NSData dataWithContentsOfFile: bsonFilePath];
    const int length = [data length];
    unsigned char* bytes = new unsigned char[ length ]; // will be deleted by IByteBuffer's destructor
    [data getBytes: bytes
            length: length];
    
    IByteBuffer* buffer = new ByteBuffer_iOS(bytes, length);
    if (buffer) {
      SGShape* plane = SceneJSShapesParser::parseFromBSON(buffer, URL::FILE_PROTOCOL, false, NULL, ABSOLUTE);
      if (plane) {
        ((G3MAppUserData*) [_widget userData])->setPlane(plane);
        plane->setPosition(new Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
                                          Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
                                          10000) );
        const double scale = 200;
        plane->setScale(scale, scale, scale);
        plane->setPitch(Angle::fromDegrees(90));
        
        context->getThreadUtils()->invokeInRendererThread(new G3MAddShapeTask(_widget), true);
      }
      delete buffer;
    }
  }
  
  /*
   NSString *jsonFilePath = [[NSBundle mainBundle] pathForResource: @"A320"
   ofType: @"json"];
   if (jsonFilePath) {
   NSString *nsPlaneJSON = [NSString stringWithContentsOfFile: jsonFilePath
   encoding: NSUTF8StringEncoding
   error: nil];
   if (nsPlaneJSON) {
   std::string planeJSON = [nsPlaneJSON UTF8String];
   Shape* plane = SceneJSShapesParser::parseFromJSON(planeJSON, URL::FILE_PROTOCOL);
   if (plane) {
   ((G3MAppUserData*) [_widget getUserData])->setPlane(plane);
   plane->setPosition(new Geodetic3D(Angle::fromDegreesMinutesSeconds(38, 53, 42.24),
   Angle::fromDegreesMinutesSeconds(-77, 2, 10.92),
   10000) );
   const double scale = 200;
   plane->setScale(scale, scale, scale);
   plane->setPitch(Angle::fromDegrees(90));
   
   context->getThreadUtils()->invokeInRendererThread(new G3MAddShapeTask(_widget), true);
   }
   }
   }
   */
}
