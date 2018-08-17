package org.glob3.mobile.generated;import java.util.*;

//
//  LeveledTexturedMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/08/12.
//
//

//
//  LeveledTexturedMesh.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/08/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureIDReference;

public abstract class LazyTextureMappingInitializer
{
  public void dispose()
  {
  }

  public abstract void initialize();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const Vector2F getScale() const = 0;
  public abstract Vector2F getScale();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const Vector2F getTranslation() const = 0;
  public abstract Vector2F getTranslation();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IFloatBuffer* createTextCoords() const = 0;
  public abstract IFloatBuffer createTextCoords();
}
