//
//  Tile.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Tile_h
#define G3MiOSSDK_Tile_h

#include "Sector.hpp"

class RenderContext;
class Mesh;
#include "TileTessellator.hpp"

class Tile {
private:
  const Sector _sector;
  const int    _level;
  const int    _row;
  const int    _column;
  
  Mesh* _mesh;
  
  Mesh* getMesh(const RenderContext* rc,
                const TileTessellator* tessellator) {
    if (_mesh == NULL) {
      _mesh = tessellator->createMesh(rc, this);
    }
    return _mesh;
  }
  
public:
  Tile(const Sector& sector,
       int level,
       int row,
       int column):
  _sector(sector),
  _level(level),
  _row(row),
  _column(column),
  _mesh(NULL)
  {
  }
  
  ~Tile();
  
  //  void createVertices(const Planet *planet);
  //  void render(const RenderContext* rc);
  //  
  //  static void createIndices(unsigned int resol, bool skirts);
  //  static void deleteIndices();
  
  Sector getSector() const {
    return _sector;
  }
  
  int getLevel() const {
    return _level;
  }
  
  int getRow() const {
    return _row;
  }
  
  int getColumn() const {
    return _column;
  }
  
  void render(const RenderContext* rc,
              const TileTessellator* tessellator);
  
};

#endif
