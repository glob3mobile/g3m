//
//  CartoCSSLexer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/18/13.
//
//

#include "CartoCSSLexer.hpp"

#include "IStringUtils.hpp"
#include "CartoCSSTokens.hpp"


CartoCSSLexer::CartoCSSLexer(const std::string& source) :
_source(source),
_sourceSize(source.size()),
_cursor(0),
//  _previousToken(NULL),
//  _returnPreviousToken(false),
_su(IStringUtils::instance()),
_lastToken(NULL)
{
}

bool CartoCSSLexer::skipComments() {
  if (_cursor < _sourceSize-1) {
#ifdef C_CODE
    const char c     = _source[_cursor];
    const char nextC = _source[_cursor+1];
#endif
#ifdef JAVA_CODE
    final char c = _source.charAt(_cursor);
    final char nextC = _source.charAt(_cursor+1);
#endif
    if (c == '/') {
      if (nextC == '/') {
        const int eolPosition = _su->indexOf(_source, "\n", _cursor+2);
        _cursor = (eolPosition < 0) ? _sourceSize : eolPosition + 1;
        return true;
      }
      else if (nextC == '*') {
        const int eocPosition = _su->indexOf(_source, "*/", _cursor+2);
        _cursor = (eocPosition < 0) ? _sourceSize : eocPosition + 3;
        return true;
      }
    }
  }
  return false;
}

bool CartoCSSLexer::skipBlanks() {
  const int cursor = _su->indexOfFirstNonBlank(_source, _cursor);
  if (cursor < 0) {
    _cursor = _sourceSize;
    return false;
  }
  const bool changedCursor = (cursor != _cursor);
  if (changedCursor) {
    _cursor = cursor;
  }
  return changedCursor;
}

void CartoCSSLexer::skipCommentsAndBlanks() {
  bool skipedBlanks;
  bool skipedCommment;
  do {
    skipedBlanks   = skipBlanks();
    skipedCommment = skipComments();
  }
  while (skipedBlanks || skipedCommment);
}


CartoCSSToken* CartoCSSLexer::getNextToken() {
  //    if (_returnPreviousToken) {
  //      _returnPreviousToken = false;
  //      return _previousToken;
  //    }

  skipCommentsAndBlanks();
  if (_cursor >= _sourceSize) {
    return NULL;
  }

  CartoCSSToken* token;

#ifdef C_CODE
  const char c = _source[_cursor];
#endif
#ifdef JAVA_CODE
  final char c = _source.charAt(_cursor);
#endif

  switch (c) {

    case '{': {
      token = new OpenBraceCartoCSSToken(_cursor);
      _cursor++;
      break;
    }

    case '}': {
      token = new CloseBraceCartoCSSToken(_cursor);
      _cursor++;
      break;
    }

    case ':': {
      if ((_cursor + 1 < _sourceSize) &&
          (_source[_cursor + 1] == ':')) {
        token = new StringCartoCSSToken("::", _cursor);
        _cursor += 2;
      }
      else {
        token = new ColonCartoCSSToken(_cursor);
        _cursor++;
      }
      break;
    }

    case ';': {
      token = new SemicolonCartoCSSToken(_cursor);
      _cursor++;
      break;
    }

    case '[': {
      const int closeBraquetPosition = _su->indexOf(_source, "]", _cursor+1);
      if (closeBraquetPosition < 0) {
        token = new ErrorCartoCSSToken("Unbalanced braquet", _cursor);
      }
      else {
        token = new ExpressionCartoCSSToken(_su->substring(_source, _cursor+1, closeBraquetPosition),
                                            _cursor);
        _cursor = closeBraquetPosition+1;
      }
      break;
    }

    default: {
      const int cursor = _su->indexOfFirstNonChar(_source, "{}:;[]\n\r", _cursor);
      if (cursor < 0) {
        token = new ErrorCartoCSSToken("Unknown token", _cursor);
      }
      else {
        const std::string str = _su->substring(_source, _cursor, cursor);
        if ((_lastToken != NULL) &&
            (_lastToken->_type == STRING)) {
          ((StringCartoCSSToken*) _lastToken)->appendString(str);
          token = new SkipCartoCSSToken(_cursor);
        }
        else {
          token = new StringCartoCSSToken(str, _cursor);
        }
        _cursor = cursor;
      }

      break;
    }
  }

  //
  // http://www.w3.org/TR/CSS21/grammar.html
  //
  //   h          [0-9a-f]
  //   unicode    \\{h}{1,6}(\r\n|[ \t\r\n\f])?
  //   escape     {unicode}|\\[^\r\n\f0-9a-f]
  //   nonascii   [\240-\377]
  //   nmchar     [_a-z0-9-]|{nonascii}|{escape}
  //   name       {nmchar}+

  //    _previousToken = token;

  _lastToken = token;
  return token;
}

std::vector<CartoCSSToken*> CartoCSSLexer::tokenize(const std::string& source) {
  CartoCSSLexer lexer(source);

  std::vector<CartoCSSToken*> result;

  bool finish = false;
  while (!finish) {
    CartoCSSToken* token = lexer.getNextToken();

    if (token == NULL) {
      finish = true;
    }
    else {
      if (token->_type == ERROR) {
        finish = true;
      }

      if (token->_type == SKIP) {
        delete token;
      }
      else {
        result.push_back(token);
      }
    }
  }

  return result;
}

