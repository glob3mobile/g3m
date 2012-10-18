//
//  SceneParser.h
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 15/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_SceneParser_hpp
#define G3MiOSSDK_SceneParser_hpp


#include "JSONObject.hpp"
#include "ILogger.hpp"
#include "LayerSet.hpp"

const std::string layers = "layers";
const std::string type = "type";
const std::string datasource = "datasource";
const std::string version = "version";
const std::string items = "items";
const std::string status = "status";
const std::string name = "name";

const std::string wms110 = "1.1.0";
const std::string wms111 = "1.1.1";
const std::string wms130 = "1.3.0";

enum layer_type {
  WMS,THREED,PANO
};

class SceneParser{
  static SceneParser* _instance;
  std::map<std::string, layer_type> mapLayerType;
  
public:
  
  static SceneParser* instance();
  void parse(LayerSet* layerSet, std::string namelessParameter);
  void parserJSONLayerList(LayerSet* layerSet, JSONObject* jsonLayers);
  void parserJSONWMSLayer(LayerSet* layerSet, JSONObject* jsonLayer);
  void parserJSON3DLayer(LayerSet* layerSet, JSONObject* jsonLayer);
  void parserJSONPanoLayer(LayerSet* layerSet, JSONObject* jsonLayer);
  
protected:
  SceneParser();
  SceneParser(const SceneParser &);
  SceneParser &operator= (const SceneParser &);
  
};

#endif

