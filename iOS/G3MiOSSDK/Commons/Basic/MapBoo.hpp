//
//  MapBoo.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/21/15.
//
//

#ifndef __G3MiOSSDK__MapBoo__
#define __G3MiOSSDK__MapBoo__

class IG3MBuilder;
class LayerSet;


class MapBoo {
private:
  IG3MBuilder* _builder;

  LayerSet* _layerSet;


public:
  MapBoo(IG3MBuilder* builder);

  ~MapBoo();

};

#endif
