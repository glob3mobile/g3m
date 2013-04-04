//
//  GLState.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_GLState_hpp
#define G3MiOSSDK_GLState_hpp

class IFloatBuffer;

#include "Color.hpp"
#include "GLConstants.hpp"
#include "IFloatBuffer.hpp"
#include "MutableVector2D.hpp"
#include "MutableMatrix44D.hpp"
#include "Vector2D.hpp"

#include <list.h>


class GL;
struct AttributesStruct;
class UniformsStruct;
class GPUProgram;

class GLState {
private:
  bool _depthTest;
  bool _blend;
  bool _textures;
  //bool _texture2D;
  bool _vertexColor;
  bool _verticesPosition;
  bool _flatColor;
  bool _cullFace;
  int  _culledFace;
  
  IFloatBuffer* _colors; //Vertex colors
  int           _colorsTimeStamp;
  float         _intensity;
  float         _flatColorR;
  float         _flatColorG;
  float         _flatColorB;
  float         _flatColorA;
  
  IFloatBuffer* _vertices;
  int           _verticesTimestamp;
  int           _verticesSize;
  int           _verticesStride;
  
  IFloatBuffer* _textureCoordinates;
  int           _textureCoordinatesTimestamp;
  int           _textureCoordinatesSize;
  int           _textureCoordinatesStride;
  float _textureCoordinatesScaleX;
  float _textureCoordinatesScaleY;
  float _textureCoordinatesTranslationX;
  float _textureCoordinatesTranslationY;

  //Texture Extent
  float _textureWidth;
  float _textureHeight;
  
#ifdef C_CODE
  const IGLTextureId* _boundTextureId;
#endif
#ifdef JAVA_CODE
  private IGLTextureId _boundTextureId;
#endif
  
  float _lineWidth;
  float _pointSize;
  
  //Polygon Offset
  bool  _polygonOffsetFill;
  float _polygonOffsetFactor;
  float _polygonOffsetUnits;
  
  //Blending Factors
  int _blendSFactor;
  int _blendDFactor;
  
  //Billboarding
  bool _billboarding;
  
  //Viewport
  int _viewportWidth;
  int _viewportHeight;
  
  //Texture Parameters
  int _pixelStoreIAlignmentUnpack;
  
  //Clear color
  float _clearColorR;
  float _clearColorG;
  float _clearColorB;
  float _clearColorA;
  
//  MutableMatrix44D _projectionMatrix;
  MutableMatrix44D _modelViewMatrix;
  
  GPUProgram* _program;
  
  
  GLState() :
  _depthTest(false),
  _blend(false),
  _textures(false),
  //_texture2D(false),
  _vertexColor(false),
  _verticesPosition(false),
  _flatColor(false),
  _cullFace(true),
  _culledFace(GLCullFace::back()),
  _colors(NULL),
  _colorsTimeStamp(0),
  _intensity(0),
  _flatColorR(0),
  _flatColorG(0),
  _flatColorB(0),
  _flatColorA(0),
  _lineWidth(1),
  _pointSize(1),
  _polygonOffsetFactor(0),
  _polygonOffsetUnits(0),
  _polygonOffsetFill(false),
  _blendDFactor(GLBlendFactor::zero()),
  _blendSFactor(GLBlendFactor::one()),
  _vertices(NULL),
  _verticesTimestamp(0),
  _verticesSize(0),
  _verticesStride(0),
  _textureCoordinates(NULL),
  _textureCoordinatesTimestamp(0),
  _textureCoordinatesSize(0),
  _textureCoordinatesStride(0),
  _textureCoordinatesScaleX(1.0),
  _textureCoordinatesScaleY(1.0),
  _textureCoordinatesTranslationX(0.0),
  _textureCoordinatesTranslationY(0.0),
  _boundTextureId(NULL),
  _billboarding(false),
  _viewportHeight(0),
  _viewportWidth(0),
  _pixelStoreIAlignmentUnpack(-1),
  _clearColorR(0.0),
  _clearColorG(0.0),
  _clearColorB(0.0),
  _clearColorA(0.0),
//  _projectionMatrix(MutableMatrix44D::invalid()),
  _modelViewMatrix(MutableMatrix44D::invalid()),
  _textureWidth(0.0),
  _textureHeight(0.0),
  _program(NULL)
  {
  }
  
  
  
public:
  static GLState* newDefault() {
    return new GLState();
  }
  
