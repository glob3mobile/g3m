//
//  Mark.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "Mark.hpp"
#include "Camera.hpp"
#include "GL.hpp"
#include "TexturesHandler.hpp"
#include "TextureBuilder.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "IGLTextureId.hpp"
#include "IDownloader.hpp"
#include "IImageDownloadListener.hpp"
#include "MarkTouchListener.hpp"
#include "ITextUtils.hpp"
#include "IImageListener.hpp"

#include "GPUProgramState.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"

class MarkLabelImageListener : public IImageListener {
private:
  IImage* _iconImage;
  Mark*   _mark;
  
public:
  MarkLabelImageListener(IImage* iconImage,
                         Mark* mark) :
  _iconImage(iconImage),
  _mark(mark)
  {
    
  }
  
  void imageCreated(IImage* image) {
    if (_iconImage != NULL) {
      IFactory::instance()->deleteImage(_iconImage);
      _iconImage = NULL;
    }
    
    if (image == NULL) {
      _mark->onTextureDownloadError();
    }
    else {
      _mark->onTextureDownload(image);
    }
  }
};



class IconDownloadListener : public IImageDownloadListener {
private:
  Mark*              _mark;
  const std::string  _label;
  const bool         _labelBottom;
  const float        _labelFontSize;
  const Color*       _labelFontColor;
  const Color*       _labelShadowColor;
  const int          _labelGapSize;
  
public:
  IconDownloadListener(Mark* mark,
                       const std::string& label,
                       bool  labelBottom,
                       const float labelFontSize,
                       const Color* labelFontColor,
                       const Color* labelShadowColor,
                       const int labelGapSize) :
  _mark(mark),
  _label(label),
  _labelBottom(labelBottom),
  _labelFontSize(labelFontSize),
  _labelFontColor(labelFontColor),
  _labelShadowColor(labelShadowColor),
  _labelGapSize(labelGapSize)
  {
    
  }
  
  void onDownload(const URL& url,
                  IImage* image,
                  bool expired) {
    const bool hasLabel = ( _label.length() != 0 );
    
    if (hasLabel) {
#ifdef C_CODE
      LabelPosition labelPosition = _labelBottom ? Bottom : Right;
#endif
#ifdef JAVA_CODE
      LabelPosition labelPosition = _labelBottom ? LabelPosition.Bottom : LabelPosition.Right;
#endif
      
      ITextUtils::instance()->labelImage(image,
                                         _label,
                                         labelPosition,
                                         _labelGapSize,
                                         _labelFontSize,
                                         _labelFontColor,
                                         _labelShadowColor,
                                         new MarkLabelImageListener(image, _mark),
                                         true);
      //      ITextUtils::instance()->labelImage(image,
      //                                         _label,
      //                                         labelPosition,
      //                                         new MarkLabelImageListener(image, _mark),
      //                                         true);
    }
    else {
      _mark->onTextureDownload(image);
    }
  }
  
  void onError(const URL& url) {
    ILogger::instance()->logError("Error trying to download image \"%s\"", url.getPath().c_str());
    _mark->onTextureDownloadError();
  }
  
  void onCancel(const URL& url) {
    // ILogger::instance()->logError("Download canceled for image \"%s\"", url.getPath().c_str());
    _mark->onTextureDownloadError();
  }
  
  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired) {
    // do nothing
  }
};


IFloatBuffer* Mark::_billboardTexCoord = NULL;


Mark::Mark(const std::string& label,
           const URL          iconURL,
           const Geodetic3D&  position,
           double             minDistanceToCamera,
           const bool         labelBottom,
           const float        labelFontSize,
           const Color*       labelFontColor,
           const Color*       labelShadowColor,
           const int          labelGapSize,
           MarkUserData*      userData,
           bool               autoDeleteUserData,
           MarkTouchListener* listener,
           bool               autoDeleteListener) :
_label(label),
_iconURL(iconURL),
_position(position),
_labelBottom(labelBottom),
_labelFontSize(labelFontSize),
_labelFontColor(labelFontColor),
_labelShadowColor(labelShadowColor),
_labelGapSize(labelGapSize),
_textureId(NULL),
_cartesianPosition(NULL),
_vertices(NULL),
_textureSolved(false),
_textureImage(NULL),
_renderedMark(false),
_textureWidth(0),
_textureHeight(0),
_userData(userData),
_autoDeleteUserData(autoDeleteUserData),
_minDistanceToCamera(minDistanceToCamera),
_listener(listener),
_autoDeleteListener(autoDeleteListener),
_imageID( iconURL.getPath() + "_" + label ),
_planet(NULL)
{
  
}

