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
_tokens(CartoCSSLexer::tokenize(source)),
_tokensSize(_tokens.size()),
_result(new CartoCSSResult())
{
}

CartoCSSResult* CartoCSSParser::parse(const IByteBuffer* css) {
  return parse(css->getAsString());
}

CartoCSSResult* CartoCSSParser::parse(const std::string& css) {
  CartoCSSParser parser(css);
  return parser.pvtParse();
}

CartoCSSResult* CartoCSSParser::pvtParse() {
//  bool continueParsing = true;
//  while (continueParsing && (_cursor < _cssSize)) {
//    continueParsing = parseSymbolizer();
//  }

//  std::map<std::string, std::string> variables;
//
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



  // selectors:
  //
  //  /* All layers */
  //  * {}
  //  /* layers with class "red" */
  //  .red {}
  //  /* layers with class blue beyond zoom 8 */
  //  .blue[zoom > 8] {}
  //  /* features in #countries with NAME 'USA' */
  // #countries[NAME='USA']

  // [condition]
  // [zoom > 1]  --> zoom is tile's level
  // #layerName
  // #layerName::symbolizerName
  // #layerName[condition]
  // #layerName[field=5]
  // #layerName[field>5]
  // [condition]
  // .class
  
  // #earthquakes {
  //   [Magnitude >= 2.5] { marker-width:6; }
  //   [Magnitude >= 3]   { marker-width:8; }
  //   [Magnitude >= 3.5] { marker-width:10; }
  //   [Magnitude >= 4]   { marker-width:12; }
  //   [Magnitude >= 4.5] { marker-width:14; }
  //   [Magnitude >= 5]   { marker-width:16; }
  //   [Magnitude >= 5.5] { marker-width:18; }
  //   [Magnitude >= 6]   { marker-width:20; }
  // }


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



  return _result;
}
