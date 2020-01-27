//
//  GradualSplitsTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/6/16.
//
//

#ifndef GradualSplitsTileLODTester_hpp
#define GradualSplitsTileLODTester_hpp

#include "DecoratorTileLODTester.hpp"

class TimeInterval;


class GradualSplitsTileLODTester : public DecoratorTileLODTester {
private:
  const long long _delayInMs;

public:

  GradualSplitsTileLODTester(const TimeInterval& delay,
                             TileLODTester* tileLODTester);

  ~GradualSplitsTileLODTester();

  bool meetsRenderCriteria(const G3MRenderContext* rc,
                           const PlanetRenderContext* prc,
                           const Tile* tile) const;

};

#endif
