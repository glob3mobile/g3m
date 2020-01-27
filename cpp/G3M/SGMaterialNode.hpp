//
//  SGMaterialNode.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3M__SGMaterialNode__
#define __G3M__SGMaterialNode__

#include "SGNode.hpp"

class Color;


class SGMaterialNode : public SGNode {
private:
#ifdef C_CODE
  const Color* _baseColor;
#endif
#ifdef JAVA_CODE
  private Color _baseColor;
#endif
  const Color* _specularColor;

  //  const double _specular;
  //  const double _shine;
  //  const double _alpha;
  //  const double _emit;

  GLState* _glState;

public:

  SGMaterialNode(const std::string& id,
                 const std::string& sID,
                 const Color* baseColor,
                 const Color* specularColor,
                 double specular,
                 double shine,
                 double alpha,
                 double emit);

  const GLState* createState(const G3MRenderContext* rc,
                             const GLState* parentState);

  void setBaseColor(Color* baseColor);

  ~SGMaterialNode();

  const std::string description() {
    return "SGMaterialNode";
  }
  
};

#endif
