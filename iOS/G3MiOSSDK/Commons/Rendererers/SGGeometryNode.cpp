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
#include "Vector3D.hpp"
#include "Vector2F.hpp"


SGGeometryNode::SGGeometryNode(const std::string& id,
                               const std::string& sId,
                               int                primitive,
                               IFloatBuffer*      vertices,
                               IFloatBuffer*      colors,
                               IFloatBuffer*      uv,
                               IFloatBuffer*      normals,
                               IShortBuffer*      indices) :
SGNode(id, sId),
_primitive(primitive),
_vertices(vertices),
_colors(colors),
_uv(uv),
_normals(normals),
_indices(indices),
_glState(new GLState())
{
  createGLState();
  
  // compute boundingBox
  float xmin=1e10, ymin=1e10, zmin=1e10, xmax=-1e10, ymax=-1e10, zmax=-1e10;
  int verticesCount = vertices->size() / 3;
  for (int i = 0; i < verticesCount*3; i+=3) {
    float x = vertices->get(i);
    float y = vertices->get(i+1);
    float z = vertices->get(i+2);
    if (x<xmin) xmin = x;
    if (y<ymin) ymin = y;
    if (z<zmin) zmin = z;
    if (x>xmax) xmax = x;
    if (y>ymax) ymax = y;
    if (z>zmax) zmax = z;
  }
  const Vector3D lower = Vector3D(xmin, ymin, zmin);
  const Vector3D upper = Vector3D(xmax, ymax, zmax);
  _boundingBox = new Box(lower, upper);
}



SGGeometryNode::~SGGeometryNode() {
  delete _vertices;
  delete _colors;
  delete _uv;
  delete _normals;
  delete _indices;

  _glState->_release();
  
  delete _boundingBox;

#ifdef JAVA_CODE
  super.dispose();
#endif

}

void SGGeometryNode::createGLState() {

  _glState->addGLFeature(new GeometryGLFeature(_vertices,    //The attribute is a float vector of 4 elements
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

  if (_normals != NULL) {

    //    _glState->addGLFeature(new DirectionLightGLFeature(Vector3D(1, 0,0),  Color::yellow(),
    //                                                      (float)0.0), false);

    _glState->addGLFeature(new VertexNormalGLFeature(_normals,3,0,false,0),
                           false);


  }

  if (_uv != NULL) {
    _glState->addGLFeature(new TextureCoordsGLFeature(_uv,
                                                      2,
                                                      0,
                                                      false,
                                                      0,
                                                      false,
                                                      Vector2F::zero(),
                                                      Vector2F::zero()) ,
                           false);
  }
}

void SGGeometryNode::rawRender(const G3MRenderContext* rc, const GLState* glState) {
  GL* gl = rc->getGL();
  gl->drawElements(_primitive, _indices, glState, *rc->getGPUProgramManager(), REGULAR_RENDER);
}

void SGGeometryNode::zRawRender(const G3MRenderContext* rc,
                        const GLState* parentState){
  GL* gl = rc->getGL();
  gl->drawElements(_primitive, _indices, parentState, *rc->getGPUProgramManager(), Z_BUFFER_RENDER);
}

const GLState* SGGeometryNode::createZRenderState(const G3MRenderContext* rc,
                                          const GLState* parentState) {
  GLState* state = new GLState();
  state->setParent(parentState);

  state->addGLFeature(new GeometryGLFeature(_vertices,    //The attribute is a float vector of 4 elements
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

  //TODO: TEXTURES
//  if (_uv != NULL) {
//    _glState->addGLFeature(new TextureCoordsGLFeature(_uv,
//                                                      2,
//                                                      0,
//                                                      false,
//                                                      0,
//                                                      false, Vector2D::zero(), Vector2D::zero()) ,
//                           false);
//  }

  return state;
}
