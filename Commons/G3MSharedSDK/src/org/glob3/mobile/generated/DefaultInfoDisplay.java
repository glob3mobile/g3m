package org.glob3.mobile.generated; 
public class DefaultInfoDisplay implements InfoDisplay
{
  private Default_HUDRenderer _defaultHUDRenderer;

  public DefaultInfoDisplay(Default_HUDRenderer defaultHUDRenderer)
  {
     _defaultHUDRenderer = defaultHUDRenderer;

  }

  public final void changedInfo(java.util.ArrayList<Info> info)
  {
    _defaultHUDRenderer.updateInfo(info);
  }

  public final void showDisplay()
  {
    _defaultHUDRenderer.setEnable(true);
  }

  public final void hideDisplay()
  {
    _defaultHUDRenderer.setEnable(false);
  }

  public final boolean isShowing()
  {
    return _defaultHUDRenderer.isEnable();
  }

  public void dispose() { }

}