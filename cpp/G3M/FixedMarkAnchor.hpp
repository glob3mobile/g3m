//
//  FixedMarkAnchor.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/8/26.
//

#ifndef G3M_FixedMarkAnchor
#define G3M_FixedMarkAnchor

#include "MarkAnchor.hpp"


class FixedMarkAnchor : public MarkAnchor {
private:
  const float _anchorU;
  const float _anchorV;

public:
  FixedMarkAnchor(const float anchorU,
                  const float anchorV) :
  _anchorU(anchorU),
  _anchorV(anchorV)
  {
  }

  ~FixedMarkAnchor() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  Vector2F getAnchor(const IImage* image) const {
    return Vector2F(_anchorU, _anchorV);
  }

};

#endif
