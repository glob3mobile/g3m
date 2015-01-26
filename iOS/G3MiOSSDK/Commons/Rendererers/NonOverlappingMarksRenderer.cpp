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

#pragma mark MarkWidget

MarkWidget::MarkWidget(const TextureIDReference* texID,
                       float width, float height,
                       float viewportWidth, float viewportHeight){
  
  _glState = new GLState();
  
  width /= 2.0;
  height /= 2.0;
    
    FloatBufferBuilderFromCartesian2D pos2D;
    pos2D.add( -width, -height); //vertex 1
    pos2D.add( -width, height); //vertex 2
    pos2D.add( width, -height); //vertex 3
    pos2D.add( width, height); //vertex 4
  
  _geo2Dfeature = new Geometry2DGLFeature(pos2D.create(),
                                          2,
                                          0,
                                          true,
                                          0,
                                          1.0,
                                          true,
                                          10.0,
                                          Vector2F(0.0,0.0));
  
    _glState->addGLFeature(_geo2Dfeature,
                           false);
  
    FloatBufferBuilderFromCartesian2D texCoords;
    texCoords.add( 0.0f, 1.0f); //vertex 1
    texCoords.add( 0.0f, 0.0f); //vertex 2
    texCoords.add( 1.0f, 1.0f); //vertex 3
    texCoords.add( 1.0f, 0.0f); //vertex 4
    
    SimpleTextureMapping* textureMapping = new SimpleTextureMapping(texID,
                                                                    texCoords.create(),
                                                                    true,
                                                                    true);
  
  _glState->addGLFeature(new ViewportExtentGLFeature((int)viewportWidth, (int)viewportHeight), false);
    
    textureMapping->modifyGLState(*_glState);
}

void MarkWidget::render(const G3MRenderContext *rc, GLState *glState){
  rc->getGL()->drawArrays(GLPrimitive::triangleStrip(), 0, 4, _glState, *(rc->getGPUProgramManager()));
}

void MarkWidget::setScreenPos(float x, float y){
  _geo2Dfeature->setTranslation(x, y);
}

#pragma mark NonOverlappingMark

NonOverlappingMark::NonOverlappingMark(IImageBuilder* imageBuilder, const Geodetic3D& position, float springLengthInPixels):
_imageBuilder(imageBuilder),
_geoPosition(position),
_springLengthInPixels(springLengthInPixels),
_anchorScreenPos(NULL),
_screenPos(NULL),
_cartesianPos(NULL),
_dX(0),
_dY(0),
_glState(new GLState()),
_image(NULL),
_widget(NULL)
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

void NonOverlappingMark::render(const G3MRenderContext* rc, GLState* glState){
  
  _imageBuilder->build(rc, new NonOverlappingMark::NonOverlappingMarkImageListener(this), true);
  if (_image == NULL){
    return;
  }
  
  if (_widget == NULL){
    const TextureIDReference* textureID = rc->getTexturesHandler()->getTextureIDReference(_image,
                                                                                          GLFormat::rgba(),
                                                                                          _imageName,
                                                                                          false);
    _widget = new MarkWidget(textureID,
                             _image->getWidth(), _image->getHeight(),
                             rc->getCurrentCamera()->getViewPortWidth(), rc->getCurrentCamera()->getViewPortHeight());
  }
  
  computeScreenPos(rc->getCurrentCamera(), rc->getPlanet());
  
  _widget->setScreenPos(_screenPos->_x, _screenPos->_y);
  //printf("%f, %f\n", _screenPos->_x, _screenPos->_y);
  
  _widget->render(rc, glState);
  

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