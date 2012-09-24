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

#include "IFloatBuffer.hpp"

#include "GLConstants.hpp"

#include <list>

class IGLProgramId;
class IGLUniformID;

#include "IGLTextureId.hpp"

class GL {
private:
  
  INativeGL* const _gl;
  
  MutableMatrix44D            _modelView;
  
  // stack of ModelView matrices
  std::list<MutableMatrix44D> _matrixStack;
  
  std::list<const IGLTextureId*>      _texturesIdBag;
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
  
  int _cullFace_face;

  
  
  
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
  
  const IGLTextureId* getGLTextureId();
  
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
  int checkedGetAttribLocation(IGLProgramId* program, const std::string& name);
  IGLUniformID* checkedGetUniformLocation(IGLProgramId* program, const std::string& name);
  
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
  _cullFace_face(GLCullFace::back()),
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
    //Init Constants
    GLCullFace::init(gl);
    GLBufferType::init(gl);
    GLFeature::init(gl);
    GLType::init(gl);
    GLPrimitive::init(gl);
    GLBlendFactor::init(gl);
    GLTextureType::init(gl);
    GLTextureParameter::init(gl);
    GLTextureParameterValue::init(gl);
    GLAlignment::init(gl);
    GLFormat::init(gl);
    GLVariable::init(gl);
    GLError::init(gl);
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
  
  bool useProgram(IGLProgramId* program);
  
  void enablePolygonOffset(float factor, float units);
  
  void disablePolygonOffset();
  
  void lineWidth(float width);
  
  void pointSize(float size);
  
  int getError();
  
  const IGLTextureId* uploadTexture(const IImage* image, int format, bool generateMipmap);
  
  //  const const GLTextureId*uploadTexture(const IImage* image,
  //                                  int textureWidth, int textureHeight,
  //                                  bool generateMipmap);
  
  void setTextureCoordinates(int size,
                             int stride,
                             IFloatBuffer* texcoord);
  
  void bindTexture(const IGLTextureId* textureId);
  
  void enableDepthTest();
  void disableDepthTest();
  
  void enableBlend();
  void disableBlend();
  
  void drawBillBoard(const IGLTextureId* textureId,
                     IFloatBuffer* vertices,
                     const float viewPortRatio);
  
  void deleteTexture(const IGLTextureId* textureId);
  
  void enableCullFace(int face);
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
    _gl->getIntegerv(GLVariable::viewport(), v);
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
