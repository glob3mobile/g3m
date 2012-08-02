//
//  LatLonMeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 02/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#include <iostream>

#include "LatLonMeshRenderer.h"
#include "Context.hpp"


void LatLonMeshRenderer::initialize(const InitializationContext* ic)
{
  
}  


int LatLonMeshRenderer::render(const RenderContext* rc)
{
  // obtaing gl object reference
  IGL *gl = rc->getGL();
    
  return MAX_TIME_TO_RENDER;
}
