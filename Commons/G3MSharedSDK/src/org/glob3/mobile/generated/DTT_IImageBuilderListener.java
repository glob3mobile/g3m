package org.glob3.mobile.generated; 
public class DTT_IImageBuilderListener implements IImageBuilderListener
{


  private DefaultTileTexturizer _defaultTileTesturizer;


  public DTT_IImageBuilderListener(DefaultTileTexturizer defaultTileTesturizer)
  {
     _defaultTileTesturizer = defaultTileTesturizer;
  }

  public void dispose()
  {
  }

  public final void imageCreated(IImage image, String imageName)
  {
    _defaultTileTesturizer.setDefaultBackGroundImage(image);
    _defaultTileTesturizer.setDefaultBackGroundImageName(imageName);
    _defaultTileTesturizer.setDefaultBackGroundImageLoaded(true);
    ILogger.instance().logInfo("Default Background Image loaded...");

  }

  public final void onError(String error)
  {
    ILogger.instance().logError(error);
    _defaultTileTesturizer._errors.add("Can't download background image default");
    _defaultTileTesturizer._errors.add(error);

  }
}