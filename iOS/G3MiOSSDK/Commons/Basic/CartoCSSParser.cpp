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

bool CartoCSSParser::parseVariableDeclaration(CartoCSSSymbolizer* currentSymbolizer) {
  if ( !lookAhead(_variableDefinitionTokensType) ) {
    return false;
  }

  const std::string variableName  = ((const StringCartoCSSToken*) _tokens[_tokensCursor])->str();

  if (variableName[0] != '@') {
    return false;
  }

  const std::string variableValue = ((const StringCartoCSSToken*) _tokens[_tokensCursor + 2])->str();

  currentSymbolizer->setVariableDeclaration(variableName, variableValue);

//  ILogger::instance()->logInfo("Found variable \"%s\"=\"%s\"",
//                               variableName.c_str(),
//                               variableValue.c_str());

  _tokensCursor += _variableDefinitionTokensType.size();
  return true;
}

int CartoCSSParser::lookAheadManyOf(const CartoCSSTokenType alternative1,
                                    const CartoCSSTokenType alternative2,
                                    int from,
                                    int to) const {
  if (from < to-1) {
    const CartoCSSTokenType firstType = _tokens[from]->_type;
    if ((firstType != alternative1) &&
        (firstType != alternative2)) {
      return -1;
    }

    for (int i = from+1; i < to; i++) {
      const CartoCSSTokenType type = _tokens[i]->_type;
      if ((type != alternative1) &&
          (type != alternative2)) {
        return i-1;
      }
    }
  }

  return -1;
}

int CartoCSSParser::lookAheadBalancedBraces(int from,
                                            int to) const {
  if (from < to) {
    if (_tokens[from]->_type == OPEN_BRACE) {
      int openedCount = 1;
      for (int i = from+1; i < _tokensSize; i++) {
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

int CartoCSSParser::parseSymbolizerBlock(CartoCSSSymbolizer* currentSymbolizer,
                                         int from,
                                         int to) const {
  const int lasSelectorCursor = lookAheadManyOf(STRING, EXPRESION, from, to);
  if (lasSelectorCursor < 0) {
    return -1;
  }

  const int lastCloseBracePosition = lookAheadBalancedBraces(lasSelectorCursor + 1, to);
  if (lastCloseBracePosition <= 0) {
    return -1;
  }

  std::vector<std::string> selectors;
  for (int i = from; i <= lasSelectorCursor; i++) {
    const CartoCSSToken* token = _tokens[i];
    const CartoCSSTokenType type = token->_type;
    switch (type) {
      case STRING: {
        const StringCartoCSSToken* stringToken = (const StringCartoCSSToken*) token;
        selectors.push_back( stringToken->str() );
        //ILogger::instance()->logInfo("\"%s\"", stringToken->str().c_str());
//#warning Diego at work!
        break;
      }
        
      case EXPRESION: {
        const ExpressionCartoCSSToken* expressionToken = (const ExpressionCartoCSSToken*) token;
        selectors.push_back( "[" + expressionToken->_source + "]");
        //ILogger::instance()->logInfo("[%s]", expressionToken->_source.c_str());
//#warning Diego at work!
        break;
      }

      default: {
        ILogger::instance()->logError("CartoCSSParser: Logic error (1)");
        return -1;
      }
    }
  }

  CartoCSSSymbolizer* newSymbolizer = new CartoCSSSymbolizer(selectors);
  currentSymbolizer->addSymbolizer(newSymbolizer);

  int newCursor;
  int cursor = lasSelectorCursor+2;
  while (cursor < lastCloseBracePosition) {
    if (lookAhead(_variableDefinitionTokensType, cursor, lastCloseBracePosition)) {
      const std::string variableName  = ((const StringCartoCSSToken*) _tokens[cursor])->str();
      const std::string variableValue = ((const StringCartoCSSToken*) _tokens[cursor + 2])->str();

      newSymbolizer->setProperty(variableName, variableValue);
      //ILogger::instance()->logInfo("    %s=%s", variableName.c_str(), variableValue.c_str());

      cursor += _variableDefinitionTokensType.size();
      continue;
    }

    if ( (newCursor = parseSymbolizerBlock(newSymbolizer, cursor, lastCloseBracePosition)) >= 0 ) {
      cursor = newCursor;
      continue;
    }

    ILogger::instance()->logInfo("****%s", _tokens[cursor]->description().c_str());
//#warning Diego at work!
    cursor++;
  }

  return lastCloseBracePosition+1;
}

bool CartoCSSParser::parseSymbolizerBlock(CartoCSSSymbolizer* currentSymbolizer) {
  const int cursor = parseSymbolizerBlock(currentSymbolizer, _tokensCursor, _tokensSize);
  if (cursor < 0) {
    return false;
  }
  _tokensCursor = cursor;
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

  CartoCSSSymbolizer* rootSymbolizer = new CartoCSSSymbolizer();
  CartoCSSResult* result = new CartoCSSResult(rootSymbolizer);

  while (_tokensCursor < _tokensSize) {
    if (!parseVariableDeclaration(rootSymbolizer) &&
        !parseSymbolizerBlock(rootSymbolizer)) {
      result->addError(CartoCSSError("Sintax error", _tokens[_tokensCursor]->_position));
      break;
    }
  }

  return result;
}
