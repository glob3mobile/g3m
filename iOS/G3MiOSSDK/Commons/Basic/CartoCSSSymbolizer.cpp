//
//  CartoCSSSymbolizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/16/13.
//
//

#include "CartoCSSSymbolizer.hpp"

#include "IStringUtils.hpp"
#include "IStringBuilder.hpp"


CartoCSSSymbolizer::~CartoCSSSymbolizer() {

  const int variablesSize = _variables.size();
  for (int i = 0; i < variablesSize; i++) {
    CartoCSSVariable* variable = _variables[i];
    delete variable;
  }

  const int propertiesSize = _properties.size();
  for (int i = 0; i < propertiesSize; i++) {
    CartoCSSVariable* property = _properties[i];
    delete property;
  }

  const int childrenSize = _children.size();
  for (int i = 0; i < childrenSize; i++) {
    CartoCSSSymbolizer* child = _children[i];
    delete child;
  }
}

void CartoCSSSymbolizer::setVariableDeclaration(const std::string& name,
                                                const std::string& value) {
  const IStringUtils* su = IStringUtils::instance();
  const std::string n = su->trim(name);
  const std::string v = su->trim(value);
  _variables.push_back(new CartoCSSVariable(n, v));
}

void CartoCSSSymbolizer::setProperty(const std::string& name,
                                     const std::string& value) {
  const IStringUtils* su = IStringUtils::instance();
  const std::string n = su->trim(name);
  const std::string v = su->trim(value);
  _properties.push_back(new CartoCSSVariable(n, v));
}

void CartoCSSSymbolizer::setParent(CartoCSSSymbolizer* parent) {
  if (parent == NULL) {
    ILogger::instance()->logError("Can't set a null parent");
  }
  else {
    if (_parent != NULL) {
      ILogger::instance()->logError("parent already set");
    }
    else {
      _parent = parent;
    }
  }
}

void CartoCSSSymbolizer::addSymbolizer(CartoCSSSymbolizer* symbolizer) {
  if (symbolizer != NULL) {
    _children.push_back(symbolizer);
    symbolizer->setParent(this);
  }
}

void CartoCSSSymbolizer::indent(IStringBuilder* isb,
                                int delta) const {
  const int depth = getDepth() + delta;
  for (int i = 0; i < depth; i++) {
    isb->addString("   ");
  }
}

void CartoCSSSymbolizer::buildSelectorsDescription(IStringBuilder* isb,
                                                   int delta) const {
  if (!_selectors.empty()) {
    isb->addString("\n");
    indent(isb, 1 + delta);
    isb->addString("selectors=");
    const int childrenSize = _selectors.size();
    for (int i = 0; i < childrenSize; i++) {
      isb->addString("\n");
      indent(isb, 2 + delta);
      isb->addString(_selectors[i]);
    }
  }
}

void CartoCSSSymbolizer::buildVariablesDescription(IStringBuilder* isb,
                                                   int delta) const {
  if (!_variables.empty()) {
    isb->addString("\n");
    indent(isb, 1 + delta);
    isb->addString("variables=");
    const int variablesSize = _variables.size();
    for (int i = 0; i < variablesSize; i++) {
      CartoCSSVariable* variable = _variables[i];
      isb->addString("\n");
      indent(isb, 2 + delta);
      isb->addString(variable->_name);
      isb->addString(":");
      isb->addString(variable->_value);
    }
  }
}

void CartoCSSSymbolizer::buildPropertiesDescription(IStringBuilder* isb,
                                                    int delta) const {
  if (!_properties.empty()) {
    isb->addString("\n");
    indent(isb, 1 + delta);
    isb->addString("properties=");
    const int propertiesSize = _properties.size();
    for (int i = 0; i < propertiesSize; i++) {
      CartoCSSVariable* property = _properties[i];
      isb->addString("\n");
      indent(isb, 2 + delta);
      isb->addString(property->_name);
      isb->addString(":");
      isb->addString(property->_value);
    }
  }
}

void CartoCSSSymbolizer::buildChildrenDescription(IStringBuilder* isb,
                                                  int delta) const {
  if (!_children.empty()) {
    isb->addString("\n");
    indent(isb, 1 + delta);
    isb->addString("children=");
    const int childrenSize = _children.size();
    for (int i = 0; i < childrenSize; i++) {
      isb->addString(_children[i]->description(true, 1 + delta));
    }
  }
}

const std::string CartoCSSSymbolizer::description(bool detailed,
                                                  int delta) const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  if (detailed) {
    isb->addString("\n");
    indent(isb, delta);
    isb->addString("[CartoCSSSymbolizer");

    buildSelectorsDescription(isb, delta);
    buildVariablesDescription(isb, delta);
    buildPropertiesDescription(isb, delta);
    buildChildrenDescription(isb, delta);

    isb->addString("\n");
    indent(isb, delta);
    isb->addString("]");
  }
  else {
    isb->addString("[CartoCSSSymbolizer ");
    isb->addString(" depth=");       isb->addInt(getDepth());
    isb->addString(", variables=");  isb->addInt(_variables.size());
    isb->addString(", properties="); isb->addInt(_properties.size());
    isb->addString(", children=");   isb->addInt(_children.size());
    isb->addString("]");
  }

  const std::string s = isb->getString();
  delete isb;
  return s;
}
