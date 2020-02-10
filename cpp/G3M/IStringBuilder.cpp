//
//  IStringBuilder.cpp
//  G3M
//
//  Created by José Miguel S N on 22/08/12.
//

#include "IStringBuilder.hpp"
#include "ILogger.hpp"

IStringBuilder* IStringBuilder::_exemplar = NULL;

const int IStringBuilder::DEFAULT_FLOAT_PRECISION = 20;


void IStringBuilder::setInstance(IStringBuilder* exemplar) {
  if (_exemplar != NULL) {
    ILogger::instance()->logWarning("IStringBuilder set two times");
  }
  _exemplar = exemplar;
}

IStringBuilder* IStringBuilder::newStringBuilder(const int floatPrecision) {
  return _exemplar->clone(floatPrecision);
}
