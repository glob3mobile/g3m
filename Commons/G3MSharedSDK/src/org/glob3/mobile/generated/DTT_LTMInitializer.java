package org.glob3.mobile.generated;public class DTT_LTMInitializer extends LazyTextureMappingInitializer
{
  private final Tile _tile;
  private final Tile _ancestor;

  private float _translationU;
  private float _translationV;
  private float _scaleU;
  private float _scaleV;

  private final TileTessellator _tessellator;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Vector2S _resolution = new Vector2S();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Vector2S _resolution = new internal();
//#endif

  public DTT_LTMInitializer(Vector2S resolution, Tile tile, Tile ancestor, TileTessellator tessellator)
  {
	  _resolution = new Vector2S(resolution);
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif

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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector2F getTranslation() const
  public final Vector2F getTranslation()
  {
	return new Vector2F(_translationU, _translationV);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector2F getScale() const
  public final Vector2F getScale()
  {
	return new Vector2F(_scaleU, _scaleV);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* createTextCoords() const
  public final IFloatBuffer createTextCoords()
  {
	return _tessellator.createTextCoords(new Vector2S(_resolution._x, _resolution._y), _tile);
  }

}
