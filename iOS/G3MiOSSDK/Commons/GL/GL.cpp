//
//  GL.cpp
//  Glob3 Mobile
//
//  Created by Agustin Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

#include <list>

#include "GL.hpp"

#include "IImage.hpp"
#include "Vector3D.hpp"
#include "Vector2D.hpp"
#include "INativeGL.hpp"
//#include "IIntBuffer.hpp"
#include "IShortBuffer.hpp"
#include "IFactory.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "IGLUniformID.hpp"
#include "IGLTextureId.hpp"

class UniformsStruct {
public:

  IGLUniformID* Projection;
  IGLUniformID* Modelview;
  IGLUniformID* Sampler;
  IGLUniformID* EnableTexture;
  IGLUniformID* FlatColor;
  IGLUniformID* TranslationTexCoord;
  IGLUniformID* ScaleTexCoord;
  IGLUniformID* PointSize;

  //FOR BILLBOARDING
  IGLUniformID* BillBoard;
  IGLUniformID* ViewPortExtent;
  IGLUniformID* TextureExtent;


  //FOR COLOR MIXING
  IGLUniformID* FlatColorIntensity;
  IGLUniformID* EnableColorPerVertex;
  IGLUniformID* EnableFlatColor;
  IGLUniformID* ColorPerVertexIntensity;

  UniformsStruct() {
    Projection = NULL;
    Modelview = NULL;
    Sampler = NULL;
    EnableTexture = NULL;
    FlatColor = NULL;
    TranslationTexCoord = NULL;
    ScaleTexCoord = NULL;
    PointSize = NULL;

    //FOR BILLBOARDING
    BillBoard = NULL;
    ViewPortExtent = NULL;
    TextureExtent = NULL;

    //FOR COLOR MIXING
    FlatColorIntensity = NULL;
    EnableColorPerVertex = NULL;
    EnableFlatColor = NULL;
    ColorPerVertexIntensity = NULL;
  }

  void deleteUniformsIDs(){
    delete Projection;
    delete Modelview;
    delete Sampler;
    delete EnableTexture;
    delete FlatColor;
    delete TranslationTexCoord;
    delete ScaleTexCoord;
    delete PointSize;

    //FOR BILLBOARDING
    delete BillBoard;
    delete ViewPortExtent;
    delete TextureExtent;

    //FOR COLOR MIXING
    delete FlatColorIntensity;
    delete EnableColorPerVertex;
    delete EnableFlatColor;
    delete ColorPerVertexIntensity;
  }

  ~UniformsStruct(){
    deleteUniformsIDs();
  }
} Uniforms;


struct AttributesStruct {
  int Position;
  int TextureCoord;
  int Color;
} Attributes;

int GL::checkedGetAttribLocation(ShaderProgram* program,
                                 const std::string& name) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::checkedGetAttribLocation()");
//  }
  int l = _nativeGL->getAttribLocation(program, name);
  if (l == -1) {
    ILogger::instance()->logError("Error fetching Attribute, Program=%s, Variable=\"%s\"",
                                  program->description().c_str(),
                                  name.c_str());
    _errorGettingLocationOcurred = true;
  }
  return l;
}

IGLUniformID* GL::checkedGetUniformLocation(ShaderProgram* program,
                                            const std::string& name) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::checkedGetUniformLocation()");
//  }

  IGLUniformID* uID = _nativeGL->getUniformLocation(program, name);
  if (!uID->isValid()) {
    ILogger::instance()->logError("Error fetching Uniform, Program=%s, Variable=\"%s\"",
                                  program->description().c_str(),
                                  name.c_str());
    _errorGettingLocationOcurred = true;
  }
  return uID;
}

