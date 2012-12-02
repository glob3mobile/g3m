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
#include "IStringBuilder.hpp"

#define TEXTURES_DOWNLOAD_PRIORITY 1000000


class ImageDownloadListener : public IImageDownloadListener {
private:
  SGLayerNode* _layerNode;

public:
  ImageDownloadListener(SGLayerNode* layerNode) :
  _layerNode(layerNode)
  {

  }

  void onDownload(const URL& url,
                  const IImage* image) {
    _layerNode->onImageDownload(image);
  }

  void onError(const URL& url) {
    ILogger::instance()->logWarning("Can't download texture \"%s\"",
                                    url.getPath().c_str());
  }

  void onCancel(const URL& url) {

  }

  void onCanceledDownload(const URL& url,
                          const IImage* image) {

  }
};


//void TextureDownloadListener::onDownload(const URL& url,
//                                         const IImage* image) {
//  _layerNode->onImageDownload(image);
//}

void SGLayerNode::onImageDownload(const IImage* image) {
  if (_downloadedImage != NULL) {
    delete _downloadedImage;
  }
  _downloadedImage = image->shallowCopy();
}

URL SGLayerNode::getURL() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString(_shape->getURIPrefix());
  isb->addString(_uri);
  const std::string path = isb->getString();
  delete isb;

  return URL(path, false);
}

void SGLayerNode::requestImage() {
  if (_uri.compare("") == 0) {
    return;
  }

  if (_context == NULL) {
    return;
  }

  _context->getDownloader()->requestImage(getURL(),
                                          TEXTURES_DOWNLOAD_PRIORITY,
                                          new ImageDownloadListener(this),
                                          true);
}

void SGLayerNode::initialize(const G3MContext* context,
                             SGShape *shape) {
  SGNode::initialize(context, shape);

  requestImage();
}

//void SGLayerNode::setURI(const std::string& uri) {
//  if (_uri.compare(uri) == 0) {
//    return;
//  }
//
//  _uri = uri;
//
//  if (_requestInProgress != -1) {
//    if (_context != NULL) {
//      _context->getDownloader()->cancelRequest(_requestInProgress);
//      _requestInProgress = -1;
//    }
//  }
//
//  int __release_texture_id_uploaded;
//}

const IGLTextureId* SGLayerNode::getTextureId(const G3MRenderContext* rc) {
  if (_textureId == NULL) {
    if (_downloadedImage != NULL) {
      const bool hasMipMap = false;
      _textureId = rc->getTexturesHandler()->getGLTextureId(_downloadedImage,
                                                            GLFormat::rgba(),
                                                            getURL().getPath(),
                                                            hasMipMap);

      delete _downloadedImage;
      _downloadedImage = NULL;
    }
  }
  return _textureId;
}

void SGLayerNode::prepareRender(const G3MRenderContext* rc) {
  SGNode::prepareRender(rc);

  //  SGShape* shape = getShape();

  const IGLTextureId* texId = getTextureId(rc);
  _textureBound = (texId != NULL);
  if (_textureBound) {
    GL *gl = rc->getGL();

    //    if (_transparent) {
    //      gl->enableBlend();
    //    }

    // OJO
    //gl->enableTextures();
    //gl->enableTexture2D();

    //    _textureMapping->bind(rc);
    //    gl->transformTexCoords(_scale, _translation);
    gl->bindTexture(texId);
    //    gl->setTextureCoordinates(2, 0, _texCoords);
  }
}

void SGLayerNode::cleanUpRender(const G3MRenderContext* rc) {
  if (_textureBound) {
    GL *gl = rc->getGL();

    // OJO
    //gl->disableTexture2D();
    //gl->disableTextures();

    //    if (_transparent) {
    //      gl->disableBlend();
    //    }
    
    _textureBound = false;
  }
  
  SGNode::cleanUpRender(rc);
}
