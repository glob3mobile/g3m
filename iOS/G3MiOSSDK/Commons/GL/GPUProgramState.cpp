//
//  GPUProgramState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//

#include "GPUProgramState.hpp"

void GPUProgramState::onUniformsChanged(){
  _uniformsCode = 0;
  for (int i = 0; i < 32; i++) {
    if (_uniformValues[i] != NULL){
      _highestUniformKey = i;
    }
  }
}
void GPUProgramState::onAttributesChanged(){
  _attributeCode = 0;
  for (int i = 0; i < 32; i++) {
    if (_attributeValues[i] != NULL){
      _highestAttributeKey = i;
    }
  }
}

//void GPUProgramState::onStructureChanged(){
////  delete _uniformKeys;
////  _uniformKeys = NULL;
//  //    _linkedProgram = NULL;
//  _uniformsCode = 0;
//  _attributeCode = 0;
//
////  if (_attributeKeys != NULL){
////    delete _attributeKeys;
////    _attributeKeys = NULL;
////  }
//
//  _highestAttributeKey = 0;
//  _highestUniformKey = 0;
//  for (int i = 0; i < 32; i++) {
//    if (_uniformValues[i] != NULL){
//      _highestUniformKey = i;
//    }
//    if (_attributeValues[i] != NULL){
//      _highestAttributeKey = i;
//    }
//  }
//}

GPUProgramState::~GPUProgramState(){
  clear();
  //  delete _uniformKeys;
}

void GPUProgramState::clear(){
  //  _linkedProgram = NULL;

  for (int i = 0; i < 32; i++) {
    //delete _uniformValues[i];
    GPUUniformValue* u = _uniformValues[i];
    if (u != NULL){
      u->_release();
      _uniformValues[i] = NULL;
    }
    //    delete _attributeValues[i];
    GPUAttributeValue* a = _attributeValues[i];
    if (a != NULL){
      a->_release();
      _attributeValues[i] = NULL;
    }
  }
}

//void GPUProgramState::applyValuesToLinkedProgram() const{
//
//  for (int i = 0; i < 32; i++) {
//    GPUUniformValue* u = _uniformValues[i];
//    if (u != NULL){
//      u->setValueToLinkedUniform();
//    }
//    GPUAttributeValue* a = _attributeValues[i];
//    if (a != NULL){
//      a->setValueToLinkedAttribute();
//    }
//  }
//}

void GPUProgramState::applyValuesToProgram(GPUProgram* prog) const{

  for (int i = 0; i <= _highestUniformKey; i++) {
    GPUUniformValue* u = _uniformValues[i];
    if (u != NULL){
      prog->setGPUUniformValue(i, u);
    }
  }

  for (int i = 0; i <= _highestAttributeKey; i++) {
    GPUAttributeValue* a = _attributeValues[i];
    if (a != NULL){
      prog->setGPUAttributeValue(i, a);
    }
  }

  //  for (int i = 0; i < 32; i++) {
  //    GPUUniformValue* u = _uniformValues[i];
  //    if (u != NULL){
  //      prog->setGPUUniformValue(i, u);
  //    }
  //    GPUAttributeValue* a = _attributeValues[i];
  //    if (a != NULL){
  //      prog->setGPUAttributeValue(i, a);
  //    }
  //  }
}

//void GPUProgramState::linkToProgram(GPUProgram* prog) const{
//
//  if (_linkedProgram == prog){
//    return; //Already linked
//  }
//
//  for (int i = 0; i < 32; i++) {
//    GPUUniformValue* u = _uniformValues[i];
//    if (u != NULL){
//      if (!u->linkToGPUProgram(prog, i)){
//        return;
//      }
//    }
//    GPUAttributeValue* a = _attributeValues[i];
//    if (a != NULL){
//      if (!a->linkToGPUProgram(prog, i)){
//        return;
//      }
//    }
//  }
//
//  _linkedProgram = prog;
//}

//void GPUProgramState::applyChanges(GL* gl) const{
//  if (_linkedProgram == NULL){
//    ILogger::instance()->logError("Trying to use unlinked GPUProgramState.");
//  }
//  applyValuesToLinkedProgram();
//  _linkedProgram->applyChanges(gl);
//}

bool GPUProgramState::setGPUUniformValue(GPUUniformKey key, GPUUniformValue* v){

  //  GPUUniform* prevLinkedUniform = NULL;
  bool uniformExisted = false;

#ifdef C_CODE
  const int index = key;
#endif
#ifdef JAVA_CODE
  final int index = key.getValue();
#endif

  GPUUniformValue* u = _uniformValues[index];
  if (u != NULL){
    //    prevLinkedUniform = u->getLinkedUniform();
    //    delete u;
    u->_release();
    uniformExisted = true;
  }

  //  v->linkToGPUUniform(prevLinkedUniform);
  _uniformValues[index] = v;

  if (!uniformExisted){
    onUniformsChanged();
  }

  return uniformExisted;
}

