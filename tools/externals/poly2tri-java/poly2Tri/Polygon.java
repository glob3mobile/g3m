

package poly2Tri;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import poly2Tri.splayTree.BTreeNode;
import poly2Tri.splayTree.SplayTree;


/**
 * Merged with BDMFile (Boundary Mesh File)
 */
public class Polygon {

   /**
    * Was unsigned int! Number of _contours.
    */
   protected int                            _ncontours       = 0;

   /**
    * vector<unsigned int> _nVertices; //
    */
   protected int[]                          _nVertices       = null;

   /**
    * typedef map<unsigned int, Pointbase*> PointbaseMap; all _vertices ... is needed as normal array, map in C++ code probably
    * because od adding into map ... see _pointsKeys
    */
   protected HashMap<Integer, Pointbase>    _points          = new HashMap<Integer, Pointbase>();

   /**
    * Initialized in initialize() method ... _number of points doesn't change during iteration - all iteration in C++ code are
    * done from smaller to bigger ... HashMap.keySet().iterator() isn't returning keys in natural order.
    */
   protected int[]                          _pointsKeys      = null;

   /**
    * typedef map<unsigned int, Linebase*> LineMap; all edges
    */
   protected HashMap<Integer, Linebase>     _edges           = new HashMap<Integer, Linebase>();

   /**
    * See _pointsKeys ... same for _edges. ---{ Today, it's you }--- Terry Pratchett's Death Right now I'm not sure wether _number
    * of edges can't change... ... better call initializeEdgesKeys() all the time ;)
    */
   protected int[]                          _edgesKeys       = null;

   /**
    * typedef priority_queue<Pointbase> PQueue; ... use PointbaseComparatorCoordinatesReverse! (Jakub Gemrot) priority queue for
    * event points
    */
   private final PriorityQueue<Pointbase>   _qpoints         = new PriorityQueue<Pointbase>(30,
                                                                      new PointbaseComparatorCoordinatesReverse());

   /**
    * typedef SplayTree<Linebase*, double> EdgeBST; edge binary searching tree (splaytree)
    */
   private final SplayTree                  _edgebst         = new SplayTree();

   /**
    * typedef list<Monopoly> Monopolys; typedef list<unsigned int> Monopoly; all monotone polygon piece list;
    */
   private final List<List<Integer>>        _mpolys          = new ArrayList<List<Integer>>();

   /**
    * all triangle list; typedef list<Triangle> Triangles; typedef vector<unsigned int> Triangle;
    */
   //   private final List<List<Integer>>        _triangles       = new ArrayList<List<Integer>>();
   private final List<Triangle>             _triangles       = new ArrayList<Triangle>();

   /**
    * typedef map<unsigned int, set<unsigned int> > AdjEdgeMap; data for monotone piece searching purpose;
    */
   private final Map<Integer, Set<Integer>> _startAdjEdgeMap = new HashMap<Integer, Set<Integer>>();

   /**
    * typedef map<unsigned int, Linebase*> LineMap; added diagonals to partition polygon to monotont pieces, not all diagonals of
    * given polygon
    */
   private final Map<Integer, Linebase>     _diagonals       = new HashMap<Integer, Linebase>();

   /**
    * debug option;
    */
   private boolean                          _debug           = false;

   /**
    * log file for debug purpose;
    */
   private FileWriter                       _logfile         = null;

   /**
    * This is used to change key of all items in SplayTree.
    */
   private final UpdateKey                  updateKey        = new UpdateKey();

   /**
    * If _debug == true, file with this _name will be used to log the messages.
    */
   private String                           _debugFileName   = "polygon_triangulation_log.txt";


   public HashMap<Integer, Pointbase> points() {
      return _points;
   }


   public HashMap<Integer, Linebase> edges() {
      return _edges;
   }


