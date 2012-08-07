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
  long                        _texturesIdAllocationCounter;
  long                        _texturesIdGetCounter;
  long                        _texturesIdTakeCounter;
  
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
  GLCullFace _cullFace_face;
  
  float _scaleX;
  float _scaleY;
  float _translationX;
  float _translationY;
  
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
  _cullFace_face(Back),
  _texturesIdAllocationCounter(0),
  _scaleX(1),
  _scaleY(1),
  _translationX(0),
  _translationY(0),
  _texturesIdGetCounter(0),
  _texturesIdTakeCounter(0)
  {
    
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
  
  int getError();
  
  int uploadTexture(const IImage* image,
                    int textureWidth, int textureHeight);
  
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
  
  void transformTexCoords(float scaleX,
                          float scaleY,
                          float translationX,
                          float translationY);
  
  void transformTexCoords(double scaleX,
                          double scaleY,
                          double translationX,
                          double translationY) {
    transformTexCoords((float) scaleX,
                       (float) scaleY,
                       (float) translationX,
                       (float) translationY);
  }

  void transformTexCoords(const Vector2D& scale,
                          const Vector2D& translation) {
    transformTexCoords((float) scale.x(),
                       (float) scale.y(),
                       (float) translation.x(),
                       (float) translation.y());
  }

  void transformTexCoords(const MutableVector2D& scale,
                          const MutableVector2D& translation) {
    transformTexCoords((float) scale.x(),
                       (float) scale.y(),
                       (float) translation.x(),
                       (float) translation.y());
  }


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
