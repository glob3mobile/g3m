//
//  MarkOutfit.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//

#ifndef G3M_MarkOutfit
#define G3M_MarkOutfit

class IImageFactory;
class MarkAnchor;


/** how a mark looks: its image and the point of it that sits on the mark's position */
class MarkOutfit {
private:
  IImageFactory* _imageFactory;
  MarkAnchor*    _anchor;

public:
  /** anchor NULL: the mark keeps its own anchor (setMarkAnchor) */
  MarkOutfit(IImageFactory* imageFactory,
             MarkAnchor*    anchor);

  ~MarkOutfit();

  /** the factory goes to whoever creates the image, which may outlive the mark */
  IImageFactory* takeImageFactory();

  const MarkAnchor* getAnchor() const {
    return _anchor;
  }

};

#endif
