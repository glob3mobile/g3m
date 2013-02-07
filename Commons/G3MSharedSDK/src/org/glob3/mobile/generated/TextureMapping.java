package org.glob3.mobile.generated; 
//
//  TextureMapping.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  TextureMapping.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


///#include <vector>

//class IGLTextureId;

//class G3MRenderContext;
//class IFloatBuffer;

public abstract class TextureMapping
{

  public void dispose()
  {
  }

  public abstract void bind(G3MRenderContext rc);

  public abstract boolean isTransparent(G3MRenderContext rc);

}