bool GPUProgramState::setGPUAttributeValue(GPUAttributeKey key, GPUAttributeValue* v){
  //  GPUAttribute* prevLinkedAttribute = NULL;
  bool attributeExisted = false;

#ifdef C_CODE
  const int index = key;
#endif
#ifdef JAVA_CODE
  final int index = key.getValue();
#endif

  GPUAttributeValue* a = _attributeValues[index];
  if (a != NULL){
    //    prevLinkedAttribute = a->getLinkedAttribute();
    a->_release();
    attributeExisted = true;
  }

  //  v->linkToGPUAttribute(prevLinkedAttribute);
  _attributeValues[index] = v;

  if (!attributeExisted){
    onAttributesChanged();
  }

  return attributeExisted;
}

bool GPUProgramState::setAttributeValue(GPUAttributeKey key,
                                        IFloatBuffer* buffer, int attributeSize,
                                        int arrayElementSize, int index, bool normalized, int stride){
  switch (attributeSize) {
    case 1:
      return setGPUAttributeValue(key, new GPUAttributeValueVec1Float(buffer, arrayElementSize, index, stride, normalized) );
    case 2:
      return setGPUAttributeValue(key, new GPUAttributeValueVec2Float(buffer, arrayElementSize, index, stride, normalized) );
    case 3:
      return setGPUAttributeValue(key, new GPUAttributeValueVec3Float(buffer, arrayElementSize, index, stride, normalized) );
    case 4:
      return setGPUAttributeValue(key, new GPUAttributeValueVec4Float(buffer, arrayElementSize, index, stride, normalized) );
    default:
      ILogger::instance()->logError("Invalid size for Attribute.");
      return false;
  }
}

bool GPUProgramState::setUniformValue(GPUUniformKey key, bool b){
  return setGPUUniformValue(key, new GPUUniformValueBool(b) );
}

bool GPUProgramState::setUniformValue(GPUUniformKey key, float f){
  return setGPUUniformValue(key, new GPUUniformValueFloat(f));
}

bool GPUProgramState::setUniformValue(GPUUniformKey key, const Vector2D& v){
  return setGPUUniformValue(key, new GPUUniformValueVec2Float(v._x, v._y));
}

bool GPUProgramState::setUniformValue(GPUUniformKey key, double x, double y, double z, double w){
  return setGPUUniformValue(key, new GPUUniformValueVec4Float(x,y,z,w));
}

bool GPUProgramState::setUniformValue(GPUUniformKey key, double x, double y){
  return setGPUUniformValue(key, new GPUUniformValueVec2Float(x, y));
}

//bool GPUProgramState::setUniformMatrixValue(GPUUniformKey key, const MutableMatrix44D& m, bool isTransform){
//  GPUUniformValueMatrix4FloatTransform *uv = new GPUUniformValueMatrix4FloatTransform(m, isTransform);
//  return setGPUUniformValue(key, uv);
//}

//void GPUProgramState::setAttributeEnabled(GPUAttributeKey key, bool enabled){
//  //TODO: REMOVE FUNCTION
//  if (!enabled){
//    setAttributeDisabled(key);
//  }
//}

void GPUProgramState::setAttributeDisabled(GPUAttributeKey key){
  setGPUAttributeValue(key, new GPUAttributeValueDisabled());
}

std::string GPUProgramState::description() const{
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("PROGRAM STATE\n==========\n");

  for(int i = 0; i <= _highestUniformKey; i++){
    GPUUniformValue* v = _uniformValues[i];
    if (v != NULL){
      isb->addString("Uniform ");
      isb->addInt(i);
      isb->addString(":\n");
      isb->addString(v->description() + "\n");
    }
  }

  for(int i = 0; i <= _highestAttributeKey; i++){
    GPUAttributeValue* v = _attributeValues[i];
    if (v != NULL){
      isb->addString("Uniform ");
      isb->addInt(i);
      isb->addString(":\n");
      isb->addString(v->description() + "\n");
    }
  }

  std::string s = isb->getString();
  delete isb;
  return s;
}

bool GPUProgramState::removeGPUUniformValue(GPUUniformKey key){

#ifdef C_CODE
  const int index = key;
#endif
#ifdef JAVA_CODE
  final int index = key.getValue();
#endif

  if (_uniformValues[index] != NULL){
    _uniformValues[index]->_release();
    _uniformValues[index] = NULL;
    onUniformsChanged();
    return true;
  }
  return false;
}

int GPUProgramState::getUniformsCode() const{
  if (_uniformsCode == 0){
    for (int i = 0; i <= _highestUniformKey; i++){
      if (_uniformValues[i] != NULL){
        _uniformsCode = _uniformsCode | GPUVariable::getUniformCode(i);
      }
    }
  }
  return _uniformsCode;
}

int GPUProgramState::getAttributesCode() const{
  if (_attributeCode == 0){
    for (int i = 0; i <= _highestAttributeKey; i++){
      if (_attributeValues[i] != NULL){
        _attributeCode = _attributeCode | GPUVariable::getAttributeCode(i);
      }
    }
  }
  return _attributeCode;
}