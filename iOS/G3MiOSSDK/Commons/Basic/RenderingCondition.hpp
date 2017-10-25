//
//  RenderingCondition.hpp
//  CaceresCommon
//
//  Created by Jose Miguel SN on 25/10/2017.
//  Copyright © 2017 IGO SOFTWARE S.L. All rights reserved.
//

#ifndef RenderingCondition_hpp
#define RenderingCondition_hpp

#include <stdio.h>
#include "G3MRenderContext.hpp"

class RenderingCondition{
public:
  virtual ~RenderingCondition(){}
  virtual bool mustRender(const G3MRenderContext* context) = 0;
};

#endif /* RenderingCondition_hpp */
