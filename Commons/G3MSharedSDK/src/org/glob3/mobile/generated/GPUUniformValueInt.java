package org.glob3.mobile.generated; 
public class GPUUniformValueInt extends GPUUniformValue
{
  public void dispose()
  {
    super.dispose();
  }

  public final int _value;

  public GPUUniformValueInt(int b)
  {
     super(GLType.glInt());
     _value = b;
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    gl.uniform1i(id, _value);
  }
  public final boolean isEquals(GPUUniformValue v)
  {
    return _value == ((GPUUniformValueInt)v)._value;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("Uniform Value Integer: ");
    isb.addInt(_value);
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
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
