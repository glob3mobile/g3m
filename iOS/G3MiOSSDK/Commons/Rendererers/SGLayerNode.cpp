//
//  SGLayerNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/9/12.
//
//

#include "SGLayerNode.hpp"

#include "IGLTextureId.hpp"
#include "GL.hpp"
#include "Context.hpp"
#include "IDownloader.hpp"
#include "SGShape.hpp"
#include "IImageDownloadListener.hpp"
#include "TexturesHandler.hpp"
#include "TextureIDReference.hpp"
#include "IStringBuilder.hpp"
#include "GPUProgramManager.hpp"
#include "GPUProgram.hpp"

#define TEXTURES_DOWNLOAD_PRIORITY 1000000


class SGLayerNode_ImageDownloadListener : public IImageDownloadListener {
private:
  SGLayerNode* _layerNode;

public:
  SGLayerNode_ImageDownloadListener(SGLayerNode* layerNode) :
  _layerNode(layerNode)
  {

  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired) {
    _layerNode->onImageDownload(image);
  }

  void onError(const URL& url) {
    ILogger::instance()->logWarning("Can't download texture \"%s\"",
                                    url.getPath().c_str());
  }

  void onCancel(const URL& url) {

  }

  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired) {

  }
};

SGLayerNode::~SGLayerNode() {
#ifdef C_CODE
  delete _textureId; //Releasing texture through TextureIDReference class
#endif
#ifdef JAVA_CODE
  _textureId.dispose(); //Releasing texture through TextureIDReference class
#endif
}

bool SGLayerNode::isReadyToRender(const G3MRenderContext* rc) {
  if (!_initialized) {
    _initialized = true;
    requestImage(rc);
  }

  const TextureIDReference* textureId = getTextureId(rc);
  return (textureId != NULL);
}

void SGLayerNode::onImageDownload(const IImage* image) {
  if (_downloadedImage != NULL) {
    IFactory::instance()->deleteImage(_downloadedImage);
  }
  _downloadedImage = image;
}

URL SGLayerNode::getURL() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString(_shape->getURIPrefix());
  isb->addString(_uri);
  const std::string path = isb->getString();
  delete isb;

  return URL(path, false);
}

void SGLayerNode::requestImage(const G3MRenderContext* rc) {
  if (_uri.compare("") == 0) {
    return;
  }

  rc->getDownloader()->requestImage(getURL(),
                                    TEXTURES_DOWNLOAD_PRIORITY,
                                    TimeInterval::fromDays(30),
                                    true,
                                    new SGLayerNode_ImageDownloadListener(this),
                                    true);
}

const TextureIDReference* SGLayerNode::getTextureId(const G3MRenderContext* rc) {
  if (_textureId == NULL) {
    if (_downloadedImage != NULL) {
      const bool generateMipmap = false;
      _textureId = rc->getTexturesHandler()->getTextureIDReference(_downloadedImage,
                                                                   GLFormat::rgba(),
                                                                   getURL().getPath(),
                                                                   generateMipmap);

      IFactory::instance()->deleteImage(_downloadedImage);
      _downloadedImage = NULL;
    }
  }
  return _textureId;
}

bool SGLayerNode::modifyGLState(const G3MRenderContext* rc, GLState* state) {

  if (!_initialized) {
    _initialized = true;
    requestImage(rc);
  }

  _textureId = getTextureId(rc);
  if (_textureId == NULL) {
    return false;
  }
  state->clearGLFeatureGroup(COLOR_GROUP);

  state->addGLFeature(new TextureIDGLFeature(_textureId->getID()), false);
  
  return true;
  
}
