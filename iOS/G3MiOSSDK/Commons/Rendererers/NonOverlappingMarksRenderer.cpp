//
//  NonOverlappingMarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/1/15.
//
//

#include "NonOverlappingMarksRenderer.hpp"

#include "Camera.hpp"
#include "Planet.hpp"
#include "GLState.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"

#include "Context.hpp"
#include "TexturesHandler.hpp"
#include "Camera.hpp"
#include "Vector3D.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "DirectMesh.hpp"
#include "TexturedMesh.hpp"
#include "HUDPosition.hpp"
#include "HUDSize.hpp"
#include "RenderState.hpp"
#include "IImageBuilder.hpp"
#include "IImageBuilderListener.hpp"
#include "SimpleTextureMapping.hpp"
#include "MultiTextureMapping.hpp"
#include "TextureIDReference.hpp"

NonOverlappingMark::NonOverlappingMark(IImageBuilder* imageBuilder, Geodetic3D& position, float springLengthInPixels):
_imageBuilder(imageBuilder),
_geoPosition(position),
_springLengthInPixels(springLengthInPixels),
_anchorScreenPos(NULL),
_screenPos(NULL),
_cartesianPos(NULL),
_dX(0),
_dY(0),
_glState(new GLState()),
_image(NULL)
{
  
}

Vector3D NonOverlappingMark::getCartesianPosition(const Planet* planet) const{
  if (_cartesianPos == NULL){
    _cartesianPos = new Vector3D(planet->toCartesian(_geoPosition));
  }
  return *_cartesianPos;
}

void NonOverlappingMark::computeScreenPos(const Camera* cam, const Planet* planet){
  if (_screenPos != NULL){
    delete _screenPos;
  }
  
  _screenPos = new Vector2F(cam->point2Pixel(getCartesianPosition(planet)));
}


void NonOverlappingMark::applyCoulombsLaw(const NonOverlappingMark* that){ //EM
  
}

void NonOverlappingMark::applyHookesLaw(const NonOverlappingMark* that){   //Spring
  
}

/*

Mesh* NonOverlappingMark::createMesh(const G3MRenderContext* rc) {
  if (_image == NULL) {
    return NULL;
  }
  
  const bool hasBackground = (_backgroundImageBuilder != NULL);
  
  if (hasBackground && (_backgroundImage == NULL)) {
    return NULL;
  }
  
  TexturesHandler* texturesHandler = rc->getTexturesHandler();
  
  const TextureIDReference* textureID = texturesHandler->getTextureIDReference(_image,
                                                                               GLFormat::rgba(),
                                                                               _imageName,
                                                                               false);
  if (textureID == NULL) {
    rc->getLogger()->logError("Can't upload texture to GPU");
    return NULL;
  }
  
#ifdef C_CODE
  const TextureIDReference* backgroundTextureID = NULL;
#endif
#ifdef JAVA_CODE
  TextureIDReference backgroundTextureID = null;
#endif
  if (hasBackground) {
    backgroundTextureID = texturesHandler->getTextureIDReference(_backgroundImage,
                                                                 GLFormat::rgba(),
                                                                 _backgroundImageName,
                                                                 false);
    
    if (backgroundTextureID == NULL) {
      delete textureID;
      
      rc->getLogger()->logError("Can't background upload texture to GPU");
      return NULL;
    }
  }
  
  const Camera* camera = rc->getCurrentCamera();
  const int viewPortWidth  = camera->getViewPortWidth();
  const int viewPortHeight = camera->getViewPortHeight();
  
  const float width  = _widthSize->getSize(viewPortWidth, viewPortHeight, _imageWidth, _imageHeight);
  const float height = _heightSize->getSize(viewPortWidth, viewPortHeight, _imageWidth, _imageHeight);
  
  const float x = _xPosition->getPosition(viewPortWidth, viewPortHeight, width, height);
  const float y = _yPosition->getPosition(viewPortWidth, viewPortHeight, width, height);
  
  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithoutCenter();
  vertices->add( x,       height+y, 0 );
  vertices->add( x,       y,        0 );
  vertices->add( width+x, height+y, 0 );
  vertices->add( width+x, y,        0 );
  
  FloatBufferBuilderFromCartesian2D texCoords;
  texCoords.add( 0, 0 );
  texCoords.add( 0, 1 );
  texCoords.add( 1, 0 );
  texCoords.add( 1, 1 );
  
  DirectMesh* dm = new DirectMesh(GLPrimitive::triangleStrip(),
                                  true,
                                  vertices->getCenter(),
                                  vertices->create(),
                                  1,
                                  1);
  
  delete vertices;
  
  if (hasBackground) {
    _textureMapping = new MultiTextureMapping(textureID,
                                              texCoords.create(),
                                              true,
                                              true,
                                              backgroundTextureID,
                                              texCoords.create(),
                                              true,
                                              true,
                                              _texCoordsTranslationU,
                                              _texCoordsTranslationV,
                                              _texCoordsScaleU,
                                              _texCoordsScaleV,
                                              _texCoordsRotationInRadians,
                                              _texCoordsRotationCenterU,
                                              _texCoordsRotationCenterV);
  }
  else {
    _textureMapping = new SimpleTextureMapping(textureID,
                                               texCoords.create(),
                                               true,
                                               true,
                                               _texCoordsTranslationU,
                                               _texCoordsTranslationV,
                                               _texCoordsScaleU,
                                               _texCoordsScaleV,
                                               _texCoordsRotationInRadians,
                                               _texCoordsRotationCenterU,
                                               _texCoordsRotationCenterV);
  }
  
  return new TexturedMesh(dm, true, _textureMapping, true, true);
}


void NonOverlappingMark::render(const G3MRenderContext* rc, GLState* glState){

}

*/


