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



//class TextureIDReference;

public abstract class LazyTextureMappingInitializer
{
  public void dispose()
  {
  }

  public abstract void initialize();

  public abstract Vector2F getScale();

  public abstract Vector2F getTranslation();

  public abstract IFloatBuffer createTextCoords();
}