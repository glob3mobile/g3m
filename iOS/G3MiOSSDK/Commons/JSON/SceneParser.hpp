//
//  SceneParser.h
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 15/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_SceneParser_hpp
#define G3MiOSSDK_SceneParser_hpp


#include "JSONObject.hpp"
#include "ILogger.hpp"
#include "LayerSet.hpp"

enum layer_type {
  WMS,THREED,PANO
};

class SceneParser{
    static const std::string LAYERS;
    static const std::string TYPE;
    static const std::string DATASOURCE;
    static const std::string VERSION;
    static const std::string ITEMS;
    static const std::string STATUS;
    static const std::string NAME;
    
    static const std::string WMS110;
    static const std::string WMS111;
    static const std::string WMS130;
    
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

