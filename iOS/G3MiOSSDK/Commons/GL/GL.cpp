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
  int Projection;
  int Modelview;
  int Sampler;
  int EnableTexture;
  int FlatColor;
  int TranslationTexCoord;
  int ScaleTexCoord;
  int PointSize;
  
  //FOR BILLBOARDING
  int BillBoard;
  int ViewPortRatio;
  
  //FOR COLOR MIXING
  int FlatColorIntensity;
  int EnableColorPerVertex;
  int EnableFlatColor;
  int ColorPerVertexIntensity;
} Uniforms;


struct AttributesStruct {
  int Position;
  int TextureCoord;
  int Color;
  int Normal;
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
  _gl->uniform2f(Uniforms.ScaleTexCoord, _scaleX, _scaleY);
  _gl->uniform2f(Uniforms.TranslationTexCoord, _translationX, _translationY);
  _gl->uniform1f(Uniforms.PointSize, 1);
  
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
  if (
      (_flatColorR != r) ||
      (_flatColorG != g) ||
      (_flatColorB != b) ||
      (_flatColorA != a)
      ) {
    _gl->uniform4f(Uniforms.FlatColor, r, g, b, a);
    
    _flatColorR = r;
    _flatColorG = g;
    _flatColorB = b;
    _flatColorA = a;
  }
  
//  _gl->uniform4f(Uniforms.FlatColor, r, g, b, a);
}

void GL::transformTexCoords(float scaleX,
                            float scaleY,
                            float translationX,
                            float translationY) {
  if ((_scaleX != scaleX) || (_scaleY != scaleY)) {
    _gl->uniform2f(Uniforms.ScaleTexCoord,
                   scaleX,
                   scaleY);
    _scaleX = scaleX;
    _scaleY = scaleY;
  }
  
  if ((_translationX != translationX) || (_translationY != translationY)) {
    _gl->uniform2f(Uniforms.TranslationTexCoord,
                   translationX,
                   translationY);
    _translationX = translationX;
    _translationY = translationY;
  }
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
#ifdef C_CODE
  _gl->vertexAttribPointer(Attributes.Position, size, Float, false, stride, (const void *) vertex);
#else
  _gl->vertexAttribPointer(Attributes.Position, size, GLType.Float, false, stride, (const void *) vertex);
#endif
}

void GL::drawTriangleStrip(int n, const int i[]) {
#ifdef C_CODE
  _gl->drawElements(TriangleStrip, n, UnsignedInt, i);
#else
  _gl->drawElements(GLPrimitive.TriangleStrip, n, GLType.UnsignedInt, i);
#endif
}

void GL::drawLines(int n, const int i[]) {
#ifdef C_CODE
  _gl->drawElements(Lines, n, UnsignedInt, i);
#else
  _gl->drawElements(GLPrimitive.Lines, n, GLType.UnsignedInt, i);
#endif
}

void GL::drawLineLoop(int n, const int i[]) {
#ifdef C_CODE
  _gl->drawElements(LineLoop, n, UnsignedInt, i);
#else
  _gl->drawElements(GLPrimitive.LineLoop, n, GLType.UnsignedInt, i);
#endif
}

void GL::drawPoints(int n, const int i[]) {
#ifdef C_CODE
  _gl->drawElements(Points, n, UnsignedInt, i);
#else
  _gl->drawElements(GLPrimitive.Points, n, GLType.UnsignedInt, i);
#endif
}

void GL::lineWidth(float width) {
  _gl->lineWidth(width);
}

void GL::pointSize(float size) {
  _gl->uniform1f(Uniforms.PointSize, size);
}

GLError GL::getError() {
  return _gl->getError();
}