bool GL::useProgram(ShaderProgram* program) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::useProgram()");
//  }

  if (_program == program) {
    return true;
  }
  _program = program;
  
  // set shaders
  _nativeGL->useProgram(program);

  //Methods checkedGetAttribLocation and checkedGetUniformLocation
  //will turn _errorGettingLocationOcurred to true is that happens
  _errorGettingLocationOcurred = false;

  // Extract the handles to attributes
  Attributes.Position     = checkedGetAttribLocation(program, "Position");
  Attributes.TextureCoord = checkedGetAttribLocation(program, "TextureCoord");
  Attributes.Color        = checkedGetAttribLocation(program, "Color");

  Uniforms.deleteUniformsIDs(); //DELETING

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
  _nativeGL->uniform2f(Uniforms.ScaleTexCoord, _scaleX, _scaleY);
  _nativeGL->uniform2f(Uniforms.TranslationTexCoord, _translationX, _translationY);
  _nativeGL->uniform1f(Uniforms.PointSize, 1);

  //BILLBOARDS
  Uniforms.BillBoard      = checkedGetUniformLocation(program, "BillBoard");
  Uniforms.ViewPortExtent = checkedGetUniformLocation(program, "ViewPortExtent");
  Uniforms.TextureExtent  = checkedGetUniformLocation(program, "TextureExtent");
  
  _nativeGL->uniform1i(Uniforms.BillBoard, 0); //NOT DRAWING BILLBOARD

  //FOR FLAT COLOR MIXING
  Uniforms.FlatColorIntensity      = checkedGetUniformLocation(program, "FlatColorIntensity");
  Uniforms.ColorPerVertexIntensity = checkedGetUniformLocation(program, "ColorPerVertexIntensity");
  Uniforms.EnableColorPerVertex    = checkedGetUniformLocation(program, "EnableColorPerVertex");
  Uniforms.EnableFlatColor         = checkedGetUniformLocation(program, "EnableFlatColor");

  //Return
  return !_errorGettingLocationOcurred;
}

void GL::loadModelView() {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::loadModelView()");
//  }

  _nativeGL->uniformMatrix4fv(Uniforms.Modelview,
                              false,
                              &_modelView);
}

void GL::setProjection(const MutableMatrix44D &projection) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::setProjection()");
//  }

  _nativeGL->uniformMatrix4fv(Uniforms.Projection,
                              false,
                              &projection);
}

void GL::loadMatrixf(const MutableMatrix44D &modelView) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::loadMatrixf()");
//  }

  _modelView = modelView;

  loadModelView();
}

void GL::multMatrixf(const MutableMatrix44D &m) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::multMatrixf()");
//  }

  _modelView = _modelView.multiply(m);

  loadModelView();
}

void GL::popMatrix() {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::popMatrix()");
//  }

  _modelView = _matrixStack.back();
  _matrixStack.pop_back();

  loadModelView();
}

void GL::pushMatrix() {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::pushMatrix()");
//  }

  _matrixStack.push_back(_modelView);
}

void GL::clearScreen(float r, float g, float b, float a) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::clearScreen()");
//  }

  _nativeGL->clearColor(r, g, b, a);
  _nativeGL->clear(GLBufferType::colorBuffer() | GLBufferType::depthBuffer());
}

void GL::color(float r, float g, float b, float a) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::color()");
//  }

  if (
      (_flatColorR != r) ||
      (_flatColorG != g) ||
      (_flatColorB != b) ||
      (_flatColorA != a)
      ) {
    _nativeGL->uniform4f(Uniforms.FlatColor, r, g, b, a);

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
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::transformTexCoords()");
//  }

  if ((_scaleX != scaleX) || (_scaleY != scaleY)) {
    _nativeGL->uniform2f(Uniforms.ScaleTexCoord,
                         scaleX,
                         scaleY);
    _scaleX = scaleX;
    _scaleY = scaleY;
  }

  if ((_translationX != translationX) || (_translationY != translationY)) {
    _nativeGL->uniform2f(Uniforms.TranslationTexCoord,
                         translationX,
                         translationY);
    _translationX = translationX;
    _translationY = translationY;
  }
}

void GL::enablePolygonOffset(float factor, float units) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::enablePolygonOffset()");
//  }

  _nativeGL->enable(GLFeature::polygonOffsetFill());
  _nativeGL->polygonOffset(factor, units);
}

