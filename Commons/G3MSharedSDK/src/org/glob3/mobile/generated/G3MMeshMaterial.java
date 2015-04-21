package org.glob3.mobile.generated; 
//
//  G3MMeshMaterial.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/14.
//
//

//
//  G3MMeshMaterial.hp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/29/14.
//
//


//class Color;
//class URL;

public class G3MMeshMaterial
{
  public final String _id;
  public final Color _color;
  public final URL _textureURL;

  public G3MMeshMaterial(String id, Color color, URL textureURL)
  {
     _id = id;
     _color = color;
     _textureURL = textureURL;

  }

  public void dispose()
  {
    if (_color != null)
       _color.dispose();
    if (_textureURL != null)
       _textureURL.dispose();
  }
}