   /**
    * For params see contructor Polygon(int, int[], double[][])
    * 
    * @param _numContours
    * @param numVerticesInContures
    * @param _vertices
    * 
    *           ---{ CLEAR }---
    */
   private void initPolygon(final int numContours,
                            final int[] numVerticesInContours,
                            final double[][] vertices) {
      int i, j;
      int nextNumber = 1;

      _ncontours = numContours;
      _nVertices = new int[_ncontours];
      for (i = 0; i < numContours; ++i) {
         for (j = 0; j < numVerticesInContours[i]; ++j) {
            _points.put(nextNumber, new Pointbase(nextNumber, vertices[nextNumber - 1][0], vertices[nextNumber - 1][1],
                     Poly2TriUtils.INPUT));
            ++nextNumber;
         }
      }
      _nVertices[0] = numVerticesInContours[0];
      for (i = 1; i < _ncontours; ++i) {
         _nVertices[i] = _nVertices[i - 1] + numVerticesInContours[i];
      }
      i = 0;
      j = 1;
      int first = 1;
      Linebase edge;

      while (i < _ncontours) {
         for (; (j + 1) <= _nVertices[i]; ++j) {
            edge = new Linebase(_points.get(j), _points.get(j + 1), Poly2TriUtils.INPUT);
            _edges.put(Poly2TriUtils.l_id, edge);
         }
         edge = new Linebase(_points.get(j), _points.get(first), Poly2TriUtils.INPUT);
         _edges.put(Poly2TriUtils.l_id, edge);

         j = _nVertices[i] + 1;
         first = _nVertices[i] + 1;
         ++i;
      }
      Poly2TriUtils.p_id = _nVertices[_ncontours - 1];
   }


   /**
    * numContures == _number of contures of polygon (1 OUTER + n INNER) numVerticesInContures == array numVerticesInContures[x] ==
    * _number of _vertices in x. contures, 0-based _vertices == array of _vertices, each item of array contains doubl[2] ~ {x,y}
    * First conture is OUTER -> _vertices must be COUNTER CLOCKWISE! Other contures must be INNER -> _vertices must be CLOCKWISE!
    * Example: numContures = 1 (1 OUTER CONTURE, 1 INNER CONTURE) numVerticesInContures = { 3, 3 } // triangle with inner triangle
    * as a hol _vertices = { {0, 0}, {7, 0}, {4, 4}, // outer conture, counter clockwise order {2, 2}, {2, 3}, {3, 3} // inner
    * conture, clockwise order }
    * 
    * @param numContures
    *           _number of contures of polygon (1 OUTER + n INNER)
    * @param numVerticesInContures
    *           array numVerticesInContures[x] == _number of _vertices in x. contures, 0-based
    * @param _vertices
    *           array of _vertices, each item of array contains doubl[2] ~ {x,y}
    */
   Polygon(final int numContures,
           final int[] numVerticesInContures,
           final double[][] vertices) {
      Poly2TriUtils.initPoly2TriUtils();
      initPolygon(numContures, numVerticesInContures, vertices);
      initializate();
      _debug = false;
   }


   public void writeToLog(final String s) {
      if (!_debug) {
         return;
      }
      try {
         _logfile.write(s);
      }
      catch (final IOException e) {
         _debug = false;
         System.out.println("Writing to LogFile (debugging) failed.");
         e.printStackTrace();
         System.out.println("Setting _debug = false, continuing the work.");
      }
   }


   public Pointbase getPoint(final int index) {
      return _points.get(index);
   }


   public Linebase getEdge(final int index) {
      return _edges.get(index);
   }


   public Pointbase qpointsTop() {
      return _qpoints.peek();
   }


   public Pointbase qpointsPop() {
      return _qpoints.poll();
   }


   public boolean is_exist(final double x,
                           final double y) {
      final Iterator<Integer> iter = _points.keySet().iterator();
      Pointbase pb;
      while (iter.hasNext()) {
         pb = getPoint(iter.next());
         if ((pb.x == x) && (pb.y == y)) {
            return true;
         }
      }
      return false;
   }


