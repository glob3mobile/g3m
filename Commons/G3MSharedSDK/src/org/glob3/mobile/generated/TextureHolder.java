package org.glob3.mobile.generated;import java.util.*;

public class TextureHolder
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  public final TextureSpec _textureSpec = new TextureSpec();
  public final IGLTextureId _glTextureId;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final TextureSpec _textureSpec = new public();
  public IGLTextureId _glTextureId = new public();
//#endif

  public int _referenceCounter;

  public TextureHolder(TextureSpec textureSpec)
  {
	  _referenceCounter = 1;
	  _textureSpec = new TextureSpec(textureSpec);
	  _glTextureId = null;

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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return _textureSpec.equalsTo(textureSpec);
	return _textureSpec.equalsTo(new TextureSpec(textureSpec));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("(#");
	isb.addString(_glTextureId.description());
	isb.addString(", counter=");
	isb.addLong(_referenceCounter);
	isb.addString(")");
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }
}
