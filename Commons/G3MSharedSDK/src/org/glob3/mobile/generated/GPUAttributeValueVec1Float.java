package org.glob3.mobile.generated;
//
//  GPUAttributeValueVec1Float.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

//
//  GPUAttributeValueVec1Float.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//




public class GPUAttributeValueVec1Float extends GPUAttributeValueVecFloat
{
  public void dispose()
  {
    super.dispose();
  }

  public GPUAttributeValueVec1Float(IFloatBuffer buffer, int arrayElementSize, int index, int stride, boolean normalized)
  {
     super(buffer, 1, arrayElementSize, index, stride, normalized);
  
  }
}