void NonOverlappingMark::render(const G3MRenderContext* rc, GLState* glState){
  
  //  (IFloatBuffer* buffer,
  //   int arrayElementSize,
  //   int index,
  //   bool normalized,
  //   int stride,
  //   float lineWidth,
  //   bool needsPointSize,
  //   float pointSize) :
  
  
  //_glState->setParent(glState);
  
  if (_glState->getNumberOfGLFeatures() == 0){
    
    FloatBufferBuilderFromCartesian2D pos2D;
//    pos2D.add(1,1);
//    pos2D.add(1,0);
//    pos2D.add(0,1);
//    pos2D.add(0,0);
    
    
    pos2D.add( 0.0f, 0.0f); //vertex 1
    pos2D.add( 0.0f, 1.0f); //vertex 2
    pos2D.add( 1.0f, 0.0f); //vertex 3
    pos2D.add( 1.0f, 1.0f); //vertex 4
    
    
    _glState->addGLFeature(new Geometry2DGLFeature(pos2D.create(),
                                                   2,
                                                   0,
                                                   true,
                                                   0,
                                                   1.0,
                                                   true,
                                                   10.0),
                           false);
  }
  
  rc->getGL()->drawArrays(GLPrimitive::triangleStrip(), 0, 4, _glState, *(rc->getGPUProgramManager()));
  
}

#pragma-mark Renderer

NonOverlappingMarksRenderer::NonOverlappingMarksRenderer(int maxVisibleMarks):
_maxVisibleMarks(maxVisibleMarks)
{
  
}


void NonOverlappingMarksRenderer::addMark(NonOverlappingMark* mark){
  _marks.push_back(mark);
  
}

std::vector<NonOverlappingMark*> NonOverlappingMarksRenderer::getMarksToBeRendered(const Camera* cam, const Planet* planet) const{
  
  std::vector<NonOverlappingMark*> out;
  
  for (int i = 0; i < _marks.size(); i++) {
    NonOverlappingMark* m = _marks[i];
    
    if (cam->getFrustumInModelCoordinates()->contains(m->getCartesianPosition(planet)) ){
      out.push_back(m);
      if (out.size() == _maxVisibleMarks){
        break;
      }
    }
  }
  
  return out;
}

void NonOverlappingMarksRenderer::render(const G3MRenderContext* rc, GLState* glState){
  
  std::vector<NonOverlappingMark*> visibleMarks = getMarksToBeRendered(rc->getCurrentCamera(), rc->getPlanet());
  
  //Compute Mark Positions
  
  //Draw Lines
  
  //Draw Anchors
  
  //Draw Marks
  for (int i = 0; i < _marks.size(); i++) {
    _marks[i]->render(rc, glState);
  }
  
  
}