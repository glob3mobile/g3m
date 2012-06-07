//
//  DummyRenderer.cpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#include "DummyRenderer.hpp"
#include "GLU.hpp"



DummyRenderer::~DummyRenderer()
{
  delete[] index;
  delete[] vertices;
}

void DummyRenderer::initialize(const InitializationContext* ic)
{
  int res = 12;
  vertices = new float[res * res * 3];
  numIndices = 2 * (res - 1) * (res + 1);
  index = new unsigned char[numIndices];
  
  // create vertices
  halfSize = 7e6;
  
  int n = 0;
  for (int j = 0; j < res; j++) {
    for (int i = 0; i < res; i++) {
      vertices[n++] = (float) 0;
      vertices[n++] = (float) (-halfSize + i / (float) (res - 1) * 2*halfSize);
      vertices[n++] = (float) (halfSize - j / (float) (res - 1) * 2*halfSize);
    }
  }
  
  n = 0;
  for (int j = 0; j < res - 1; j++) {
    if (j > 0) index[n++] = (char) (j * res);
    for (int i = 0; i < res; i++) {
      index[n++] = (char) (j * res + i);
      index[n++] = (char) (j * res + i + res);
    }
    index[n++] = (char) (j * res + 2 * res - 1);
  }
}  


bool DummyRenderer::onTouchEvent(const TouchEvent* touchEvent){
  return false;
}

int DummyRenderer::render(const RenderContext* rc)
{
  
  // obtaing gl object reference
  IGL *gl = rc->getGL();
  
  // insert pointers
  gl->disableTextures();
  gl->vertexPointer(3, 0, vertices);
 
  {
    // draw a red square
    gl->color((float) 1, (float) 0, (float) 0);
    gl->pushMatrix();
    MutableMatrix44D T = GLU::translationMatrix(Vector3D(halfSize,0,0));
    gl->multMatrixf(T);
    gl->drawTriangleStrip(numIndices, index);
    gl->popMatrix();
  }
  
  {
    // draw a green square
    gl->color((float) 0, (float) 1, (float) 0);
    gl->pushMatrix();
    MutableMatrix44D T = GLU::translationMatrix(Vector3D(0,halfSize,0));
    MutableMatrix44D R = GLU::rotationMatrix(Angle::fromDegrees(90), Vector3D(0,0,1));
    gl->multMatrixf(T.multMatrix(R));
    gl->drawTriangleStrip(numIndices, index);
    gl->popMatrix();
  }
  
  {
    // draw a blue square
    gl->color((float) 0, (float) 0, (float) 1);
    gl->pushMatrix();
    MutableMatrix44D T = GLU::translationMatrix(Vector3D(0,-halfSize,0));
    MutableMatrix44D R = GLU::rotationMatrix(Angle::fromDegrees(-90), Vector3D(0,0,1));
    gl->multMatrixf(T.multMatrix(R));
    gl->drawTriangleStrip(numIndices, index);
    gl->popMatrix();
  }
  
  {
    // draw a purple square
    gl->color((float) 1, (float) 0, (float) 1);
    gl->pushMatrix();
    MutableMatrix44D T = GLU::translationMatrix(Vector3D(0,0,-halfSize));
    MutableMatrix44D R = GLU::rotationMatrix(Angle::fromDegrees(90), Vector3D(0,1,0));
    gl->multMatrixf(T.multMatrix(R));
    gl->drawTriangleStrip(numIndices, index);
    gl->popMatrix();
  }
    
  {
    // draw a cian square
    gl->color((float) 0, (float) 1, (float) 1);
    gl->pushMatrix();
    MutableMatrix44D T = GLU::translationMatrix(Vector3D(0,0,halfSize));
    MutableMatrix44D R = GLU::rotationMatrix(Angle::fromDegrees(-90), Vector3D(0,1,0));
    gl->multMatrixf(T.multMatrix(R));
    gl->drawTriangleStrip(numIndices, index);
    gl->popMatrix();
  }
  
  {
    // draw a grey square
    gl->color((float) 0.5, (float) 0.5, (float) 0.5);
    gl->pushMatrix();
    MutableMatrix44D T = GLU::translationMatrix(Vector3D(-halfSize,0,0));
    MutableMatrix44D R = GLU::rotationMatrix(Angle::fromDegrees(180), Vector3D(0,0,1));
    gl->multMatrixf(T.multMatrix(R));
    gl->drawTriangleStrip(numIndices, index);
    gl->popMatrix();
  }
  
  gl->enableTextures();
  
  return 9999;
}
