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
  _lastProgramUsed = NULL;
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
  
  if (_lastProgramUsed == prog){
    return; //Already linked
  }
  
#ifdef JAVA_CODE
  
  for (java.util.Map.Entry<String, GPUUniformValue> entry : _uniformValues.entrySet()){
    final String name = entry.getKey();
    final GPUUniformValue v = entry.getValue();
    
    final int type = v.getType();
    final GPUUniform u = prog.getUniformOfType(name, type); //Getting uniform from program
    
    if (u == null) {
      ILogger.instance().logError("UNIFORM " + name + " NOT FOUND");
      return;
    }
    v.linkToGPUUniform(u);
  }
  
  for (java.util.Map.Entry<String, GPUAttributeValue> entry : _attributesValues.entrySet()){
    final String name = entry.getKey();
    final GPUAttributeValue v = entry.getValue();
    
    GPUAttribute a = null; //Getting attribute from program
    if (!v.getEnabled()){
      a = prog.getGPUAttribute(name);
    } else{
      final int type = v.getType();
      final int size = v.getAttributeSize();
      if (type==GLType.glFloat()){
        a = prog.getGPUAttributeVecXFloat(name,size);
      }
    }
    
    if (a == null){
      ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name);
      return;
    }
    v.linkToGPUAttribute(a);
  }

#endif
#ifdef C_CODE

  for(std::map<int, GPUUniformValue*> ::const_iterator it = _uniformValues.begin();
      it != _uniformValues.end();
      it++){
    
    const int key = it->first;
    GPUUniformValue* v = it->second;
    
    GPUUniform* u = prog->getGPUUniform(key);
    if (u == NULL){
      ILogger::instance()->logError("UNIFORM WITH KEY %d NOT FOUND", key);
      return;
    }
    v->linkToGPUUniform(u);
  }
  
  for(std::map<int, GPUAttributeValue*> ::const_iterator it = _attributesValues.begin();
      it != _attributesValues.end();
      it++){
    
    const int key = it->first;
    GPUAttributeValue* v = it->second;
    
    GPUAttribute* a = NULL;
    if (!v->getEnabled()){
      a = prog->getGPUAttribute(key);
    } else{
      const int type = v->getType();
      if (type==GLType::glFloat()){
        const int size = v->getAttributeSize();
        a = prog->getGPUAttributeVecXFloat(key,size);
      }
    }
    
    if (a == NULL){
      ILogger::instance()->logError("ATTRIBUTE WITH KEY %d NOT FOUND ", key);
      return;
    }
    v->linkToGPUAttribute(a);
  }
  
#endif
  
  
  _lastProgramUsed = prog;
}

void GPUProgramState::applyChanges(GL* gl) const{
  if (_lastProgramUsed == NULL){
    ILogger::instance()->logError("Trying to use unlinked GPUProgramState.");
  }
  applyValuesToLinkedProgram();
  _lastProgramUsed->applyChanges(gl);
}

bool GPUProgramState::setGPUUniformValue(int key, GPUUniformValue* v){
  
  GPUUniform* prevLinkedUniform = NULL;
  bool uniformExisted = false;
  
  
#ifdef C_CODE
  std::map<int, GPUUniformValue*> ::iterator it = _uniformValues.find(key);
  if (it != _uniformValues.end()){
    prevLinkedUniform = it->second->getLinkedUniform();
    delete it->second;
    uniformExisted = true;
  }
#endif
#ifdef JAVA_CODE
  GPUUniformValue pv = _uniformValues.get(name);
  if (pv != null){
    uniformExisted = true;
    prevLinkedUniform = pv.getLinkedUniform();
  }
#endif
  
  v->linkToGPUUniform(prevLinkedUniform);
  _uniformValues[key] = v;
  
  if (!uniformExisted){
    onStructureChanged();
  }
  
  return uniformExisted;
}

