//
//  MarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "MarksRenderer.hpp"
#include "Camera.hpp"
#include "GL.hpp"
#include "TouchEvent.hpp"
#include "RectangleI.hpp"
#include "Mark.hpp"
#include "MarkTouchListener.hpp"
#include "DownloadPriority.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"

#include "GPUProgram.hpp"
#include "GPUProgramManager.hpp"

void MarksRenderer::setMarkTouchListener(MarkTouchListener* markTouchListener,
                                         bool autoDelete) {
  if ( _autoDeleteMarkTouchListener ) {
    delete _markTouchListener;
  }
  
  _markTouchListener = markTouchListener;
  _autoDeleteMarkTouchListener = autoDelete;
}

MarksRenderer::MarksRenderer(bool readyWhenMarksReady) :
_readyWhenMarksReady(readyWhenMarksReady),
_context(NULL),
_lastCamera(NULL),
_markTouchListener(NULL),
_autoDeleteMarkTouchListener(false),
_downloadPriority(DownloadPriority::MEDIUM),
_programState(NULL)
{
}


MarksRenderer::~MarksRenderer() {
  int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    delete _marks[i];
  }
  
  if ( _autoDeleteMarkTouchListener ) {
    delete _markTouchListener;
  }
  _markTouchListener = NULL;
  
  if (_billboardTexCoord != NULL){
    delete _billboardTexCoord;
  }
};


void MarksRenderer::initialize(const G3MContext* context) {
  _context = context;
  
  int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    Mark* mark = _marks[i];
    mark->initialize(context, _downloadPriority);
  }
  
  //Creating program state
  _programState.setAttributeEnabled("Position", true);
  _programState.setUniformValue("BillBoard", true);
  _programState.setAttributeEnabled("TextureCoord", true);
  _programState.setUniformValue("EnableTexture", true);
  
  FloatBufferBuilderFromCartesian2D texCoor;
  texCoor.add(1,1);
  texCoor.add(1,0);
  texCoor.add(0,1);
  texCoor.add(0,0);
  _billboardTexCoord = texCoor.create();
  
  _programState.setAttributeValue("TextureCoord",
                                  _billboardTexCoord, 2,
                                  2,
                                  0,
                                  false,
                                  0);
  
  
  
  _programState.setUniformValue("TranslationTexCoord", Vector2D(0.0, 0.0));
  _programState.setUniformValue("ScaleTexCoord", Vector2D(1.0, 1.0));
  _programState.setUniformValue("ColorPerVertexIntensity", (float) 0.0);
  _programState.setUniformValue("EnableColorPerVertex", false);
  _programState.setUniformValue("EnableFlatColor", false);
  _programState.setUniformValue("FlatColor", (float) 0.0, (float) 0.0, (float) 0.0, (float) 0.0);
  _programState.setUniformValue("FlatColorIntensity", (float) 0.0);
  _programState.setUniformValue("PointSize", (float) 0.0);
}

void MarksRenderer::addMark(Mark* mark) {
  _marks.push_back(mark);
  if (_context != NULL) {
    mark->initialize(_context, _downloadPriority);
  }
}

void MarksRenderer::removeMark(Mark* mark){
  int pos = -1;
  const int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    if (_marks[i] == mark) {
      pos = i;
      break;
    }
  }
  if (pos != -1) {
#ifdef C_CODE
    _marks.erase(_marks.begin() + pos);
#endif
#ifdef JAVA_CODE
    _marks.remove(pos);
#endif
  }
}

void MarksRenderer::removeAllMarks() {
  const int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    delete _marks[i];
  }
  _marks.clear();
}

