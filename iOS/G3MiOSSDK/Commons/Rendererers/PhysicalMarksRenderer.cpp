//
//  PhysicalMarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/1/15.
//
//

//
//  PhysicalMarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "PhysicalMarksRenderer.hpp"
#include "RenderState.hpp"
#include "Camera.hpp"
#include "GL.hpp"
#include "TouchEvent.hpp"
#include "RectangleF.hpp"
#include "Mark.hpp"
#include "MarkTouchListener.hpp"
#include "DownloadPriority.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "GPUProgram.hpp"
#include "GPUProgramManager.hpp"
#include "Vector2F.hpp"
#include "LineShape.hpp"

void PhysicalMarksRenderer::setMarkTouchListener(MarkTouchListener* markTouchListener,
                                         bool autoDelete) {
  if ( _autoDeleteMarkTouchListener ) {
    delete _markTouchListener;
  }
  
  _markTouchListener = markTouchListener;
  _autoDeleteMarkTouchListener = autoDelete;
}

PhysicalMarksRenderer::PhysicalMarksRenderer(bool readyWhenMarksReady,
                                             ShapesRenderer *shapesRenderer) :
_readyWhenMarksReady(readyWhenMarksReady),
_shapesRenderer(shapesRenderer),
_lastCamera(NULL),
_markTouchListener(NULL),
_autoDeleteMarkTouchListener(false),
_downloadPriority(DownloadPriority::MEDIUM),
_glState(new GLState()),
_billboardTexCoords(NULL)
{
  _context = NULL;
}


PhysicalMarksRenderer::~PhysicalMarksRenderer() {
  int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    delete _marks[i];
  }
  
  if ( _autoDeleteMarkTouchListener ) {
    delete _markTouchListener;
  }
  _markTouchListener = NULL;
  
  _glState->_release();
  
  delete _billboardTexCoords;
  
#ifdef JAVA_CODE
  super.dispose();
#endif
  
};


void PhysicalMarksRenderer::onChangedContext() {
  int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    Mark* mark = _marks[i];
    mark->initialize(_context, _downloadPriority);
  }
}

void PhysicalMarksRenderer::addMark(Mark* mark) {
  _anchors.push_back(new Geodetic3D(mark->getPosition()));
  _pixels.push_back(NULL);
  _marks.push_back(mark);
  if (_context != NULL) {
    mark->initialize(_context, _downloadPriority);
  }
}