bool GPUProgramState::setGPUAttributeValue(int key, GPUAttributeValue* v){
  GPUAttribute* prevLinkedAttribute = NULL;
  bool attributeExisted = false;
#ifdef C_CODE
  std::map<int, GPUAttributeValue*> ::iterator it = _attributesValues.find(key);
  if (it != _attributesValues.end()){
    prevLinkedAttribute = it->second->getLinkedAttribute();
    delete it->second;
    attributeExisted = true;
  }
#endif
#ifdef JAVA_CODE
  GPUAttributeValue pv = _attributesValues.get(name);
  if (pv != null){
    attributeExisted = true;
    prevLinkedAttribute = pv.getLinkedAttribute();
  }
#endif
  
  v->linkToGPUAttribute(prevLinkedAttribute);
  _attributesValues[key] = v;
  
  if (!attributeExisted){
    onStructureChanged();
  }
  
  return attributeExisted;
}

bool GPUProgramState::setAttributeValue(int key,
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

bool GPUProgramState::setUniformValue(int key, bool b){
  return setGPUUniformValue(key, new GPUUniformValueBool(b) );
}

bool GPUProgramState::setUniformValue(int key, float f){
  return setGPUUniformValue(key, new GPUUniformValueFloat(f));
}

bool GPUProgramState::setUniformValue(int key, const Vector2D& v){
  return setGPUUniformValue(key, new GPUUniformValueVec2Float(v._x, v._y));
}

bool GPUProgramState::setUniformValue(int key, double x, double y, double z, double w){
  return setGPUUniformValue(key, new GPUUniformValueVec4Float(x,y,z,w));
}

bool GPUProgramState::setUniformValue(int key, double x, double y){
  return setGPUUniformValue(key, new GPUUniformValueVec2Float(x, y));
}

bool GPUProgramState::setUniformMatrixValue(int key, const MutableMatrix44D& m, bool isTransform){
  GPUUniformValueMatrix4FloatTransform *uv = new GPUUniformValueMatrix4FloatTransform(m, isTransform);
  return setGPUUniformValue(key, uv);
}

void GPUProgramState::setAttributeEnabled(int key, bool enabled){
  //TODO: REMOVE FUNCTION
  if (!enabled){
    setAttributeDisabled(key);
  }
}

void GPUProgramState::setAttributeDisabled(int key){
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
    _uniformNames = new java.util.ArrayList<String>();
    _uniformNames.addAll(_uniformValues.keySet());
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
    
    
  }
  return _attributeKeys;
}

/*
bool GPUProgramState::isLinkableToProgram(const GPUProgram& program) const{
#ifdef C_CODE
  if (program.getGPUAttributesNumber() != _attributesValues.size()){
    return false;
  }
  
  if (program.getGPUUniformsNumber()   != _uniformValues.size()){
    return false;
  }
  
  for(std::map<int, GPUUniformValue*> ::const_iterator it = _uniformValues.begin();
      it != _uniformValues.end();
      it++){
    if (program.getGPUUniform(it->first) == NULL){
      return false;
    }
  }
  
  for(std::map<std::string, GPUAttributeValue*> ::const_iterator it = _attributesValues.begin();
      it != _attributesValues.end();
      it++){
    if (program.getGPUAttribute(it->first) == NULL){
      return false;
    }
  }
  
  return true;
#endif
#ifdef JAVA_CODE
  
  if (program.getGPUAttributesNumber() != (_attributesValues.size())) {
    return false;
  }
  
  if (program.getGPUUniformsNumber() != _uniformValues.size()) {
    return false;
  }
  
  
  final Object[] uniNames = _uniformValues.keySet().toArray();
  for (int i = 0; i < uniNames.length; i++) {
    String thisName = (String) uniNames[i];
    if (program.getGPUUniform(thisName) == null) {
      return false;
    }
  }
  
  final Object[] attNames = _attributesValues.keySet().toArray();
  for (int i = 0; i < attNames.length; i++) {
    String thisName = (String) attNames[i];
    if (program.getGPUAttribute(thisName) == null) {
      return false;
    }
  }
  
  return true;
#endif
}
*/