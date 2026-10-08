package org.glob3.mobile.generated;
//
//  MarkOutfit.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//

//
//  MarkOutfit.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//


//class IImageFactory;
//class MarkAnchor;


/** how a mark looks: its image and the point of it that sits on the mark's position */
public class MarkOutfit
{
  private IImageFactory _imageFactory;
  private MarkAnchor _anchor;

  /** anchor NULL: the mark keeps its own anchor (setMarkAnchor) */
  public MarkOutfit(IImageFactory imageFactory, MarkAnchor anchor)
  {
     _imageFactory = imageFactory;
     _anchor = anchor;
  }

  public void dispose()
  {
    if (_imageFactory != null)
       _imageFactory.dispose();
    if (_anchor != null)
       _anchor.dispose();
  }

  /** the factory goes to whoever creates the image, which may outlive the mark */
  public final IImageFactory takeImageFactory()
  {
    IImageFactory imageFactory = _imageFactory;
    _imageFactory = null;
    return imageFactory;
  }

  public final MarkAnchor getAnchor()
  {
    return _anchor;
  }

}