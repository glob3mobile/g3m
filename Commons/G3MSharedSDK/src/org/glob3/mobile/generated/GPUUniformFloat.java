package org.glob3.mobile.generated; 
//class GPUUniformValueFloatMutable : public GPUUniformValue {
//protected:
//  ~GPUUniformValueFloatMutable() {
///#ifdef JAVA_CODE
//    super.dispose();
///#endif
//  }
//  
//private:
//  mutable bool _hasChangedSinceLastSetUniform;
//  
//public:
//  float _value;
//  
//  GPUUniformValueFloatMutable(float d):GPUUniformValue(GLType::glFloat()),_value(d), _hasChangedSinceLastSetUniform(true) {}
//  
//  void changeValue(float x) {
//    if (x != _value){
//      _value = x;
//      _hasChangedSinceLastSetUniform = true;
//    }
//  }
//  
//  void setUniform(GL* gl, const IGLUniformID* id) const {
//    gl->uniform1f(id, _value);
//    _hasChangedSinceLastSetUniform = false;
//  }
//  bool isEquals(const GPUUniformValue* v) const {
//    
//    if (v == this && _hasChangedSinceLastSetUniform){
//      return true;
//    }
//    
//    GPUUniformValueFloat *v2 = (GPUUniformValueFloat *)v;
//    return _value == v2->_value;
//  }
//  
//  std::string description() const {
//    IStringBuilder* isb = IStringBuilder::newStringBuilder();
//    isb->addString("Uniform Value Float: ");
//    isb->addDouble(_value);
//    std::string s = isb->getString();
//    delete isb;
//    return s;
//  }
//};


public class GPUUniformFloat extends GPUUniform
{
  public GPUUniformFloat(String name, IGLUniformID id)
  {
     super(name,id, GLType.glFloat());
  }

  public void dispose()
  {
    super.dispose();
  }

}