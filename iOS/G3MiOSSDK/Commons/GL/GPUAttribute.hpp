//
//  Attribute.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

#ifndef G3MiOSSDK_Attribute
#define G3MiOSSDK_Attribute


#include "INativeGL.hpp"
#include "IFloatBuffer.hpp"
#include "GL.hpp"

#include "IStringBuilder.hpp"

#include "GPUVariable.hpp"

#include "RCObject.hpp"

class GPUAttribute;

class GPUAttributeValue : public RCObject {
protected:
  virtual ~GPUAttributeValue() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  const bool _enabled;
  const int  _type;
  const int  _attributeSize;
  const int  _index;
  const int  _stride;
  const bool _normalized;
  const int  _arrayElementSize;

  GPUAttributeValue(bool enabled):
  _enabled(enabled),
  _type(0),
  _attributeSize(0),
  _index(0),
  _stride(0),
  _normalized(0),
  _arrayElementSize(0)
  {}

  GPUAttributeValue(int type, int attributeSize, int arrayElementSize, int index, int stride, bool normalized):
  _enabled(true),
  _type(type),
  _attributeSize(attributeSize),
  _index(index),
  _stride(stride),
  _normalized(normalized),
  _arrayElementSize(arrayElementSize)
  {}

  //  int getType() const { return _type;}
  //  int getAttributeSize() const { return _attributeSize;}
  //  int getIndex() const { return _index;}
  //  int getStride() const { return _stride;}
  //  bool getNormalized() const { return _normalized;}
  //  bool getEnabled() const { return _enabled;}

  virtual void setAttribute(GL* gl, const int id) const = 0;
  virtual bool isEquals(const GPUAttributeValue* v) const = 0;
  virtual std::string description() const = 0;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};

class GPUAttribute: public GPUVariable {
private:

  bool _dirty;
#ifdef C_CODE
  const GPUAttributeValue* _value;
#endif
#ifdef JAVA_CODE
  private GPUAttributeValue _value;
#endif

  bool _enabled;


public:
  const int _id;
  const int _type;
  const int _size;
  const GPUAttributeKey _key;

  virtual ~GPUAttribute() {
    if (_value != NULL) {
      _value->_release();
    }

#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  GPUAttribute(const std::string&name, int id, int type, int size):
  GPUVariable(name, ATTRIBUTE),
  _id(id),
  _dirty(false),
  _value(NULL),
  _type(type),
  _size(size),
  _enabled(false),
  _key(getAttributeKey(name))
  {
  }

  //  const std::string getName() const{ return _name;}
  //  const int getID() const{ return _id;}
  //  int getType() const{ return _type;}
  //  int getSize() const{ return _size;}
  bool wasSet() const{ return _value != NULL;}
  bool isEnabled() const { return _enabled;}
  //  GPUAttributeKey getKey() const { return _key;}


  int getIndex() const {
#ifdef C_CODE
    return _key;
#endif
#ifdef JAVA_CODE
    return _key.getValue();
#endif
  }

  void unset(GL* gl) {
    if (_value != NULL) {
      _value->_release();
      _value = NULL;
    }
    _enabled = false;
    _dirty = false;

    gl->disableVertexAttribArray(_id);
  }

  void set(const GPUAttributeValue* v) {
    if (v != _value) {

      if (v->_enabled && _type != v->_type) { //type checking
        ILogger::instance()->logError("Attempting to set attribute " + _name + "with invalid value type.");
        return;
      }
      if (_value == NULL || !_value->isEquals(v)) {
        _dirty = true;

        if (_value != NULL) {
          _value->_release();
        }
        _value = v;
        _value->_retain();

      }
    }
  }


  virtual void applyChanges(GL* gl) {
    if (_value == NULL) {
      if (_enabled) {
        ILogger::instance()->logError("Attribute " + _name + " was not set but it is enabled.");
      }
    }
    else {
      if (_dirty) {
        if (_value->_enabled) {
          if (!_enabled) {
            gl->enableVertexAttribArray(_id);
            _enabled = true;
          }
          _value->setAttribute(gl, _id);
        }
        else {
          if (_enabled) {
            gl->disableVertexAttribArray(_id);
            _enabled = false;
          }
        }

        _dirty = false;
      }
    }
  }
};

///////////

class GPUAttributeValueDisabled : public GPUAttributeValue {
private:
  ~GPUAttributeValueDisabled() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeValueDisabled():
  GPUAttributeValue(false) {}

  void setAttribute(GL* gl, const int id) const{
  }

  bool isEquals(const GPUAttributeValue* v) const{
    return (v->_enabled == false);
  }

  GPUAttributeValue* shallowCopy() const{
    return new GPUAttributeValueDisabled();
  }

  std::string description() const{
    return "Attribute Disabled.";
  }

