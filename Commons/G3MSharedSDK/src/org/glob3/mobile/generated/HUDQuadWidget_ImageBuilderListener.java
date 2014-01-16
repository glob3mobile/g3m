package org.glob3.mobile.generated; 
public class HUDQuadWidget_ImageBuilderListener implements IImageBuilderListener
{
  private HUDQuadWidget _quadWidget;

  public HUDQuadWidget_ImageBuilderListener(HUDQuadWidget quadWidget)
  {
     _quadWidget = quadWidget;
  }

  public void dispose()
  {
  }

  public final void imageCreated(IImage image, String imageName)
  {
    _quadWidget.imageCreated(image, imageName);
  }

  public final void onError(String error)
  {
    _quadWidget.onImageBuildError(error);
  }
}