void PhysicalMarksRenderer::removeMark(Mark* mark) {
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

void PhysicalMarksRenderer::removeAllMarks() {
  const int marksSize = _marks.size();
  for (int i = 0; i < marksSize; i++) {
    delete _marks[i];
  }
  _marks.clear();
}

bool PhysicalMarksRenderer::onTouchEvent(const G3MEventContext* ec,
                                 const TouchEvent* touchEvent) {
  
  bool handled = false;
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
        const Vector2F markPixel = _lastCamera->point2Pixel(*cartesianMarkPosition);
        
        const RectangleF markPixelBounds(markPixel._x - (textureWidth / 2),
                                         markPixel._y - (textureHeight / 2),
                                         textureWidth,
                                         textureHeight);
        
        if (markPixelBounds.contains(touchedPixel._x, touchedPixel._y)) {
          const double sqDistance = markPixel.squaredDistanceTo(touchedPixel);
          if (sqDistance < minSqDistance) {
            nearestMark = mark;
            minSqDistance = sqDistance;
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

RenderState PhysicalMarksRenderer::getRenderState(const G3MRenderContext* rc) {
  if (_readyWhenMarksReady) {
    int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++) {
      if (!_marks[i]->isReady()) {
        return RenderState::busy();
      }
    }
  }
  
  return RenderState::ready();
}

IFloatBuffer* PhysicalMarksRenderer::getBillboardTexCoords() {
  if (_billboardTexCoords == NULL) {
    FloatBufferBuilderFromCartesian2D texCoor;
    texCoor.add(1,1);
    texCoor.add(1,0);
    texCoor.add(0,1);
    texCoor.add(0,0);
    _billboardTexCoords = texCoor.create();
  }
  return _billboardTexCoords;
}


//std::vector<Vector2F*> prev;

std::vector<Vector2F*> layoutMarksGraph(std::vector<Vector2F*> anchors,
                                        std::vector<Vector2F*> prevPos,
                                        Vector2F minCorner, Vector2F maxCorner)
{
  std::vector<float> posX;    std::vector<float> posY;
  std::vector<float> forceX;  std::vector<float> forceY;
  std::vector<float> velX;    std::vector<float> velY;
  
  // initialization
  
  float sumForces = 1e10;
  float prevSumForces = 2*sumForces;
  int iter = 0;
  float offsetMarginX = (maxCorner._x - minCorner._x) * 0.1;
  float offsetMarginY = (maxCorner._y - minCorner._y) * 0.1;
  float repFactor = 5e4 / anchors.size();
  float repMarginFactor = repFactor;
  float atrFactor = 0.5;
  float friction = 0.5;

  
  // compute centroid
  // OJO: habrá que mirar casos con más de uno
  float centerX = 0;
  float centerY = 0;
  for (int i=0; i<anchors.size(); i++) {
    centerX += anchors[i]->_x;
    centerY += anchors[i]->_y;
  }
  centerX /= anchors.size();
  centerY /= anchors.size();
  
  for (int i=0; i<anchors.size(); i++) {
/*    float vx = anchors[i]->_x - centerX;
    float vy = anchors[i]->_y - centerY;
    float mod = sqrt(vx*vx+vy*vy);
    posX.push_back(anchors[i]->_x + (maxCorner._x*0.2)*vx/mod);
    posY.push_back(anchors[i]->_y + (maxCorner._y*0.2)*vy/mod);
    if (posX[i]<64) posX[i]=64;
    if (posX[i]>maxCorner._x-64) posX[i]=maxCorner._x-64;
    if (posY[i]<64) posY[i]=64;
    if (posY[i]>maxCorner._y-64) posY[i]=maxCorner._y-64;*/
    
/*    if (prev.empty()) {
      posX.push_back(anchors[i]->_x);
      posY.push_back(anchors[i]->_y);
    } else {
      posX.push_back(prev[i]->_x);
      posY.push_back(prev[i]->_y);
    }*/
    
    
    if (prevPos[i]!=NULL) {
      posX.push_back(prevPos[i]->_x);
      posY.push_back(prevPos[i]->_y);
    } else {
      posX.push_back(anchors[i]->_x);
      posY.push_back(anchors[i]->_y);
    }
    
    forceX.push_back(0);              forceY.push_back(0);
    velX.push_back(0);                velY.push_back(0);
  }

  
  while (sumForces<prevSumForces && iter<200) {
 //while (sumForces>anchors.size() && iter<100) {
    iter++;
   //friction *= 0.09;
    //printf ("\nIteracion %d:\n", iter);
    prevSumForces = sumForces;
    sumForces = 0;
  
    // process velocities
    for (int i=0; i<anchors.size(); i++) {
      
      // compute atraction to anchors
      forceX[i] = atrFactor * (anchors[i]->_x-posX[i]);
      forceY[i] = atrFactor * (anchors[i]->_y-posY[i]);
      
      /*// compute repulsion to margins
      forceX[i] += repMarginFactor / (posX[i]-minCorner._x+offsetMarginX);
      forceX[i] += repMarginFactor / (posX[i]-maxCorner._x-offsetMarginX);
      forceY[i] += repMarginFactor / (posY[i]-minCorner._y+offsetMarginY);
      forceY[i] += repMarginFactor / (posY[i]-maxCorner._y-offsetMarginY);*/
      
      // compute repulsion to other marks
      for (int j=0; j<anchors.size(); j++) {
        if (i!=j) {
          float dist2 = (posX[j]-posX[i])*(posX[j]-posX[i])+(posY[j]-posY[i])*(posY[j]-posY[i]);
          if (dist2 < 1) dist2 = 1;
          forceX[i] += repFactor * (posX[i]-posX[j]) / dist2;
          forceY[i] += repFactor * (posY[i]-posY[j]) / dist2;
          if (isnan(forceX[i]))
            printf("isnan\n");
        }
      }
      //velX[i] = (velX[i]+forceX[i]) * friction;
      //velY[i] = (velY[i]+forceY[i]) * friction;
      
      sumForces += forceX[i]*forceX[i] + forceY[i]*forceY[i];
    }
    
    //printf("sumforces=%f\n", fabs(sumForces));
    
    // set new positions
    for (int i=0; i<anchors.size(); i++) {
      //posX[i] += velX[i];
      //posY[i] += velY[i];
      
//      posX[i] += friction * forceX[i];
//      posY[i] += friction * forceY[i];
//      if (posX[i]<64) posX[i]=64;
//      if (posX[i]>maxCorner._x-64) posX[i]=maxCorner._x-64;
//      if (posY[i]<64) posY[i]=64;
//      if (posY[i]>maxCorner._y-64) posY[i]=maxCorner._y-64;
      
      
      float modForce = sqrt(forceX[i]*forceX[i]+forceY[i]*forceY[i]);
      if (modForce>10) {
        forceX[i] *= 10/modForce;
        forceY[i] *= 10/modForce;
      }
      
      if (isnan(forceX[i]))
        printf("isnan\n");
 
      
      if (isnan(posX[i]) || isnan(posY[i]))
        printf("isnan!!\n");

      float newPosX = posX[i]+friction*forceX[i];
      float newPosY = posY[i]+friction*forceY[i];
      if (newPosX<64) {
        float f = (65-posX[i]) / forceX[i];
        if (isnan(f))
          printf("isnan\n");
        posX[i] = 65;
        posY[i] += f * forceY[i];
      } else {
        posX[i] += friction * forceX[i];
        posY[i] += friction * forceY[i];
      }

    
      if (isnan(posX[i]) || isnan(posY[i]))
        printf("isnan!!\n");
    }
  }
  
//  printf ("punto %d: (%.1f, %.1f). Force = (%.1f, %.1f)\n", i, posX[i], posY[i], forceX[i], forceY[i]);
  if (iter>20)
    printf ("Iters=%d.  Num=%d\n", iter, anchors.size());

  
  std::vector<Vector2F*> posOut;
  for (int i=0; i<anchors.size(); i++) {
    posOut.push_back(new Vector2F(posX[i], posY[i]));
  }
  
 /*
  if (!prev.empty()) {
    for (int i=0; i<anchors.size(); i++) {
      delete prev[i];
    }
  }
  prev.clear();
  for (int i=0; i<anchors.size(); i++) {
    prev.push_back(new Vector2F(*posOut[i]));
  }
  */
  
  
  return posOut;
}


void PhysicalMarksRenderer::render(const G3MRenderContext* rc, GLState* glState) {
  const int marksSize = _marks.size();
  
  if (marksSize > 0) {
    const Camera* camera = rc->getCurrentCamera();
    _lastCamera = camera; // Saving camera for use in onTouchEvent
    
    const MutableVector3D cameraPosition = camera->getCartesianPositionMutable();
    const double cameraHeight = camera->getGeodeticPosition()._height;
    
    updateGLState(rc);
    
    const Planet* planet = rc->getPlanet();
    GL* gl = rc->getGL();
    
    IFloatBuffer* billboardTexCoord = getBillboardTexCoords();
    
    _shapesRenderer->removeAllShapes();
    
    std::vector<Vector2F*> anchors;
    //std::vector<Vector2F*> floating;
    std::vector<Vector2F*> prevPos;
    
    for (int i = 0; i < marksSize; i++) {
      const Vector2F pixelAnchor = camera->point2Pixel(planet->toCartesian(*_anchors[i]));
      if (pixelAnchor._x>64 && pixelAnchor._x<camera->getViewPortWidth()-64 &&
          pixelAnchor._y>64 && pixelAnchor._y<camera->getViewPortHeight()-64) {
        anchors.push_back(new Vector2F(pixelAnchor));
        if (_pixels[i]!=NULL)
          prevPos.push_back(new Vector2F(*_pixels[i]));
        else
          prevPos.push_back(NULL);
        //Vector2F pixelFloating = camera->point2Pixel(planet->toCartesian(_marks[i]->getPosition()));
        //floating.push_back(new Vector2F(pixelFloating));
      }
    }
    
    Vector2F maxCorner = Vector2F(camera->getViewPortWidth(),
                                  camera->getViewPortHeight());
    std::vector<Vector2F*> pixels = layoutMarksGraph(anchors, prevPos, Vector2F(0,0), maxCorner);
                                                     
    
    int n=0;
    for (int i = 0; i < marksSize; i++) {
      Mark* mark = _marks[i];
      if (_pixels[i]!=NULL) {
        delete _pixels[i];
        _pixels[i] = NULL;
      }
      
      const Vector2F pixel = camera->point2Pixel(planet->toCartesian(*_anchors[i]));
      if (pixel._x>64 && pixel._x<camera->getViewPortWidth()-64 &&
          pixel._y>64 && pixel._y<camera->getViewPortHeight()-64) {
        
        const Geodetic2D point = planet->toGeodetic2D(camera->pixel2PlanetPoint(Vector2I((int)pixels[n]->_x,(int)pixels[n]->_y)));
        Shape* line = new LineShape(new Geodetic3D(_anchors[i]->_latitude, _anchors[i]->_longitude, cameraHeight/100),
                                    new Geodetic3D(point, cameraHeight/100),
                                    ABSOLUTE,
                                    5,
                                    Color::fromRGBA(0, 0, 0, 1));
        _shapesRenderer->addShape(line);
        mark->setPosition(Geodetic3D(point,0));
        
        _pixels[i] = new Vector2F(*pixels[n]);
        n++;
        
        if (mark->isReady()) {
          mark->render(rc,
                       cameraPosition,
                       cameraHeight,
                       _glState,
                       planet,
                       gl,
                       billboardTexCoord);
        }
      }
    }
    
    for (int i=0; i<pixels.size(); i++) {
      delete anchors[i];
      //delete floating[i];
      delete pixels[i];
      if (prevPos[i]!=NULL)
        delete prevPos[i];
    }
  }
}

void PhysicalMarksRenderer::updateGLState(const G3MRenderContext* rc) {
  const Camera* cam = rc->getCurrentCamera();
  
  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(cam), true);
  }
  else {
    f->setMatrix(cam->getModelViewMatrix44D());
  }
  
  if (_glState->getGLFeature(GLF_VIEWPORT_EXTENT) == NULL) {
    _glState->clearGLFeatureGroup(NO_GROUP);
    _glState->addGLFeature(new ViewportExtentGLFeature(cam->getViewPortWidth(), cam->getViewPortHeight()), false);
  }
}

void PhysicalMarksRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                          int width, int height) {
  _glState->clearGLFeatureGroup(NO_GROUP);
  _glState->addGLFeature(new ViewportExtentGLFeature(width, height), false);
}
