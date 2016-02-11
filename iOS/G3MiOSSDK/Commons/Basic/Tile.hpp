//
//  Tile.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Tile
#define G3MiOSSDK_Tile

#include <vector>
#include "TileTessellator.hpp"
#include "Sector.hpp"
//#include "TileLODTester.hpp"

class TileTexturizer;
class Mesh;
class TileElevationDataRequest;
class Vector3D;
class TilesRenderParameters;
class LayerTilesRenderParameters;
class Frustum;
class TilesStatistics;
class ElevationDataProvider;
class ITimer;
class GLState;
class LayerSet;
class ITexturizerData;
class PlanetTileTessellatorData;
class PlanetRenderer;
class TileKey;
class Geodetic3D;
class TileLODTester;
class TileData;
class TileVisibilityTester;


class Tile {
private:
  TileTexturizer* _texturizer;
  Tile*           _parent;
  
  Mesh* _tessellatorMesh;
  bool _tessellatorMeshIsMeshHolder;

  Mesh* _debugMesh;
  Mesh* _texturizedMesh;
  TileElevationDataRequest* _elevationDataRequest;
  
  Mesh* _flatColorMesh;
  
  bool _textureSolved;
  std::vector<Tile*>* _subtiles;
  bool _justCreatedSubtiles;
  
  bool _texturizerDirty;
  
  float _verticalExaggeration;
  TileTessellatorMeshData _tileTessellatorMeshData;

  void prepareTestLODData(const Planet* planet);
  
  inline Mesh* getTessellatorMesh(const G3MRenderContext* rc,
                                  ElevationDataProvider* elevationDataProvider,
                                  const TileTessellator* tessellator,
                                  const LayerTilesRenderParameters* layerTilesRenderParameters,
                                  const TilesRenderParameters* tilesRenderParameters);
  
  Mesh* getDebugMesh(const G3MRenderContext* rc,
                     const TileTessellator* tessellator,
                     const LayerTilesRenderParameters* layerTilesRenderParameters);
  
  inline bool isVisible(const G3MRenderContext* rc,
                        const Sector* renderedSector,
                        TileVisibilityTester* tileVisibilityTester,
                        long long nowInMS,
                        const Frustum* frustumInModelCoordinates);
  
  inline bool meetsRenderCriteria(const G3MRenderContext* rc,
                                  TileLODTester* tileLODTester,
                                  const TilesRenderParameters* tilesRenderParameters,
                                  const ITimer* lastSplitTimer,
                                  const double texWidthSquared,
                                  const double texHeightSquared,
                                  long long nowInMS);

  inline void rawRender(const G3MRenderContext* rc,
                        const GLState* glState,
                        TileTexturizer* texturizer,
                        ElevationDataProvider* elevationDataProvider,
                        const TileTessellator* tessellator,
                        const LayerTilesRenderParameters* layerTilesRenderParameters,
                        const LayerSet* layerSet,
                        const TilesRenderParameters* tilesRenderParameters,
                        bool forceFullRender,
                        long long tileDownloadPriority,
                        bool logTilesPetitions);
  
  void debugRender(const G3MRenderContext* rc,
                   const GLState* glState,
                   const TileTessellator* tessellator,
                   const LayerTilesRenderParameters* layerTilesRenderParameters);
  
  inline Tile* createSubTile(const Angle& lowerLat, const Angle& lowerLon,
                             const Angle& upperLat, const Angle& upperLon,
                             const int level,
                             const int row, const int column,
                             bool setParent);
  
  Tile(const Tile& that);
  
  void ancestorTexturedSolvedChanged(Tile* ancestor,
                                     bool textureSolved);
  
  bool _isVisible;
  void setIsVisible(bool isVisible,
                    TileTexturizer* texturizer);
  
  void deleteTexturizedMesh(TileTexturizer* texturizer);
  
  ITexturizerData* _texturizerData;
  PlanetTileTessellatorData* _tessellatorData;
  
  int                    _elevationDataLevel;
  ElevationData*         _elevationData;
  bool                   _mustActualizeMeshDueToNewElevationData;
  ElevationDataProvider* _lastElevationDataProvider;
  int _lastTileMeshResolutionX;
  int _lastTileMeshResolutionY;
  
