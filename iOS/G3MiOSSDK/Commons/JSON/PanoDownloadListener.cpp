//
//  PanoDownloadListener.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 7/12/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "PanoDownloadListener.hpp"

#import "IJSONParser.hpp"
#import "JSONNumber.hpp"
#import "JSONString.hpp"
#import "MarksRenderer.hpp"
#import "JSONObject.hpp"


PanoDownloadListener::PanoDownloadListener(MarksRenderer* panoMarksRenderer,
                                           const std::string& urlIcon) :
_panoMarksRenderer(panoMarksRenderer),
_urlIcon(urlIcon)
{
}

void PanoDownloadListener::onDownload(const URL& url,
                                      IByteBuffer* buffer,
                                      bool expired){
  std::string string = buffer->getAsString();
  const JSONBaseObject* json = IJSONParser::instance()->parse(string);
  ILogger::instance()->logInfo(url._path);
  parseMETADATA(IStringUtils::instance()->substring(url._path, 0, IStringUtils::instance()->indexOf(url._path, "/info.txt")),json->asObject());
  IJSONParser::instance()->deleteJSONData(json);
  delete buffer;
}

void PanoDownloadListener::parseMETADATA(const std::string& url,
                                         const JSONObject* json){
  
  const Angle latitude  = Angle::fromDegrees(json->getAsObject("position")->getAsNumber("lat")->value());
  const Angle longitude = Angle::fromDegrees(json->getAsObject("position")->getAsNumber("lon")->value());
  
  Mark* mark = new Mark(URL(_urlIcon),
                        Geodetic3D(latitude, longitude, 0),
                        RELATIVE_TO_GROUND,
                        0,
                        new PanoMarkUserData(json->getAsString("name")->value(),
                                             new URL(url)),
                        true);
  
  mark->setOnScreenSizeOnProportionToImage(2.0f, 2.0f);
  
  _panoMarksRenderer->addMark(mark);
}
