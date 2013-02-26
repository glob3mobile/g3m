package org.glob3.mobile.generated; 
public class LayerSet
{
  private java.util.ArrayList<Layer> _layers = new java.util.ArrayList<Layer>();

  private LayerSetChangedListener _listener;

  public LayerSet()
  {
     _listener = null;

  }

  public void dispose()
  {
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
    if (_listener == null)
    {
      //    ILogger::instance()->logError("Can't notify, _listener not set");
    }
    else
    {
      _listener.changed(this);
    }
  }

  public final java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, Tile tile, Vector2I tileTextureResolution)
  {
    java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
    for (int i = 0; i < _layers.size(); i++)
    {
      Layer layer = _layers.get(i);
      if (layer.isAvailable(rc, tile))
      {
        java.util.ArrayList<Petition> pet = layer.getMapPetitions(rc, tile, tileTextureResolution);
  
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

  public final void onTerrainTouchEvent(G3MEventContext ec, Geodetic3D position, Tile tile)
  {
  
    for (int i = 0; i < _layers.size(); i++)
    {
      Layer layer = _layers.get(i);
      if (layer.isAvailable(ec, tile))
      {
        TerrainTouchEvent tte = new TerrainTouchEvent(position, tile.getSector(), layer);
  
        layer.onTerrainTouchEventListener(ec, tte);
      }
    }
  
  }

  public final boolean isReady()
  {
    for (int i = 0; i<_layers.size(); i++)
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
    if (_listener == null)
    {
      //    ILogger::instance()->logError("Can't notify, _listener not set");
    }
    else
    {
      _listener.changed(this);
    }
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
    for (int i = 0; i < _layers.size(); i++)
    {
      if (name.equals(_layers.get(i).getName()))
      {
        return _layers.get(i);
      }
    }
  
    return null;
  }
}