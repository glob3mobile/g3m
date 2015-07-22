package org.glob3.mobile.generated; 
//
//  CartoDBLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/27/13.
//
//

//
//  CartoDBLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/27/13.
//
//



public class CartoDBLayer extends MercatorTiledLayer
{
  private final String _userName;
  private final String _table;

  private static java.util.ArrayList<String> getSubdomains()
  {
    java.util.ArrayList<String> result = new java.util.ArrayList<String>();
    result.add("0.");
    result.add("1.");
    result.add("2.");
    result.add("3.");
    return result;
  }

  protected final String getLayerType()
  {
    return "CartoDB";
  }

  protected final boolean rawIsEquals(Layer that)
  {
    CartoDBLayer t = (CartoDBLayer) that;
    return (_domain.equals(t._domain));
  }


  // http://0.tiles.cartocdn.com/mdelacalle/tiles/tm_world_borders_simpl_0_3/2/0/1.png

  public CartoDBLayer(String userName, String table, TimeInterval timeToCache, boolean readExpired, float transparency, boolean isTransparent, LayerCondition condition)
  {
     this(userName, table, timeToCache, readExpired, transparency, isTransparent, condition, new java.util.ArrayList<Info>());
  }
  public CartoDBLayer(String userName, String table, TimeInterval timeToCache, boolean readExpired, float transparency, boolean isTransparent)
  {
     this(userName, table, timeToCache, readExpired, transparency, isTransparent, null, new java.util.ArrayList<Info>());
  }
  public CartoDBLayer(String userName, String table, TimeInterval timeToCache, boolean readExpired, float transparency)
  {
     this(userName, table, timeToCache, readExpired, transparency, false, null, new java.util.ArrayList<Info>());
  }
  public CartoDBLayer(String userName, String table, TimeInterval timeToCache, boolean readExpired)
  {
     this(userName, table, timeToCache, readExpired, 1, false, null, new java.util.ArrayList<Info>());
  }
  public CartoDBLayer(String userName, String table, TimeInterval timeToCache)
  {
     this(userName, table, timeToCache, true, 1, false, null, new java.util.ArrayList<Info>());
  }
  public CartoDBLayer(String userName, String table, TimeInterval timeToCache, boolean readExpired, float transparency, boolean isTransparent, LayerCondition condition, java.util.ArrayList<Info> layerInfo)
  {
     super("http://", "tiles.cartocdn.com/" + userName + "/tiles/" + table, getSubdomains(), "png", timeToCache, readExpired, 2, 17, isTransparent, transparency, condition, layerInfo);
     _userName = userName;
     _table = table;
  }

  public final String description()
  {
    return "[CartoDBLayer]";
  }

  @Override
  public String toString() {
    return description();
  }

  public final CartoDBLayer copy()
  {
    return new CartoDBLayer(_userName, _table, _timeToCache, _readExpired, _transparency, _isTransparent, (_condition == null) ? null : _condition.copy(), _layerInfo);
  }

  public final RenderState getRenderState()
  {
    _errors.clear();
    if (_userName.compareTo("") == 0)
    {
      _errors.add("Missing layer parameter: userName");
    }
    if (_table.compareTo("") == 0)
    {
      _errors.add("Missing layer parameter: table");
    }
  
    if (_errors.size() > 0)
    {
      return RenderState.error(_errors);
    }
    return RenderState.ready();
  }
}