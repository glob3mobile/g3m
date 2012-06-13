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
  
   void drawTriangleStrip(int n, unsigned char *i) ;
  
   void drawLines(int n, unsigned char *i); 
   
   void drawLineLoop(int n, unsigned char *i); 
  
   void setProjection(const MutableMatrix44D &projection) ;
  
   void useProgram(unsigned int program) ;
  
   void enablePolygonOffset(float factor, float units) ;
  
   void disablePolygonOffset() ;
  
   void lineWidth(float width);

  void getError();
  
  int uploadTexture(const IImage& image, int widthTexture, int heightTexture);
  
  void setTextureCoordinates(int size, int stride, const float texcoord[]);
  
  void bindTexture (unsigned int n);
  
  
private:
    // stack of ModelView matrices
    MutableMatrix44D _modelView;
    std::list<MutableMatrix44D> _matrixStack;
};

