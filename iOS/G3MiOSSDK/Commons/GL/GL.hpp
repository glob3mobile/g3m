//
//  GL.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_GL_hpp
#define G3MiOSSDK_GL_hpp

#include "MutableMatrix44D.hpp"

#include "IGL.hpp"

#include "INativeGL.hpp"

#include <list>

class GL {
private:
  
  const INativeGL*      _gl;      //NATIVE GL
  
  MutableMatrix44D            _modelView;
  
  // stack of ModelView matrices
  std::list<MutableMatrix44D> _matrixStack;
  
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
  CullFace _cullFace_face;
  
  inline void loadModelView();
  
public:
  
  GL(const INativeGL* gl) :
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
  _cullFace_face(BACK)
  {
    
  }
  
  void enableVerticesPosition() ;
  
  void enableTextures() ;
  
  void verticesColors(bool v);
  
  void enableTexture2D() ;
  
  void enableVertexFlatColor(float r, float g, float b, float a,
                             float intensity);
  
  void disableVertexFlatColor();
  
  void disableTexture2D() ;
  
  void disableVerticesPosition() ;
  
  void disableTextures() ;
  
  void clearScreen(float r, float g, float b, float a) ;
  
  void color(float r, float g, float b, float a) ;
  
  void enableVertexColor(float const colors[], float intensity);
  
  void disableVertexColor();
  
  void enableVertexNormal(float const normals[]);
  
  void disableVertexNormal();
  
  void pushMatrix();
  
  void popMatrix();
  
  void loadMatrixf(const MutableMatrix44D &m) ;
  
  void multMatrixf(const MutableMatrix44D &m) ;
  
  void vertexPointer(int size, int stride, const float vertex[]) ;
  
  void drawTriangleStrip(int n, const int i[]) ;
  
  void drawLines(int n, const int i[]); 
  
  void drawLineLoop(int n, const int i[]); 
  
  void drawPoints(int n, const int i[]);
  
  void setProjection(const MutableMatrix44D &projection) ;
  
  void useProgram(unsigned int program) ;
  
  void enablePolygonOffset(float factor, float units) ;
  
  void disablePolygonOffset() ;
  
  void lineWidth(float width);
  
  void pointSize(float size);
  
  int getError();
  
  int uploadTexture(const IImage* image, int textureWidth, int textureHeight);
  
  void setTextureCoordinates(int size, int stride, const float texcoord[]);
  
  void bindTexture (unsigned int n);
  
  void enableDepthTest();
  void disableDepthTest();
  
  void enableBlend();
  void disableBlend();
  
  void drawBillBoard(const unsigned int textureId,
                     const Vector3D& pos,
                     const float viewPortRatio);
  
  void deleteTexture(int glTextureId);
  
  void enableCullFace(CullFace face);
  void disableCullFace();
  
  void transformTexCoords(const Vector2D& scale, const Vector2D& translation);
  
};


#endif
