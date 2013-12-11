//
//  AndTileCondition.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/5/13.
//
//

#ifndef __G3MiOSSDK__AndTileCondition__
#define __G3MiOSSDK__AndTileCondition__

#include "LayerCondition.hpp"

#include <vector>

class AndTileCondition : public LayerCondition {
private:
  std::vector<LayerCondition*> _children;

  AndTileCondition(std::vector<LayerCondition*> children) :
  _children(children)
  {
  }

public:

  AndTileCondition(LayerCondition* child1,
                   LayerCondition* child2) {
    _children.push_back(child1);
    _children.push_back(child2);
  }

  ~AndTileCondition();

  bool isAvailable(const G3MRenderContext* rc,
                   const Tile* tile) const;

  bool isAvailable(const G3MEventContext* ec,
                   const Tile* tile) const;

  LayerCondition* copy() const;
  
};

#endif
