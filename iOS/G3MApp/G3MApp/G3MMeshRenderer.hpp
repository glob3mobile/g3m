//
//  G3MMeshRenderer.hpp
//  G3MApp
//
//  Created by Mari Luz Mateo on 25/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import <Foundation/Foundation.h>

class MeshRenderer;
class Planet;

class G3MMeshRenderer {
public:
  static MeshRenderer* createMeshRenderer(const Planet* planet);
};