  explicit GLState(const GLState& parentState) :
  _depthTest(parentState._depthTest),
  _blend(parentState._blend),
  _textures(parentState._textures),
//  _texture2D(parentState._texture2D),
  _vertexColor(parentState._vertexColor),
  _verticesPosition(parentState._verticesPosition),
  _flatColor(parentState._flatColor),
  _cullFace(parentState._cullFace),
  _culledFace(parentState._culledFace),
  _colors(parentState._colors),
  _colorsTimeStamp(parentState._colorsTimeStamp),
  _intensity(parentState._intensity),
  _flatColorR(parentState._flatColorR),
  _flatColorG(parentState._flatColorG),
  _flatColorB(parentState._flatColorB),
  _flatColorA(parentState._flatColorA),
  _lineWidth(parentState._lineWidth),
  _pointSize(parentState._pointSize),
  _polygonOffsetFactor(parentState._polygonOffsetFactor),
  _polygonOffsetUnits(parentState._polygonOffsetUnits),
  _polygonOffsetFill(parentState._polygonOffsetFill),
  _blendDFactor(parentState._blendDFactor),
  _blendSFactor(parentState._blendSFactor),
  _vertices(parentState._vertices),
  _verticesTimestamp(parentState._verticesTimestamp),
  _verticesSize(parentState._verticesSize),
  _verticesStride(parentState._verticesStride),
  _textureCoordinates(parentState._textureCoordinates),
  _textureCoordinatesTimestamp(parentState._textureCoordinatesTimestamp),
  _textureCoordinatesSize(parentState._textureCoordinatesSize),
  _textureCoordinatesStride(parentState._textureCoordinatesStride),
  _textureCoordinatesScaleX(parentState._textureCoordinatesScaleX),
  _textureCoordinatesScaleY(parentState._textureCoordinatesScaleY),
  _textureCoordinatesTranslationX(parentState._textureCoordinatesTranslationX),
  _textureCoordinatesTranslationY(parentState._textureCoordinatesTranslationY),
  _boundTextureId(parentState._boundTextureId),
  _billboarding(parentState._billboarding),
  _viewportWidth(parentState._viewportWidth),
  _viewportHeight(parentState._viewportHeight),
  _pixelStoreIAlignmentUnpack(parentState._pixelStoreIAlignmentUnpack),
  _clearColorR(parentState._clearColorR),
  _clearColorG(parentState._clearColorG),
  _clearColorB(parentState._clearColorB),
  _clearColorA(parentState._clearColorA),
//  _projectionMatrix(parentState._projectionMatrix),
  _modelViewMatrix(parentState._modelViewMatrix),
  _program(parentState._program)
  {
  }
  
  ~GLState() {}
  
  void enableBillboarding(){
    _billboarding = true;
  }
  void disableBillboarding(){
    _billboarding = false;
  }
  
  void setViewportSize(int w, int h){
    _viewportWidth = w;
    _viewportHeight = h;
  }
  
  void enableDepthTest() {
      _depthTest = true;
  }
  void disableDepthTest() {
      _depthTest = false;
  }
  bool isEnabledDepthTest() const { return _depthTest; }
  
  void enableBlend() {
      _blend = true;
  }
  void disableBlend() {
      _blend = false;
  }
  bool isEnabledBlend() const { return _blend; }
  
  void enableTextures() {
      _textures = true;
  }
  void disableTextures() {
      _textures = false;
  }
  bool isEnabledTextures() const { return _textures; }
  
//  void enableTexture2D() {
//      _texture2D = true;
//  }
//  void disableTexture2D() {
//      _texture2D = false;
//  }
//  bool isEnabledTexture2D() const { return _texture2D; }
  
  void enableVertexColor(IFloatBuffer* colors,
                         float intensity) {
      _vertexColor  = true;
      _colors       = colors;
      _intensity    = intensity;
      _colorsTimeStamp = colors->timestamp();
  }
  void disableVertexColor() { _vertexColor = false; }
  bool isEnabledVertexColor() const { return _vertexColor; }
  IFloatBuffer* getColors() const { return _colors; }
  float getIntensity() const { return _intensity; }
  
  void enableVerticesPosition() {
    _verticesPosition = true;
  }
  
  void disableVerticesPosition() { _verticesPosition = false; }
  bool isEnabledVerticesPosition() const { return _verticesPosition; }
  
