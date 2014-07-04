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
#include <list>
#include "TileTessellator.hpp"
#include "Sector.hpp"

#include <vector>

class TileTexturizer;
class Mesh;
class TileElevationDataRequest;
class BoundingVolume;
class Vector3D;
class TilesRenderParameters;
class LayerTilesRenderParameters;
class Frustum;
class TilesStatistics;
class ElevationDataProvider;
class ITimer;
class GLState;
class TileRasterizer;
class LayerSet;
class ITexturizerData;
class PlanetTileTessellatorData;
class PlanetRenderer;
class TileRenderingListener;
class TileKey;
class Geodetic3D;

class TileCache{
  int _maxSize;
  
  
  std::vector<Tile*> _tileCache;
  
public:
  
  TileCache(int maxSize): _maxSize(maxSize){}
  
  void setTileCacheSize(int size);
  void cropTileCache(int size);
  void clearTile(Tile* tile);
  Tile* getSubTileFromCache(int level, int row, int column);
  
  bool has4SubTilesCached(const Tile* tile);
};

class Tile {
private:
  TileTexturizer* _texturizer;
  Tile*           _parent;

  Mesh* _tessellatorMesh;

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

  Vector3D* _northWestPoint;
  Vector3D* _northEastPoint;
  Vector3D* _southWestPoint;
  Vector3D* _southEastPoint;

  static double getSquaredArcSegmentRatio(const Vector3D& a,
                                          const Vector3D& b);

  void computeTileCorners(const Planet* planet);

  double _northArcSegmentRatioSquared;
  double _southArcSegmentRatioSquared;
  double _eastArcSegmentRatioSquared;
  double _westArcSegmentRatioSquared;


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
                        const Planet* planet,
                        const Vector3D& cameraNormalizedPosition,
                        double cameraAngle2HorizonInRadians,
                        const Frustum* cameraFrustumInModelCoordinates,
                        const Frustum* cameraWiderFrustumInModelCoordinates,
                        ElevationDataProvider* elevationDataProvider,
                        const Sector* renderedSector,
                        const TileTessellator* tessellator,
                        const LayerTilesRenderParameters* layerTilesRenderParameters,
                        const TilesRenderParameters* tilesRenderParameters);

  bool _lastMeetsRenderCriteriaResult;
  double _lastMeetsRenderCriteriaTimeInMS;

  inline bool meetsRenderCriteria(const G3MRenderContext* rc,
                                  const LayerTilesRenderParameters* layerTilesRenderParameters,
                                  TileTexturizer* texturizer,
                                  const TilesRenderParameters* tilesRenderParameters,
                                  const TilesStatistics* tilesStatistics,
                                  const ITimer* lastSplitTimer,
                                  double texWidthSquared,
                                  double texHeightSquared,
                                  double nowInMS);

  inline void rawRender(const G3MRenderContext* rc,
                        const GLState* glState,
                        TileTexturizer* texturizer,
                        ElevationDataProvider* elevationDataProvider,
                        const TileTessellator* tessellator,
                        TileRasterizer* tileRasterizer,
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


  inline std::vector<Tile*>* getSubTiles(const Angle& splitLatitude,
                                         const Angle& splitLongitude);

  Tile(const Tile& that);

  void ancestorTexturedSolvedChanged(Tile* ancestor,
                                     bool textureSolved);

  bool _isVisible;
  void setIsVisible(bool isVisible,
                    TileTexturizer* texturizer);


  ITexturizerData* _texturizerData;
  PlanetTileTessellatorData* _tessellatorData;

  int                    _elevationDataLevel;
  ElevationData*         _elevationData;
  bool                   _mustActualizeMeshDueToNewElevationData;
  ElevationDataProvider* _lastElevationDataProvider;
  int _lastTileMeshResolutionX;
  int _lastTileMeshResolutionY;

  const PlanetRenderer* _planetRenderer;

  const BoundingVolume* getBoundingVolume(const G3MRenderContext* rc,
                                          ElevationDataProvider* elevationDataProvider,
                                          const TileTessellator* tessellator,
                                          const LayerTilesRenderParameters* layerTilesRenderParameters,
                                          const TilesRenderParameters* tilesRenderParameters);

  void setRendered(const bool rendered, TileRenderingListener* tileRenderingListener);

  bool _rendered;
  TileRenderingListener* _tileRenderingListener;

  static std::string createTileId(int level,
                                  int row,
                                  int column);
  
  TileCache* _tileCache;
  bool _deleteTextureWhenNotVisible;
  
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
       const PlanetRenderer* planetRenderer,
       TileCache* tileCache,
       bool deleteTextureWhenNotVisible);

  ~Tile();

  //Change to public for TileCache
  //std::vector<Tile*>* getSubTiles(const bool mercator);

