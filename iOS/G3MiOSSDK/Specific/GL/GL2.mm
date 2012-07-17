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


struct UniformsStruct {
  GLuint Projection;
  GLuint Modelview;
  GLint Sampler;
  GLint EnableTexture;
  GLint FlatColor;
  
  //FOR BILLBOARDING
  GLint BillBoard;
  GLint ViewPortRatio;
  
  //FOR COLOR MIXING
  GLint FlatColorIntensity;
  GLint EnableColorPerVertex;
  GLint EnableFlatColor;
  GLint ColorPerVertexIntensity;
  
} Uniforms;

struct AttributesStruct {
  GLint Position;
  GLint TextureCoord;
  GLint Color;
  GLint Normal;
} Attributes;


void GL2::useProgram(unsigned int program) {
  // set shaders
  glUseProgram(program);
  
  // Extract the handles to attributes
  Attributes.Position = glGetAttribLocation(program, "Position");
  Attributes.TextureCoord = glGetAttribLocation(program, "TextureCoord");
  Attributes.Color = glGetAttribLocation(program, "Color");
  Attributes.Normal = glGetAttribLocation(program, "Normal");
  
  // Extract the handles to uniforms
  Uniforms.Projection = glGetUniformLocation(program, "Projection");
  Uniforms.Modelview = glGetUniformLocation(program, "Modelview");
  Uniforms.Sampler = glGetUniformLocation(program, "Sampler");
  Uniforms.EnableTexture = glGetUniformLocation(program, "EnableTexture");
  Uniforms.FlatColor = glGetUniformLocation(program, "FlatColor");
  
  //BILLBOARDS
  Uniforms.BillBoard = glGetUniformLocation(program, "BillBoard");
  glUniform1i(Uniforms.BillBoard, false); //NOT DRAWING BILLBOARD
  Uniforms.ViewPortRatio = glGetUniformLocation(program, "ViewPortRatio");
  
  //FOR FLAT COLOR MIXING
  Uniforms.FlatColorIntensity = glGetUniformLocation(program, "FlatColorIntensity");
  Uniforms.ColorPerVertexIntensity = glGetUniformLocation(program, "ColorPerVertexIntensity");
  Uniforms.EnableColorPerVertex = glGetUniformLocation(program, "EnableColorPerVertex");
  Uniforms.EnableFlatColor = glGetUniformLocation(program, "EnableFlatColor");
}

void GL2::setProjection(const MutableMatrix44D &projection) {
  float M[16];
  projection.copyToFloatMatrix(M);
  glUniformMatrix4fv(Uniforms.Projection, 1, 0, M);
}

void GL2::loadMatrixf(const MutableMatrix44D &m) {
  float M[16];
  m.copyToFloatMatrix(M);
  
  glUniformMatrix4fv(Uniforms.Modelview, 1, 0, M);
  _modelView = m;
}

void GL2::multMatrixf(const MutableMatrix44D &m) {
  MutableMatrix44D product = _modelView.multiply(m);
  
  float M[16];
  product.copyToFloatMatrix(M);
  glUniformMatrix4fv(Uniforms.Modelview, 1, 0, M);
  _modelView = product;
}

void GL2::popMatrix() {
  _modelView = _matrixStack.back();
  _matrixStack.pop_back();
  
  float M[16];
  _modelView.copyToFloatMatrix(M);
  
  glUniformMatrix4fv(Uniforms.Modelview, 1, 0, M);
}

void GL2::pushMatrix() {
  _matrixStack.push_back(_modelView);
}

void GL2::enableVerticesPosition() {
  glEnableVertexAttribArray(Attributes.Position);
}

void GL2::enableTextures() {
  glEnableVertexAttribArray(Attributes.TextureCoord);
}

void GL2::enableVertexColor(float const colors[], float intensity)
{
  if (colors != NULL){
    glUniform1i(Uniforms.EnableColorPerVertex, true);
    glEnableVertexAttribArray(Attributes.Color);
    glVertexAttribPointer(Attributes.Color, 4, GL_FLOAT, 0, 0, colors);
    glUniform1f(Uniforms.ColorPerVertexIntensity, intensity);
  }
}

