package org.glob3.mobile.generated; 
//
//  TileRendererBuilder.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 22/11/12.
//
//

//
//  TileRendererBuilder.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 22/11/12.
//
//




public class TileRendererBuilder
{

  private TileTessellator _tileTessellator;
  private TileTexturizer _texturizer;
  private LayerSet _layerSet;
  private TilesRenderParameters _parameters;
  private boolean _showStatistics;
  private boolean _renderDebug;
  private boolean _useTilesSplitBudget;
  private boolean _forceTopLevelTilesRenderOnStart;
  private boolean _incrementalTileQuality;
  private java.util.ArrayList<VisibleSectorListener> _visibleSectorListeners = new java.util.ArrayList<VisibleSectorListener>();
  private java.util.ArrayList<Long> _stabilizationMilliSeconds = new java.util.ArrayList<Long>();

  private LayerSet createLayerSet()
  {
    LayerSet layerSet = new LayerSet();
  
    WMSLayer bing = LayerBuilder.createBingLayer(true);
    layerSet.addLayer(bing);
  
    return layerSet;
  }
  private TilesRenderParameters createTileRendererParameters()
  {
    return TilesRenderParameters.createDefault(_renderDebug, _useTilesSplitBudget, _forceTopLevelTilesRenderOnStart, _incrementalTileQuality);
  }
  private TileTessellator createTileTessellator()
  {
    return new EllipsoidalTileTessellator(_parameters._tileMeshResolution, true);
  }


  ///#include "WMSBillElevationDataProvider.hpp"
  
  public TileRendererBuilder()
  {
    _showStatistics = false;
    _renderDebug = false;
    _useTilesSplitBudget = true;
    _forceTopLevelTilesRenderOnStart = true;
    _incrementalTileQuality = false;
  
    _parameters = createTileRendererParameters();
    _layerSet = createLayerSet();
    _texturizer = new MultiLayerTileTexturizer();
    _tileTessellator = createTileTessellator();
  }
  public void dispose()
  {
  }
  public final TileRenderer create()
  {
    int __TODO_make_configurable;
  
    ElevationDataProvider elevationDataProvider = null;
  
  //  ElevationDataProvider* elevationDataProvider = new WMSBillElevationDataProvider();
  
  //  ElevationDataProvider* elevationDataProvider;
  //  elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///full-earth-2048x1024.bil", false),
  //                                                              Sector::fullSphere(),
  //                                                              Vector2I(2048, 1024),
  //                                                              0);
  
  //  elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///full-earth-4096x2048.bil", false),
  //                                                              Sector::fullSphere(),
  //                                                              Vector2I(4096, 2048),
  //                                                              0);
  
    int ___TODO_make_configurable;
    float verticalExaggeration = 10F;
  
    TileRenderer tileRenderer = new TileRenderer(_tileTessellator, elevationDataProvider, verticalExaggeration, _texturizer, _layerSet, _parameters, _showStatistics);
  
    for (int i = 0; i < _visibleSectorListeners.size(); i++)
    {
      tileRenderer.addVisibleSectorListener(_visibleSectorListeners.get(i), TimeInterval.fromMilliseconds(_stabilizationMilliSeconds.get(i)));
    }
  
    return tileRenderer;
  }
  public final void setTileTessellator(TileTessellator tileTessellator)
  {
    if (_tileTessellator != tileTessellator)
    {
      if (_tileTessellator != null)
         _tileTessellator.dispose();
      _tileTessellator = tileTessellator;
    }
  }
  public final void setTileTexturizer(TileTexturizer tileTexturizer)
  {
    if (_texturizer != tileTexturizer)
    {
      if (_texturizer != null)
         _texturizer.dispose();
      _texturizer = tileTexturizer;
    }
  }
  public final void setLayerSet(LayerSet layerSet)
  {
    if (_layerSet != layerSet)
    {
      if (_layerSet != null)
         _layerSet.dispose();
      _layerSet = layerSet;
    }
  }
  public final void setTileRendererParameters(TilesRenderParameters parameters)
  {
    if (_parameters != parameters)
    {
      if (_parameters != null)
         _parameters.dispose();
      _parameters = parameters;
    }
  }
  public final void setShowStatistics(boolean showStatistics)
  {
    _showStatistics = showStatistics;
  }
  public final void setRenderDebug(boolean renderDebug)
  {
    _renderDebug = renderDebug;
  }
  public final void setUseTilesSplitBuget(boolean useTilesSplitBudget)
  {
    _useTilesSplitBudget = useTilesSplitBudget;
  }
  public final void setForceTopLevelTilesRenderOnStart(boolean forceTopLevelTilesRenderOnStart)
  {
    _forceTopLevelTilesRenderOnStart = forceTopLevelTilesRenderOnStart;
  }
  public final void setIncrementalTileQuality(boolean incrementalTileQuality)
  {
    _incrementalTileQuality = incrementalTileQuality;
  }
  public final void addVisibleSectorListener(VisibleSectorListener listener, TimeInterval stabilizationInterval)
  {
    _visibleSectorListeners.add(listener);
    _stabilizationMilliSeconds.add(stabilizationInterval.milliseconds());
  }
  public final void addVisibleSectorListener(VisibleSectorListener listener)
  {
    addVisibleSectorListener(listener, TimeInterval.zero());
  }

}