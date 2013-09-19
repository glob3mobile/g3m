//
//  CartoCSSTokens.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/17/13.
//
//

#ifndef __G3MiOSSDK__CartoCSSTokens__
#define __G3MiOSSDK__CartoCSSTokens__

#include <string>


enum CartoCSSTokenType {
  ERROR,
  STRING,
  OPEN_BRACE,    //  {
  CLOSE_BRACE,   //  }
  EXPRESION,     //  [ source ]
//  AT,            //  @
  COLON,         //  :
  DOUBLE_COLON,  //  ::
  SEMICOLON,     //  ;
};


class CartoCSSToken {
protected:

  CartoCSSToken(CartoCSSTokenType type,
                const int position) :
  _type(type),
  _position(position)
  {
  }

public:
  const CartoCSSTokenType _type;
  const int _position;

  virtual ~CartoCSSToken() {
  }

  virtual const std::string description() const = 0;
};


class ErrorCartoCSSToken : public CartoCSSToken {
public:
  const std::string _description;

  ErrorCartoCSSToken(const std::string& description,
                     int position) :
  CartoCSSToken(ERROR, position),
  _description(description)
  {
  }

  const std::string description() const;
};


class OpenBraceCartoCSSToken : public CartoCSSToken {
public:
  OpenBraceCartoCSSToken(const int position) : CartoCSSToken(OPEN_BRACE, position) { }

  const std::string description() const { return "[OpenBrace]"; }
};


class CloseBraceCartoCSSToken : public CartoCSSToken {
public:
  CloseBraceCartoCSSToken(const int position) : CartoCSSToken(CLOSE_BRACE, position) { }

  const std::string description() const { return "[CloseBrace]"; }
};


//class AtCartoCSSToken : public CartoCSSToken {
//public:
//  AtCartoCSSToken(const int position) : CartoCSSToken(AT, position) { }
//
//  const std::string description() const { return "[At]"; }
//};


class ColonCartoCSSToken : public CartoCSSToken {
public:
  ColonCartoCSSToken(const int position) : CartoCSSToken(COLON, position) { }

  const std::string description() const { return "[Colon]"; }
};

class DoubleColonCartoCSSToken : public CartoCSSToken {
public:
  DoubleColonCartoCSSToken(const int position) : CartoCSSToken(DOUBLE_COLON, position) { }

  const std::string description() const { return "[DoubleColon]"; }
};



class SemicolonCartoCSSToken : public CartoCSSToken {
public:
  SemicolonCartoCSSToken(const int position) : CartoCSSToken(SEMICOLON, position) { }

  const std::string description() const { return "[Semicolon]"; }
};


class ExpressionCartoCSSToken : public CartoCSSToken {
public:
  const std::string _source;

  ExpressionCartoCSSToken(const std::string& source,
                          const int position) :
  CartoCSSToken(EXPRESION, position),
  _source(source)
  {
  }

  const std::string description() const;
  
};


class StringCartoCSSToken : public CartoCSSToken {
public:
  const std::string _str;

  StringCartoCSSToken(const std::string& str,
                      const int position) :
  CartoCSSToken(STRING, position),
  _str(str)
  {
  }

  const std::string description() const;
};


#endif
