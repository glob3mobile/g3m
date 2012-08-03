//
//  GL2.cpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#include <OpenGLES/ES2/gl.h>
#include <list>

#include "GL2.hpp"

#include "Image_iOS.hpp"
#include "Vector3D.hpp"
#include "Vector2D.hpp"

#include "NativeGL2_iOS.hpp"


struct UniformsStruct {
  GLuint  Projection;
  GLuint  Modelview;
  GLint   Sampler;
  GLint   EnableTexture;
  GLint   FlatColor;
  GLint   TranslationTexCoord;
  GLint   ScaleTexCoord;
  GLint   PointSize;
  
  //FOR BILLBOARDING
  GLint   BillBoard;
  GLint   ViewPortRatio;
  
  //FOR COLOR MIXING
  GLint   FlatColorIntensity;
  GLint   EnableColorPerVertex;
  GLint   EnableFlatColor;
  GLint   ColorPerVertexIntensity;
} Uniforms;


struct AttributesStruct {
  GLint   Position;
  GLint   TextureCoord;
  GLint   Color;
  GLint   Normal;
} Attributes;


void GL2::useProgram(unsigned int program) {
//  // set shaders
//  glUseProgram(program);
//  
//  // Extract the handles to attributes
//  Attributes.Position     = glGetAttribLocation(program, "Position");
//  Attributes.TextureCoord = glGetAttribLocation(program, "TextureCoord");
//  Attributes.Color        = glGetAttribLocation(program, "Color");
//  Attributes.Normal       = glGetAttribLocation(program, "Normal");
//  
//  // Extract the handles to uniforms
//  Uniforms.Projection          = glGetUniformLocation(program, "Projection");
//  Uniforms.Modelview           = glGetUniformLocation(program, "Modelview");
//  Uniforms.Sampler             = glGetUniformLocation(program, "Sampler");
//  Uniforms.EnableTexture       = glGetUniformLocation(program, "EnableTexture");
//  Uniforms.FlatColor           = glGetUniformLocation(program, "FlatColor");
//  Uniforms.TranslationTexCoord = glGetUniformLocation(program, "TranslationTexCoord");
//  Uniforms.ScaleTexCoord       = glGetUniformLocation(program, "ScaleTexCoord");
//  Uniforms.PointSize           = glGetUniformLocation(program, "PointSize");
//  
//  // default values
//  glUniform2f(Uniforms.TranslationTexCoord, 0, 0);
//  glUniform2f(Uniforms.ScaleTexCoord, 1, 1);
//  glUniform1f(Uniforms.PointSize, (float) 1.0);
//  
//  //BILLBOARDS
//  Uniforms.BillBoard     = glGetUniformLocation(program, "BillBoard");
//  Uniforms.ViewPortRatio = glGetUniformLocation(program, "ViewPortRatio");
//  glUniform1i(Uniforms.BillBoard, false); //NOT DRAWING BILLBOARD
//  
//  //FOR FLAT COLOR MIXING
//  Uniforms.FlatColorIntensity      = glGetUniformLocation(program, "FlatColorIntensity");
//  Uniforms.ColorPerVertexIntensity = glGetUniformLocation(program, "ColorPerVertexIntensity");
//  Uniforms.EnableColorPerVertex    = glGetUniformLocation(program, "EnableColorPerVertex");
//  Uniforms.EnableFlatColor         = glGetUniformLocation(program, "EnableFlatColor");
  
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
  _gl->uniform1i(Uniforms.BillBoard, false); //NOT DRAWING BILLBOARD
  
  //FOR FLAT COLOR MIXING
  Uniforms.FlatColorIntensity      = _gl->getUniformLocation(program, "FlatColorIntensity");
  Uniforms.ColorPerVertexIntensity = _gl->getUniformLocation(program, "ColorPerVertexIntensity");
  Uniforms.EnableColorPerVertex    = _gl->getUniformLocation(program, "EnableColorPerVertex");
  Uniforms.EnableFlatColor         = _gl->getUniformLocation(program, "EnableFlatColor");
}

void GL2::loadModelView() {
  static float M[16];
  _modelView.copyToFloatMatrix(M);
  _gl->uniformMatrix4fv(Uniforms.Modelview, 1, 0, M);
}

