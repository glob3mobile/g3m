package org.glob3.mobile.generated;
//
//  GPUAttributeValueVec3Float.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//

//
//  GPUAttributeValueVec3Float.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 1/4/19.
//




public class GPUAttributeValueVec3Float extends GPUAttributeValueVecFloat
{
  public void dispose()
  {
    super.dispose();
  }

  public GPUAttributeValueVec3Float(IFloatBuffer buffer, int arrayElementSize, int index, int stride, boolean normalized)
  {
     super(buffer, 3, arrayElementSize, index, stride, normalized);
  
  }

}
