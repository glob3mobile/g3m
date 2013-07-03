package org.glob3.mobile.generated; 
public class GPUAttributeVec4Float extends GPUAttribute
{
  public GPUAttributeVec4Float(String name, int id)
  {
     super(name, id, GLType.glFloat(), 4);
  }
}
////////


/*
void GPUAttributeValue::setValueToLinkedAttribute() const {
  if (_attribute == NULL) {
    ILogger::instance()->logError("Attribute unlinked");
  }
  else {
//    _attribute->set((GPUAttributeValue*)this);
    _attribute->set(this);
    //    _attribute->applyChanges(gl);
  }
}

bool GPUAttributeValue::linkToGPUProgram(const GPUProgram* prog, int key) const{
  if (_enabled){
    if (_type==GLType::glFloat()){
      _attribute = prog->getGPUAttributeVecXFloat(key,_attributeSize);
    }
  } else{
    _attribute = prog->getGPUAttribute(key);
  }
  
  if (_attribute == NULL){
    ILogger::instance()->logError("ATTRIBUTE WITH KEY %d NOT FOUND ", key);
    return false;
  } else{
    return true;
  }
}
*/