   /**
    * return the previous point (or edge) id for a given ith point (or edge);
    * 
    * was all UNSIGNED (ints)
    */
   private int prev(final int i) {
      int j = 0, prevLoop = 0, currentLoop = 0;

      while (i > _nVertices[currentLoop]) {
         prevLoop = currentLoop;
         currentLoop++;
      }

      if ((i == 1) || (i == (_nVertices[prevLoop] + 1))) {
         j = _nVertices[currentLoop];
      }
      else if (i <= _nVertices[currentLoop]) {
         j = i - 1;
      }

      return j;
   }


   /**
    * return the next point (or edge) id for a given ith point (or edge); was all UNSIGNED!
    */
   private int next(final int i) {
      int j = 0, prevLoop = 0, currentLoop = 0;

      while (i > _nVertices[currentLoop]) {
         prevLoop = currentLoop;
         currentLoop++;
      }

      if (i < _nVertices[currentLoop]) {
         j = i + 1;
      }
      else if (i == _nVertices[currentLoop]) {
         if (currentLoop == 0) {
            j = 1;
         }
         else {
            j = _nVertices[prevLoop] + 1;
         }
      }

      return j;
   }


   /**
    * rotate input polygon by angle theta, not used;
    */
   //   private void rotate(final double theta) {
   //      for (final int _pointsKey : _pointsKeys) {
   //         (getPoint(_pointsKey)).rotate(theta);
   //      }
   //   }


   private int[] getSorted(final Set<Integer> s) {
      final Object[] temp = s.toArray();
      final int[] result = new int[temp.length];
      for (int i = 0; i < temp.length; ++i) {
         result[i] = ((Integer) temp[i]).intValue();
      }
      Arrays.sort(result);
      return result;
   }


   private void initializePointsKeys() {
      _pointsKeys = getSorted(_points.keySet());
   }


   //   private void initializeEdgesKeys() {
   //      _edgesKeys = getSorted(_edges.keySet());
   //   }


   private Set<Integer> getSetFromStartAdjEdgeMap(final int index) {
      Set<Integer> s = _startAdjEdgeMap.get(index);
      if (s != null) {
         return s;
      }
      s = new HashSet<Integer>();
      _startAdjEdgeMap.put(index, s);
      return s;
   }


   /**
    * polygon initialization; to find types of all polygon _vertices; create a priority queue for all _vertices; construct an edge
    * set for each vertex (the set holds all edges starting from the vertex, only for loop searching purpose).
    * 
    * ---{ CLEAR }---
    */
   private void initializate() {
      initializePointsKeys();

      int id, idp, idn;
      Pointbase p, pnext, pprev; // was Pointbase p, pnext, pprev; ... COPY CONTRUCTOR whenever =
      double area;

      for (final int pointsKey : _pointsKeys) {
         id = pointsKey;
         idp = prev(id);
         idn = next(id);

         p = getPoint(id);
         pnext = getPoint(idn);
         pprev = getPoint(idp);

         if ((p.compareTo(pnext) > 0) && (pprev.compareTo(p) > 0)) {
            p.type = Poly2TriUtils.REGULAR_DOWN;
         }
         else if ((p.compareTo(pprev) > 0) && (pnext.compareTo(p) > 0)) {
            p.type = Poly2TriUtils.REGULAR_UP;
         }
         else {
            area = Poly2TriUtils.orient2d(new double[] { pprev.x, pprev.y }, new double[] { p.x, p.y }, new double[] { pnext.x,
                     pnext.y });

            if ((pprev.compareTo(p) > 0) && (pnext.compareTo(p) > 0)) {
               p.type = (area > 0) ? Poly2TriUtils.END : Poly2TriUtils.MERGE;
            }
            if ((pprev.compareTo(p) < 0) && (pnext.compareTo(p) < 0)) {
               p.type = (area > 0) ? Poly2TriUtils.START : Poly2TriUtils.SPLIT;
            }
         }

         // C++ code: _qpoints.push(*(it.second));
         // must use copy constructor!
         _qpoints.add(new Pointbase(p));

         getSetFromStartAdjEdgeMap(id).add(id);
      }
   }


