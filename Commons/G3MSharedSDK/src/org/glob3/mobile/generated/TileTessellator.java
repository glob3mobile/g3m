package org.glob3.mobile.generated; 
public abstract class TileTessellator
{
  public void dispose()
  {
  }

  public abstract Mesh createTileMesh(Planet planet, Vector2I resolution, Tile tile, ElevationData elevationData, float verticalExaggeration, boolean debug, TileTessellatorMeshData data);

  public abstract Vector2I getTileMeshResolution(Planet planet, Vector2I resolution, Tile tile, boolean debug);

  public abstract Mesh createTileDebugMesh(Planet planet, Vector2I resolution, Tile tile);

  public abstract IFloatBuffer createTextCoords(Vector2I resolution, Tile tile);

  public Vector2F getTextCoord(Tile tile, Geodetic2D position)
  {
    return getTextCoord(tile, position._latitude, position._longitude);
  }

  public abstract Vector2F getTextCoord(Tile tile, Angle latitude, Angle longitude);

  public abstract void setRenderedSector(Sector sector);

}