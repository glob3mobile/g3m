package org.glob3.mobile.generated; 
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





public abstract class LazyTextureMappingInitializer
{
  public void dispose()
  {
  }

  public abstract void initialize();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const MutableVector2D getScale() const = 0;
  public abstract MutableVector2D getScale();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const MutableVector2D getTranslation() const = 0;
  public abstract MutableVector2D getTranslation();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IFloatBuffer* getTexCoords() const = 0;
  public abstract IFloatBuffer getTexCoords();
}