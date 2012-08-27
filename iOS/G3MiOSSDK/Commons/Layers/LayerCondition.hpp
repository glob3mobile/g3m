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
class RenderContext;
class EventContext;

class LayerCondition {
public:
  virtual ~LayerCondition() {
  }

  virtual bool isAvailable(const RenderContext* rc,
                           const Tile* tile) const = 0;
  
  virtual bool isAvailable(const EventContext* ec,
                           const Tile* tile) const = 0;

};

#endif
