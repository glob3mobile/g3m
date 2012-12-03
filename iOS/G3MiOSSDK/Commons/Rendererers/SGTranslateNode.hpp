//
//  SGTranslateNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3MiOSSDK__SGTranslateNode__
#define __G3MiOSSDK__SGTranslateNode__

#include "SGNode.hpp"

class SGTranslateNode : public SGNode {
private:
  const double _x;
  const double _y;
  const double _z;

public:

  SGTranslateNode(const std::string& id,
                  const std::string& sId,
                  double x,
                  double y,
                  double z) :
  SGNode(id, sId),
  _x(x),
  _y(y),
  _z(z)
  {

  }

  void prepareRender(const G3MRenderContext* rc);

  void cleanUpRender(const G3MRenderContext* rc);

  const GLState* createState(const G3MRenderContext* rc,
                             const GLState& parentState) {
    return NULL;
  }

};

#endif
