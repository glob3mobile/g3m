package org.glob3.mobile.generated; 
//
//  SGLayerNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/9/12.
//
//

//
//  SGLayerNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/9/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLTextureId;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;

public class SGLayerNode extends SGNode
{
  private final String _uri;

  private final String _applyTo;
  private final String _blendMode;
  private final boolean _flipY;

  private final String _magFilter;
  private final String _minFilter;
  private final String _wrapS;
  private final String _wrapT;

  private IGLTextureId getTextureId(G3MRenderContext rc)
  {
	if (_textureId == null)
	{
	  if (_downloadedImage != null)
	  {
		final boolean hasMipMap = false;
		_textureId = rc.getTexturesHandler().getGLTextureId(_downloadedImage, GLFormat.rgba(), getURL().getPath(), hasMipMap);
  
		IFactory.instance().deleteImage(_downloadedImage);
		_downloadedImage = null;
	  }
	}
	return _textureId;
  }

  private IImage _downloadedImage;
  private void requestImage()
  {
	if (_uri.compareTo("") == 0)
	{
	  return;
	}
  
	if (_context == null)
	{
	  return;
	}
  
	_context.getDownloader().requestImage(getURL(), DefineConstants.TEXTURES_DOWNLOAD_PRIORITY, TimeInterval.fromDays(30), new ImageDownloadListener(this), true);
  }

  private IGLTextureId _textureId;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: URL getURL() const
  private URL getURL()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString(_shape.getURIPrefix());
	isb.addString(_uri);
	final String path = isb.getString();
	if (isb != null)
		isb.dispose();
  
	return new URL(path, false);
  }


  public SGLayerNode(String id, String sId, String uri, String applyTo, String blendMode, boolean flipY, String magFilter, String minFilter, String wrapS, String wrapT)
//  _textureBound(false),
  {
	  super(id, sId);
	  _uri = uri;
	  _applyTo = applyTo;
	  _blendMode = blendMode;
	  _flipY = flipY;
	  _magFilter = magFilter;
	  _minFilter = minFilter;
	  _wrapS = wrapS;
	  _wrapT = wrapT;
	  _downloadedImage = null;
	  _textureId = null;

  }

  public final void onImageDownload(IImage image)
  {
	if (_downloadedImage != null)
	{
	  IFactory.instance().deleteImage(_downloadedImage);
	}
	_downloadedImage = image.shallowCopy();
  }

  public final void initialize(G3MContext context, SGShape shape)
  {
	super.initialize(context, shape);
  
	requestImage();
  }

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
	final IGLTextureId texId = getTextureId(rc);
	if (texId == null)
	{
	  return null;
	}
  
	GLState state = new GLState(parentState);
	state.enableTextures();
	state.enableTexture2D();
  
	GL gl = rc.getGL();
	gl.bindTexture(texId);
  
	return state;
  }

}