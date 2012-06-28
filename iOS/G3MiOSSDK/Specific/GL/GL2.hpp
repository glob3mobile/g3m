//
//  GL2.hpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 14/06/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#include "IGL.hpp"

#include "MutableMatrix44D.hpp"

class GL2: public IGL {
  
public:
  
  void enableVertices() ;
  
  void enableTextures() ;
  
  void enableTexture2D() ;
  
  void disableTexture2D() ;
  
  void disableVertices() ;
  
  void disableTextures() ;
  
  void clearScreen(float r, float g, float b, float a) ;
  
  void color(float r, float g, float b, float a) ;
  
  void pushMatrix() ;
  
  void popMatrix() ;
  
  void loadMatrixf(const MutableMatrix44D &m) ;
  
  void multMatrixf(const MutableMatrix44D &m) ;
  
  void vertexPointer(int size, int stride, const float vertex[]) ;
  
  void drawTriangleStrip(int n, const unsigned char *i) ;
  
  void drawLines(int n, const unsigned char *i); 
  
  void drawLineLoop(int n, const unsigned char *i); 
  
  void setProjection(const MutableMatrix44D &projection) ;
  
  void useProgram(unsigned int program) ;
  
  void enablePolygonOffset(float factor, float units) ;
  
  void disablePolygonOffset() ;
  
  void lineWidth(float width);
  
  int getError();
  
  int uploadTexture(const IImage& image, int textureWidth, int textureHeight);
  
  void setTextureCoordinates(int size, int stride, const float texcoord[]);
  
  void bindTexture (unsigned int n);
  
  void depthTest(bool b);
  
  void blend(bool b);
  
  void drawBillBoard(const unsigned int textureId,
                     const Vector3D& pos,
                     const float viewPortRatio);
  
  void deleteTexture(int glTextureId);
  
  void cullFace(bool b, CullFace face);
  
private:
  MutableMatrix44D            _modelView;

  // stack of ModelView matrices
  std::list<MutableMatrix44D> _matrixStack;
};

