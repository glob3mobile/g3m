//
//  SGTextureNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGTextureNode.hpp"

#include "SGLayerNode.hpp"
#include "GLGlobalState.hpp"



class EnvMappingCustomShader : public CustomShaderGLFeature {
  
public:
  EnvMappingCustomShader() :
  CustomShaderGLFeature("env-geometry-shader") {
  };
  
protected:
    //using CustomShaderGLFeature::onInitializeShader;
    bool onInitializeShader(const GL* gl, const GLState* state, const GPUProgram* linkedProgram) {
      return false;
    }
  
    //using CustomShaderGLFeature::onAfterApplyShaderOnGPU;
    void onAfterApplyShaderOnGPU(const GL* gl, const GLState* state, const GPUProgram* linkedProgram) {
    }
  
    //using CustomShaderGLFeature::applyOnGlobalGLState;
    void applyOnGlobalGLState(GLGlobalState* state) const {
    }
};

void SGTextureNode::addLayer(SGLayerNode* layer) {
  _layers.push_back(layer);

  if (_context != NULL) {
    layer->initialize(_context, _shape);
  }

  if (_glState != NULL) {
    _glState->_release();
  }
  _glState = NULL;
}

bool SGTextureNode::isReadyToRender(const G3MRenderContext* rc) {
  const size_t layersCount = _layers.size();
  for (size_t i = 0; i < layersCount; i++) {
    SGLayerNode* layer = _layers[i];
    if (!layer->isReadyToRender(rc)) {
      return false;
    }
  }

  return SGNode::isReadyToRender(rc);
}

void SGTextureNode::initialize(const G3MContext* context,
                               SGShape *shape) {
  SGNode::initialize(context, shape);

  const size_t layersCount = _layers.size();
  for (size_t i = 0; i < layersCount; i++) {
    SGLayerNode* child = _layers[i];
    child->initialize(context, shape);
  }
}

SGTextureNode::~SGTextureNode() {
  const size_t layersCount = _layers.size();
  for (size_t i = 0; i < layersCount; i++) {
    SGLayerNode* layer = _layers[i];
    delete layer;
  }

  if (_glState != NULL) {
    _glState->_release();
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

const GLState* SGTextureNode::createState(const G3MRenderContext* rc, const GLState* parentState) {
  if (_glState == NULL) {
    _glState = new GLState();
    if (_envEffect) {
        EnvMappingCustomShader* env = new EnvMappingCustomShader();
        _glState->addGLFeature(env, true);
    }


    const size_t layersCount = _layers.size();
    for (size_t i = 0; i < layersCount; i++) {
      SGLayerNode* layer = _layers[i];
      if (!layer->modifyGLState(rc, _glState)) {
        _glState->_release();
        _glState = NULL;
        return parentState;
      }
    }

  }
  if (_glState != NULL) {
    _glState->setParent(parentState);
  }
  return _glState;
}
