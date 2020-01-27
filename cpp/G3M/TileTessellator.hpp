//
//  TileTessellator.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 27/06/12.
//

#ifndef G3M_TileTessellator
#define G3M_TileTessellator

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
class DEMGrid;


class TileTessellatorMeshData {
public:
  double _minHeight;
  double _maxHeight;
  double _averageHeight;

  TileTessellatorMeshData() :
  _minHeight(0),
  _maxHeight(0),
  _averageHeight(0)
  {

  }
};


class TileTessellator {
public:
  virtual ~TileTessellator() {
  }

  virtual Mesh* createTileMesh(const G3MRenderContext* rc,
                               const PlanetRenderContext* prc,
                               Tile* tile,
                               const ElevationData* elevationData,
                               const DEMGrid* grid,
                               TileTessellatorMeshData& tileTessellatorMeshData) const = 0;

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