   /**
    * Add a diagonal from point id _i to j
    * 
    * C++ code: was all unsigned (_i,j)
    */
   private void addDiagonal(final int i,
                            final int j) {
      final int type = Poly2TriUtils.INSERT;

      final Linebase diag = new Linebase(getPoint(i), getPoint(j), type);
      _edges.put(diag.id(), diag);

      getSetFromStartAdjEdgeMap(i).add(diag.id());
      getSetFromStartAdjEdgeMap(j).add(diag.id());

      _diagonals.put(diag.id(), diag);

      writeToLog("Add Diagonal from " + i + " to " + j + "\n");
   }


   /**
    * handle event vertext according to vertex type; Handle start vertex
    * 
    * C++ code: was all UNSIGNED
    */
   private void handleStartVertex(final int i) {

      final double y = _points.get(i).y;

      _edgebst.inOrder(updateKey, y); // ya ... some special things happens ... see Linebase.setKeyValue()

      final Linebase edge = getEdge(i);
      edge.setHelper(i);
      edge.setKeyValue(y);

      _edgebst.insert(edge);

      if (_debug) {
         writeToLog("set e" + i + " helper to " + i + "\n");
         writeToLog("Insert e" + i + " to splay tree\n");
         writeToLog("key:" + edge.keyValue() + "\n");
      }
   }


   /**
    * handle event vertext according to vertex type; Handle end vertex
    * 
    * C++ code: param _i was unsigned
    */
   private void handleEndVertex(final int i) {
      final double y = getPoint(i).y;

      _edgebst.inOrder(updateKey, y);

      final int previ = prev(i);
      final Linebase edge = getEdge(previ);
      final int helper = edge.helper();

      if (getPoint(helper).type == Poly2TriUtils.MERGE) {
         addDiagonal(i, helper);
      }
      _edgebst.delete(edge.keyValue());

      if (_debug) {
         writeToLog("Remove e" + previ + " from splay tree\n");
         writeToLog("key:" + edge.keyValue() + "\n");
      }
   }


   /**
    * handle event vertext according to vertex type; Handle split vertex C++ code: _i was unsigned, helper was unsigned
    */
   private void handleSplitVertex(final int i) {
      final Pointbase point = getPoint(i);
      final double x = point.x, y = point.y;

      _edgebst.inOrder(updateKey, y);

      final BTreeNode leftnode = _edgebst.findMaxSmallerThan(x);
      final Linebase leftedge = (Linebase) leftnode.data();

      final int helper = leftedge.helper();
      addDiagonal(i, helper);

      if (_debug) {
         writeToLog("Search key:" + x + " edge key:" + leftedge.keyValue() + "\n");
         writeToLog("e" + leftedge.id() + " is directly left to v" + i + "\n");
         writeToLog("Set e" + leftedge.id() + " helper to " + i + "\n");
         writeToLog("set e" + i + " helper to " + i + "\n");
         writeToLog("Insert e" + i + " to splay tree\n");
         writeToLog("Insert key:" + getEdge(i).keyValue() + "\n");
      }

      leftedge.setHelper(i);
      final Linebase edge = getEdge(i);
      edge.setHelper(i);
      edge.setKeyValue(y);
      _edgebst.insert(edge);
   }


