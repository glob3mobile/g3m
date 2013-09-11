package org.glob3.mobile.generated; 
//
//  PlanetTileTessellator.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  PlanetTileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



//class IShortBuffer;
//class Sector;
//class FloatBufferBuilderFromGeodetic;
//class ShortBufferBuilder;


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