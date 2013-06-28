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

//  std::map<GPUUniformKey, GPUUniformValue*> _uniformValues;
//  std::map<GPUAttributeKey, GPUAttributeValue*> _attributesValues;

  private GPUUniformValue[] _uniformValues = new GPUUniformValue[32];
  private GPUAttributeValue[] _attributeValues = new GPUAttributeValue[32];

  private boolean setGPUUniformValue(GPUUniformKey key, GPUUniformValue v)
  {
  
    GPUUniform prevLinkedUniform = null;
    boolean uniformExisted = false;
  
    final int index = key.getValue();
  
    GPUUniformValue u = _uniformValues[index];
    if (u != null)
    {
      prevLinkedUniform = u.getLinkedUniform();
      if (u != null)
         u.dispose();
      uniformExisted = true;
    }
  
    v.linkToGPUUniform(prevLinkedUniform);
    _uniformValues[index] = v;
  
    if (!uniformExisted)
    {
      onStructureChanged();
    }
  
    return uniformExisted;
  }
  private boolean setGPUAttributeValue(GPUAttributeKey key, GPUAttributeValue v)
  {
    GPUAttribute prevLinkedAttribute = null;
    boolean attributeExisted = false;
  
    final int index = key.getValue();
  
    GPUAttributeValue a = _attributeValues[index];
    if (a != null)
    {
      prevLinkedAttribute = a.getLinkedAttribute();
      if (a != null)
         a.dispose();
      attributeExisted = true;
    }
  
    v.linkToGPUAttribute(prevLinkedAttribute);
    _attributeValues[index] = v;
  
    if (!attributeExisted)
    {
      onStructureChanged();
    }
  
    return attributeExisted;
  }

  private java.util.ArrayList<Integer> _uniformKeys;
  private java.util.ArrayList<Integer> _attributeKeys;

  private GPUProgram _linkedProgram;

  private void onStructureChanged()
  {
    _uniformKeys = null;
    _uniformKeys = null;
    _linkedProgram = null;

    if (_attributeKeys != null)
    {
      _attributeKeys = null;
      _attributeKeys = null;
    }
  }


  public GPUProgramState()
  {
     _linkedProgram = null;
     _uniformKeys = null;
     _attributeKeys = null;
    for (int i = 0; i < 32; i++)
    {
      _uniformValues[i] = null;
      _attributeValues[i] = null;
    }

  }

  public void dispose()
  {
    clear();
    _uniformKeys = null;
  }

  public final void clear()
  {
    _linkedProgram = null;
  
    for (int i = 0; i < 32; i++)
    {
      if (_uniformValues[i] != null)
         _uniformValues[i].dispose();
      _uniformValues[i] = null;
      if (_attributeValues[i] != null)
         _attributeValues[i].dispose();
      _attributeValues[i] = null;
    }
  }

  public final boolean setUniformValue(GPUUniformKey key, boolean b)
  {
    return setGPUUniformValue(key, new GPUUniformValueBool(b));
  }

  public final boolean setUniformValue(GPUUniformKey key, float f)
  {
    return setGPUUniformValue(key, new GPUUniformValueFloat(f));
  }

  public final boolean setUniformValue(GPUUniformKey key, Vector2D v)
  {
    return setGPUUniformValue(key, new GPUUniformValueVec2Float(v._x, v._y));
  }

  public final boolean setUniformValue(GPUUniformKey key, double x, double y)
  {
    return setGPUUniformValue(key, new GPUUniformValueVec2Float(x, y));
  }

  public final boolean setUniformValue(GPUUniformKey key, double x, double y, double z, double w)
  {
    return setGPUUniformValue(key, new GPUUniformValueVec4Float(x,y,z,w));
  }

  public final boolean setUniformMatrixValue(GPUUniformKey key, MutableMatrix44D m, boolean isTransform)
  {
    GPUUniformValueMatrix4FloatTransform uv = new GPUUniformValueMatrix4FloatTransform(m, isTransform);
    return setGPUUniformValue(key, uv);
  }

  public final boolean setAttributeValue(GPUAttributeKey key, IFloatBuffer buffer, int attributeSize, int arrayElementSize, int index, boolean normalized, int stride)
  {
    switch (attributeSize)
    {
      case 1:
        return setGPUAttributeValue(key, new GPUAttributeValueVec1Float(buffer, arrayElementSize, index, stride, normalized));
      case 2:
        return setGPUAttributeValue(key, new GPUAttributeValueVec2Float(buffer, arrayElementSize, index, stride, normalized));
      case 3:
        return setGPUAttributeValue(key, new GPUAttributeValueVec3Float(buffer, arrayElementSize, index, stride, normalized));
      case 4:
        return setGPUAttributeValue(key, new GPUAttributeValueVec4Float(buffer, arrayElementSize, index, stride, normalized));
      default:
        ILogger.instance().logError("Invalid size for Attribute.");
        return false;
    }
  }

  public final void setAttributeEnabled(GPUAttributeKey key, boolean enabled)
  {
    //TODO: REMOVE FUNCTION
    if (!enabled)
    {
      setAttributeDisabled(key);
    }
  }
  public final void setAttributeDisabled(GPUAttributeKey key)
  {
    setGPUAttributeValue(key, new GPUAttributeValueDisabled());
  }

  public final void applyChanges(GL gl)
  {
    if (_linkedProgram == null)
    {
      ILogger.instance().logError("Trying to use unlinked GPUProgramState.");
    }
    applyValuesToLinkedProgram();
    _linkedProgram.applyChanges(gl);
  }

  public final void linkToProgram(GPUProgram prog)
  {
  
    if (_linkedProgram == prog)
    {
      return; //Already linked
    }
  
    for (int i = 0; i < 32; i++)
    {
      GPUUniformValue u = _uniformValues[i];
      if (u != null)
      {
        if (!u.linkToGPUProgram(prog, i))
        {
          return;
        }
      }
      GPUAttributeValue a = _attributeValues[i];
      if (a != null)
      {
        if (!a.linkToGPUProgram(prog, i))
        {
          return;
        }
      }
    }
  
    _linkedProgram = prog;
  }

  public final boolean isLinkedToProgram()
  {
    return _linkedProgram != null;
  }

  public final GPUProgram getLinkedProgram()
  {
    return _linkedProgram;
  }

  public final java.util.ArrayList<Integer> getUniformsKeys()
  {
  
    if (_uniformKeys == null)
    {
  
      _uniformKeys = new java.util.ArrayList<Integer>();
  
      for (int i = 0; i < 32; i++)
      {
        if (_uniformValues[i] != null)
        {
          _uniformKeys.add(i);
        }
      }
  
    }
    return _uniformKeys;
  }
  public final java.util.ArrayList<Integer> getAttributeKeys()
  {
  
    if (_attributeKeys == null)
    {
  
      _attributeKeys = new java.util.ArrayList<Integer>();
  
      for (int i = 0; i < 32; i++)
      {
        if (_attributeValues[i] != null)
        {
          _attributeKeys.add(i);
        }
      }
  
  
    }
    return _attributeKeys;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("PROGRAM STATE\n==========\n");
  
    for(int i = 0; i < 32; i++)
    {
      GPUUniformValue v = _uniformValues[i];
      if (v != null)
      {
        isb.addString("Uniform ");
        isb.addInt(i);
        isb.addString(":\n");
        isb.addString(v.description() + "\n");
      }
    }
  
    for(int i = 0; i < 32; i++)
    {
      GPUAttributeValue v = _attributeValues[i];
      if (v != null)
      {
        isb.addString("Uniform ");
        isb.addInt(i);
        isb.addString(":\n");
        isb.addString(v.description() + "\n");
      }
    }
  
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final void applyValuesToLinkedProgram()
  {
  
    for (int i = 0; i < 32; i++)
    {
      GPUUniformValue u = _uniformValues[i];
      if (u != null)
      {
        u.setValueToLinkedUniform();
      }
      GPUAttributeValue a = _attributeValues[i];
      if (a != null)
      {
        a.setValueToLinkedAttribute();
      }
    }
  }

  public final boolean removeGPUUniformValue(GPUUniformKey key)
  {
  
    final int index = key.getValue();
  
    if (_uniformValues[index] != null)
    {
      if (_uniformValues[index] != null)
         _uniformValues[index].dispose();
      _uniformValues[index] = null;
      onStructureChanged();
      return true;
    }
    else
    {
      return false;
    }
  }


  /*
   bool setUniformValue(const std::string& name, bool b){
   return setUniformValue(getKeyForName(name, UNIFORM), b);
   }
   
   bool setUniformValue(const std::string& name, float f){
   return setUniformValue(getKeyForName(name, UNIFORM), f);
   }
   
   bool setUniformValue(const std::string& name, const Vector2D& v){
   return setUniformValue(getKeyForName(name, UNIFORM), v);
   }
   
   bool setUniformValue(const std::string& name, double x, double y){
   return setUniformValue(getKeyForName(name, UNIFORM), x, y);
   }
   
   bool setUniformValue(const std::string& name, double x, double y, double z, double w){
   return setUniformValue(getKeyForName(name, UNIFORM), x, y, z, w);
   }
   
   bool setUniformMatrixValue(const std::string& name, const MutableMatrix44D& m, bool isTransform){
   return setUniformMatrixValue(getKeyForName(name, UNIFORM), m, isTransform);
   }
   
   bool setAttributeValue(const std::string& name,
   IFloatBuffer* buffer, int attributeSize,
   int arrayElementSize, int index, bool normalized, int stride){
   return setAttributeValue(getKeyForName(name, ATTRIBUTE),
   buffer, attributeSize,
   arrayElementSize, index, normalized, stride);
   }
   
   void setAttributeEnabled(const std::string& name, bool enabled){
   setAttributeEnabled(getKeyForName(name, ATTRIBUTE), enabled);
   }
   void setAttributeDisabled(const std::string& name){
   setAttributeDisabled(getKeyForName(name, ATTRIBUTE));
   }
   */

}