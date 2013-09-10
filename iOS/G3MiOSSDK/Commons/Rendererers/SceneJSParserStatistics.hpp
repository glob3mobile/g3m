//
//  SceneJSParserStatistics.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 11/03/13.
//
//

#ifndef __G3MiOSSDK__SceneJSParserStatistics__
#define __G3MiOSSDK__SceneJSParserStatistics__

#include <string>

class ILogger;

class SceneJSParserStatistics {
private:
  mutable int _nodesCount;
  mutable int _materialsCount;
  mutable int _geometriesCount;
  mutable int _verticesCount;
public:
  SceneJSParserStatistics();
  ~SceneJSParserStatistics();
  void computeNode();
  int getNodesCount() const;
  void computeMaterial();
  int getMaterialsCount() const;
  void computeGeometry();
  int getGeometriesCount() const;
  void computeVertex();
  int getVerticesCount() const;
  
  std::string asLogString() const;
  void log() const;
};

#endif
