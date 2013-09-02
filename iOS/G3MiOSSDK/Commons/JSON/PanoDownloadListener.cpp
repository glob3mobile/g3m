//
//  PanoDownloadListener.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 7/12/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "PanoDownloadListener.hpp"
#include "MarksRenderer.hpp"
#include "Mark.hpp"

#include "ILogger.hpp"
#include "IStringBuilder.hpp"
#include "IStringUtils.hpp"
#include "IJSONParser.hpp"
#include "JSONObject.hpp"
#include "JSONArray.hpp"
#include "JSONString.hpp"
#include "JSONNumber.hpp"


const std::string PanoDownloadListener::NAME = "name";
const std::string PanoDownloadListener::POSITION = "position";
const std::string PanoDownloadListener::LAT = "lat";
const std::string PanoDownloadListener::LON = "lon";

PanoDownloadListener::PanoDownloadListener(MarksRenderer* marksRenderer, MarkTouchListener* panoTouchListener, std::string urlIcon){
  _marksRenderer = marksRenderer;
  _panoTouchListener = panoTouchListener;
  _urlIcon = urlIcon;
}

void PanoDownloadListener::onDownload(const URL& url,
                                      IByteBuffer* buffer,
                                      bool expired){
  std::string string = buffer->getAsString();
  const JSONBaseObject* json = IJSONParser::instance()->parse(string);
  ILogger::instance()->logInfo(url.getPath());
  parseMETADATA(IStringUtils::instance()->substring(url.getPath(), 0, IStringUtils::instance()->indexOf(url.getPath(), "/info.txt")),json->asObject());
  IJSONParser::instance()->deleteJSONData(json);
  delete buffer;
}


void PanoDownloadListener::parseMETADATA(std::string url, const JSONObject* json){
  
  const Angle latitude = Angle::fromDegrees(json->getAsObject(POSITION)->getAsNumber(LAT)->value());
  const Angle longitude = Angle::fromDegrees(json->getAsObject(POSITION)->getAsNumber(LON)->value());
  
  Mark* mark = new Mark(URL(_urlIcon,false),
                        Geodetic3D(latitude, longitude, 0), RELATIVE_TO_GROUND, 0, new PanoMarkUserData(json->getAsString(NAME)->value(), new URL(url, false)),true, _panoTouchListener,true);
  
  _marksRenderer->addMark(mark);
}
