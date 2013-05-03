//
//  Attribute.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

#ifndef G3MiOSSDK_Attribute_h
#define G3MiOSSDK_Attribute_h


#include "INativeGL.hpp"
#include "IFloatBuffer.hpp"
#include "GL.hpp"

class GPUAttributeValue{
protected:
  const int _type;
  const int _attributeSize;
  const int _index;
  int           _stride;
  bool          _normalized;
  
  const int _arrayElementSize;
  
public:
  GPUAttributeValue(int type, int attributeSize, int arrayElementSize, int index, int stride, bool normalized):
  _type(type),
  _attributeSize(attributeSize),
  _index(index),
  _stride(stride),
  _normalized(normalized),
  _arrayElementSize(arrayElementSize){}
  
  int getType() const { return _type;}
  int getAttributeSize() const { return _attributeSize;}
  int getIndex() const { return _index;}
  int getStride() const { return _stride;}
  bool getNormalized() const { return _normalized;}
  virtual ~GPUAttributeValue(){}
  virtual void setAttribute(GL* gl, const int id) const = 0;
  virtual bool isEqualsTo(const GPUAttributeValue* v) const = 0;
  virtual GPUAttributeValue* shallowCopy() const = 0;
};

class GPUAttribute{
protected:
  const std::string _name;
  const int _id;
  
  bool _dirty;
  GPUAttributeValue* _value;
  
  const int _type;
  const int _size;
  
public:
  
  virtual ~GPUAttribute(){
    delete _value;
  }
  
  GPUAttribute(const std::string&name, int id, int type, int size):
  _name(name),
  _id(id),
  _dirty(false),
  _value(NULL),
  _type(type),
  _size(size){}
  
  const std::string getName() const{ return _name;}
  const int getID() const{ return _id;}
  int getType() const{ return _type;}
  int getSize() const{ return _size;}
  
  void set(GPUAttributeValue* v){
    if (_type != v->getType() || _size != v->getAttributeSize()){ //type checking
      delete v;
      ILogger::instance()->logError("Attempting to set uniform " + _name + "with invalid value type.");
      return;
    }
    if (_value == NULL || _value->isEqualsTo(v)){
      _dirty = true;
      if (_value != NULL){
        delete _value;
      }
      _value = v->shallowCopy();
    }
  }
  
  virtual void applyChanges(GL* gl){
    if (_dirty){
      _value->setAttribute(gl, _id);
      _dirty = false;
    }
  }
};

///////////

class GPUAttributeValueVecFloat: public GPUAttributeValue{
  IFloatBuffer* _buffer;
  int _timeStamp;
public:
  GPUAttributeValueVecFloat(IFloatBuffer* buffer, int arrayElementSize, int attributeSize, int index, int stride, bool normalized):
  _buffer(buffer),
  _timeStamp(buffer->timestamp()),
  GPUAttributeValue(GLType::glFloat(), attributeSize, arrayElementSize, index, stride, normalized){}
  
  void setAttribute(GL* gl, const int id) const{
    gl->vertexAttribPointer(_index, _attributeSize, _normalized, _stride, _buffer);
  }
  
  bool isEqualsTo(const GPUAttributeValue* v) const{
    return (_buffer == ((GPUAttributeValueVecFloat*)v)->_buffer) &&
    (_timeStamp == ((GPUAttributeValueVecFloat*)v)->_timeStamp) &&
    (_type == v->getType()) &&
    (_attributeSize == v->getAttributeSize()) &&
    (_stride == v->getStride()) &&
    (_normalized == v->getNormalized());
  }
  
  GPUAttributeValue* shallowCopy() const{
    GPUAttributeValueVecFloat* v = new GPUAttributeValueVecFloat(_buffer, _attributeSize,
                                                                 _arrayElementSize, _index, _stride, _normalized);
    v->_timeStamp = _timeStamp;
    return v;
  }
  
};

class GPUAttributeValueVec1Float: public GPUAttributeValueVecFloat{
public:
  GPUAttributeValueVec1Float(IFloatBuffer* buffer, int arrayElementSize, int index, int stride, bool normalized):
  GPUAttributeValueVecFloat(buffer, 1, arrayElementSize, index, stride, normalized){}
};
class GPUAttributeVec1Float: public GPUAttribute{
public:
  GPUAttributeVec1Float(const std::string&name, int id):GPUAttribute(name, id, GLType::glFloat(), 1){}
};
////////
///////////
class GPUAttributeValueVec2Float: public GPUAttributeValueVecFloat{
public:
  GPUAttributeValueVec2Float(IFloatBuffer* buffer, int arrayElementSize, int index, int stride, bool normalized):
  GPUAttributeValueVecFloat(buffer, 2, arrayElementSize, index, stride, normalized){}
};

class GPUAttributeVec2Float: public GPUAttribute{
public:
  GPUAttributeVec2Float(const std::string&name, int id):GPUAttribute(name, id, GLType::glFloat(), 2){}
};
////////

///////////
class GPUAttributeValueVec3Float: public GPUAttributeValueVecFloat{
public:
  GPUAttributeValueVec3Float(IFloatBuffer* buffer, int arrayElementSize, int index, int stride, bool normalized):
  GPUAttributeValueVecFloat(buffer, 3, arrayElementSize, index, stride, normalized){}
};
class GPUAttributeVec3Float: public GPUAttribute{
public:
  GPUAttributeVec3Float(const std::string&name, int id):GPUAttribute(name, id, GLType::glFloat(), 3){}
};
////////

///////////
class GPUAttributeValueVec4Float: public GPUAttributeValueVecFloat{
public:
  GPUAttributeValueVec4Float(IFloatBuffer* buffer, int arrayElementSize, int index, int stride, bool normalized):
  GPUAttributeValueVecFloat(buffer, 4, arrayElementSize, index, stride, normalized){}
};
class GPUAttributeVec4Float: public GPUAttribute{
public:
  GPUAttributeVec4Float(const std::string&name, int id):GPUAttribute(name, id, GLType::glFloat(), 4){}
};
////////

#endif


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