void GL::disablePolygonOffset() {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::disablePolygonOffset()");
//  }

  _nativeGL->disable(GLFeature::polygonOffsetFill());
}

void GL::vertexPointer(int size,
                       int stride,
                       IFloatBuffer* vertices) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::vertexPointer(size=%d, stride=%d, vertices=%s)",
//                                 size,
//                                 stride,
//                                 vertices->description().c_str());
//  }

  if ((_vertices != vertices) ||
      (_verticesTimestamp != vertices->timestamp()) ) {
    _nativeGL->vertexAttribPointer(Attributes.Position, size, false, stride, vertices);
    _vertices = vertices;
    _verticesTimestamp = _vertices->timestamp();
  }
}

//void GL::drawElements(int mode,
//                      IIntBuffer* indices) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::drawElements(%d, %s)",
//                                 mode,
//                                 indices->description().c_str());
//  }
//
//  _nativeGL->drawElements(mode,
//                          indices->size(),
//                          indices);
//}
void GL::drawElements(int mode,
                      IShortBuffer* indices) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::drawElements(%d, %s)",
//                                 mode,
//                                 indices->description().c_str());
//  }

  _nativeGL->drawElements(mode,
                          indices->size(),
                          indices);
}

void GL::drawArrays(int mode,
                    int first,
                    int count) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::drawArrays(%d, %d, %d)",
//                                 mode,
//                                 first,
//                                 count);
//  }

  _nativeGL->drawArrays(mode,
                        first,
                        count);
}

int GL::getError() {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::getError()");
//  }

  return _nativeGL->getError();
}

const IGLTextureId* GL::uploadTexture(const IImage* image,
                                      int format,
                                      bool generateMipmap){
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::uploadTexture()");
//  }

  const IGLTextureId* texId = getGLTextureId();
  if (texId != NULL) {
    //_nativeGL->blendFunc(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
    _nativeGL->pixelStorei(GLAlignment::unpack(), 1);

    _nativeGL->bindTexture(GLTextureType::texture2D(), texId);
    _nativeGL->texParameteri(GLTextureType::texture2D(),
                             GLTextureParameter::minFilter(),
                             GLTextureParameterValue::linear());
    _nativeGL->texParameteri(GLTextureType::texture2D(),
                             GLTextureParameter::magFilter(),
                             GLTextureParameterValue::linear());
    _nativeGL->texParameteri(GLTextureType::texture2D(),
                             GLTextureParameter::wrapS(),
                             GLTextureParameterValue::clampToEdge());
    _nativeGL->texParameteri(GLTextureType::texture2D(),
                             GLTextureParameter::wrapT(),
                             GLTextureParameterValue::clampToEdge());
    _nativeGL->texImage2D(image, format);

    if (generateMipmap) {
      _nativeGL->generateMipmap(GLTextureType::texture2D());
    }
  }
  else {
    ILogger::instance()->logError("can't get a valid texture id\n");
    return NULL;
  }

  return texId;
}

void GL::setTextureCoordinates(int size,
                               int stride,
                               IFloatBuffer* textureCoordinates) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::setTextureCoordinates(size=%d, stride=%d, textureCoordinates=%s)",
//                                 size,
//                                 stride,
//                                 textureCoordinates->description().c_str());
//  }

  if ((_textureCoordinates != textureCoordinates) ||
      (_textureCoordinatesTimestamp != textureCoordinates->timestamp()) ) {
    _nativeGL->vertexAttribPointer(Attributes.TextureCoord, size, false, stride, textureCoordinates);
    _textureCoordinates = textureCoordinates;
    _textureCoordinatesTimestamp = _textureCoordinates->timestamp();
  }
}

void GL::bindTexture(const IGLTextureId* textureId) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::bindTexture()");
//  }

  if (textureId == NULL) {
    ILogger::instance()->logError("Can't bind a NULL texture");
  }
  else {
//    if ((_boundTextureId == NULL) || !_boundTextureId->isEqualsTo(textureId)) {
      _nativeGL->bindTexture(GLTextureType::texture2D(), textureId);
//      _boundTextureId = textureId;
//    }
//    else {
//      ILogger::instance()->logInfo("TextureId %s already bound", textureId->description().c_str());
//    }
  }
}

