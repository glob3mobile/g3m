//
//  MercatorPyramidDEMProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/19/16.
//
//

#ifndef MercatorPyramidDEMProvider_hpp
#define MercatorPyramidDEMProvider_hpp

#include "PyramidDEMProvider.hpp"

class MercatorPyramidDEMProvider : public PyramidDEMProvider {
protected:

  MercatorPyramidDEMProvider(const double deltaHeight,
                             const Vector2I& tileExtent);

  virtual ~MercatorPyramidDEMProvider();

public:

  PyramidNode* createNode(const PyramidNode* parent,
                          const size_t childID);

};

#endif
