package org.glob3.mobile.generated;//
//  GEOSymbolizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOSymbol;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DMultiLineStringGeometry;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DLineStringGeometry;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DPointGeometry;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEOObject;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DPolygonGeometry;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GEO2DMultiPolygonGeometry;



public abstract class GEOSymbolizer
{
  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<GEOSymbol*>* createSymbols(const GEO2DPointGeometry* geometry) const = 0;
  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DPointGeometry geometry);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<GEOSymbol*>* createSymbols(const GEO2DLineStringGeometry* geometry) const = 0;
  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DLineStringGeometry geometry);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<GEOSymbol*>* createSymbols(const GEO2DMultiLineStringGeometry* geometry) const = 0;
  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DMultiLineStringGeometry geometry);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<GEOSymbol*>* createSymbols(const GEO2DPolygonGeometry* geometry) const = 0;
  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DPolygonGeometry geometry);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<GEOSymbol*>* createSymbols(const GEO2DMultiPolygonGeometry* geometry) const = 0;
  public abstract java.util.ArrayList<GEOSymbol> createSymbols(GEO2DMultiPolygonGeometry geometry);

}
