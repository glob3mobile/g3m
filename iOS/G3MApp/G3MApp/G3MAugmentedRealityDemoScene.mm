//
//  G3MAugmentedRealityDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 3/4/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#include "G3MAugmentedRealityDemoScene.hpp"

#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/BingMapsLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/Camera.hpp>
#include <G3MiOSSDK/GTask.hpp>
#include <G3MiOSSDK/PeriodicalTask.hpp>

#include "G3MDemoModel.hpp"

#import <CoreLocation/CoreLocation.h>
#import <CoreMotion/CoreMotion.h>


void G3MAugmentedRealityDemoScene::deactivate(const G3MContext* context) {
  [_locationManager stopUpdatingLocation];
  [_locationManager stopUpdatingHeading];
  _locationManager = nil;

  [_motionManager stopDeviceMotionUpdates];
  _motionManager = nil;

  G3MDemoScene::deactivate(context);
}


class UpdateCameraTask : public GTask {
private:
  G3MWidget*         _g3mWidget;
  CLLocationManager* _locationManager;
  CMMotionManager*   _motionManager;

public:
  UpdateCameraTask(G3MWidget*         g3mWidget,
                   CLLocationManager* locationManager,
                   CMMotionManager*   motionManager) :
  _g3mWidget(g3mWidget),
  _locationManager(locationManager),
  _motionManager(motionManager)
  {
  }

  void run(const G3MContext* context) {
    CLHeading*      heading  = [_locationManager heading];
    CLLocation*     location = [_locationManager location];
    CMDeviceMotion* motion   = [_motionManager deviceMotion];

    CLLocationDirection trueHeading = [heading trueHeading];

    CMAttitude* attitude = [motion attitude];
    double roll  = [attitude roll];
    double pitch = [attitude pitch];
//    double yaw   = [attitude yaw];


    UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;
    double headingInDegrees;
    double pitchRadians;
    if (orientation == UIInterfaceOrientationLandscapeLeft) {
      headingInDegrees = -trueHeading + 90;
      pitchRadians = (2*PI - roll) - PI/2;
    }
    else if (orientation == UIInterfaceOrientationLandscapeRight) {
      headingInDegrees = -trueHeading - 90;
      pitchRadians = roll - PI/2;
    }
    else if (orientation == UIInterfaceOrientationPortraitUpsideDown) {
      headingInDegrees = -trueHeading - 180;
      pitchRadians = (2*PI -pitch) - PI/2;
    }
    else {
      headingInDegrees = -trueHeading;
      pitchRadians = pitch - PI/2;
    }

    CLLocationCoordinate2D coordinate = [location coordinate];
    CLLocationDistance altitude = [location altitude];


    Camera* camera = _g3mWidget->getNextCamera();

//    //    camera->setRoll( Angle::fromRadians(rollRadians) );

    camera->setHeading( Angle::fromDegrees( headingInDegrees ) );

    camera->setPitch( Angle::fromRadians( pitchRadians ) );

    camera->setGeodeticPosition( Geodetic3D::fromDegrees(coordinate.latitude,
                                                         coordinate.longitude,
                                                         altitude + 500) );
  }
};


void G3MAugmentedRealityDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();


  BingMapsLayer* layer = new BingMapsLayer(BingMapType::AerialWithLabels(),
                                           "AnU5uta7s5ql_HTrRZcPLI4_zotvNefEeSxIClF1Jf7eS-mLig1jluUdCoecV7jc",
                                           TimeInterval::fromDays(30));
//  MapQuestLayer* layer = MapQuestLayer::newOpenAerial(TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);


  _locationManager = [[CLLocationManager alloc] init];
  _locationManager.desiredAccuracy = kCLLocationAccuracyBestForNavigation;
  _locationManager.distanceFilter = kCLDistanceFilterNone;
  _locationManager.headingFilter = 0.001;
  [_locationManager requestAlwaysAuthorization];

  [_locationManager startUpdatingHeading];
  [_locationManager startUpdatingLocation];


  _motionManager = [[CMMotionManager alloc] init];
  _motionManager.showsDeviceMovementDisplay = YES;
  _motionManager.deviceMotionUpdateInterval = 10.0 / 1000.0; // 10ms
  [_motionManager startDeviceMotionUpdatesUsingReferenceFrame:CMAttitudeReferenceFrameXTrueNorthZVertical];


  g3mWidget->addPeriodicalTask(new PeriodicalTask(TimeInterval::fromMilliseconds(10),
                                                  new UpdateCameraTask(g3mWidget,
                                                                       _locationManager,
                                                                       _motionManager)));
  
}
