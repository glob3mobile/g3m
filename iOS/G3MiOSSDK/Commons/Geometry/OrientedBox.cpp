//
//  OrientedBox.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 19/02/16.
//

#include "OrientedBox.hpp"
#include "Mesh.hpp"


OrientedBox::~OrientedBox() {
  delete _mesh;
  
#ifdef JAVA_CODE
  super.dispose();
#endif

};

