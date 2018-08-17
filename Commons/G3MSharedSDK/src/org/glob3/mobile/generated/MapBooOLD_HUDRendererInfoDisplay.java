package org.glob3.mobile.generated;import java.util.*;

public class MapBooOLD_HUDRendererInfoDisplay extends InfoDisplay
{
  private MapBooOLD_HUDRenderer _mapBooHUDRenderer;

  public MapBooOLD_HUDRendererInfoDisplay(MapBooOLD_HUDRenderer mapBooHUDRenderer)
  {
	  _mapBooHUDRenderer = mapBooHUDRenderer;

  }

  public final void changedInfo(java.util.ArrayList<const Info> info)
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

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  public void dispose()
  {
  }
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final void dispose()
  {
  }
//#endif

}
