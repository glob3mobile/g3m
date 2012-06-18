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

#include "Image_iOS.h"


/*
 IGL* CreateGL()
 {
 return new GL2();
 }*/

struct UniformsStruct {
  GLuint Projection;
  GLuint Modelview;
  GLint Sampler;
  GLint EnableTexture;
  GLint FlatColor;
  
  //FOR BILLBOARDING
  GLint BillBoard;
  GLint ViewPortRatio;
} Uniforms;

struct AttributesStruct {
  GLint Position;
  GLint TextureCoord;
} Attributes;




void GL2::useProgram(unsigned int program) {
  // set shaders
  glUseProgram(program);
  
  // Extract the handles to attributes
  Attributes.Position = glGetAttribLocation(program, "Position");
  Attributes.TextureCoord = glGetAttribLocation(program, "TextureCoord");
  
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
  MutableMatrix44D product = _modelView.multMatrix(m);
  
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


void GL2::enableVertices() {
  glEnableVertexAttribArray(Attributes.Position);
}


void GL2::enableTextures() {
  glEnableVertexAttribArray(Attributes.TextureCoord);
}


void GL2::enableTexture2D() {
  glUniform1i(Uniforms.EnableTexture, true);
}


void GL2::disableTexture2D() {
  glUniform1i(Uniforms.EnableTexture, false);
}


void GL2::disableVertices() {
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

void GL2::drawTriangleStrip(int n, unsigned char *i) {
  glDrawElements(GL_TRIANGLE_STRIP, n, GL_UNSIGNED_BYTE, i);
}

void GL2::drawLines(int n, unsigned char *i) {
  glDrawElements(GL_LINES, n, GL_UNSIGNED_BYTE, i);
}

void GL2::drawLineLoop(int n, unsigned char *i) {
  glDrawElements(GL_LINE_LOOP, n, GL_UNSIGNED_BYTE, i);
}

void GL2::lineWidth(float width) {
  glLineWidth(width);
}

int GL2::getError() {
  return glGetError();
}

int GL2::uploadTexture(const IImage& image, int widthTexture, int heightTexture)
{
//  UIImage * im = ((Image_iOS&) image).getUIImage();
//  
//  int numComponents = 4;
//  CGImageRef imageRef = [im CGImage];
//  
//  //Allocate texture data
//  GLubyte* textureData = new GLubyte[widthTexture * heightTexture * numComponents];  
//  
//  //Creating Context
//  CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
//  NSUInteger bytesPerPixel = 4;
//  NSUInteger bytesPerRow = bytesPerPixel * widthTexture;
//  NSUInteger bitsPerComponent = 8;
//  CGContextRef context = CGBitmapContextCreate(textureData, widthTexture, heightTexture,
//                                               bitsPerComponent, bytesPerRow, colorSpace,
//                                               kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big);
//  CGColorSpaceRelease(colorSpace);
//  
//  CGContextDrawImage(context, CGRectMake(0, 0, widthTexture, heightTexture), imageRef);
//  CGContextRelease(context);
//  
//  glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//
//  GLuint textureID;    
//  glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
//  
//  glGenTextures(1, &textureID);
//  
//  glBindTexture(GL_TEXTURE_2D, textureID);
//  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
//  
//  
//  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, widthTexture, heightTexture, 0, GL_RGBA, GL_UNSIGNED_BYTE, textureData); 
//  
//  return textureID;
  
  
  UIImage * im = ((Image_iOS&) image).getUIImage();

  
  CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
  void *imageData = malloc( heightTexture * widthTexture * 4 );
  CGContextRef context = CGBitmapContextCreate(imageData,
                                               widthTexture, heightTexture,
                                               8, 4 * widthTexture,
                                               colorSpace,
                                               kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big );
  CGColorSpaceRelease( colorSpace );
  CGContextClearRect( context, CGRectMake( 0, 0, widthTexture, heightTexture ) );
  CGContextTranslateCTM( context, 0, heightTexture - heightTexture );
  CGContextDrawImage( context, CGRectMake( 0, 0, widthTexture, heightTexture ), im.CGImage );
  
  
  glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  
  GLuint textureID;    
  glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
  
  glGenTextures(1, &textureID);
  
  glBindTexture(GL_TEXTURE_2D, textureID);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
  
  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, widthTexture, heightTexture, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
  
  CGContextRelease(context);
  
  free(imageData);

  return textureID;
}

void GL2::setTextureCoordinates(int size, int stride, const float texcoord[])
{
  glVertexAttribPointer(Attributes.TextureCoord, size, GL_FLOAT, 0, stride, (const void *) texcoord);
}

void GL2::bindTexture (unsigned int n)
{
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
                        const float x, const float y, const float z,
                        const float viewPortRatio) {
  glUniform1i(Uniforms.BillBoard, true);
  
  const float vertex[] = {
    x, y, z,
    x, y, z,
    x, y, z,
    x, y, z
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
