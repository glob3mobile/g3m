//
//  G3MDemoBuilder.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/14/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MDemoBuilder__
#define __G3MApp__G3MDemoBuilder__

#include <stddef.h>

class G3MBuilder_iOS;
class LayerSet;

class G3MDemoBuilder {
private:
  G3MBuilder_iOS* _builder;

  LayerSet* createLayerSet();

public:
  G3MDemoBuilder(G3MBuilder_iOS* builder) :
  _builder(builder)
  {

  }

  void build();


  ~G3MDemoBuilder();

};

#endif
