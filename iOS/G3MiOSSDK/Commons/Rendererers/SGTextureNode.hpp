//
//  SGTextureNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3MiOSSDK__SGTextureNode__
#define __G3MiOSSDK__SGTextureNode__

#include "SGNode.hpp"

class SGLayerNode;

class SGTextureNode : public SGNode {
private:
  std::vector<SGLayerNode*> _layers;

protected:

  void rawRender(const RenderContext* rc);

public:
  void addLayer(SGLayerNode* layer);

  bool isReadyToRender(const RenderContext* rc);

};

#endif
