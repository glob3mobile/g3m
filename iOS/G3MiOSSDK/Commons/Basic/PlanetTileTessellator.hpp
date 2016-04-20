//
//  PlanetTileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//

#ifndef G3MiOSSDK_PlanetTileTessellator
#define G3MiOSSDK_PlanetTileTessellator

#include "TileTessellator.hpp"
#include <map>
#include "Sector.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"


class IShortBuffer;
class Sector;
class FloatBufferBuilderFromGeodetic;
class ShortBufferBuilder;
class Vector2S;


class PlanetTileTessellatorData {
public:
  FloatBufferBuilderFromCartesian2D* _textCoords;
  PlanetTileTessellatorData(FloatBufferBuilderFromCartesian2D* textCoords):
  _textCoords(textCoords) {}
  
  ~PlanetTileTessellatorData() {
    delete _textCoords;
  }
};


class PlanetTileTessellator : public TileTessellator {
private:
  const bool _skirted;
#ifdef C_CODE
  const Sector* _renderedSector;
#endif
#ifdef JAVA_CODE
  private Sector _renderedSector;
#endif
  
  Vector2S calculateResolution(const PlanetRenderContext* prc,
                               const Tile* tile,
                               const Sector& renderedSector) const;
  
  Geodetic3D getGeodeticOnPlanetSurface(const IMathUtils* mu,
                                        const Planet* planet,
                                        const ElevationData* elevationData,
                                        float verticalExaggeration,
                                        const Geodetic2D& g) const;
  
  bool needsEastSkirt(const Sector& tileSector) const {
    if (_renderedSector == NULL) {
      return true;
    }
    return _renderedSector->_upper._longitude.greaterThan(tileSector._upper._longitude);
  }
  
  bool needsNorthSkirt(const Sector& tileSector) const {
    if (_renderedSector == NULL) {
      return true;
    }
    return _renderedSector->_upper._latitude.greaterThan(tileSector._upper._latitude);
  }
  
  bool needsWestSkirt(const Sector& tileSector) const {
    if (_renderedSector == NULL) {
      return true;
    }
    return _renderedSector->_lower._longitude.lowerThan(tileSector._lower._longitude);
  }
  
  bool needsSouthSkirt(const Sector& tileSector) const {
    if (_renderedSector == NULL) {
      return true;
    }
    return _renderedSector->_lower._latitude.lowerThan(tileSector._lower._latitude);
  }
  
  Sector getRenderedSectorForTile(const Tile* tile) const;
  
  
  double createSurfaceVertices(const Vector2S& meshResolution, //Mesh resolution
                               const Sector& meshSector,
                               const ElevationData* elevationData,
                               float verticalExaggeration,
                               FloatBufferBuilderFromGeodetic* vertices,
                               TileTessellatorMeshData& data) const;
  
  double createSurface(const Sector& tileSector,
                       const Sector& meshSector,
                       const Vector2S& meshResolution,
                       const ElevationData* elevationData,
                       float verticalExaggeration,
                       bool mercator,
                       FloatBufferBuilderFromGeodetic* vertices,
                       ShortBufferBuilder& indices,
                       FloatBufferBuilderFromCartesian2D& textCoords,
                       TileTessellatorMeshData& data) const;
  
  void createEastSkirt(const Planet* planet,
                       const Sector& tileSector,
                       const Sector& meshSector,
                       const Vector2S& meshResolution,
                       double skirtHeight,
                       FloatBufferBuilderFromGeodetic* vertices,
                       ShortBufferBuilder& indices,
                       FloatBufferBuilderFromCartesian2D& textCoords) const;
  
  void createNorthSkirt(const Planet* planet,
                        const Sector& tileSector,
                        const Sector& meshSector,
                        const Vector2S& meshResolution,
                        double skirtHeight,
                        FloatBufferBuilderFromGeodetic* vertices,
                        ShortBufferBuilder& indices,
                        FloatBufferBuilderFromCartesian2D& textCoords) const;
  
  void createWestSkirt(const Planet* planet,
                       const Sector& tileSector,
                       const Sector& meshSector,
                       const Vector2S& meshResolution,
                       double skirtHeight,
                       FloatBufferBuilderFromGeodetic* vertices,
                       ShortBufferBuilder& indices,
                       FloatBufferBuilderFromCartesian2D& textCoords) const;
  
  void createSouthSkirt(const Planet* planet,
                        const Sector& tileSector,
                        const Sector& meshSector,
                        const Vector2S& meshResolution,
                        double skirtHeight,
                        FloatBufferBuilderFromGeodetic* vertices,
                        ShortBufferBuilder& indices,
                        FloatBufferBuilderFromCartesian2D& textCoords) const;
  
  static double skirtDepthForSector(const Planet* planet, const Sector& sector);
  
public:
  
  PlanetTileTessellator(const bool skirted, const Sector& sector);
  
  ~PlanetTileTessellator();
  
  Vector2S getTileMeshResolution(const G3MRenderContext* rc,
                                 const PlanetRenderContext* prc,
                                 const Tile* tile) const;
  
  
  Mesh* createTileMesh(const G3MRenderContext* rc,
                       const PlanetRenderContext* prc,
                       Tile* tile,
                       const ElevationData* elevationData,
                       TileTessellatorMeshData& data) const;
  
  Mesh* createTileDebugMesh(const G3MRenderContext* rc,
                            const PlanetRenderContext* prc,
                            const Tile* tile) const;
  
  IFloatBuffer* createTextCoords(const Vector2S& resolution,
                                 const Tile* tile) const;
  
  const Vector2F getTextCoord(const Tile* tile,
                              const Angle& latitude,
                              const Angle& longitude) const;
  
  void setRenderedSector(const Sector& sector) {
    if (_renderedSector == NULL || !_renderedSector->isEquals(sector)) {
      delete _renderedSector;
      
      if (sector.isEquals(Sector::fullSphere())) {
        _renderedSector = NULL;
      }
      else {
        _renderedSector = new Sector(sector);
      }
    }
  }
  
};

#endif
