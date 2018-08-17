package org.glob3.mobile.generated;import java.util.*;

//
//  MeshBoundingVolumeTileVisibilityTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

//
//  MeshBoundingVolumeTileVisibilityTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//




public class MeshBoundingVolumeTileVisibilityTester extends TileVisibilityTester
{

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isVisible(const G3MRenderContext* rc, const PlanetRenderContext* prc, Tile* tile) const
  public final boolean isVisible(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
	final Mesh mesh = tile.getTessellatorMesh(rc, prc);
	if (mesh == null)
	{
	  return false;
	}
  
	return mesh.getBoundingVolume().touchesFrustum(prc._frustumInModelCoordinates);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void onTileHasChangedMesh(const Tile* tile) const
  public final void onTileHasChangedMesh(Tile tile)
  {
  
  }

  public final void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void renderStarted() const
  public final void renderStarted()
  {
  
  }

}
