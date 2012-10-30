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

#include "Renderer.hpp"
#include "MarksRenderer.hpp"
#include "IDownloader.hpp"

enum layer_type {
  WMS,THREED,PANO,GEOJSON
};

class SceneParser{
    static const std::string LAYERS;
    static const std::string TYPE;
    static const std::string DATASOURCE;
    static const std::string VERSION;
    static const std::string ITEMS;
    static const std::string STATUS;
    static const std::string NAME;
    static const std::string ICON;
    
    static const std::string WMS110;
    static const std::string WMS111;
    static const std::string WMS130;
    
  static SceneParser* _instance;
  std::map<std::string, layer_type> _mapLayerType;
  
public:
  
  static SceneParser* instance();
  void parse(LayerSet* layerSet, IDownloader* downloader, std::vector<Renderer*>* renderers, std::string namelessParameter);

private:
  void parserJSONLayerList(LayerSet* layerSet, IDownloader* downloader, std::vector<Renderer*>* renderers, JSONObject* jsonLayers);
  void parserJSONWMSLayer(LayerSet* layerSet, JSONObject* jsonLayer);
  void parserJSON3DLayer(LayerSet* layerSet, JSONObject* jsonLayer);
  void parserJSONPanoLayer(LayerSet* layerSet, JSONObject* jsonLayer);
  void parserGEOJSONLayer(LayerSet* layerSet, IDownloader* downloader, std::vector<Renderer*>* renderers, JSONObject* jsonLayer, MarksRenderer* marksRenderer);
  
protected:
  SceneParser();
  SceneParser(const SceneParser &);
  SceneParser &operator= (const SceneParser &);
  
};

#endif