void GL2::setProjection(const MutableMatrix44D &projection) {
  static float M[16];
  projection.copyToFloatMatrix(M);
  _gl->uniformMatrix4fv(Uniforms.Projection, 1, 0, M);
}

void GL2::loadMatrixf(const MutableMatrix44D &modelView) {
  _modelView = modelView;
  
  loadModelView();
}

void GL2::multMatrixf(const MutableMatrix44D &m) {
  _modelView = _modelView.multiply(m);
  
  loadModelView();
}

void GL2::popMatrix() {
  _modelView = _matrixStack.back();
  _matrixStack.pop_back();
  
  loadModelView();
}

void GL2::pushMatrix() {
  _matrixStack.push_back(_modelView);
}

void GL2::clearScreen(float r, float g, float b, float a) {
  _gl->clearColor(r, g, b, a);
  GLBufferType buffers[] = { ColorBuffer, DepthBuffer };
  _gl->clear(2, buffers);
}

void GL2::color(float r, float g, float b, float a) {
  _gl->uniform4f(Uniforms.FlatColor, r, g, b, a);
}

void GL2::transformTexCoords(const Vector2D& scale,
                             const Vector2D& translation) {
  _gl->uniform2f(Uniforms.ScaleTexCoord,
                 (float) scale.x(),
                 (float) scale.y());
  _gl->uniform2f(Uniforms.TranslationTexCoord,
                 (float) translation.x(),
                 (float) translation.y());
}

void GL2::enablePolygonOffset(float factor, float units) {
  _gl->enable(PolygonOffsetFill);
  _gl->polygonOffset(factor, units);
}

void GL2::disablePolygonOffset() {
  _gl->disable(PolygonOffsetFill);
}

void GL2::vertexPointer(int size, int stride, const float vertex[]) {
  _gl->vertexAttribPointer(Attributes.Position, size, Float, 0, stride, (const void *) vertex);
}

void GL2::drawTriangleStrip(int n, const int i[]) {
  _gl->drawElements(TriangleStrip, n, UnsignedInt, i);
}

void GL2::drawLines(int n, const int i[]) {
  _gl->drawElements(Lines, n, UnsignedInt, i);
}

void GL2::drawLineLoop(int n, const int i[]) {
  _gl->drawElements(LineLoop, n, UnsignedInt, i);
}

void GL2::drawPoints(int n, const int i[]) {
  _gl->drawElements(Points, n, UnsignedInt, i);
}

void GL2::lineWidth(float width) {
  _gl->lineWidth(width);
}

void GL2::pointSize(float size) {
  _gl->uniform1f(Uniforms.PointSize, size);
}

int GL2::getError() {
  return _gl->getError();
}

int GL2::uploadTexture(const IImage* image, int textureWidth, int textureHeight) {

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
  
  //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, textureWidth, textureHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
  
  
  _gl->texImage2D(Texture2D, 0, RGBA, textureWidth, textureHeight, 0, RGBA, UnsignedByte, imageData);
  
  return texID;
}

void GL2::setTextureCoordinates(int size, int stride, const float texcoord[]) {
  glVertexAttribPointer(Attributes.TextureCoord, size, GL_FLOAT, 0, stride, (const void *) texcoord);
}

void GL2::bindTexture(unsigned int n) {
  glBindTexture(GL_TEXTURE_2D, n);
}

void GL2::drawBillBoard(const unsigned int textureId,
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
  
  glUniform1i(Uniforms.BillBoard, true);
  
  glUniform1f(Uniforms.ViewPortRatio, viewPortRatio);
  
  disableDepthTest();
  
  enableTexture2D();
  color(1, 1, 1, 1);
  
  bindTexture(textureId);

  vertexPointer(3, 0, vertex);
  setTextureCoordinates(2, 0, texcoord);
  
  glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
  
  enableDepthTest();
  
  glUniform1i(Uniforms.BillBoard, false);
}

void GL2::deleteTexture(int glTextureId) {
  unsigned int textures[] = { glTextureId };
  glDeleteTextures(1, textures);
}

// state handling
void GL2::enableTextures() {
  if (!_enableTextures) {
    glEnableVertexAttribArray(Attributes.TextureCoord);
    _enableTextures = true;
  }
}

