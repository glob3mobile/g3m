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
  protected boolean _enabled;
  protected int _type;
  protected int _attributeSize;
  protected int _index;
  protected int _stride;
  protected boolean _normalized;

  protected int _arrayElementSize;

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

  public final void changeParameters(boolean enabled, int type, int attributeSize, int arrayElementSize, int index, int stride, boolean normalized)
  {
    _enabled = enabled;
    _type = type;
    _attributeSize = attributeSize;
    _index = index;
    _stride = stride;
    _normalized = normalized;
    _arrayElementSize = arrayElementSize;
    //    _attribute = NULL;
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
  //  GPUAttribute* getLinkedAttribute() const { return _attribute;}
  public void dispose()
  {
  }
  public abstract void setAttribute(GL gl, int id);
  public abstract boolean isEqualsTo(GPUAttributeValue v);
  public abstract GPUAttributeValue shallowCopy();

  public abstract String description();

  //  void linkToGPUAttribute(GPUAttribute* a) const{
  //    _attribute = a;
  //  }
  //
  //  void unLinkToGPUAttribute(){
  //    _attribute = NULL;
  //  }

  //  void setValueToLinkedAttribute() const;

  //  bool linkToGPUProgram(const GPUProgram* prog, int key) const;

  public abstract GPUAttributeValue copyOrCreate(GPUAttributeValue oldAtt);

}