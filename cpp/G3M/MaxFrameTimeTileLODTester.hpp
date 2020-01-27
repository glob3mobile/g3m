//
//  MaxFrameTimeTileLODTester.hpp
//  G3M
//
//  Created by Jose Miguel SN on 22/12/15.
//
//

#ifndef MaxFrameTimeTileLODTester_hpp
#define MaxFrameTimeTileLODTester_hpp


#include "DecoratorTileLODTester.hpp"

class TimeInterval;


class MaxFrameTimeTileLODTester : public DecoratorTileLODTester {
private:
  const long long _maxFrameTimeInMs;

  mutable int _splitsInFrameCounter;

public:

  MaxFrameTimeTileLODTester(const TimeInterval& maxFrameTime,
                            TileLODTester* tileLODTester);

  ~MaxFrameTimeTileLODTester();
  
  bool meetsRenderCriteria(const G3MRenderContext* rc,
                           const PlanetRenderContext* prc,
                           const Tile* tile) const;

  void renderStarted() const;

};

#endif
