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
#include <list>

class RenderContext;
class Mesh;
class TileTessellator;
class TileTexturizer;
class TilesRenderParameters;
class ITimer;
class TilesStatistics;
class TileRenderContext;
class TileKey;
class Vector3D;

class ITexturizerData {
public:
  virtual ~ITexturizerData() { }
};


class Tile {
private:
  TileTexturizer* _texturizer;
  
  const Sector _sector;
  const int    _level;
  const int    _row;
  const int    _column;
  
  Mesh* _tessellatorMesh;
  Mesh* _debugMesh;
  Mesh* _texturizerMesh;
  
  Tile* _parent;
  bool _textureSolved;
  std::vector<Tile*>* _subtiles;
  
  ITimer* _texturizerTimer;
  
  bool _justCreatedSubtiles;
  bool _texturizerDirty;
  
  inline Mesh* getTessellatorMesh(const RenderContext* rc,
                                  const TileRenderContext* trc);
  
  Mesh* getDebugMesh(const RenderContext* rc,
                     const TileRenderContext* trc);
  
  inline bool isVisible(const RenderContext* rc,
                        const TileRenderContext* trc);
  
  inline bool meetsRenderCriteria(const RenderContext* rc,
                                  const TileRenderContext* trc);
  
  inline std::vector<Tile*>* createSubTiles();
  
  inline void rawRender(const RenderContext* rc,
                        const TileRenderContext* trc);
  
  void debugRender(const RenderContext* rc,
                   const TileRenderContext* trc);
  
  inline Tile* createSubTile(const Angle& lowerLat, const Angle& lowerLon,
                             const Angle& upperLat, const Angle& upperLon,
                             const int level,
                             const int row, const int column);
  
  
  inline std::vector<Tile*>* getSubTiles();
  
  inline void prune(const TileRenderContext* trc);
  
  Tile(const Tile& that);
  
  void ancestorTexturedSolvedChanged(Tile* ancestor,
                                     bool textureSolved);

  bool _isVisible;
  void setIsVisible(bool isVisible);
  
  void deleteTexturizerMesh();
  
  ITexturizerData* _texturizerData;

public:
  Tile(TileTexturizer* texturizer,
       Tile* parent,
       const Sector& sector,
       int level,
       int row,
       int column):
  _texturizer(texturizer),
  _parent(parent),
  _sector(sector),
  _level(level),
  _row(row),
  _column(column),
  _tessellatorMesh(NULL),
  _debugMesh(NULL),
  _texturizerMesh(NULL),
  _textureSolved(false),
  _texturizerDirty(true),
  _subtiles(NULL),
  _justCreatedSubtiles(false),
  _texturizerTimer(NULL),
  _isVisible(false),
  _texturizerData(NULL)
  {
  }
  
  ~Tile();
  
  
  const Sector getSector() const {
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
  
  Mesh* getTexturizerMesh() const {
    return _texturizerMesh;
  }
  
  Tile* getParent() const {
    return _parent;
  }
  
  void render(const RenderContext* rc,
              const TileRenderContext* trc,
              std::list<Tile*>* toVisitInNextIteration);
  
  const TileKey getKey() const;

  void setTextureSolved(bool textureSolved);
  
  bool isTextureSolved() const {
    return _textureSolved;
  }

  void setTexturizerDirty(bool texturizerDirty) {
    _texturizerDirty = texturizerDirty;
  }
  
  bool isTexturizerDirty() const {
    return _texturizerDirty;
  }
  
  Geodetic3D intersection(const Vector3D& origin,
                          const Vector3D& ray,
                          const EventContext* ec) const;

  
  bool hasTexturizerData() const {
    return (_texturizerData != NULL);
  }
  
  ITexturizerData* getTexturizerData() const {
    return _texturizerData;
  }
  
  void setTexturizerData(ITexturizerData* texturizerData) {
    if (_texturizerData != NULL) {
      delete _texturizerData;
    }
    _texturizerData = texturizerData;
  }
  
};

#endif