  const PlanetRenderer* _planetRenderer;
  
  bool _rendered;
  
  static std::string createTileId(int level,
                                  int row,
                                  int column);
  
  mutable TileData** _data;
  mutable size_t     _dataSize;

public:
  const Sector      _sector;
  const bool        _mercator;
  const int         _level;
  const int         _row;
  const int         _column;
  const std::string _id;
  
  Tile(TileTexturizer* texturizer,
       Tile* parent,
       const Sector& sector,
       const bool mercator,
       int level,
       int row,
       int column,
       const PlanetRenderer* planetRenderer);
  
  ~Tile();
  
  //Change to public for TileCache
  std::vector<Tile*>* getSubTiles();
  
  Mesh* getTexturizedMesh() const {
    return _texturizedMesh;
  }
  
  Tile* getParent() const {
    return _parent;
  }
  
  void prepareForFullRendering(const G3MRenderContext* rc,
                               TileTexturizer* texturizer,
                               ElevationDataProvider* elevationDataProvider,
                               const TileTessellator* tessellator,
                               const LayerTilesRenderParameters* layerTilesRenderParameters,
                               const LayerSet* layerSet,
                               const TilesRenderParameters* tilesRenderParameters,
                               bool forceFullRender,
                               long long tileDownloadPriority,
                               float verticalExaggeration,
                               bool logTilesPetitions);
  
  void render(const G3MRenderContext* rc,
              const GLState& parentState,
              std::vector<Tile*>* toVisitInNextIteration,
              TileLODTester* tileLODTester,
              TileVisibilityTester* tileVisibilityTester,
              const Frustum* frustumInModelCoordinates,
              TilesStatistics* tilesStatistics,
              const float verticalExaggeration,
              const LayerTilesRenderParameters* layerTilesRenderParameters,
              TileTexturizer* texturizer,
              const TilesRenderParameters* tilesRenderParameters,
              ITimer* lastSplitTimer,
              ElevationDataProvider* elevationDataProvider,
              const TileTessellator* tessellator,
              const LayerSet* layerSet,
              const Sector* renderedSector,
              bool forceFullRender,
              long long tileDownloadPriority,
              double texWidth,
              double texHeight,
              long long nowInMS,
              const bool renderTileMeshes,
              bool logTilesPetitions);
  
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
  
  void setTexturizerData(ITexturizerData* texturizerData);
  
  PlanetTileTessellatorData* getTessellatorData() const {
    return _tessellatorData;
  }
  
  void setTessellatorData(PlanetTileTessellatorData* tessellatorData);
  
  const Tile* getDeepestTileContaining(const Geodetic3D& position) const;
  
  void prune(TileTexturizer*           texturizer,
             ElevationDataProvider*    elevationDataProvider);
  
  void toBeDeleted(TileTexturizer*        texturizer,
                   ElevationDataProvider* elevationDataProvider);
  
  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif
  
  std::vector<Tile*>* createSubTiles(bool setParent);
  
  bool isElevationDataSolved() const {
    return (_elevationDataLevel == _level);
  }
  
  ElevationData* getElevationData() const {
    return _elevationData;
  }
  
  void setElevationData(ElevationData* ed, int level);
  
  void getElevationDataFromAncestor(const Vector2I& extent);
  
  void initializeElevationData(ElevationDataProvider* elevationDataProvider,
                               const TileTessellator* tesselator,
                               const Vector2I& tileMeshResolution,
                               const Planet* planet,
                               bool renderDebug);
  
  void ancestorChangedElevationData(Tile* ancestor);
  
  ElevationData* createElevationDataSubviewFromAncestor(Tile* ancestor) const;
  
  Vector2I getNormalizedPixelsFromPosition(const Geodetic2D& position2D,
                                           const Vector2I& size) const;

  const Mesh* getTessellatorMesh() const;

  TileData* getData(int id) const;
  void setData(int id, TileData* data) const;

  const TileTessellatorMeshData* getTessellatorMeshData() const {
#warning ask JM
    return &_tileTessellatorMeshData;
  }
  
  const Mesh* getCurrentTessellatorMesh() const {
#warning TODO: remove this method
    return _tessellatorMesh;
  }

  bool areSubtilesCreated() const {
    return _subtiles != NULL;
  }
  
};

#endif
