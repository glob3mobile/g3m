//
//  CartoCSSSymbolizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/16/13.
//
//

#ifndef __G3MiOSSDK__CartoCSSSymbolizer__
#define __G3MiOSSDK__CartoCSSSymbolizer__

#include <string>
//#include <map>
#include <vector>
class IStringBuilder;

class CartoCSSVariable {
public:
  const std::string _name;
  const std::string _value;

  CartoCSSVariable(const std::string& name,
                   const std::string& value) :
  _name(name),
  _value(value)
  {
  }

  ~CartoCSSVariable() {

  }
};


class CartoCSSSymbolizer {
private:
  std::vector<std::string>         _selectors;
  std::vector<CartoCSSVariable*>   _variables;
  std::vector<CartoCSSVariable*>   _properties;
  std::vector<CartoCSSSymbolizer*> _children;

  CartoCSSSymbolizer* _parent;
  void setParent(CartoCSSSymbolizer* parent);

  int getDepth() const {
    return (_parent == NULL) ? 0 : _parent->getDepth()+1;
  }

  void indent(IStringBuilder* isb,
              int delta) const;

  void buildSelectorsDescription(IStringBuilder* isb,
                                 int delta) const;
  void buildVariablesDescription(IStringBuilder* isb,
                                 int delta) const;
  void buildPropertiesDescription(IStringBuilder* isb,
                                  int delta) const;
  void buildChildrenDescription(IStringBuilder* isb,
                                int delta) const;

public:
  CartoCSSSymbolizer() :
  _parent(NULL)
  {
  }

  CartoCSSSymbolizer(const std::vector<std::string>& selectors) :
  _selectors(selectors),
  _parent(NULL)
  {
  }

  ~CartoCSSSymbolizer();

  void setVariableDeclaration(const std::string& name,
                              const std::string& value);

  void setProperty(const std::string& name,
                   const std::string& value);

  void addSymbolizer(CartoCSSSymbolizer* symbolizer);

  const std::string description(bool detailed=false,
                                int delta=0) const;

};

#endif
