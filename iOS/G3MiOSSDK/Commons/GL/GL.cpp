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

int GL::checkedGetAttribLocation(int program, const std::string& name) const{
  int l = _gl->getAttribLocation(program, name);
  if (l == -1){
    ILogger::instance()->logError("Error fetching Attribute, Program = %d, Variable = %s", program, name.c_str());
  }
  return l;
}
int GL::checkedGetUniformLocation(int program, const std::string& name) const{
  int l = _gl->getUniformLocation(program, name);
  if (l == -1){
    ILogger::instance()->logError("Error fetching Uniform, Program = %d, Variable = %s", program, name.c_str());
  }
  return l;
}

void GL::useProgram(unsigned int program) {
  // set shaders
  _gl->useProgram(program);
  
  // Extract the handles to attributes
  Attributes.Position     = checkedGetAttribLocation(program, "Position");
  Attributes.TextureCoord = checkedGetAttribLocation(program, "TextureCoord");
  Attributes.Color        = checkedGetAttribLocation(program, "Color");
  //Attributes.Normal       = checkedGetAttribLocation(program, "Normal");
  
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
}

void GL::loadModelView() {
  if (Uniforms.Modelview == -1){
    ILogger::instance()->logError("Uniforms Modelview Invalid");
  }
  
  static float M[16];
  _modelView.copyToColumnMajorFloatArray(M);
  _gl->uniformMatrix4fv(Uniforms.Modelview, 1, false, M);
}

void GL::setProjection(const MutableMatrix44D &projection) {
  if (Uniforms.Projection == -1){
    ILogger::instance()->logError("Uniforms Projection Invalid");
  }
  
  static float M[16];
  projection.copyToColumnMajorFloatArray(M);
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
  if (Uniforms.FlatColor == -1){
    ILogger::instance()->logError("Uniforms FlatColor Invalid");
  }
  
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
  if (Uniforms.ScaleTexCoord == -1){
    ILogger::instance()->logError("Uniforms ScaleTexCoord Invalid");
  }
  if (Uniforms.TranslationTexCoord == -1){
    ILogger::instance()->logError("Uniforms TranslationTexCoord Invalid");
  }
  
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
  if (Attributes.Position == -1){
    ILogger::instance()->logError("Attribute Position Invalid");
  }
  
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
  if (Uniforms.PointSize == -1){
    ILogger::instance()->logError("Uniforms PointSize Invalid");
  }

  _gl->uniform1f(Uniforms.PointSize, size);
}

GLError GL::getError() {
  return _gl->getError();
}

const GLTextureId GL::uploadTexture(const IImage* image,
                                    int textureWidth, int textureHeight,
                                    bool generateMipmap) {
  const GLTextureId texId = getGLTextureId();
  if (texId.isValid()) {
    const bool lastImageDataIsValid = ((_lastTextureWidth == textureWidth) &&
                                       (_lastTextureHeight == textureHeight) &&
                                       (_lastImageData != NULL));
    
#ifdef C_CODE
    unsigned char* imageData;
    
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
    
    _gl->bindTexture(Texture2D, texId.getGLTextureId());
    _gl->texParameteri(Texture2D, MinFilter, Linear);
    _gl->texParameteri(Texture2D, MagFilter, Linear);
    _gl->texParameteri(Texture2D, WrapS, ClampToEdge);
    _gl->texParameteri(Texture2D, WrapT, ClampToEdge);
    _gl->texImage2D(Texture2D, 0, RGBA, textureWidth, textureHeight, 0, RGBA, UnsignedByte, imageData);
    
    if (generateMipmap) {
      _gl->generateMipmap(Texture2D);
    }
#endif
    
#ifdef JAVA_CODE
    byte[] imageData;
    
    if (lastImageDataIsValid) {
      imageData = _lastImageData;
    }
    else {
      imageData = new byte[textureWidth * textureHeight * 4];
      _lastImageData = imageData;
      _lastTextureWidth = textureWidth;
      _lastTextureHeight = textureHeight;
    }
    
    image.fillWithRGBA8888(imageData, textureWidth, textureHeight);
    
    _gl.blendFunc(GLBlendFactor.SrcAlpha, GLBlendFactor.OneMinusSrcAlpha);
    _gl.pixelStorei(GLAlignment.Unpack, 1);
    
    _gl.bindTexture(GLTextureType.Texture2D, texId.getGLTextureId());
    _gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.MinFilter, GLTextureParameterValue.Linear);
    _gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.MagFilter, GLTextureParameterValue.Linear);
    _gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.WrapS, GLTextureParameterValue.ClampToEdge);
    _gl.texParameteri(GLTextureType.Texture2D, GLTextureParameter.WrapT, GLTextureParameterValue.ClampToEdge);
    _gl.texImage2D(GLTextureType.Texture2D, 0, GLFormat.RGBA, textureWidth, textureHeight, 0, GLFormat.RGBA, GLType.UnsignedByte, imageData);
    
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

