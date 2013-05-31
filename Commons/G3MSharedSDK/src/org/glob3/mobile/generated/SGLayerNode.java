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



//class IGLTextureId;
//class IImage;

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
  
    rc.getDownloader().requestImage(getURL(), DefineConstants.TEXTURES_DOWNLOAD_PRIORITY, TimeInterval.fromDays(30), true, new SGLayerNode_ImageDownloadListener(this), true);
  }

  private IGLTextureId _textureId;

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
//  _applyTo(applyTo),
//  _blendMode(blendMode),
//  _flipY(flipY),
//  _magFilter(magFilter),
//  _minFilter(minFilter),
//  _wrapS(wrapS),
//  _wrapT(wrapT),
  {
     super(id, sId);
     _uri = uri;
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

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GLGlobalState createState(G3MRenderContext rc, GLGlobalState parentState);


  /*
  GPUProgramState* createGPUProgramState(const G3MRenderContext* rc, const GPUProgramState* parentState){
  
    GPUProgramState* progState = new GPUProgramState(parentState);
    progState->setAttributeEnabled("TextureCoord", true);
    progState->setUniformValue("EnableTexture", true);
    return progState;
  }
  
  GLGlobalState* SGLayerNode::createState(const G3MRenderContext* rc,
                                          const GLGlobalState& parentState) {
    if (!_initialized) {
      _initialized = true;
      requestImage(rc);
    }
  
    const IGLTextureId* texId = getTextureId(rc);
    if (texId == NULL) {
      return NULL;
    }
  
    GLGlobalState* state = new GLGlobalState(parentState);
    state->bindTexture(texId);
    int __WORKING;
  
    return state;
  }
  */
  //void SGLayerNode::modifyGLGlobalState(GLGlobalState& GLGlobalState) const{
  //
  //  int __WORKING;
  //  /*if (!_initialized) {
  //    _initialized = true;
  //    requestImage(rc);
  //  }
  //
  //  const IGLTextureId* texId = getTextureId(rc);
  //  if (texId != NULL) {
  //    state.bindTexture(texId);
  //  }
  //   */
  //}
  
  public final void SGLayerNode.modifyGPUProgramState(GPUProgramState progState)
  {
    progState.setAttributeEnabled("TextureCoord", true);
    progState.setUniformValue("EnableTexture", true);
  }

  public final void modifyGLGlobalState(GLGlobalState GLGlobalState)
  {
  }
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void modifyGPUProgramState(GPUProgramState progState);

}