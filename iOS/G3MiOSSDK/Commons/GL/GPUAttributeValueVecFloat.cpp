//
//  GPUAttributeValueVecFloat.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

#include "GPUAttributeValueVecFloat.hpp"

#include "IFloatBuffer.hpp"
#include "GLConstants.hpp"
#include "ILogger.hpp"
#include "GL.hpp"
#include "IStringBuilder.hpp"


GPUAttributeValueVecFloat::GPUAttributeValueVecFloat(const IFloatBuffer* buffer,
                                                     int attributeSize,
                                                     int arrayElementSize,
                                                     int index,
                                                     int stride,
                                                     bool normalized) :
GPUAttributeValue(GLType::glFloat(),
                  attributeSize,
                  arrayElementSize,
                  index,
                  stride,
                  normalized),
_buffer(buffer),
_timestamp(buffer->timestamp()),
_id(buffer->getID())
{

}

void GPUAttributeValueVecFloat::setAttribute(GL* gl,
                                             const int id) const {
  if (_index != 0) {
    //TODO: Change vertexAttribPointer
    ILogger::instance()->logError("INDEX NO 0");
  }

  gl->vertexAttribPointer(id, _arrayElementSize, _normalized, _stride, _buffer);
}

bool GPUAttributeValueVecFloat::isEquals(const GPUAttributeValue* v) const {

  if (!v->_enabled) {
    return false;          //Is a disabled value
  }
  GPUAttributeValueVecFloat* vecV = (GPUAttributeValueVecFloat*)v;
  bool equal = ((_id            == vecV->_buffer->getID()) &&
                (_timestamp     == vecV->_timestamp      ) &&
                (_type          == v->_type              ) &&
                (_attributeSize == v->_attributeSize     ) &&
                (_stride        == v->_stride            ) &&
                (_normalized    == v->_normalized        ) );

  return equal;
}


const std::string GPUAttributeValueVecFloat::description() const {
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

  const std::string s = isb->getString();
  delete isb;
  return s;
}
