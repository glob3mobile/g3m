package org.glob3.mobile.generated; 
//
//  GEOObject.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

//
//  GEOObject.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLState;

public abstract class GEOObject
{
  public void dispose()
  {

  }

  public void initialize(G3MContext context)
  {

  }

  public boolean isReadyToRender(G3MRenderContext rc)
  {
	return true;
  }

  public abstract void render(G3MRenderContext rc, GLState parentState);

}