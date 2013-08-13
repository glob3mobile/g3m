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

  //  mutable GPUAttribute* _attribute;


  public GPUAttributeValue(boolean enabled)
  //  ,
  //  _attribute(NULL)
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
  //,
  //  _attribute(NULL)
  {
     _enabled = true;
     _type = type;
     _attributeSize = attributeSize;
     _index = index;
     _stride = stride;
     _normalized = normalized;
     _arrayElementSize = arrayElementSize;
  }

//  void changeParameters(bool enabled, int type, int attributeSize, int arrayElementSize, int index, int stride, bool normalized) {
//    _enabled = enabled;
//    _type = type;
//    _attributeSize = attributeSize;
//    _index = index;
//    _stride = stride;
//    _normalized = normalized;
//    _arrayElementSize = arrayElementSize;
//    //    _attribute = NULL;
//  }

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
  //  GPUAttribute* getLinkedAttribute() const { return _attribute;}
  public void dispose()
  {
  super.dispose();

  }
  public abstract void setAttribute(GL gl, int id);
  public abstract boolean isEqualsTo(GPUAttributeValue v);
//  virtual GPUAttributeValue* shallowCopy() const = 0;

  public abstract String description();

  //  void linkToGPUAttribute(GPUAttribute* a) const{
  //    _attribute = a;
  //  }
  //
  //  void unLinkToGPUAttribute() {
  //    _attribute = NULL;
  //  }

  //  void setValueToLinkedAttribute() const;

  //  bool linkToGPUProgram(const GPUProgram* prog, int key) const;

//  virtual GPUAttributeValue* copyOrCreate(GPUAttributeValue* oldAtt) const = 0;

}