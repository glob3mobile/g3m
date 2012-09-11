package org.glob3.mobile.generated; 
public class SimpleTextureMapping extends TextureMapping
{
  private GLTextureId _glTextureId = new GLTextureId();
  private IFloatBuffer _texCoords;
  private final boolean _ownedTexCoords;

  private MutableVector2D _translation = new MutableVector2D();
  private MutableVector2D _scale = new MutableVector2D();


  public SimpleTextureMapping(GLTextureId glTextureId, IFloatBuffer texCoords, boolean ownedTexCoords)
  {
	  _glTextureId = new GLTextureId(glTextureId);
	  _texCoords = texCoords;
	  _translation = new MutableVector2D(0, 0);
	  _scale = new MutableVector2D(1, 1);
	  _ownedTexCoords = ownedTexCoords;

  }

//  SimpleTextureMapping(const GLTextureId& glTextureId,
//                       std::vector<MutableVector2D> texCoords);

  public final void setTranslationAndScale(Vector2D translation, Vector2D scale)
  {
	_translation = translation.asMutableVector2D();
	_scale = scale.asMutableVector2D();
  }

  public void dispose()
  {
	if (_ownedTexCoords)
	{
	  if (_texCoords != null)
		  _texCoords.dispose();
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const GLTextureId getGLTextureId() const
  public final GLTextureId getGLTextureId()
  {
	return _glTextureId;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* getTexCoords() const
  public final IFloatBuffer getTexCoords()
  {
	  return _texCoords;
  }


  //SimpleTextureMapping::SimpleTextureMapping(const GLTextureId& glTextureId,
  //                                           std::vector<MutableVector2D> texCoords) :
  //_glTextureId(glTextureId),
  //_translation(0, 0),
  //_scale(1, 1),
  //_ownedTexCoords(true)
  //{
  //  const int texCoordsSize = texCoords.size();
  //  float* texCoordsA = new float[2 * texCoordsSize];
  //  int p = 0;
  //  for (int i = 0; i < texCoordsSize; i++) {
  //    texCoordsA[p++] = (float) texCoords[i].x();
  //    texCoordsA[p++] = (float) texCoords[i].y();
  //  }
  //  _texCoords = texCoordsA;
  //}
  
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