//
//  DummyRenderer.cpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#include "DummyRenderer.hpp"

#include "Context.hpp"
#include "GL.hpp"
#include "Planet.hpp"
#include "Vector3D.hpp"

DummyRenderer::~DummyRenderer()
{
  delete[] _index;
  delete[] _vertices;
}

void DummyRenderer::initialize(const InitializationContext* ic)
{
  int res = 12;
  _vertices = new float[res * res * 3];
  _numIndices = 2 * (res - 1) * (res + 1);
  _index = new int[_numIndices];
  
  // create vertices
  
  if (ic != NULL && ic->getPlanet() != NULL)
    _halfSize = ic->getPlanet()->getRadii().x() / 2.0;
  else     
    _halfSize = 7e6;
  
  int n = 0;
  for (int j = 0; j < res; j++) {
    for (int i = 0; i < res; i++) {
      _vertices[n++] = (float) 0;
      _vertices[n++] = (float) (-_halfSize + i / (float) (res - 1) * 2*_halfSize);
      _vertices[n++] = (float) (_halfSize - j / (float) (res - 1) * 2*_halfSize);
    }
  }
  
  n = 0;
  for (int j = 0; j < res - 1; j++) {
    if (j > 0) _index[n++] = (char) (j * res);
    for (int i = 0; i < res; i++) {
      _index[n++] = (j * res + i);
      _index[n++] = (j * res + i + res);
    }
    _index[n++] = (j * res + 2 * res - 1);
  }
}  


bool DummyRenderer::onTouchEvent(const EventContext* ec,
                                 const TouchEvent* touchEvent){
  return false;
}

void DummyRenderer::render(const RenderContext* rc) {
  
  // obtaing gl object reference
  GL *gl = rc->getGL();
  
  gl->enableVerticesPosition();
  
  // insert pointers
  gl->disableTextures();
  gl->vertexPointer(3, 0, _vertices);
 
  {
    // draw a red square
    gl->color((float) 1, (float) 0, (float) 0, 1);
    gl->pushMatrix();
    //MutableMatrix44D T = GLU::translationMatrix(Vector3D(halfSize,0,0));
    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(_halfSize,0,0));
    gl->multMatrixf(T);
    gl->drawTriangleStrip(_numIndices, _index);
    gl->popMatrix();
  }
  
  {
    // draw a green square
    gl->color((float) 0, (float) 1, (float) 0, 1);
    gl->pushMatrix();
    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(0,_halfSize,0));
    MutableMatrix44D R = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(90), Vector3D(0,0,1));
    gl->multMatrixf(T.multiply(R));
    gl->drawTriangleStrip(_numIndices, _index);
    gl->popMatrix();
  }
  
  {
    // draw a blue square
    gl->color((float) 0, (float) 0, (float) 1, 1);
    gl->pushMatrix();
    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(0,-_halfSize,0));
    MutableMatrix44D R = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(-90), Vector3D(0,0,1));
    gl->multMatrixf(T.multiply(R));
    gl->drawTriangleStrip(_numIndices, _index);
    gl->popMatrix();
  }
  
  {
    // draw a purple square
    gl->color((float) 1, (float) 0, (float) 1, 1);
    gl->pushMatrix();
    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(0,0,-_halfSize));
    MutableMatrix44D R = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(90), Vector3D(0,1,0));
    gl->multMatrixf(T.multiply(R));
    gl->drawTriangleStrip(_numIndices, _index);
    gl->popMatrix();
  }
    
  {
    // draw a cian square
    gl->color((float) 0, (float) 1, (float) 1, 1);
    gl->pushMatrix();
    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(0,0,_halfSize));
    MutableMatrix44D R = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(-90), Vector3D(0,1,0));
    gl->multMatrixf(T.multiply(R));
    gl->drawTriangleStrip(_numIndices, _index);
    gl->popMatrix();
  }
  
  {
    // draw a grey square
    gl->color((float) 0.5, (float) 0.5, (float) 0.5, 1);
    gl->pushMatrix();
    MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(Vector3D(-_halfSize,0,0));
    MutableMatrix44D R = MutableMatrix44D::createRotationMatrix(Angle::fromDegrees(180), Vector3D(0,0,1));
    gl->multMatrixf(T.multiply(R));
    gl->drawTriangleStrip(_numIndices, _index);
    gl->popMatrix();
  }
  
  gl->enableTextures();
  
}
