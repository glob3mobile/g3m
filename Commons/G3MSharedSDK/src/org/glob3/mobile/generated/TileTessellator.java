package org.glob3.mobile.generated;import java.util.*;

public abstract class TileTessellator
{
  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Mesh* createTileMesh(const G3MRenderContext* rc, const PlanetRenderContext* prc, Tile* tile, const ElevationData* elevationData, TileTessellatorMeshData& data) const = 0;
  public abstract Mesh createTileMesh(G3MRenderContext rc, PlanetRenderContext prc, Tile tile, ElevationData elevationData, tangible.RefObject<TileTessellatorMeshData> data);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector2S getTileMeshResolution(const G3MRenderContext* rc, const PlanetRenderContext* prc, const Tile* tile) const = 0;
  public abstract Vector2S getTileMeshResolution(G3MRenderContext rc, PlanetRenderContext prc, Tile tile);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Mesh* createTileDebugMesh(const G3MRenderContext* rc, const PlanetRenderContext* prc, const Tile* tile) const = 0;
  public abstract Mesh createTileDebugMesh(G3MRenderContext rc, PlanetRenderContext prc, Tile tile);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IFloatBuffer* createTextCoords(const Vector2S& resolution, const Tile* tile) const = 0;
  public abstract IFloatBuffer createTextCoords(Vector2S resolution, Tile tile);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const Vector2F getTextCoord(const Tile* tile, const Geodetic2D& position) const
  public Vector2F getTextCoord(Tile tile, Geodetic2D position)
  {
	return getTextCoord(tile, position._latitude, position._longitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const Vector2F getTextCoord(const Tile* tile, const Angle& latitude, const Angle& longitude) const = 0;
  public abstract Vector2F getTextCoord(Tile tile, Angle latitude, Angle longitude);

  public abstract void setRenderedSector(Sector sector);

}
