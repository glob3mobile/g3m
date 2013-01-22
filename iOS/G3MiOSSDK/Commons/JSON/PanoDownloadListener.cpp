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

PanoDownloadListener::PanoDownloadListener(MarksRenderer* marksRenderer, MarkTouchListener* panoTouchListener){
    _marksRenderer = marksRenderer;
    _panoTouchListener = panoTouchListener;
}

void PanoDownloadListener::onDownload(const URL& url,
                                         const IByteBuffer* buffer){
    std::string string = buffer->getAsString();
    JSONBaseObject* json = IJSONParser::instance()->parse(string);
    ILogger::instance()->logInfo(url.getPath());
    parseMETADATA(IStringUtils::instance()->substring(url.getPath(), 0, IStringUtils::instance()->indexOf(url.getPath(), "/info.txt")),json->asObject());
    IJSONParser::instance()->deleteJSONData(json);
}


void PanoDownloadListener::parseMETADATA(std::string url, const JSONObject* json){
    
    const Angle latitude = Angle::fromDegrees(json->getAsObject(POSITION)->getAsNumber(LAT)->doubleValue());
    const Angle longitude = Angle::fromDegrees(json->getAsObject(POSITION)->getAsNumber(LON)->doubleValue());
  
    Mark* mark = new Mark(URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png",false),
                        Geodetic3D(latitude, longitude, 0), 0, new PanoMarkUserData(json->getAsString(NAME)->value(), new URL(url, false)),true, _panoTouchListener,true);
    
    _marksRenderer->addMark(mark);
}
