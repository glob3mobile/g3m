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
//#include "TileTessellator.hpp"
class TileTessellator;
class TileTexturizer;

class Tile {
private:
  const Sector _sector;
  const int    _level;
  const int    _row;
  const int    _column;
  
  bool _textureSolved;
  bool _wireframe;
  
  Mesh* _mesh;
  
  inline Mesh* getMesh(const RenderContext* rc,
                       const TileTessellator* tessellator);
  
  inline bool isVisible(const RenderContext* rc);
  inline bool hasEnoughDetail(const RenderContext* rc);
  
  inline std::vector<Tile*> createSubTiles();
  
  inline void rawRender(const RenderContext* rc,
                        const TileTessellator* tessellator,
                        const TileTexturizer* texturizer);
  
public:
  Tile(const Sector& sector,
       int level,
       int row,
       int column,
       bool wireframe):
  _sector(sector),
  _level(level),
  _row(row),
  _column(column),
  _mesh(NULL),
  _textureSolved(false),
  _wireframe(wireframe)
  {
  }
  
  ~Tile();
  
  
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
  
  
  void setTextureSolved(bool textureSolved) {
    _textureSolved = textureSolved;
  }
  
  bool isTextureSolved() const {
    return _textureSolved;
  }
  
  void render(const RenderContext* rc,
              const TileTessellator* tessellator,
              const TileTexturizer* texturizer,
              double distanceToCamera);
  
};

#endif
