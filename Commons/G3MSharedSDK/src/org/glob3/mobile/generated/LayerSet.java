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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<Petition*> createTileMapPetitions(const G3MRenderContext* rc, const Tile* tile, int width, int height) const
  public final java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, Tile tile, int width, int height)
  {
	java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
	for (int i = 0; i < _layers.size(); i++)
	{
	  Layer layer = _layers.get(i);
	  if (layer.isAvailable(rc, tile))
	  {
		java.util.ArrayList<Petition> pet = layer.getMapPetitions(rc, tile, width, height);
  
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void onTerrainTouchEvent(const G3MEventContext* ec, const Geodetic3D& position, const Tile* tile) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isReady()const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void initialize(const G3MContext* context)const
  public final void initialize(G3MContext context)
  {
	for (int i = 0; i<_layers.size(); i++)
	{
	  _layers.get(i).initialize(context);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int size() const
  public final int size()
  {
	return _layers.size();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void layerChanged(const Layer* layer) const
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

}