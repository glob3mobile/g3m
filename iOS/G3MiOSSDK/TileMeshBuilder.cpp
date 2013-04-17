//
//  TileMeshBuilder.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/04/13.
//
//

#include "TileMeshBuilder.hpp"

#include "LeveledMesh.hpp"
#include "ElevationDataProvider.hpp"

void TileMeshBuilder::findAncestorWithElevationDataSolved(){
  
  if (_ancestorWithElevationDataSolved != NULL){
    return;
  }
  
  Tile * ancestor = _tile;
  while (!ancestor->isElevationDataSolved() && ancestor != NULL){
    ancestor = ancestor->getParent();
  }
  
  _ancestorWithElevationDataSolved = NULL;
}

Mesh* TileMeshBuilder::createMeshWithoutElevation(const Planet* planet,
                                                  const Vector2I& resolution,
                                                  bool debug) const{
  
  return _tesselator->createTileMesh(planet,
                                     resolution,
                                     _tile,
                                     NULL,
                                     0,
                                     debug);
  
}

Mesh* TileMeshBuilder::createMeshWithElevation(const Planet* planet,
                                               const Vector2I& resolution,
                                               float verticalExaggeration,
                                               bool debug) const{
  return _tesselator->createTileMesh(planet,
                                     resolution,
                                     _tile,
                                     _tile->getElevationData(),
                                     verticalExaggeration,
                                     debug);
}

LeveledMesh* TileMeshBuilder::createTileMesh(const Planet* planet,
                                             const Vector2I& resolution,
                                             float verticalExaggeration,
                                             bool debug,
                                             double defaultHeight){
  
  LeveledMesh* leveledMesh = _tile->getLeveledMesh();
  
  Mesh* mesh = NULL;
  if (_provider == NULL){
    mesh = createMeshWithoutElevation(planet, resolution, debug);
  } else{
    
    if (_tile->isElevationDataSolved()){ //Tile has its elevation data
      mesh = createMeshWithElevation(planet, resolution, verticalExaggeration, debug);
    } else{
      
      //TODO: LAUNCH PETITION
      
      //Creating FallBack Mesh
      findAncestorWithElevationDataSolved();
      
      
      if ((_ancestorWithElevationDataSolved == NULL) ||
          (leveledMesh != NULL && _ancestorWithElevationDataSolved->getLevel() > leveledMesh->getLevel())){
        
        //There's no fallback or it is not necessary
        mesh = createMeshWithoutElevation(planet, resolution, debug);
      } else{

        //There's a fallback
        ElevationData* fallBackED = _ancestorWithElevationDataSolved->getElevationData();
        ElevationData* subViewED = _provider->createSubviewOfElevationData(fallBackED,
                                                                           _tile->getSector(),
                                                                           resolution); //Asking provider for subview
        
        _tile->setElevationData(subViewED, false); //Storing subview (not solved data)
        
        //Creating fallback
        mesh = createMeshWithElevation(planet, resolution, verticalExaggeration, debug);
      }
      
    }
  }
  
  if (leveledMesh == NULL){
    leveledMesh = new LeveledMesh(mesh, _tile->getLevel());
  }
  return leveledMesh;
  
  
}