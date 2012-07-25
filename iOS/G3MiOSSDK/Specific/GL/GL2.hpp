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
private:
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
  
public:
  
  GL2() :
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
  
  void enableVertexFlatColor(const Color& color,
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
  
  void pushMatrix() ;
  
  void popMatrix() ;
  
  void loadMatrixf(const MutableMatrix44D &m) ;
  
  void multMatrixf(const MutableMatrix44D &m) ;
  
  void vertexPointer(int size, int stride, const float vertex[]) ;
  
  void drawTriangleStrip(int n, const unsigned int i[]) ;
  
  void drawLines(int n, const unsigned int *i); 
  
  void drawLineLoop(int n, const unsigned int *i); 
  
  void drawPoints(int n, const unsigned int *i);
  
  void setProjection(const MutableMatrix44D &projection) ;
  
  void useProgram(unsigned int program) ;
  
  void enablePolygonOffset(float factor, float units) ;
  
  void disablePolygonOffset() ;
  
  void lineWidth(float width);
  
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
  
//  void cullFace(bool b, CullFace face);
  void enableCullFace(CullFace face);
  void disableCullFace();

  void transformTexCoords(const Vector2D& scale, const Vector2D& translation);

};

