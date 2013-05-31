package org.glob3.mobile.generated; 
//
//  GPUProgramState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//

//
//  GPUProgramState.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 05/04/13.
//
//




public class GPUProgramState
{

  private static class attributeEnabledStruct
  {
    public boolean value;
    public GPUAttribute attribute;
  }
  private java.util.HashMap<String, GPUUniformValue> _uniformValues = new java.util.HashMap<String, GPUUniformValue>();
  private java.util.HashMap<String, GPUAttributeValue> _attributesValues = new java.util.HashMap<String, GPUAttributeValue>();
  private java.util.HashMap<String, attributeEnabledStruct> _attributesEnabled = new java.util.HashMap<String, attributeEnabledStruct>();

  private void setUniformValue(String name, GPUUniformValue v)
  {
    _uniformValues.put(name, v);
  }
  private void setAttributeValue(String name, GPUAttributeValue v)
  {
    _attributesValues.put(name, v);
  }

  private void applyValuesToLinkedProgram()
  {
    final GPUUniformValue[] uni = (GPUUniformValue[]) _uniformValues.values().toArray();
    for (final GPUUniformValue v : uni) {
      v.setValueToLinkedUniform();
    }
  
    final attributeEnabledStruct[] attEnabled = (attributeEnabledStruct[]) _attributesEnabled.values().toArray();
    for (final attributeEnabledStruct a : attEnabled) {
      if (a.attribute == null) {
        ILogger.instance().logError("NO ATTRIBUTE LINKED");
      }
      else {
        a.attribute.setEnable(a.value);
      }
    }
  
    final GPUAttributeValue[] att = (GPUAttributeValue[]) _attributesValues.values().toArray();
    for (final GPUAttributeValue a : att) {
      a.setValueToLinkedAttribute();
    }
  }

  private GPUProgram _lastProgramUsed;


  public GPUProgramState()
  {
     _lastProgramUsed = null;
  }

  public void dispose()
  {
    clear();
  }

  public final void clear()
  {
    _lastProgramUsed = null;
    _uniformValues.clear();
  
    _attributesEnabled.clear();
    _attributesValues.clear();
  }

  public final void setUniformValue(String name, boolean b)
  {
    setUniformValue(name, new GPUUniformValueBool(b));
  }

  public final void setUniformValue(String name, float f)
  {
    setUniformValue(name, new GPUUniformValueFloat(f));
  }

  public final void setUniformValue(String name, Vector2D v)
  {
    setUniformValue(name, new GPUUniformValueVec2Float(v._x, v._y));
  }

  public final void setUniformValue(String name, double x, double y, double z, double w)
  {
    setUniformValue(name, new GPUUniformValueVec4Float(x,y,z,w));
  }

  public final void setUniformValue(String name, MutableMatrix44D m)
  {
  
    final Iterator it = _uniformValues.entrySet().iterator();
    while (it.hasNext()) {
      final Map.Entry pairs = (Map.Entry)it.next();
      final String thisName =  (String) pairs.getKey();
      final GPUUniformValue uv = (GPUUniformValue) pairs.getValue();
      if ((thisName.compareTo(name) == 0) && (uv.getType() == GLType.glMatrix4Float()))
      {
        final GPUUniformValueMatrix4FloatStack v = (GPUUniformValueMatrix4FloatStack)uv;
        v.loadMatrix(m);
        return;
      }
    }
  
    setUniformValue(name, new GPUUniformValueMatrix4FloatStack(m));
  }

  public final void multiplyUniformValue(String name, MutableMatrix44D m)
  {
  
    final Iterator it = _uniformValues.entrySet().iterator();
    while (it.hasNext()) {
      final Map.Entry pairs = (Map.Entry)it.next();
      final String thisName =  (String) pairs.getKey();
      final GPUUniformValue uv = (GPUUniformValue) pairs.getValue();
      if ((thisName.compareTo(name) == 0) && (uv.getType() == GLType.glMatrix4Float()))
      {
        final GPUUniformValueMatrix4FloatStack v = (GPUUniformValueMatrix4FloatStack)uv;
        v.multiplyMatrix(m);
        return;
      }
    }
  
    ILogger.instance().logError("CAN'T MULTIPLY UNLOADED MATRIX");
  
  }

