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

#include "IFloatBuffer.hpp"

#include <list>


class GL {
private:
  
  INativeGL* const _gl;
  
  MutableMatrix44D            _modelView;
  
  // stack of ModelView matrices
  std::list<MutableMatrix44D> _matrixStack;
  
  std::list<GLTextureId>      _texturesIdBag;
  long                        _texturesIdAllocationCounter;
  long                        _texturesIdGetCounter;
  long                        _texturesIdTakeCounter;
  
  // state handling
  bool _enableTextures;
  bool _enableTexture2D;
  bool _enableVertexColor;
  bool _enableVerticesPosition;
  bool _enableFlatColor;
  bool _enableDepthTest;
  bool _enableBlend;
  
  bool _enableCullFace;
  
#ifdef C_CODE
  GLCullFace _cullFace_face;
#endif
#ifdef JAVA_CODE
  GLCullFace _cullFace_face = GLCullFace.Back;
#endif
  
  
  
  float _scaleX;
  float _scaleY;
  float _translationX;
  float _translationY;
  
  IFloatBuffer* _vertices;
  IFloatBuffer* _textureCoordinates;
  IFloatBuffer* _colors;
  
  float _flatColorR;
  float _flatColorG;
  float _flatColorB;
  float _flatColorA;
  float _flatColorIntensity;
  
  inline void loadModelView();
  
  const GLTextureId getGLTextureId();
  
//  int _lastTextureWidth;
//  int _lastTextureHeight;
//#ifdef C_CODE
//  unsigned char* _lastImageData;
//#endif
//#ifdef JAVA_CODE
//  byte[] _lastImageData;
//#endif

  //Get Locations warning of errors
  bool _errorGettingLocationOcurred;
  int checkedGetAttribLocation(int program, const std::string& name);
  int checkedGetUniformLocation(int program, const std::string& name);
  
  IFloatBuffer* _billboardTexCoord;
  IFloatBuffer* getBillboardTexCoord();
  
public:
  
  GL(INativeGL* const gl) :
  _gl(gl),
  _enableTextures(false),
  _enableTexture2D(false),
  _enableVertexColor(false),
  _enableVerticesPosition(false),
//  _enableFlatColor(false),
  _enableBlend(false),
  _enableDepthTest(false),
  _enableCullFace(false),
#ifdef C_CODE
  _cullFace_face(Back),
#else
  _cullFace_face(GLCullFace.Back),
#endif
  _texturesIdAllocationCounter(0),
  _scaleX(1),
  _scaleY(1),
  _translationX(0),
  _translationY(0),
  _texturesIdGetCounter(0),
  _texturesIdTakeCounter(0),
  _vertices(NULL),
  _textureCoordinates(NULL),
  _colors(NULL),
  _flatColorR(0),
  _flatColorG(0),
  _flatColorB(0),
  _flatColorA(0),
  _flatColorIntensity(0),
  _billboardTexCoord(NULL)
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
  
  void enableVertexColor(IFloatBuffer* colors, float intensity);
  
  void disableVertexColor();
  
  void pushMatrix();
  
  void popMatrix();
  
  void loadMatrixf(const MutableMatrix44D &m);
  
  void multMatrixf(const MutableMatrix44D &m);
  
  void vertexPointer(int size, int stride, IFloatBuffer* vertices);
  
  void drawTriangleStrip(IIntBuffer* indices) ;
  
  void drawLines(IIntBuffer* indices);
  
  void drawLineLoop(IIntBuffer* indices);
  
  void drawPoints(IIntBuffer* indices);
  
  void setProjection(const MutableMatrix44D &projection);
  
  bool useProgram(unsigned int program);
  
  void enablePolygonOffset(float factor, float units);
  
  void disablePolygonOffset();
  
  void lineWidth(float width);
  
  void pointSize(float size);
  
  GLError getError();
  
  const GLTextureId uploadTexture(const IImage* image, GLFormat format, bool generateMipmap);
  
  //  const GLTextureId uploadTexture(const IImage* image,
  //                                  int textureWidth, int textureHeight,
  //                                  bool generateMipmap);
  
  void setTextureCoordinates(int size,
                             int stride,
                             IFloatBuffer* texcoord);
  
  void bindTexture(const GLTextureId& textureId);
  
  void enableDepthTest();
  void disableDepthTest();
  
  void enableBlend();
  void disableBlend();
  
  void drawBillBoard(const GLTextureId& textureId,
                     IFloatBuffer* vertices,
                     const float viewPortRatio);
  
  void deleteTexture(const GLTextureId& textureId);
  
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
  
  void setBlendFuncSrcAlpha();
  
  void getViewport(int v[]){
#ifdef C_CODE
    _gl->getIntegerv(Viewport, v);
#else
    _gl->getIntegerv(GLVariable.Viewport, v);
#endif
  }
  
  ~GL() {
#ifdef C_CODE
    delete _gl;
#endif
    
//    if (_lastImageData != NULL) {
//      delete [] _lastImageData;
//      _lastImageData = NULL;
//    }

    if (_vertices != NULL) {
      delete _vertices;
    }
    if (_textureCoordinates != NULL) {
      delete _textureCoordinates;
    }
    if (_colors != NULL) {
      delete _colors;
    }
     
  }
  
};

#endif
