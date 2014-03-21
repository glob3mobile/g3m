package org.glob3.mobile.generated; 
//
//  GPUAttribute.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/05/13.
//
//

//
//  Attribute.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//







//class GPUAttribute;

public abstract class GPUAttributeValue extends RCObject
{
  public void dispose()
  {
    super.dispose();
  }

  public final boolean _enabled;
  public final int _type;
  public final int _attributeSize;
  public final int _index;
  public final int _stride;
  public final boolean _normalized;
  public final int _arrayElementSize;

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

  //  int getType() const { return _type;}
  //  int getAttributeSize() const { return _attributeSize;}
  //  int getIndex() const { return _index;}
  //  int getStride() const { return _stride;}
  //  bool getNormalized() const { return _normalized;}
  //  bool getEnabled() const { return _enabled;}

  public abstract void setAttribute(GL gl, int id);
  public abstract boolean isEquals(GPUAttributeValue v);
  public abstract String description();
  @Override
  public String toString() {
    return description();
  }

}