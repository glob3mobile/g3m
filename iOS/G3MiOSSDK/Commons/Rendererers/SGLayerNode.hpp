//
//  SGLayerNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/9/12.
//
//

#ifndef __G3MiOSSDK__SGLayerNode__
#define __G3MiOSSDK__SGLayerNode__

#include "SGNode.hpp"
#include "URL.hpp"

class IGLTextureId;
class IImage;
class TextureIDReference;

class SGLayerNode : public SGNode {
private:
  const std::string _uri;

//  const std::string _applyTo;
//  const std::string _blendMode;
//  const bool        _flipY;
//
//  const std::string _magFilter;
//  const std::string _minFilter;
//  const std::string _wrapS;
//  const std::string _wrapT;

  mutable bool _initialized;

  const TextureIDReference* getTextureId(const G3MRenderContext* rc);

#ifdef C_CODE
  const IImage* _downloadedImage;
#endif
#ifdef JAVA_CODE
  private IImage _downloadedImage;
#endif
  void requestImage(const G3MRenderContext* rc);

#ifdef C_CODE
  const TextureIDReference* _textureId;
#endif
#ifdef JAVA_CODE
  private TextureIDReference _textureId;
#endif

  URL getURL() const;

public:

  SGLayerNode(const std::string& id,
              const std::string& sId,
              const std::string& uri,
              const std::string& applyTo,
              const std::string& blendMode,
              bool flipY,
              const std::string& magFilter,
              const std::string& minFilter,
              const std::string& wrapS,
              const std::string& wrapT) :
  SGNode(id, sId),
  _uri(uri),
  _downloadedImage(NULL),
  _textureId(NULL),
  _initialized(false)
  {
  }

  ~SGLayerNode();
  
  bool isReadyToRender(const G3MRenderContext* rc);

  void onImageDownload(const IImage* image);
  
  bool modifyGLState(const G3MRenderContext* rc, GLState* state);

  std::string description() {
    return "SGLayerNode";
  }
};

#endif