   /**
    * handle event vertext according to vertex type; Handle merge vertex C++ code: _i was unsigned, previ + helper also unsigned
    */
   private void handleMergeVertex(final int i) {
      final Pointbase point = getPoint(i);
      final double x = point.x, y = point.y;

      _edgebst.inOrder(updateKey, y);

      final int previ = prev(i);
      final Linebase previEdge = getEdge(previ);
      int helper = previEdge.helper();

      Pointbase helperPoint = getPoint(helper);

      if (helperPoint.type == Poly2TriUtils.MERGE) {
         addDiagonal(i, helper);
      }

      _edgebst.delete(previEdge.keyValue());

      if (_debug) {
         writeToLog("e" + previ + " helper is " + helper + "\n");
         writeToLog("Remove e" + previ + " from splay tree.\n");
      }

      final BTreeNode leftnode = _edgebst.findMaxSmallerThan(x);
      final Linebase leftedge = (Linebase) leftnode.data();

      helper = leftedge.helper();
      helperPoint = getPoint(helper);
      if (helperPoint.type == Poly2TriUtils.MERGE) {
         addDiagonal(i, helper);
      }

      leftedge.setHelper(i);

      if (_debug) {
         writeToLog("Search key:" + x + " found:" + leftedge.keyValue() + "\n");
         writeToLog("e" + leftedge.id() + " is directly left to v" + i + "\n");
         writeToLog("Set e" + leftedge.id() + " helper to " + i + "\n");
      }
   }


   /**
    * handle event vertext according to vertex type; Handle regular down vertex C++ code: _i was unsigned, previ + helper also
    * unsigned
    */
   private void handleRegularVertexDown(final int i) {
      final Pointbase point = getPoint(i);

      final double y = point.y;

      _edgebst.inOrder(updateKey, y);

      final int previ = prev(i);

      final Linebase previEdge = getEdge(previ);

      final int helper = previEdge.helper();

      final Pointbase helperPoint = getPoint(helper);

      if (helperPoint.type == Poly2TriUtils.MERGE) {
         addDiagonal(i, helper);
      }

      _edgebst.delete(previEdge.keyValue());

      final Linebase edge = getEdge(i);
      edge.setHelper(i);
      edge.setKeyValue(y);
      _edgebst.insert(edge);

      if (_debug) {
         writeToLog("e" + previ + " helper is " + helper + "\n");
         writeToLog("Remove e" + previ + " from splay tree.\n");
         writeToLog("Set e" + i + " helper to " + i + "\n");
         writeToLog("Insert e" + i + " to splay tree\n");
         writeToLog("Insert key:" + edge.keyValue() + "\n");
      }
   }


   /**
    * handle event vertext according to vertex type; Handle regular up vertex C++ code: _i was unsigned, helper also unsigned
    */
   private void handleRegularVertexUp(final int i) {
      final Pointbase point = getPoint(i);

      final double x = point.x, y = point.y;

      _edgebst.inOrder(updateKey, y);

      final BTreeNode leftnode = _edgebst.findMaxSmallerThan(x);

      final Linebase leftedge = (Linebase) leftnode.data();

      final int helper = leftedge.helper();
      final Pointbase helperPoint = getPoint(helper);
      if (helperPoint.type == Poly2TriUtils.MERGE) {
         addDiagonal(i, helper);
      }
      leftedge.setHelper(i);

      if (_debug) {
         writeToLog("Search key:" + x + " found:" + leftedge.keyValue() + "\n");
         writeToLog("e" + leftedge.id() + " is directly left to v" + i + " and its helper is:" + helper + "\n");
         writeToLog("Set e" + leftedge.id() + " helper to " + i + "\n");
      }
   }


