

package org.glob3.mobile.tools;

import java.util.ArrayList;
import java.util.List;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Sector;


public class SectorVec
         extends
            Sector {

   public SectorVec _parent;
   public int       _level;
   public int       _row;
   public int       _column;


   public SectorVec(final Geodetic2D lower,
                    final Geodetic2D upper,
                    final SectorVec parent,
                    final int level,
                    final int row,
                    final int column) {

      super(lower, upper);
      _parent = parent;
      _level = level;
      _row = row;
      _column = column;
   }


   public SectorVec(final Sector sector,
                    final SectorVec parent,
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


   public List<SectorVec> getSubsectors() {

      final List<SectorVec> subSectors = new ArrayList<SectorVec>(4);
      final int subLevel = this._level + 1;

      int rowInc = this._row - 1;
      int columnInc = this._column - 1;
      final SectorVec s11 = new SectorVec(this._lower, this._center, this, subLevel, this._row + rowInc, this._column + columnInc);

      rowInc = this._row - 1;
      columnInc = this._column;
      final SectorVec s12 = new SectorVec(new Geodetic2D(this._lower._latitude, this._center._longitude), new Geodetic2D(
               this._center._latitude, this._upper._longitude), this, subLevel, this._row + rowInc, this._column + columnInc);
      rowInc = this._row;
      columnInc = this._column - 1;
      final SectorVec s21 = new SectorVec(new Geodetic2D(this._center._latitude, this._lower._longitude), new Geodetic2D(
               this._upper._latitude, this._center._longitude), this, subLevel, this._row + rowInc, this._column + columnInc);
      rowInc = this._row;
      columnInc = this._column;
      final SectorVec s22 = new SectorVec(this._center, this._upper, this, subLevel, this._row + rowInc, this._column + columnInc);

      subSectors.add(s11);
      subSectors.add(s12);
      subSectors.add(s21);
      subSectors.add(s22);

      return subSectors;
   }


   @Override
   public String toString() {
      return "SectorVec [_parent=" + _parent + ", _level=" + _level + ", _row=" + _row + ", _column=" + _column + ", _lower="
             + toStringGeodetic2D(_lower) + ", _upper=" + toStringGeodetic2D(_upper) + ", _center=" + toStringGeodetic2D(_center)
             + ", _deltaLatitude=" + toStringAngle(_deltaLatitude) + ", _deltaLongitude=" + toStringAngle(_deltaLongitude) + "]";
   }


   public String toStringGeodetic2D(final Geodetic2D g) {

      return "(" + g._latitude._degrees + ", " + g._longitude._degrees + ")";
   }


   public String toStringAngle(final Angle a) {

      return "(" + a._degrees + ")";
   }

}
