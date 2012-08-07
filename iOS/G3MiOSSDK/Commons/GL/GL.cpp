//
//  GL.cpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#include <list>

#include "GL.hpp"

#include "IImage.hpp"
#include "Vector3D.hpp"
#include "Vector2D.hpp"

#include "INativeGL.hpp"


struct UniformsStruct {
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
} Uniforms;


struct AttributesStruct {
  int   Position;
  int   TextureCoord;
  int   Color;
  int   Normal;
} Attributes;


void GL::useProgram(unsigned int program) {
  // set shaders
  _gl->useProgram(program);
  
  // Extract the handles to attributes
  Attributes.Position     = _gl->getAttribLocation(program, "Position");
  Attributes.TextureCoord = _gl->getAttribLocation(program, "TextureCoord");
  Attributes.Color        = _gl->getAttribLocation(program, "Color");
  Attributes.Normal       = _gl->getAttribLocation(program, "Normal");
  
  // Extract the handles to uniforms
  Uniforms.Projection          = _gl->getUniformLocation(program, "Projection");
  Uniforms.Modelview           = _gl->getUniformLocation(program, "Modelview");
  Uniforms.Sampler             = _gl->getUniformLocation(program, "Sampler");
  Uniforms.EnableTexture       = _gl->getUniformLocation(program, "EnableTexture");
  Uniforms.FlatColor           = _gl->getUniformLocation(program, "FlatColor");
  Uniforms.TranslationTexCoord = _gl->getUniformLocation(program, "TranslationTexCoord");
  Uniforms.ScaleTexCoord       = _gl->getUniformLocation(program, "ScaleTexCoord");
  Uniforms.PointSize           = _gl->getUniformLocation(program, "PointSize");
  
  // default values
  _gl->uniform2f(Uniforms.TranslationTexCoord, 0, 0);
  _gl->uniform2f(Uniforms.ScaleTexCoord, 1, 1);
  _gl->uniform1f(Uniforms.PointSize, (float) 1.0);
  
  //BILLBOARDS
  Uniforms.BillBoard     = _gl->getUniformLocation(program, "BillBoard");
  Uniforms.ViewPortRatio = _gl->getUniformLocation(program, "ViewPortRatio");
  _gl->uniform1i(Uniforms.BillBoard, 0); //NOT DRAWING BILLBOARD
  
  //FOR FLAT COLOR MIXING
  Uniforms.FlatColorIntensity      = _gl->getUniformLocation(program, "FlatColorIntensity");
  Uniforms.ColorPerVertexIntensity = _gl->getUniformLocation(program, "ColorPerVertexIntensity");
  Uniforms.EnableColorPerVertex    = _gl->getUniformLocation(program, "EnableColorPerVertex");
  Uniforms.EnableFlatColor         = _gl->getUniformLocation(program, "EnableFlatColor");
}

void GL::loadModelView() {
  static float M[16];
  _modelView.copyToFloatMatrix(M);
  _gl->uniformMatrix4fv(Uniforms.Modelview, 1, false, M);
}

void GL::setProjection(const MutableMatrix44D &projection) {
  static float M[16];
  projection.copyToFloatMatrix(M);
  _gl->uniformMatrix4fv(Uniforms.Projection, 1, false, M);
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
#ifdef C_CODE
  GLBufferType buffers[] = { ColorBuffer, DepthBuffer };
#else
  GLBufferType buffers[] = { GLBufferType.ColorBuffer, GLBufferType.DepthBuffer };
#endif
  _gl->clear(2, buffers);
}

void GL::color(float r, float g, float b, float a) {
  _gl->uniform4f(Uniforms.FlatColor, r, g, b, a);
}

void GL::transformTexCoords(const Vector2D& scale,
                             const Vector2D& translation) {
  _gl->uniform2f(Uniforms.ScaleTexCoord,
                 (float) scale.x(),
                 (float) scale.y());
  _gl->uniform2f(Uniforms.TranslationTexCoord,
                 (float) translation.x(),
                 (float) translation.y());
}

