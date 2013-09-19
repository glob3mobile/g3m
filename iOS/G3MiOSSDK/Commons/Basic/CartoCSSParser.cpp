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
#include "CartoCSSLexer.hpp"
#include <map>


CartoCSSParser::CartoCSSParser(const std::string& source) :
_tokens(CartoCSSLexer::tokenize(source)),
_tokensSize(_tokens.size()),
_tokensCursor(0)
{
  _variableDefinitionTokensType.push_back(STRING);
  _variableDefinitionTokensType.push_back(COLON);
  _variableDefinitionTokensType.push_back(STRING);
  _variableDefinitionTokensType.push_back(SEMICOLON);
}

CartoCSSResult* CartoCSSParser::parse(const IByteBuffer* css) {
  return parse(css->getAsString());
}

CartoCSSResult* CartoCSSParser::parse(const std::string& css) {
  CartoCSSParser parser(css);
  return parser.pvtParse();
}

CartoCSSResult* CartoCSSParser::pvtParse() {
  CartoCSSResult* result = document();

  if (_tokensCursor < _tokensSize) {
    result->addError(CartoCSSError("End of CartoCSS expected", _tokens[_tokensCursor]->_position));
  }

  for (int i = 0; i < _tokensSize; i++) {
    const CartoCSSToken* token = _tokens[i];
    delete token;
  }

  return result;
}

bool CartoCSSParser::lookAhead(const std::vector<CartoCSSTokenType>& expectedTokensType,
                               int from,
                               int to) const {
  const int expectedTokensTypeSize = expectedTokensType.size();
  const int lastCursor = from + expectedTokensTypeSize;
  if (lastCursor > to) {
    return false;
  }

  for (int i = 0; i < expectedTokensTypeSize; i++) {
    const CartoCSSTokenType expectedTokenType = expectedTokensType[i];
    const CartoCSSToken* token = _tokens[from + i];
    if (token->_type != expectedTokenType) {
      return false;
    }
  }

  return true;
}

//bool CartoCSSParser::lookAhead(const std::vector<CartoCSSTokenType>& expectedTokensType) const {
//  const int expectedTokensTypeSize = expectedTokensType.size();
//  const int lastCursor = _tokensCursor + expectedTokensTypeSize;
//  if (lastCursor > _tokensSize) {
//    return false;
//  }
//
//  for (int i = 0; i < expectedTokensTypeSize; i++) {
//    const CartoCSSTokenType expectedTokenType = expectedTokensType[i];
//    const CartoCSSToken* token = _tokens[_tokensCursor + i];
//    if (token->_type != expectedTokenType) {
//      return false;
//    }
//  }
//
//  return true;
//}

bool CartoCSSParser::parseVariableDeclaration() {
  if ( !lookAhead(_variableDefinitionTokensType) ) {
    return false;
  }

  const std::string variableName  = ((const StringCartoCSSToken*) _tokens[_tokensCursor])->str();

  if (variableName[0] != '@') {
    return false;
  }

  const std::string variableValue = ((const StringCartoCSSToken*) _tokens[_tokensCursor + 2])->str();

  //    result->addVariableDefinition(variableName, variableValue);
  ILogger::instance()->logInfo("Found variable \"%s\"=\"%s\"",
                               variableName.c_str(),
                               variableValue.c_str());

  _tokensCursor += _variableDefinitionTokensType.size();
  return true;
}

int CartoCSSParser::lookAheadManyOf(const CartoCSSTokenType alternative1,
                                    const CartoCSSTokenType alternative2) const {
  if (_tokensCursor < _tokensSize-1) {

    const CartoCSSTokenType firstType = _tokens[_tokensCursor]->_type;
    if ((firstType != alternative1) &&
        (firstType != alternative2)) {
      return -1;
    }

    for (int i = _tokensCursor+1; i < _tokensSize; i++) {
      const CartoCSSTokenType type = _tokens[i]->_type;
      if ((type != alternative1) &&
          (type != alternative2)) {
        return i-1;
      }
    }
  }

  return -1;
}

int CartoCSSParser::lookAheadBalancedBraces(int cursor) const {
  if (cursor < _tokensSize) {
    if (_tokens[cursor]->_type == OPEN_BRACE) {
      int openedCount = 1;
      for (int i = cursor+1; i < _tokensSize; i++) {
        const CartoCSSTokenType type = _tokens[i]->_type;
        if (type == OPEN_BRACE) {
          openedCount++;
        }
        else if (type == CLOSE_BRACE) {
          openedCount--;
          if (openedCount <= 0) {
            return i;
          }
        }
      }
    }
  }
  return -1;
}

bool CartoCSSParser::parseSymbolizerBlock() {
  const int lasSelectorCursor = lookAheadManyOf(STRING, EXPRESION);
  if (lasSelectorCursor < 0) {
    return false;
  }

  const int lastCloseBracePosition = lookAheadBalancedBraces(lasSelectorCursor + 1);
  if (lastCloseBracePosition <= 0) {
    return false;
  }

  for (int i = _tokensCursor; i <= lasSelectorCursor; i++) {
    const CartoCSSToken* token = _tokens[i];
    const CartoCSSTokenType type = token->_type;
    switch (type) {
      case STRING: {
        const StringCartoCSSToken* stringToken = (const StringCartoCSSToken*) token;
        ILogger::instance()->logInfo("\"%s\"", stringToken->str().c_str());
        break;
      }

      case EXPRESION: {
        const ExpressionCartoCSSToken* expressionToken = (const ExpressionCartoCSSToken*) token;
        ILogger::instance()->logInfo("[%s]", expressionToken->_source.c_str());
        break;
      }

      default:
        ILogger::instance()->logError("CartoCSSParser: Logic error (1)");
        return false;
    }
  }

//  for (int i = lasSelectorCursor+2; i < lastCloseBracePosition; i++) {
//    ILogger::instance()->logInfo("    %s", _tokens[i]->description().c_str());
//  }

  int cursor = lasSelectorCursor+2;
  while (cursor < lastCloseBracePosition) {
    if (lookAhead(_variableDefinitionTokensType, cursor, lastCloseBracePosition)) {
      const std::string variableName  = ((const StringCartoCSSToken*) _tokens[cursor])->str();
      const std::string variableValue = ((const StringCartoCSSToken*) _tokens[cursor + 2])->str();

      ILogger::instance()->logInfo("    %s=%s", variableName.c_str(), variableValue.c_str());

      cursor += _variableDefinitionTokensType.size();
    }
    else {
      ILogger::instance()->logInfo("****%s", _tokens[cursor]->description().c_str());
      cursor++;
    }
  }

  _tokensCursor = lastCloseBracePosition+1;

  return true;
}


CartoCSSResult* CartoCSSParser::document() {
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


  CartoCSSResult* result = new CartoCSSResult();

  while (_tokensCursor < _tokensSize) {

    if (!parseVariableDeclaration() &&
        !parseSymbolizerBlock()) {
      result->addError(CartoCSSError("Sintax error", _tokens[_tokensCursor]->_position));
      break;
    }
  }
  
  return result;
}