Mark::Mark(const std::string& label,
           const Geodetic3D&  position,
           double             minDistanceToCamera,
           const float        labelFontSize,
           const Color*       labelFontColor,
           const Color*       labelShadowColor,
           MarkUserData*      userData,
           bool               autoDeleteUserData,
           MarkTouchListener* listener,
           bool               autoDeleteListener) :
_label(label),
_labelBottom(true),
_iconURL("", false),
_position(position),
_labelFontSize(labelFontSize),
_labelFontColor(labelFontColor),
_labelShadowColor(labelShadowColor),
_labelGapSize(2),
_textureId(NULL),
_cartesianPosition(NULL),
_vertices(NULL),
_textureSolved(false),
_textureImage(NULL),
_renderedMark(false),
_textureWidth(0),
_textureHeight(0),
_userData(userData),
_autoDeleteUserData(autoDeleteUserData),
_minDistanceToCamera(minDistanceToCamera),
_listener(listener),
_autoDeleteListener(autoDeleteListener),
_imageID( "_" + label ),
_planet(NULL)
{
  
}

Mark::Mark(const URL          iconURL,
           const Geodetic3D&  position,
           double             minDistanceToCamera,
           MarkUserData*      userData,
           bool               autoDeleteUserData,
           MarkTouchListener* listener,
           bool               autoDeleteListener) :
_label(""),
_labelBottom(true),
_iconURL(iconURL),
_position(position),
_labelFontSize(20),
_labelFontColor(Color::newFromRGBA(1, 1, 1, 1)),
_labelShadowColor(Color::newFromRGBA(0, 0, 0, 1)),
_labelGapSize(2),
_textureId(NULL),
_cartesianPosition(NULL),
_vertices(NULL),
_textureSolved(false),
_textureImage(NULL),
_renderedMark(false),
_textureWidth(0),
_textureHeight(0),
_userData(userData),
_autoDeleteUserData(autoDeleteUserData),
_minDistanceToCamera(minDistanceToCamera),
_listener(listener),
_autoDeleteListener(autoDeleteListener),
_imageID( iconURL.getPath() + "_" ),
_planet(NULL)
{
  
}

Mark::Mark(IImage*            image,
           const std::string& imageID,
           const Geodetic3D&  position,
           double             minDistanceToCamera,
           MarkUserData*      userData,
           bool               autoDeleteUserData,
           MarkTouchListener* listener,
           bool               autoDeleteListener) :
_label(""),
_labelBottom(true),
_iconURL(URL("", false)),
_position(position),
_labelFontSize(20),
_labelFontColor(NULL),
_labelShadowColor(NULL),
_labelGapSize(2),
_textureId(NULL),
_cartesianPosition(NULL),
_vertices(NULL),
_textureSolved(true),
_textureImage(image),
_renderedMark(false),
_textureWidth(image->getWidth()),
_textureHeight(image->getHeight()),
_userData(userData),
_autoDeleteUserData(autoDeleteUserData),
_minDistanceToCamera(minDistanceToCamera),
_listener(listener),
_autoDeleteListener(autoDeleteListener),
_imageID( imageID ),
_planet(NULL)
{
  
}

void Mark::initialize(const G3MContext* context,
                      long long downloadPriority) {
  
  _planet = context->getPlanet();
  
  if (!_textureSolved) {
    const bool hasLabel   = ( _label.length()             != 0 );
    const bool hasIconURL = ( _iconURL.getPath().length() != 0 );
    
    if (hasIconURL) {
      IDownloader* downloader = context->getDownloader();
      
      downloader->requestImage(_iconURL,
                               downloadPriority,
                               TimeInterval::fromDays(30),
                               true,
                               new IconDownloadListener(this,
                                                        _label,
                                                        _labelBottom,
                                                        _labelFontSize,
                                                        _labelFontColor,
                                                        _labelShadowColor,
                                                        _labelGapSize),
                               true);
    }
    else {
      if (hasLabel) {
        ITextUtils::instance()->createLabelImage(_label,
                                                 _labelFontSize,
                                                 _labelFontColor,
                                                 _labelShadowColor,
                                                 new MarkLabelImageListener(NULL, this),
                                                 true);
      }
      else {
        ILogger::instance()->logWarning("Marker created without label nor icon");
      }
    }
  }
}

