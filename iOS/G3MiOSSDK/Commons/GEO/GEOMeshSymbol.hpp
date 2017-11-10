//
//  GEOMeshSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#ifndef __G3MiOSSDK__GEOMeshSymbol__
#define __G3MiOSSDK__GEOMeshSymbol__

#include "GEOSymbol.hpp"
#include "Mesh.hpp"

class Geodetic2D;
class Color;
class Ellipsoid;
class Planet;

class GEOMeshSymbol : public GEOSymbol {
protected:

  Mesh* createLine2DMesh(const std::vector<Geodetic2D*>* coordinates,
                         const Color& lineColor,
                         float lineWidth,
                         double deltaHeight,
                         const Planet* planet) const;

  Mesh* createLines2DMesh(const std::vector<const std::vector<Geodetic2D*>*>* coordinatesArray,
                          const Color& lineColor,
                          float lineWidth,
                          double deltaHeight,
                          const Planet* planet) const;


  virtual Mesh* createMesh(const G3MRenderContext* rc) const = 0;
  
  mutable bool _meshEnabledAtCreation;
  mutable Mesh::MeshUserData* _meshData;
  void modifyMeshAfterCreation(Mesh* result) const{
    if (_meshData != NULL){
      result->setUserData(_meshData);
      _meshData = NULL;
    }
    result->setEnable(_meshEnabledAtCreation);
  }

public:
  
  GEOMeshSymbol():_meshData(NULL), _meshEnabledAtCreation(true){}

  bool symbolize(const G3MRenderContext* rc,
                 const GEOSymbolizer*    symbolizer,
                 MeshRenderer*           meshRenderer,
                 ShapesRenderer*         shapesRenderer,
                 MarksRenderer*          marksRenderer,
                 GEOVectorLayer*         geoVectorLayer) const;
  
  void setMeshUserData(Mesh::MeshUserData* meshData){
    _meshData = meshData;
  }
  
  void setMeshEnabled(bool enabled){
    _meshEnabledAtCreation = enabled;
  }


};

#endif
