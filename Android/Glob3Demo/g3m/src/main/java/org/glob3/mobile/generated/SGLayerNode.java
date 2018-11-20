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



//class TextureIDReference;
//class IImage;
//class URL;


public class SGLayerNode extends SGNode
{
  private final String _uri;
  private final int _wrapS;
  private final int _wrapT;
  private final boolean _generateMipmap;

  private boolean _initialized;

  private TextureIDReference getTextureID(G3MRenderContext rc)
  {
    if (_textureID == null)
    {
      if (_downloadedImage != null)
      {
        _textureID = rc.getTexturesHandler().getTextureIDReference(_downloadedImage, GLFormat.rgba(), getURL()._path, _generateMipmap, _wrapS, _wrapT);
  
        _downloadedImage = null;
        _downloadedImage = null;
      }
    }
    return _textureID;
  }

  private IImage _downloadedImage;
  private void requestImage(G3MRenderContext rc)
  {
    if (_uri.compareTo("") == 0)
    {
      return;
    }
  
    rc.getDownloader().requestImage(getURL(), DownloadPriority.HIGHEST, TimeInterval.fromDays(30), true, new SGLayerNode_ImageDownloadListener(this), true);
  }

  private TextureIDReference _textureID;

  private URL getURL()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString(_uriPrefix);
    isb.addString(_uri);
    final String path = isb.getString();
    if (isb != null)
       isb.dispose();
  
    return new URL(path, false);
  }


  public SGLayerNode(String id, String sID, String uri, int wrapS, int wrapT, boolean generateMipmap)
  {
     super(id, sID);
     _uri = uri;
     _wrapS = wrapS;
     _wrapT = wrapT;
     _generateMipmap = generateMipmap;
     _downloadedImage = null;
     _textureID = null;
     _initialized = false;
  }

  public void dispose()
  {
    _textureID.dispose(); //Releasing texture through TextureIDReference class
    _textureID = null;
    super.dispose();
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    if (!_initialized)
    {
      _initialized = true;
      requestImage(rc);
    }
  
    final TextureIDReference textureID = getTextureID(rc);
    return (textureID != null);
  }

  public final void onImageDownload(IImage image)
  {
    _downloadedImage = null;
    _downloadedImage = image;
  }

  public final boolean modifyGLState(G3MRenderContext rc, GLState state)
  {
    if (!_initialized)
    {
      _initialized = true;
      requestImage(rc);
    }
  
    _textureID = getTextureID(rc);
    if (_textureID == null)
    {
      return false;
    }
    state.clearGLFeatureGroup(GLFeatureGroupName.COLOR_GROUP);
  
    state.addGLFeature(new TextureIDGLFeature(_textureID.getID()), false);
  
    return true;
  }

  public final String description()
  {
    return "SGLayerNode";
  }
}
