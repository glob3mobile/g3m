package org.glob3.mobile.generated; 
//
//  CartoCSSLexer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/18/13.
//
//

//
//  CartoCSSLexer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/18/13.
//
//


//class IStringUtils;
//class CartoCSSToken;


public class CartoCSSLexer
{
  private final String _source;
  private final int _sourceSize;
  private int _cursor;

  private CartoCSSToken _lastToken;
  //  bool           _returnPreviousToken;

  private final IStringUtils _su;

  private boolean skipComments()
  {
    if (_cursor < _sourceSize-1)
    {
      final char c = _source.charAt(_cursor);
      final char nextC = _source.charAt(_cursor+1);
      if (c == '/')
      {
        if (nextC == '/')
        {
          final int eolPosition = _su.indexOf(_source, "\n", _cursor+2);
          _cursor = (eolPosition < 0) ? _sourceSize : eolPosition + 1;
          return true;
        }
        else if (nextC == '*')
        {
          final int eocPosition = _su.indexOf(_source, "*/", _cursor+2);
          _cursor = (eocPosition < 0) ? _sourceSize : eocPosition + 3;
          return true;
        }
      }
    }
    return false;
  }

  private boolean skipBlanks()
  {
    final int cursor = _su.indexOfFirstNonBlank(_source, _cursor);
    if (cursor < 0)
    {
      _cursor = _sourceSize;
      return false;
    }
    final boolean changedCursor = (cursor != _cursor);
    if (changedCursor)
    {
      _cursor = cursor;
    }
    return changedCursor;
  }

  private void skipCommentsAndBlanks()
  {
    boolean skipedBlanks;
    boolean skipedCommment;
    do
    {
      skipedBlanks = skipBlanks();
      skipedCommment = skipComments();
    }
    while (skipedBlanks || skipedCommment);
  }

  private CartoCSSLexer(String source)
  //  _previousToken(NULL),
  //  _returnPreviousToken(false),
  {
     _source = source;
     _sourceSize = source.length();
     _cursor = 0;
     _su = IStringUtils.instance();
     _lastToken = null;
  }

  //  void revert() {
  //    _returnPreviousToken = true;
  //  }

  private CartoCSSToken getNextToken()
  {
    //    if (_returnPreviousToken) {
    //      _returnPreviousToken = false;
    //      return _previousToken;
    //    }
  
    skipCommentsAndBlanks();
    if (_cursor >= _sourceSize)
    {
      return null;
    }
  
    CartoCSSToken token;
  
    final char c = _source.charAt(_cursor);
  
    switch (c)
    {
  
      case '{':
      {
        token = new OpenBraceCartoCSSToken(_cursor);
        _cursor++;
        break;
      }
  
      case '}':
      {
        token = new CloseBraceCartoCSSToken(_cursor);
        _cursor++;
        break;
      }
  
      case ':':
      {
        if ((_cursor + 1 < _sourceSize) && (_source.charAt(_cursor + 1) == ':'))
        {
          token = new StringCartoCSSToken("::", _cursor);
          _cursor += 2;
        }
        else
        {
          token = new ColonCartoCSSToken(_cursor);
          _cursor++;
        }
        break;
      }
  
      case ';':
      {
        token = new SemicolonCartoCSSToken(_cursor);
        _cursor++;
        break;
      }
  
      case '[':
      {
        final int closeBraquetPosition = _su.indexOf(_source, "]", _cursor+1);
        if (closeBraquetPosition < 0)
        {
          token = new ErrorCartoCSSToken("Unbalanced braquet", _cursor);
        }
        else
        {
          token = new ExpressionCartoCSSToken(_su.substring(_source, _cursor+1, closeBraquetPosition), _cursor);
          _cursor = closeBraquetPosition+1;
        }
        break;
      }
  
      default:
      {
        final int cursor = _su.indexOfFirstNonChar(_source, "{}:;[]\n\r", _cursor);
        if (cursor < 0)
        {
          token = new ErrorCartoCSSToken("Unknown token", _cursor);
        }
        else
        {
          final String str = _su.substring(_source, _cursor, cursor);
          if ((_lastToken != null) && (_lastToken._type == CartoCSSTokenType.STRING))
          {
            ((StringCartoCSSToken) _lastToken).appendString(str);
            token = new SkipCartoCSSToken(_cursor);
          }
          else
          {
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

  public static java.util.ArrayList<CartoCSSToken> tokenize(String source)
  {
    CartoCSSLexer lexer = new CartoCSSLexer(source);
  
    java.util.ArrayList<CartoCSSToken> result = new java.util.ArrayList<CartoCSSToken>();
  
    boolean finish = false;
    while (!finish)
    {
      CartoCSSToken token = lexer.getNextToken();
  
      if (token == null)
      {
        finish = true;
      }
      else
      {
        if (token._type == CartoCSSTokenType.ERROR)
        {
          finish = true;
        }
  
        if (token._type == CartoCSSTokenType.SKIP)
        {
          if (token != null)
             token.dispose();
        }
        else
        {
          result.add(token);
        }
      }
    }
  
    return result;
  }

}