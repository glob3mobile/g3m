//
//  LayerCondition.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#ifndef __G3MiOSSDK__LayerCondition__
#define __G3MiOSSDK__LayerCondition__

class Tile;
class G3MRenderContext;
class G3MEventContext;

#include "Disposable.hpp"

class LayerCondition : public Disposable {
public:
  virtual ~LayerCondition() {
    JAVA_POST_DISPOSE
  }

  virtual bool isAvailable(const G3MRenderContext* rc,
                           const Tile* tile) const = 0;
  
  virtual bool isAvailable(const G3MEventContext* ec,
                           const Tile* tile) const = 0;

};

#endif
