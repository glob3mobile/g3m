package org.glob3.mobile.generated;
public class HUDWidgetImage_ImageFactoryListener implements IImageFactoryListener
{
  private HUDWidgetImage _widgetImage;

  public HUDWidgetImage_ImageFactoryListener(HUDWidgetImage widgetImage)
  {
     _widgetImage = widgetImage;
  }

  public void dispose()
  {
  }

  public final void imageCreated(IImage image, String imageName)
  {
    _widgetImage.imageCreated(image, imageName);
  }

  public final void onError(String error)
  {
    _widgetImage.onImageBuildError(error);
  }
}