  public final void setAttributeValue(String name, IFloatBuffer buffer, int attributeSize, int arrayElementSize, int index, boolean normalized, int stride)
  {
    switch (attributeSize)
    {
      case 1:
        setAttributeValue(name, new GPUAttributeValueVec1Float(buffer, arrayElementSize, index, stride, normalized));
        break;
  
      case 2:
        setAttributeValue(name, new GPUAttributeValueVec2Float(buffer, arrayElementSize, index, stride, normalized));
        break;
  
      case 3:
        setAttributeValue(name, new GPUAttributeValueVec3Float(buffer, arrayElementSize, index, stride, normalized));
        break;
  
      case 4:
        setAttributeValue(name, new GPUAttributeValueVec4Float(buffer, arrayElementSize, index, stride, normalized));
        break;
  
      default:
        ILogger.instance().logError("Invalid size for Attribute.");
        break;
    }
  }

  public final void setAttributeEnabled(String name, boolean enabled)
  {
    attributeEnabledStruct ae = new attributeEnabledStruct();
    ae.value = enabled;
    ae.attribute = null;
  
    _attributesEnabled.put(name, ae);
  }

  public final void applyChanges(GL gl)
  {
    if (_lastProgramUsed == null)
    {
      ILogger.instance().logError("Trying to use unlinked GPUProgramState.");
    }
    applyValuesToLinkedProgram();
    _lastProgramUsed.applyChanges(gl);
  }

  public final void linkToProgram(GPUProgram prog)
  {
  
    _lastProgramUsed = prog;
  
    final Iterator it = _uniformValues.entrySet().iterator();
    while (it.hasNext()) {
      final Map.Entry pairs = (Map.Entry)it.next();
      final String name =  (String) pairs.getKey();
      final GPUUniformValue v = (GPUUniformValue) pairs.getValue();
  
  
      int type = v.getType();
      GPUUniform u = prog.getUniformOfType(name, type);
  
      if (u == NULL){
        ILogger.instance().logError("UNIFORM " + name + " NOT FOUND");
      } else{
        v.linkToGPUUniform(u);
      }
    }
  
    final Iterator it = _attributesEnabled.entrySet().iterator();
    while (it.hasNext()) {
      final Map.Entry pairs = (Map.Entry)it.next();
      final String name =  (String) pairs.getKey();
      final attributeEnabledStruct ae = (attributeEnabledStruct) pairs.getValue();
  
      GPUAttribute a = prog.getGPUAttribute(name);
      if (a == NULL){
        ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name + ". COULDN'T CHANGE ENABLED STATE.");
      } else{
        ae.attribute = a;
      }
    }
  
  
  
    for(java.util.HashMap<String, GPUUniformValue> const_iterator it = _uniformValues.iterator(); it != _uniformValues.end(); it++)
    {
  
      String name = it.first;
      GPUUniformValue v = it.second;
  
      final int type = v.getType();
  
      GPUUniform u = prog.getUniformOfType(name, type);
  //    if (type == GLType::glBool()){
  //      u = prog.getGPUUniformBool(name);
  //    } else {
  //      if (type == GLType::glVec2Float()){
  //        u = prog.getGPUUniformVec2Float(name);
  //      } else{
  //        if (type == GLType::glVec4Float()){
  //          u = prog.getGPUUniformVec4Float(name);
  //        } else{
  //          if (type == GLType::glFloat()){
  //            u = prog.getGPUUniformFloat(name);
  //          } else
  //            if (type == GLType::glMatrix4Float()){
  //              u = prog.getGPUUniformMatrix4Float(name);
  //            }
  //        }
  //      }
  //    }
  
      if (u == null)
      {
        ILogger.instance().logError("UNIFORM " + name + " NOT FOUND");
      }
      else
      {
        v.linkToGPUUniform(u);
      }
    }
  
