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
  CartoCSSSymbolizer*        _symbolizer;
  std::vector<CartoCSSError> _errors;

public:
  CartoCSSResult(CartoCSSSymbolizer* symbolizer) :
  _symbolizer(symbolizer)
  {
  }

  ~CartoCSSResult() {
  }

  const CartoCSSSymbolizer* getSymbolizer() const {
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
  std::vector<CartoCSSTokenType> _variableDefinitionTokensType;

#ifdef C_CODE
  const std::vector<CartoCSSToken*> _tokens;
#else
  std::vector<CartoCSSToken*> _tokens;
#endif
  const int                         _tokensSize;

  int _tokensCursor;

  CartoCSSParser(const std::string& source);

  CartoCSSResult* pvtParse();

  ~CartoCSSParser() {
  }

  bool lookAhead(const std::vector<CartoCSSTokenType>& expectedTokensType,
                 int from,
                 int to) const;

  bool lookAhead(const std::vector<CartoCSSTokenType>& expectedTokensType) const {
    return lookAhead(expectedTokensType, _tokensCursor, _tokensSize);
  }

  int lookAheadManyOf(const CartoCSSTokenType alternative1,
                      const CartoCSSTokenType alternative2,
                      int from,
                      int to) const;
  int lookAheadBalancedBraces(int from,
                              int to) const;

  bool parseVariableDeclaration(CartoCSSSymbolizer* currentSymbolizer);
  bool parseSymbolizerBlock(CartoCSSSymbolizer* currentSymbolizer);
  int parseSymbolizerBlock(CartoCSSSymbolizer* currentSymbolizer,
                           int from,
                           int to) const;

  CartoCSSResult* document();

public:
  static CartoCSSResult* parse(const std::string& css);

  static CartoCSSResult* parse(const IByteBuffer* css);
};

#endif
