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

	private LayerSet createLayerSet()
	{
	  LayerSet layerSet = new LayerSet();
    
	  WMSLayer bing = new WMSLayer("ve", new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false), WMSServerVersion.WMS_1_1_0, Sector.fullSphere(), "image/jpeg", "EPSG:4326", "", false, null);
	  layerSet.addLayer(bing);
    
	  return layerSet;
	}
	private TilesRenderParameters createTileRendererParameters()
	{
	  TilesRenderParameters parameters = TilesRenderParameters.createDefault(_renderDebug, _useTilesSplitBudget, _forceTopLevelTilesRenderOnStart, _incrementalTileQuality);
    
	  return parameters;
	}
	private TileTessellator createTileTessellator()
	{
	  TileTessellator tileTessellator = new EllipsoidalTileTessellator(_parameters._tileResolution, true);
    
	  return tileTessellator;
	}

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
	  //    delete _tileTessellator;
	  //    delete _texturizer;
	  //    delete _layerSet;
	  ///#ifdef C_CODE
	  //    delete _parameters;
	  ///#endif
	}
	public final TileRenderer create()
	{
	  TileRenderer tileRenderer = new TileRenderer(_tileTessellator, _texturizer, _layerSet, _parameters, _showStatistics);
    
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

}