   /**
    * main member function for polygon triangulation; partition polygon to monotone polygon pieces C++ code: id was unsigned
    * 
    * @return success
    * @throws TriangulationException
    */
   public void partition2Monotone() throws TriangulationException {
      if (qpointsTop().type != Poly2TriUtils.START) {
         throw new TriangulationException("Please check your input polygon: 1)orientations? 2)duplicated points?");
      }

      int id;
      while (_qpoints.size() > 0) {
         final Pointbase vertex = qpointsPop();

         id = vertex.id;

         if (_debug) {
            String stype;
            switch (vertex.type) {
               case Poly2TriUtils.START:
                  stype = "START";
                  break;
               case Poly2TriUtils.END:
                  stype = "END";
                  break;
               case Poly2TriUtils.MERGE:
                  stype = "MERGE";
                  break;
               case Poly2TriUtils.SPLIT:
                  stype = "SPLIT";
                  break;
               case Poly2TriUtils.REGULAR_UP:
                  stype = "REGULAR_UP";
                  break;
               case Poly2TriUtils.REGULAR_DOWN:
                  stype = "REGULAR_DOWN";
                  break;
               default:
                  throw new TriangulationException("No duplicated points please! poly2tri stopped");
            }
            writeToLog("\n\nHandle vertex:" + vertex.id + " type:" + stype + "\n");
         }

         switch (vertex.type) {
            case Poly2TriUtils.START:
               handleStartVertex(id);
               break;
            case Poly2TriUtils.END:
               handleEndVertex(id);
               break;
            case Poly2TriUtils.MERGE:
               handleMergeVertex(id);
               break;
            case Poly2TriUtils.SPLIT:
               handleSplitVertex(id);
               break;
            case Poly2TriUtils.REGULAR_UP:
               handleRegularVertexUp(id);
               break;
            case Poly2TriUtils.REGULAR_DOWN:
               handleRegularVertexDown(id);
               break;
            default:
               throw new TriangulationException("No duplicated points please! poly2tri stopped");
         }
      }
   }


   /**
    * angle ABC for three given points, for monotone polygon searching purpose; calculate angle B for A, B, C three given points
    * auxiliary function to find monotone polygon pieces
    */
   private double angleCosb(final double[] pa,
                            final double[] pb,
                            final double[] pc) {
      final double dxab = pa[0] - pb[0];
      final double dyab = pa[1] - pb[1];

      final double dxcb = pc[0] - pb[0];
      final double dycb = pc[1] - pb[1];

      final double dxab2 = dxab * dxab;
      final double dyab2 = dyab * dyab;
      final double dxcb2 = dxcb * dxcb;
      final double dycb2 = dycb * dycb;
      final double ab = dxab2 + dyab2;
      final double cb = dxcb2 + dycb2;

      double cosb = (dxab * dxcb) + (dyab * dycb);
      final double denom = Math.sqrt(ab * cb);

      cosb /= denom;

      return cosb;
   }


   /**
    * find the next edge, for monotone polygon searching purpose; for any given edge, find the next edge we should choose when
    * searching for monotone polygon pieces; auxiliary function to find monotone polygon pieces C++ code: return unsigned int, eid
    * also unsigned, same for nexte_ccw, nexte_cw
    */
   private int selectNextEdge(final Linebase edge) {
      final int eid = edge.endPoint(1).id;
      final Set<Integer> edges = getSetFromStartAdjEdgeMap(eid);

      assert (edges.size() != 0);

      int nexte = 0;

      if (edges.size() == 1) {
         nexte = (edges.iterator().next());
      }
      else {
         //final int[] edgesKeys = getSorted(edges);

         int nexte_ccw = 0, nexte_cw = 0;
         double max = -2.0, min = 2.0; // max min of cos(alfa)
         Linebase iEdge;

         final Iterator<Integer> iter = edges.iterator();
         int it;
         while (iter.hasNext()) {
            it = iter.next();
            if (it == edge.id()) {
               continue;
            }

            iEdge = getEdge(it);

            final double[] A = { 0, 0 }, B = { 0, 0 }, C = { 0, 0 };
            A[0] = edge.endPoint(0).x;
            A[1] = edge.endPoint(0).y;
            B[0] = edge.endPoint(1).x;
            B[1] = edge.endPoint(1).y;

            if (!edge.endPoint(1).equals(iEdge.endPoint(0))) {
               iEdge.reverse();
            }
            C[0] = iEdge.endPoint(1).x;
            C[1] = iEdge.endPoint(1).y;

            final double area = Poly2TriUtils.orient2d(A, B, C);
            final double cosb = angleCosb(A, B, C);

            if ((area > 0) && (max < cosb)) {
               nexte_ccw = it;
               max = cosb;
            }
            else if (min > cosb) {
               nexte_cw = it;
               min = cosb;
            }
         }

         nexte = (nexte_ccw != 0) ? nexte_ccw : nexte_cw;
      }

      return nexte;
   }


