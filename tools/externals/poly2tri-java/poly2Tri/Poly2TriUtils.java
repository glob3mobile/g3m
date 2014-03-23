

package poly2Tri;

public class Poly2TriUtils {

   // const double PI=3.141592653589793238462643383279502884197169399375105820974944592308;
   public static final double PI           = Math.PI;

   //enum  Type      { UNKNOWN, INPUT, INSERT, START, END, MERGE, SPLIT, REGULAR_UP, REGULAR_DOWN};
   public static final int    UNKNOWN      = 1;
   public static final int    INPUT        = 2;
   public static final int    INSERT       = 3;
   public static final int    START        = 4;
   public static final int    END          = 5;
   public static final int    MERGE        = 6;
   public static final int    SPLIT        = 7;
   public static final int    REGULAR_UP   = 8;
   public static final int    REGULAR_DOWN = 9;


   public static String typeToString(final int type) {
      switch (type) {
         case Poly2TriUtils.UNKNOWN:
            return "UNKNOWN";
         case Poly2TriUtils.INPUT:
            return "INPUT";
         case Poly2TriUtils.INSERT:
            return "INERT";
         case Poly2TriUtils.START:
            return "START";
         case Poly2TriUtils.END:
            return "END";
         case Poly2TriUtils.MERGE:
            return "MERGE";
         case Poly2TriUtils.SPLIT:
            return "SPLIT";
         case Poly2TriUtils.REGULAR_UP:
            return "REGULAR_UP";
         case Poly2TriUtils.REGULAR_DOWN:
            return "REGULAR_DOWN";
         default:
            return "??? (" + type + ")";
      }
   }


   /*
       class   Pointbase;
       class   Linebase;

   	template <class T, class KeyType> class         SplayTree;
   	typedef map<unsigned int, Pointbase*>           PointbaseMap;
   	typedef map<unsigned int, Linebase*>            LineMap;
   	typedef priority_queue<Pointbase>               PQueue;
   	typedef SplayTree<Linebase*, double>            EdgeBST;
   	typedef list<unsigned int>                      Monopoly;
   	typedef list<Monopoly>                          Monopolys; 
   	typedef vector<unsigned int>                    Triangle;
   	typedef list<Triangle>                          Triangles;
   	typedef map<unsigned int, set<unsigned int> >   AdjEdgeMap;
    */

   //#define sqr(t)  (t)*(t)

   /**
    * In original poly2tri there is an exact arithemtic from Jonathan Shewchuk ... sorry I didn't have time to reimplement that
    * (also I don't know if you can reimplement it 1:1 ...)
    */
   public static double orient2d(final double[] pa,
                                 final double[] pb,
                                 final double[] pc) {
      double detleft, detright;

      detleft = (pa[0] - pc[0]) * (pb[1] - pc[1]);
      detright = (pa[1] - pc[1]) * (pb[0] - pc[0]);

      return detleft - detright;
   }

   public static int l_id = 0; // changed to INT ... because of hash maps
   public static int p_id = 0; // changed to INT ... because of hash maps


   public static void initPoly2TriUtils() {
      l_id = 0;
      p_id = 0;
   }

}
