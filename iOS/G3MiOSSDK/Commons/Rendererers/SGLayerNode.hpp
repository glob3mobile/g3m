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

class SGLayerNode : public SGNode {
private:
  std::string _uri;
  std::string _applyTo;
  std::string _blendMode;
  bool        _flipY;

  std::string _magFilter;
  std::string _minFilter;
  std::string _wrapS;
  std::string _wrapT;

protected:
  virtual void prepareRender(const G3MRenderContext* rc);

  virtual void cleanUpRender(const G3MRenderContext* rc);

public:

  void setUri(const std::string& uri) {
    _uri = uri;
  }
  
  void setApplyTo(const std::string& applyTo) {
    _applyTo = applyTo;
  }

  void setBlendMode(const std::string& blendMode) {
    _blendMode = blendMode;
  }

  void setFlipY(bool flipY) {
    _flipY = flipY;
  }

  void setMagFilter(const std::string& magFilter) {
    _magFilter = magFilter;
  }

  void setMinFilter(const std::string& minFilter) {
    _minFilter = minFilter;
  }

  void setWrapS(const std::string& wrapS) {
    _wrapS = wrapS;
  }

  void setWrapT(const std::string& wrapT) {
    _wrapT = wrapT;
  }

};

#endif
