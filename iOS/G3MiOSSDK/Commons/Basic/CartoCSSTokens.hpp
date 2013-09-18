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

enum CartoCSSTokenKind {
  ERROR,
  OPEN_BRACE,           // {
  CLOSE_BRACE,          // }
  CONDITION_SELECTOR,
  VARIABLE,
  AT,                   // @
  COLON,                // :
  SEMICOLON,            // ;
  STRING,
};


class CartoCSSToken {
protected:

  CartoCSSToken(CartoCSSTokenKind kind) :
  _kind(kind)
  {
  }

public:
  const CartoCSSTokenKind _kind;

  virtual ~CartoCSSToken() {
  }

  virtual const std::string description() const = 0;
};


class ErrorCartoCSSToken : public CartoCSSToken {
public:
  const std::string _message;
  const int         _position;

  ErrorCartoCSSToken(const std::string& message,
                     int position) :
  CartoCSSToken(ERROR),
  _message(message),
  _position(position)
  {
  }

  const std::string description() const;
};


class OpenBraceCartoCSSToken : public CartoCSSToken {
public:
  OpenBraceCartoCSSToken() : CartoCSSToken(OPEN_BRACE) { }

  const std::string description() const { return "[OpenBrace]"; }
};


class CloseBraceCartoCSSToken : public CartoCSSToken {
public:
  CloseBraceCartoCSSToken() : CartoCSSToken(CLOSE_BRACE) { }

  const std::string description() const { return "[CloseBrace]"; }
};


class AtCartoCSSToken : public CartoCSSToken {
public:
  AtCartoCSSToken() : CartoCSSToken(AT) { }

  const std::string description() const { return "[At]"; }
};


class ColonCartoCSSToken : public CartoCSSToken {
public:
  ColonCartoCSSToken() : CartoCSSToken(COLON) { }

  const std::string description() const { return "[Colon]"; }
};


class SemicolonCartoCSSToken : public CartoCSSToken {
public:
  SemicolonCartoCSSToken() : CartoCSSToken(SEMICOLON) { }

  const std::string description() const { return "[Semicolon]"; }
};


class ExpressionCartoCSSToken : public CartoCSSToken {
public:
  const std::string _source;

  ExpressionCartoCSSToken(const std::string& source) :
  CartoCSSToken(CONDITION_SELECTOR),
  _source(source)
  {
  }

  const std::string description() const;
  
};


class VariableCartoCSSToken : public CartoCSSToken {
public:
  const std::string _name;
  const std::string _value;

  VariableCartoCSSToken(const std::string& name,
                        const std::string& value) :
  CartoCSSToken(VARIABLE),
  _name(name),
  _value(value)
  {

  }

  const std::string description() const;
};

class StringCartoCSSToken : public CartoCSSToken {
public:
  const std::string _str;

  StringCartoCSSToken(const std::string& str) :
  CartoCSSToken(STRING),
  _str(str)
  {

  }

  const std::string description() const;
};


#endif
