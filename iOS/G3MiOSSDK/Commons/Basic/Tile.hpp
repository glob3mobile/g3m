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
class TileTessellator;
class TileTexturizer;
class TileParameters;
class TilesCache;


class Tile {
private:
  const Sector _sector;
  const int    _level;
  const int    _row;
  const int    _column;

  Mesh* _mesh;
  Tile* _fallbackTextureTile;
  bool _textureSolved;
  
  inline Mesh* getMesh(const RenderContext* rc,
                       const TileTessellator* tessellator);
  
  inline bool isVisible(const RenderContext* rc);
  inline bool meetsRenderCriteria(const RenderContext* rc,
                                  const TileParameters* parameters);
  
  inline std::vector<Tile*> createSubTiles(TilesCache* tilesCache);
  
  inline void rawRender(const RenderContext* rc,
                        const TileTessellator* tessellator,
                        const TileTexturizer* texturizer);
  
  inline Tile* createSubTile(TilesCache* tilesCache,
                             const Angle& lowerLat, const Angle& lowerLon,
                             const Angle& upperLat, const Angle& upperLon,
                             const int level,
                             const int row, const int column,
                             Tile* fallbackTextureTile);
  
public:
  Tile(const Sector& sector,
       int level,
       int row,
       int column,
       Tile* fallbackTextureTile):
  _sector(sector),
  _level(level),
  _row(row),
  _column(column),
  _mesh(NULL),
  _textureSolved(false),
  _fallbackTextureTile(fallbackTextureTile)
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
              const TileParameters* parameters,
              TilesCache* tilesCache);
  
};

#endif
