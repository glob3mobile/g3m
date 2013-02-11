

package org.glob3.mobile.specific;

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
import org.glob3.mobile.generated.xyTuple;


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

    final int level = tile.getLevel() + 2;

    final xyTuple lowerTileXY = getTileXY(tileSector.lower(), level);
    final xyTuple upperTileXY = getTileXY(tileSector.upper(), level);

    final int deltaX = upperTileXY.x - lowerTileXY.x;
    final int deltaY = lowerTileXY.y - upperTileXY.y;

    for (int x = lowerTileXY.x; x <= lowerTileXY.x + deltaX; x++) {
      for (int y = upperTileXY.y; y <= upperTileXY.y + deltaY; y++) {

        final StringBuilder reqA = new StringBuilder(req);
        reqA.append("db=");
        reqA.append(_dataBaseMBTiles);
        reqA.append("&z=");
        reqA.append(level);
        reqA.append("&x=");
        reqA.append(x);
        reqA.append("&y=");
        reqA.append(y);

        System.out.println("URL REQUEST: " + reqA.toString());

        final Petition petition = new Petition(sector, new URL(
            reqA.toString(), false), _timeToCache);
        petitions.add(petition);


      }

    }

    final int x = tile.getColumn();
    final int y = tile.getRow();
    // ((1 << z) - y - 1)

    return petitions;
  }


  public final xyTuple getTileXY(final Geodetic2D latLon,
                                 final int level) {

    // LatLon to Pixels XY
    final int mapSize = 256 << level;
    final double lonDeg = latLon.longitude()._degrees;
    double latDeg = latLon.latitude()._degrees;
    if (latDeg < -85.05112878) {
      latDeg = -85.05112878;
    }
    if (latDeg > 85.05112878) {
      latDeg = 85.05112878;
    }

    double x = (lonDeg + 180.0) / 360;
    final double sinLat = IMathUtils.instance().sin(
        latDeg * IMathUtils.instance().pi() / 180.0);
    double y = 0.5 - IMathUtils.instance().log((1 + sinLat) / (1 - sinLat))
               / (4.0 * IMathUtils.instance().pi());

    x = x * mapSize + 0.5;
    y = y * mapSize + 0.5;


    if (x < 0) {
      x = 0;
    }
    if (y < 0) {
      y = 0;
    }
    if (x > (mapSize - 1)) {
      x = mapSize - 1;
    }
    if (y > (mapSize - 1)) {
      y = mapSize - 1;
    }

    final int pixelX = (int) x;
    final int pixelY = (int) y;

    // Pixel XY to Tile XY
    final int tileX = pixelX / 256;
    final int tileY = pixelY / 256;

    final xyTuple tileXY = new xyTuple();

    tileXY.x = tileX;
    tileXY.y = tileY;

    return tileXY;
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

}
