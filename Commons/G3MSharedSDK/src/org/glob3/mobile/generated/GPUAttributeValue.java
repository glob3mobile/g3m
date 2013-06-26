package org.glob3.mobile.generated; 
//
//  GPUAttribute.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/05/13.
//
//

//
//  Attribute.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//






//class GPUAttribute;

public abstract class GPUAttributeValue
{
  protected final boolean _enabled;
  protected final int _type;
  protected final int _attributeSize;
  protected final int _index;
  protected int _stride;
  protected boolean _normalized;

  protected final int _arrayElementSize;

  protected GPUAttribute _attribute;


  public GPUAttributeValue(boolean enabled)
  {
     _enabled = enabled;
     _type = 0;
     _attributeSize = 0;
     _index = 0;
     _stride = 0;
     _normalized = false;
     _arrayElementSize = 0;
     _attribute = null;
  }

  public GPUAttributeValue(int type, int attributeSize, int arrayElementSize, int index, int stride, boolean normalized)
  {
     _enabled = true;
     _type = type;
     _attributeSize = attributeSize;
     _index = index;
     _stride = stride;
     _normalized = normalized;
     _arrayElementSize = arrayElementSize;
     _attribute = null;
  }

  public final int getType()
  {
     return _type;
  }
  public final int getAttributeSize()
  {
     return _attributeSize;
  }
  public final int getIndex()
  {
     return _index;
  }
  public final int getStride()
  {
     return _stride;
  }
  public final boolean getNormalized()
  {
     return _normalized;
  }
  public final boolean getEnabled()
  {
     return _enabled;
  }
  public final GPUAttribute getLinkedAttribute()
  {
     return _attribute;
  }
  public void dispose()
  {
  }
  public abstract void setAttribute(GL gl, int id);
  public abstract boolean isEqualsTo(GPUAttributeValue v);
  public abstract GPUAttributeValue shallowCopy();

  public abstract String description();

  public final void linkToGPUAttribute(GPUAttribute a)
  {
    _attribute = a;
  }

  public final void unLinkToGPUAttribute()
  {
    _attribute = null;
  }

  ////////
  
  
  
  public final void setValueToLinkedAttribute()
  {
    if (_attribute == null)
    {
      ILogger.instance().logError("Attribute unlinked");
    }
    else
    {
  
      _attribute.set((GPUAttributeValue)this);
  //    _attribute->applyChanges(gl);
    }
  }

  public final boolean linkToGPUProgram(GPUProgram prog, int key)
  {
    if (_enabled)
    {
      if (_type == GLType.glFloat())
      {
        _attribute = prog.getGPUAttributeVecXFloat(key, _attributeSize);
      }
    }
    else
    {
      _attribute = prog.getGPUAttribute(key);
    }
  
    if (_attribute == null)
    {
      ILogger.instance().logError("ATTRIBUTE WITH KEY %d NOT FOUND ", key);
      return false;
    }
    else
    {
      return true;
    }
  }

}