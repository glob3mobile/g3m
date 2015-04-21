

package poly2Tri;

import poly2Tri.splayTree.SplayTreeItem;


/**
 * base class for polygon boundary Linebase class is a directed line segment with start/end point
 */
public class Linebase
         implements
            SplayTreeItem {

   /**
    * Was unsigned int! id of a line segment;
    */
   private final int         _id;

   /**
    * two end points;
    */
   private final Pointbase[] _endp   = { null, null };

   /**
    * type of a line segement, input/insert Type...
    */
   private int               _type   = Poly2TriUtils.UNKNOWN;

   /**
    * key of a line segment for splay tree searching
    */
   private double            _key    = 0;

   /**
    * Was unsigned int! helper of a line segemnt
    */
   private int               _helper = -1;


   public Linebase() {
      for (int i = 0; i < 2; i++) {
         _endp[i] = null;
      }
      _id = 0;
   }


   public Linebase(final Pointbase ep1,
                   final Pointbase ep2,
                   final int iType) {
      _endp[0] = ep1;
      _endp[1] = ep2;
      _id = ++Poly2TriUtils.l_id;
      _type = iType;
   }


   public Linebase(final Linebase line) {
      _id = line._id;
      _endp[0] = line._endp[0];
      _endp[1] = line._endp[1];
      _key = line._key;
      _helper = line._helper;
   }


   public int id() {
      return _id;
   }


   public Pointbase endPoint(final int i) {
      return _endp[i];
   }


   public int type() {
      return _type;
   }


   @Override
   public Double keyValue() {
      return _key;
   }


   public void setKeyValue(final double y) {
      if (_endp[1].y == _endp[0].y) {
         _key = _endp[0].x < _endp[1].x ? _endp[0].x : _endp[1].x;
      }
      else {
         _key = (((y - _endp[0].y) * (_endp[1].x - _endp[0].x)) / (_endp[1].y - _endp[0].y)) + _endp[0].x;
      }
   }


   /**
    * reverse a directed line segment; reversable only for inserted diagonals
    */
   public void reverse() {
      assert (_type == Poly2TriUtils.INSERT);
      final Pointbase tmp = _endp[0];
      _endp[0] = _endp[1];
      _endp[1] = tmp;
   }


   /**
    * set and return helper of a directed line segment
    */
   public void setHelper(final int i) {
      _helper = i;
   }


   public int helper() {
      return _helper;
   }


   @Override
   public String toString() {
      final StringBuffer sb = new StringBuffer();
      sb.append("Linebase(");
      sb.append("ID = " + _id);
      sb.append(", " + Poly2TriUtils.typeToString(_type));
      sb.append(", [");
      sb.append(_endp[0]);
      sb.append(", ");
      sb.append(_endp[1]);
      sb.append("], type = " + _type);
      sb.append(", keyValue =" + keyValue());
      return sb.toString();
   }


   /**
    * slightly increased the key to avoid duplicated key for searching tree.
    */
   @Override
   public void increaseKeyValue(final double delta) {
      _key += delta;
   }

}
