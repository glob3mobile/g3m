//
//  GPUAttribute.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/05/13.
//
//

#include "GPUAttribute.hpp"

#include "GPUAttributeValue.hpp"
#include "GL.hpp"
#include "ILogger.hpp"


GPUAttribute::GPUAttribute(const std::string& name,
                           int id,
                           int type,
                           int size) :
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

GPUAttribute::~GPUAttribute() {
  if (_value != NULL) {
    _value->_release();
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

bool GPUAttribute::wasSet() const {
  return _value != NULL;
}

bool GPUAttribute::isEnabled() const {
  return _enabled;
}

int GPUAttribute::getIndex() const {
#ifdef C_CODE
  return _key;
#endif
#ifdef JAVA_CODE
  return _key.getValue();
#endif
}

void GPUAttribute::unset(GL* gl) {
  if (_value != NULL) {
    _value->_release();
    _value = NULL;
  }
  _enabled = false;
  _dirty = false;

  gl->disableVertexAttribArray(_id);
}

void GPUAttribute::put(const GPUAttributeValue* v) {
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

void GPUAttribute::applyChanges(GL* gl) {
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
