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
  private long _texturePriority;


  /**
   * Returns the _tileTessellator.
   * Lazy initialization.
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
   * Lazy initialization.
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
   * Lazy initialization.
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
   * Lazy initialization.
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
   * Method created to keep convention. It is not needed as it does not have to create a default value.
   *
   * @return _showStatistics: bool
   */
  private boolean getShowStatistics()
  {
    return _showStatistics;
  }

  /**
   * Returns the renderDebug flag.
   * Method created to keep convention. It is not needed as it does not have to create a default value.
   *
   * @return _renderDebug: bool
   */
  private boolean getRenderDebug()
  {
    return _renderDebug;
  }

  /**
   * Returns the useTilesSplitBudget flag.
   * Method created to keep convention. It is not needed as it does not have to create a default value.
   *
   * @return _useTilesSplitBudget: bool
   */
  private boolean getUseTilesSplitBudget()
  {
    return _useTilesSplitBudget;
  }

  /**
   * Returns the forceTopLevelTilesRenderOnStart flag.
   * Method created to keep convention. It is not needed as it does not have to create a default value.
   *
   * @return _forceTopLevelTilesRenderOnStart: bool
   */
  private boolean getForceTopLevelTilesRenderOnStart()
  {
    return _forceTopLevelTilesRenderOnStart;
  }

  /**
   * Returns the incrementalTileQuality flag.
   * Method created to keep convention. It is not needed as it does not have to create a default value.
   *
   * @return _incrementalTileQuality: bool
   */
  private boolean getIncrementalTileQuality()
  {
    return _incrementalTileQuality;
  }

  /**
   * Returns the array of visibleSectorListeners.
   * Method created to keep convention. It is not needed as it does not have to create a default value.
   *
   * @return _forceTopLevelTilesRenderOnStart: std::vector<VisibleSectorListener*>
   */
  private java.util.ArrayList<VisibleSectorListener> getVisibleSectorListeners()
  {
    return _visibleSectorListeners;
  }

  /**
    * Returns the array of stabilization milliseconds related to visible-sector listeners.
    * Method created to keep convention. It is not needed as it does not have to create a default value.
    *
    * @return _stabilizationMilliSeconds: std::vector<long long>
    */
  private java.util.ArrayList<Long> getStabilizationMilliSeconds()
  {
    return _stabilizationMilliSeconds;
  }

  /**
   * Returns the _texturePriority
   * Method created to keep convention. It is not needed as it does not have to create a default value.
   *
   * @return _texturePriority: long long
   */
  private long getTexturePriority()
  {
    return _texturePriority;
  }

  private LayerSet createLayerSet()
  {
    LayerSet layerSet = LayerBuilder.createDefaultSatelliteImagery();
  
    return layerSet;
  }
  private TilesRenderParameters createTileRendererParameters()
  {
    return TilesRenderParameters.createDefault(getRenderDebug(), getUseTilesSplitBudget(), getForceTopLevelTilesRenderOnStart(), getIncrementalTileQuality());
  }
  private TileTessellator createTileTessellator()
  {
    return new EllipsoidalTileTessellator(_parameters._tileResolution, true);
  }

  public TileRendererBuilder()
  {
    _showStatistics = false;
    _renderDebug = false;
    _useTilesSplitBudget = true;
    _forceTopLevelTilesRenderOnStart = true;
    _incrementalTileQuality = false;
  
    _parameters = null;
    _layerSet = null;
    _texturizer = null;
    _tileTessellator = null;
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
    TileRenderer tileRenderer = new TileRenderer(getTileTessellator(), getTexturizer(), getLayerSet(), getParameters(), getShowStatistics(), getTexturePriority());
    int __TODO_make_inflator_configurable;
  //  ElevationDataProvider* elevationDataProvider = new WMSBillElevationDataProvider();
    ElevationDataProvider elevationDataProvider = null;
    TileRenderer tileRenderer = new TileRenderer(_tileTessellator, elevationDataProvider, _texturizer, _layerSet, _parameters, _showStatistics);
  
    for (int i = 0; i < getVisibleSectorListeners().size(); i++)
    {
      tileRenderer.addVisibleSectorListener(getVisibleSectorListeners().get(i), TimeInterval.fromMilliseconds(getStabilizationMilliSeconds().get(i)));
    }
  
    _parameters = null;
    _layerSet = null;
    _texturizer = null;
    _tileTessellator = null;
  
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
    if (_texturizer != tileTexturizer)
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