package org.glob3.mobile.generated; 
public class LayerSet
{
  private java.util.ArrayList<Layer> _layers = new java.util.ArrayList<Layer>();

  private LayerSetChangedListener _listener;

  private LayerTilesRenderParameters _layerTilesRenderParameters;


  private LayerTilesRenderParameters createLayerTilesRenderParameters()
  {
    Sector mergedSector = null;
    int splitsByLatitude = 0;
    int splitsByLongitude = 0;
    int maxLevel = 0;
    int tileTextureWidth = 0;
    int tileTextureHeight = 0;
    int tileMeshWidth = 0;
    int tileMeshHeight = 0;
    boolean mercator = false;
  
    boolean first = true;
    final int layersCount = _layers.size();
    for (int i = 0; i < layersCount; i++)
    {
      Layer layer = _layers.get(i);
  
      if (layer.isEnable())
      {
        final LayerTilesRenderParameters layerParam = layer.getLayerTilesRenderParameters();
  
        if (first)
        {
          first = false;
  
          mergedSector = new Sector(layerParam._topSector);
          splitsByLatitude = layerParam._splitsByLatitude;
          splitsByLongitude = layerParam._splitsByLongitude;
          maxLevel = layerParam._maxLevel;
  
          tileTextureWidth = layerParam._tileTextureResolution._x;
          tileTextureHeight = layerParam._tileTextureResolution._y;
          tileMeshWidth = layerParam._tileMeshResolution._x;
          tileMeshHeight = layerParam._tileMeshResolution._y;
          mercator = layerParam._mercator;
        }
        else
        {
          if (!mergedSector.fullContains(layerParam._topSector))
          {
            Sector oldSector = mergedSector;
            mergedSector = new Sector(oldSector.mergedWith(layerParam._topSector));
            if (oldSector != null)
               oldSector.dispose();
          }
  
          if (splitsByLatitude != layerParam._splitsByLatitude)
          {
            ILogger.instance().logError("Inconsistency in Layer's Parameters: splitsByLatitude");
            return null;
          }
  
          if (splitsByLongitude != layerParam._splitsByLongitude)
          {
            ILogger.instance().logError("Inconsistency in Layer's Parameters: splitsByLongitude");
            return null;
          }
  
          //if (layerParam->_maxLevel > maxLevel) {
          //  maxLevel = layerParam->_maxLevel;
          //}
          if (maxLevel != layerParam._maxLevel)
          {
            ILogger.instance().logError("Inconsistency in Layer's Parameters: maxLevel");
            return null;
          }
  
          if ((tileTextureWidth != layerParam._tileTextureResolution._x) || (tileTextureHeight != layerParam._tileTextureResolution._y))
          {
            ILogger.instance().logError("Inconsistency in Layer's Parameters: tileTextureResolution");
            return null;
          }
  
          if ((tileMeshWidth != layerParam._tileMeshResolution._x) || (tileMeshHeight != layerParam._tileMeshResolution._y))
          {
            ILogger.instance().logError("Inconsistency in Layer's Parameters: tileMeshResolution");
            return null;
          }
  
          if (mercator != layerParam._mercator)
          {
            ILogger.instance().logError("Inconsistency in Layer's Parameters: mercator");
            return null;
          }
  
        }
      }
    }
  
    if (first)
    {
      ILogger.instance().logError("Can't create LayerSet's LayerTilesRenderParameters, not found any enable Layer");
      return null;
    }
  
    final Sector topSector = new Sector(mergedSector);
    if (mergedSector != null)
       mergedSector.dispose();
    mergedSector = null;
  
    return new LayerTilesRenderParameters(topSector, splitsByLatitude, splitsByLongitude, maxLevel, new Vector2I(tileTextureWidth, tileTextureHeight), new Vector2I(tileMeshWidth, tileMeshHeight), mercator);
  }
  private void layersChanged()
  {
    if (_layerTilesRenderParameters != null)
       _layerTilesRenderParameters.dispose();
    _layerTilesRenderParameters = null;
  
    if (_listener != null)
    {
      _listener.changed(this);
    }
  }

  public LayerSet()
  {
     _listener = null;
     _layerTilesRenderParameters = null;

  }

  public void dispose()
  {
    if (_layerTilesRenderParameters != null)
       _layerTilesRenderParameters.dispose();
    for (int i = 0; i < _layers.size(); i++)
    {
      if (_layers.get(i) != null)
         _layers.get(i).dispose();
    }
  }

  public final void addLayer(Layer layer)
  {
    layer.setLayerSet(this);
    _layers.add(layer);
  
    layersChanged();
  }

  public final java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, Tile tile)
  {
    java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
    for (int i = 0; i < _layers.size(); i++)
    {
      Layer layer = _layers.get(i);
      if (layer.isAvailable(rc, tile))
      {
        java.util.ArrayList<Petition> pet = layer.createTileMapPetitions(rc, tile);
  
        //Storing petitions
        for (int j = 0; j < pet.size(); j++)
        {
          petitions.add(pet.get(j));
        }
      }
    }
  
    if (petitions.isEmpty())
    {
      rc.getLogger().logWarning("Can't create map petitions for tile %s", tile.getKey().description());
    }
  
    return petitions;
  }

  public final boolean onTerrainTouchEvent(G3MEventContext ec, Geodetic3D position, Tile tile)
  {
  
  
  
    for (int i = _layers.size()-1; i >= 0; i--)
    {
      Layer layer = _layers.get(i);
      if (layer.isAvailable(ec, tile))
      {
        TerrainTouchEvent tte = new TerrainTouchEvent(position, tile.getSector(), layer);
  
        if (layer.onTerrainTouchEventListener(ec, tte))
        {
          return true;
        }
      }
    }
  
    return false;
  }

  public final boolean isReady()
  {
    final int layersCount = _layers.size();
    if (layersCount < 1)
    {
      return false;
    }
  
    for (int i = 0; i < layersCount; i++)
    {
      if (!(_layers.get(i).isReady()))
      {
        return false;
      }
    }
    return true;
  }

  public final void initialize(G3MContext context)
  {
    for (int i = 0; i<_layers.size(); i++)
    {
      _layers.get(i).initialize(context);
    }
  }

  public final int size()
  {
    return _layers.size();
  }

  public final void layerChanged(Layer layer)
  {
    layersChanged();
  }

  public final void setChangeListener(LayerSetChangedListener listener)
  {
    if (_listener != null)
    {
      ILogger.instance().logError("Listener already set");
    }
    _listener = listener;
  }

  public final Layer get(int index)
  {
    if (index < _layers.size())
    {
      return _layers.get(index);
    }
  
    return null;
  }

  public final Layer getLayer(String name)
  {
    final int layersCount = _layers.size();
    for (int i = 0; i < layersCount; i++)
    {
      if (name.equals(_layers.get(i).getName()))
      {
        return _layers.get(i);
      }
    }
  
    return null;
  }

  public final LayerTilesRenderParameters getLayerTilesRenderParameters()
  {
    if (_layerTilesRenderParameters == null)
    {
      _layerTilesRenderParameters = createLayerTilesRenderParameters();
    }
    return _layerTilesRenderParameters;
  }

//  const Angle calculateSplitLatitude(const Tile* tile) const;

}