void GL::setTextureCoordinates(int size, int stride, const float texcoord[]) {
  if (Attributes.TextureCoord == -1){
    ILogger::instance()->logError("Attribute TextureCoord Invalid");
  }
  
  if (_textureCoordinates != texcoord) {
#ifdef C_CODE
    _gl->vertexAttribPointer(Attributes.TextureCoord, size, Float, false, stride, (const void *) texcoord);
#else
    _gl->vertexAttribPointer(Attributes.TextureCoord, size, GLType.Float, false, stride, (const void *) texcoord);
#endif
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

void GL::drawBillBoard(const GLTextureId& textureId,
                       const Vector3D& pos,
                       const float viewPortRatio) {
  if (Uniforms.BillBoard == -1){
    ILogger::instance()->logError("Uniforms BillBoard Invalid");
  }
  
  if (Uniforms.ViewPortRatio == -1){
    ILogger::instance()->logError("Uniforms ViewPortRatio Invalid");
  }
  
  
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
  if (Attributes.TextureCoord == -1){
    ILogger::instance()->logError("Attribute TextureCoord Invalid");
  }
  
  if (!_enableTextures) {
    _gl->enableVertexAttribArray(Attributes.TextureCoord);
    _enableTextures = true;
  }
}

void GL::disableTextures() {
  if (Attributes.TextureCoord == -1){
    ILogger::instance()->logError("Attribute TextureCoord Invalid");
  }
  
  if (_enableTextures) {
    _gl->disableVertexAttribArray(Attributes.TextureCoord);
    _enableTextures = false;
  }
}

void GL::enableTexture2D() {
  if (Uniforms.EnableTexture == -1){
    ILogger::instance()->logError("Uniforms EnableTexture Invalid");
  }
  
  if (!_enableTexture2D) {
    _gl->uniform1i(Uniforms.EnableTexture, 1);
    _enableTexture2D = true;
  }
}

void GL::disableTexture2D() {
  if (Uniforms.EnableTexture == -1){
    ILogger::instance()->logError("Uniforms EnableTexture Invalid");
  }
  
  if (_enableTexture2D) {
    _gl->uniform1i(Uniforms.EnableTexture, 0);
    _enableTexture2D = false;
  }
}

void GL::enableVertexColor(float const colors[], float intensity) {
  if (Attributes.Color == -1){
    ILogger::instance()->logError("Attribute Color Invalid");
  }
  if (Uniforms.EnableColorPerVertex == -1){
    ILogger::instance()->logError("Uniforms EnableColorPerVertex Invalid");
  }
  if (Uniforms.ColorPerVertexIntensity == -1){
    ILogger::instance()->logError("Uniforms ColorPerVertexIntensity Invalid");
  }
  
  //if (!_enableVertexColor) {
  _gl->uniform1i(Uniforms.EnableColorPerVertex, 1);
  _gl->enableVertexAttribArray(Attributes.Color);
#ifdef C_CODE
  _gl->vertexAttribPointer(Attributes.Color, 4, Float, false, 0, colors);
#else
  _gl->vertexAttribPointer(Attributes.Color, 4, GLType.Float, false, 0, colors);
#endif
  _gl->uniform1f(Uniforms.ColorPerVertexIntensity, intensity);
  //_enableVertexColor = true;
  //}
}

void GL::disableVertexColor() {
  if (Attributes.Color == -1){
    ILogger::instance()->logError("Attribute Color Invalid");
  }
  if (Uniforms.EnableColorPerVertex == -1){
    ILogger::instance()->logError("Uniforms EnableColorPerVertex Invalid");
  }
  
  //  if (_enableVertexColor) {
  _gl->disableVertexAttribArray(Attributes.Color);
  _gl->uniform1i(Uniforms.EnableColorPerVertex, 0);
  //    _enableVertexColor = false;
  //  }
}

void GL::enableVertexNormal(float const normals[]) {
  int TODO_No_Normals_In_Shader;
//  if (Attributes.Normal == -1){
//    ILogger::instance()->logError("Attribute Normal Invalid");
//  }
//  
//  //  if (!_enableVertexNormal) {
//  _gl->enableVertexAttribArray(Attributes.Normal);
//#ifdef C_CODE
//  _gl->vertexAttribPointer(Attributes.Normal, 3, Float, false, 0, normals);
//#else
//  _gl->vertexAttribPointer(Attributes.Normal, 3, GLType.Float, false, 0, normals);
//#endif
//  //    _enableVertexNormal = true;
//  //  }
}

void GL::disableVertexNormal() {
    int TODO_No_Normals_In_Shader;
//  if (Attributes.Normal == -1){
//    ILogger::instance()->logError("Attribute Normal Invalid");
//  }
//  
//  //  if (_enableVertexNormal) {
//  _gl->disableVertexAttribArray(Attributes.Normal);
//  //    _enableVertexNormal = false;
//  //  }
}

void GL::enableVerticesPosition() {
  if (Attributes.Position == -1){
    ILogger::instance()->logError("Attribute Position Invalid");
  }
  
  if (!_enableVerticesPosition) {
    _gl->enableVertexAttribArray(Attributes.Position);
    _enableVerticesPosition = true;
  }
}

void GL::disableVerticesPosition() {
  if (Attributes.Position == -1){
    ILogger::instance()->logError("Attribute Position Invalid");
  }
  
  if (_enableVerticesPosition) {
    _gl->disableVertexAttribArray(Attributes.Position);
    _enableVerticesPosition = false;
  }
}

void GL::enableVertexFlatColor(float r, float g, float b, float a,
                               float intensity) {
  if (Uniforms.EnableFlatColor == -1){
    ILogger::instance()->logError("Uniforms EnableFlatColor Invalid");
  }
  
  if (Uniforms.FlatColorIntensity == -1){
    ILogger::instance()->logError("Uniforms FlatColorIntensity Invalid");
  }
  
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
  if (Uniforms.EnableFlatColor == -1){
    ILogger::instance()->logError("Uniforms EnableFlatColor Invalid");
  }
  
  
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
