//
//  Tile.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
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
class PlanetRendererContext;
class TileKey;
class Vector3D;
class GLState;
class BoundingVolume;
class ElevationDataProvider;
class ElevationData;
class MeshHolder;
class Vector2I;
class GPUProgramState;
class TileElevationDataRequest;
class Frustum;
class Box;
class PlanetRenderer;
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
  TileElevationDataRequest* _elevationDataRequest;
  
  Mesh* _flatColorMesh;

  bool _textureSolved;
  std::vector<Tile*>* _subtiles;
  bool _justCreatedSubtiles;

  bool _texturizerDirty;    //Texturizer needs to be called

  float _verticalExaggeration;
  double _minHeight;
  double _maxHeight;

  BoundingVolume* _boundingVolume;

  inline Mesh* getTessellatorMesh(const G3MRenderContext* rc,
                                  const PlanetRendererContext* prc);

  Mesh* getDebugMesh(const G3MRenderContext* rc,
                     const PlanetRendererContext* prc);

  inline bool isVisible(const G3MRenderContext* rc,
                        const PlanetRendererContext* prc,
                        const Planet* planet,
                        const Vector3D& cameraNormalizedPosition,
                        double cameraAngle2HorizonInRadians,
                        const Frustum* cameraFrustumInModelCoordinates);

  ITimer* _lodTimer;
  bool _lastLodTest;
  inline bool meetsRenderCriteria(const G3MRenderContext* rc,
                                  const PlanetRendererContext* prc);

  inline void rawRender(const G3MRenderContext* rc,
                        const PlanetRendererContext* prc,
                        const GLState* glState);

  void debugRender(const G3MRenderContext* rc,
                   const PlanetRendererContext* prc, const GLState* glState);

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

  void deleteTexturizedMesh(TileTexturizer* texturizer);

  ITexturizerData* _texturizerData;

  Box* _tileBoundingVolume;
  Box* getTileBoundingVolume(const G3MRenderContext *rc);

  int                    _elevationDataLevel;
  ElevationData*         _elevationData;
  bool                   _mustActualizeMeshDueToNewElevationData;
  ElevationDataProvider* _lastElevationDataProvider;
  int _lastTileMeshResolutionX;
  int _lastTileMeshResolutionY;

  const PlanetRenderer* _planetRenderer;

  const BoundingVolume* getBoundingVolume(const G3MRenderContext *rc,
                                          const PlanetRendererContext* prc);

public:
  Tile(TileTexturizer* texturizer,
       Tile* parent,
       const Sector& sector,
       int level,
       int row,
       int column,
       const PlanetRenderer* planetRenderer);

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
                               const PlanetRendererContext* prc);

  void render(const G3MRenderContext* rc,
              const PlanetRendererContext* prc,
              const GLState& parentState,
              std::list<Tile*>* toVisitInNextIteration,
              const Planet* planet,
              const Vector3D& cameraNormalizedPosition,
              double cameraAngle2HorizonInRadians,
              const Frustum* cameraFrustumInModelCoordinates);

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
    if (texturizerData != _texturizerData) {
      delete _texturizerData;
      _texturizerData = texturizerData;
    }
  }

  const Tile* getDeepestTileContaining(const Geodetic3D& position) const;

  inline void prune(TileTexturizer*        texturizer,
                    ElevationDataProvider* elevationDataProvider);

  void toBeDeleted(TileTexturizer*        texturizer,
                   ElevationDataProvider* elevationDataProvider);

  
  double getMinHeight() const;
  double getMaxHeight() const;

  const std::string description() const;

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

};

#endif
