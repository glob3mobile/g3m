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
#include "CartoCSSLexer.hpp"
#include <map>


CartoCSSParser::CartoCSSParser(const std::string& source) :
//_css(css),
//_cssSize(css.size()),
//_cursor(0),
//_lexer(new CartoCSSLexer(css)),
_tokens(CartoCSSLexer::tokenize(source)),
_tokensSize(_tokens.size()),
_result(new CartoCSSResult())
//_su(IStringUtils::instance())
{
}

//CartoCSSParser::~CartoCSSParser() {
//  delete _lexer;
//}

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

  for (int i = 0; i < _tokensSize; i++) {
    const CartoCSSToken* token = _tokens[i];

    ILogger::instance()->logInfo("%s", token->description().c_str());
    if (token->_kind == VARIABLE) {
      const VariableCartoCSSToken* variableToken = (VariableCartoCSSToken*) token;
      variables[variableToken->_name] = variableToken->_value;
    }
    else if (token->_kind == ERROR) {
      const ErrorCartoCSSToken* errorToken = (ErrorCartoCSSToken*) token;
      _result->addError(new CartoCSSError(errorToken->_message,
                                          errorToken->_position));
      break;
    }
  }

  for (int i = 0; i < _tokensSize; i++) {
    const CartoCSSToken* token = _tokens[i];
    delete token;
  }


//  bool finish = false;
//  while (!finish) {
//    const CartoCSSToken* token = _lexer->getNextToken();
//
//    if (token == NULL) {
//      finish = true;
//    }
//    else {
//      ILogger::instance()->logInfo("%s", token->description().c_str());
//
//      if (token->_kind == VARIABLE) {
//        const VariableCartoCSSToken* variableToken = (VariableCartoCSSToken*) token;
//        variables[variableToken->_name] = variableToken->_value;
//      }
//      else if (token->_kind == ERROR) {
//        finish = true;
//        const ErrorCartoCSSToken* errorToken = (ErrorCartoCSSToken*) token;
//        _result->addError(new CartoCSSError(errorToken->_message,
//                                            errorToken->_position));
//      }
//
//      delete token;
//    }
//  }

  return _result;
}
