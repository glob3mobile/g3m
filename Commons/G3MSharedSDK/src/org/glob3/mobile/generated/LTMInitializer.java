package org.glob3.mobile.generated; 
public class LTMInitializer extends LazyTextureMappingInitializer
{
  private final Tile _tile;
  private final Tile _ancestor;

  private MutableVector2D _scale = new MutableVector2D();
  private MutableVector2D _translation = new MutableVector2D();

  private IFloatBuffer _texCoords;

  public LTMInitializer(Tile tile, Tile ancestor, IFloatBuffer texCoords)
  {
	  _tile = tile;
	  _ancestor = ancestor;
	  _texCoords = texCoords;
	  _scale = new MutableVector2D(1,1);
	  _translation = new MutableVector2D(0,0);

  }

  public void dispose()
  {

  }

  public final void initialize()
  {
	// The default scale and translation are ok when (tile == _ancestor)
	if (_tile != _ancestor)
	{
	  final Sector tileSector = _tile.getSector();
	  final Sector ancestorSector = _ancestor.getSector();

	  _scale = tileSector.getScaleFactor(ancestorSector).asMutableVector2D();
	  _translation = tileSector.getTranslationFactor(ancestorSector).asMutableVector2D();
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const MutableVector2D getScale() const
  public final MutableVector2D getScale()
  {
	return _scale;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const MutableVector2D getTranslation() const
  public final MutableVector2D getTranslation()
  {
	return _translation;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* getTexCoords() const
  public final IFloatBuffer getTexCoords()
  {
	return _texCoords;
  }

}