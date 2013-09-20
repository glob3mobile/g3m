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
#include <map>
#include <vector>
class IStringBuilder;


class CartoCSSSymbolizer {
private:
  mutable std::map<std::string, std::string> _variables;
  mutable std::map<std::string, std::string> _properties;
  std::vector<CartoCSSSymbolizer*>   _children;

  CartoCSSSymbolizer* _parent;
  void setParent(CartoCSSSymbolizer* parent);

  int getDepth() const {
    return (_parent == NULL) ? 0 : _parent->getDepth()+1;
  }

  void indent(IStringBuilder* isb,
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
