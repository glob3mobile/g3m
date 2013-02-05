//
//  ITileVisitor.hpp
//  G3MiOSSDK
//
//  Created by Vidal on 1/22/13.
//
//

#ifndef G3MiOSSDK_ITileVisitor_hpp
#define G3MiOSSDK_ITileVisitor_hpp

#include "Tile.hpp"

class ITileVisitor{
public:
    virtual ~ITileVisitor() { }
    
    virtual void visitTile(Tile* tile) const = 0;
};


#endif
