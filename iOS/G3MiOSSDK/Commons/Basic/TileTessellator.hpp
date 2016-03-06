//
//  TileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//

#ifndef G3MiOSSDK_TileTessellator
#define G3MiOSSDK_TileTessellator

class G3MRenderContext;
class Planet;
class Mesh;
class Tile;
class MutableVector2D;
class IFloatBuffer;
class ElevationData;
class Geodetic2D;
class Sector;
class PlanetRenderContext;
class Vector2S;
class Vector2F;
class Angle;

class TileTessellatorMeshData{
public:
  double _minHeight;
  double _maxHeight;
  double _averageHeight;
  double _maxTriangleLatitudeLenght;
  double _maxTriangleLongitudeLenght;
  
  int _meshResLat;
  int _meshResLon;
  
  short _demDistanceToNextLoD;
};


class TileTessellator {
public:
  virtual ~TileTessellator() {
  }

  virtual Mesh* createTileMesh(const G3MRenderContext* rc,
                               const PlanetRenderContext* prc,
                               Tile* tile,
                               const ElevationData* elevationData,
                               TileTessellatorMeshData& data) const = 0;

  virtual Vector2S getTileMeshResolution(const G3MRenderContext* rc,
                                         const PlanetRenderContext* prc,
                                         const Tile* tile) const = 0;

  virtual Mesh* createTileDebugMesh(const G3MRenderContext* rc,
                                    const PlanetRenderContext* prc,
                                    const Tile* tile) const = 0;

  virtual IFloatBuffer* createTextCoords(const Vector2S& resolution,
                                         const Tile* tile) const = 0;

  virtual const Vector2F getTextCoord(const Tile* tile,
                                      const Geodetic2D& position) const;

  virtual const Vector2F getTextCoord(const Tile* tile,
                                      const Angle& latitude,
                                      const Angle& longitude) const = 0;

  virtual void setRenderedSector(const Sector& sector) = 0;

};

#endif
