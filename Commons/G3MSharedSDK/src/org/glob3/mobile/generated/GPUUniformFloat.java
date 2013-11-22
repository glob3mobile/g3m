package org.glob3.mobile.generated; 
public class GPUUniformFloat extends GPUUniform
{
  public void dispose()
  {
    super.dispose();
  }

  public GPUUniformFloat(String name, IGLUniformID id)
  {
     super(name,id, GLType.glFloat());
  }
}
//GPUUniformValue* GPUUniformValueMatrix4FloatTransform::copyOrCreate(GPUUniformValue* value) const {
//  if (value == NULL) {
//    return new GPUUniformValueMatrix4FloatTransform(_m, _isTransform);
//  } else{
//    GPUUniformValueMatrix4FloatTransform* valueM = (GPUUniformValueMatrix4FloatTransform*)value;
///#ifdef C_CODE
//    if (_isTransform) {
//      valueM->_m.copyValueOfMultiplication(valueM->_m, _m);
//    } else {
//      valueM->_m.copyValue(_m);
//    }
///#endif
///#ifdef JAVA_CODE
//    if (_isTransform) {
//      valueM._m = valueM._m.multiply(_m);
//    } else {
//      valueM._m = new MutableMatrix44D(_m);
//    }
///#endif
//    return value;
//  }
//}

//GPUUniformValue* GPUUniformValueMatrix4Float::copyOrCreate(GPUUniformValue* value) const{
//  delete value;
//  return new GPUUniformValueMatrix4Float(*_m);
//}