   /**
    * searching all monotone pieces; C++ code: unsigned - nexte
    * 
    * @return success
    * @throws TriangulationException
    */
   public void searchMonotones() throws TriangulationException {
      int loop = 0;

      @SuppressWarnings("unchecked")
      final HashMap<Integer, Linebase> edges = (HashMap<Integer, Linebase>) _edges.clone();

      ArrayList<Integer> poly;
      int[] edgesKeys;
      int it;
      Linebase itEdge;

      Pointbase startp, endp;
      Linebase next;
      int nexte;

      while (edges.size() > _diagonals.size()) {
         loop++;
         // typedef list<unsigned int> Monopoly;
         poly = new ArrayList<Integer>();

         edgesKeys = getSorted(edges.keySet());

         it = edgesKeys[0];
         itEdge = edges.get(it);

         // Pointbase* startp=startp=it.second.endPoint(0); // ??? startp=startp :-O
         startp = itEdge.endPoint(0);
         endp = null;
         next = itEdge;

         poly.add(startp.id);

         if (_debug) {
            writeToLog("Searching for loops:" + loop + "\n");
            writeToLog("vertex index:" + startp.id + " ");
         }

         for (;;) {

            endp = next.endPoint(1);

            if (next.type() != Poly2TriUtils.INSERT) {
               edges.remove(next.id());
               getSetFromStartAdjEdgeMap(next.endPoint(0).id).remove(next.id());
            }
            if (endp == startp) {
               break;
            }
            poly.add(endp.id);

            writeToLog(endp.id + " ");

            nexte = selectNextEdge(next);

            if (nexte == 0) {
               throw new TriangulationException(
                        "Please check your input polygon: 1)orientations? 2)with duplicated points?\n3)is a simple one?");
            }

            next = edges.get(nexte);
            if (!(next.endPoint(0).equals(endp))) {
               next.reverse();
            }
         }

         writeToLog("\nloop closed!\n\n");

         _mpolys.add(poly);
      }
   }


