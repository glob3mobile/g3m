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


//class G3MRenderContext;
<<<<<<< HEAD
//class GLState;
//class GEOSymbolizer;
//class GPUProgramState;
=======
//class GEOSymbolizationContext;
>>>>>>> webgl-port

public abstract class GEOObject
{
  public void dispose()
  {

  }

<<<<<<< HEAD
  public void initialize(G3MContext context)
  {

  }

  public boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public abstract void render(G3MRenderContext rc, GLState parentState, GPUProgramState parentProgramState, GEOSymbolizer symbolizer);
=======
  public abstract void symbolize(G3MRenderContext rc, GEOSymbolizationContext sc);
>>>>>>> webgl-port

}