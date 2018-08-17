package org.glob3.mobile.generated;//
//  TileVisibilityTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

//
//  TileVisibilityTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class PlanetRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerTilesRenderParameters;

public abstract class TileVisibilityTester
{

  public TileVisibilityTester()
  {
  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isVisible(const G3MRenderContext* rc, const PlanetRenderContext* prc, Tile* tile) const = 0;
  public abstract boolean isVisible(G3MRenderContext rc, PlanetRenderContext prc, Tile tile);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void onTileHasChangedMesh(const Tile* tile) const = 0;
  public abstract void onTileHasChangedMesh(Tile tile);

  public abstract void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void renderStarted() const = 0;
  public abstract void renderStarted();

}