const GLTextureID GL::uploadTexture(const IImage* image,
                                    int textureWidth, int textureHeight) {
  const GLTextureID texID = getGLTextureID();
  if (texID.isValid()) {
    
#ifdef C_CODE
    unsigned char* imageData;
    
    const bool lastImageDataIsValid = ((_lastTextureWidth == textureWidth) &&
                                       (_lastTextureHeight == textureHeight) &&
                                       (_lastImageData != NULL));
    
    if (lastImageDataIsValid) {
      imageData = _lastImageData;
    }
    else {
      imageData = new unsigned char[textureWidth * textureHeight * 4];
      if (_lastImageData != NULL) {
        delete [] _lastImageData;
      }
      _lastImageData = imageData;
      _lastTextureWidth = textureWidth;
      _lastTextureHeight = textureHeight;
    }

    image->fillWithRGBA8888(imageData, textureWidth, textureHeight);
    
    _gl->blendFunc(SrcAlpha, OneMinusSrcAlpha);
    _gl->pixelStorei(Unpack, 1);
    
    _gl->bindTexture(Texture2D, texID.getGLTextureID());
    _gl->texParameteri(Texture2D, MinFilter, Linear);
    _gl->texParameteri(Texture2D, MagFilter, Linear);
    _gl->texParameteri(Texture2D, WrapS, ClampToEdge);
    _gl->texParameteri(Texture2D, WrapT, ClampToEdge);
    _gl->texImage2D(Texture2D, 0, RGBA, textureWidth, textureHeight, 0, RGBA, UnsignedByte, imageData);
#endif
    
#ifdef JAVA_CODE
    byte[] imageData = new byte[textureWidth * textureHeight * 4];
    image.fillWithRGBA8888(imageData, textureWidth, textureHeight);
    
    int texID = getTextureID();
    _gl.blendFunc(GLBlendFactor.SrcAlpha, GLBlendFactor.OneMinusSrcAlpha);
    _gl.pixelStorei(GLAlignment.Unpack, 1);
    
    _gl.bindTexture(GLTextureType.Texture2D, texID);
    _gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.MinFilter, GLTextureParameterValue.Linear);
    _gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.MagFilter, GLTextureParameterValue.Linear);
    _gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.WrapS, GLTextureParameterValue.ClampToEdge);
    _gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.WrapT, GLTextureParameterValue.ClampToEdge);
    _gl.texImage2D(GLTextureType.Texture2D, 0, GLFormat.RGBA, textureWidth, textureHeight, 0, GLFormat.RGBA, GLType.UnsignedByte, imageData);
    return texID;
#endif
    
  }
  else {
    printf("can't get a valid texture id\n");
  }
  return texID;
}

void GL::setTextureCoordinates(int size, int stride, const float texcoord[]) {
    if (_textureCoordinates != texcoord) {
#ifdef C_CODE
  _gl->vertexAttribPointer(Attributes.TextureCoord, size, Float, false, stride, (const void *) texcoord);
#else
  _gl->vertexAttribPointer(Attributes.TextureCoord, size, GLType.Float, false, stride, (const void *) texcoord);
#endif
    }
}

void GL::bindTexture(const GLTextureID& textureId) {
#ifdef C_CODE
  _gl->bindTexture(Texture2D, textureId.getGLTextureID());
#else
  _gl->bindTexture(GLTextureType.Texture2D, textureId.getGLTextureID());
#endif
}

void GL::drawBillBoard(const GLTextureID& textureId,
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
  
#ifdef C_CODE
  _gl->drawArrays(TriangleStrip, 0, 4);
#else
  _gl->drawArrays(GLPrimitive.TriangleStrip, 0, 4);
#endif
  
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
#ifdef C_CODE
    _gl->vertexAttribPointer(Attributes.Color, 4, Float, false, 0, colors);
#else
    _gl->vertexAttribPointer(Attributes.Color, 4, GLType.Float, false, 0, colors);
#endif
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
  if (!_enableVertexNormal) {
    _gl->enableVertexAttribArray(Attributes.Normal);
#ifdef C_CODE
    _gl->vertexAttribPointer(Attributes.Normal, 3, Float, false, 0, normals);
#else
    _gl->vertexAttribPointer(Attributes.Normal, 3, GLType.Float, false, 0, normals);
#endif
    _enableVertexNormal = true;
  }
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
  
  color(r, g, b, a);
  
//  _gl->uniform1f(Uniforms.FlatColorIntensity, intensity);
  if (_flatColorIntensity != intensity) {
    _gl->uniform1f(Uniforms.FlatColorIntensity, intensity);
    _flatColorIntensity = intensity;
  }
}

