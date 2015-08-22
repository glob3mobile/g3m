//
//  MapBoo.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/21/15.
//
//

#ifndef __G3MiOSSDK__MapBoo__
#define __G3MiOSSDK__MapBoo__

#include "URL.hpp"

class IG3MBuilder;
class LayerSet;

class MapBoo {
private:
  IG3MBuilder* _builder;
#ifdef C_CODE
  const URL         _serverURL;
#endif
#ifdef JAVA_CODE
  private final URL _serverURL;
#endif

  LayerSet* _layerSet;


public:
  MapBoo(IG3MBuilder* builder,
         const URL& serverURL);

  ~MapBoo();

};

#endif
