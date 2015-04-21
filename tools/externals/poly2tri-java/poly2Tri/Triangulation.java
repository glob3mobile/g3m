

package poly2Tri;

import java.util.List;


public class Triangulation {

   /**
    * Set this to TRUE to obtain file with _name debugFileName with the log about triangulation
    */
   public static boolean debug         = false;

   /**
    * If debug == true file with this _name will be created during triangulation.
    */
   public static String  debugFileName = "polygon_triangulation_log.txt";


   /**
    * numContures == _number of contures of polygon (1 OUTER + n INNER) numVerticesInContures == array numVerticesInContures[x] ==
    * _number of _vertices in x. contures, 0-based _vertices == array of _vertices, each item of array contains doubl[2] ~ {x,y}
    * First conture is OUTER -> _vertices must be COUNTER CLOCKWISE! Other contures must be INNER -> _vertices must be CLOCKWISE!
    * Example: numContures = 1 (1 OUTER CONTURE, 1 INNER CONTURE) numVerticesInContures = { 3, 3 } // triangle with inner triangle
    * as a hol _vertices = { {0, 0}, {7, 0}, {4, 4}, // outer conture, counter clockwise order {2, 2}, {2, 3}, {3, 3} // inner
    * conture, clockwise order }
    * 
    * If error occurs during triangulation, null is returned.
    * 
    * @param numContures
    *           _number of contures of polygon (1 OUTER + n INNER)
    * @param numVerticesInContures
    *           array numVerticesInContures[x] == _number of _vertices in x. contures, 0-based
    * @param _vertices
    *           array of _vertices, each item of array contains doubl[2] ~ {x,y}
    * @return ArrayList of ArrayLists which are _triangles in form of indexes into array _vertices
    * @throws TriangulationException
    */
   public static List<Triangle> triangulate(final int numContures,
                                            final int[] numVerticesInContures,
                                            final double[][] vertices) throws TriangulationException {
      final Polygon p = new Polygon(numContures, numVerticesInContures, vertices);
      if (debug) {
         p.setDebugFile(debugFileName);
         p.setDebugOption(debug);
      }
      else {
         p.setDebugOption(false);
      }
      p.triangulation();
      return p.triangles();
   }
}
