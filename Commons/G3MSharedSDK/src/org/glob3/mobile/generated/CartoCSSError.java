package org.glob3.mobile.generated; 
//
//  CartoCSSParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/16/13.
//
//

//
//  CartoCSSParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/16/13.
//
//


//class IByteBuffer;
//class IStringUtils;
//class CartoCSSLexer;


public class CartoCSSError
{
  private String _description;
  private int _position;

  public CartoCSSError(String description, int position)
  {
     _description = description;
     _position = position;
  }

  public void dispose()
  {
  }

  public final String getDescription()
  {
    return _description;
  }

  public final int getPosition()
  {
    return _position;
  }
}