package org.glob3.mobile.generated; 
public class TextureHolder
{
  public final TextureSpec _textureSpec = new TextureSpec();
  public GLTextureID _glTextureId = new GLTextureID();

  public int _referenceCounter;

  public TextureHolder(TextureSpec textureSpec)
  {
	  _referenceCounter = 1;
	  _textureSpec = new TextureSpec(textureSpec);
	  _glTextureId = new GLTextureID(GLTextureID.invalid());

  }

  public void dispose()
  {
  }

  public final void retain()
  {
	_referenceCounter++;
  }

  public final void release()
  {
	_referenceCounter--;
  }

  public final boolean isRetained()
  {
	return _referenceCounter > 0;
  }

  public final boolean hasSpec(TextureSpec textureSpec)
  {
	return _textureSpec.equalsTo(textureSpec);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	std.ostringstream buffer = new std.ostringstream();
	buffer << "(#";
	buffer << _glTextureId.getGLTextureID();
	buffer << ", counter=";
	buffer << _referenceCounter;
	buffer << ")";
	return buffer.str();

	////    return "TextureHolder(textureSpec=" + _textureSpec.description() + ", glTextureId=" + _glTextureId.description() + ")";
	//    return "TextureHolder(#" + _glTextureId.description() + ", counter=" + _referenceCounter + ")";
  }
}