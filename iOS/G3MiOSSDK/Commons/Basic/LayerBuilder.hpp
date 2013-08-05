//
//  LayerBuilder.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 21/12/12.
//
//

#ifndef __G3MiOSSDK__LayerBuilder__
#define __G3MiOSSDK__LayerBuilder__

#include "WMSLayer.hpp"
#include <vector>

class LayerBuilder {
public:
  static LayerSet* createDefaultSatelliteImagery();
  static std::vector<std::string> getDefaultLayersNames();
  static WMSLayer* createBingLayer(bool enabled);
  static WMSLayer* createOSMLayer(bool enabled);
  static WMSLayer* createPNOALayer(bool enabled);
  static WMSLayer* createBlueMarbleLayer(bool enabled);
  static WMSLayer* createI3LandSatLayer(bool enabled);
  static WMSLayer* createPoliticalLayer(bool enabled);
};

#endif
