//
//  CartoCSSLexer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/18/13.
//
//

#ifndef __G3MiOSSDK__CartoCSSLexer__
#define __G3MiOSSDK__CartoCSSLexer__

#include <string>
#include <vector>
class IStringUtils;
class CartoCSSToken;


class CartoCSSLexer {
private:
  const std::string _source;
  const int         _sourceSize;
  int               _cursor;

  CartoCSSToken* _lastToken;
  //  bool           _returnPreviousToken;

  const IStringUtils* _su;

  bool skipComments();

  bool skipBlanks();

  void skipCommentsAndBlanks();

  CartoCSSLexer(const std::string& source);

  //  void revert() {
  //    _returnPreviousToken = true;
  //  }

  CartoCSSToken* getNextToken();

public:
  static std::vector<CartoCSSToken*> tokenize(const std::string& source);
  
};

#endif
