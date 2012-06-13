//
//  IGL.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IGL_h
#define G3MiOSSDK_IGL_h

#include <vector>

#include "IImage.hpp"

#include "MutableMatrix44D.hpp"


class IGL {
public:
  
  virtual ~IGL() {};
  
  virtual void enableVertices() = 0;
  
  virtual void enableTextures() = 0;
  
  virtual void enableTexture2D() = 0;
  
  virtual void disableTexture2D() = 0;
  
  virtual void disableVertices() = 0;
  
  virtual void disableTextures() = 0;
  
  virtual void clearScreen(float r, float g, float b) = 0;
  
  virtual void color(float r, float g, float b) = 0;
  
  virtual void pushMatrix() = 0;
  
  virtual void popMatrix() = 0;
  
  virtual void loadMatrixf(const MutableMatrix44D &m) = 0;
  
  virtual void multMatrixf(const MutableMatrix44D &m) = 0;
  
  virtual void vertexPointer(int size, int stride, const float vertex[]) = 0;
  
  virtual void drawTriangleStrip(int n, unsigned char i[]) = 0;

  virtual void setProjection(const MutableMatrix44D &projection) = 0;
  
  virtual void useProgram(unsigned int program) = 0;
  
  virtual void enablePolygonOffset(float factor, float units) = 0;
  
  virtual void disablePolygonOffset() = 0;
  
  virtual void getError() = 0;
  
  virtual int uploadTexture(const IImage& image, int widthTexture, int heightTexture) = 0;
  
  virtual void setTextureCoordinates(int size, int stride, const float texcoord[]) = 0;
  
  virtual void bindTexture(unsigned int n) = 0;

};


#endif
