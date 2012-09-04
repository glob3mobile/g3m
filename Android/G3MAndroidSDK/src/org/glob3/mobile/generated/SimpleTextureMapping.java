package org.glob3.mobile.generated; 
public class SimpleTextureMapping extends TextureMapping
{
  private GLTextureId _glTextureId = new GLTextureId();
  private final float[] _texCoords;
  private final boolean _ownedTexCoords;

  private MutableVector2D _translation = new MutableVector2D();
  private MutableVector2D _scale = new MutableVector2D();


  public SimpleTextureMapping(GLTextureId glTextureId, float[] texCoords, boolean ownedTexCoords)
  {
	  _glTextureId = new GLTextureId(glTextureId);
	  _texCoords = texCoords;
	  _translation = new MutableVector2D(0, 0);
	  _scale = new MutableVector2D(1, 1);
	  _ownedTexCoords = ownedTexCoords;

  }

  public SimpleTextureMapping(GLTextureId glTextureId, java.util.ArrayList<MutableVector2D> texCoords)
  {
	  _glTextureId = new GLTextureId(glTextureId);
	  _translation = new MutableVector2D(0, 0);
	  _scale = new MutableVector2D(1, 1);
	  _ownedTexCoords = true;
	final int texCoordsSize = texCoords.size();
	float[] texCoordsA = new float[2 * texCoordsSize];
	int p = 0;
	for (int i = 0; i < texCoordsSize; i++)
	{
	  texCoordsA[p++] = (float) texCoords.get(i).x();
	  texCoordsA[p++] = (float) texCoords.get(i).y();
	}
	_texCoords = texCoordsA;
  }

  public final void setTranslationAndScale(Vector2D translation, Vector2D scale)
  {
	_translation = translation.asMutableVector2D();
	_scale = scale.asMutableVector2D();
  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GLTextureId getGLTextureId() const
  public final GLTextureId getGLTextureId()
  {
	return _glTextureId;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const float[] getTexCoords() const
  public final float[] getTexCoords()
  {
	  return _texCoords;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void bind(const RenderContext* rc) const
  public final void bind(RenderContext rc)
  {
	GL gl = rc.getGL();
  
	gl.transformTexCoords(_scale, _translation);
	gl.bindTexture(_glTextureId);
	gl.setTextureCoordinates(2, 0, _texCoords);
  }

}