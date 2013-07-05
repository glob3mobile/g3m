package org.glob3.mobile.generated; 
//class GPUUniformValueMatrix4FloatTransform:public GPUUniformValue{
//
//  GPUUniformValueMatrix4FloatTransform(const GPUUniformValueMatrix4FloatTransform* that):
//  GPUUniformValue(GLType::glMatrix4Float()),
//  _m(MutableMatrix44D(that->_m)),
//  _isTransform(that->_isTransform)
//  {}
//
//public:
//  MutableMatrix44D _m;
//  bool _isTransform;
//
//  GPUUniformValueMatrix4FloatTransform(const MutableMatrix44D& m, bool isTransform):
//  GPUUniformValue(GLType::glMatrix4Float()),_m(MutableMatrix44D(m)), _isTransform(isTransform)/*, _transformedMatrix(m)*/{}
//
//  void setUniform(GL* gl, const IGLUniformID* id) const{
//    gl->uniformMatrix4fv(id, false, _m.asMatrix44D());
//  }
//
//  bool isEqualsTo(const GPUUniformValue* v) const{
//    GPUUniformValueMatrix4FloatTransform *v2 = (GPUUniformValueMatrix4FloatTransform *)v;
//    return _m.isEqualsTo(v2->_m);
//  }
//
//  GPUUniformValue* copyOrCreate(GPUUniformValue* value) const;
//
//  std::string description() const{
//    IStringBuilder *isb = IStringBuilder::newStringBuilder();
//    isb->addString("Uniform Value Matrix44D.");
//    std::string s = isb->getString();
//    delete isb;
//    return s;
//  }
//
//  const MutableMatrix44D* getValue() const{
//    //    return &_transformedMatrix;
//    return &_m;
//  }
//};


public class GPUUniformMatrix4Float extends GPUUniform
{
  public GPUUniformMatrix4Float(String name, IGLUniformID id)
  {
     super(name,id, GLType.glMatrix4Float());
  }
}