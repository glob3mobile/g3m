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
  const int childrenSize = _children.size();
  for (int i = 0; i < childrenSize; i++) {
    CartoCSSSymbolizer* child = _children[i];
    delete child;
  }
}

void CartoCSSSymbolizer::setVariableDeclaration(const std::string& name,
                                                const std::string& value) {
  const IStringUtils* su = IStringUtils::instance();
  const std::string k = su->trim(name);
  const std::string v = su->trim(value);
  _variables[k] = v;
}

void CartoCSSSymbolizer::setProperty(const std::string& name,
                                     const std::string& value) {
  const IStringUtils* su = IStringUtils::instance();
  const std::string k = su->trim(name);
  const std::string v = su->trim(value);
  _properties[k] = v;
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
    isb->addString("  ");
  }
}

void CartoCSSSymbolizer::buildVariablesDescription(IStringBuilder* isb,
                                                   int delta) const {
  indent(isb, 1 + delta);
  isb->addString("variables={");
  if (!_variables.empty()) {
    for (std::map<std::string, std::string>::iterator iter = _variables.begin();
         iter != _variables.end();
         iter++) {
      isb->addString("\n");
      indent(isb, 2 + delta);
      isb->addString(iter->first);
      isb->addString(":");
      isb->addString(iter->second);
    }
    isb->addString("\n");
    indent(isb, 1 + delta);
  }
  isb->addString("}");
}

void CartoCSSSymbolizer::buildPropertiesDescription(IStringBuilder* isb,
                                                    int delta) const {
  indent(isb, 1 + delta);
  isb->addString("properties={");
  if (!_properties.empty()) {
    for (std::map<std::string, std::string>::iterator iter = _properties.begin();
         iter != _properties.end();
         iter++) {
      isb->addString("\n");
      indent(isb, 2 + delta);
      isb->addString(iter->first);
      isb->addString(":");
      isb->addString(iter->second);
    }
    isb->addString("\n");
    indent(isb, 1 + delta);
  }
  isb->addString("}");
}

void CartoCSSSymbolizer::buildChildrenDescription(IStringBuilder* isb,
                                                  int delta) const {
  indent(isb, 1 + delta);
  isb->addString("children={");
  if (!_children.empty()) {
    const int childrenSize = _children.size();
    for (int i = 0; i < childrenSize; i++) {
      isb->addString(_children[i]->description(true, 1 + delta));
    }
    isb->addString("\n");
    indent(isb, 1 + delta);
  }
  isb->addString("}");
}

const std::string CartoCSSSymbolizer::description(bool detailed,
                                                  int delta) const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  if (detailed) {
    isb->addString("\n");
    indent(isb, delta);
    isb->addString("[CartoCSSSymbolizer");

    isb->addString("\n");
    buildVariablesDescription(isb, delta);

    isb->addString("\n");
    buildPropertiesDescription(isb, delta);

    isb->addString("\n");
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