   /**
    * triangulate a monotone polygon piece; void triangulateMonotone(Monopoly& mpoly); Monopoly == list<Monopoly>
    */
   private void triangulateMonotone(final List<Integer> mpoly) {
      final PriorityQueue<Pointbase> qvertex = new PriorityQueue<Pointbase>(30, new PointbaseComparatorCoordinatesReverse());
      // is it realy ID?

      int i, it, itnext;
      Pointbase point; // C++ code: Pointbase point;     -> must do copy contructor!
      Pointbase pointnext; // C++ code: Pointbase pointnext; -> must do copy contructor!
      for (it = 0; it < mpoly.size(); it++) {
         itnext = it + 1;
         if (itnext == mpoly.size()) {
            itnext = 0;
         }
         point = new Pointbase(getPoint(mpoly.get(it)));
         pointnext = new Pointbase(getPoint(mpoly.get(itnext)));
         point.left = (point.compareTo(pointnext) > 0) ? true : false;
         qvertex.add(point);
      }

      final Stack<Pointbase> spoint = new Stack<Pointbase>();

      for (i = 0; i < 2; i++) {
         spoint.push(qvertex.poll());
      }

      Pointbase topQueuePoint; // C++ code: Pontbase topQueuePoint; -> must do copy constructor!
      Pointbase topStackPoint; // C++ code: Pontbase topStackPoint; -> must do copy constructor!
      Pointbase p1, p2; // again copy constructor !
      Pointbase stack1Point, stack2Point; // again copy ...

      final double[] pa = { 0, 0 }, pb = { 0, 0 }, pc = { 0, 0 };
      double area;
      boolean left;
      //ArrayList<Integer> v; // typedef vector<unsigned int> Triangle;

      // TODO -> doesn't seem that copy constructors are needed here
      //		    nothing is changing ... we're wasting time here!
      //			YES ... if you look through the code, there's no need 
      //				    to create new instances, changed
      while (qvertex.size() > 1) {

         topQueuePoint = qvertex.peek();
         topStackPoint = spoint.peek();

         if (topQueuePoint.left != topStackPoint.left) {
            while (spoint.size() > 1) {
               p1 = spoint.peek();
               spoint.pop();
               p2 = spoint.peek();

               addTriangle(new Triangle( //
                        topQueuePoint.id - 1, //
                        p1.id - 1, //
                        p2.id - 1));
            }
            spoint.pop();
            spoint.push(topStackPoint);
            spoint.push(topQueuePoint);
         }
         else {
            while (spoint.size() > 1) {
               stack1Point = spoint.peek();
               spoint.pop();
               stack2Point = spoint.peek();
               spoint.push(stack1Point);

               pa[0] = topQueuePoint.x;
               pa[1] = topQueuePoint.y;
               pb[0] = stack2Point.x;
               pb[1] = stack2Point.y;
               pc[0] = stack1Point.x;
               pc[1] = stack1Point.y;

               if (_debug) {
                  writeToLog("current top queue vertex index=" + topQueuePoint.id + "\n");
                  writeToLog("Current top stack vertex index=" + stack1Point.id + "\n");
                  writeToLog("Second stack vertex index=" + stack2Point.id + "\n");
               }

               area = Poly2TriUtils.orient2d(pa, pb, pc);
               left = stack1Point.left;

               if (((area > 0) && left) || ((area < 0) && !left)) {
                  addTriangle(new Triangle( //
                           topQueuePoint.id - 1, //
                           stack2Point.id - 1, //
                           stack1Point.id - 1));
                  spoint.pop();
               }
               else {
                  break;
               }
            }
            spoint.push(topQueuePoint);
         }
         qvertex.poll();
      }

      final Pointbase lastQueuePoint = qvertex.peek();
      Pointbase topPoint, top2Point; // C++ code ... copy construtors
      while (spoint.size() != 1) {
         topPoint = spoint.peek();
         spoint.pop();
         top2Point = spoint.peek();

         addTriangle(new Triangle(lastQueuePoint.id - 1, //
                  topPoint.id - 1, //
                  top2Point.id - 1));
      }
   }


   private void addTriangle(final Triangle triangle) {
      _triangles.add(triangle);
      writeToLog("Add triangle:" + (triangle._vertex0 + 1) + " " + (triangle._vertex1 + 1) + " " + (triangle._vertex2 + 1) + "\n");
   }


   /**
    * main triangulation function; In _triangles is result -> polygon _triangles.
    * 
    * @return success
    * @throws TriangulationException
    */
   public void triangulation() throws TriangulationException {
      partition2Monotone();

      searchMonotones();

      for (int i = 0; i < _mpolys.size(); ++i) {
         triangulateMonotone(_mpolys.get(i));
      }

      setDebugOption(false); // possibly closing the log file
   }


   /**
    * return all _triangles
    */
   public List<Triangle> triangles() {
      return _triangles;
   }


   /**
    * output file format;
    */
   public void setDebugOption(final boolean debug) {
      if (debug == _debug) {
         return;
      }
      if (_debug) {
         try {
            _logfile.close();
         }
         catch (final IOException e) {
            System.out.println("Problem closing logfile.");
            e.printStackTrace();
            System.out.println("Continueing the work");
         }
      }
      else {
         try {
            _logfile = new FileWriter(_debugFileName);
         }
         catch (final IOException e) {
            System.out.println("Error creating file polygon_triangulation_log.txt, switchin debug off, continuing.");
            e.printStackTrace();
            _debug = false;
         }
      }
      _debug = debug;
   }


   public void setDebugFile(final String debugFileName) {
      _debugFileName = debugFileName;
   }
}