void Mark::onTextureDownloadError() {
  _textureSolved = true;
  
  delete _labelFontColor;
  delete _labelShadowColor;
  
  ILogger::instance()->logError("Can't create texture for Mark (iconURL=\"%s\", label=\"%s\")",
                                _iconURL.getPath().c_str(),
                                _label.c_str());
}

void Mark::onTextureDownload(IImage* image) {
  _textureSolved = true;
  
  delete _labelFontColor;
  delete _labelShadowColor;
  //  _textureImage = image->shallowCopy();
  _textureImage = image;
  _textureWidth = _textureImage->getWidth();
  _textureHeight = _textureImage->getHeight();
  //  IFactory::instance()->deleteImage(image);
}

bool Mark::isReady() const {
  return _textureSolved;
}

Mark::~Mark() {
  delete _cartesianPosition;
  delete _vertices;
  if (_autoDeleteListener) {
    delete _listener;
  }
  if (_autoDeleteUserData) {
    delete _userData;
  }
  if (_textureImage != NULL) {
    IFactory::instance()->deleteImage(_textureImage);
  }
}

Vector3D* Mark::getCartesianPosition(const Planet* planet) {
  if (_cartesianPosition == NULL) {
    _cartesianPosition = new Vector3D( planet->toCartesian(_position) );
  }
  return _cartesianPosition;
}

IFloatBuffer* Mark::getVertices(const Planet* planet) {
  if (_vertices == NULL) {
    const Vector3D* pos = getCartesianPosition(planet);
    
    FloatBufferBuilderFromCartesian3D vertex(CenterStrategy::noCenter(), Vector3D::zero());
    vertex.add(*pos);
    vertex.add(*pos);
    vertex.add(*pos);
    vertex.add(*pos);
    
    _vertices = vertex.create();
  }
  return _vertices;
}

void Mark::render(const G3MRenderContext* rc,
                  const Vector3D& cameraPosition) {
  const Planet* planet = rc->getPlanet();
  
  const Vector3D* markPosition = getCartesianPosition(planet);
  
  const Vector3D markCameraVector = markPosition->sub(cameraPosition);
  
  
  // mark will be renderered only if is renderable by distance and placed on a visible globe area
  bool renderableByDistance;
  if (_minDistanceToCamera == 0) {
    renderableByDistance = true;
  }
  else {
    const double squaredDistanceToCamera = markCameraVector.squaredLength();
    renderableByDistance = ( squaredDistanceToCamera <= (_minDistanceToCamera * _minDistanceToCamera) );
  }
  
  _renderedMark = false;
  if (renderableByDistance) {
    const Vector3D normalAtMarkPosition = planet->geodeticSurfaceNormal(*markPosition);
    
    if (normalAtMarkPosition.angleBetween(markCameraVector)._radians > IMathUtils::instance()->halfPi()) {
      
      if (_textureId == NULL) {
        if (_textureImage != NULL) {
          _textureId = rc->getTexturesHandler()->getGLTextureId(_textureImage,
                                                                GLFormat::rgba(),
                                                                _imageID,
                                                                false);
          
          rc->getFactory()->deleteImage(_textureImage);
          _textureImage = NULL;
          
          _viewportWidth = rc->getCurrentCamera()->getWidth();
          _viewportHeight = rc->getCurrentCamera()->getHeight();
          actualizeGLGlobalState(rc->getCurrentCamera()); //Ready for rendering
        }
      } else{
        if (rc->getCurrentCamera()->getWidth() != _viewportWidth ||
            rc->getCurrentCamera()->getHeight() != _viewportHeight){
          _viewportWidth = rc->getCurrentCamera()->getWidth();
          _viewportHeight = rc->getCurrentCamera()->getHeight();
          actualizeGLGlobalState(rc->getCurrentCamera()); //Ready for rendering
        }
      }
      
      if (_textureId != NULL) {
        GL* gl = rc->getGL();
        
        
        //        GLGlobalState state(parentState);
        //        state.bindTexture(_textureId);
        
        GPUProgramManager& progManager = *rc->getGPUProgramManager();
        
        gl->drawArrays(GLPrimitive::triangleStrip(),
                       0,
                       4,
                       _GLGlobalState,
                       progManager,
                       &_progState);
        
        _renderedMark = true;
      }
    }
  }
}