void GL::disableVertexFlatColor() {
  if (_enableFlatColor) {
    _gl->uniform1i(Uniforms.EnableFlatColor, 0);
    _enableFlatColor = false;
  }
}

void GL::enableDepthTest() {
  if (!_enableDepthTest) {
#ifdef C_CODE
    _gl->enable(DepthTest);
#else
    _gl->enable(GLFeature.DepthTest);
#endif
    _enableDepthTest = true;
  }
}

void GL::disableDepthTest() {
  if (_enableDepthTest) {
#ifdef C_CODE
    _gl->disable(DepthTest);
#else
    _gl->disable(GLFeature.DepthTest);
#endif
    _enableDepthTest = false;
  }
}

void GL::enableBlend() {
  if (!_enableBlend) {
#ifdef C_CODE
    _gl->enable(Blend);
#else
    _gl->enable(GLFeature.Blend);
#endif
    _enableBlend = true;
  }
}

void GL::disableBlend() {
  if (_enableBlend) {
#ifdef C_CODE
    _gl->disable(Blend);
#else
    _gl->disable(GLFeature.Blend);
#endif
    _enableBlend = false;
  }
  
}

void GL::setBlendFuncSrcAlpha(){
#ifdef C_CODE
  _gl->blendFunc(SrcAlpha, OneMinusSrcAlpha);
#else
  _gl->blendFunc(GLBlendFactor.SrcAlpha, GLBlendFactor.OneMinusSrcAlpha);
#endif
}

void GL::enableCullFace(GLCullFace face) {
  if (!_enableCullFace) {
#ifdef C_CODE
    _gl->enable(CullFacing);
#else
    _gl->enable(GLFeature.CullFacing);
#endif
    _enableCullFace = true;
  }
  
  if (_cullFace_face != face) {
    _gl->cullFace(face);
    _cullFace_face = face;
  }
}

void GL::disableCullFace() {
  if (_enableCullFace) {
#ifdef C_CODE
    _gl->disable(CullFacing);
#else
    _gl->disable(GLFeature.CullFacing);
#endif
    _enableCullFace = false;
  }
}

const GLTextureID GL::getGLTextureID() {
  if (_texturesIdBag.size() == 0) {
    const int bugdetSize = 256;
    
    printf("= Creating %d texturesIds...\n", bugdetSize);
    
    const std::vector<GLTextureID> ids = _gl->genTextures(bugdetSize);
    
    for (int i = 0; i < bugdetSize; i++) {
//      _texturesIdBag.push_back(ids[i]);
      _texturesIdBag.push_front(ids[i]);
    }
    
    _texturesIdAllocationCounter += bugdetSize;
    
#ifdef C_CODE
    printf("= Created %d texturesIds (accumulated %ld).\n", bugdetSize, _texturesIdAllocationCounter);
#endif
  }
  
  _texturesIdGetCounter++;
  
  const GLTextureID result = _texturesIdBag.back();
  _texturesIdBag.pop_back();
  
//  printf("   - Assigning 1 texturesId (#%d) from bag (bag size=%ld). Gets:%ld, Takes:%ld, Delta:%ld.\n",
//         result.getGLTextureID(),
//         _texturesIdBag.size(),
//         _texturesIdGetCounter,
//         _texturesIdTakeCounter,
//         _texturesIdGetCounter - _texturesIdTakeCounter);
  
  return result;
}

void GL::deleteTexture(const GLTextureID& textureId) {
  if (!textureId.isValid()) {
    return;
  }
  const int textures[] = {
    textureId.getGLTextureID()
  };
  _gl->deleteTextures(1, textures);
  
  _texturesIdBag.push_back(textureId);
  
  _texturesIdTakeCounter++;
#if C_CODE
  printf("   - Delete 1 texturesId (bag size=%ld). Gets:%ld, Takes:%ld, Delta:%ld.\n",
         _texturesIdBag.size(),
         _texturesIdGetCounter,
         _texturesIdTakeCounter,
         _texturesIdGetCounter - _texturesIdTakeCounter);
#endif
}
