package org.glob3.mobile.client.generated; 
public class TextureKey
{
  public final String _textureId;
  public final int _textureWidth;
  public final int _textureHeight;
  public int _glTextureId;

  public int _referenceCounter;

  public TextureKey(String textureId, int textureWidth, int textureHeight)
  {
	  _textureId = textureId;
	  _textureWidth = textureWidth;
	  _textureHeight = textureHeight;
	_referenceCounter = 1;
	_glTextureId = -1;
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

  public final boolean equalsTo(TextureKey other)
  {
	if (_textureWidth != other._textureWidth)
	{
	  return false;
	}
	if (_textureHeight != other._textureHeight)
	{
	  return false;
	}

	if (_textureId.compareTo(other._textureId) != 0)
	{
	  return false;
	}

	return true;
  }
}