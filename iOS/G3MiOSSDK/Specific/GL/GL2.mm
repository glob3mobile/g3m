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


struct UniformsStructGL2 {
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
} UniformsGL2;


struct AttributesStructGL2 {
  GLint   Position;
  GLint   TextureCoord;
  GLint   Color;
  GLint   Normal;
} AttributesGL2;



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
  AttributesGL2.Position     = _gl->getAttribLocation(program, "Position");
  AttributesGL2.TextureCoord = _gl->getAttribLocation(program, "TextureCoord");
  AttributesGL2.Color        = _gl->getAttribLocation(program, "Color");
  AttributesGL2.Normal       = _gl->getAttribLocation(program, "Normal");
  
  // Extract the handles to uniforms
  UniformsGL2.Projection          = _gl->getUniformLocation(program, "Projection");
  UniformsGL2.Modelview           = _gl->getUniformLocation(program, "Modelview");
  UniformsGL2.Sampler             = _gl->getUniformLocation(program, "Sampler");
  UniformsGL2.EnableTexture       = _gl->getUniformLocation(program, "EnableTexture");
  UniformsGL2.FlatColor           = _gl->getUniformLocation(program, "FlatColor");
  UniformsGL2.TranslationTexCoord = _gl->getUniformLocation(program, "TranslationTexCoord");
  UniformsGL2.ScaleTexCoord       = _gl->getUniformLocation(program, "ScaleTexCoord");
  UniformsGL2.PointSize           = _gl->getUniformLocation(program, "PointSize");
  
  // default values
  _gl->uniform2f(UniformsGL2.TranslationTexCoord, 0, 0);
  _gl->uniform2f(UniformsGL2.ScaleTexCoord, 1, 1);
  _gl->uniform1f(UniformsGL2.PointSize, (float) 1.0);
  
  //BILLBOARDS
  UniformsGL2.BillBoard     = _gl->getUniformLocation(program, "BillBoard");
  UniformsGL2.ViewPortRatio = _gl->getUniformLocation(program, "ViewPortRatio");
  _gl->uniform1i(UniformsGL2.BillBoard, false); //NOT DRAWING BILLBOARD
  
  //FOR FLAT COLOR MIXING
  UniformsGL2.FlatColorIntensity      = _gl->getUniformLocation(program, "FlatColorIntensity");
  UniformsGL2.ColorPerVertexIntensity = _gl->getUniformLocation(program, "ColorPerVertexIntensity");
  UniformsGL2.EnableColorPerVertex    = _gl->getUniformLocation(program, "EnableColorPerVertex");
  UniformsGL2.EnableFlatColor         = _gl->getUniformLocation(program, "EnableFlatColor");
}

void GL2::loadModelView() {
  static float M[16];
  _modelView.copyToFloatMatrix(M);
  _gl->uniformMatrix4fv(UniformsGL2.Modelview, 1, 0, M);
}

void GL2::setProjection(const MutableMatrix44D &projection) {
  static float M[16];
  projection.copyToFloatMatrix(M);
  _gl->uniformMatrix4fv(UniformsGL2.Projection, 1, 0, M);
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
  _gl->uniform4f(UniformsGL2.FlatColor, r, g, b, a);
}

