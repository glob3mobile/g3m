package org.glob3.mobile.generated;//
//  PlanetRenderContext.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/4/16.
//
//

//
//  PlanetRenderContext.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/4/16.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileLODTester;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileVisibilityTester;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Frustum;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerTilesRenderParameters;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileTexturizer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TilesRenderParameters;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ITimer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ElevationDataProvider;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileTessellator;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerSet;

public class PlanetRenderContext
{
  public TileLODTester _tileLODTester;
  public TileVisibilityTester _tileVisibilityTester;
  public float _verticalExaggeration;
  public TileTexturizer _texturizer;
  public ITimer _lastSplitTimer;
  public ElevationDataProvider _elevationDataProvider;
  public TileTessellator _tessellator;
  public LayerSet _layerSet;
  public long _tileDownloadPriority;
  public double _texWidthSquared;
  public double _texHeightSquared;
  public long _nowInMS;
  public boolean _renderTileMeshes;
  public boolean _logTilesPetitions;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  public final Frustum _frustumInModelCoordinates;
  public final LayerTilesRenderParameters _layerTilesRenderParameters;
  public final TilesRenderParameters _tilesRenderParameters;
//#else
  public Frustum _frustumInModelCoordinates;
  public LayerTilesRenderParameters _layerTilesRenderParameters;
  public TilesRenderParameters _tilesRenderParameters;
//#endif

  public void dispose()
  {
  }

}
