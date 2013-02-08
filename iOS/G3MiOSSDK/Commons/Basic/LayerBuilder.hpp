//
//  LayerBuilder.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 21/12/12.
//
//

#ifndef __G3MiOSSDK__LayerBuilder__
#define __G3MiOSSDK__LayerBuilder__

#include <iostream>

#include "WMSLayer.hpp"

class LayerBuilder {
public:
  static WMSLayer* createBingLayer(bool enabled);
  static WMSLayer* createOSMLayer(bool enabled);
  static WMSLayer* createPNOALayer(bool enabled);
  static WMSLayer* createBlueMarbleLayer(bool enabled);
  static WMSLayer* createI3LandSatLayer(bool enabled);
  static WMSLayer* createPoliticalLayer(bool enabled);
  static WMSLayer* createCaceresStreetMapLayer(bool enabled);
  static WMSLayer* createCanaryIslandStreetMapLayer(bool enabled);
};

#endif
