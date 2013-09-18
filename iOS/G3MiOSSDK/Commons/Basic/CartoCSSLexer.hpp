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

  //  CartoCSSToken* _previousToken;
  //  bool           _returnPreviousToken;

  const IStringUtils* _su;

  bool skipComments();

  bool skipBlanks();

  void skipCommentsAndBlanks();

  CartoCSSLexer(const std::string& source);

  //  void revert() {
  //    _returnPreviousToken = true;
  //  }

  const CartoCSSToken* getNextToken();

public:
  static std::vector<const CartoCSSToken*> tokenize(const std::string& source);
  
};

#endif
