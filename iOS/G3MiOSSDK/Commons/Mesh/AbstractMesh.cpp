//
//  AbstractMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

#include "AbstractMesh.hpp"

#include "IFloatBuffer.hpp"
#include "Color.hpp"
#include "GL.hpp"
#include "Box.hpp"

//#include "GPUProgramState.hpp"
#include "Camera.hpp"

#include "GLFeature.hpp"

AbstractMesh::~AbstractMesh() {
  if (_owner) {
    delete _vertices;
    delete _colors;
    delete _flatColor;
  }

  delete _extent;
  delete _translationMatrix;
}

AbstractMesh::AbstractMesh(const int primitive,
                           bool owner,
                           const Vector3D& center,
                           IFloatBuffer* vertices,
                           float lineWidth,
                           float pointSize,
                           Color* flatColor,
                           IFloatBuffer* colors,
                           const float colorsIntensity,
                           bool depthTest) :
_primitive(primitive),
_owner(owner),
_vertices(vertices),
_flatColor(flatColor),
_colors(colors),
_colorsIntensity(colorsIntensity),
_extent(NULL),
_center(center),
_translationMatrix(( center.isNan() || center.isZero() )
                   ? NULL
                   : new MutableMatrix44D(MutableMatrix44D::createTranslationMatrix(center)) ),
_lineWidth(lineWidth),
_pointSize(pointSize),
_depthTest(depthTest)
{
  createGLState();
}

Extent* AbstractMesh::computeExtent() const {
  const int vertexCount = getVertexCount();

  if (vertexCount <= 0) {
    return NULL;
  }

  double minX = 1e12;
  double minY = 1e12;
  double minZ = 1e12;

  double maxX = -1e12;
  double maxY = -1e12;
  double maxZ = -1e12;

  for (int i=0; i < vertexCount; i++) {
    const int i3 = i * 3;

    const double x = _vertices->get(i3    ) + _center._x;
    const double y = _vertices->get(i3 + 1) + _center._y;
    const double z = _vertices->get(i3 + 2) + _center._z;

    if (x < minX) minX = x;
    if (x > maxX) maxX = x;

    if (y < minY) minY = y;
    if (y > maxY) maxY = y;

    if (z < minZ) minZ = z;
    if (z > maxZ) maxZ = z;
  }

  return new Box(Vector3D(minX, minY, minZ),
                 Vector3D(maxX, maxY, maxZ));
}

Extent* AbstractMesh::getExtent() const {
  if (_extent == NULL) {
    _extent = computeExtent();
  }
  return _extent;
}

const Vector3D AbstractMesh::getVertex(int i) const {
  const int p = i * 3;
  return Vector3D(_vertices->get(p  ) + _center._x,
                  _vertices->get(p+1) + _center._y,
                  _vertices->get(p+2) + _center._z);
}

int AbstractMesh::getVertexCount() const {
  return _vertices->size() / 3;
}

bool AbstractMesh::isTransparent(const G3MRenderContext* rc) const {
  if (_flatColor == NULL) {
    return false;
  }
  return _flatColor->isTransparent();
}


void AbstractMesh::render(const G3MRenderContext *rc) const {
  rawRender(rc);
}

void AbstractMesh::createGLState(){

//  GLGlobalState* globalState = _glState.getGLGlobalState();

//  globalState->setLineWidth(_lineWidth);
//  if (_depthTest){
//    globalState->enableDepthTest();
//  } else{
//    globalState->disableDepthTest();
//  }

//  if (_flatColor != NULL && _flatColor->isTransparent()){
//    globalState->enableBlend();
//    globalState->setBlendFactors(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
//  }

//  GPUProgramState& progState = *_glState.getGPUProgramState();



  _glState.addGLFeatureAndRelease(new GeometryGLFeature(_vertices,    //The attribute is a float vector of 4 elements
                                              3,            //Our buffer contains elements of 3
                                              0,            //Index 0
                                              false,        //Not normalized
                                              0,            //Stride 0
                                              true,         //Depth test
                                              false, 0,
                                              false, (float)0.0, (float)0.0,
                                              _lineWidth,
                                              true, _pointSize));   //POINT SIZE

  if (_translationMatrix != NULL){
    //progState.setUniformMatrixValue(MODELVIEW, *_translationMatrix, true);
//    _glState.setModelView(_translationMatrix->asMatrix44D(), true);

    _glState.addGLFeatureAndRelease(new ModelTransformGLFeature(_translationMatrix->asMatrix44D()));
  }

  if (_flatColor != NULL && _colors == NULL){  //FlatColorMesh Shader
//    progState.setAttributeValue(POSITION,
//                                _vertices, 4, //The attribute is a float vector of 4 elements
//                                3,            //Our buffer contains elements of 3
//                                0,            //Index 0
//                                false,        //Not normalized
//                                0);           //Stride 0
    //progState.setUniformValue(FLAT_COLOR, *_flatColor);

    _glState.addGLFeatureAndRelease(new FlatColorGLFeature(*_flatColor,
                                                           _flatColor->isTransparent(),
                                                           GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha()));




    return;
  }


//  progState.setUniformValue(POINT_SIZE, _pointSize);

//  progState.setAttributeValue(POSITION,
//                              _vertices, 4, //The attribute is a float vector of 4 elements
//                              3,            //Our buffer contains elements of 3
//                              0,            //Index 0
//                              false,        //Not normalized
//                              0);           //Stride 0

  if (_colors != NULL){
    //    progState.setUniformValue(EnableColorPerVertex, true);
    //    progState.setAttributeValue(COLOR,
    //                                _colors, 4,   //The attribute is a float vector of 4 elements RGBA
    //                                4,            //Our buffer contains elements of 4
    //                                0,            //Index 0
    //                                false,        //Not normalized
    //                                0);           //Stride 0

    _glState.addGLFeatureAndRelease(new ColorGLFeature(_colors,   //The attribute is a float vector of 4 elements RGBA
                                                       4,            //Our buffer contains elements of 4
                                                       0,            //Index 0
                                                       false,        //Not normalized
                                                       0,            //Stride 0
                                                       true, GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha()));

    //    progState.setUniformValue(ColorPerVertexIntensity, _colorsIntensity);
  } else{
    //    progState.setAttributeDisabled(COLOR);
    //    progState.setUniformValue(EnableColorPerVertex, false);
    //    progState.setUniformValue(ColorPerVertexIntensity, (float)0.0);
  }

  //  if (_flatColor != NULL){
  //    progState.setUniformValue(EnableFlatColor, true);
  //    progState.setUniformValue(FLAT_COLOR,
  //                              (double)_flatColor->getRed(),
  //                              (double)_flatColor->getGreen(),
  //                              (double) _flatColor->getBlue(),
  //                              (double) _flatColor->getAlpha());
  //
  //    progState.setUniformValue(FlatColorIntensity, _colorsIntensity);
  //  } else{
  //    progState.setUniformValue(EnableFlatColor, false);
  //    progState.setUniformValue(ColorPerVertexIntensity, (float)0.0);
  //    progState.setUniformValue(FLAT_COLOR, (float)0.0, (float)0.0, (float)0.0, (float)0.0);
  //    progState.setUniformValue(FlatColorIntensity, (float)0.0);
  //  }
}

void AbstractMesh::render(const G3MRenderContext* rc, const GLState* parentGLState) {
  
  _glState.setParent(parentGLState);
  rawRender(rc);
}
