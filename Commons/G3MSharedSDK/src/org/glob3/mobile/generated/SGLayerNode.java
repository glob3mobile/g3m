package org.glob3.mobile.generated;import java.util.*;

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
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureIDReference;

public class SGLayerNode extends SGNode
{
  private final String _uri;

//  const std::string _applyTo;
//  const std::string _blendMode;
//  const bool        _flipY;
//
//  const std::string _magFilter;
//  const std::string _minFilter;
//  const std::string _wrapS;
//  const std::string _wrapT;

  private boolean _initialized;

  private TextureIDReference getTextureId(G3MRenderContext rc)
  {
	if (_textureId == null)
	{
	  if (_downloadedImage != null)
	  {
		final boolean generateMipmap = false;
		_textureId = rc.getTexturesHandler().getTextureIDReference(_downloadedImage, GLFormat.rgba(), getURL()._path, generateMipmap, GLTextureParameterValue.repeat());
  
		if (_downloadedImage != null)
			_downloadedImage.dispose();
		_downloadedImage = null;
	  }
	}
	return _textureId;
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final IImage _downloadedImage;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public IImage _downloadedImage = new internal();
//#endif
  private void requestImage(G3MRenderContext rc)
  {
	if (_uri.compareTo("") == 0)
	{
	  return;
	}
  
	rc.getDownloader().requestImage(getURL(), DownloadPriority.HIGHEST, TimeInterval.fromDays(30), true, new SGLayerNode_ImageDownloadListener(this), true);
									  //TEXTURES_DOWNLOAD_PRIORITY,
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final TextureIDReference _textureId;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public TextureIDReference _textureId = new internal();
//#endif

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
  {
	  super(id, sId);
	  _uri = uri;
	  _downloadedImage = null;
	  _textureId = null;
	  _initialized = false;
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	if (_textureId != null)
		_textureId.dispose(); //Releasing texture through TextureIDReference class
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_textureId.dispose(); //Releasing texture through TextureIDReference class
//#endif
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
	if (!_initialized)
	{
	  _initialized = true;
	  requestImage(rc);
	}
  
	final TextureIDReference textureId = getTextureId(rc);
	return (textureId != null);
  }

  public final void onImageDownload(IImage image)
  {
	if (_downloadedImage != null)
		_downloadedImage.dispose();
	_downloadedImage = image;
  }

  public final boolean modifyGLState(G3MRenderContext rc, GLState state)
  {
  
	if (!_initialized)
	{
	  _initialized = true;
	  requestImage(rc);
	}
  
	_textureId = getTextureId(rc);
	if (_textureId == null)
	{
	  return false;
	}
	state.clearGLFeatureGroup(GLFeatureGroupName.COLOR_GROUP);
  
	state.addGLFeature(new TextureIDGLFeature(_textureId.getID()), false);
  
	return true;
  
  }

  public final String description()
  {
	return "SGLayerNode";
  }
}
