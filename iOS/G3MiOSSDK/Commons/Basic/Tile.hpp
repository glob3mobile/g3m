//
//  Tile.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Tile_h
#define G3MiOSSDK_Tile_h

#include "Sector.hpp"
#include <list>

class G3MRenderContext;
class Mesh;
class TileTessellator;
class TileTexturizer;
class TilesRenderParameters;
class ITimer;
class TilesStatistics;
class TileRenderContext;
class TileKey;
class Vector3D;
class GLState;
#include "ITexturizerData.hpp"

class Tile {
private:
  TileTexturizer* _texturizer;
  Tile*           _parent;
  const Sector    _sector;
  const int       _level;
  const int       _row;
  const int       _column;

  Mesh* _tessellatorMesh;
  Mesh* _debugMesh;
  Mesh* _texturizedMesh;

  bool _textureSolved;
  std::vector<Tile*>* _subtiles;
  bool _justCreatedSubtiles;

  bool _texturizerDirty;

  inline Mesh* getTessellatorMesh(const G3MRenderContext* rc,
                                  const TileRenderContext* trc);

  Mesh* getDebugMesh(const G3MRenderContext* rc,
                     const TileRenderContext* trc);

  inline bool isVisible(const G3MRenderContext* rc,
                        const TileRenderContext* trc);

  inline bool meetsRenderCriteria(const G3MRenderContext* rc,
                                  const TileRenderContext* trc);

  inline std::vector<Tile*>* createSubTiles();

  inline void rawRender(const G3MRenderContext* rc,
                        const TileRenderContext* trc,
                        const GLState& parentState);

  void debugRender(const G3MRenderContext* rc,
                   const TileRenderContext* trc,
                   const GLState& parentState);

  inline Tile* createSubTile(const Angle& lowerLat, const Angle& lowerLon,
                             const Angle& upperLat, const Angle& upperLon,
                             const int level,
                             const int row, const int column);


  inline std::vector<Tile*>* getSubTiles();

  Tile(const Tile& that);

  void ancestorTexturedSolvedChanged(Tile* ancestor,
                                     bool textureSolved);

  bool _isVisible;
  void setIsVisible(bool isVisible,
                    TileTexturizer* texturizer);

  void deleteTexturizedMesh(TileTexturizer* texturizer);

  ITexturizerData* _texturizerData;

public:
  Tile(TileTexturizer* texturizer,
       Tile* parent,
       const Sector& sector,
       int level,
       int row,
       int column);

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

  Mesh* getTexturizedMesh() const {
    return _texturizedMesh;
  }

  Tile* getParent() const {
    return _parent;
  }

  void prepareForFullRendering(const G3MRenderContext* rc,
                               const TileRenderContext* trc);

  void render(const G3MRenderContext* rc,
              const TileRenderContext* trc,
              const GLState& parentState,
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

  bool hasTexturizerData() const {
    return (_texturizerData != NULL);
  }

  ITexturizerData* getTexturizerData() const {
    return _texturizerData;
  }

  void setTexturizerData(ITexturizerData* texturizerData) {
    delete _texturizerData;
    _texturizerData = texturizerData;
  }

  const Tile* getDeepestTileContaining(const Geodetic3D& position) const;

  inline void prune(TileTexturizer* texturizer);
  
};

#endif
