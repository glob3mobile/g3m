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



//class TileTessellator;
//class TileTexturizer;
//class GEOVectorLayer;
//class TileLODTester;
//class TileVisibilityTester;
//class LayerSet;
//class VisibleSectorListener;
//class ElevationDataProvider;
//class TerrainElevationProvider;
//class Sector;
//class ChangedRendererInfoListener;
//class IImageBuilder;
//class PlanetRenderer;


public class PlanetRendererBuilder
{

  private TileTessellator _tileTessellator;
  private TileTexturizer _texturizer;
  private java.util.ArrayList<GEOVectorLayer> _geoVectorLayers = new java.util.ArrayList<GEOVectorLayer>();
  private TileLODTester _tileLODTester;
  private TileVisibilityTester _tileVisibilityTester;

  private LayerSet _layerSet;
  private TilesRenderParameters _parameters;
  private boolean _showStatistics;
  private boolean _renderDebug;
  private boolean _incrementalTileQuality;
  private Quality _quality;
  private java.util.ArrayList<VisibleSectorListener> _visibleSectorListeners;
  private java.util.ArrayList<Long> _stabilizationMilliSeconds;
  private long _tileTextureDownloadPriority;

  private ElevationDataProvider _elevationDataProvider;
  private TerrainElevationProvider _terrainElevationProvider;
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
      _texturizer = new DefaultTileTexturizer(this.getDefaultTileBackgroundImageBuilder());
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
   * Returns the _tileTextureDownloadPriority.
   *
   * @return _tileTextureDownloadPriority: long long
   */
  private long getTileTextureDownloadPriority()
  {
    return _tileTextureDownloadPriority;
  }

  private boolean _logTilesPetitions;

  private LayerSet createLayerSet()
  {
    return LayerBuilder.createDefault();
  }
  private TilesRenderParameters createPlanetRendererParameters()
  {
    return new TilesRenderParameters(getRenderDebug(), getIncrementalTileQuality(), getQuality());
  }
  private TileTessellator createTileTessellator()
  {
    ///#warning Testing Terrain Normals
    final boolean skirted = true;
    return new PlanetTileTessellator(skirted, getRenderedSector());
  }

  private ElevationDataProvider getElevationDataProvider()
  {
    return _elevationDataProvider;
  }
  private TerrainElevationProvider getTerrainElevationProvider()
  {
    return _terrainElevationProvider;
  }

  private float getVerticalExaggeration()
  {
    if (_verticalExaggeration <= 0.0f)
    {
      _verticalExaggeration = 1.0f;
    }
    return _verticalExaggeration;
  }

  private Sector _renderedSector;
  private Sector getRenderedSector()
  {
    if (_renderedSector == null)
    {
      return Sector.fullSphere();
    }
    return _renderedSector;
  }

  private boolean _renderTileMeshes;
  private boolean getRenderTileMeshes()
  {
    return _renderTileMeshes;
  }

  private boolean getLogTilesPetitions()
  {
    return _logTilesPetitions;
  }

  private ChangedRendererInfoListener _changedInfoListener;

  private TouchEventType _touchEventTypeOfTerrainTouchListener;

  private TouchEventType getTouchEventTypeOfTerrainTouchListener()
  {
    return _touchEventTypeOfTerrainTouchListener;
  }

  private IImageBuilder _defaultTileBackgroundImage = null;

  private IImageBuilder getDefaultTileBackgroundImageBuilder()
  {
    if (_defaultTileBackgroundImage == null)
    {
      return new DefaultChessCanvasImageBuilder(256, 256, Color.black(), Color.white(), 4);
    }
    return _defaultTileBackgroundImage;
  }

  private TileLODTester createDefaultTileLODTester()
  {
    TileLODTester proj = new ProjectedCornersDistanceTileLODTester();
  
    TileLODTester timed = new TimedCacheTileLODTester(TimeInterval.fromMilliseconds(500), proj);
  
    TileLODTester maxLevel = new MaxLevelTileLODTester();
  
    TileLODTester gradual = new GradualSplitsTileLODTester(TimeInterval.fromMilliseconds(10), timed);
  
    TileLODTester composite = new OrTileLODTester(maxLevel, gradual);
  
    return new MaxFrameTimeTileLODTester(TimeInterval.fromMilliseconds(25), composite);
  }