//  const Sector getSector() const {
  //    return _sector;
  //  }
  //
  //  int getLevel() const {
  //    return _level;
  //  }
  //
  //  int getRow() const {
  //    return _row;
  //  }
  //
  //  int getColumn() const {
  //    return _column;
  //  }
  
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
                               TileRasterizer* tileRasterizer,
                               const LayerTilesRenderParameters* layerTilesRenderParameters,
                               const LayerSet* layerSet,
                               const TilesRenderParameters* tilesRenderParameters,
                               bool forceFullRender,
                               long long tileDownloadPriority,
                               float verticalExaggeration,
                               bool logTilesPetitions);

  //RETURN ISRAWRENDER
  bool render(const G3MRenderContext* rc,
              const GLState& parentState,
              std::list<Tile*>* toVisitInNextIteration,
              const Planet* planet,
              const Vector3D& cameraNormalizedPosition,
              double cameraAngle2HorizonInRadians,
              const Frustum* cameraFrustumInModelCoordinates,
              const Frustum* cameraWiderFrustumInModelCoordinates,
              TilesStatistics* tilesStatistics,
              const float verticalExaggeration,
              const LayerTilesRenderParameters* layerTilesRenderParameters,
              TileTexturizer* texturizer,
              const TilesRenderParameters* tilesRenderParameters,
              ITimer* lastSplitTimer,
              ElevationDataProvider* elevationDataProvider,
              const TileTessellator* tessellator,
              TileRasterizer* tileRasterizer,
              const LayerSet* layerSet,
              const Sector* renderedSector,
              bool forceFullRender,
              long long tileDownloadPriority,
              double texWidth,
              double texHeight,
              double nowInMS,
              const bool renderTileMeshes,
              bool logTilesPetitions,
              TileRenderingListener* tileRenderingListener);

void actualizeQuadTree(const G3MRenderContext* rc,
                         std::list<Tile*>& renderedTiles,
                         const Planet* planet,
                         const Vector3D& cameraNormalizedPosition,
                         double cameraAngle2HorizonInRadians,
                       const Frustum* cameraFrustumInModelCoordinates,
                       const Frustum* cameraWiderFrustumInModelCoordinates,
                         TilesStatistics* tilesStatistics,
                         const float verticalExaggeration,
                         const LayerTilesRenderParameters* layerTilesRenderParameters,
                         TileTexturizer* texturizer,
                         const TilesRenderParameters* tilesRenderParameters,
                         ITimer* lastSplitTimer,
                         ElevationDataProvider* elevationDataProvider,
                         const TileTessellator* tessellator,
                         TileRasterizer* tileRasterizer,
                         const LayerSet* layerSet,
                         const Sector* renderedSector,
                         bool isForcedFullRender,
                         long long texturePriority,
                         double texWidthSquared,
                         double texHeightSquared,
                         double nowInMS);


  void performRawRender(const G3MRenderContext* rc,
                        const GLState* glState,
                        TileTexturizer* texturizer,
                        ElevationDataProvider* elevationDataProvider,
                        const TileTessellator* tessellator,
                        TileRasterizer* tileRasterizer,
                        const LayerTilesRenderParameters* layerTilesRenderParameters,
                        const LayerSet* layerSet,
                        const TilesRenderParameters* tilesRenderParameters,
                        bool isForcedFullRender,
                        long long texturePriority,
                        TilesStatistics* tilesStatistics,
                        bool logTilesPetitions);

  void zRender(const G3MRenderContext* rc,
               const GLState& parentState);

  //const TileKey getKey() const;

//  const TileKey getKey() const;
//  const std::string getId() const;


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

  void prune(TileTexturizer*        texturizer,
             ElevationDataProvider* elevationDataProvider);

  void toBeDeleted(TileTexturizer*        texturizer,
                   ElevationDataProvider* elevationDataProvider);

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  inline std::vector<Tile*>* createSubTiles(const Angle& splitLatitude,
                                            const Angle& splitLongitude,
                                            bool setParent);

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

  TileTexturizer* getTexturizer() const{
    return _texturizer;
  }

  void deleteTexturizedMesh(TileTexturizer* texturizer);
  
  Vector2I getLastTileMeshResolution() const {
    return Vector2I(_lastTileMeshResolutionX, _lastTileMeshResolutionY);
  }
};

#endif
