package org.glob3.mobile.generated;public class DefaultInfoDisplay extends InfoDisplay
{
  private Default_HUDRenderer _defaultHUDRenderer;

  public DefaultInfoDisplay(Default_HUDRenderer defaultHUDRenderer)
  {
	  _defaultHUDRenderer = defaultHUDRenderer;

  }

  public final void changedInfo(java.util.ArrayList<const Info> info)
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
