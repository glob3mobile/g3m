//
//  CameraHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 30/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "CameraHandler.h"
#include "IGL.hpp"

const Planet*     CameraHandler::_planet;
const ILogger*    CameraHandler::_logger;
IGL*              CameraHandler::gl;

Camera            CameraHandler::_camera0(NULL, 0, 0);               
Camera*           CameraHandler::_camera = NULL;               

Gesture           CameraHandler::_currentGesture = None;


