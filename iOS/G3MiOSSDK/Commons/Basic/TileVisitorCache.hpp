//
//  TileVisitorCache.h
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 04/12/13.
//
//

#ifndef __G3MiOSSDK__TileVisitorCache__
#define __G3MiOSSDK__TileVisitorCache__

#include <iostream>
#include "ITileVisitor.hpp"

#endif /* defined(__G3MiOSSDK__TileVisitorCache__) */


class TileVisitorCache : public ITileVisitor {
  G3MContext* _context;
  long long _numVisits;
  long long _numPetitions;
  
private:
  
public:
  virtual ~TileVisitorCache(){
    #ifdef JAVA_CODE
    super.dispose();
    #endif
  }
  
  TileVisitorCache(G3MContext*    context):_context(context){
    _numVisits = 0;
    _numPetitions = 0;
  };
  
  void visitTile(std::vector<Layer*>& layers,
                 const Tile* tile);
};