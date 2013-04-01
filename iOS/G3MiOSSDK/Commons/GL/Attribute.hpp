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


class Attribute{
  std::string _name;
  int _id;
public:
  Attribute(std::string name, int id):_name(name),_id(id){}
  
  const std::string getName() const{ return _name;}
  int getID() const{ return _id;}
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
    
    if (equalsTo(buffer, index, stride, normalized)){
      _nativeGL->vertexAttribPointer(index,//Index
                                     _size,//Size
                                     normalized,//Normalized
                                     stride,//Stride
                                     buffer);
      
      save(buffer, index, stride, normalized);
    }
  }

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



#endif
