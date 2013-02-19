

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.G3MRenderContext;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.Layer;
import org.glob3.mobile.generated.LayerCondition;
import org.glob3.mobile.generated.Petition;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Tile;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;


public class TileMillLayer
    extends
      Layer {


  private final URL _mapServerURL;
  private final URL _queryServerURL;

  private final String _mapLayer;
  private final String _dataBaseMBTiles;

  private final Sector _sector;

  private final String _format;
  private final String _srs;
  private final String _style;
  private final boolean _isTransparent;

  private String _extraParameter;


  // Example:
  // http://projects.bryanmcbride.com/php-mbtiles-server/mbtiles.php?db=geography-class.mbtiles&z=1&x=1&y=0


  public TileMillLayer(final String mapLayer,
                       final URL mapServerURL,
                       final String dataBaseMBTiles,
                       final Sector sector,
                       final String format,
                       final String srs,
                       final String style,
                       final boolean isTransparent,
                       final LayerCondition condition,
                       final TimeInterval timeToCache) {
    super(condition, mapLayer, timeToCache);
    _mapLayer = mapLayer;
    _mapServerURL = mapServerURL;
    _dataBaseMBTiles = dataBaseMBTiles;
    _queryServerURL = mapServerURL;
    _sector = new Sector(sector);
    _format = format;
    _srs = srs;
    _style = style;
    _isTransparent = isTransparent;
    _extraParameter = "";

  }


  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: java.util.ArrayList<Petition*> getMapPetitions(const
  // G3MRenderContext* rc, const Tile* tile, int width, int height) const
  @Override
  public final java.util.ArrayList<Petition> getMapPetitions(final G3MRenderContext rc,
                                                             final Tile tile,
                                                             final int width,
                                                             final int height) {
    System.out.println("PETITIONS OF TILE: (" + tile.getColumn() + ","
                       + tile.getRow() + ")");


    final java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();

    final Sector tileSector = tile.getSector();
    if (!_sector.touchesWith(tileSector)) {
      return petitions;
    }

    final double basicTileResolution = tileSector.getDeltaLongitude().degrees() / 256;

    final Sector sector = tileSector.intersection(_sector);
    if (sector.getDeltaLatitude().isZero()
        || sector.getDeltaLongitude().isZero()) {
      return petitions;
    }

    // Server name
    final StringBuilder req = new StringBuilder(_mapServerURL.getPath());
    if (req.charAt(req.length() - 1) != '?') {
      req.append('?');
    }

    final StringBuilder reqA = new StringBuilder(req);
    reqA.append("db=");
    reqA.append(_dataBaseMBTiles);
    reqA.append("&z=");
    reqA.append(tile.getLevel() + 1);
    reqA.append("&x=");
    reqA.append(tile.getColumn());
    reqA.append("&y=");
    reqA.append(tile.getRow());

    System.out.println("URL REQUEST: " + reqA.toString());

    final Petition petition = new Petition(sector, new URL(reqA.toString(),
        false), _timeToCache);
    petitions.add(petition);


    // int tileMillLevel = 0;
    // double tileMillResolution = 360 / (IMathUtils.instance().pow(2,
    // tileMillLevel) * 256);
    // while ((tileMillResolution > basicTileResolution) && (tileMillLevel
    // <= 20)) {
    // tileMillResolution = 360 / (IMathUtils.instance().pow(2,
    // tileMillLevel) * 256);
    // tileMillLevel++;
    // }
    //
    // // Regla del 10%
    // if ((basicTileResolution * 0.9 < tileMillResolution)
    // && (tileMillResolution < basicTileResolution * 1.1)) {
    //
    // final crTuple lowerTileXY = getTileCR(tileSector.lower(),
    // tileMillLevel);
    // final crTuple upperTileXY = getTileCR(tileSector.upper(),
    // tileMillLevel);
    //
    // final int deltaX = upperTileXY.c - lowerTileXY.c;
    // final int deltaY = IMathUtils.instance().abs(
    // lowerTileXY.r - upperTileXY.r);
    //
    // for (int x = lowerTileXY.c; x <= lowerTileXY.c + deltaX; x++) {
    // for (int y = lowerTileXY.r; y <= lowerTileXY.r + deltaY; y++) {
    //
    // final int[] tileXY = new int[2];
    // tileXY[0] = x;
    // tileXY[1] = y;
    // final Sector tileMillSector = getTileMillTileAsSector(tileXY,
    // tileMillLevel);
    //
    // System.out.println("tileMillSector: " +
    // tileMillSector.description());
    // if (!tileMillSector.touchesWith(tileSector)) {
    // final StringBuilder reqA = new StringBuilder(req);
    // reqA.append("db=");
    // reqA.append(_dataBaseMBTiles);
    // reqA.append("&z=");
    // reqA.append(tileMillLevel);
    // reqA.append("&x=");
    // reqA.append(x);
    // reqA.append("&y=");
    // reqA.append(y);
    //
    // System.out.println("URL REQUEST: " + reqA.toString());
    //
    // final Petition petition = new Petition(tileMillSector, new URL(
    // reqA.toString(), false), _timeToCache);
    // petitions.add(petition);
    // }
    // }
    // }
    // }
    // final int level = tile.getLevel() + 2;


    // final int x = tile.getColumn();
    // final int y = tile.getRow();
    // ((1 << z) - y - 1)

    return petitions;
  }


  public final crTuple getTileCR(final Geodetic2D latLon,
                                 final int level) {


    final double lonDeg = latLon.longitude()._degrees;

    double latDeg = latLon.latitude()._degrees;
    // TODO: ¿?¿?
    if (latDeg < -85.05112878) {
      latDeg = -85.05112878;
    }
    if (latDeg > 85.05112878) {
      latDeg = 85.05112878;
    }


    final double c = (lonDeg + 180)
                     / (360 / IMathUtils.instance().pow(2, level));

    final double r = (latDeg + 85.05112878)
                     / (180 / IMathUtils.instance().pow(2, (level - 1)));


    return new crTuple((int) c, (int) r);
  }


  public final Sector getTileMillTileAsSector(final int[] tileXY,
                                              final int level) {


    final Geodetic2D topLeft = getLatLon(tileXY, level);
    final int maxTile = ((int) IMathUtils.instance().pow((double) 2,
        (double) level)) - 1;

    final Angle lowerLon = topLeft.longitude();
    final Angle upperLat = topLeft.latitude();

    final int[] tileBelow = new int[2];
    tileBelow[0] = tileXY[0];
    double lowerLatDeg;
    if (tileXY[1] + 1 > maxTile) {
      lowerLatDeg = -85.05112878;
    }
    else {
      tileBelow[1] = tileXY[1] + 1;
      lowerLatDeg = getLatLon(tileBelow, level).latitude()._degrees;
    }


    final int[] tileRight = new int[2];
    double upperLonDeg;
    tileRight[1] = tileXY[1];
    if (tileXY[0] + 1 > maxTile) {
      upperLonDeg = 180.0;
    }
    else {
      tileRight[0] = tileXY[0] + 1;
      upperLonDeg = getLatLon(tileRight, level).longitude()._degrees;
    }

    return new Sector(
        new Geodetic2D(Angle.fromDegrees(lowerLatDeg), lowerLon),
        new Geodetic2D(upperLat, Angle.fromDegrees(upperLonDeg)));

  }


  public final Geodetic2D getLatLon(final int[] tileXY,
                                    final int level) {


    int pixelX = tileXY[0] * 256;
    int pixelY = tileXY[1] * 256;

    // Pixel XY to LatLon
    final int mapSize = 256 << level;
    if (pixelX < 0) {
      pixelX = 0;
    }
    if (pixelY < 0) {
      pixelY = 0;
    }
    if (pixelX > mapSize - 1) {
      pixelX = mapSize - 1;
    }
    if (pixelY > mapSize - 1) {
      pixelY = mapSize - 1;
    }
    final double x = (((double) pixelX) / ((double) mapSize)) - 0.5;
    final double y = 0.5 - (((double) pixelY) / ((double) mapSize));

    final double latDeg = 90.0
                          - 360.0
                          * IMathUtils.instance().atan(
                              IMathUtils.instance().exp(
                                  -y * 2.0 * IMathUtils.instance().pi()))
                          / IMathUtils.instance().pi();
    final double lonDeg = 360.0 * x;

    return new Geodetic2D(Angle.fromDegrees(latDeg), Angle.fromDegrees(lonDeg));

  }


  // bool isTransparent() const{
  // return _isTransparent;
  // }

  // C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in
  // Java:
  // ORIGINAL LINE: URL getFeatureInfoURL(const Geodetic2D& g, const
  // IFactory* factory, const Sector& tileSector, int width, int height)
  // const
  @Override
  public final URL getFeatureInfoURL(final Geodetic2D g,
                                     final IFactory factory,
                                     final Sector tileSector,
                                     final int width,
                                     final int height) {
    if (!_sector.touchesWith(tileSector)) {
      return URL.nullURL();
    }


    return URL.nullURL();
  }


  public final void setExtraParameter(final String extraParameter) {
    _extraParameter = extraParameter;
    notifyChanges();
  }

  public class crTuple {
    // Horizontal tile index
    public int c;
    // Vertical Tile index
    public int r;


    public crTuple(final int pC,
                   final int pR) {
      c = pC;
      r = pR;
    }


    public void dispose() {
    }
  }

}