  private TileVisibilityTester createDefaultTileVisibilityTester()
  {
    return new TimedCacheTileVisibilityTester(TimeInterval.fromMilliseconds(1000), new MeshBoundingVolumeTileVisibilityTester());
  }


  public PlanetRendererBuilder()
  {
     _showStatistics = false;
     _renderDebug = false;
     _incrementalTileQuality = false;
     _quality = Quality.QUALITY_LOW;
     _parameters = null;
     _layerSet = null;
     _texturizer = null;
     _tileTessellator = null;
     _visibleSectorListeners = null;
     _stabilizationMilliSeconds = null;
     _tileTextureDownloadPriority = DownloadPriority.HIGHER;
     _elevationDataProvider = null;
     _terrainElevationProvider = null;
     _verticalExaggeration = 0F;
     _renderedSector = null;
     _renderTileMeshes = true;
     _logTilesPetitions = false;
     _changedInfoListener = null;
     _touchEventTypeOfTerrainTouchListener = TouchEventType.LongPress;
     _tileLODTester = null;
     _tileVisibilityTester = null;
  }
  public void dispose()
  {
    if (_parameters != null)
       _parameters.dispose();
    if (_layerSet != null)
       _layerSet.dispose();
    if (_texturizer != null)
       _texturizer.dispose();
  
    final int geoVectorLayersSize = _geoVectorLayers.size();
    for (int i = 0; i < geoVectorLayersSize; i++)
    {
      GEOVectorLayer geoVectorLayer = _geoVectorLayers.get(i);
      if (geoVectorLayer != null)
         geoVectorLayer.dispose();
    }
  
    if (_tileTessellator != null)
       _tileTessellator.dispose();
    if (_elevationDataProvider != null)
       _elevationDataProvider.dispose();
    if (_terrainElevationProvider != null)
       _terrainElevationProvider.dispose();
  
    if (_renderedSector != null)
       _renderedSector.dispose();
  }
  public final PlanetRenderer create()
  {
  
    LayerSet layerSet = getLayerSet();
    final int geoVectorLayersSize = _geoVectorLayers.size();
    for (int i = 0; i < geoVectorLayersSize; i++)
    {
      GEOVectorLayer geoVectorLayer = _geoVectorLayers.get(i);
      layerSet.addLayer(geoVectorLayer);
    }
  
    PlanetRenderer planetRenderer = new PlanetRenderer(getTileTessellator(), getElevationDataProvider(), true, getTerrainElevationProvider(), true, getVerticalExaggeration(), getTexturizer(), layerSet, getParameters(), getShowStatistics(), getTileTextureDownloadPriority(), getRenderedSector(), getRenderTileMeshes(), getLogTilesPetitions(), getChangedRendererInfoListener(), getTouchEventTypeOfTerrainTouchListener(), getTileLODTester(), getTileVisibilityTester());
  
    for (int i = 0; i < getVisibleSectorListeners().size(); i++)
    {
      planetRenderer.addVisibleSectorListener(getVisibleSectorListeners().get(i), TimeInterval.fromMilliseconds(getStabilizationMilliSeconds().get(i)));
    }
  
    _parameters = null;
    _layerSet = null;
    _texturizer = null;
    _tileTessellator = null;
    _visibleSectorListeners = null;
    _visibleSectorListeners = null;
    _stabilizationMilliSeconds = null;
    _stabilizationMilliSeconds = null;
  
    _elevationDataProvider = null;
  
    if (_renderedSector != null)
       _renderedSector.dispose();
    _renderedSector = null;
  
    _geoVectorLayers.clear();
  
    return planetRenderer;
  }
  public final void setTileTessellator(TileTessellator tileTessellator)
  {
    if (_tileTessellator != null)
    {
      throw new RuntimeException("LOGIC ERROR: _tileTessellator already initialized");
    }
    _tileTessellator = tileTessellator;
  }
  public final void setTileTexturizer(TileTexturizer tileTexturizer)
  {
    if (_texturizer != null)
    {
      throw new RuntimeException("LOGIC ERROR: _texturizer already initialized");
    }
    _texturizer = tileTexturizer;
  }
  public final void setLayerSet(LayerSet layerSet)
  {
    if (_layerSet != null)
    {
      throw new RuntimeException("LOGIC ERROR: _layerSet already initialized");
    }
    _layerSet = layerSet;
  }
  public final void setPlanetRendererParameters(TilesRenderParameters parameters)
  {
    if (_parameters != null)
    {
      throw new RuntimeException("LOGIC ERROR: _parameters already initialized");
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
  public final void setIncrementalTileQuality(boolean incrementalTileQuality)
  {
    _incrementalTileQuality = incrementalTileQuality;
  }
  public final void addVisibleSectorListener(VisibleSectorListener listener, TimeInterval stabilizationInterval)
  {
    getVisibleSectorListeners().add(listener);
    getStabilizationMilliSeconds().add(stabilizationInterval._milliseconds);
  }
  public final void addVisibleSectorListener(VisibleSectorListener listener)
  {
    addVisibleSectorListener(listener, TimeInterval.zero());
  }
  public final void setTileTextureDownloadPriority(long tileTextureDownloadPriority)
  {
    _tileTextureDownloadPriority = tileTextureDownloadPriority;
  }

  public final void setElevationDataProvider(ElevationDataProvider elevationDataProvider)
  {
    if (_elevationDataProvider != null)
    {
      throw new RuntimeException("LOGIC ERROR: _elevationDataProvider already initialized");
    }
    _elevationDataProvider = elevationDataProvider;
  }

  public final void setTerrainElevationProvider(TerrainElevationProvider terrainElevationProvider)
  {
    if (_terrainElevationProvider != null)
    {
      throw new RuntimeException("LOGIC ERROR: _terrainElevationProvider already initialized");
    }
    _terrainElevationProvider = terrainElevationProvider;
  }

  public final void setVerticalExaggeration(float verticalExaggeration)
  {
    if (_verticalExaggeration > 0.0f)
    {
      throw new RuntimeException("LOGIC ERROR: _verticalExaggeration already initialized");
    }
    _verticalExaggeration = verticalExaggeration;
  }

  public final void setRenderedSector(Sector sector)
  {
    if (_renderedSector != null)
    {
      throw new RuntimeException("LOGIC ERROR: _renderedSector already initialized");
    }
    _renderedSector = new Sector(sector);
  }

  public final GEOVectorLayer createGEOVectorLayer()
  {
    GEOVectorLayer geoVectorLayer = new GEOVectorLayer();
    _geoVectorLayers.add(geoVectorLayer);
    return geoVectorLayer;
  }

  public final Quality getQuality()
  {
    return _quality;
  }
  public final void setQuality(Quality quality)
  {
    _quality = quality;
  }

  public final void setRenderTileMeshes(boolean renderTileMeshes)
  {
    _renderTileMeshes = renderTileMeshes;
  }

  public final void setLogTilesPetitions(boolean logTilesPetitions)
  {
    _logTilesPetitions = logTilesPetitions;
  }

  public final ChangedRendererInfoListener getChangedRendererInfoListener()
  {
    return _changedInfoListener;
  }

  public final void setChangedRendererInfoListener(ChangedRendererInfoListener changedInfoListener)
  {
    if (_changedInfoListener != null)
    {
      throw new RuntimeException("LOGIC ERROR: ChangedInfoListener in Planet Render Builder already set");
    }
    _changedInfoListener = changedInfoListener;
  }

  public final void setTouchEventTypeOfTerrainTouchListener(TouchEventType touchEventTypeOfTerrainTouchListener)
  {
    _touchEventTypeOfTerrainTouchListener = touchEventTypeOfTerrainTouchListener;
  }

  public final void setDefaultTileBackgroundImage(IImageBuilder defaultTileBackgroundImage)
  {
    _defaultTileBackgroundImage = defaultTileBackgroundImage;
  }

  public final void setTileLODTester(TileLODTester tlt)
  {
    _tileLODTester = tlt;
  }

  public final TileLODTester getTileLODTester()
  {
    if (_tileLODTester == null)
    {
      _tileLODTester = createDefaultTileLODTester();
    }
    return _tileLODTester;
  }

  public final TileVisibilityTester getTileVisibilityTester()
  {
    if (_tileVisibilityTester == null)
    {
      _tileVisibilityTester = createDefaultTileVisibilityTester();
    }
    return _tileVisibilityTester;
  }

}