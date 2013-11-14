//
//  LevelTileCondition.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#ifndef __G3MiOSSDK__LevelTileCondition__
#define __G3MiOSSDK__LevelTileCondition__

#include "LayerCondition.hpp"

class LevelTileCondition : public LayerCondition {
private:
  const int _minLevel;
  const int _maxLevel;
  
public:
  LevelTileCondition(int minLevel,
                     int maxLevel) :
  _minLevel(minLevel),
  _maxLevel(maxLevel)
  {
  }

  virtual ~LevelTileCondition() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }
  
  bool isAvailable(const G3MRenderContext* rc,
                   const Tile* tile) const;
  
  bool isAvailable(const G3MEventContext* ec,
                   const Tile* tile) const;

  LayerCondition* copy() const;

};

#endif