  void enableFlatColor(const Color& color,
                       float intensity) {
    _flatColor = true;
    _flatColorR = color.getRed();
    _flatColorG = color.getGreen();
    _flatColorB = color.getBlue();
    _flatColorA= color.getAlpha();
    _intensity = intensity;
  }
  void disableFlatColor() { _flatColor = false; }
  bool isEnabledFlatColor() const { return _flatColor; }
  Color getFlatColor() const {
    return Color::fromRGBA(_flatColorR, _flatColorG, _flatColorB, _flatColorA);
  }
  
  void enableCullFace(int face) {
    _cullFace   = true;
    _culledFace = face;
  }
  void disableCullFace() { _cullFace = false; }
  bool isEnabledCullFace() const { return _cullFace; }
  int getCulledFace() const { return _culledFace; }
  
  void setLineWidth(float lineWidth) { _lineWidth = lineWidth; }
  float lineWidth() const { return _lineWidth; }
  
  void setPointSize(float ps) { _pointSize = ps;}
  float pointSize() const { return _pointSize;}
  
  void enablePolygonOffsetFill(float factor, float units){
    _polygonOffsetFill = true;
    _polygonOffsetFactor = factor;
    _polygonOffsetUnits = units;
  }
  void disPolygonOffsetFill(){
    _polygonOffsetFill = false;
  }
  
  bool getPolygonOffsetFill() const { return _polygonOffsetFill;}
  float getPolygonOffsetUnits() const { return _polygonOffsetUnits;}
  float getPolygonOffsetFactor() const { return _polygonOffsetFactor;}
  
  void setBlendFactors(int sFactor, int dFactor) {
    _blendSFactor = sFactor;
    _blendDFactor = dFactor;
  }
  
  void setVertices(IFloatBuffer* vertices, int size, int stride) {
    _vertices      = vertices;
    _verticesTimestamp = vertices->timestamp();
    _verticesSize = size;
    _verticesStride = stride;
  }
  
  void setTextureCoordinates(IFloatBuffer* texCoors, int size, int stride){
    _textureCoordinates = texCoors;
    _textureCoordinatesTimestamp = texCoors->timestamp();
    _textureCoordinatesSize = size;
    _textureCoordinatesStride = stride;
  }
  
  void scaleTextureCoordinates(float x, float y){
    _textureCoordinatesScaleX = x;
    _textureCoordinatesScaleY = y;
  }
  
  void scaleTextureCoordinates(const MutableVector2D& scale){
    _textureCoordinatesScaleX = (float) scale.x();
    _textureCoordinatesScaleY = (float) scale.y();
  }
  
  void scaleTextureCoordinates(const Vector2D& scale){
    _textureCoordinatesScaleX = (float) scale.x();
    _textureCoordinatesScaleY = (float) scale.y();
  }
  
  void translateTextureCoordinates(float x, float y){
    _textureCoordinatesTranslationX = x;
    _textureCoordinatesTranslationY = y;
  }
  
  void translateTextureCoordinates(const MutableVector2D& translation){
    _textureCoordinatesTranslationX = (float) translation.x();
    _textureCoordinatesTranslationY = (float) translation.y();
  }
  
  void translateTextureCoordinates(const Vector2D& translation){
    _textureCoordinatesTranslationX = (float) translation.x();
    _textureCoordinatesTranslationY = (float) translation.y();
  }
  
  void bindTexture(const IGLTextureId* textureId){
    _boundTextureId = textureId;
  }
  
  const IGLTextureId* getBoundTexture() const{
    return _boundTextureId;
  }
  
  void setPixelStoreIAlignmentUnpack(int p){
    _pixelStoreIAlignmentUnpack = p;
  }
  
  void setClearColor(const Color& color){
    _clearColorR = color.getRed();
    _clearColorG = color.getGreen();
    _clearColorB = color.getBlue();
    _clearColorA = color.getAlpha();
  }
  
//  void setProjectionMatrix(const MutableMatrix44D& projection){
//    _projectionMatrix = projection;
//  }
  
  void setModelViewMatrix(const MutableMatrix44D& mv){
    _modelViewMatrix = mv;
  }
  
  void multiplyModelViewMatrix(const MutableMatrix44D& mv){
    _modelViewMatrix = _modelViewMatrix.multiply(mv);
  }
  
  void setTextureExtent(float w, float h){
    _textureHeight = h;
    _textureWidth = w;
  }
  
  void applyChanges(GL* gl, GLState& currentState, const AttributesStruct& attributes,const UniformsStruct& uniforms) const;
  
  void setProgram(GPUProgram* program){
    _program = program;
  }
  
//  GPUProgram* getProgram() const{
//    return _program;
//  }
  
};

#endif