bool Mark::touched() {
  return (_listener == NULL) ? false : _listener->touchedMark(this);
  //  if (_listener == NULL) {
  //    return false;
  //  }
  //  return _listener->touchedMark(this);
}

void Mark::setMinDistanceToCamera(double minDistanceToCamera) {
  _minDistanceToCamera = minDistanceToCamera;
}

double Mark::getMinDistanceToCamera() {
  return _minDistanceToCamera;
}

//void Mark::getGLGlobalStateAndGPUProgramState(GLGlobalState** GLGlobalState, GPUProgramState** progState){
//  _progState.clear();
////  (*GLGlobalState) = &_GLGlobalState;
////  (*progState) = &_progState;
//}

void Mark::modifyGLGlobalState(GLGlobalState& GLGlobalState) const{
  GLGlobalState.disableDepthTest();
  GLGlobalState.enableBlend();
  GLGlobalState.setBlendFactors(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
  GLGlobalState.bindTexture(_textureId);
}

void Mark::modifyGPUProgramState(GPUProgramState& progState) const{
  if (_planet == NULL){
    ILogger::instance()->logError("Planet NULL");
  } else{
    
    progState.setAttributeEnabled("Position", true);
    progState.setAttributeEnabled("TextureCoord", true);
    
    if (_billboardTexCoord == NULL){
      FloatBufferBuilderFromCartesian2D texCoor;
      texCoor.add(1,1);
      texCoor.add(1,0);
      texCoor.add(0,1);
      texCoor.add(0,0);
      _billboardTexCoord = texCoor.create();
    }
    
    progState.setAttributeValue("TextureCoord",
                                _billboardTexCoord, 2,
                                2,
                                0,
                                false,
                                0);
    
    const Vector3D* pos = new Vector3D( _planet->toCartesian(_position) );
    FloatBufferBuilderFromCartesian3D vertex(CenterStrategy::noCenter(), Vector3D::zero());
    vertex.add(*pos);
    vertex.add(*pos);
    vertex.add(*pos);
    vertex.add(*pos);
    
    IFloatBuffer* vertices = vertex.create();
    
    progState.setAttributeValue("Position",
                                vertices, 4, //The attribute is a float vector of 4 elements
                                3,            //Our buffer contains elements of 3
                                0,            //Index 0
                                false,        //Not normalized
                                0);           //Stride 0
    
    progState.setUniformValue("TextureExtent", Vector2D(_textureWidth, _textureHeight));
    progState.setUniformValue("ViewPortExtent", Vector2D( (double)_viewportWidth, (double)_viewportHeight ));
  }
}

bool Mark::isVisible(const G3MRenderContext* rc){
  _renderedMark = false;
  
  _viewportWidth = rc->getCurrentCamera()->getWidth();
  _viewportHeight = rc->getCurrentCamera()->getHeight();
  if (_viewportHeight < 2 || _viewportWidth < 2){
    //ILogger::instance()->logInfo("Viewport has not been set yet.");
    return false;
  }
  
  const Planet* planet = rc->getPlanet();
  
  const Vector3D* markPosition = getCartesianPosition(planet);
  
  const Vector3D markCameraVector = markPosition->sub(rc->getCurrentCamera()->getCartesianPosition());
  
  // mark will be renderered only if is renderable by distance and placed on a visible globe area
  bool renderableByDistance;
  if (_minDistanceToCamera == 0) {
    renderableByDistance = true;
  }
  else {
    const double squaredDistanceToCamera = markCameraVector.squaredLength();
    renderableByDistance = ( squaredDistanceToCamera <= (_minDistanceToCamera * _minDistanceToCamera) );
  }
  
  _renderedMark = false;
  if (renderableByDistance) {
    const Vector3D normalAtMarkPosition = planet->geodeticSurfaceNormal(*markPosition);
    if (normalAtMarkPosition.angleBetween(markCameraVector)._radians > IMathUtils::instance()->halfPi()) {
      return true;
    }
  }
  
  //Checking with frustum
  
  _renderedMark = rc->getCurrentCamera()->getFrustumInModelCoordinates()->contains(*_cartesianPosition);
  return _renderedMark;
}

void Mark::rawRender(const G3MRenderContext* rc, GLStateTreeNode* myStateTreeNode){
  //  printf("RENDERING SGMARK\n");
  
  if (_textureId == NULL) {
    if (_textureImage != NULL) {
      _textureId = rc->getTexturesHandler()->getGLTextureId(_textureImage,
                                                            GLFormat::rgba(),
                                                            _imageID,
                                                            false);
      
      rc->getFactory()->deleteImage(_textureImage);
      _textureImage = NULL;
      
      _viewportWidth = rc->getCurrentCamera()->getWidth();
      _viewportHeight = rc->getCurrentCamera()->getHeight();
//      actualizeGLGlobalState(rc->getCurrentCamera()); //Ready for rendering
    }
  } 
  
  if (rc->getCurrentCamera()->getWidth() != _viewportWidth ||
      rc->getCurrentCamera()->getHeight() != _viewportHeight){
    _viewportWidth = rc->getCurrentCamera()->getWidth();
    _viewportHeight = rc->getCurrentCamera()->getHeight();
    //actualizeGLGlobalState(rc->getCurrentCamera()); //Ready for rendering
  }
  
  if (_textureId != NULL) {
    _planet = rc->getPlanet();
    
    GL* gl = rc->getGL();
    
    GPUProgramManager& progManager = *rc->getGPUProgramManager();
    
    GLState* glState = myStateTreeNode->getGLState();
    
    gl->drawArrays(GLPrimitive::triangleStrip(),
                   0,
                   4,
                   glState,
                   progManager);
  }
  
}

void Mark::modifiyGLState(GLState* state){
  
  GLGlobalState *globalState = state->getGLGlobalState();
  
  globalState->disableDepthTest();
  globalState->enableBlend();
  globalState->setBlendFactors(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
  globalState->bindTexture(_textureId);
  
  GPUProgramState* progState = state->getGPUProgramState();
  
  if (_planet == NULL){
    ILogger::instance()->logError("Planet NULL");
  } else{
    
    progState->setAttributeEnabled("Position", true);
    progState->setAttributeEnabled("TextureCoord", true);
    
    if (_billboardTexCoord == NULL){
      FloatBufferBuilderFromCartesian2D texCoor;
      texCoor.add(1,1);
      texCoor.add(1,0);
      texCoor.add(0,1);
      texCoor.add(0,0);
      _billboardTexCoord = texCoor.create();
    }
    
    progState->setAttributeValue("TextureCoord",
                                 _billboardTexCoord, 2,
                                 2,
                                 0,
                                 false,
                                 0);
    
    const Vector3D* pos = new Vector3D( _planet->toCartesian(_position) );
    FloatBufferBuilderFromCartesian3D vertex(CenterStrategy::noCenter(), Vector3D::zero());
    vertex.add(*pos);
    vertex.add(*pos);
    vertex.add(*pos);
    vertex.add(*pos);
    
    IFloatBuffer* vertices = vertex.create();
    
    progState->setAttributeValue("Position",
                                 vertices, 4, //The attribute is a float vector of 4 elements
                                 3,            //Our buffer contains elements of 3
                                 0,            //Index 0
                                 false,        //Not normalized
                                 0);           //Stride 0
    
    progState->setUniformValue("TextureExtent", Vector2D(_textureWidth, _textureHeight));
    progState->setUniformValue("ViewPortExtent", Vector2D( (double)_viewportWidth, (double)_viewportHeight ));
  }
  
}

void Mark::onInitialize(const G3MContext* context) {
  
  _planet = context->getPlanet();
  
  if (!_textureSolved) {
    const bool hasLabel   = ( _label.length()             != 0 );
    const bool hasIconURL = ( _iconURL.getPath().length() != 0 );
    
    if (hasIconURL) {
      IDownloader* downloader = context->getDownloader();
      
      int downloadPriority = 100;
      
      downloader->requestImage(_iconURL,
                               downloadPriority,
                               TimeInterval::fromDays(30),
                               true,
                               new IconDownloadListener(this,
                                                        _label,
                                                        _labelBottom,
                                                        _labelFontSize,
                                                        _labelFontColor,
                                                        _labelShadowColor,
                                                        _labelGapSize),
                               true);
    }
    else {
      if (hasLabel) {
        ITextUtils::instance()->createLabelImage(_label,
                                                 _labelFontSize,
                                                 _labelFontColor,
                                                 _labelShadowColor,
                                                 new MarkLabelImageListener(NULL, this),
                                                 true);
      }
      else {
        ILogger::instance()->logWarning("Marker created without label nor icon");
      }
    }
  }
}