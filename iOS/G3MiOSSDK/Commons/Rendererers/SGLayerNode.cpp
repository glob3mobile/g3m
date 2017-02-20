//
//  SGLayerNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/9/12.
//
//

#include "SGLayerNode.hpp"

#include "IImageDownloadListener.hpp"
#include "ILogger.hpp"
#include "URL.hpp"
#include "IStringBuilder.hpp"
#include "G3MRenderContext.hpp"
#include "IDownloader.hpp"
#include "DownloadPriority.hpp"
#include "TimeInterval.hpp"
#include "TexturesHandler.hpp"
#include "GLConstants.hpp"
#include "GLState.hpp"
#include "TextureIDReference.hpp"


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
                                    url._path.c_str());
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
  delete _textureID; //Releasing texture through TextureIDReference class
#endif
#ifdef JAVA_CODE
  _textureID.dispose(); //Releasing texture through TextureIDReference class
  super.dispose();
#endif
}

bool SGLayerNode::isReadyToRender(const G3MRenderContext* rc) {
  if (!_initialized) {
    _initialized = true;
    requestImage(rc);
  }

  const TextureIDReference* textureID = getTextureID(rc);
  return (textureID != NULL);
}

void SGLayerNode::onImageDownload(const IImage* image) {
  delete _downloadedImage;
  _downloadedImage = image;
}

URL SGLayerNode::getURL() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString(_uriPrefix);
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
                                    DownloadPriority::HIGHEST,
                                    TimeInterval::fromDays(30),
                                    true,
                                    new SGLayerNode_ImageDownloadListener(this),
                                    true);
}

const TextureIDReference* SGLayerNode::getTextureID(const G3MRenderContext* rc) {
  if (_textureID == NULL) {
    if (_downloadedImage != NULL) {
      const bool generateMipmap = false;
      _textureID = rc->getTexturesHandler()->getTextureIDReference(_downloadedImage,
                                                                   GLFormat::rgba(),
                                                                   getURL()._path,
                                                                   generateMipmap);

      delete _downloadedImage;
      _downloadedImage = NULL;
    }
  }
  return _textureID;
}

bool SGLayerNode::modifyGLState(const G3MRenderContext* rc, GLState* state) {
  if (!_initialized) {
    _initialized = true;
    requestImage(rc);
  }

  _textureID = getTextureID(rc);
  if (_textureID == NULL) {
    return false;
  }
  state->clearGLFeatureGroup(COLOR_GROUP);

  state->addGLFeature(new TextureIDGLFeature(_textureID->getID()), false);
  
  return true;
}
