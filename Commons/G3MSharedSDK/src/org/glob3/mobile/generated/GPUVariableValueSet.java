package org.glob3.mobile.generated; 
//
//  GPUVariableValueSet.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

//
//  GPUVariableValueSet.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//




public class GPUVariableValueSet
{
  private GPUUniformValue[] _uniformValues = new GPUUniformValue[32];
  private GPUAttributeValue[] _attributeValues = new GPUAttributeValue[32];
  private int _highestUniformKey;
  private int _highestAttributeKey;

  private int _uniformsCode;
  private int _attributeCode;


  public GPUVariableValueSet()
  {
     _highestAttributeKey = 0;
     _highestUniformKey = 0;
     _uniformsCode = 0;
     _attributeCode = 0;
    for (int i = 0; i < 32; i++)
    {
      _uniformValues[i] = null;
      _attributeValues[i] = null;
    }
  }
  public void dispose()
  {
  
    for (int i = 0; i <= _highestUniformKey; i++)
    {
      GPUUniformValue u = _uniformValues[i];
      if (u != null)
      {
        u._release();
      }
    }
  
    for (int i = 0; i <= _highestAttributeKey; i++)
    {
      GPUAttributeValue a = _attributeValues[i];
      if (a != null)
      {
        a._release();
      }
    }
  }

  public final void addUniformValue(GPUUniformKey key, GPUUniformValue v)
  {
    _uniformValues[(int)key] = v;
    v._retain();
    if (key > _highestUniformKey)
    {
      _highestUniformKey = (int)key;
    }
  }

  public final void addAttributeValue(GPUAttributeKey key, GPUAttributeValue v)
  {
    _attributeValues[(int)key] = v;
    v._retain();
    if (key > _highestAttributeKey)
    {
      _highestAttributeKey = (int)key;
    }
  }

  public final void addNewAttributeValue(GPUAttributeKey key, GPUAttributeValue v)
  {
    _attributeValues[(int)key] = v;
    if (key > _highestAttributeKey)
    {
      _highestAttributeKey = (int)key;
    }
  }

  public final void addNewUniformValue(GPUUniformKey key, GPUUniformValue v)
  {
    _uniformValues[(int)key] = v;
    if (key > _highestUniformKey)
    {
      _highestUniformKey = (int)key;
    }
  }

  public final GPUAttributeValue getAttributeValue(int key)
  {
    return _attributeValues[key];
  }

  public final GPUUniformValue getUniformValue(int key)
  {
    return _uniformValues[key];
  }

  public final void combineWith(GPUVariableValueSet vs)
  {
  
      for (int i = 0; i <= vs._highestUniformKey; i++)
      {
        if (vs._uniformValues[i] != null)
        {
          _uniformValues[i] = vs._uniformValues[i];
          _uniformValues[i]._retain();
          if (i > _highestUniformKey)
          {
            _highestUniformKey = i;
          }
        }
      }
  
      for (int i = 0; i <= vs._highestAttributeKey; i++)
      {
        if (vs._attributeValues[i] != null)
        {
          _attributeValues[i] = vs._attributeValues[i];
          _attributeValues[i]._retain();
          if (i > _highestAttributeKey)
          {
            _highestAttributeKey = i;
          }
        }
      }
  
  }

  public final void applyValuesToProgram(GPUProgram prog)
  {
  
    for (int i = 0; i <= _highestUniformKey; i++)
    {
      GPUUniformValue u = _uniformValues[i];
      if (u != null)
      {
        prog.setGPUUniformValue(i, u);
      }
    }
  
    for (int i = 0; i <= _highestAttributeKey; i++)
    {
      GPUAttributeValue a = _attributeValues[i];
      if (a != null)
      {
        prog.setGPUAttributeValue(i, a);
      }
    }
  }

  public final int getUniformsCode()
  {
    if (_uniformsCode == 0)
    {
      for (int i = 0; i <= _highestUniformKey; i++)
      {
        if (_uniformValues[i] != null)
        {
          _uniformsCode = _uniformsCode | GPUVariable.getUniformCode(i);
        }
      }
    }
    return _uniformsCode;
  }

  public final int getAttributesCode()
  {
    if (_attributeCode == 0)
    {
      for (int i = 0; i <= _highestAttributeKey; i++)
      {
        if (_attributeValues[i] != null)
        {
          _attributeCode = _attributeCode | GPUVariable.getAttributeCode(i);
        }
      }
    }
    return _attributeCode;
  }

}