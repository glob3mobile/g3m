package org.glob3.mobile.generated;
//
//  GPUAttributeValueVec4Float.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

//
//  GPUAttributeValueVec4Float.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//




public class GPUAttributeValueVec4Float extends GPUAttributeValueVecFloat
{
  public void dispose()
  {
    super.dispose();
  }

  public GPUAttributeValueVec4Float(IFloatBuffer buffer, int arrayElementSize, int index, int stride, boolean normalized)
  {
     super(buffer, 4, arrayElementSize, index, stride, normalized);
  
  }

}