void GL2::transformTexCoords(const Vector2D& scale,
                             const Vector2D& translation) {
  _gl->uniform2f(UniformsGL2.ScaleTexCoord,
                 (float) scale.x(),
                 (float) scale.y());
  _gl->uniform2f(UniformsGL2.TranslationTexCoord,
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
  _gl->vertexAttribPointer(AttributesGL2.Position, size, Float, 0, stride, (const void *) vertex);
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
  _gl->uniform1f(UniformsGL2.PointSize, size);
}

int GL2::getError() {
  return _gl->getError();
}

GLuint GL2::getTextureID() {
//  GLuint textureID;
//  glGenTextures(1, &textureID);
//  return textureID;
  
  if (_texturesIdBag.size() == 0) {
    const int bugdetSize = 256;
    
    GLuint* texturesId = new GLuint[bugdetSize];
    
    glGenTextures(bugdetSize, texturesId);
    
    for (int i = 0; i < bugdetSize; i++) {
      _texturesIdBag.push_back(texturesId[i]);
    }
    
    delete [] texturesId;
    
    _texturesIdCounter += bugdetSize;
    
    printf("= Created %d texturesIds (accumulated %ld)\n", bugdetSize, _texturesIdCounter);
  }
  
  GLuint result = _texturesIdBag.back();
  _texturesIdBag.pop_back();
  return result;
}

void GL2::deleteTexture(int glTextureId) {
  unsigned int textures[] = { glTextureId };
  glDeleteTextures(1, textures);
  
  _texturesIdBag.push_back(glTextureId);
}

int GL2::uploadTexture(const IImage* image, int textureWidth, int textureHeight) {
  CGImageRef cgImage = ((Image_iOS*) image)->getUIImage().CGImage;

  CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
  //void *imageData = malloc( textureHeight * textureWidth * 4 );
//  void *imageData = new unsigned char[textureHeight * textureWidth * 4];
  unsigned char imageData[textureWidth * textureHeight * 4];
  
  CGContextRef context = CGBitmapContextCreate(imageData,
                                               textureWidth, textureHeight,
                                               8, 4 * textureWidth,
                                               colorSpace,
                                               kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big );
  CGColorSpaceRelease( colorSpace );
  CGRect bounds = CGRectMake( 0, 0, textureWidth, textureHeight );
  CGContextClearRect( context, bounds );
  //CGContextTranslateCTM( context, 0, textureHeight - textureHeight );
  CGContextDrawImage( context, bounds, cgImage );
  
  CGContextRelease(context);

  image->fillWithRGBA(imageData, textureWidth, textureHeight);
  
  _gl->blendFunc(SrcAlpha, OneMinusSrcAlpha);   
  _gl->pixelStorei(Unpack, 1);
  
  GLuint textureID = getTextureID();
  
  _gl->bindTexture(Texture2D, textureID);
  _gl->texParameteri(Texture2D, MinFilter, Linear);
  _gl->texParameteri(Texture2D, MagFilter, Linear);
  _gl->texParameteri(Texture2D, WrapS, ClampToEdge);
  _gl->texParameteri(Texture2D, WrapT, ClampToEdge);
  
  //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, textureWidth, textureHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
  
  _gl->texImage2D(Texture2D, 0, RGBA, textureWidth, textureHeight, 0, RGBA, UnsignedByte, imageData);
  
  return textureID;
}

void GL2::setTextureCoordinates(int size, int stride, const float texcoord[]) {
  glVertexAttribPointer(AttributesGL2.TextureCoord, size, GL_FLOAT, 0, stride, (const void *) texcoord);
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
  
  glUniform1i(UniformsGL2.BillBoard, true);
  
  glUniform1f(UniformsGL2.ViewPortRatio, viewPortRatio);
  
  disableDepthTest();
  
  enableTexture2D();
  color(1, 1, 1, 1);
  
  bindTexture(textureId);

  vertexPointer(3, 0, vertex);
  setTextureCoordinates(2, 0, texcoord);
  
  glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
  
  enableDepthTest();
  
  glUniform1i(UniformsGL2.BillBoard, false);
}

// state handling
void GL2::enableTextures() {
  if (!_enableTextures) {
    glEnableVertexAttribArray(AttributesGL2.TextureCoord);
    _enableTextures = true;
  }
}

void GL2::disableTextures() {
  if (_enableTextures) {
    glDisableVertexAttribArray(AttributesGL2.TextureCoord);
    _enableTextures = false;
  }
}

void GL2::enableTexture2D() {
  if (!_enableTexture2D) {
    glUniform1i(UniformsGL2.EnableTexture, true);
    _enableTexture2D = true;
  }
}

void GL2::disableTexture2D() {
  if (_enableTexture2D) {
    glUniform1i(UniformsGL2.EnableTexture, false);
    _enableTexture2D = false;
  }
}

void GL2::enableVertexColor(float const colors[], float intensity) {
//  if (colors != NULL) {
  if (!_enableVertexColor) {
    glUniform1i(UniformsGL2.EnableColorPerVertex, true);
    glEnableVertexAttribArray(AttributesGL2.Color);
    glVertexAttribPointer(AttributesGL2.Color, 4, GL_FLOAT, 0, 0, colors);
    glUniform1f(UniformsGL2.ColorPerVertexIntensity, intensity);
    _enableVertexColor = true;
  }
//  }
}

void GL2::disableVertexColor() {
  if (_enableVertexColor) {
    glDisableVertexAttribArray(AttributesGL2.Color);
    glUniform1i(UniformsGL2.EnableColorPerVertex, false);
    _enableVertexColor = false;
  }
}

void GL2::enableVertexNormal(float const normals[]) {
//  if (normals != NULL) {
  if (!_enableVertexNormal) {
    glEnableVertexAttribArray(AttributesGL2.Normal);
    glVertexAttribPointer(AttributesGL2.Normal, 3, GL_FLOAT, 0, 0, normals);
    _enableVertexNormal = true;
  }
//  }
}

void GL2::disableVertexNormal() {
  if (_enableVertexNormal) {
    glDisableVertexAttribArray(AttributesGL2.Normal);
    _enableVertexNormal = false;
  }
}

void GL2::enableVerticesPosition() {
  if (!_enableVerticesPosition) {
    glEnableVertexAttribArray(AttributesGL2.Position);
    _enableVerticesPosition = true;
  }
}

void GL2::disableVerticesPosition() {
  if (_enableVerticesPosition) {
    glDisableVertexAttribArray(AttributesGL2.Position);
    _enableVerticesPosition = false;
  }
}

void GL2::enableVertexFlatColor(float r, float g, float b, float a,
                                float intensity) {
  if (!_enableFlatColor) {
    glUniform1i(UniformsGL2.EnableFlatColor, true);
    _enableFlatColor = true;
  }
  glUniform4f(UniformsGL2.FlatColor, r, g, b, a);
  glUniform1f(UniformsGL2.FlatColorIntensity, intensity);
}

void GL2::disableVertexFlatColor() {
  if (_enableFlatColor) {
    glUniform1i(UniformsGL2.EnableFlatColor, false);
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
