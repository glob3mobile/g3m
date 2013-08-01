//
//  SGGeometryNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#include "SGGeometryNode.hpp"

#include "Context.hpp"
#include "GL.hpp"

#include "IFloatBuffer.hpp"
#include "IShortBuffer.hpp"

#include "GLState.hpp"

SGGeometryNode::~SGGeometryNode() {
  delete _vertices;
  delete _colors;
  delete _uv;
  delete _normals;
  delete _indices;
}

void SGGeometryNode::createGLState() {

  _glState.addGLFeature(new GeometryGLFeature(_vertices,    //The attribute is a float vector of 4 elements
                                              3,            //Our buffer contains elements of 3
                                              0,            //Index 0
                                              false,        //Not normalized
                                              0,            //Stride 0
                                              true,         //Depth test
                                              false, 0,
                                              false, (float)0.0, (float)0.0,
                                              (float)1.0,
                                              true, (float)1.0),
                        false);

  if (_uv != NULL) {
    _glState.addGLFeature(new TextureCoordsGLFeature(_uv,
                                                     2,
                                                     0,
                                                     false,
                                                     0,
                                                     false, Vector2D::zero(), Vector2D::zero()) ,
                          false);
  }
}

void SGGeometryNode::rawRender(const G3MRenderContext* rc, const GLState* glState) {
  GL* gl = rc->getGL();
  gl->drawElements(_primitive, _indices, glState, *rc->getGPUProgramManager());
}
