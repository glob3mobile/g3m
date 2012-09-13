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

#include "IIntBuffer.hpp"

#include "IFactory.hpp"

#include "FloatBufferBuilderFromCartesian2D.hpp"

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
} Attributes;

int GL::checkedGetAttribLocation(int program, const std::string& name) {
  int l = _gl->getAttribLocation(program, name);
  if (l == -1) {
    ILogger::instance()->logError("Error fetching Attribute, Program = %d, Variable = %s", program, name.c_str());
    _errorGettingLocationOcurred = true;
  }
  return l;
}
int GL::checkedGetUniformLocation(int program, const std::string& name) {
  int l = _gl->getUniformLocation(program, name);
  if (l == -1) {
    ILogger::instance()->logError("Error fetching Uniform, Program = %d, Variable = %s", program, name.c_str());
    _errorGettingLocationOcurred = true;
  }
  return l;
}

bool GL::useProgram(unsigned int program) {
  // set shaders
  _gl->useProgram(program);
  
  //Methods checkedGetAttribLocation and checkedGetUniformLocation
  //will turn _errorGettingLocationOcurred to true is that happens
  _errorGettingLocationOcurred = false;
  
  // Extract the handles to attributes
  Attributes.Position     = checkedGetAttribLocation(program, "Position");
  Attributes.TextureCoord = checkedGetAttribLocation(program, "TextureCoord");
  Attributes.Color        = checkedGetAttribLocation(program, "Color");
  
  // Extract the handles to uniforms
  Uniforms.Projection          = checkedGetUniformLocation(program, "Projection");
  Uniforms.Modelview           = checkedGetUniformLocation(program, "Modelview");
  Uniforms.Sampler             = checkedGetUniformLocation(program, "Sampler");
  Uniforms.EnableTexture       = checkedGetUniformLocation(program, "EnableTexture");
  Uniforms.FlatColor           = checkedGetUniformLocation(program, "FlatColor");
  Uniforms.TranslationTexCoord = checkedGetUniformLocation(program, "TranslationTexCoord");
  Uniforms.ScaleTexCoord       = checkedGetUniformLocation(program, "ScaleTexCoord");
  Uniforms.PointSize           = checkedGetUniformLocation(program, "PointSize");
  
  // default values
  _gl->uniform2f(Uniforms.ScaleTexCoord, _scaleX, _scaleY);
  _gl->uniform2f(Uniforms.TranslationTexCoord, _translationX, _translationY);
  _gl->uniform1f(Uniforms.PointSize, 1);
  
  //BILLBOARDS
  Uniforms.BillBoard     = checkedGetUniformLocation(program, "BillBoard");
  Uniforms.ViewPortRatio = checkedGetUniformLocation(program, "ViewPortRatio");
  _gl->uniform1i(Uniforms.BillBoard, 0); //NOT DRAWING BILLBOARD
  
  //FOR FLAT COLOR MIXING
  Uniforms.FlatColorIntensity      = checkedGetUniformLocation(program, "FlatColorIntensity");
  Uniforms.ColorPerVertexIntensity = checkedGetUniformLocation(program, "ColorPerVertexIntensity");
  Uniforms.EnableColorPerVertex    = checkedGetUniformLocation(program, "EnableColorPerVertex");
  Uniforms.EnableFlatColor         = checkedGetUniformLocation(program, "EnableFlatColor");
  
  //Return
  return !_errorGettingLocationOcurred;
}

void GL::loadModelView() {
#ifdef C_CODE
  float* M = _modelView.getColumnMajorFloatArray();
#else
  float[] M = _modelView.getColumnMajorFloatArray();
#endif
  _gl->uniformMatrix4fv(Uniforms.Modelview, 1, false, M);
}

void GL::setProjection(const MutableMatrix44D &projection) {
#ifdef C_CODE
  float* M = projection.getColumnMajorFloatArray();
#else
  float[] M = projection.getColumnMajorFloatArray();
#endif
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

void GL::vertexPointer(int size, int stride, IFloatBuffer* vertices) {
  if ((_vertices != vertices) ||
      (_vertices->timestamp() != vertices->timestamp()) ) {
    
    _gl->vertexAttribPointer(Attributes.Position, size, false, stride, vertices);
    _vertices = vertices;
  }
}

void GL::drawTriangleStrip(IIntBuffer* indices) {
#ifdef C_CODE
  _gl->drawElements(TriangleStrip,
                    indices->size(),
                    indices);
#else
  _gl->drawElements(GLPrimitive.TriangleStrip,
                    indices.size(),
                    indices);
#endif
}

void GL::drawLines(IIntBuffer* indices) {
#ifdef C_CODE
  _gl->drawElements(Lines,
                    indices->size(),
                    indices);
#else
  _gl->drawElements(GLPrimitive.Lines,
                    indices.size(),
                    indices);
#endif
}

void GL::drawLineLoop(IIntBuffer* indices) {
#ifdef C_CODE
  _gl->drawElements(LineLoop,
                    indices->size(),
                    indices);
#else
  _gl->drawElements(GLPrimitive.LineLoop,
                    indices.size(),
                    indices);
#endif
}

void GL::drawPoints(IIntBuffer* indices) {
#ifdef C_CODE
  _gl->drawElements(Points,
                    indices->size(),
                    indices);
#else
  _gl->drawElements(GLPrimitive.Points,
                    indices.size(),
                    indices);
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

const GLTextureId GL::uploadTexture(const IImage* image, GLFormat format, bool generateMipmap){
  const GLTextureId texId = getGLTextureId();
  if (texId.isValid()) {
#ifdef C_CODE
    _gl->blendFunc(SrcAlpha, OneMinusSrcAlpha);
    _gl->pixelStorei(Unpack, 1);
    
    _gl->bindTexture(Texture2D, texId.getGLTextureId());
    _gl->texParameteri(Texture2D, MinFilter, Linear);
    _gl->texParameteri(Texture2D, MagFilter, Linear);
    _gl->texParameteri(Texture2D, WrapS, ClampToEdge);
    _gl->texParameteri(Texture2D, WrapT, ClampToEdge);
    _gl->texImage2D(image, format);
    
    if (generateMipmap) {
      _gl->generateMipmap(Texture2D);
    }
#endif
#ifdef JAVA_CODE
    _gl.blendFunc(GLBlendFactor.SrcAlpha, GLBlendFactor.OneMinusSrcAlpha);
    _gl.pixelStorei(GLAlignment.Unpack, 1);
    
    _gl.bindTexture(GLTextureType.Texture2D, texId.getGLTextureId());
    _gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.MinFilter, GLTextureParameterValue.Linear);
    _gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.MagFilter, GLTextureParameterValue.Linear);
    _gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.WrapS, GLTextureParameterValue.ClampToEdge);
    _gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.WrapT, GLTextureParameterValue.ClampToEdge);
    _gl.texImage2D(image, format);
    
    if (generateMipmap) {
      _gl.generateMipmap(GLTextureType.Texture2D);
    }