bool MarksRenderer::onTouchEvent(const G3MEventContext* ec,
                                 const TouchEvent* touchEvent) {
  
  bool handled = false;
  
  //  if ( (touchEvent->getType() == Down) && (touchEvent->getTouchCount() == 1) ) {
  if ( touchEvent->getType() == DownUp ) {
    
    if (_lastCamera != NULL) {
      const Vector2I touchedPixel = touchEvent->getTouch(0)->getPos();
      const Planet* planet = ec->getPlanet();
      
      double minSqDistance = IMathUtils::instance()->maxDouble();
      Mark* nearestMark = NULL;
      
      const int marksSize = _marks.size();
      for (int i = 0; i < marksSize; i++) {
        Mark* mark = _marks[i];
        
        if (!mark->isReady()) {
          continue;
        }
        if (!mark->isRendered()) {
          continue;
        }
        
        const int textureWidth = mark->getTextureWidth();
        if (textureWidth <= 0) {
          continue;
        }
        
        const int textureHeight = mark->getTextureHeight();
        if (textureHeight <= 0) {
          continue;
        }
        
        const Vector3D* cartesianMarkPosition = mark->getCartesianPosition(planet);
        const Vector2I markPixel = _lastCamera->point2Pixel(*cartesianMarkPosition);
        
        const RectangleI markPixelBounds(markPixel._x - (textureWidth / 2),
                                         markPixel._y - (textureHeight / 2),
                                         textureWidth,
                                         textureHeight);
        
        if (markPixelBounds.contains(touchedPixel._x, touchedPixel._y)) {
          const double distance = markPixel.sub(touchedPixel).squaredLength();
          if (distance < minSqDistance) {
            nearestMark = mark;
            minSqDistance = distance;
          }
        }
      }
      
      if (nearestMark != NULL) {
        handled = nearestMark->touched();
        if (!handled) {
          if (_markTouchListener != NULL) {
            handled = _markTouchListener->touchedMark(nearestMark);
          }
        }
      }
    }
  }
  
  return handled;
}

bool MarksRenderer::isReadyToRender(const G3MRenderContext* rc) {
  if (_readyWhenMarksReady) {
    int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++) {
      if (!_marks[i]->isReady()) {
        return false;
      }
    }
  }
  
  return true;
}

void MarksRenderer::render(const G3MRenderContext* rc,
                           const GLState& parentState) {
  //  rc.getLogger()->logInfo("MarksRenderer::render()");
  
  // Saving camera for use in onTouchEvent
  _lastCamera = rc->getCurrentCamera();
  
  
//  GL* gl = rc->getGL();
  
  GLState state(parentState);
  state.disableDepthTest();
  state.enableBlend();

  //  state.enableTextures();
  
  //  GPUProgram* prog = rc->getGPUProgramManager()->getProgram("DefaultProgram");
  int _WORKING_JM;
  //UniformBool* enableTexture = prog->getUniformBool("EnableTexture");
  //enableTexture->set(true);
  
  //  state.enableTexture2D();
  //state.enableVerticesPosition();
  
  
  
  Vector2D textureTranslation(0.0, 0.0);
  Vector2D textureScale(1.0, 1.0);
  
//  state.translateTextureCoordinates(textureTranslation);
//  state.scaleTextureCoordinates(textureScale);
//  
  state.setBlendFactors(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
  
  const Camera* camera = rc->getCurrentCamera();
  const Vector3D cameraPosition = camera->getCartesianPosition();
  
  //state.enableBillboarding();
  //  state.setViewportSize(camera->getWidth(),
  //                        camera->getHeight());
  
  _lastCamera->applyOnGPUProgramState(_programState);
  
  _programState.setUniformValue("ViewPortExtent", Vector2D( (double)camera->getWidth(), (double)camera->getHeight() ));
  
  
  
//  state.setTextureCoordinates(_billboardTexCoord, 2, 0);
  
  const int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    Mark* mark = _marks[i];
    //rc->getLogger()->logInfo("Rendering Mark: \"%s\"", mark->getName().c_str());
    
    if (mark->isReady()) {
      mark->render(rc, cameraPosition, state, &_programState);
    }
  }
  
//  int IS_A_HACK_;//???????
//  int _WORKING_JM2;
  //enableTexture->set(false); //DISABLING TEXTURES
}
