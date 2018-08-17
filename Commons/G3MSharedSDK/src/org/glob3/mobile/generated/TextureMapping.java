package org.glob3.mobile.generated;//
//  TextureMapping.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//

//
//  TextureMapping.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLState;

public abstract class TextureMapping
{

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void modifyGLState(GLState& state) const = 0;
  public abstract void modifyGLState(tangible.RefObject<GLState> state);
}
