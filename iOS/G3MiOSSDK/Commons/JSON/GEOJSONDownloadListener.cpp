//
//  SceneParserDownloadListener.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 26/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "GEOJSONDownloadListener.hpp"
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


const std::string GEOJSONDownloadListener::FEATURES = "features";
const std::string GEOJSONDownloadListener::GEOMETRY = "geometry";
const std::string GEOJSONDownloadListener::TYPE = "type";
const std::string GEOJSONDownloadListener::COORDINATES = "coordinates";
const std::string GEOJSONDownloadListener::PROPERTIES = "properties";
const std::string GEOJSONDownloadListener::DENOMINATION = "DENOMINACI";
const std::string GEOJSONDownloadListener::CLASE = "CLASE";
const std::string GEOJSONDownloadListener::URLICON = "URLICON";
const std::string GEOJSONDownloadListener::URLWEB = "URL";




GEOJSONDownloadListener::GEOJSONDownloadListener(MarksRenderer* marksRenderer, std::string icon){
    _marksRenderer = marksRenderer;
    _icon = icon;
}

void GEOJSONDownloadListener::onDownload(const URL& url,
                                         const IByteBuffer* buffer){
    std::string string = buffer->getAsString();
    JSONObject* json = IJSONParser::instance()->parse(string)->asObject();
    ILogger::instance()->logInfo(url.getPath());
    parseGEOJSON(json);
    
}


void GEOJSONDownloadListener::parseGEOJSON(JSONObject* geojson){
    JSONArray* jsonFeatures = geojson->get(FEATURES)->asArray();
    for (int i = 0; i < jsonFeatures->size(); i++) {
        JSONObject* jsonFeature = jsonFeatures->getAsObject(i);
        JSONObject* jsonGeometry = jsonFeature->getAsObject(GEOMETRY);
        std::string jsonType = jsonGeometry->getAsString(TYPE)->value();
        if (jsonType == "Point"){
            parsePointObject(jsonFeature);
        }
    }
}

void GEOJSONDownloadListener::parsePointObject(JSONObject* point){
    JSONObject* jsonProperties = point->getAsObject(PROPERTIES);
    JSONObject* jsonGeometry = point->getAsObject(GEOMETRY);
    JSONArray* jsonCoordinates = jsonGeometry->getAsArray(COORDINATES);
    
    const Angle latitude = Angle::fromDegrees( jsonCoordinates->getAsNumber(1)->doubleValue() );
    const Angle longitude = Angle::fromDegrees( jsonCoordinates->getAsNumber(0)->doubleValue() );
    
    JSONBaseObject* denominaci = jsonProperties->get(DENOMINATION);
    JSONBaseObject* clase = jsonProperties->get(CLASE);
    
    Mark* mark;
    
    if (denominaci != NULL && clase != NULL){
                
        IStringBuilder *name = IStringBuilder::newStringBuilder();
        name->addString(IStringUtils::instance()->capitalize(clase->asString()->value()));
        name->addString(" ");
        name->addString(denominaci->asString()->value());
               
        if (_icon.length() > 0) {
            mark = new Mark(name->getString(),
                                  URL(_icon,false),
                            Geodetic3D(latitude, longitude, 0),jsonProperties->getAsString(URLWEB),10000);
        } else {
            mark = new Mark(name->getString(),
                                  URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png",false),
                                  Geodetic3D(latitude, longitude, 0),jsonProperties->getAsString(URLWEB),10000);
        }
    } else {
        mark = new Mark("Unknown POI",
                        URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png",false),
                        Geodetic3D(latitude, longitude, 0),NULL,10000);
    }
    _marksRenderer->addMark(mark);
}