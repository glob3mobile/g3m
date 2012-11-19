//
//  Mesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Mesh_hpp
#define G3MiOSSDK_Mesh_hpp

#include "Context.hpp"
#include "Extent.hpp"

class Vector3D;

class Mesh {
public:
  
  virtual ~Mesh() { }
  
  virtual int getVertexCount() const = 0;
  
  virtual const Vector3D getVertex(int i) const = 0;
  
  virtual void render(const RenderContext* rc) const = 0;
  
  virtual Extent* getExtent() const = 0;

  virtual bool isTransparent(const RenderContext* rc) const = 0;

};


#endif
