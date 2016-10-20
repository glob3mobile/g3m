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

  MercatorPyramidDEMProvider();

  virtual ~MercatorPyramidDEMProvider();

public:

  PyramidDEMNode* createNode(const PyramidDEMNode* parent,
                             const size_t childID);

};

#endif