void GL::enablePolygonOffset(float factor, float units) {
#ifdef C_CODE
  _gl->enable(PolygonOffsetFill);
#else
  _gl->enable(GLFeature.PolygonOffsetFill);
#endif
  _gl->polygonOffset(factor, units);
}

void GL::disablePolygonOffset() {
#ifdef C_CODE
  _gl->disable(PolygonOffsetFill);
#else
  _gl->disable(GLFeature.PolygonOffsetFill);
#endif
}

void GL::vertexPointer(int size, int stride, const float vertex[]) {
#if C_CODE
  _gl->vertexAttribPointer(Attributes.Position, size, Float, 0, stride, (const void *) vertex);
#else
  _gl->vertexAttribPointer(Attributes.Position, size, GLType.Float, 0, stride, (const void *) vertex);
#endif
}

void GL::drawTriangleStrip(int n, const int i[]) {
  _gl->drawElements(TriangleStrip, n, (GLType)UnsignedInt, i);
}

void GL::drawLines(int n, const int i[]) {
  _gl->drawElements(Lines, n, UnsignedInt, i);
}

void GL::drawLineLoop(int n, const int i[]) {
  _gl->drawElements(LineLoop, n, UnsignedInt, i);
}

void GL::drawPoints(int n, const int i[]) {
  _gl->drawElements(Points, n, UnsignedInt, i);
}

void GL::lineWidth(float width) {
  _gl->lineWidth(width);
}

void GL::pointSize(float size) {
  _gl->uniform1f(Uniforms.PointSize, size);
}

int GL::getError() {
  return _gl->getError();
}

int GL::uploadTexture(const IImage* image, int textureWidth, int textureHeight) {
  
  unsigned char imageData[textureWidth * textureHeight * 4];
  image->fillWithRGBA(imageData, textureWidth, textureHeight);
  
  _gl->blendFunc(SrcAlpha, OneMinusSrcAlpha);
  _gl->pixelStorei(Unpack, 1);
  
  int texID = getTextureID();
  
  _gl->bindTexture(Texture2D, texID);
  _gl->texParameteri(Texture2D, MinFilter, Linear);
  _gl->texParameteri(Texture2D, MagFilter, Linear);
  _gl->texParameteri(Texture2D, WrapS, ClampToEdge);
  _gl->texParameteri(Texture2D, WrapT, ClampToEdge);
  _gl->texImage2D(Texture2D, 0, RGBA, textureWidth, textureHeight, 0, RGBA, UnsignedByte, imageData);
  
  return texID;
}

void GL::setTextureCoordinates(int size, int stride, const float texcoord[]) {
  _gl->vertexAttribPointer(Attributes.TextureCoord, size, Float, 0, stride, (const void *) texcoord);
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
  
  _gl->uniform1i(Uniforms.BillBoard, 1);
  
  _gl->uniform1f(Uniforms.ViewPortRatio, viewPortRatio);
  
  disableDepthTest();
  
  enableTexture2D();
  color(1, 1, 1, 1);
  
  bindTexture(textureId);
  
  vertexPointer(3, 0, vertex);
  setTextureCoordinates(2, 0, texcoord);
  
  _gl->drawArrays(TriangleStrip, 0, 4);
  
  enableDepthTest();
  
  _gl->uniform1i(Uniforms.BillBoard, 0);
}

// state handling
void GL::enableTextures() {
  if (!_enableTextures) {
    _gl->enableVertexAttribArray(Attributes.TextureCoord);
    _enableTextures = true;
  }
}

void GL::disableTextures() {
  if (_enableTextures) {
    _gl->disableVertexAttribArray(Attributes.TextureCoord);
    _enableTextures = false;
  }
}

void GL::enableTexture2D() {
  if (!_enableTexture2D) {
    _gl->uniform1i(Uniforms.EnableTexture, 1);
    _enableTexture2D = true;
  }
}

