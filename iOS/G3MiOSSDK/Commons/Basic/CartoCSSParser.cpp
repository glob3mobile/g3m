//
//  CartoCSSParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/16/13.
//
//

#include "CartoCSSParser.hpp"

#include "IByteBuffer.hpp"
#include "IStringUtils.hpp"
#include "CartoCSSTokens.hpp"
#include <map>




class CartoCSSLexer {
private:
  const std::string _source;
  const int         _sourceSize;
  int               _cursor;

  //  CartoCSSToken* _previousToken;
  //  bool           _returnPreviousToken;

  const IStringUtils* _su;

  bool skipComments() {
    if (_cursor < _sourceSize-1) {
      const char c     = _source[_cursor];
      const char nextC = _source[_cursor+1];
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

  bool skipBlanks() {
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

  void skipCommentsAndBlanks() {
    bool skipedBlanks;
    bool skipedCommment;
    do {
      skipedBlanks   = skipBlanks();
      skipedCommment = skipComments();
    }
    while (skipedBlanks || skipedCommment);
  }

public:
  CartoCSSLexer(const std::string& source) :
  _source(source),
  _sourceSize(source.size()),
  _cursor(0),
  //  _previousToken(NULL),
  //  _returnPreviousToken(false),
  _su(IStringUtils::instance())
  {
  }

  //  void revert() {
  //    _returnPreviousToken = true;
  //  }

  const CartoCSSToken* getNextToken() {
    //    if (_returnPreviousToken) {
    //      _returnPreviousToken = false;
    //      return _previousToken;
    //    }

    skipCommentsAndBlanks();
    if (_cursor >= _sourceSize) {
      return NULL;
    }

    CartoCSSToken* token;

    const char c = _source[_cursor];

    switch (c) {

      case '{': {
        token = new OpenBraceCartoCSSToken();
        _cursor++;
        break;
      }

      case '}': {
        token = new CloseBraceCartoCSSToken();
        _cursor++;
        break;
      }

      case '@': {
        token = new AtCartoCSSToken();
        _cursor++;
        break;
      }

      case ':': {
        token = new ColonCartoCSSToken();
        _cursor++;
        break;
      }

      case ';': {
        token = new SemicolonCartoCSSToken();
        _cursor++;
        break;
      }

      case '[': {
        const int closeBraquetPosition = _su->indexOf(_source, "]", _cursor+1);
        if (closeBraquetPosition < 0) {
          token = new ErrorCartoCSSToken("Unbalanced braquet", _cursor);
        }
        else {
          token = new ExpressionCartoCSSToken(_su->substring(_source, _cursor+1, closeBraquetPosition));
          _cursor = closeBraquetPosition+1;
        }
        break;
      }

        //      case '@': {
        //        const int semicolonPosition = _su->indexOf(_source, ";", _cursor+1);
        //        if (semicolonPosition < 0) {
        //          token = new ErrorCartoCSSToken("Can't find semicolon (;)", _cursor);
        //        }
        //        else {
        //          const int colonPosition = _su->indexOf(_source, ":", _cursor+1, semicolonPosition);
        //          if (colonPosition < 0) {
        //            token = new ErrorCartoCSSToken("Can't find colon (:)", _cursor);
        //          }
        //          else {
        //            const std::string name  = _su->trim( _su->substring(_source, _cursor+1, colonPosition) );
        //            const std::string value = _su->trim( _su->substring(_source, colonPosition+1, semicolonPosition) );
        //            token = new VariableCSSToken(name, value);
        //            _cursor = semicolonPosition+1;
        //          }
        //        }
        //        break;
        //      }

        //        OPEN_BRACE,           // {
        //        CLOSE_BRACE,          // }
        //        CONDITION_SELECTOR,
        //        VARIABLE,
        //        AT,                   // @
        //        COLON,                // :
        //        SEMICOLON,            // ;

      default: {
        const int cursor = _su->indexOfFirstNonChar(_source, "{}@:;", _cursor);
        if (cursor < 0) {
          token = new ErrorCartoCSSToken("Unknown token", _cursor);
        }
        else {
          const std::string str = _su->substring(_source, _cursor, cursor);
          token = new StringCartoCSSToken( str );
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
    
    return token;
  }
};


CartoCSSParser::CartoCSSParser(const std::string& css) :
//_css(css),
//_cssSize(css.size()),
//_cursor(0),
_lexer(new CartoCSSLexer(css)),
_result(new CartoCSSResult())
//_su(IStringUtils::instance())
{
}

CartoCSSParser::~CartoCSSParser() {
  delete _lexer;
}

CartoCSSResult* CartoCSSParser::parse(const IByteBuffer* css) {
  return parse(css->getAsString());
}

CartoCSSResult* CartoCSSParser::parse(const std::string& css) {
  CartoCSSParser parser(css);
  return parser.pvtParse();
}

//int CartoCSSParser::findClosingBrace(int openBracePosition) {
//  int openedCounter = 1;
//  for (int i = openBracePosition+1; i < _cssSize; i++) {
//    const char c = _css[i];
//    if (c == '{') {
//      openedCounter++;
//    }
//    else if (c == '}') {
//      openedCounter--;
//      if (openedCounter <= 0) {
//        return i;
//      }
//    }
//  }
//  return -1;
//}

//bool CartoCSSParser::parseSymbolizer() {
//  const int cursor = _su->indexOfFirstNonBlank(_css, _cursor);
//  if ((cursor < 0) || (cursor > _cssSize)) {
//    return false;
//  }
//  _cursor = cursor;
//
//  const int openBracePosition = _su->indexOf(_css, "{", _cursor);
//  if (openBracePosition < 0) {
//    _result->addError(new CartoCSSError("Can't find an open grace '{'",
//                                        _cursor, _cssSize));
//    return false;
//  }
//
//  const int closeBracePosition = findClosingBrace(openBracePosition);
//  if (closeBracePosition < 0) {
//    _result->addError(new CartoCSSError("Can't find an closing brace '}'",
//                                        openBracePosition, _cssSize));
//    return false;
//  }
//
//  // selectors:
//  //
//  //  /* All layers */
//  //  * {}
//  //  /* layers with class "red" */
//  //  .red {}
//  //  /* layers with class blue beyond zoom 8 */
//  //  .blue[zoom > 8] {}
//  //  /* features in #countries with NAME 'USA' */
//  // #countries[NAME='USA']
//
//  // [condition]
//  // [zoom > 1]  --> zoom is tile's level
//  // #layerName
//  // #layerName::symbolizerName
//  // #layerName[condition]
//  // #layerName[field=5]
//  // #layerName[field>5]
//  // [condition]
//  // .class
//
//  const std::string selectorString = _su->substring(_css, _cursor, openBracePosition);
//  ILogger::instance()->logInfo("Selector: \"%s\"", selectorString.c_str());
//
//
//  // #earthquakes {
//  //   [Magnitude >= 2.5] { marker-width:6; }
//  //   [Magnitude >= 3]   { marker-width:8; }
//  //   [Magnitude >= 3.5] { marker-width:10; }
//  //   [Magnitude >= 4]   { marker-width:12; }
//  //   [Magnitude >= 4.5] { marker-width:14; }
//  //   [Magnitude >= 5]   { marker-width:16; }
//  //   [Magnitude >= 5.5] { marker-width:18; }
//  //   [Magnitude >= 6]   { marker-width:20; }
//  // }
//
//
//  const std::string symbolizerBody = _su->substring(_css, openBracePosition+1, closeBracePosition);
//  ILogger::instance()->logInfo("Body: \"%s\"", symbolizerBody.c_str());
//
//  //  const std::string symbolizerString = _su->substring(_css, _cursor, closeBracePosition+1);
//  //  ILogger::instance()->logInfo("\"%s\"", symbolizerString.c_str());
//
//  _cursor = closeBracePosition + 1;
//
//  int __DGD_At_Work;
//
//  return true;
//}






CartoCSSResult* CartoCSSParser::pvtParse() {
  //  bool continueParsing = true;
  //  while (continueParsing && (_cursor < _cssSize)) {
  //    continueParsing = parseSymbolizer();
  //  }


  std::map<std::string, std::string> variables;

  bool finish = false;
  while (!finish) {
    const CartoCSSToken* token = _lexer->getNextToken();

    if (token != NULL) {
      ILogger::instance()->logInfo("%s", token->description().c_str());

      if (token->_kind == VARIABLE) {
        const VariableCartoCSSToken* variableToken = (VariableCartoCSSToken*) token;
        variables[variableToken->_name] = variableToken->_value;
      }
    }

    finish = (token == NULL) || (token->_kind == ERROR);
    
    delete token;
  }
  
  return _result;
}
