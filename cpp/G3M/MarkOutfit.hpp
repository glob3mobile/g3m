//
//  MarkOutfit.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//

#ifndef G3M_MarkOutfit
#define G3M_MarkOutfit

#include "MarkTransitionMode.hpp"

class IImageFactory;
class MarkAnchor;


/** how a mark looks: its image and the point of it that sits on the mark's position */
class MarkOutfit {
private:
  IImageFactory*           _imageFactory;
  MarkAnchor*              _anchor;
  const bool               _hasTransitionMode;
  const MarkTransitionMode _transitionMode;
  const bool               _hasDetailLevel;
  const int                _detailLevel;

public:
  /** anchor NULL: the mark keeps its own anchor (setMarkAnchor); the transition is the renderer's */
  MarkOutfit(IImageFactory* imageFactory,
             MarkAnchor*    anchor);

  /** how this outfit comes in and goes away, whatever the renderer's mode */
  MarkOutfit(IImageFactory*     imageFactory,
             MarkAnchor*        anchor,
             MarkTransitionMode transitionMode);

  /** detailLevel: outfits of a mark with the same level are alternatives (the label on one side or the other), a higher one shows more */
  MarkOutfit(IImageFactory* imageFactory,
             MarkAnchor*    anchor,
             int            detailLevel);

  MarkOutfit(IImageFactory*     imageFactory,
             MarkAnchor*        anchor,
             int                detailLevel,
             MarkTransitionMode transitionMode);

  ~MarkOutfit();

  /** the factory goes to whoever creates the image, which may outlive the mark */
  IImageFactory* takeImageFactory();

  const MarkAnchor* getAnchor() const {
    return _anchor;
  }

  MarkTransitionMode getTransitionMode(MarkTransitionMode rendererMode) const {
    return _hasTransitionMode ? _transitionMode : rendererMode;
  }

  int getDetailLevel(int levelByOrder) const {
    return _hasDetailLevel ? _detailLevel : levelByOrder;
  }

};

#endif
