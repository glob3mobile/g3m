package org.glob3.mobile.generated; 
//
//  PlanetRendererBuilder.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 22/11/12.
//
//

//
//  PlanetRendererBuilder.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 22/11/12.
//
//




public class PlanetRendererBuilder
{

  private TileTessellator _tileTessellator;
  private TileTexturizer _texturizer;
  private TileRasterizer _tileRasterizer;

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

  private ElevationDataProvider _elevationDataProvider;
  private float _verticalExaggeration;


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
  private TileRasterizer getTileRasterizer()
  {
    return _tileRasterizer;
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
      _parameters = createPlanetRendererParameters();
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
  private TilesRenderParameters createPlanetRendererParameters()
  {
    return new TilesRenderParameters(getRenderDebug(), getUseTilesSplitBudget(), getForceFirstLevelTilesRenderOnStart(), getIncrementalTileQuality());
  }
  private TileTessellator createTileTessellator()
  {
    return new EllipsoidalTileTessellator(true);
  }

  private ElevationDataProvider getElevationDataProvider()
  {
    return _elevationDataProvider;
  }
  private float getVerticalExaggeration()
  {
    if (_verticalExaggeration <= 0.0f)
    {
      _verticalExaggeration = 1.0f;
    }
    return _verticalExaggeration;
  }

  public PlanetRendererBuilder()
  {
    _showStatistics = false;
    _renderDebug = false;
    _useTilesSplitBudget = true;
    _forceFirstLevelTilesRenderOnStart = true;
    _incrementalTileQuality = false;
  
    _parameters = null;
    _layerSet = null;
    _texturizer = null;
    _tileRasterizer = null;
    _tileTessellator = null;
    _visibleSectorListeners = null;
    _stabilizationMilliSeconds = null;
    _texturePriority = DownloadPriority.HIGHER;
  
    _elevationDataProvider = null;
    _verticalExaggeration = 0.0f;
  }
  public void dispose()
  {
    if (_parameters != null)
       _parameters.dispose();
    if (_layerSet != null)
       _layerSet.dispose();
    if (_texturizer != null)
       _texturizer.dispose();
    if (_tileRasterizer != null)
       _tileRasterizer.dispose();
    if (_tileTessellator != null)
       _tileTessellator.dispose();
    if (_elevationDataProvider != null)
       _elevationDataProvider.dispose();
  
    JAVA_POST_DISPOSE
  }
  public final PlanetRenderer create()
  {
    PlanetRenderer planetRenderer = new PlanetRenderer(getTileTessellator(), getElevationDataProvider(), getVerticalExaggeration(), getTexturizer(), getTileRasterizer(), getLayerSet(), getParameters(), getShowStatistics(), getTexturePriority());
  
    for (int i = 0; i < getVisibleSectorListeners().size(); i++)
    {
      planetRenderer.addVisibleSectorListener(getVisibleSectorListeners().get(i), TimeInterval.fromMilliseconds(getStabilizationMilliSeconds().get(i)));
    }
  
    _parameters = null;
    _layerSet = null;
    _texturizer = null;
    _tileRasterizer = null;
    _tileTessellator = null;
    _visibleSectorListeners = null;
    _visibleSectorListeners = null;
    _stabilizationMilliSeconds = null;
    _stabilizationMilliSeconds = null;
  
    _elevationDataProvider = null;
  
    return planetRenderer;
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
  public final void setTileRasterizer(TileRasterizer tileRasterizer)
  {
    if (_tileRasterizer != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _tileRasterizer already initialized");
      return;
    }
    _tileRasterizer = tileRasterizer;
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
  public final void setPlanetRendererParameters(TilesRenderParameters parameters)
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

  public final void setElevationDataProvider(ElevationDataProvider elevationDataProvider)
  {
    if (_elevationDataProvider != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _elevationDataProvider already initialized");
      return;
    }
    _elevationDataProvider = elevationDataProvider;
  }

  public final void setVerticalExaggeration(float verticalExaggeration)
  {
    if (_verticalExaggeration > 0.0f)
    {
      ILogger.instance().logError("LOGIC ERROR: _verticalExaggeration already initialized");
      return;
    }
    _verticalExaggeration = verticalExaggeration;
  }

}