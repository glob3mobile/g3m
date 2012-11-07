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






GEOJSONDownloadListener::GEOJSONDownloadListener(MarksRenderer* marksRenderer, std::string icon){
    _marksRenderer = marksRenderer;
    _icon = icon;
}

void GEOJSONDownloadListener::onDownload(const URL& url,
                                         const IByteBuffer* buffer){
    std::string string = buffer->getAsString();
    JSONObject* json = IJSONParser::instance()->parse(string)->getObject();
    ILogger::instance()->logInfo(url.getPath());
    parseGEOJSON(json);
    
}


void GEOJSONDownloadListener::parseGEOJSON(JSONObject* geojson){
    JSONArray* jsonFeatures = geojson->getObjectForKey(FEATURES)->getArray();
    for (int i = 0; i < jsonFeatures->getSize(); i++) {
        JSONObject* jsonFeature = jsonFeatures->getElement(i)->getObject();
        JSONObject* jsonGeometry = jsonFeature->getObjectForKey(GEOMETRY)->getObject();
        std::string jsonType = jsonGeometry->getObjectForKey(TYPE)->getString()->getValue();
        if (jsonType == "Point"){
            parsePointObject(jsonFeature);
        }
    }
}

void GEOJSONDownloadListener::parsePointObject(JSONObject* point){
    JSONObject* jsonProperties = point->getObjectForKey(PROPERTIES)->getObject();
    JSONObject* jsonGeometry = point->getObjectForKey(GEOMETRY)->getObject();
    JSONArray* jsonCoordinates = jsonGeometry->getObjectForKey(COORDINATES)->getArray();
    
    JSONBaseObject* denominaci = jsonProperties->getObjectForKey(DENOMINATION);
    JSONBaseObject* clase = jsonProperties->getObjectForKey(CLASE);
    
    if (denominaci != NULL && clase != NULL){
        ILogger::instance()->logInfo(denominaci->getString()->getValue());
        
        IStringBuilder *iconUrl = IStringBuilder::newStringBuilder();
        iconUrl->addString("http://glob3m.glob3mobile.com/icons/markers/ayto/");
        iconUrl->addString(_icon);
        iconUrl->addString(".png");
        
        IStringBuilder *name = IStringBuilder::newStringBuilder();
        name->addString(clase->getString()->getValue());
        name->addString(" ");
        name->addString(denominaci->getString()->getValue());
        
        std::cout<<name->getString()<<std::endl;
        std::cout<<iconUrl->getString()<<std::endl;
        std::cout<<jsonCoordinates->getElement(1)->getNumber()->getDoubleValue()<<std::endl;
        std::cout<<jsonCoordinates->getElement(0)->getNumber()->getDoubleValue()<<std::endl;
        
        const Angle latitude = Angle::fromDegrees( jsonCoordinates->getElement(1)->getNumber()->getDoubleValue() );
        const Angle longitude = Angle::fromDegrees( jsonCoordinates->getElement(0)->getNumber()->getDoubleValue() );
        
        Mark* mark = new Mark(name->getString(),
                              URL(iconUrl->getString(),false),
                              Geodetic3D(latitude, longitude, 0));
        
        _marksRenderer->addMark(mark);
    }
}