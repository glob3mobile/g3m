package org.glob3.mobile.generated; 
public class TextureHolder
{
  public final TextureSpec _textureSpec;
  public GLTextureId _glTextureId = new GLTextureId();

  public int _referenceCounter;

  public TextureHolder(TextureSpec textureSpec)
  {
	  _referenceCounter = 1;
	  _textureSpec = textureSpec;
	  _glTextureId = new GLTextureId(GLTextureId.invalid());

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
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add("(#").add(_glTextureId.getGLTextureId()).add(", counter=").add(_referenceCounter).add(")");
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }
}