//
//  G3MWeatherDownloadListener.mm
//  G3MApp
//
//  Created by Mari Luz Mateo on 23/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import "G3MWeatherDownloadListener.hpp"

#import <G3MiOSSDK/G3MWidget_iOS.h>
#import <G3MiOSSDK/JSONBaseObject.hpp>
#import <G3MiOSSDK/IJSONParser.hpp>
#import <G3MiOSSDK/JSONObject.hpp>
#import <G3MiOSSDK/JSONArray.hpp>
#import <G3MiOSSDK/JSONString.hpp>
#import <G3MiOSSDK/JSONNumber.hpp>
#import <G3MiOSSDK/MarksRenderer.hpp>
#import <G3MiOSSDK/Mark.hpp>
#import <G3MiOSSDK/IStringBuilder.hpp>
#import "G3MAppUserData.hpp"
#import "G3MMarkUserData.hpp"
#import "G3MAppInitializationTask.hpp"

G3MWeatherDownloadListener::G3MWeatherDownloadListener(GInitializationTask* initTask,
                                                       G3MWidget_iOS* widget) {
  _initTask = initTask;
  _widget = widget;
}

void G3MWeatherDownloadListener::onDownload(const URL& url,
                                            IByteBuffer* buffer, bool expired) {
  MarksRenderer* marksRenderer = ((G3MAppUserData*) [_widget userData])->getMarksRenderer();
  const JSONBaseObject* json = IJSONParser::instance()->parse(buffer->getAsString());
  const JSONArray* marks = json->asObject()->getAsArray("list");

  for (int i = 0; i < marks->size(); i++) {
    const JSONObject* city = marks->getAsObject(i);
    const JSONObject* coords = city->getAsObject("coord");
    Geodetic2D position2D = Geodetic2D(Angle::fromDegrees(coords->getAsNumber("lat")->value()), //
                                       Angle::fromDegrees(coords->getAsNumber("lon")->value()));
    const JSONObject* weatherObj = city->getAsArray("weather")->getAsObject(0);
    IStringBuilder* iconISB = IStringBuilder::newStringBuilder();
    IStringBuilder* iconPathISB = IStringBuilder::newStringBuilder();
    iconPathISB->addString("http://openweathermap.org/img/w/");
    if (weatherObj->getAsString("icon", "DOUBLE") == "DOUBLE") {
      iconISB->addDouble(weatherObj->getAsNumber("icon")->value());
      iconISB->addString("d.png");
      if (iconISB->getString().length() < 7) {
        iconPathISB->addInt(0);
      }
    }
    else {
      iconISB->addString(weatherObj->getAsString("icon", "DOUBLE"));
      iconISB->addString(".png");
    }
    iconPathISB->addString(iconISB->getString());

    Mark* marker = new Mark(city->getAsString("name", ""),
                            URL(iconPathISB->getString(), false),
                            Geodetic3D(position2D, 0),
                            ABSOLUTE,
                            0.0,
                            true,
                            14.0);
    MarkUserData* mud = new G3MMarkUserData(city->getAsString("name", ""));
    marker->setUserData(mud);
    marksRenderer->addMark(marker);

    delete iconISB;
    delete iconPathISB;
  }
  ((G3MAppInitializationTask*) _initTask)->setWeatherMarkersParsed(true);
  delete buffer;
  IJSONParser::instance()->deleteJSONData(json);
}

void G3MWeatherDownloadListener::onError(const URL& url) {
  NSString* message = [NSString stringWithFormat: @"Oops!\nThere was a problem getting weather markers info"];

  UIAlertView *alert = [[UIAlertView alloc] initWithTitle: @"glob3 mobile"
                                                  message: message
                                                 delegate: nil
                                        cancelButtonTitle: @"OK"
                                        otherButtonTitles: nil];
  [alert show];
}
