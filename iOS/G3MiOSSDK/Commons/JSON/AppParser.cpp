//
//  AppParser.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 18/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "AppParser.hpp"

#include "IJSONParser.hpp"

#include "JSONBaseObject.hpp"
#include "JSONNumber.hpp"
#include "JSONObject.hpp"
#include "JSONArray.hpp"
#include "JSONString.hpp"
#include "JSONBoolean.hpp"

#include "LayerSet.hpp"
#include "WMSLayer.hpp"

#include "Mark.hpp"

const std::string AppParser::WORLD = "_world";
const std::string AppParser::BASELAYER = "_baselayer";
const std::string AppParser::BBOX = "_bbox";
const std::string AppParser::CUSTOMDATA = "_customdata";
const std::string AppParser::FEATURES = "features";
const std::string AppParser::GEOMETRY = "geometry";
const std::string AppParser::TYPE = "type";
const std::string AppParser::COORDINATES = "coordinates";
const std::string AppParser::PROPERTIES = "properties";
const std::string AppParser::NAME = "name";

AppParser* AppParser::_instance = NULL;

AppParser* AppParser::instance(){
  if (_instance == NULL){
    _instance = new AppParser();
  }
  return _instance;
}

AppParser::AppParser(){
}

void AppParser::parse(LayerSet* layerSet, MarksRenderer* marks, std::string namelessParameter){
  JSONBaseObject* json = IJSONParser::instance()->parse(namelessParameter);
  parseWorldConfiguration(layerSet, marks, json->asObject()->getAsObject(WORLD));
  IJSONParser::instance()->deleteJSONData(json);
}

void AppParser::parseWorldConfiguration(LayerSet* layerSet, MarksRenderer* marks, const JSONObject* jsonWorld){
  std::string jsonBaseLayer = jsonWorld->getAsString(BASELAYER)->value();
  const JSONArray* jsonBbox = jsonWorld->getAsArray(BBOX);
  
  if (jsonBaseLayer == "BING"){
    WMSLayer* bing = new WMSLayer("ve",
                                  URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?",true),
                                  WMS_1_1_0,
                                  Sector::fromDegrees(jsonBbox->getAsNumber(1)->doubleValue(), jsonBbox->getAsNumber(0)->doubleValue(), jsonBbox->getAsNumber(3)->doubleValue(), jsonBbox->getAsNumber(2)->doubleValue()),
                                  "image/jpeg",
                                  "EPSG:4326",
                                  "",
                                  false,
                                  NULL, TimeInterval::fromDays(30));
    layerSet->addLayer(bing);
  }else{
    WMSLayer* osm = new WMSLayer("osm",
                                 URL("http://wms.latlon.org/",true),
                                 WMS_1_1_0,
                                 Sector::fromDegrees(jsonBbox->getAsNumber(1)->doubleValue(), jsonBbox->getAsNumber(0)->doubleValue(), jsonBbox->getAsNumber(3)->doubleValue(), jsonBbox->getAsNumber(2)->doubleValue()),                                 
                                 "image/jpeg",
                                 "EPSG:4326",
                                 "",
                                 false,
                                 NULL, TimeInterval::fromDays(30));
    layerSet->addLayer(osm);
  }
  parseCustomData(marks, jsonWorld->getAsObject(CUSTOMDATA));
}

void AppParser::parseCustomData(MarksRenderer* marks, const JSONObject* jsonCustomData){
  const JSONArray* jsonFeatures = jsonCustomData->getAsArray(FEATURES);
  for (int i = 0; i < jsonFeatures->size(); i++) {
    const JSONObject* jsonFeature = jsonFeatures->getAsObject(i);
    const JSONObject* jsonGeometry = jsonFeature->getAsObject(GEOMETRY);
    std::string jsonType = jsonGeometry->getAsString(TYPE)->value();
    if (jsonType == "Point"){
      parseGEOJSONPointObject(marks, jsonFeature);
    }
  }
}
  
void AppParser::parseGEOJSONPointObject(MarksRenderer* marks, const JSONObject* point){
    const JSONObject* jsonProperties = point->getAsObject(PROPERTIES);
    const JSONObject* jsonGeometry = point->getAsObject(GEOMETRY);
    const JSONArray* jsonCoordinates = jsonGeometry->getAsArray(COORDINATES);
    
    Mark* mark = new Mark(jsonProperties->getAsString(NAME)->value(),
                        URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png",false),
                          Geodetic3D(Angle::fromDegrees(jsonCoordinates->getAsNumber(1)->doubleValue()), Angle::fromDegrees(jsonCoordinates->getAsNumber(0)->doubleValue()), 0), NULL, 0, NULL);
    
    marks->addMark(mark);
}