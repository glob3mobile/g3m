//
//  GL.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "GL.hpp"

#include "Vector3D.hpp"
#include "Vector2D.hpp"

#include "INativeGL.hpp"

struct UniformsStructGL {
  int  Projection;
  int  Modelview;
  int   Sampler;
  int   EnableTexture;
  int   FlatColor;
  int   TranslationTexCoord;
  int   ScaleTexCoord;
  int   PointSize;
  
  //FOR BILLBOARDING
  int   BillBoard;
  int   ViewPortRatio;
  
  //FOR COLOR MIXING
  int   FlatColorIntensity;
  int   EnableColorPerVertex;
  int   EnableFlatColor;
  int   ColorPerVertexIntensity;
} UniformsGL;


struct AttributesStructGL {
  int   Position;
  int   TextureCoord;
  int   Color;
  int   Normal;
} AttributesGL;



void GL::useProgram(unsigned int program) {
  // set shaders
  _gl->useProgram(program);
  
  // Extract the handles to attributes
  AttributesGL.Position     = _gl->getAttribLocation(program, "Position");
  AttributesGL.TextureCoord = _gl->getAttribLocation(program, "TextureCoord");
  AttributesGL.Color        = _gl->getAttribLocation(program, "Color");
  AttributesGL.Normal       = _gl->getAttribLocation(program, "Normal");
  
  // Extract the handles to uniforms
  UniformsGL.Projection          = _gl->getUniformLocation(program, "Projection");
  UniformsGL.Modelview           = _gl->getUniformLocation(program, "Modelview");
  UniformsGL.Sampler             = _gl->getUniformLocation(program, "Sampler");
  UniformsGL.EnableTexture       = _gl->getUniformLocation(program, "EnableTexture");
  UniformsGL.FlatColor           = _gl->getUniformLocation(program, "FlatColor");
  UniformsGL.TranslationTexCoord = _gl->getUniformLocation(program, "TranslationTexCoord");
  UniformsGL.ScaleTexCoord       = _gl->getUniformLocation(program, "ScaleTexCoord");
  UniformsGL.PointSize           = _gl->getUniformLocation(program, "PointSize");
  
  // default values
  _gl->uniform2f(UniformsGL.TranslationTexCoord, 0, 0);
  _gl->uniform2f(UniformsGL.ScaleTexCoord, 1, 1);
  _gl->uniform1f(UniformsGL.PointSize, (float) 1.0);
  
  //BILLBOARDS
  UniformsGL.BillBoard     = _gl->getUniformLocation(program, "BillBoard");
  UniformsGL.ViewPortRatio = _gl->getUniformLocation(program, "ViewPortRatio");
  _gl->uniform1i(UniformsGL.BillBoard, false); //NOT DRAWING BILLBOARD
  
  //FOR FLAT COLOR MIXING
  UniformsGL.FlatColorIntensity      = _gl->getUniformLocation(program, "FlatColorIntensity");
  UniformsGL.ColorPerVertexIntensity = _gl->getUniformLocation(program, "ColorPerVertexIntensity");
  UniformsGL.EnableColorPerVertex    = _gl->getUniformLocation(program, "EnableColorPerVertex");
  UniformsGL.EnableFlatColor         = _gl->getUniformLocation(program, "EnableFlatColor");
}

void GL::loadModelView() {
  float M[16];
  _modelView.copyToFloatMatrix(M);
  _gl->uniformMatrix4fv(UniformsGL.Modelview, 1, 0, M);
}

void GL::setProjection(const MutableMatrix44D &projection) {
  float M[16];
  projection.copyToFloatMatrix(M);
  _gl->uniformMatrix4fv(UniformsGL.Projection, 1, 0, M);
}

void GL::loadMatrixf(const MutableMatrix44D &modelView) {
  _modelView = modelView;
  
  loadModelView();
}

void GL::multMatrixf(const MutableMatrix44D &m) {
  _modelView = _modelView.multiply(m);
  
  loadModelView();
}

void GL::popMatrix() {
  _modelView = _matrixStack.back();
  _matrixStack.pop_back();
  
  loadModelView();
}

void GL::pushMatrix() {
  _matrixStack.push_back(_modelView);
}

void GL::clearScreen(float r, float g, float b, float a) {
  _gl->clearColor(r, g, b, a);
  
  GLBufferType buffers[] = { ColorBuffer, DepthBuffer };
  _gl->clear(2, buffers);
}

