package org.glob3.mobile.generated;
//
//  PlanetTileTessellator.cpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//

//
//  PlanetTileTessellator.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//




//class IShortBuffer;
//class Sector;
//class FloatBufferBuilderFromGeodetic;
//class ShortBufferBuilder;
//class Vector2S;


public class PlanetTileTessellatorData
{
  public FloatBufferBuilderFromCartesian2D _textCoords;
  public PlanetTileTessellatorData(FloatBufferBuilderFromCartesian2D textCoords)
  {
     _textCoords = textCoords;
  }

  public void dispose()
  {
    if (_textCoords != null)
       _textCoords.dispose();
  }
}