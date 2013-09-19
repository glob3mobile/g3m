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
//_result(new CartoCSSResult()),
_tokensCursor(0)
{
  _variableDefinitionTokensKind.push_back(AT);
  _variableDefinitionTokensKind.push_back(STRING);
  _variableDefinitionTokensKind.push_back(COLON);
  _variableDefinitionTokensKind.push_back(STRING);
  _variableDefinitionTokensKind.push_back(SEMICOLON);
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

bool CartoCSSParser::lookAhead(const std::vector<CartoCSSTokenKind>& expectedTokensKind) const {
  const int expectedTokensKindSize = expectedTokensKind.size();
  const int lastCursor = _tokensCursor + expectedTokensKindSize;
  if (lastCursor > _tokensSize) {
    return false;
  }

  for (int i = 0; i < expectedTokensKindSize; i++) {
    const CartoCSSTokenKind expectedTokenKind = expectedTokensKind[i];
    const CartoCSSToken* token = _tokens[_tokensCursor + i];
    if (token->_kind != expectedTokenKind) {
      return false;
    }
  }

  return true;
}

//int CartoCSSParser::lookAheadWithBalancedBraces(const CartoCSSTokenKind expectedTokenKind) const {
//  if ((_tokensCursor < _tokensSize-1) &&
//      (_tokens[_tokensCursor]->_kind == expectedTokenKind)) {
//
//    if (_tokens[_tokensCursor + 1]->_kind == OPEN_BRACE) {
//      int openedCount = 1;
//      for (int i = _tokensCursor+2; i < _tokensSize; i++) {
//        const CartoCSSTokenKind kind = _tokens[i]->_kind;
//        if (kind == OPEN_BRACE) {
//          openedCount++;
//        }
//        else if (kind == CLOSE_BRACE) {
//          openedCount--;
//          if (openedCount <= 0) {
//            return i;
//          }
//        }
//      }
//    }
//  }
//  return -1;
//}

bool CartoCSSParser::parseVariableDeclaration() {
  if ( !lookAhead(_variableDefinitionTokensKind) ) {
    return false;
  }

  const std::string variableName  = ((const StringCartoCSSToken*) _tokens[_tokensCursor + 1])->_str;
  const std::string variableValue = ((const StringCartoCSSToken*) _tokens[_tokensCursor + 3])->_str;

  //    result->addVariableDefinition(variableName, variableValue);
  ILogger::instance()->logInfo("Found variable \"%s\"=\"%s\"",
                               variableName.c_str(),
                               variableValue.c_str());

  _tokensCursor += _variableDefinitionTokensKind.size();
  return true;
}

int CartoCSSParser::lookAheadManyOf(const CartoCSSTokenKind alternative1,
                                    const CartoCSSTokenKind alternative2) const {
  if (_tokensCursor < _tokensSize-1) {

    const CartoCSSTokenKind firstKind = _tokens[_tokensCursor]->_kind;
    if ((firstKind != alternative1) &&
        (firstKind != alternative2)) {
      return -1;
    }

    for (int i = _tokensCursor+1; i < _tokensSize; i++) {
      const CartoCSSTokenKind kind = _tokens[i]->_kind;
      if ((kind != alternative1) &&
          (kind != alternative2)) {
        return i-1;
      }
    }
  }

  return -1;
}

int CartoCSSParser::lookAheadBalancedBraces(int cursor) const {
  if (cursor < _tokensSize) {
    if (_tokens[cursor]->_kind == OPEN_BRACE) {
      int openedCount = 1;
      for (int i = cursor+1; i < _tokensSize; i++) {
        const CartoCSSTokenKind kind = _tokens[i]->_kind;
        if (kind == OPEN_BRACE) {
          openedCount++;
        }
        else if (kind == CLOSE_BRACE) {
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
    const CartoCSSTokenKind kind = token->_kind;
    switch (kind) {
      case STRING: {
        const StringCartoCSSToken* stringToken = (const StringCartoCSSToken*) token;
        ILogger::instance()->logInfo("\"%s\"", stringToken->_str.c_str());
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

  for (int i = lasSelectorCursor+2; i < lastCloseBracePosition; i++) {
    ILogger::instance()->logInfo("    %s", _tokens[i]->description().c_str());
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

//    if (parseVariableDeclaration()) {
//      continue;
//    }
//
//    if (parseSymbolizerBlock()) {
//      continue;
//    }
//
//    result->addError(CartoCSSError("Sintax error", _tokens[_tokensCursor]->_position));
//    break;
  }
  
  return result;
}
