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
  protected final int _enabled;
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
  
  
  
  /*
   
   class Attribute{
   protected:
   std::string _name;
   int _id;
   
   bool _wasSet;
   bool _dirty;
   
   public:
   virtual ~Attribute(){}
   Attribute(std::string name, int id):_name(name),_id(id),_wasSet(false), _dirty(false){}
   
   const std::string getName() const{ return _name;}
   int getID() const{ return _id;}
   
   virtual void applyChanges(GL* gl) = 0;
   };
   
   class AttributeVecFloat: public Attribute{
   protected:
   IFloatBuffer* _buffer;
   int           _timeStamp;
   int           _index;
   int           _stride;
   bool          _normalized;
   
   int _size;
   
   
   bool equalsTo(IFloatBuffer* buffer, int index, int stride, bool normalized) const{
   return !(_buffer != buffer || _index != index || _stride != stride
   || _normalized != normalized || _timeStamp != _buffer->timestamp());
   }
   
   void save(IFloatBuffer* buffer, int index, int stride, bool normalized){
   _buffer = buffer;
   _index = index;
   _stride = stride;
   _normalized = normalized;
   _timeStamp = _buffer->timestamp();
   }
   
   public:
   AttributeVecFloat(std::string name, int id, int size):
   Attribute(name, id),
   _buffer(NULL),
   _timeStamp(-1),
   _index(-1),
   _stride(-1),
   _normalized(false),
   _size(size){}
   
   
   void set(INativeGL* _nativeGL, IFloatBuffer* buffer, int index, int stride, bool normalized) {
   
   if (!_wasSet || equalsTo(buffer, index, stride, normalized)){
   //      _nativeGL->vertexAttribPointer(index,//Index
   //                                     _size,//Size
   //                                     normalized,//Normalized
   //                                     stride,//Stride
   //                                     buffer);
   
   save(buffer, index, stride, normalized);
   _wasSet = true;
   _dirty = true;
   }
   }
   
   void applyChanges(GL* gl){
   if (_dirty){
   gl->vertexAttribPointer(_index, _size, _normalized, _stride, _buffer);
   _dirty = false;
   }
   }
   
   int getSize() const { return _size;}
   
   };
   
   class AttributeVec1Float: public AttributeVecFloat{
   public:
   AttributeVec1Float(std::string name, int id):AttributeVecFloat(name, id, 1){}
   };
   
   class AttributeVec2Float: public AttributeVecFloat{
   public:
   AttributeVec2Float(std::string name, int id):AttributeVecFloat(name, id, 2){}
   };
   
   class AttributeVec3Float: public AttributeVecFloat{
   public:
   AttributeVec3Float(std::string name, int id):AttributeVecFloat(name, id, 3){}
   };
   
   class AttributeVec4Float: public AttributeVecFloat{
   public:
   AttributeVec4Float(std::string name, int id):AttributeVecFloat(name, id, 4){}
   };
   */
  /////////////////////////////////////////////////////////////////
  
  
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


}