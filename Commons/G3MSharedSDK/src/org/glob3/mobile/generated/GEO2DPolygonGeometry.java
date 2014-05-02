package org.glob3.mobile.generated; 
//
//  GEO2DPolygonGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

//
//  GEO2DPolygonGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//


//class Geodetic2D;
///#include <vector>
//class Geodetic2D;

//class GEO2DPolygonData;

public class GEO2DPolygonGeometry extends GEOGeometry2D
{
//  std::vector<Geodetic2D*>*               _coordinates;
//  std::vector<std::vector<Geodetic2D*>*>* _holesCoordinatesArray;
  private final GEO2DPolygonData _polygonData;

  protected final java.util.ArrayList<GEOSymbol> createSymbols(GEOSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }


  protected final java.util.ArrayList<GEORasterSymbol> createRasterSymbols(GEORasterSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }

//  GEO2DPolygonGeometry(std::vector<Geodetic2D*>* coordinates,
//                       std::vector<std::vector<Geodetic2D*>*>* holesCoordinatesArray) :
//  _coordinates(coordinates),
//  _holesCoordinatesArray(holesCoordinatesArray)
//  {
//  }

  public GEO2DPolygonGeometry(GEO2DPolygonData polygonData)
  {
     _polygonData = polygonData;
  }


  public void dispose()
  {
    if (_polygonData != null)
       _polygonData.dispose();
  
  //  const int coordinatesCount = _coordinates->size();
  //  for (int i = 0; i < coordinatesCount; i++) {
  //    Geodetic2D* coordinate = _coordinates->at(i);
  //    delete coordinate;
  //  }
  //  delete _coordinates;
  //
  //
  //  if (_holesCoordinatesArray != NULL) {
  //    const int holesCoordinatesArraySize = _holesCoordinatesArray->size();
  //    for (int j = 0; j < holesCoordinatesArraySize; j++) {
  //      const std::vector<Geodetic2D*>* holeCoordinates = _holesCoordinatesArray->at(j);
  //
  //      const int holeCoordinatesCount = holeCoordinates->size();
  //      for (int i =0; i < holeCoordinatesCount; i++) {
  //        const Geodetic2D* holeCoordinate = holeCoordinates->at(i);
  //
  //        delete holeCoordinate;
  //      }
  //
  //      delete holeCoordinates;
  //    }
  //    delete _holesCoordinatesArray;
  //  }
  
    super.dispose();
  }

  public final GEO2DPolygonData getPolygonData()
  {
    return _polygonData;
  }

  public final java.util.ArrayList<Geodetic2D> getCoordinates()
  {
    return _polygonData.getCoordinates();
  }

  public final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> getHolesCoordinatesArray()
  {
    return _polygonData.getHolesCoordinatesArray();
  }

}