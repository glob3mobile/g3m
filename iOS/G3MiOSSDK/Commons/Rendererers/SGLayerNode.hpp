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

  bool _initialized;

  const IGLTextureId* getTextureId(const G3MRenderContext* rc);

  IImage* _downloadedImage;
  void requestImage(const G3MRenderContext* rc);

#ifdef C_CODE
  const IGLTextureId* _textureId;
#endif
#ifdef JAVA_CODE
  private IGLTextureId _textureId;
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
//  _applyTo(applyTo),
//  _blendMode(blendMode),
//  _flipY(flipY),
//  _magFilter(magFilter),
//  _minFilter(minFilter),
//  _wrapS(wrapS),
//  _wrapT(wrapT),
  _downloadedImage(NULL),
  _textureId(NULL),
  _initialized(false)
  {

  }

  bool isReadyToRender(const G3MRenderContext* rc);

  void onImageDownload(IImage* image);

  const GLState* createState(const G3MRenderContext* rc,
                             const GLState& parentState);

};

#endif
