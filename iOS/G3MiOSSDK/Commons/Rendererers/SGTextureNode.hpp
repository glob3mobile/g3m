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

  GLState* _glState;

public:
  SGTextureNode(const std::string& id,
                const std::string& sId) :
  SGNode(id, sId),
  _glState(NULL)
  {

  }

  virtual ~SGTextureNode();

  void addLayer(SGLayerNode* layer);

  bool isReadyToRender(const G3MRenderContext* rc);

  void initialize(const G3MContext* context,
                  SGShape *shape);

//  void prepareRender(const G3MRenderContext* rc);
//
//  void cleanUpRender(const G3MRenderContext* rc);

  const GLState* createState(const G3MRenderContext* rc,
                             const GLState* parentState);

//  void render(const G3MRenderContext* rc,
//              GLState* parentState,
//              bool renderNotReadyShapes);

  std::string description(){
    return "SGTextureNode";
  }

};

#endif
