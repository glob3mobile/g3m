package org.glob3.mobile.generated; 
public class MapBoo_Scene
{
  private final String _name;
  private final String _description;
  private final String _icon;
  private final Color _backgroundColor ;
  private Layer _baseLayer;
  private Layer _overlayLayer;

  public MapBoo_Scene(String name, String description, String icon, Color backgroundColor, Layer baseLayer, Layer overlayLayer)
  {
     _name = name;
     _description = description;
     _icon = icon;
     _backgroundColor = new Color(backgroundColor);
     _baseLayer = baseLayer;
     _overlayLayer = overlayLayer;
  }

  public final String getName()
  {
    return _name;
  }

  public final String getDescription()
  {
    return _description;
  }

  public final String getIcon()
  {
    return _icon;
  }

  public final Color getBackgroundColor()
  {
    return _backgroundColor;
  }

  public final void recreateLayerSet(LayerSet layerSet)
  {
    if (_baseLayer != null)
    {
      layerSet.addLayer(_baseLayer);
    }
  
    if (_overlayLayer != null)
    {
      layerSet.addLayer(_overlayLayer);
    }
  }

  public void dispose()
  {
    if (_baseLayer != null)
       _baseLayer.dispose();
    if (_overlayLayer != null)
       _overlayLayer.dispose();
  }
}