void GL2::enableVertexFlatColor(Color c, float intensity)
{
    glUniform1i(Uniforms.EnableFlatColor, true);
    glUniform4f(Uniforms.FlatColor, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    glUniform1f(Uniforms.FlatColorIntensity, intensity);
}

void GL2::disableVertexColor()
{
  glDisableVertexAttribArray(Attributes.Color);
  glUniform1i(Uniforms.EnableColorPerVertex, false);
}

void GL2::disableVertexFlatColor()
{
  glUniform1i(Uniforms.EnableFlatColor, false);
}

void GL2::enableVertexNormal(float const normals[])
{
  if (normals != NULL){
    glEnableVertexAttribArray(Attributes.Normal);
    glVertexAttribPointer(Attributes.Normal, 3, GL_FLOAT, 0, 0, normals);
  }
}

void GL2::disableVertexNormal()
{
  glDisableVertexAttribArray(Attributes.Normal);
}

void GL2::enableTexture2D() {
  glUniform1i(Uniforms.EnableTexture, true);
}

void GL2::disableTexture2D() {
  glUniform1i(Uniforms.EnableTexture, false);
}

void GL2::disableVerticesPosition() {
  glDisableVertexAttribArray(Attributes.Position);
}

void GL2::disableTextures() {
  glDisableVertexAttribArray(Attributes.TextureCoord);
}

void GL2::clearScreen(float r, float g, float b, float a) {
  glClearColor(r, g, b, a);
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
}

void GL2::color(float r, float g, float b, float a) {
  glUniform4f(Uniforms.FlatColor, r, g, b, a);
}

void GL2::enablePolygonOffset(float factor, float units) {
  glEnable(GL_POLYGON_OFFSET_FILL);
  glPolygonOffset(factor, units);
}

void GL2::disablePolygonOffset() {
  glDisable(GL_POLYGON_OFFSET_FILL);
}

void GL2::vertexPointer(int size, int stride, const float vertex[]) {
  glVertexAttribPointer(Attributes.Position, size, GL_FLOAT, 0, stride, (const void *) vertex);
}

void GL2::drawTriangleStrip(int n, const unsigned int i[]) {
  glDrawElements(GL_TRIANGLE_STRIP, n, GL_UNSIGNED_INT, i);
}

void GL2::drawLines(int n, const unsigned int *i) {
  glDrawElements(GL_LINES, n, GL_UNSIGNED_INT, i);
}

void GL2::drawLineLoop(int n, const unsigned int *i) {
  glDrawElements(GL_LINE_LOOP, n, GL_UNSIGNED_INT, i);
}

void GL2::lineWidth(float width) {
  glLineWidth(width);
}

int GL2::getError() {
  return glGetError();
}

int GL2::uploadTexture(const IImage* image, int textureWidth, int textureHeight)
{
  UIImage * im = ((Image_iOS*) image)->getUIImage();
  
  
  CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
  void *imageData = malloc( textureHeight * textureWidth * 4 );
  CGContextRef context = CGBitmapContextCreate(imageData,
                                               textureWidth, textureHeight,
                                               8, 4 * textureWidth,
                                               colorSpace,
                                               kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big );
  CGColorSpaceRelease( colorSpace );
  CGContextClearRect( context, CGRectMake( 0, 0, textureWidth, textureHeight ) );
  CGContextTranslateCTM( context, 0, textureHeight - textureHeight );
  CGContextDrawImage( context, CGRectMake( 0, 0, textureWidth, textureHeight ), im.CGImage );
  
  CGContextRelease(context);

  glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  
  GLuint textureID;    
  glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
  
  glGenTextures(1, &textureID);
  
  glBindTexture(GL_TEXTURE_2D, textureID);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
  
  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, textureWidth, textureHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
  
  free(imageData);
  
  return textureID;
}

void GL2::setTextureCoordinates(int size, int stride, const float texcoord[]) {
  glVertexAttribPointer(Attributes.TextureCoord, size, GL_FLOAT, 0, stride, (const void *) texcoord);
}

void GL2::bindTexture (unsigned int n) {
  glBindTexture(GL_TEXTURE_2D, n);
}

void GL2::depthTest(bool b) {
  if (b) {
    glEnable(GL_DEPTH_TEST);
  } else {
    glDisable(GL_DEPTH_TEST);
  }
}

void GL2::blend(bool b) {
  if (b) {
    glEnable(GL_BLEND);
  } else {
    glDisable(GL_BLEND);
  }
}

void GL2::drawBillBoard(const unsigned int textureId,
                        const Vector3D& pos,
                        const float viewPortRatio) {
  glUniform1i(Uniforms.BillBoard, true);
  
  const float vertex[] = {
    pos.x(), pos.y(), pos.z(),
    pos.x(), pos.y(), pos.z(),
    pos.x(), pos.y(), pos.z(),
    pos.x(), pos.y(), pos.z()
  };
  
  glUniform1f(Uniforms.ViewPortRatio, viewPortRatio);
  
  const static float texcoord[] = {
    1, 1,
    1, 0,
    0, 1,
    0, 0
  };
  
  glDisable(GL_DEPTH_TEST);
  
  glUniform1i(Uniforms.EnableTexture, true);
  glUniform4f(Uniforms.FlatColor, 1.0, 0.0, 0.0, 1);
  
  glBindTexture(GL_TEXTURE_2D, textureId);
  glVertexAttribPointer(Attributes.Position, 3, GL_FLOAT, 0, 0, (const void *) vertex);
  glVertexAttribPointer(Attributes.TextureCoord, 2, GL_FLOAT, 0, 0, (const void *) texcoord);
  
  glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
  
  glEnable(GL_DEPTH_TEST);
  
  glUniform1i(Uniforms.BillBoard, false); //NOT DRAWING BILLBOARD
}

void GL2::deleteTexture(int glTextureId) {
  unsigned int textures[] = {glTextureId};
  glDeleteTextures(1, textures);
}

void GL2::cullFace(bool b, CullFace face) {
  if (b) {
    glEnable(GL_CULL_FACE);  
  }
  else {
    glDisable(GL_CULL_FACE);
  }
  
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
}
