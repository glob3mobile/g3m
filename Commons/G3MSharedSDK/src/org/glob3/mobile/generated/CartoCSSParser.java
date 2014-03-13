package org.glob3.mobile.generated; 
public class CartoCSSParser
{
  private java.util.ArrayList<CartoCSSTokenType> _variableDefinitionTokensType = new java.util.ArrayList<CartoCSSTokenType>();

  private java.util.ArrayList<CartoCSSToken> _tokens = new java.util.ArrayList<CartoCSSToken>();
  private final int _tokensSize;

  private int _tokensCursor;

  private CartoCSSParser(String source)
  {
     _tokens = CartoCSSLexer.tokenize(source);
     _tokensSize = _tokens.size();
     _tokensCursor = 0;
    _variableDefinitionTokensType.add(CartoCSSTokenType.STRING);
    _variableDefinitionTokensType.add(CartoCSSTokenType.COLON);
    _variableDefinitionTokensType.add(CartoCSSTokenType.STRING);
    _variableDefinitionTokensType.add(CartoCSSTokenType.SEMICOLON);
  }

  private CartoCSSResult pvtParse()
  {
    CartoCSSResult result = document();
  
    if (_tokensCursor < _tokensSize)
    {
      result.addError(new CartoCSSError("End of CartoCSS expected", _tokens.get(_tokensCursor)._position));
    }
  
    for (int i = 0; i < _tokensSize; i++)
    {
      final CartoCSSToken token = _tokens.get(i);
      if (token != null)
         token.dispose();
    }
  
    return result;
  }

  public void dispose()
  {
  }

  private boolean lookAhead(java.util.ArrayList<CartoCSSTokenType> expectedTokensType, int from, int to)
  {
    final int expectedTokensTypeSize = expectedTokensType.size();
    final int lastCursor = from + expectedTokensTypeSize;
    if (lastCursor > to)
    {
      return false;
    }
  
    for (int i = 0; i < expectedTokensTypeSize; i++)
    {
      final CartoCSSTokenType expectedTokenType = expectedTokensType.get(i);
      final CartoCSSToken token = _tokens.get(from + i);
      if (token._type != expectedTokenType)
      {
        return false;
      }
    }
  
    return true;
  }

  private boolean lookAhead(java.util.ArrayList<CartoCSSTokenType> expectedTokensType)
  {
    return lookAhead(expectedTokensType, _tokensCursor, _tokensSize);
  }

  private int lookAheadManyOf(CartoCSSTokenType alternative1, CartoCSSTokenType alternative2, int from, int to)
  {
    if (from < to-1)
    {
      final CartoCSSTokenType firstType = _tokens.get(from)._type;
      if ((firstType != alternative1) && (firstType != alternative2))
      {
        return -1;
      }
  
      for (int i = from+1; i < to; i++)
      {
        final CartoCSSTokenType type = _tokens.get(i)._type;
        if ((type != alternative1) && (type != alternative2))
        {
          return i-1;
        }
      }
    }
  
    return -1;
  }
  private int lookAheadBalancedBraces(int from, int to)
  {
    if (from < to)
    {
      if (_tokens.get(from)._type == CartoCSSTokenType.OPEN_BRACE)
      {
        int openedCount = 1;
        for (int i = from+1; i < _tokensSize; i++)
        {
          final CartoCSSTokenType type = _tokens.get(i)._type;
          if (type == CartoCSSTokenType.OPEN_BRACE)
          {
            openedCount++;
          }
          else if (type == CartoCSSTokenType.CLOSE_BRACE)
          {
            openedCount--;
            if (openedCount <= 0)
            {
              return i;
            }
          }
        }
      }
    }
    return -1;
  }

