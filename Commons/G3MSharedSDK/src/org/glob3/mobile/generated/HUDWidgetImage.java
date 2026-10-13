package org.glob3.mobile.generated;
//
//  HUDWidgetImage.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/12/26.
//
//

//
//  HUDWidgetImage.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/12/26.
//
//



//class IImage;
//class IImageFactory;
//class G3MContext;
//class ChangedListener;


// An image a HUD widget draws, built by its factory (owned)
public class HUDWidgetImage
{
  private IImageFactory _imageFactory;

  private IImage _image;

  private String _imageName;
  private int _imageWidth;
  private int _imageHeight;

  private boolean _building;

  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private void startBuilding(G3MContext context)
  {
    _building = true;
    _imageFactory.create(context, new HUDWidgetImage_ImageFactoryListener(this), true);
  }

  public HUDWidgetImage(IImageFactory imageFactory)
  {
     _imageFactory = imageFactory;
     _image = null;
     _imageWidth = 0;
     _imageHeight = 0;
     _building = false;
  }

  public void dispose()
  {
    if (_imageFactory != null)
       _imageFactory.dispose();
    _image = null;
  }

  /** starts building the image unless it exists or is being built; a mutable factory reports its changes to changedListener */
  public final void initialize(G3MContext context, ChangedListener changedListener)
  {
    if (!_building && (_image == null))
    {
      startBuilding(context);
      if (_imageFactory.isMutable())
      {
        _imageFactory.setChangeListener(changedListener);
      }
    }
  }

  /** drops the image and builds it again */
  public final void rebuild(G3MContext context)
  {
    _image = null;
    _image = null;
    _imageName = "";
    _imageWidth = 0;
    _imageHeight = 0;
  
    startBuilding(context);
  }

  public final boolean isBuilding()
  {
    return _building;
  }

  public final java.util.ArrayList<String> getErrors()
  {
    return _errors;
  }

  /** NULL until built */
  public final IImage getImage()
  {
    return _image;
  }

  public final String getImageName()
  {
    return _imageName;
  }

  public final int getImageWidth()
  {
    return _imageWidth;
  }

  public final int getImageHeight()
  {
    return _imageHeight;
  }

  /** private, do not call */
  public final void imageCreated(IImage image, String imageName)
  {
    _building = false;
    _image = image;
    _imageName = imageName;
    _imageWidth = _image.getWidth();
    _imageHeight = _image.getHeight();
  }

  /** private, do not call */
  public final void onImageBuildError(String error)
  {
    _errors.add(error);
  }

}