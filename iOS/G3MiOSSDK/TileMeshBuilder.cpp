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
  
  if (_ancestorWithElevationDataSolved == NULL){
    _ancestorWithElevationDataSolved = _tile->getParent();
    while (_ancestorWithElevationDataSolved != NULL && !_ancestorWithElevationDataSolved->isElevationDataSolved()){
      _ancestorWithElevationDataSolved = _ancestorWithElevationDataSolved->getParent();
    }
  }
}

void TileMeshBuilder::fillTileWithAncestorElevationData(ElevationDataProvider* provider){
  if (_ancestorWithElevationDataSolved != NULL && provider != NULL)
    if (_ancestorWithElevationDataSolved->getLevel() > _tile->getElevationDataLevel()){
      ElevationData* fallBackED = _ancestorWithElevationDataSolved->getElevationData();
      if (fallBackED ==NULL){
        printf("ERROR");
      }
      
      ElevationData* subViewED = provider->createSubviewOfElevationData(fallBackED,
                                                                        _tile->getSector(),
                                                                        Vector2I(_resX, _resY)); //Asking provider for subview
      
      _tile->setElevationData(subViewED, _ancestorWithElevationDataSolved->getLevel());
    }
}

void TileMeshBuilder::fillTileWithElevationData(ElevationDataProvider* provider, const Vector2I& resolution){
  _provider = provider;
  if (!_tile->isElevationDataSolved()){
    
    if (_tile->getElevationData() == NULL){ //Only looking for ancestors if there is no data
      findAncestorWithElevationDataSolved();
      fillTileWithAncestorElevationData(provider);
    }
    
    if (_listener == NULL && provider != NULL){
      //sendPetition(provider, resolution);
    }
  }
}
/*
 Mesh* TileMeshBuilder::createMeshWithoutElevation(const TileTessellator* tesselator,
 const Planet* planet,
 const Vector2I& resolution,
 bool debug) const{
 
 return tesselator->createTileMesh(planet,
 resolution,
 _tile,
 NULL,
 0,
 debug);
 
 }
 
 Mesh* TileMeshBuilder::createMeshWithElevation(const TileTessellator* tesselator,
 const Planet* planet,
 const Vector2I& resolution,
 float verticalExaggeration,
 bool debug) const{
 return tesselator->createTileMesh(planet,
 resolution,
 _tile,
 _tile->getElevationData(),
 verticalExaggeration,
 debug);
 }
 */
void TileMeshBuilder::sendPetition(ElevationDataProvider* provider, const Vector2I& resolution){
  if (_listener == NULL){
    _listener = new ElevationDataProviderListener(this);
    _listener->launch(provider, _tile->getSector(), resolution);
  }
}

void TileMeshBuilder::onAncestorSolvedElevationData(Tile* ancestor){ //Storing new ancestor
  if (_ancestorWithElevationDataSolved == NULL ||
      _ancestorWithElevationDataSolved->getLevel() < ancestor->getLevel()) {
    _ancestorWithElevationDataSolved = ancestor;
    
    if (_provider == NULL){
      printf("FALLO");
    }
    
    fillTileWithAncestorElevationData(_provider);
  }
}

void TileMeshBuilder::onSolvedElevationData(ElevationData* ed){
  _tile->setElevationData(ed, _tile->getLevel());  //Elevation Data of Tile is solved
}

void TileMeshBuilder::cancelElevationDataRequest(){
  if (_listener != NULL){
    _listener->cancel();
    _listener = NULL;
  }
}
/*
 LeveledMesh* TileMeshBuilder::createTileMesh(const TileTessellator* tesselator,
 ElevationDataProvider* provider,
 const Planet* planet,
 const Vector2I& resolution,
 float verticalExaggeration,
 bool debug,
 double defaultHeight){
 
 _provider = provider; //Storing provider for subviewing data
 _resX = resolution._x;
 _resY = resolution._y; //Storing resolution of tile
 
 LeveledMesh* leveledMesh = _tile->getLeveledMesh();
 
 Mesh* mesh = NULL;
 int level = 0;
 if (provider == NULL){
 mesh = createMeshWithoutElevation(tesselator, planet, resolution, debug);
 level = -1;
 } else{
 
 if (_tile->isElevationDataSolved()){ //Tile has its elevation data
 mesh = createMeshWithElevation(tesselator, planet, resolution, verticalExaggeration, debug);
 level = _tile->getLevel();
 } else{
 
 //Launching petition
 sendPetition(provider, resolution);
 
 
 //Creating FallBack Mesh
 
 if (_ancestorWithElevationDataSolved == NULL){
 //There's no fallback but we need some mesh
 mesh = createMeshWithoutElevation(tesselator, planet, resolution, debug);
 level = -1;
 } else{
 
 //If there is not a better mesh we create a new one with fallback data
 if ((leveledMesh == NULL) ||
 (leveledMesh != NULL && _ancestorWithElevationDataSolved->getLevel() > leveledMesh->getLevel())){
 //Creating mesh with fallback elevation data
 mesh = createMeshWithElevation(tesselator, planet, resolution, verticalExaggeration, debug);
 level = _ancestorWithElevationDataSolved->getLevel();
 }
 }
 
 }
 }
 
 if (leveledMesh == NULL && mesh != NULL){
 leveledMesh = new LeveledMesh(mesh, level);
 } else{
 if (mesh != NULL){
 leveledMesh->setMesh(mesh, level);
 } else{
 printf("NO NEEDED NEW MESH");
 }
 }
 
 if (leveledMesh == NULL){
 printf("FALLO 2");
 }
 
 return leveledMesh;
 }
 */

#pragma mark ElevationDataProviderListener

void ElevationDataProviderListener::launch(ElevationDataProvider* provider,
                                           const Sector& sector,
                                           const Vector2I& resolution){
  _provider = provider;
  _id = provider->requestElevationData(sector, resolution, this, false);
}

void ElevationDataProviderListener::cancel(){
  _meshBuilder = NULL;
  if (_provider != NULL){
    _provider->cancelRequest(_id);
  }
}

void ElevationDataProviderListener::onData(const Sector& sector,
                                           const Vector2I& resolution,
                                           ElevationData* elevationData){
  if (_meshBuilder != NULL){
    _meshBuilder->onSolvedElevationData(elevationData);
  }
  //TODO: DELETE LISTENER
  //delete this;
  
}

void ElevationDataProviderListener::onError(const Sector& sector,
                                            const Vector2I& resolution){
  //delete this;
}