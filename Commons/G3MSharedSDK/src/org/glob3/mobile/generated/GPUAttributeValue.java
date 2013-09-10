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

public abstract class GPUAttributeValue extends RCObject
{
  protected final boolean _enabled;
  protected final int _type;
  protected final int _attributeSize;
  protected final int _index;
  protected final int _stride;
  protected final boolean _normalized;

  protected final int _arrayElementSize;


  public GPUAttributeValue(boolean enabled)
  {
     _enabled = enabled;
     _type = 0;
     _attributeSize = 0;
     _index = 0;
     _stride = 0;
     _normalized = false;
     _arrayElementSize = 0;
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
  public void dispose()
  {
    super.dispose();

  }
  public abstract void setAttribute(GL gl, int id);
  public abstract boolean isEquals(GPUAttributeValue v);
  public abstract String description();

}