#endif
  }
  else {
    printf("can't get a valid texture id\n");
  }
  
  return texId;
  
  
}

void GL::setTextureCoordinates(int size, int stride, IFloatBuffer* texcoord) {
  if ((_textureCoordinates != texcoord) ||
      (_textureCoordinates->timestamp() != texcoord->timestamp()) ) {
    _gl->vertexAttribPointer(Attributes.TextureCoord, size, false, stride, texcoord);
    _textureCoordinates = texcoord;
  }
}

void GL::bindTexture(const GLTextureId& textureId) {
#ifdef C_CODE
  _gl->bindTexture(Texture2D, textureId.getGLTextureId());
#else
  _gl->bindTexture(GLTextureType.Texture2D, textureId.getGLTextureId());
#endif
}

IFloatBuffer* GL::getBillboardTexCoord() {
  
  if (_billboardTexCoord == NULL) {
    FloatBufferBuilderFromCartesian2D texCoor;
    texCoor.add(1,1);
    texCoor.add(1,0);
    texCoor.add(0,1);
    texCoor.add(0,0);
    _billboardTexCoord = texCoor.create();
  }
  
  return _billboardTexCoord;
}

void GL::drawBillBoard(const GLTextureId& textureId,
                       IFloatBuffer* vertices,
                       const float viewPortRatio) {
  int TODO_refactor_billboard;
  
  _gl->uniform1i(Uniforms.BillBoard, 1);
  
  _gl->uniform1f(Uniforms.ViewPortRatio, viewPortRatio);
  
  disableDepthTest();
  
  enableTexture2D();
  color(1, 1, 1, 1);
  
  bindTexture(textureId);
  
  vertexPointer(3, 0, vertices);
  setTextureCoordinates(2, 0, getBillboardTexCoord());
  
#ifdef C_CODE
  _gl->drawArrays(TriangleStrip, 0, vertices->size() / 3);
#else
  _gl->drawArrays(GLPrimitive.TriangleStrip, 0, vertices->size() / 3);
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

void GL::enableVertexColor(IFloatBuffer* colors, float intensity) {
  
  if (!_enableVertexColor) {
    _gl->uniform1i(Uniforms.EnableColorPerVertex, 1);
    _gl->enableVertexAttribArray(Attributes.Color);
    _enableVertexColor = true;
  }
  
  if ((_colors != colors) ||
      (_colors->timestamp() != colors->timestamp()) ) {
    _gl->vertexAttribPointer(Attributes.Color, 4, false, 0, colors);
    _colors = colors;
  }
  
  _gl->uniform1f(Uniforms.ColorPerVertexIntensity, intensity);
}

void GL::disableVertexColor() {
  if (_enableVertexColor) {
    _gl->disableVertexAttribArray(Attributes.Color);
    _gl->uniform1i(Uniforms.EnableColorPerVertex, 0);
    _enableVertexColor = false;
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

void GL::setBlendFuncSrcAlpha() {
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

const GLTextureId GL::getGLTextureId() {
  if (_texturesIdBag.size() == 0) {
    const int bugdetSize = 256;
    
    printf("= Creating %d texturesIds...\n", bugdetSize);
    
    const std::vector<GLTextureId> ids = _gl->genTextures(bugdetSize);
    
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
  
  const GLTextureId result = _texturesIdBag.back();
  _texturesIdBag.pop_back();
  
  //  printf("   - Assigning 1 texturesId (#%d) from bag (bag size=%ld). Gets:%ld, Takes:%ld, Delta:%ld.\n",
  //         result.getGLTextureId(),
  //         _texturesIdBag.size(),
  //         _texturesIdGetCounter,
  //         _texturesIdTakeCounter,
  //         _texturesIdGetCounter - _texturesIdTakeCounter);
  
  return result;
}

void GL::deleteTexture(const GLTextureId& textureId) {
  if (!textureId.isValid()) {
    return;
  }
  const int textures[] = {
    textureId.getGLTextureId()
  };
  _gl->deleteTextures(1, textures);
  
  _texturesIdBag.push_back(textureId);
  
  _texturesIdTakeCounter++;
}
