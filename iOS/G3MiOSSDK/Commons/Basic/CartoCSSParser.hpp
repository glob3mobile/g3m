//
//  CartoCSSParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/16/13.
//
//

#ifndef __G3MiOSSDK__CartoCSSParser__
#define __G3MiOSSDK__CartoCSSParser__

#include <vector>
#include <string>
#include "CartoCSSTokens.hpp"
#include "CartoCSSSymbolizer.hpp"
class IByteBuffer;
class IStringUtils;
class CartoCSSLexer;


class CartoCSSError {
private:
  std::string _description;
  int         _position;

public:
  CartoCSSError(const std::string& description,
                int position) :
  _description(description),
  _position(position)
  {
  }

  ~CartoCSSError() {
  }

  std::string getDescription() const {
    return _description;
  }

  int getPosition() const {
    return _position;
  }
};


class CartoCSSResult {
private:
//  std::vector<CartoCSSSymbolizer> _symbolizers;
  CartoCSSSymbolizer         _symbolizer;
  std::vector<CartoCSSError> _errors;

public:
  CartoCSSResult() {
  }

  ~CartoCSSResult() {
//    const int errorsSize = _errors.size();
//    for (int i = 0; i < errorsSize; i++) {
//      delete _errors[i];
//    }
//
//    int __delete__symbolizers;
  }

//  void addSymbolizer(const CartoCSSSymbolizer& symbolizer) {
//    _symbolizers.push_back(symbolizer);
//  }
//
//  std::vector<CartoCSSSymbolizer> getSymbolizers() const {
//    return _symbolizers;
//  }

  void setSymbolizer(const CartoCSSSymbolizer& symbolizer) {
    _symbolizer = symbolizer;
  }

  CartoCSSSymbolizer getSymbolizer() const {
    return _symbolizer;
  }

  void addError(const CartoCSSError& error) {
    _errors.push_back(error);
  }

  bool hasError() const {
    return !_errors.empty();
  }

  std::vector<CartoCSSError> getErrors() const {
    return _errors;
  }

};


class CartoCSSParser {
private:
  std::vector<CartoCSSTokenKind> _variableDefinitionTokensKind;


  const std::vector<const CartoCSSToken*> _tokens;
  const int                               _tokensSize;

  int _tokensCursor;

//  CartoCSSResult* _result;

  CartoCSSParser(const std::string& source);

  CartoCSSResult* pvtParse();

  ~CartoCSSParser() {
  }

  bool lookAhead(const std::vector<CartoCSSTokenKind>& expectedTokensKind) const;
  int lookAheadWithBalancedBraces(const CartoCSSTokenKind expectedTokenKind) const;

  CartoCSSResult* document();

public:
  static CartoCSSResult* parse(const std::string& css);

  static CartoCSSResult* parse(const IByteBuffer* css);
};

#endif
