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

#include "IImage.hpp"

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


void GL2::clearScreen(float r, float g, float b) {
    glClearColor(r, g, b, 1);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
}


void GL2::color(float r, float g, float b) {
    glUniform4f(Uniforms.FlatColor, r, g, b, 1);
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


