

package org.glob3.mobile.tools;

import java.util.ArrayList;
import java.util.List;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.LayerTilesRenderParameters;
import org.glob3.mobile.generated.MercatorUtils;
import org.glob3.mobile.generated.Sector;


public class TileSector
         extends
            Sector {

   private final static Angle     POS180             = Angle.fromDegrees(180.0);
   private final static Angle     NEG180             = Angle.fromDegrees(-180.0);
   private final static Angle     POS360             = Angle.fromDegrees(360.0);

   public final static TileSector FULL_SPHERE_SECTOR = new TileSector(Sector.fullSphere(), null, 0, 0, 0);
   private final static Sector    EMPTY_SECTOR       = Sector.fromDegrees(0, 0, 0, 0);

   public TileSector              _parent;
   public int                     _level;
   public int                     _row;
   public int                     _column;


   public TileSector(final Geodetic2D lower,
                     final Geodetic2D upper,
                     final TileSector parent,
                     final int level,
                     final int row,
                     final int column) {

      super(lower, upper);
      _parent = parent;
      _level = level;
      _row = row;
      _column = column;
   }


   public TileSector(final Sector sector,
                     final TileSector parent,
                     final int level,
                     final int row,
                     final int column) {

      super(sector);
      _parent = parent;
      _level = level;
      _row = row;
      _column = column;
   }


   public Sector getSector() {
      return this;
   }


   public int getRow(final LayerTilesRenderParameters renderParameters) {

      int row = this._row;
      if (renderParameters._mercator) {
         final int numRows = (int) (renderParameters._topSectorSplitsByLatitude * IMathUtils.instance().pow(2.0, this._level));
         row = numRows - this._row - 1;
      }
      return row;
   }


   public boolean intersects(final TileSector thatSector) {

      return (!this.intersection(thatSector).equals(EMPTY_SECTOR));
   }


   //   public Sector getExtendedSector(final double tolerance) {
   //
   //      final Geodetic2D geodeticDelta = new Geodetic2D(this._deltaLatitude.times(tolerance), this._deltaLongitude.times(tolerance));
   //      final Geodetic2D extendedLower = this._lower.sub(geodeticDelta);
   //      final Geodetic2D extendedUpper = this._upper.add(geodeticDelta);
   //
   //      return new Sector(extendedLower, extendedUpper);
   //
   //      //      final TileSector result = new TileSector(extendedLower, extendedUpper, this._parent, this._level, this._row, this._column);
   //      //      System.out.println("SECTOR: " + this.toString());
   //      //      System.out.println("EXTENDED SECTOR: " + result.toString());
   //      //      return result;
   //   }

   public List<Sector> getExtendedSector(final double overlapPercentage) {

      return TileSector.getExtendedSector(this, overlapPercentage);
   }


   public static final double getAngularAreaInSquaredDegrees(final List<Sector> extendedSector) {

      double area = 0.0;
      for (final Sector s : extendedSector) {
         area += s.getAngularAreaInSquaredDegrees();
      }

      return area;
   }


   public final TileSector mergedWith(final TileSector that) {
      final Sector pepe = super.mergedWith(that);

      //return pepe;
      return new TileSector(pepe, this._parent, this._level, this._row, this._column);
   }


   public static List<Sector> getExtendedSector(final Sector sector,
                                                final double overlapPercentage) {

      final ArrayList<Sector> sectorList = new ArrayList<Sector>();

      if (overlapPercentage == 0) {
         sectorList.add(sector);
         return sectorList;
      }

      final Geodetic2D geodeticDelta = new Geodetic2D(sector._deltaLatitude.times(overlapPercentage / 100.0),
               sector._deltaLongitude.times(overlapPercentage / 100.0));
      final Geodetic2D extendedLower = sector._lower.sub(geodeticDelta);
      final Geodetic2D extendedUpper = sector._upper.add(geodeticDelta);

      if (extendedLower._longitude.lowerThan(NEG180)) {
         final Angle extendedLowerLon1 = POS360.add(extendedLower._longitude);

         final Geodetic2D extendedLower1 = new Geodetic2D(extendedLower._latitude, extendedLowerLon1);
         final Geodetic2D extendedUpper1 = new Geodetic2D(extendedUpper._latitude, POS180);
         final Sector extendSector1 = new Sector(extendedLower1, extendedUpper1);
         sectorList.add(extendSector1);

         final Geodetic2D extendedLower2 = new Geodetic2D(extendedLower._latitude, NEG180);
         final Geodetic2D extendedUpper2 = new Geodetic2D(extendedUpper._latitude, extendedUpper._longitude);
         final Sector extendSector2 = new Sector(extendedLower2, extendedUpper2);
         sectorList.add(extendSector2);
      }
      else if (extendedUpper._longitude.greaterThan(POS180)) {
         final Angle extendedUpperLon2 = extendedUpper._longitude.sub(POS360);

         final Geodetic2D extendedLower1 = new Geodetic2D(extendedLower._latitude, extendedLower._longitude);
         final Geodetic2D extendedUpper1 = new Geodetic2D(extendedUpper._latitude, POS180);
         final Sector extendedSector1 = new Sector(extendedLower1, extendedUpper1);
         sectorList.add(extendedSector1);

         final Geodetic2D extendedLower2 = new Geodetic2D(extendedLower._latitude, NEG180);
         final Geodetic2D extendedUpper2 = new Geodetic2D(extendedUpper._latitude, extendedUpperLon2);
         final Sector extendedSector2 = new Sector(extendedLower2, extendedUpper2);
         sectorList.add(extendedSector2);
      }
      else {
         final Sector extendedSector = new Sector(extendedLower, extendedUpper);
         sectorList.add(extendedSector);
      }

      return sectorList;
   }


   public final ArrayList<TileSector> getSubTileSectors(final boolean mercator) {

      final Angle splitLongitude = Angle.midAngle(_lower._longitude, _upper._longitude);

      final Angle splitLatitude = mercator ? MercatorUtils.calculateSplitLatitude(_lower._latitude, _upper._latitude)
                                          : Angle.midAngle(_lower._latitude, _upper._latitude);

      return createSubTileSectors(splitLatitude, splitLongitude);
   }


   private final ArrayList<TileSector> createSubTileSectors(final Angle splitLatitude,
                                                            final Angle splitLongitude) {

      final int nextLevel = _level + 1;

      final int row2 = 2 * _row;
      final int column2 = 2 * _column;

      final ArrayList<TileSector> subSectors = new ArrayList<TileSector>();

      final TileSector s00 = createSubTileSector(_lower._latitude, _lower._longitude, splitLatitude, splitLongitude, nextLevel,
               row2, column2);
      subSectors.add(s00);

      final TileSector s01 = createSubTileSector(_lower._latitude, splitLongitude, splitLatitude, _upper._longitude, nextLevel,
               row2, column2 + 1);
      subSectors.add(s01);

      final TileSector s10 = createSubTileSector(splitLatitude, _lower._longitude, _upper._latitude, splitLongitude, nextLevel,
               row2 + 1, column2);
      subSectors.add(s10);

      final TileSector s11 = createSubTileSector(splitLatitude, splitLongitude, _upper._latitude, _upper._longitude, nextLevel,
               row2 + 1, column2 + 1);
      subSectors.add(s11);

      return subSectors;
   }


   private TileSector createSubTileSector(final Angle lowerLat,
                                          final Angle lowerLon,
                                          final Angle upperLat,
                                          final Angle upperLon,
                                          final int subLevel,
                                          final int row,
                                          final int column) {

      return new TileSector(new Geodetic2D(lowerLat, lowerLon), new Geodetic2D(upperLat, upperLon), this, subLevel, row, column);
   }


   //   public List<TileSector> getSubTileSectors() {
   //
   //      final List<TileSector> subSectors = new ArrayList<TileSector>(4);
   //      final int subLevel = this._level + 1;
   //
   //      int rowInc = this._row;
   //      int columnInc = this._column;
   //      final TileSector s00 = new TileSector(this._lower, this._center, this, subLevel, this._row + rowInc, this._column
   //                                                                                                           + columnInc);
   //
   //      rowInc = this._row;
   //      columnInc = this._column + 1;
   //      final TileSector s01 = new TileSector(new Geodetic2D(this._lower._latitude, this._center._longitude), new Geodetic2D(
   //               this._center._latitude, this._upper._longitude), this, subLevel, this._row + rowInc, this._column + columnInc);
   //
   //      rowInc = this._row + 1;
   //      columnInc = this._column;
   //      final TileSector s10 = new TileSector(new Geodetic2D(this._center._latitude, this._lower._longitude), new Geodetic2D(
   //               this._upper._latitude, this._center._longitude), this, subLevel, this._row + rowInc, this._column + columnInc);
   //
   //      rowInc = this._row + 1;
   //      columnInc = this._column + 1;
   //      final TileSector s11 = new TileSector(this._center, this._upper, this, subLevel, this._row + rowInc, this._column
   //                                                                                                           + columnInc);
   //
   //      subSectors.add(s00);
   //      subSectors.add(s01);
   //      subSectors.add(s10);
   //      subSectors.add(s11);
   //
   //      return subSectors;
   //   }

   //   public List<SectorVec> getSubsectors() {
   //
   //      final List<SectorVec> subSectors = new ArrayList<SectorVec>(4);
   //      final int subLevel = this._level + 1;
   //
   //      int rowInc = this._row - 1;
   //      int columnInc = this._column - 1;
   //      final SectorVec s11 = new SectorVec(this._lower, this._center, this, subLevel, this._row + rowInc, this._column + columnInc);
   //
   //      rowInc = this._row - 1;
   //      columnInc = this._column;
   //      final SectorVec s12 = new SectorVec(new Geodetic2D(this._lower._latitude, this._center._longitude), new Geodetic2D(
   //               this._center._latitude, this._upper._longitude), this, subLevel, this._row + rowInc, this._column + columnInc);
   //      rowInc = this._row;
   //      columnInc = this._column - 1;
   //      final SectorVec s21 = new SectorVec(new Geodetic2D(this._center._latitude, this._lower._longitude), new Geodetic2D(
   //               this._upper._latitude, this._center._longitude), this, subLevel, this._row + rowInc, this._column + columnInc);
   //      rowInc = this._row;
   //      columnInc = this._column;
   //      final SectorVec s22 = new SectorVec(this._center, this._upper, this, subLevel, this._row + rowInc, this._column + columnInc);
   //
   //      subSectors.add(s11);
   //      subSectors.add(s12);
   //      subSectors.add(s21);
   //      subSectors.add(s22);
   //
   //      return subSectors;
   //   }


   @Override
   public String toString() {
      return "TileSector [_level=" + _level + ", _row=" + _row + ", _column=" + _column + ", _lower="
             + toStringGeodetic2D(_lower) + ", _upper=" + toStringGeodetic2D(_upper) + ", _center=" + toStringGeodetic2D(_center)
             + ", _deltaLatitude=" + toStringAngle(_deltaLatitude) + ", _deltaLongitude=" + toStringAngle(_deltaLongitude) + "]";
   }


   public String label() {
      return "TileSector [_level=" + _level + ", _row=" + _row + ", _column=" + _column + "]";
   }


   public String toStringGeodetic2D(final Geodetic2D g) {

      return "(" + g._latitude._degrees + ", " + g._longitude._degrees + ")";
   }


   public String toStringAngle(final Angle a) {

      return "(" + a._degrees + ")";
   }

}