IFloatBuffer* GL::getBillboardTexCoord() {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::getBillboardTexCoord()");
//  }

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

void GL::startBillBoardDrawing(int viewPortWidth,
                               int viewPortHeight) {
  _nativeGL->uniform1i(Uniforms.BillBoard, 1);
  _nativeGL->uniform2f(Uniforms.ViewPortExtent, viewPortWidth, viewPortHeight);

  color(1, 1, 1, 1);

  setTextureCoordinates(2, 0, getBillboardTexCoord());
}

void GL::stopBillBoardDrawing() {
  _nativeGL->uniform1i(Uniforms.BillBoard, 0);
}


void GL::drawBillBoard(const IGLTextureId* textureId,
                       IFloatBuffer* vertices,
                       int textureWidth,
                       int textureHeight) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::drawBillBoard()");
//  }

  int TODO_refactor_billboard;

  _nativeGL->uniform2f(Uniforms.TextureExtent, textureWidth, textureHeight);

  bindTexture(textureId);

  vertexPointer(3, 0, vertices);

  _nativeGL->drawArrays(GLPrimitive::triangleStrip(), 0, vertices->size() / 3);
}

void GL::setBlendFuncSrcAlpha() {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::setBlendFuncSrcAlpha()");
//  }

  _nativeGL->blendFunc(GLBlendFactor::srcAlpha(), GLBlendFactor::oneMinusSrcAlpha());
}

const IGLTextureId* GL::getGLTextureId() {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::getGLTextureId()");
//  }

  if (_texturesIdBag.size() == 0) {
    //const int bugdetSize = 256;
    const int bugdetSize = 1024;
    //const int bugdetSize = 10240;

    const std::vector<IGLTextureId*> ids = _nativeGL->genTextures(bugdetSize);
    const int idsCount = ids.size();
    for (int i = 0; i < idsCount; i++) {
      // ILogger::instance()->logInfo("  = Created textureId=%s", ids[i]->description().c_str());
      _texturesIdBag.push_front(ids[i]);
    }

    _texturesIdAllocationCounter += idsCount;

    ILogger::instance()->logInfo("= Created %d texturesIds (accumulated %d).",
                                 idsCount,
                                 _texturesIdAllocationCounter);
  }

  //  _texturesIdGetCounter++;

  if (_texturesIdBag.size() == 0) {
    ILogger::instance()->logError("TextureIds bag exhausted");
    return NULL;
  }

  const IGLTextureId* result = _texturesIdBag.back();
  _texturesIdBag.pop_back();

  //  printf("   - Assigning 1 texturesId (#%d) from bag (bag size=%ld). Gets:%ld, Takes:%ld, Delta:%ld.\n",
  //         result.getGLTextureId(),
  //         _texturesIdBag.size(),
  //         _texturesIdGetCounter,
  //         _texturesIdTakeCounter,
  //         _texturesIdGetCounter - _texturesIdTakeCounter);

  return result;
}

void GL::deleteTexture(const IGLTextureId* textureId) {
//  if (_verbose) {
//    ILogger::instance()->logInfo("GL::deleteTexture()");
//  }

  if (textureId != NULL) {
    if ( _nativeGL->deleteTexture(textureId) ) {
      _texturesIdBag.push_back(textureId);
    }
    else {
      delete textureId;
    }

//    if (_boundTextureId != NULL) {
//      if (_boundTextureId->isEqualsTo(textureId)) {
//        _boundTextureId = NULL;
//      }
//    }

    //ILogger::instance()->logInfo("  = delete textureId=%s", texture->description().c_str());
  }
}