  private boolean parseVariableDeclaration(CartoCSSSymbolizer currentSymbolizer)
  {
    if (!lookAhead(_variableDefinitionTokensType))
    {
      return false;
    }
  
    final String variableName = ((StringCartoCSSToken) _tokens.get(_tokensCursor)).str();
  
    if (variableName.charAt(0) != '@')
    {
      return false;
    }
  
    final String variableValue = ((StringCartoCSSToken) _tokens.get(_tokensCursor + 2)).str();
  
    currentSymbolizer.setVariableDeclaration(variableName, variableValue);
  
  //  ILogger::instance()->logInfo("Found variable \"%s\"=\"%s\"",
  //                               variableName.c_str(),
  //                               variableValue.c_str());
  
    _tokensCursor += _variableDefinitionTokensType.size();
    return true;
  }
  private boolean parseSymbolizerBlock(CartoCSSSymbolizer currentSymbolizer)
  {
    final int cursor = parseSymbolizerBlock(currentSymbolizer, _tokensCursor, _tokensSize);
    if (cursor < 0)
    {
      return false;
    }
    _tokensCursor = cursor;
    return true;
  }
  private int parseSymbolizerBlock(CartoCSSSymbolizer currentSymbolizer, int from, int to)
  {
    final int lasSelectorCursor = lookAheadManyOf(CartoCSSTokenType.STRING, CartoCSSTokenType.EXPRESION, from, to);
    if (lasSelectorCursor < 0)
    {
      return -1;
    }
  
    final int lastCloseBracePosition = lookAheadBalancedBraces(lasSelectorCursor + 1, to);
    if (lastCloseBracePosition <= 0)
    {
      return -1;
    }
  
    java.util.ArrayList<String> selectors = new java.util.ArrayList<String>();
    for (int i = from; i <= lasSelectorCursor; i++)
    {
      final CartoCSSToken token = _tokens.get(i);
      final CartoCSSTokenType type = token._type;
      switch (type)
      {
        case STRING:
        {
          final StringCartoCSSToken stringToken = (StringCartoCSSToken) token;
          selectors.add(stringToken.str());
          //ILogger::instance()->logInfo("\"%s\"", stringToken->str().c_str());
  ///#warning Diego at work!
          break;
        }
  
        case EXPRESION:
        {
          final ExpressionCartoCSSToken expressionToken = (ExpressionCartoCSSToken) token;
          selectors.add("[" + expressionToken._source + "]");
          //ILogger::instance()->logInfo("[%s]", expressionToken->_source.c_str());
  ///#warning Diego at work!
          break;
        }
  
        default:
        {
          ILogger.instance().logError("CartoCSSParser: Logic error (1)");
          return -1;
        }
      }
    }
  
    CartoCSSSymbolizer newSymbolizer = new CartoCSSSymbolizer(selectors);
    currentSymbolizer.addSymbolizer(newSymbolizer);
  
    int newCursor;
    int cursor = lasSelectorCursor+2;
    while (cursor < lastCloseBracePosition)
    {
      if (lookAhead(_variableDefinitionTokensType, cursor, lastCloseBracePosition))
      {
        final String variableName = ((StringCartoCSSToken) _tokens.get(cursor)).str();
        final String variableValue = ((StringCartoCSSToken) _tokens.get(cursor + 2)).str();
  
        newSymbolizer.setProperty(variableName, variableValue);
        //ILogger::instance()->logInfo("    %s=%s", variableName.c_str(), variableValue.c_str());
  
        cursor += _variableDefinitionTokensType.size();
        continue;
      }
  
      if ((newCursor = parseSymbolizerBlock(newSymbolizer, cursor, lastCloseBracePosition)) >= 0)
      {
        cursor = newCursor;
        continue;
      }
  
      ILogger.instance().logInfo("****%s", _tokens.get(cursor).description());
  ///#warning Diego at work!
      cursor++;
    }
  
    return lastCloseBracePosition+1;
  }

  private CartoCSSResult document()
  {
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
  
    CartoCSSSymbolizer rootSymbolizer = new CartoCSSSymbolizer();
    CartoCSSResult result = new CartoCSSResult(rootSymbolizer);
  
    while (_tokensCursor < _tokensSize)
    {
      if (!parseVariableDeclaration(rootSymbolizer) && !parseSymbolizerBlock(rootSymbolizer))
      {
        result.addError(new CartoCSSError("Sintax error", _tokens.get(_tokensCursor)._position));
        break;
      }
    }
  
    return result;
  }

  public static CartoCSSResult parse(String css)
  {
    CartoCSSParser parser = new CartoCSSParser(css);
    return parser.pvtParse();
  }

  public static CartoCSSResult parse(IByteBuffer css)
  {
    return parse(css.getAsString());
  }
}