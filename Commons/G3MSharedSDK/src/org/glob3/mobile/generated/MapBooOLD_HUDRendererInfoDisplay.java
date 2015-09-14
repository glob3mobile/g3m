package org.glob3.mobile.generated; 
public class MapBooOLD_HUDRendererInfoDisplay implements InfoDisplay
{
  private MapBooOLD_HUDRenderer _mapBooHUDRenderer;

  public MapBooOLD_HUDRendererInfoDisplay(MapBooOLD_HUDRenderer mapBooHUDRenderer)
  {
     _mapBooHUDRenderer = mapBooHUDRenderer;

  }

  public final void changedInfo(java.util.ArrayList<Info> info)
  {
    _mapBooHUDRenderer.updateInfo(info);

  }

  public final void showDisplay()
  {
    _mapBooHUDRenderer.setEnable(true);
  }

  public final void hideDisplay()
  {
    _mapBooHUDRenderer.setEnable(false);
  }

  public final boolean isShowing()
  {
    return _mapBooHUDRenderer.isEnable();
  }

  public void dispose() { }

}