

package poly2Tri;

public class Triangle {

   public final int _vertex0;
   public final int _vertex1;
   public final int _vertex2;


   Triangle(final int vertex0,
            final int vertex1,
            final int vertex2) {
      _vertex0 = vertex0;
      _vertex1 = vertex1;
      _vertex2 = vertex2;
   }


   public int get(final int i) {
      switch (i) {
         case 0:
            return _vertex0;
         case 1:
            return _vertex1;
         case 2:
            return _vertex2;
         default:
            throw new IndexOutOfBoundsException();
      }
   }


}