  GPUAttributeValue* copyOrCreate(GPUAttributeValue* oldAtt) const{
    if (oldAtt == NULL) {
      return new GPUAttributeValueDisabled();
    }
    if (oldAtt->_enabled) {
      oldAtt->_release();
      return new GPUAttributeValueDisabled();
    }
    return oldAtt;
  }

};

class GPUAttributeValueVecFloat : public GPUAttributeValue {
private:
  const IFloatBuffer* _buffer;
  const int _timeStamp;
  const long long _id;

protected:
  ~GPUAttributeValueVecFloat() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeValueVecFloat(IFloatBuffer* buffer, int attributeSize, int arrayElementSize, int index, int stride, bool normalized):
  GPUAttributeValue(GLType::glFloat(), attributeSize, arrayElementSize, index, stride, normalized),
  _buffer(buffer),
  _timeStamp(buffer->timestamp()),
  _id(buffer->getID()) {}

  void setAttribute(GL* gl, const int id) const{
    if (_index != 0) {
      //TODO: Change vertexAttribPointer
      ILogger::instance()->logError("INDEX NO 0");
    }

    gl->vertexAttribPointer(id, _arrayElementSize, _normalized, _stride, _buffer);
  }

  bool isEquals(const GPUAttributeValue* v) const{

    if (!v->_enabled) {
      return false;          //Is a disabled value
    }
    GPUAttributeValueVecFloat* vecV = (GPUAttributeValueVecFloat*)v;
    bool equal = ((_id      == vecV->_buffer->getID())     &&
                  (_timeStamp     == vecV->_timeStamp)  &&
                  (_type          == v->_type)          &&
                  (_attributeSize == v->_attributeSize) &&
                  (_stride        == v->_stride)        &&
                  (_normalized    == v->_normalized) );

    return equal;
  }

  std::string description() const{

    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    isb->addString("Attribute Value Float.");
    isb->addString(" ArrayElementSize:");
    isb->addInt(_arrayElementSize);
    isb->addString(" AttributeSize:");
    isb->addInt(_attributeSize);
    isb->addString(" Index:");
    isb->addInt(_index);
    isb->addString(" Stride:");
    isb->addInt(_stride);
    isb->addString(" Normalized:");
    isb->addBool(_normalized);

    std::string s = isb->getString();
    delete isb;
    return s;
  }

};

class GPUAttributeValueVec1Float: public GPUAttributeValueVecFloat {
private:
  ~GPUAttributeValueVec1Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeValueVec1Float(IFloatBuffer* buffer, int arrayElementSize, int index, int stride, bool normalized):
  GPUAttributeValueVecFloat(buffer, 1, arrayElementSize, index, stride, normalized) {}
};

class GPUAttributeVec1Float: public GPUAttribute {
private:
  ~GPUAttributeVec1Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeVec1Float(const std::string&name, int id):GPUAttribute(name, id, GLType::glFloat(), 1) {}
};

class GPUAttributeValueVec2Float: public GPUAttributeValueVecFloat {
private:
  ~GPUAttributeValueVec2Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeValueVec2Float(IFloatBuffer* buffer, int arrayElementSize, int index, int stride, bool normalized):
  GPUAttributeValueVecFloat(buffer, 2, arrayElementSize, index, stride, normalized) {}
};

class GPUAttributeVec2Float: public GPUAttribute {
private:
  ~GPUAttributeVec2Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeVec2Float(const std::string&name, int id):GPUAttribute(name, id, GLType::glFloat(), 2) {}
};
////////

///////////
class GPUAttributeValueVec3Float: public GPUAttributeValueVecFloat {
private:
  ~GPUAttributeValueVec3Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeValueVec3Float(IFloatBuffer* buffer, int arrayElementSize, int index, int stride, bool normalized):
  GPUAttributeValueVecFloat(buffer, 3, arrayElementSize, index, stride, normalized) {}
};

class GPUAttributeVec3Float: public GPUAttribute{
private:
  ~GPUAttributeVec3Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeVec3Float(const std::string&name, int id):GPUAttribute(name, id, GLType::glFloat(), 3) {}
};
////////

///////////
class GPUAttributeValueVec4Float: public GPUAttributeValueVecFloat {
private:
  ~GPUAttributeValueVec4Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

public:
  GPUAttributeValueVec4Float(IFloatBuffer* buffer, int arrayElementSize, int index, int stride, bool normalized):
  GPUAttributeValueVecFloat(buffer, 4, arrayElementSize, index, stride, normalized) {}
};

class GPUAttributeVec4Float: public GPUAttribute{
private:
  ~GPUAttributeVec4Float() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
  
public:
  GPUAttributeVec4Float(const std::string&name, int id):GPUAttribute(name, id, GLType::glFloat(), 4) {}
};
////////

#endif
