package org.glob3.mobile.generated; 
//
//  Layer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public abstract class Layer
{


  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean fullContains(const Sector& s) const = 0;
  public abstract boolean fullContains(Sector s);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<Petition*> getTilePetitions(const IFactory& factory, const Tile& tile, int width, int height) const = 0;
  public abstract java.util.ArrayList<Petition> getTilePetitions(IFactory factory, Tile tile, int width, int height);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isAvailable(const RenderContext* rc, const Tile& tile) const = 0;
  public abstract boolean isAvailable(RenderContext rc, Tile tile);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isTransparent() const = 0;
  public abstract boolean isTransparent();

}