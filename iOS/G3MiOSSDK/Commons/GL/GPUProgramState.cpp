//
//  GPUProgramState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//

#include "GPUProgramState.hpp"

GPUProgramState::~GPUProgramState(){
  clear();
  delete _uniformKeys;
}

void GPUProgramState::clear(){
  _linkedProgram = NULL;
#ifdef C_CODE
  for(std::map<int, GPUUniformValue*> ::const_iterator it = _uniformValues.begin();
      it != _uniformValues.end();
      it++){
    delete it->second;
  }
#endif
  _uniformValues.clear();
  
#ifdef C_CODE
  for(std::map<int, GPUAttributeValue*> ::const_iterator it = _attributesValues.begin();
      it != _attributesValues.end();
      it++){
    delete it->second;
  }
#endif
  _attributesValues.clear();
}

void GPUProgramState::applyValuesToLinkedProgram() const{
#ifdef C_CODE
  for(std::map<int, GPUUniformValue*> ::const_iterator it = _uniformValues.begin();
      it != _uniformValues.end();
      it++){
    GPUUniformValue* v = it->second;
    v->setValueToLinkedUniform();
  }
  
  for(std::map<int, GPUAttributeValue*> ::const_iterator it = _attributesValues.begin();
      it != _attributesValues.end();
      it++){
    GPUAttributeValue* v = it->second;
    v->setValueToLinkedAttribute();
  }
#endif
#ifdef JAVA_CODE
  for (final GPUUniformValue u : _uniformValues.values()){
    u.setValueToLinkedUniform();
  }
  
  for (final GPUAttributeValue a : _attributesValues.values()){
    a.setValueToLinkedAttribute();
  }
#endif
}


void GPUProgramState::linkToProgram(GPUProgram* prog) const{
  
  if (_linkedProgram == prog){
    return; //Already linked
  }
  
#ifdef JAVA_CODE
  
  for (java.util.Map.Entry<Integer, GPUUniformValue> entry : _uniformValues.entrySet()){
    final Integer key = entry.getKey();
    final GPUUniformValue v = entry.getValue();
    
    if (!v.linkToGPUProgram(prog, key)){
      return;
    }
  }
  
  for (java.util.Map.Entry<Integer, GPUAttributeValue> entry : _attributesValues.entrySet()){
    final Integer key = entry.getKey();
    final GPUAttributeValue v = entry.getValue();
    
    if (!v.linkToGPUProgram(prog, key)){
      return;
    }
  }

#endif
#ifdef C_CODE

  for(std::map<int, GPUUniformValue*> ::const_iterator it = _uniformValues.begin();
      it != _uniformValues.end();
      it++){
    
    const int key = it->first;
    GPUUniformValue* v = it->second;
    
    if (!v->linkToGPUProgram(prog, key)){
      return;
    }
  }
  
  for(std::map<int, GPUAttributeValue*> ::const_iterator it = _attributesValues.begin();
      it != _attributesValues.end();
      it++){
    
    const int key = it->first;
    GPUAttributeValue* v = it->second;
    
    if (!v->linkToGPUProgram(prog, key)){
      return;
    }
  }
  
#endif
  
  
  _linkedProgram = prog;
}

void GPUProgramState::applyChanges(GL* gl) const{
  if (_linkedProgram == NULL){
    ILogger::instance()->logError("Trying to use unlinked GPUProgramState.");
  }
  applyValuesToLinkedProgram();
  _linkedProgram->applyChanges(gl);
}

bool GPUProgramState::setGPUUniformValue(GPUUniformKey key, GPUUniformValue* v){
  
  GPUUniform* prevLinkedUniform = NULL;
  bool uniformExisted = false;

#ifdef C_CODE
  const int index = key;
#endif
#ifdef JAVA_CODE
  final int index = key.getValue();
#endif
  
  
#ifdef C_CODE
  std::map<int, GPUUniformValue*> ::iterator it = _uniformValues.find(index);
  if (it != _uniformValues.end()){
    prevLinkedUniform = it->second->getLinkedUniform();
    delete it->second;
    uniformExisted = true;
  }
#endif
#ifdef JAVA_CODE
  GPUUniformValue pv = _uniformValues.get(index);
  if (pv != null){
    uniformExisted = true;
    prevLinkedUniform = pv.getLinkedUniform();
  }
#endif
  
  v->linkToGPUUniform(prevLinkedUniform);
  _uniformValues[index] = v;
  
  if (!uniformExisted){
    onStructureChanged();
  }
  
  return uniformExisted;
}

