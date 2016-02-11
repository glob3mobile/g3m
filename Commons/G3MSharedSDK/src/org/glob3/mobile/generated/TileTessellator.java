package org.glob3.mobile.generated; 
public abstract class TileTessellator
{
  public void dispose()
  {
  }

  public abstract Mesh createTileMesh(G3MRenderContext rc, PlanetRenderContext prc, Tile tile, ElevationData elevationData, TileTessellatorMeshData data);

  public abstract Vector2I getTileMeshResolution(G3MRenderContext rc, PlanetRenderContext prc, Tile tile);

  public abstract Mesh createTileDebugMesh(G3MRenderContext rc, PlanetRenderContext prc, Tile tile);

  public abstract IFloatBuffer createTextCoords(Vector2I resolution, Tile tile);

  public Vector2F getTextCoord(Tile tile, Geodetic2D position)
  {
    return getTextCoord(tile, position._latitude, position._longitude);
  }

  public abstract Vector2F getTextCoord(Tile tile, Angle latitude, Angle longitude);

  public abstract void setRenderedSector(Sector sector);

}