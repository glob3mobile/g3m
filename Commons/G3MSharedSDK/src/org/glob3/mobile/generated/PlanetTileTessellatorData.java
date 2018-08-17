package org.glob3.mobile.generated;import java.util.*;

//
//  PlanetTileTessellator.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//

//
//  PlanetTileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IShortBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Sector;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class FloatBufferBuilderFromGeodetic;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ShortBufferBuilder;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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
