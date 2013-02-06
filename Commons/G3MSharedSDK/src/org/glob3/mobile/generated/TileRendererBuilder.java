

package org.glob3.mobile.generated;

//
// TileRendererBuilder.cpp
// G3MiOSSDK
//
// Created by Mari Luz Mateo on 22/11/12.
//
//

//
// TileRendererBuilder.hpp
// G3MiOSSDK
//
// Created by Mari Luz Mateo on 22/11/12.
//
//


public class TileRendererBuilder {

  private TileTessellator _tileTessellator;
  private TileTexturizer _texturizer;
  private LayerSet _layerSet;
  private TilesRenderParameters _parameters;
  private boolean _showStatistics;
  private boolean _renderDebug;
  private boolean _useTilesSplitBudget;
  private boolean _forceTopLevelTilesRenderOnStart;
  private boolean _incrementalTileQuality;
  private final java.util.ArrayList<VisibleSectorListener> _visibleSectorListeners = new java.util.ArrayList<VisibleSectorListener>();
  private final java.util.ArrayList<Long> _stabilizationMilliSeconds = new java.util.ArrayList<Long>();


  private LayerSet createLayerSet() {
    final LayerSet layerSet = new LayerSet();

    final WMSLayer bing = LayerBuilder.createBingLayer(true);
    layerSet.addLayer(bing);

    return layerSet;
  }


  private TilesRenderParameters createTileRendererParameters() {
    return TilesRenderParameters.createDefault(_renderDebug,
        _useTilesSplitBudget, _forceTopLevelTilesRenderOnStart,
        _incrementalTileQuality);
  }


  private TileTessellator createTileTessellator() {
    final TileTessellator tileTessellator = new EllipsoidalTileTessellator(
        _parameters._tileResolution, true);

    return tileTessellator;
  }


  public TileRendererBuilder() {
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


  public void dispose() {
    // delete _tileTessellator;
    // delete _texturizer;
    // delete _layerSet;
    // /#ifdef C_CODE
    // delete _parameters;
    // /#endif
  }


  public final TileRenderer create() {
    final TileRenderer tileRenderer = new TileRenderer(_tileTessellator,
        _texturizer, _layerSet, _parameters, _showStatistics);

    for (int i = 0; i < _visibleSectorListeners.size(); i++) {
      tileRenderer.addVisibleSectorListener(_visibleSectorListeners.get(i),
          TimeInterval.fromMilliseconds(_stabilizationMilliSeconds.get(i)));
    }

    return tileRenderer;
  }


  public final void setTileTessellator(final TileTessellator tileTessellator) {
    if (_tileTessellator != tileTessellator) {
      if (_tileTessellator != null) {
        _tileTessellator.dispose();
      }
      _tileTessellator = tileTessellator;
    }
  }


  public final void setTileTexturizer(final TileTexturizer tileTexturizer) {
    if (_texturizer != tileTexturizer) {
      if (_texturizer != null) {
        _texturizer.dispose();
      }
      _texturizer = tileTexturizer;
    }
  }


  public final void setLayerSet(final LayerSet layerSet) {
    if (_layerSet != layerSet) {
      if (_layerSet != null) {
        _layerSet.dispose();
      }
      _layerSet = layerSet;
    }
  }


  public final void setTileRendererParameters(final TilesRenderParameters parameters) {
    if (_parameters != parameters) {
      _parameters = parameters;
    }
  }


  public final void setShowStatistics(final boolean showStatistics) {
    _showStatistics = showStatistics;
  }


  public final void setRenderDebug(final boolean renderDebug) {
    _renderDebug = renderDebug;
  }


  public final void setUseTilesSplitBuget(final boolean useTilesSplitBudget) {
    _useTilesSplitBudget = useTilesSplitBudget;
  }


  public final void setForceTopLevelTilesRenderOnStart(final boolean forceTopLevelTilesRenderOnStart) {
    _forceTopLevelTilesRenderOnStart = forceTopLevelTilesRenderOnStart;
  }


  public final void setIncrementalTileQuality(final boolean incrementalTileQuality) {
    _incrementalTileQuality = incrementalTileQuality;
  }


  public final void addVisibleSectorListener(final VisibleSectorListener listener,
                                             final TimeInterval stabilizationInterval) {
    _visibleSectorListeners.add(listener);
    _stabilizationMilliSeconds.add(stabilizationInterval.milliseconds());
  }


  public final void addVisibleSectorListener(final VisibleSectorListener listener) {
    addVisibleSectorListener(listener, TimeInterval.zero());
  }


  // TODO: IN CORE
  public TilesRenderParameters getTileRenderParameters() {
    return _parameters;
  }


  // TODO: IN CORE
  public TileTexturizer getTileTexturizer() {
    return _texturizer;
  }

}
