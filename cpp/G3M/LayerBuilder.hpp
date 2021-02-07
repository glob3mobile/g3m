//
//  LayerBuilder.hpp
//  G3M
//
//  Created by Mari Luz Mateo on 21/12/12.
//
//

#ifndef __G3M__LayerBuilder__
#define __G3M__LayerBuilder__

class Layer;
class LayerSet;

class LayerBuilder {
public:
  
  static Layer* createOSMLayer();
  
  static LayerSet* createDefault();
  
};

#endif
