//
//  MarkAnchor.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//

#ifndef G3M_MarkAnchor
#define G3M_MarkAnchor

#include "Vector2F.hpp"

class IImage;


/** the point of a mark's image, in UV, that sits on the mark's position; known once the image exists */
class MarkAnchor {
public:
  virtual ~MarkAnchor() {
  }

  virtual Vector2F getAnchor(const IImage* image) const = 0;

};

#endif
