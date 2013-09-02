package org.glob3.mobile.generated; 
public class LTMInitializer extends LazyTextureMappingInitializer
{
  private final Tile _tile;
  private final Tile _ancestor;

  private MutableVector2D _scale = new MutableVector2D();
  private MutableVector2D _translation = new MutableVector2D();

  private final TileTessellator _tessellator;
  private final Vector2I _resolution;

  private final boolean _mercator;

  public LTMInitializer(Vector2I resolution, Tile tile, Tile ancestor, TileTessellator tessellator, boolean mercator)
  {
     _resolution = resolution;
     _tile = tile;
     _ancestor = ancestor;
     _tessellator = tessellator;
     _scale = new MutableVector2D(1,1);
     _translation = new MutableVector2D(0,0);
     _mercator = mercator;

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
      final Sector tileSector = _tile.getSector();

      final Vector2D lowerTextCoordUV = _tessellator.getTextCoord(_ancestor, tileSector._lower, _mercator);

      final Vector2D upperTextCoordUV = _tessellator.getTextCoord(_ancestor, tileSector._upper, _mercator);

      _scale = new MutableVector2D(upperTextCoordUV._x - lowerTextCoordUV._x, lowerTextCoordUV._y - upperTextCoordUV._y);

      _translation = new MutableVector2D(lowerTextCoordUV._x, upperTextCoordUV._y);
    }
  }

  public final MutableVector2D getScale()
  {
    return _scale;
  }

  public final MutableVector2D getTranslation()
  {
    return _translation;
  }

  public final IFloatBuffer createTextCoords()
  {
    return _tessellator.createTextCoords(_resolution, _tile, _mercator);
  }

}