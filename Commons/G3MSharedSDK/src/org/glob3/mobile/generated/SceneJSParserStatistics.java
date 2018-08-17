package org.glob3.mobile.generated;import java.util.*;

//
//  SceneJSParserStatistics.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 11/03/13.
//
//

//
//  SceneJSParserStatistics.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 11/03/13.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ILogger;

public class SceneJSParserStatistics
{
  private int _nodesCount;
  private int _materialsCount;
  private int _geometriesCount;
  private int _verticesCount;
  public SceneJSParserStatistics()
  {
	_nodesCount = 0;
	_materialsCount = 0;
	_geometriesCount = 0;
	_verticesCount = 0;
  }
  public void dispose()
  {
  }
  public final void computeNode()
  {
	_nodesCount++;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getNodesCount() const
  public final int getNodesCount()
  {
	return _nodesCount;
  }
  public final void computeMaterial()
  {
	_materialsCount++;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getMaterialsCount() const
  public final int getMaterialsCount()
  {
	return _materialsCount;
  }
  public final void computeGeometry()
  {
	_geometriesCount++;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getGeometriesCount() const
  public final int getGeometriesCount()
  {
	return _geometriesCount;
  }
  public final void computeVertex()
  {
	_verticesCount++;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getVerticesCount() const
  public final int getVerticesCount()
  {
	return _verticesCount;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: String asLogString() const
  public final String asLogString()
  {
	IStringBuilder statsSB = IStringBuilder.newStringBuilder();
  
	statsSB.addString("Nodes=");
	statsSB.addInt(getNodesCount());
	statsSB.addString("; Materials=");
	statsSB.addInt(getMaterialsCount());
	statsSB.addString("; Geometries=");
	statsSB.addInt(getGeometriesCount());
	statsSB.addString("; Vertices=");
	statsSB.addInt(getVerticesCount());
	statsSB.addString("; Vert/Geom=");
	statsSB.addFloat((float) getVerticesCount() / getGeometriesCount());
	statsSB.addString("; Vert/Mat=");
	statsSB.addFloat((float) getVerticesCount() / getMaterialsCount());
	statsSB.addString("; Vert/Nod=");
	statsSB.addFloat((float) getVerticesCount() / getNodesCount());
  
	String stats = statsSB.getString();
	if (statsSB != null)
		statsSB.dispose();
  
	return stats;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void log() const
  public final void log()
  {
	if (ILogger.instance() != null)
	{
	  ILogger.instance().logInfo("\nSceneJSParserStatistics: %s", asLogString().c_str());
	}
  }
}
