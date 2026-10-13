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
  private final boolean _hasTransitionMode;
  private final MarkTransitionMode _transitionMode;
  private final boolean _hasDetailLevel;
  private final int _detailLevel;

  /** anchor NULL: the mark keeps its own anchor (setMarkAnchor); the transition is the renderer's */
  public MarkOutfit(IImageFactory imageFactory, MarkAnchor anchor)
  {
     _imageFactory = imageFactory;
     _anchor = anchor;
     _hasTransitionMode = false;
     _transitionMode = MarkTransitionMode.SCALE_AND_ALPHA;
     _hasDetailLevel = false;
     _detailLevel = 0;
  }

  /** how this outfit comes in and goes away, whatever the renderer's mode */
  public MarkOutfit(IImageFactory imageFactory, MarkAnchor anchor, MarkTransitionMode transitionMode)
  {
     _imageFactory = imageFactory;
     _anchor = anchor;
     _hasTransitionMode = true;
     _transitionMode = transitionMode;
     _hasDetailLevel = false;
     _detailLevel = 0;
  }

  /** detailLevel: outfits of a mark with the same level are alternatives (the label on one side or the other), a higher one shows more */
  public MarkOutfit(IImageFactory imageFactory, MarkAnchor anchor, int detailLevel)
  {
     _imageFactory = imageFactory;
     _anchor = anchor;
     _hasTransitionMode = false;
     _transitionMode = MarkTransitionMode.SCALE_AND_ALPHA;
     _hasDetailLevel = true;
     _detailLevel = detailLevel;
  }

  public MarkOutfit(IImageFactory imageFactory, MarkAnchor anchor, int detailLevel, MarkTransitionMode transitionMode)
  {
     _imageFactory = imageFactory;
     _anchor = anchor;
     _hasTransitionMode = true;
     _transitionMode = transitionMode;
     _hasDetailLevel = true;
     _detailLevel = detailLevel;
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

  public final MarkTransitionMode getTransitionMode(MarkTransitionMode rendererMode)
  {
    return _hasTransitionMode ? _transitionMode : rendererMode;
  }

  public final int getDetailLevel(int levelByOrder)
  {
    return _hasDetailLevel ? _detailLevel : levelByOrder;
  }

}