    for(java.util.HashMap<String, attributeEnabledStruct> const_iterator it = _attributesEnabled.iterator(); it != _attributesEnabled.end(); it++)
    {
      String name = it.first;
      GPUAttribute a = prog.getGPUAttribute(name);
      if (a == null)
      {
        ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name + ". COULDN'T CHANGE ENABLED STATE.");
      }
      else
      {
        it.second.attribute = a;
      }
    }
  
    for(java.util.HashMap<String, GPUAttributeValue> const_iterator it = _attributesValues.iterator(); it != _attributesValues.end(); it++)
    {
  
      String name = it.first;
      GPUAttributeValue v = it.second;
  
      final int type = v.getType();
      final int size = v.getAttributeSize();
      if (type == GLType.glFloat() && size == 1)
      {
        GPUAttributeVec1Float a = prog.getGPUAttributeVec1Float(name);
        if (a == null)
        {
          ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name);
        }
        else
        {
          v.linkToGPUAttribute(a);
        }
        continue;
      }
  
      if (type == GLType.glFloat() && size == 2)
      {
        GPUAttributeVec2Float a = prog.getGPUAttributeVec2Float(name);
        if (a == null)
        {
          ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name);
        }
        else
        {
          v.linkToGPUAttribute(a);
        }
        continue;
      }
  
      if (type == GLType.glFloat() && size == 3)
      {
        GPUAttribute a = prog.getGPUAttributeVec3Float(name);
        if (a == null)
        {
          a = prog.getGPUAttributeVec4Float(name); //A VEC3 COLUD BE STORED IN A VEC4 ATTRIBUTE
        }
  
        if (a == null)
        {
          ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name);
        }
        else
        {
          v.linkToGPUAttribute(a);
        }
        continue;
      }
  
      if (type == GLType.glFloat() && size == 4)
      {
        GPUAttributeVec4Float a = prog.getGPUAttributeVec4Float(name);
        if (a == null)
        {
          ILogger.instance().logError("ATTRIBUTE NOT FOUND " + name);
        }
        else
        {
          v.linkToGPUAttribute(a);
        }
        continue;
      }
  
    }
  }

  public final boolean isLinkedToProgram()
  {
    return _lastProgramUsed != null;
  }

  public final GPUProgram getLinkedProgram()
  {
    return _lastProgramUsed;
  }

  public final java.util.ArrayList<String> getUniformsNames()
  {
    java.util.ArrayList<String> us = new java.util.ArrayList<String>();
  
  
    final Iterator it = _uniformValues.entrySet().iterator();
    while (it.hasNext()) {
      final Map.Entry pairs = (Map.Entry)it.next();
      us.add((String)pairs.getKey());
    }
  
    return us;
  }

  public final String description()
  {
    String desc = "PROGRAM STATE\n==========\n";
    //TODO: IMPLEMENT
    return desc;
  }

  public final boolean isLinkableToProgram(GPUProgram program)
  {
    if (program.getGPUAttributesNumber() != _attributesEnabled.size())
    {
      return false;
    }
  
    int nDisabledAtt = 0;
    {
      final Iterator it = _attributesEnabled.entrySet().iterator();
      while (it.hasNext()) {
        final Map.Entry pairs = (Map.Entry)it.next();
        final String thisName =  (String) pairs.getKey();
        final attributeEnabledStruct ae =  (attributeEnabledStruct) pairs.getValue();
        if (ae.value == false)
        {
          nDisabledAtt++;
        }
        if (program.getGPUAttribute(thisName) == null)
        {
          return false;
        }
      }
    }
  
    if (program.getGPUAttributesNumber() != (_attributesValues.size() + nDisabledAtt))
    {
      return false;
    }
  
    if (program.getGPUUniformsNumber() != _uniformValues.size())
    {
      return false;
    }
  
    {
      final Iterator it = _uniformValues.entrySet().iterator();
      while (it.hasNext()) {
        final Map.Entry pairs = (Map.Entry)it.next();
        final String thisName =  (String) pairs.getKey();
        if (program.getGPUUniform(thisName) == null)
        {
          return false;
        }
      }
    }
  
    {
      final Iterator it = _attributesValues.entrySet().iterator();
      while (it.hasNext()) {
        final Map.Entry pairs = (Map.Entry)it.next();
        final String thisName =  (String) pairs.getKey();
        if (program.getGPUAttribute(thisName) == null)
        {
          return false;
        }
      }
    }
  
    return true;
  }

}