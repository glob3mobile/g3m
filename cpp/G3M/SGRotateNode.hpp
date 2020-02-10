//
//  SGRotateNode.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3M__SGRotateNode__
#define __G3M__SGRotateNode__

#include "SGNode.hpp"

#include "MutableMatrix44D.hpp"


class SGRotateNode : public SGNode {
private:
  const double _x;
  const double _y;
  const double _z;
  const double _angle;

  MutableMatrix44D _rotationMatrix;

  GLState* _glState;

public:
  SGRotateNode(const std::string& id,
               const std::string& sID,
               double x,
               double y,
               double z,
               double angle);

  ~SGRotateNode();

  const GLState* createState(const G3MRenderContext* rc,
                             const GLState* parentState);

  const std::string description() {
    return "SGRotateNode";
  }
  
};

#endif
