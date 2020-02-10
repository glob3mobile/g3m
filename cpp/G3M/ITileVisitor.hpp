//
//  ITileVisitor.hpp
//  G3M
//
//  Created by Vidal on 1/22/13.
//
//

#ifndef G3M_ITileVisitor
#define G3M_ITileVisitor

#include <vector>
class Layer;
class Tile;

class ITileVisitor{
public:
#ifdef C_CODE
  virtual ~ITileVisitor() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif
    
  virtual void visitTile(std::vector<Layer*>& layers,
                         const Tile* tile) const = 0;
};

#endif