void GL::disableTexture2D() {
  if (_enableTexture2D) {
    _gl->uniform1i(Uniforms.EnableTexture, 0);
    _enableTexture2D = false;
  }
}

void GL::enableVertexColor(float const colors[], float intensity) {
  if (!_enableVertexColor) {
    _gl->uniform1i(Uniforms.EnableColorPerVertex, 1);
    _gl->enableVertexAttribArray(Attributes.Color);
    _gl->vertexAttribPointer(Attributes.Color, 4, Float, 0, 0, colors);
    _gl->uniform1f(Uniforms.ColorPerVertexIntensity, intensity);
    _enableVertexColor = true;
  }
}

void GL::disableVertexColor() {
  if (_enableVertexColor) {
    _gl->disableVertexAttribArray(Attributes.Color);
    _gl->uniform1i(Uniforms.EnableColorPerVertex, 0);
    _enableVertexColor = false;
  }
}

void GL::enableVertexNormal(float const normals[]) {
  //  if (normals != NULL) {
  if (!_enableVertexNormal) {
    _gl->enableVertexAttribArray(Attributes.Normal);
    _gl->vertexAttribPointer(Attributes.Normal, 3, Float, 0, 0, normals);
    _enableVertexNormal = true;
  }
  //  }
}

void GL::disableVertexNormal() {
  if (_enableVertexNormal) {
    _gl->disableVertexAttribArray(Attributes.Normal);
    _enableVertexNormal = false;
  }
}

void GL::enableVerticesPosition() {
  if (!_enableVerticesPosition) {
    _gl->enableVertexAttribArray(Attributes.Position);
    _enableVerticesPosition = true;
  }
}

void GL::disableVerticesPosition() {
  if (_enableVerticesPosition) {
    _gl->disableVertexAttribArray(Attributes.Position);
    _enableVerticesPosition = false;
  }
}

void GL::enableVertexFlatColor(float r, float g, float b, float a,
                                float intensity) {
  if (!_enableFlatColor) {
    _gl->uniform1i(Uniforms.EnableFlatColor, 1);
    _enableFlatColor = true;
  }
  _gl->uniform4f(Uniforms.FlatColor, r, g, b, a);
  _gl->uniform1f(Uniforms.FlatColorIntensity, intensity);
}

void GL::disableVertexFlatColor() {
  if (_enableFlatColor) {
    _gl->uniform1i(Uniforms.EnableFlatColor, 0);
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

void GL::enableCullFace(GLCullFace face) {
  if (!_enableCullFace) {
    _gl->enable(CullFacing);
    _enableCullFace = true;
  }
  
  if (_cullFace_face != face) {
    _gl->cullFace(face);
    _cullFace_face = face;
  }
}

void GL::disableCullFace() {  
  if (_enableCullFace) {
    _gl->disable(CullFacing);
    _enableCullFace = false;
  }
}

int GL::getTextureID() {
  if (_texturesIdBag.size() == 0) {
    const int bugdetSize = 256;
    
    printf("= Creating %d texturesIds...\n", bugdetSize);
    
    const std::vector<int> ids = _gl->genTextures(bugdetSize);
    
    for (int i = 0; i < bugdetSize; i++) {
      _texturesIdBag.push_back(ids[i]);
    }
    
    _texturesIdCounter += bugdetSize;
    printf("= Created %d texturesIds (accumulated %ld).\n", bugdetSize, _texturesIdCounter);
  }
  
  int result = _texturesIdBag.back();
  _texturesIdBag.pop_back();
  
//  printf("   - Assigning 1 texturesId from bag (bag size=%ld).\n", _texturesIdBag.size());

  return result;
}

void GL::deleteTexture(int glTextureId) {
  int textures[] = { glTextureId };
  _gl->deleteTextures(1, textures);

  _texturesIdBag.push_back(glTextureId);

//  printf("   - Delete 1 texturesId (bag size=%ld).\n", _texturesIdBag.size());
}
