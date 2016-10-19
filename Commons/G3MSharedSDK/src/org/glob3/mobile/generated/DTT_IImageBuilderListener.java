package org.glob3.mobile.generated; 
public class DTT_IImageBuilderListener implements IImageBuilderListener
{


  private DefaultTileTexturizer _defaultTileTexturizer;


  public DTT_IImageBuilderListener(DefaultTileTexturizer defaultTileTexturizer)
  {
     _defaultTileTexturizer = defaultTileTexturizer;
  }

  public void dispose()
  {
  }

  public final void imageCreated(IImage image, String imageName)
  {
    _defaultTileTexturizer.setDefaultBackgroundImage(image);
    _defaultTileTexturizer.setDefaultBackgroundImageName(imageName);
    _defaultTileTexturizer.setDefaultBackgroundImageLoaded(true);
    ILogger.instance().logInfo("Default Background Image loaded...");

  }

  public final void onError(String error)
  {
    ILogger.instance().logError(error);
    _defaultTileTexturizer._errors.add("Can't download background image default");
    _defaultTileTexturizer._errors.add(error);

  }
}