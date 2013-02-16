package org.glob3.mobile.generated; 
public class GlobalMembersSGLayerNode
{




      public static final int TEXTURES_DOWNLOAD_PRIORITY = 1000000;
}

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



//class IGLTextureId;
//class IImage;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
public SuppressWarnings("unused") class SGLayerNode extends SGNode
{
//#else
public class SGLayerNode extends SGNode
{
//#endif
  private final String _uri;

  private final String _applyTo;
  private final String _blendMode;
  private final boolean _flipY;

  private final String _magFilter;
  private final String _minFilter;
  private final String _wrapS;
  private final String _wrapT;

  private boolean _initialized;

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
  private void requestImage(G3MRenderContext rc)
  {
    if (_uri.compareTo("") == 0)
    {
      return;
    }
  
    rc.getDownloader().requestImage(getURL(), TEXTURES_DOWNLOAD_PRIORITY, TimeInterval.fromDays(30), new ImageDownloadListener(this), true);
  }

//TangibleCopyWithoutConversion  private IGLTextureId _textureId;

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
     _applyTo = applyTo;
     _blendMode = blendMode;
     _flipY = flipY;
     _magFilter = magFilter;
     _minFilter = minFilter;
     _wrapS = wrapS;
     _wrapT = wrapT;
     _downloadedImage = null;
     _textureId = null;
     _initialized = false;

  }

  public final void onImageDownload(IImage image)
  {
    if (_downloadedImage != null)
    {
      IFactory.instance().deleteImage(_downloadedImage);
    }
    _downloadedImage = image;
  }

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
    if (!_initialized)
    {
      _initialized = true;
      requestImage(rc);
    }
  
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


public class ImageDownloadListener extends IImageDownloadListener
{
  private SGLayerNode _layerNode;

  public ImageDownloadListener(SGLayerNode layerNode)
  {
     _layerNode = layerNode;

  }

  public final void onDownload(URL url, IImage image)
  {
    _layerNode.onImageDownload(image);
  }

  public final void onError(URL url)
  {
    ILogger.instance().logWarning("Can't download texture \"%s\"", url.getPath());
  }

  public final void onCancel(URL url)
  {

  }

  public final void onCanceledDownload(URL url, IImage image)
  {

  }
}