void GL::color(float r, float g, float b, float a) {
  _gl->uniform4f(UniformsGL.FlatColor, r, g, b, a);
}

void GL::transformTexCoords(const Vector2D& scale,
                             const Vector2D& translation) {
  _gl->uniform2f(UniformsGL.ScaleTexCoord,
              (float) scale.x(),
              (float) scale.y());
  _gl->uniform2f(UniformsGL.TranslationTexCoord,
              (float) translation.x(),
              (float) translation.y());
}

void GL::enablePolygonOffset(float factor, float units) {
  _gl->enable(PolygonOffsetFill);
  _gl->polygonOffset(factor, units);
}

void GL::disablePolygonOffset() {
  _gl->disable(PolygonOffsetFill);
}

void GL::vertexPointer(int size, int stride, const float vertex[]) {
  _gl->vertexAttribPointer(AttributesGL.Position, size, Float, 0, stride, (const void *) vertex);
}

void GL::drawTriangleStrip(int n, const int* i) {
  _gl->drawElements(TriangleStrip, n, Int, i);
}

void GL::drawLines(int n, const int i[]) {
  _gl->drawElements(Lines, n, Int, i);
}

void GL::drawLineLoop(int n, const int i[]) {
  _gl->drawElements(LineLoop, n, Int, i);
}

void GL::drawPoints(int n, const int i[]) {
  _gl->drawElements(Points, n, Int, i);
}

void GL::lineWidth(float width) {
  _gl->lineWidth(width);
}

void GL::pointSize(float size) {
  _gl->uniform1f(UniformsGL.PointSize, size);
}

int GL::getError() {
  return _gl->getError();
}

int GL::uploadTexture(const IImage* image, int textureWidth, int textureHeight) {
  
  unsigned char imageData[textureWidth * textureHeight * 4];
  image->fillWithRGBA(imageData, textureWidth, textureHeight);

  
  
  _gl->blendFunc(SrcAlpha, OneMinusSrcAlpha);   
  _gl->pixelStorei(Unpack, 1);
  
  std::vector<int> ts = _gl->genTextures(1);
  int texID = ts[0];
  
  
  _gl->bindTexture(Texture2D, texID);
  _gl->texParameteri(Texture2D, MinFilter, Linear);
  _gl->texParameteri(Texture2D, MagFilter, Linear);
  _gl->texParameteri(Texture2D, WrapS, ClampToEdge);
  _gl->texParameteri(Texture2D, WrapT, ClampToEdge);
  
  _gl->texImage2D(Texture2D, 0, RGBA, textureWidth, textureHeight, 0, RGBA, UnsignedByte, imageData);

  return texID;
}

void GL::setTextureCoordinates(int size, int stride, const float texcoord[]) {
  _gl->vertexAttribPointer(AttributesGL.TextureCoord, size, Float, 0, stride, (const void *) texcoord);
}

void GL::bindTexture(unsigned int n) {
  _gl->bindTexture(Texture2D, n);
}

void GL::drawBillBoard(const unsigned int textureId,
                        const Vector3D& pos,
                        const float viewPortRatio) {
  const float vertex[] = {
    (float) pos.x(), (float) pos.y(), (float) pos.z(),
    (float) pos.x(), (float) pos.y(), (float) pos.z(),
    (float) pos.x(), (float) pos.y(), (float) pos.z(),
    (float) pos.x(), (float) pos.y(), (float) pos.z()
  };
  
  const static float texcoord[] = {
    1, 1,
    1, 0,
    0, 1,
    0, 0
  };
  
  _gl->uniform1i(UniformsGL.BillBoard, true);
  
  _gl->uniform1f(UniformsGL.ViewPortRatio, viewPortRatio);
  
  disableDepthTest();
  
  enableTexture2D();
  color(1, 1, 1, 1);
  
  bindTexture(textureId);
  
  vertexPointer(3, 0, vertex);
  setTextureCoordinates(2, 0, texcoord);
  
  _gl->drawArrays(TriangleStrip, 0, 4);
  
  enableDepthTest();
  
  _gl->uniform1i(UniformsGL.BillBoard, false);
}

void GL::deleteTexture(int glTextureId) {
  int textures[] = { glTextureId };
  _gl->deleteTextures(1, textures);
}

