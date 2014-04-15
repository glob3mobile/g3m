//
//  GEOVectorialLODRasterSymbolizer.cpp
//  G3MiOSSDK
//
//  Created by fpulido on 09/04/14.
//
//

#include "GEOVectorialLODRasterSymbolizer.hpp"
#include <iostream>
#include "GEO2DLineRasterStyle.hpp"
#include "GEO2DSurfaceRasterStyle.hpp"
#include "CircleShape.hpp"
#include "GEOGeometry.hpp"
#include "GEOFeature.hpp"
#include "GEO2DPointGeometry.hpp"
#include "GEO2DMultiPolygonGeometry.hpp"
#include "GEO2DPolygonGeometry.hpp"
#include "GEO2DMultiLineStringGeometry.hpp"
#include "GEO2DLineStringGeometry.hpp"
#include "JSONObject.hpp"
//#include "Color.hpp"
#include "GEOPolygonRasterSymbol.hpp"
#include "GEOLineRasterSymbol.hpp"
#include "GEOMultiLineRasterSymbol.hpp"
#include "GEOShapeSymbol.hpp"


//GEOVectorialLODRasterSymbolizer::~GEOVectorialLODRasterSymbolizer(){
//    //TODO:??
//}

std::vector<GEORasterSymbol*>* GEOVectorialLODRasterSymbolizer::createRasterSymbols(const GEO2DPointGeometry* geometry) const{
    
//    std::vector<GEORasterSymbol*>* symbols = new std::vector<GEORasterSymbol*>();
//    
//    symbols->push_back( new GEOShapeSymbol( createCircleShape(geometry) ) );
//    
//    return symbols;
    return NULL;
}

std::vector<GEORasterSymbol*>* GEOVectorialLODRasterSymbolizer::createRasterSymbols(const GEO2DLineStringGeometry*      geometry) const{
    
    std::vector<GEORasterSymbol*>* symbols = new std::vector<GEORasterSymbol*>();
    
    symbols->push_back( new GEOLineRasterSymbol(geometry->getCoordinates(),
                                                createLineRasterStyle(geometry)) );
    
    return symbols;
}

std::vector<GEORasterSymbol*>* GEOVectorialLODRasterSymbolizer::createRasterSymbols(const GEO2DMultiLineStringGeometry* geometry) const{
    
    std::vector<GEORasterSymbol*>* symbols = new std::vector<GEORasterSymbol*>();
    
    symbols->push_back( new GEOMultiLineRasterSymbol(geometry->getCoordinatesArray(),
                                                     createLineRasterStyle(geometry)) );
    
    return symbols;
}

std::vector<GEORasterSymbol*>* GEOVectorialLODRasterSymbolizer::createRasterSymbols(const GEO2DPolygonGeometry* geometry) const{
    
    std::vector<GEORasterSymbol*>* symbols = new std::vector<GEORasterSymbol*>();
    
    symbols->push_back( new GEOPolygonRasterSymbol(geometry->getPolygonData(),
                                                   createPolygonLineRasterStyle(geometry),
                                                   createPolygonSurfaceRasterStyle(geometry)) );
    
    return symbols;
    
}

std::vector<GEORasterSymbol*>* GEOVectorialLODRasterSymbolizer::createRasterSymbols(const GEO2DMultiPolygonGeometry* geometry) const{
    
    std::vector<GEORasterSymbol*>* symbols = new std::vector<GEORasterSymbol*>();
    
    const GEO2DLineRasterStyle    lineStyle    = createPolygonLineRasterStyle(geometry);
    const GEO2DSurfaceRasterStyle surfaceStyle = createPolygonSurfaceRasterStyle(geometry);
    
    const std::vector<GEO2DPolygonData*>* polygonsData = geometry->getPolygonsData();
    const int polygonsDataSize = polygonsData->size();
    
    for (int i = 0; i < polygonsDataSize; i++) {
        GEO2DPolygonData* polygonData = polygonsData->at(i);
        symbols->push_back( new GEOPolygonRasterSymbol(polygonData,
                                                       lineStyle,
                                                       surfaceStyle) );
        
    }
    
    return symbols;
}


GEO2DLineRasterStyle GEOVectorialLODRasterSymbolizer::createPolygonLineRasterStyle(const GEOGeometry* geometry) const {
//    const JSONObject* properties = geometry->getFeature()->getProperties();
//    
//    const int colorIndex = (int) properties->getAsNumber("mapcolor7", 0);
//    
//    const Color color = Color::fromRGBA(0.7f, 0, 0, 0.5).wheelStep(7, colorIndex).muchLighter().muchLighter();
    
    const Color color = Color::fromRGBA(0.85f, 0.85f, 0.85f, 1.0f);
    
    float dashLengths[] = {};
    int dashCount = 0;
    
    return GEO2DLineRasterStyle(color,
                                2,
                                CAP_ROUND,
                                JOIN_ROUND,
                                1,
                                dashLengths,
                                dashCount,
                                0);
}

GEO2DSurfaceRasterStyle GEOVectorialLODRasterSymbolizer::createPolygonSurfaceRasterStyle(const GEOGeometry* geometry) const {
//    const JSONObject* properties = geometry->getFeature()->getProperties();
//    
//    const int colorIndex = (int) properties->getAsNumber("mapcolor7", 0);
//    
//    const Color color = Color::fromRGBA(0.7f, 0, 0, 0.5).wheelStep(7, colorIndex);
    
    const Color color = Color::fromRGBA(0.65f, 0.65f, 0.65f, 0.6f);
    
    return GEO2DSurfaceRasterStyle( color );
}

GEO2DLineRasterStyle GEOVectorialLODRasterSymbolizer::createLineRasterStyle(const GEOGeometry* geometry) const {
    const JSONObject* properties = geometry->getFeature()->getProperties();
    
    const std::string type = properties->getAsString("type", "");
    
    float dashLengths[] = {1, 12};
    int dashCount = 2;
    //    float dashLengths[] = {};
    //    int dashCount = 0;
    
    return GEO2DLineRasterStyle(Color::fromRGBA(1, 1, 0, 0.9f),
                                8,
                                CAP_ROUND,
                                JOIN_ROUND,
                                1,
                                dashLengths,
                                dashCount,
                                0);
}

CircleShape* GEOVectorialLODRasterSymbolizer::createCircleShape(const GEO2DPointGeometry* geometry) const {
    const JSONObject* properties = geometry->getFeature()->getProperties();
    
    const double population = properties->getAsNumber("population", 0);
    
    const IMathUtils* mu = IMathUtils::instance();
    
    const double area = population * 1200;
    const float radius = (float) mu->sqrt( area / PI );
    Color color = Color::fromRGBA(1, 1, 0, 1);
    
    return new CircleShape(new Geodetic3D(geometry->getPosition(), 200),
                           RELATIVE_TO_GROUND,
                           radius,
                           color);
}

