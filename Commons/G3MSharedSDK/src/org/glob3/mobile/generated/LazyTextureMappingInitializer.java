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







public abstract class LazyTextureMappingInitializer extends Disposable
{
  public void dispose()
  {
  super.dispose();

  }

  public abstract void initialize();

  public abstract MutableVector2D getScale();

  public abstract MutableVector2D getTranslation();

  public abstract IFloatBuffer createTextCoords();
}