// state handling
void GL::enableTextures() {
  if (!_enableTextures) {
    _gl->enableVertexAttribArray(AttributesGL.TextureCoord);
    _enableTextures = true;
  }
}

void GL::disableTextures() {
  if (_enableTextures) {
    _gl->disableVertexAttribArray(AttributesGL.TextureCoord);
    _enableTextures = false;
  }
}

void GL::enableTexture2D() {
  if (!_enableTexture2D) {
    _gl->uniform1i(UniformsGL.EnableTexture, true);
    _enableTexture2D = true;
  }
}

void GL::disableTexture2D() {
  if (_enableTexture2D) {
    _gl->uniform1i(UniformsGL.EnableTexture, false);
    _enableTexture2D = false;
  }
}

void GL::enableVertexColor(float const colors[], float intensity) {
  //  if (colors != NULL) {
  if (!_enableVertexColor) {
    _gl->uniform1i(UniformsGL.EnableColorPerVertex, true);
    _gl->enableVertexAttribArray(AttributesGL.Color);
    _gl->vertexAttribPointer(AttributesGL.Color, 4, Float, 0, 0, colors);
    _gl->uniform1f(UniformsGL.ColorPerVertexIntensity, intensity);
    _enableVertexColor = true;
  }
  //  }
}

void GL::disableVertexColor() {
  if (_enableVertexColor) {
    _gl->disableVertexAttribArray(AttributesGL.Color);
    _gl->uniform1i(UniformsGL.EnableColorPerVertex, false);
    _enableVertexColor = false;
  }
}

void GL::enableVertexNormal(float const normals[]) {
  //  if (normals != NULL) {
  if (!_enableVertexNormal) {
    _gl->enableVertexAttribArray(AttributesGL.Normal);
    _gl->vertexAttribPointer(AttributesGL.Normal, 3, Float, 0, 0, normals);
    _enableVertexNormal = true;
  }
  //  }
}

void GL::disableVertexNormal() {
  if (_enableVertexNormal) {
    _gl->disableVertexAttribArray(AttributesGL.Normal);
    _enableVertexNormal = false;
  }
}

void GL::enableVerticesPosition() {
  if (!_enableVerticesPosition) {
    _gl->enableVertexAttribArray(AttributesGL.Position);
    _enableVerticesPosition = true;
  }
}

void GL::disableVerticesPosition() {
  if (_enableVerticesPosition) {
    _gl->disableVertexAttribArray(AttributesGL.Position);
    _enableVerticesPosition = false;
  }
}

void GL::enableVertexFlatColor(float r, float g, float b, float a,
                                float intensity) {
  if (!_enableFlatColor) {
    _gl->uniform1i(UniformsGL.EnableFlatColor, true);
    _enableFlatColor = true;
  }
  _gl->uniform4f(UniformsGL.FlatColor, r, g, b, a);
  _gl->uniform1f(UniformsGL.FlatColorIntensity, intensity);
}

void GL::disableVertexFlatColor() {
  if (_enableFlatColor) {
    _gl->uniform1i(UniformsGL.EnableFlatColor, false);
    _enableFlatColor = false;
  }
}

void GL::enableDepthTest() {
  if (!_enableDepthTest) {
    _gl->enable(DepthTest);
    _enableDepthTest = true;
  }
}

void GL::disableDepthTest() {
  if (_enableDepthTest) {
    _gl->disable(DepthTest);
    _enableDepthTest = false;
  }
}

void GL::enableBlend() {
  if (!_enableBlend) {
    _gl->enable(Blend);
    _enableBlend = true;
  }
}

void GL::disableBlend() {
  if (_enableBlend) {
    _gl->disable(Blend);
    _enableBlend = false;
  }
  
}

void GL::enableCullFace(CullFace face) {
  if (!_enableCullFace) {
    _gl->enable(CullFacing);  
    _enableCullFace = true;
  }
  
  if (_cullFace_face != face) {
    switch (face) {
      case FRONT:
        _gl->cullFace(FRONT);
        break;
      case BACK:
        _gl->cullFace(BACK);
        break;
      case FRONT_AND_BACK:
        _gl->cullFace(FRONT_AND_BACK);
        break;
    }
    
    _cullFace_face = face;
  }
}

void GL::disableCullFace() {
  if (_enableCullFace) {
    _gl->disable(CullFacing);
    _enableCullFace = false;
  }
}
