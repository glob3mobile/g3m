package org.glob3.mobile.generated; 
public class MapBooOLD_Scene
{
  private final String _id;
  private final String _name;
  private final String _description;
  private final MapBooOLD_MultiImage _screenshot;
  private final Color _backgroundColor ;
  private final MapBooOLD_CameraPosition _cameraPosition;
  private final Sector _sector;
  private Layer _baseLayer;
  private Layer _overlayLayer;
  private final boolean _queryable;
  private final boolean _hasWarnings;

  public MapBooOLD_Scene(String id, String name, String description, MapBooOLD_MultiImage screenshot, Color backgroundColor, MapBooOLD_CameraPosition cameraPosition, Sector sector, Layer baseLayer, Layer overlayLayer, boolean queryable, boolean hasWarnings)
  {
     _id = id;
     _name = name;
     _description = description;
     _screenshot = screenshot;
     _backgroundColor = new Color(backgroundColor);
     _cameraPosition = cameraPosition;
     _sector = sector;
     _baseLayer = baseLayer;
     _overlayLayer = overlayLayer;
     _queryable = queryable;
     _hasWarnings = hasWarnings;
  }

  public final String getId()
  {
    return _id;
  }

  public final String getName()
  {
    return _name;
  }

  public final String getDescription()
  {
    return _description;
  }

  public final MapBooOLD_MultiImage getScreenshot()
  {
    return _screenshot;
  }

  public final Color getBackgroundColor()
  {
    return _backgroundColor;
  }

  public final MapBooOLD_CameraPosition getCameraPosition()
  {
    return _cameraPosition;
  }

  public final Sector getSector()
  {
    return _sector;
  }

  public final Layer getBaseLayer()
  {
    return _baseLayer;
  }

  public final Layer getOverlayLayer()
  {
    return _overlayLayer;
  }

  public final boolean isQueryable()
  {
    return _queryable;

  }

  public final boolean hasWarnings()
  {
    return _hasWarnings;
  }

  public final LayerSet createLayerSet()
  {
    LayerSet layerSet = new LayerSet();
    if (_baseLayer != null)
    {
      layerSet.addLayer(_baseLayer.copy());
    }
    if (_overlayLayer != null)
    {
      layerSet.addLayer(_overlayLayer.copy());
    }
    return layerSet;
  }

  public void dispose()
  {
    if (_screenshot != null)
       _screenshot.dispose();
    if (_baseLayer != null)
       _baseLayer.dispose();
    if (_overlayLayer != null)
       _overlayLayer.dispose();
    if (_cameraPosition != null)
       _cameraPosition.dispose();
    if (_sector != null)
       _sector.dispose();
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("[Scene name=");
    isb.addString(_name);
  
    isb.addString(", description=");
    isb.addString(_description);
  
    isb.addString(", screenshot=");
    isb.addString((_screenshot == null) ? "null" : _screenshot.description());
  
    isb.addString(", backgroundColor=");
    isb.addString(_backgroundColor.description());
  
    isb.addString(", cameraPosition=");
    if (_cameraPosition == null)
    {
      isb.addString("NULL");
    }
    else
    {
      isb.addString(_cameraPosition.description());
    }
  
    isb.addString(", baseLayer=");
    if (_baseLayer == null)
    {
      isb.addString("NULL");
    }
    else
    {
      isb.addString(_baseLayer.description());
    }
  
    isb.addString(", overlayLayer=");
    if (_overlayLayer == null)
    {
      isb.addString("NULL");
    }
    else
    {
      isb.addString(_overlayLayer.description());
    }
  
    isb.addString(", hasWarnings=");
    isb.addBool(_hasWarnings);
  
    isb.addString("]");
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
  @Override
  public String toString() {
    return description();
  }

}