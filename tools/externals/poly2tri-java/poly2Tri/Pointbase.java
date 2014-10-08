

package poly2Tri;


public class Pointbase
         implements
            Comparable {

   /**
    * id of point;
    */
   public int     id   = -1;

   /**
    * coordinates;
    */
   public double  x    = 0, y = 0;

   /**
    * type of points;
    */
   //Type            type;            
   public int     type = Poly2TriUtils.UNKNOWN;

   /**
    * left chain or not;
    */
   public boolean left = false;


   public Pointbase() {

   }


   public Pointbase(final Pointbase pb) {
      id = pb.id;
      x = pb.x;
      y = pb.y;
      type = pb.type;
      left = pb.left;
   }


   public Pointbase(final double xx,
                    final double yy) {
      x = xx;
      y = yy;
   }


   public Pointbase(final int idd,
                    final double xx,
                    final double yy) {
      id = idd;
      x = xx;
      y = yy;
   }


   public Pointbase(final double xx,
                    final double yy,
                    final int ttype) {
      id = 0;
      x = xx;
      y = yy;
      type = ttype;
   }


   public Pointbase(final int idd,
                    final double xx,
                    final double yy,
                    final int ttype) {
      id = idd;
      x = xx;
      y = yy;
      type = ttype;
   }


   //rotate a point by angle theta, not used;
   public void rotate(final double theta) {
      final double cosa = Math.cos(theta), sina = Math.sin(theta);
      double newx, newy;
      newx = (x * cosa) - (y * sina);
      newy = (x * sina) + (y * cosa);
      x = newx;
      y = newy;
   }


   public boolean equals(final Pointbase pb) {
      return (x == pb.x) && (y == pb.y);
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      long temp;
      temp = Double.doubleToLongBits(x);
      result = (prime * result) + (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(y);
      result = (prime * result) + (int) (temp ^ (temp >>> 32));
      return result;
   }


   @Override
   public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final Pointbase other = (Pointbase) obj;
      if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) {
         return false;
      }
      if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) {
         return false;
      }
      return true;
   }


   //friend  bool operator>(const Pointbase&, const Pointbase&);
   //friend  bool operator<(const Pointbase&, const Pointbase&);    
   @Override
   public int compareTo(final Object o) {
      // operator>
      // return( (pa.y > pb.y) || ( (pa.y==pb.y) && (pa.x < pb.x)) );
      // operator<
      // return( (pa.y < pb.y) || ( (pa.y==pb.y) && (pa.x > pb.x)) );
      if (!(o instanceof Pointbase)) {
         return -1;
      }
      final Pointbase pb = (Pointbase) o;
      if (this.equals(pb)) {
         return 0;
      }
      if (y > pb.y) {
         return 1;
      }
      else if (y < pb.y) {
         return -1;
      }
      else if (x < pb.x) {
         return 1;
      }
      else {
         return -1;
      }
   }


   //friend  bool operator!=(const Pointbase&, const Pointbase&);
   //substitute with !equals(pointbase)

   //friend  ostream &operator<<(ostream &os, const Pointbase& point);
   @Override
   public String toString() {
      return "Pointbase([" + x + ", " + y + "], ID = " + id + ", " + Poly2TriUtils.typeToString(type) + ")";
   }


}