bool GPUProgramState::setGPUAttributeValue(GPUAttributeKey key, GPUAttributeValue* v){
  GPUAttribute* prevLinkedAttribute = NULL;
  bool attributeExisted = false;

#ifdef C_CODE
  const int index = key;
#endif
#ifdef JAVA_CODE
  final int index = key.getValue();
#endif

#ifdef C_CODE
  std::map<int, GPUAttributeValue*> ::iterator it = _attributesValues.find(index);
  if (it != _attributesValues.end()){
    prevLinkedAttribute = it->second->getLinkedAttribute();
    delete it->second;
    attributeExisted = true;
  }
#endif
#ifdef JAVA_CODE
  GPUAttributeValue pv = _attributesValues.get(index);
  if (pv != null){
    attributeExisted = true;
    prevLinkedAttribute = pv.getLinkedAttribute();
  }
#endif
  
  v->linkToGPUAttribute(prevLinkedAttribute);
  _attributesValues[index] = v;
  
  if (!attributeExisted){
    onStructureChanged();
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

bool GPUProgramState::setUniformMatrixValue(GPUUniformKey key, const MutableMatrix44D& m, bool isTransform){
  GPUUniformValueMatrix4FloatTransform *uv = new GPUUniformValueMatrix4FloatTransform(m, isTransform);
  return setGPUUniformValue(key, uv);
}

void GPUProgramState::setAttributeEnabled(GPUAttributeKey key, bool enabled){
  //TODO: REMOVE FUNCTION
  if (!enabled){
    setAttributeDisabled(key);
  }
}

void GPUProgramState::setAttributeDisabled(GPUAttributeKey key){
  setGPUAttributeValue(key, new GPUAttributeValueDisabled());
}

std::string GPUProgramState::description() const{
  IStringBuilder *isb = IStringBuilder::newStringBuilder();
  isb->addString("PROGRAM STATE\n==========\n");
  //TODO: IMPLEMENT
#ifdef C_CODE
  for(std::map<int, GPUUniformValue*> ::const_iterator it = _uniformValues.begin();
      it != _uniformValues.end();
      it++){
    
    int key = it->first;
    GPUUniformValue* v = it->second;
    
    isb->addString("Uniform ");
    isb->addInt(key);
    isb->addString(":\n");
    isb->addString(v->description() + "\n");
  }
  
  for(std::map<int, GPUAttributeValue*> ::const_iterator it = _attributesValues.begin();
      it != _attributesValues.end();
      it++){
    
    GPUAttributeValue* v = it->second;
    int key = it->first;
    
    isb->addString("Uniform ");
    isb->addInt(key);
    isb->addString(":\n");
    isb->addString(v->description() + "\n");
  }
#endif
  std::string s = isb->getString();
  delete isb;
  return s;
}

std::vector<int>* GPUProgramState::getUniformsKeys() const{
  
  if (_uniformKeys == NULL){
    
    _uniformKeys = new std::vector<int>();
    
#ifdef C_CODE
    for(std::map<int, GPUUniformValue*> ::const_iterator it = _uniformValues.begin();
        it != _uniformValues.end();
        it++){
      _uniformKeys->push_back(it->first);
    }
#endif
    
#ifdef JAVA_CODE
    _uniformKeys.addAll(_uniformValues.keySet());
#endif
    
  }
  return _uniformKeys;
}

std::vector<int>* GPUProgramState::getAttributeKeys() const{
  
  if (_attributeKeys == NULL){
    
    _attributeKeys = new std::vector<int>();
    
#ifdef C_CODE
    for(std::map<int, GPUAttributeValue*> ::const_iterator it = _attributesValues.begin();
        it != _attributesValues.end();
        it++){
      _attributeKeys->push_back(it->first);
    }
#endif
#ifdef JAVA_CODE
    _attributeKeys.addAll(_attributesValues.keySet());
#endif
    
    
  }
  return _attributeKeys;
}

bool GPUProgramState::removeGPUUniformValue(GPUUniformKey key){
  bool uniformExisted = false;
#ifdef C_CODE
  std::map<int, GPUUniformValue*> ::iterator it = _uniformValues.find(key);
  if (it != _uniformValues.end()){
    delete it->second;
    _uniformValues.erase(it);
    uniformExisted = true;
  }
#endif
#ifdef JAVA_CODE
  uniformExisted = (null != _uniformValues.remove(key));
#endif
  
  if (uniformExisted){
    onStructureChanged();
  }
  
  return uniformExisted;
}