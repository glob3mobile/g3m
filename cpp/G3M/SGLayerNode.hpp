//
//  SGLayerNode.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/9/12.
//
//

#ifndef __G3M__SGLayerNode__
#define __G3M__SGLayerNode__

#include "SGNode.hpp"

class TextureIDReference;
class IImage;
class URL;


class SGLayerNode : public SGNode {
private:
  const std::string _uri;
  const int _wrapS;
  const int _wrapT;
  const bool _generateMipmap;

  mutable bool _initialized;

  const TextureIDReference* getTextureID(const G3MRenderContext* rc);

#ifdef C_CODE
  const IImage* _downloadedImage;
#endif
#ifdef JAVA_CODE
  private IImage _downloadedImage;
#endif
  void requestImage(const G3MRenderContext* rc);

#ifdef C_CODE
  const TextureIDReference* _textureID;
#endif
#ifdef JAVA_CODE
  private TextureIDReference _textureID;
#endif

  URL getURL() const;

public:

  SGLayerNode(const std::string& id,
              const std::string& sID,
              const std::string& uri,
              const int wrapS,
              const int wrapT,
              const bool generateMipmap) :
  SGNode(id, sID),
  _uri(uri),
  _wrapS(wrapS),
  _wrapT(wrapT),
  _generateMipmap(generateMipmap),
  _downloadedImage(NULL),
  _textureID(NULL),
  _initialized(false)
  {
  }

  ~SGLayerNode();
  
  bool isReadyToRender(const G3MRenderContext* rc);

  void onImageDownload(const IImage* image);
  
  bool modifyGLState(const G3MRenderContext* rc, GLState* state);

  const std::string description() {
    return "SGLayerNode";
  }
};

#endif
