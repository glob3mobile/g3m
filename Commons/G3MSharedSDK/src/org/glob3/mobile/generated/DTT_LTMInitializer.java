package org.glob3.mobile.generated; 
public class DTT_LTMInitializer extends LazyTextureMappingInitializer
{
  private final Tile _tile;
  private final Tile _ancestor;

  private float _translationU;
  private float _translationV;
  private float _scaleU;
  private float _scaleV;

  private final TileTessellator _tessellator;
  private final Vector2I _resolution;

  public DTT_LTMInitializer(Vector2I resolution, Tile tile, Tile ancestor, TileTessellator tessellator)
  {
     _resolution = resolution;
     _tile = tile;
     _ancestor = ancestor;
     _tessellator = tessellator;
     _translationU = 0F;
     _translationV = 0F;
     _scaleU = 1F;
     _scaleV = 1F;

  }

  public void dispose()
  {
    super.dispose();

  }

  public final void initialize()
  {
    // The default scale and translation are ok when (tile == _ancestor)
    if (_tile != _ancestor)
    {
      final Sector tileSector = _tile._sector;

      final Vector2F lowerTextCoordUV = _tessellator.getTextCoord(_ancestor, tileSector._lower);

      final Vector2F upperTextCoordUV = _tessellator.getTextCoord(_ancestor, tileSector._upper);

      _translationU = lowerTextCoordUV._x;
      _translationV = upperTextCoordUV._y;

      _scaleU = upperTextCoordUV._x - lowerTextCoordUV._x;
      _scaleV = lowerTextCoordUV._y - upperTextCoordUV._y;
    }
  }

  public final Vector2F getTranslation()
  {
    return new Vector2F(_translationU, _translationV);
  }

  public final Vector2F getScale()
  {
    return new Vector2F(_scaleU, _scaleV);
  }

  public final IFloatBuffer createTextCoords()
  {
    return _tessellator.createTextCoords(_resolution, _tile);
  }

}