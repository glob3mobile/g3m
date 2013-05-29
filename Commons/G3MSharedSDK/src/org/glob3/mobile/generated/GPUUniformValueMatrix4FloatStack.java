package org.glob3.mobile.generated; 
////////////////////////////////////////////////////////////////////////
public class GPUUniformValueMatrix4FloatStack extends GPUUniformValue
{
  public final java.util.ArrayList<MutableMatrix44D> _stack = new java.util.ArrayList<MutableMatrix44D>();

  public GPUUniformValueMatrix4FloatStack(MutableMatrix44D m)
  {
     super(GLType.glMatrix4Float());
    _stack.add(m);
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    MutableMatrix44D m = new MutableMatrix44D(*_stack.get(0));
    final int size = _stack.size();
    for (int i = 1; i < size; i++)
    {
      m = m.multiply(*_stack.get(i));
      //m = _stack[i]->multiply(m);
    }

    gl.uniformMatrix4fv(id, false, m);
  }

  public final boolean isEqualsTo(GPUUniformValue v)
  {
    //TODO: FIX
    return false;
//    GPUUniformValueMatrix4FloatStack *v2 = (GPUUniformValueMatrix4FloatStack *)v;
//    if (_stack.size() != v2->_stack.size()){
//      return false;
//    }
//    
//    for (int i = _stack.size(); i > -1; i--) {
//      const MutableMatrix44D* m = v2->_stack[i];
//      const MutableMatrix44D* m2 = _stack[i];
//      if (!m->isEqualsTo(*m2)){
//        return false;
//      }
//    }
//    return true;
  }

  public final void multiplyMatrix(MutableMatrix44D m)
  {
    _stack.add(m);
  }

  public final void loadMatrix(MutableMatrix44D m)
  {
    _stack.clear();
    _stack.add(m);
  }

  public final GPUUniformValue deepCopy()
  {
    GPUUniformValueMatrix4FloatStack v = new GPUUniformValueMatrix4FloatStack(_stack.get(0));
    final int stackSize = _stack.size();
    for (int i = 1; i < stackSize; i++)
    {
      v.multiplyMatrix(_stack.get(i));
    }
    return v;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("Uniform Value Matrix44D Stack.");
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
}