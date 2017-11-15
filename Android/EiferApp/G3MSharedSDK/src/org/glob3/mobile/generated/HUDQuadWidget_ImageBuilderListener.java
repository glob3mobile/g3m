package org.glob3.mobile.generated; 
public class HUDQuadWidget_ImageBuilderListener implements IImageBuilderListener
{
  private HUDQuadWidget _quadWidget;
  private final int _imageRole;

  public HUDQuadWidget_ImageBuilderListener(HUDQuadWidget quadWidget, int imageRole)
  {
     _quadWidget = quadWidget;
     _imageRole = imageRole;
  }

  public void dispose()
  {
  }

  public final void imageCreated(IImage image, String imageName)
  {
    _quadWidget.imageCreated(image, imageName, _imageRole);
  }

  public final void onError(String error)
  {
    _quadWidget.onImageBuildError(error, _imageRole);
  }
}