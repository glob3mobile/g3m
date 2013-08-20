//
//  SceneJSParserStatistics.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 11/03/13.
//
//

#include "SceneJSParserStatistics.hpp"

#include "IStringBuilder.hpp"
#include "ILogger.hpp"

SceneJSParserStatistics::SceneJSParserStatistics() {
  _nodesCount = 0;
  _materialsCount = 0;
  _geometriesCount = 0;
  _verticesCount = 0;
}

SceneJSParserStatistics::~SceneJSParserStatistics() {
}

void SceneJSParserStatistics::computeNode() {
  _nodesCount++;
}

int SceneJSParserStatistics::getNodesCount() const{
  return _nodesCount;
}

void SceneJSParserStatistics::computeMaterial() {
  _materialsCount++;
}

int SceneJSParserStatistics::getMaterialsCount() const {
  return _materialsCount;
}

void SceneJSParserStatistics::computeGeometry() {
  _geometriesCount++;
}

int SceneJSParserStatistics::getGeometriesCount() const{
  return _geometriesCount;
}

void SceneJSParserStatistics::computeVertex() {
  _verticesCount++;
}

int SceneJSParserStatistics::getVerticesCount() const {
  return _verticesCount;
}

std::string SceneJSParserStatistics::asLogString() const {
  IStringBuilder* statsSB = IStringBuilder::newStringBuilder();
  
  statsSB->addString("Nodes=");
  statsSB->addInt(getNodesCount());
  statsSB->addString("; Materials=");
  statsSB->addInt(getMaterialsCount());
  statsSB->addString("; Geometries=");
  statsSB->addInt(getGeometriesCount());
  statsSB->addString("; Vertices=");
  statsSB->addInt(getVerticesCount());
  statsSB->addString("; Vert/Geom=");
  statsSB->addFloat((float) getVerticesCount() / getGeometriesCount());
  statsSB->addString("; Vert/Mat=");
  statsSB->addFloat((float) getVerticesCount() / getMaterialsCount());
  statsSB->addString("; Vert/Nod=");
  statsSB->addFloat((float) getVerticesCount() / getNodesCount());
  
  std::string stats = statsSB->getString();
  delete statsSB;

  return stats;
}

void SceneJSParserStatistics::log() const {
  if (ILogger::instance()) {
    ILogger::instance()->logInfo("\nSceneJSParserStatistics: %s", asLogString().c_str());
  }
}
