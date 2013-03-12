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
  private boolean _forceFirstLevelTilesRenderOnStart;
  private boolean _incrementalTileQuality;
  private java.util.ArrayList<VisibleSectorListener> _visibleSectorListeners;
  private java.util.ArrayList<Long> _stabilizationMilliSeconds;
  private long _texturePriority;


  /**
   * Returns the _tileTessellator.
   *
   * @return _tileTessellator: TileTessellator*
   */
  private TileTessellator getTileTessellator()
  {
    if (_tileTessellator == null)
    {
      _tileTessellator = createTileTessellator();
    }
  
    return _tileTessellator;
  }

  /**
   * Returns the _texturizer.
   *
   * @return _texturizer: TileTexturizer*
   */
  private TileTexturizer getTexturizer()
  {
    if (_texturizer == null)
    {
      _texturizer = new MultiLayerTileTexturizer();
    }
  
    return _texturizer;
  }

  /**
   * Returns the _layerSet.
   *
   * @return _layerSet: LayerSet*
   */
  private LayerSet getLayerSet()
  {
    if (_layerSet == null)
    {
      _layerSet = createLayerSet();
    }
  
    return _layerSet;
  }

  /**
   * Returns the _parameters.
   *
   * @return _parameters: TilesRenderParameters*
   */
  private TilesRenderParameters getParameters()
  {
    if (_parameters == null)
    {
      _parameters = createTileRendererParameters();
    }
  
    return _parameters;
  }

  /**
   * Returns the showStatistics flag.
   *
   * @return _showStatistics: bool
   */
  private boolean getShowStatistics()
  {
    return _showStatistics;
  }

  /**
   * Returns the renderDebug flag.
   *
   * @return _renderDebug: bool
   */
  private boolean getRenderDebug()
  {
    return _renderDebug;
  }

  /**
   * Returns the useTilesSplitBudget flag.
   *
   * @return _useTilesSplitBudget: bool
   */
  private boolean getUseTilesSplitBudget()
  {
    return _useTilesSplitBudget;
  }

  /**
   * Returns the forceFirstLevelTilesRenderOnStart flag.
   *
   * @return _forceFirstLevelTilesRenderOnStart: bool
   */
  private boolean getForceFirstLevelTilesRenderOnStart()
  {
    return _forceFirstLevelTilesRenderOnStart;
  }

  /**
   * Returns the incrementalTileQuality flag.
   *
   * @return _incrementalTileQuality: bool
   */
  private boolean getIncrementalTileQuality()
  {
    return _incrementalTileQuality;
  }

  /**
   * Returns the array of visibleSectorListeners.
   */
  private java.util.ArrayList<VisibleSectorListener> getVisibleSectorListeners()
  {
    if (_visibleSectorListeners == null)
    {
      _visibleSectorListeners = new java.util.ArrayList<VisibleSectorListener>();
    }
    return _visibleSectorListeners;
  }

  /**
    * Returns the array of stabilization milliseconds related to visible-sector listeners.
    *
    * @return _stabilizationMilliSeconds: std::vector<long long>
    */
  private java.util.ArrayList<Long> getStabilizationMilliSeconds()
  {
    if (_stabilizationMilliSeconds == null)
    {
      _stabilizationMilliSeconds = new java.util.ArrayList<Long>();
    }
    return _stabilizationMilliSeconds;
  }

  /**
   * Returns the _texturePriority.
   *
   * @return _texturePriority: long long
   */
  private long getTexturePriority()
  {
    return _texturePriority;
  }

  private LayerSet createLayerSet()
  {
    return LayerBuilder.createDefaultSatelliteImagery();
  }
  private TilesRenderParameters createTileRendererParameters()
  {
    return new TilesRenderParameters(getRenderDebug(), getUseTilesSplitBudget(), getForceFirstLevelTilesRenderOnStart(), getIncrementalTileQuality());
  }
  private TileTessellator createTileTessellator()
  {
    //return new EllipsoidalTileTessellator(getParameters()->_tileMeshResolution, true);
    return new EllipsoidalTileTessellator(true);
  }


  ///#include "WMSBillElevationDataProvider.hpp"
  
  public TileRendererBuilder()
  {
    _showStatistics = false;
    _renderDebug = false;
    _useTilesSplitBudget = true;
    _forceFirstLevelTilesRenderOnStart = true;
    _incrementalTileQuality = false;
  
    _parameters = null;
    _layerSet = null;
    _texturizer = null;
    _tileTessellator = null;
    _visibleSectorListeners = null;
    _stabilizationMilliSeconds = null;
    _texturePriority = DownloadPriority.HIGHER;
  }
  public void dispose()
  {
    if (_parameters != null)
       _parameters.dispose();
    if (_layerSet != null)
       _layerSet.dispose();
    if (_texturizer != null)
       _texturizer.dispose();
    if (_tileTessellator != null)
       _tileTessellator.dispose();
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
  
  //  ElevationDataProvider* elevationDataProvider;
  //  elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///elev-35.0_-6.0_38.0_-2.0_4096x2048.bil", false),
  //                                                              Sector::fromDegrees(35, -6, 38, -2),
  //                                                              Vector2I(4096, 2048),
  //                                                              0);
  
    //  elevationDataProvider = new SingleBillElevationDataProvider(URL("file:///full-earth-4096x2048.bil", false),
    //                                                              Sector::fullSphere(),
    //                                                              Vector2I(4096, 2048),
    //                                                              0);
  
    int ___TODO_make_configurable;
    float verticalExaggeration = 1F;
  
    TileRenderer tileRenderer = new TileRenderer(getTileTessellator(), elevationDataProvider, verticalExaggeration, getTexturizer(), getLayerSet(), getParameters(), getShowStatistics(), getTexturePriority());
  
    for (int i = 0; i < getVisibleSectorListeners().size(); i++)
    {
      tileRenderer.addVisibleSectorListener(getVisibleSectorListeners().get(i), TimeInterval.fromMilliseconds(getStabilizationMilliSeconds().get(i)));
    }
  
    _parameters = null;
    _layerSet = null;
    _texturizer = null;
    _tileTessellator = null;
    _visibleSectorListeners = null;
    _visibleSectorListeners = null;
    _stabilizationMilliSeconds = null;
    _stabilizationMilliSeconds = null;
  
    return tileRenderer;
  }
  public final void setTileTessellator(TileTessellator tileTessellator)
  {
    if (_tileTessellator != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _tileTessellator already initialized");
      return;
    }
    _tileTessellator = tileTessellator;
  }
  public final void setTileTexturizer(TileTexturizer tileTexturizer)
  {
    if (_texturizer != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _texturizer already initialized");
      return;
    }
    _texturizer = tileTexturizer;
  }
  public final void setLayerSet(LayerSet layerSet)
  {
    if (_layerSet != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _layerSet already initialized");
      return;
    }
    _layerSet = layerSet;
  }
  public final void setTileRendererParameters(TilesRenderParameters parameters)
  {
    if (_parameters != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _parameters already initialized");
      return;
    }
    _parameters = parameters;
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
  public final void setForceFirstLevelTilesRenderOnStart(boolean forceFirstLevelTilesRenderOnStart)
  {
    _forceFirstLevelTilesRenderOnStart = forceFirstLevelTilesRenderOnStart;
  }
  public final void setIncrementalTileQuality(boolean incrementalTileQuality)
  {
    _incrementalTileQuality = incrementalTileQuality;
  }
  public final void addVisibleSectorListener(VisibleSectorListener listener, TimeInterval stabilizationInterval)
  {
    getVisibleSectorListeners().add(listener);
    getStabilizationMilliSeconds().add(stabilizationInterval.milliseconds());
  }
  public final void addVisibleSectorListener(VisibleSectorListener listener)
  {
    addVisibleSectorListener(listener, TimeInterval.zero());
  }
  public final void setTexturePriority(long texturePriority)
  {
    _texturePriority = texturePriority;
  }


  /**
   * Returns an array with the names of the layers that make up the default layerSet
   *
   * @return layersNames: std::vector<std::string>
   */
  public final java.util.ArrayList<String> getDefaultLayersNames()
  {
    java.util.ArrayList<String> layersNames = new java.util.ArrayList<String>();
  
    for (int i = 0; i < getLayerSet().size(); i++)
    {
      layersNames.add(getLayerSet().get(i).getName());
    }
  
    return layersNames;
  }

}