void GL2::disableTextures() {
  if (_enableTextures) {
    glDisableVertexAttribArray(Attributes.TextureCoord);
    _enableTextures = false;
  }
}

void GL2::enableTexture2D() {
  if (!_enableTexture2D) {
    glUniform1i(Uniforms.EnableTexture, true);
    _enableTexture2D = true;
  }
}

void GL2::disableTexture2D() {
  if (_enableTexture2D) {
    glUniform1i(Uniforms.EnableTexture, false);
    _enableTexture2D = false;
  }
}

void GL2::enableVertexColor(float const colors[], float intensity) {
//  if (colors != NULL) {
  if (!_enableVertexColor) {
    glUniform1i(Uniforms.EnableColorPerVertex, true);
    glEnableVertexAttribArray(Attributes.Color);
    glVertexAttribPointer(Attributes.Color, 4, GL_FLOAT, 0, 0, colors);
    glUniform1f(Uniforms.ColorPerVertexIntensity, intensity);
    _enableVertexColor = true;
  }
//  }
}

void GL2::disableVertexColor() {
  if (_enableVertexColor) {
    glDisableVertexAttribArray(Attributes.Color);
    glUniform1i(Uniforms.EnableColorPerVertex, false);
    _enableVertexColor = false;
  }
}

void GL2::enableVertexNormal(float const normals[]) {
//  if (normals != NULL) {
  if (!_enableVertexNormal) {
    glEnableVertexAttribArray(Attributes.Normal);
    glVertexAttribPointer(Attributes.Normal, 3, GL_FLOAT, 0, 0, normals);
    _enableVertexNormal = true;
  }
//  }
}

void GL2::disableVertexNormal() {
  if (_enableVertexNormal) {
    glDisableVertexAttribArray(Attributes.Normal);
    _enableVertexNormal = false;
  }
}

void GL2::enableVerticesPosition() {
  if (!_enableVerticesPosition) {
    glEnableVertexAttribArray(Attributes.Position);
    _enableVerticesPosition = true;
  }
}

void GL2::disableVerticesPosition() {
  if (_enableVerticesPosition) {
    glDisableVertexAttribArray(Attributes.Position);
    _enableVerticesPosition = false;
  }
}

void GL2::enableVertexFlatColor(float r, float g, float b, float a,
                                float intensity) {
  if (!_enableFlatColor) {
    glUniform1i(Uniforms.EnableFlatColor, true);
    _enableFlatColor = true;
  }
  glUniform4f(Uniforms.FlatColor, r, g, b, a);
  glUniform1f(Uniforms.FlatColorIntensity, intensity);
}

void GL2::disableVertexFlatColor() {
  if (_enableFlatColor) {
    glUniform1i(Uniforms.EnableFlatColor, false);
    _enableFlatColor = false;
  }
}

void GL2::enableDepthTest() {
  if (!_enableDepthTest) {
    glEnable(GL_DEPTH_TEST);
    _enableDepthTest = true;
  }
}

void GL2::disableDepthTest() {
  if (_enableDepthTest) {
    glDisable(GL_DEPTH_TEST);
    _enableDepthTest = false;
  }
}

void GL2::enableBlend() {
  if (!_enableBlend) {
    glEnable(GL_BLEND);
    _enableBlend = true;
  }
}

void GL2::disableBlend() {
  if (_enableBlend) {
    glDisable(GL_BLEND);
    _enableBlend = false;
  }

}

void GL2::enableCullFace(CullFace face) {
  if (!_enableCullFace) {
    glEnable(GL_CULL_FACE);  
    _enableCullFace = true;
  }
  
  if (_cullFace_face != face) {
    switch (face) {
      case FRONT:
        glCullFace(GL_FRONT);
        break;
      case BACK:
        glCullFace(GL_BACK);
        break;
      case FRONT_AND_BACK:
        glCullFace(GL_FRONT_AND_BACK);
        break;
    }
    
    _cullFace_face = face;
  }
}

void GL2::disableCullFace() {
  if (_enableCullFace) {
    glDisable(GL_CULL_FACE);
    _enableCullFace = false;
  }
}
