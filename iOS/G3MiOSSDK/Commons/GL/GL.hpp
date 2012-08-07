//
//  GL.hpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 14/06/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_GL_hpp
#define G3MiOSSDK_GL_hpp

#include "INativeGL.hpp"

#include "IImage.hpp"
#include "MutableMatrix44D.hpp"
#include "Color.hpp"
#include "MutableVector2D.hpp"

#include "INativeGL.hpp"

#include <list>


class GL {
private:
  
  INativeGL* const _gl;
  
  MutableMatrix44D            _modelView;
  
  // stack of ModelView matrices
  std::list<MutableMatrix44D> _matrixStack;
  
  std::list<int>              _texturesIdBag;
  long                        _texturesIdCounter;
  
  // state handling
  bool _enableTextures;
  bool _enableTexture2D;
  bool _enableVertexColor;
  bool _enableVertexNormal;
  bool _enableVerticesPosition;
  bool _enableFlatColor;
  bool _enableDepthTest;
  bool _enableBlend;
  
  bool _enableCullFace;
  
#ifdef C_CODE
  GLCullFace _cullFace_face;
#else
  GLCullFace _cullFace_face = GLCullFace.Back;
#endif
  
  
  
  inline void loadModelView();
  
  int getTextureID();
  
public:
  
  GL(INativeGL* const gl) :
  _gl(gl),
  _enableTextures(false),
  _enableTexture2D(false),
  _enableVertexColor(false),
  _enableVertexNormal(false),
  _enableVerticesPosition(false),
  _enableFlatColor(false),
  _enableBlend(false),
  _enableDepthTest(false),
  _enableCullFace(false),
#ifdef C_CODE
  _cullFace_face(Back),
#else
  _cullFace_face(GLCullFace.Back),
#endif
  _texturesIdCounter(0)
  {
    
  }
  
  ~GL(){
#ifdef C_CODE
    delete _gl;
#endif
  }
  
  void enableVerticesPosition();
  
  void enableTextures();
  
  void verticesColors(bool v);
  
  void enableTexture2D();
  
  void enableVertexFlatColor(float r, float g, float b, float a,
                             float intensity);
  
  void disableVertexFlatColor();
  
  void disableTexture2D();
  
  void disableVerticesPosition();
  
  void disableTextures();
  
  void clearScreen(float r, float g, float b, float a);
  
  void color(float r, float g, float b, float a);
  
  void enableVertexColor(float const colors[], float intensity);
  
  void disableVertexColor();
  
  void enableVertexNormal(float const normals[]);
  
  void disableVertexNormal();
  
  void pushMatrix();
  
  void popMatrix();
  
  void loadMatrixf(const MutableMatrix44D &m);
  
  void multMatrixf(const MutableMatrix44D &m);
  
  void vertexPointer(int size, int stride, const float vertex[]);
  
  void drawTriangleStrip(int n, const int i[]) ;
  
  void drawLines(int n, const int i[]);
  
  void drawLineLoop(int n, const int i[]);
  
  void drawPoints(int n, const int i[]);
  
  void setProjection(const MutableMatrix44D &projection);
  
  void useProgram(unsigned int program);
  
  void enablePolygonOffset(float factor, float units);
  
  void disablePolygonOffset();
  
  void lineWidth(float width);
  
  void pointSize(float size);
  
  GLError getError();
  
  int uploadTexture(const IImage* image, int textureWidth, int textureHeight);
  
  void setTextureCoordinates(int size, int stride, const float texcoord[]);
  
  void bindTexture(unsigned int n);
  
  void enableDepthTest();
  void disableDepthTest();
  
  void enableBlend();
  void disableBlend();
  
  void drawBillBoard(const unsigned int textureId,
                     const Vector3D& pos,
                     const float viewPortRatio);
  
  void deleteTexture(int glTextureId);
  
  void enableCullFace(GLCullFace face);
  void disableCullFace();
  
  void transformTexCoords(const Vector2D& scale, const Vector2D& translation);
  
  void color(const Color& col) {
    color(col.getRed(),
          col.getGreen(),
          col.getBlue(),
          col.getAlpha());
  }
  
  void clearScreen(const Color& col){
    clearScreen(col.getRed(),
                col.getGreen(),
                col.getBlue(),
                col.getAlpha());
  }
  
  void enableVertexFlatColor(const Color& c, float intensity) {
    enableVertexFlatColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), intensity);
  }
  
};

#endif