void GL::setState(const GLState& state) {

  // Depth Testh
  if (_enableDepthTest != state.isEnabledDepthTest()) {
    _enableDepthTest = state.isEnabledDepthTest();
    if (_enableDepthTest) {
      _nativeGL->enable(GLFeature::depthTest());
    }
    else {
      _nativeGL->disable(GLFeature::depthTest());
    }
  }

  // Blending
  if (_enableBlend != state.isEnabledBlend()) {
    _enableBlend = state.isEnabledBlend();
    if (_enableBlend) {
      _nativeGL->enable(GLFeature::blend());
    }
    else {
      _nativeGL->disable(GLFeature::blend());
    }
  }

  // Textures
  if (_enableTextures != state.isEnabledTextures()) {
    _enableTextures = state.isEnabledTextures();
    if (_enableTextures) {
      _nativeGL->enableVertexAttribArray(Attributes.TextureCoord);
    }
    else {
      _nativeGL->disableVertexAttribArray(Attributes.TextureCoord);
    }
  }

  // Texture2D
  if (_enableTexture2D != state.isEnabledTexture2D()) {
    _enableTexture2D = state.isEnabledTexture2D();
    if (_enableTexture2D) {
      _nativeGL->uniform1i(Uniforms.EnableTexture, 1);
    }
    else {
      _nativeGL->uniform1i(Uniforms.EnableTexture, 0);
    }
  }

  // VertexColor
  if (_enableVertexColor != state.isEnabledVertexColor()) {
    _enableVertexColor = state.isEnabledVertexColor();
    if (_enableVertexColor) {
      _nativeGL->uniform1i(Uniforms.EnableColorPerVertex, 1);
      _nativeGL->enableVertexAttribArray(Attributes.Color);
    }
    else {
      _nativeGL->disableVertexAttribArray(Attributes.Color);
      _nativeGL->uniform1i(Uniforms.EnableColorPerVertex, 0);
    }
  }
  IFloatBuffer* colors = state.getColors();
  if (colors != NULL &&
      ((_colors != colors) ||
      (_colorsTimestamp != colors->timestamp())) ) {
    _nativeGL->vertexAttribPointer(Attributes.Color, 4, false, 0, colors);
    _colors = colors;
    _colorsTimestamp = _colors->timestamp();
  }

  // Vertices Position
  if (_enableVerticesPosition != state.isEnabledVerticesPosition()) {
    _enableVerticesPosition = state.isEnabledVerticesPosition();
    if (_enableVerticesPosition) {
      _nativeGL->enableVertexAttribArray(Attributes.Position);
    }
    else {
      _nativeGL->disableVertexAttribArray(Attributes.Position);
    }
  }

  // Flat Color
  if (_enableFlatColor != state.isEnabledFlatColor()) {
    _enableFlatColor = state.isEnabledFlatColor();
    if (_enableFlatColor) {
      _nativeGL->uniform1i(Uniforms.EnableFlatColor, 1);
    }
    else {
      _nativeGL->uniform1i(Uniforms.EnableFlatColor, 0);
    }
  }

  if (_enableFlatColor) {
    color(state.getFlatColor());
    const float intensity = state.getIntensity();
    if (_flatColorIntensity != intensity) {
      _nativeGL->uniform1f(Uniforms.FlatColorIntensity, intensity);
      _flatColorIntensity = intensity;
    }
  }

  // Cull Face
  if (_enableCullFace != state.isEnabledCullFace()) {
    _enableCullFace = state.isEnabledCullFace();
    if (_enableCullFace) {
      _nativeGL->enable(GLFeature::cullFace());
    }
    else {
      _nativeGL->disable(GLFeature::cullFace());
    }
  }
  const int face = state.getCulledFace();
  if (_cullFace_face != face) {
    _nativeGL->cullFace(face);
    _cullFace_face = face;
  }

  const float lineWidth = state.lineWidth();
  if (_lineWidth != lineWidth) {
    _nativeGL->lineWidth(lineWidth);
    _lineWidth = lineWidth;
  }
  
  const float pointSize = state.pointSize();
  if (_pointSize != pointSize) {
    _nativeGL->uniform1f(Uniforms.PointSize, pointSize);
    _pointSize = pointSize;
  }
}
