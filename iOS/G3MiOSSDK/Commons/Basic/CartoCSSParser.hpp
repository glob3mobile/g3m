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

class CartoCSSSymbolizer;
class IByteBuffer;
class IStringUtils;
class CartoCSSLexer;

class CartoCSSError {
private:
  const std::string _description;
  const int         _fromIndex;
  const int         _endIndex;

public:
  CartoCSSError(const std::string& description,
                int fromIndex,
                int endIndex) :
  _description(description),
  _fromIndex(fromIndex),
  _endIndex(endIndex)
  {

  }

  ~CartoCSSError() {
  }

  std::string getDescription() const {
    return _description;
  }

  int getFromIndex() const {
    return _fromIndex;
  }

  int getEndIndex() const {
    return _endIndex;
  }
};


class CartoCSSResult {
private:
  std::vector<CartoCSSSymbolizer*> _symbolizers;
  std::vector<CartoCSSError*>      _errors;

public:
  CartoCSSResult() {
  }

  ~CartoCSSResult() {
    const int errorsSize = _errors.size();
    for (int i = 0; i < errorsSize; i++) {
      delete _errors[i];
    }

    int __delete__symbolizers;
  }

  void addSymbolizer(CartoCSSSymbolizer* symbolizer) {
    _symbolizers.push_back(symbolizer);
  }

  void addError(CartoCSSError* error) {
    _errors.push_back(error);
  }

  std::vector<CartoCSSSymbolizer*> getSymbolizers() const {
    return _symbolizers;
  }

  bool hasError() const {
    return !_errors.empty();
  }

  std::vector<CartoCSSError*> getErrors() const {
    return _errors;
  }

};


class CartoCSSParser {
private:
//  const std::string _css;
//  const int         _cssSize;
//
//  const IStringUtils* _su;
//  int _cursor;

  CartoCSSLexer* _lexer;

  CartoCSSResult* _result;

  CartoCSSParser(const std::string& css);

  CartoCSSResult* pvtParse();

//  bool parseSymbolizer();

//  int findClosingBrace(int openBracePosition);

  ~CartoCSSParser();

public:

  static CartoCSSResult* parse(const std::string& css);

  static CartoCSSResult* parse(const IByteBuffer* css);
  
};

#endif
