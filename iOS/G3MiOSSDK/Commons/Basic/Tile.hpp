//
//  Tile.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//

#ifndef G3MiOSSDK_Tile
#define G3MiOSSDK_Tile

#include <vector>
#include "TileTessellator.hpp"
#include "Sector.hpp"
#include "FrameTask.hpp"

class TileTexturizer;
class TileElevationDataRequest;
class GLState;
class ITexturizerData;
class PlanetTileTessellatorData;
class ElevationDataProvider;
class PlanetRenderer;
class TileData;
class TilesStatistics;


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

  TileTessellatorMeshData _tileTessellatorMeshData;

  void prepareTestLODData(const Planet* planet);

  Mesh* getDebugMesh(const G3MRenderContext* rc,
                     const PlanetRenderContext* prc);

  inline void rawRender(const G3MRenderContext*    rc,
                        const PlanetRenderContext* prc,
                        const GLState*             glState);

  void debugRender(const G3MRenderContext*    rc,
                   const PlanetRenderContext* prc,
                   const GLState*             glState);

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

  static std::string createTileId(int level,
                                  int row,
                                  int column);

  mutable TileData** _data;
  mutable size_t     _dataSize;
    
  class TessellatorTask : public FrameTask {
  private:
    Tile *_tile;
    const PlanetRenderContext *_prc;
        
    bool &_mustActualizeMeshDueToNewElevationData;
    const PlanetRenderer *_planetRenderer;
    Mesh ** _tessellatorMesh;
    Mesh ** _debugMesh;
    TileTessellatorMeshData &_data;
    bool _shouldCancel;
  public:
    TessellatorTask(Tile * tile,
                    const PlanetRenderContext *prc,
                    bool &mustActualize,
                    const PlanetRenderer *planetRenderer,
                    Mesh **tessellatorMesh,
                    Mesh **debugMesh,
                    TileTessellatorMeshData &data):
      _tile(tile),
      _prc(prc),
      _mustActualizeMeshDueToNewElevationData(mustActualize),
      _planetRenderer(planetRenderer),
      _tessellatorMesh(tessellatorMesh),
      _debugMesh(debugMesh),
      _data(data),
      _shouldCancel(false) {
            
      }
        
      ~TessellatorTask(){
        if (_tile != NULL) {
          _tile->_tessellatorTask = NULL;
        }
      }
        
      void cancelTask (){
        _shouldCancel = true;
        _tile = NULL;
      }
        
      bool isCanceled(const G3MRenderContext* rc){
        return _shouldCancel;
      };
        
      void execute(const G3MRenderContext* rc);
  };
    
protected:
  TessellatorTask *_tessellatorTask;
  bool _shouldInitElevData;

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

  void prepareForFullRendering(const G3MRenderContext*    rc,
                               const PlanetRenderContext* prc);

  void render(const G3MRenderContext*    rc,
              const PlanetRenderContext* prc,
              const GLState*             parentState,
              TilesStatistics*           tilesStatistics,
              std::vector<Tile*>*        toVisitInNextIteration);

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

  std::vector<Tile*>* createSubTiles(bool setParent);

  bool isElevationDataSolved() const {
    return (_elevationDataLevel == _level);
  }

  ElevationData* getElevationData() const {
    return _elevationData;
  }

  void setElevationData(ElevationData* ed, int level);

  void getElevationDataFromAncestor(const Vector2I& extent);

  void initializeElevationData(const G3MRenderContext* rc,
                               const PlanetRenderContext* prc);

  void ancestorChangedElevationData(Tile* ancestor);

  ElevationData* createElevationDataSubviewFromAncestor(Tile* ancestor) const;

  Vector2I getNormalizedPixelFromPosition(const Geodetic2D& position,
                                          const Vector2I& size) const;

  TileData* getData(int id) const;
  
  void setData(TileData* data) const;
  
  void clearDataWithID(int id) const;

  const TileTessellatorMeshData* getTessellatorMeshData() const;

  Mesh* getTessellatorMesh(const G3MRenderContext* rc,
                           const PlanetRenderContext* prc);

  bool hasSubtiles() const {
    return (_subtiles != NULL);
  }
  
};

#endif
