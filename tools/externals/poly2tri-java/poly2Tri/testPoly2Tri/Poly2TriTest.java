

package poly2Tri.testPoly2Tri;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import poly2Tri.Triangle;
import poly2Tri.Triangulation;
import poly2Tri.TriangulationException;


public class Poly2TriTest {

   protected static double[] _temp;
   protected static int      _i;


   public static void printVertices(final double[][] vertices) {

      System.out.println("Vertices:");
      for (_i = 1; _i < vertices.length; ++_i) {
         _temp = vertices[_i];
         if ((_i % 6) == 0) {
            System.out.println();
         }
         System.out.print("[" + _temp[0] + ", " + _temp[1] + "]   ");
      }
      System.out.println();
      if ((_i % 5) != 0) {
         System.out.println();
      }
   }

   public static List<Triangle>      _triangles = null;
   public static int                 _numContours;
   public static int[]               _contours;
   public static double[][]          _vertices;

   public static int                 _number    = 0;
   public static List<Poly2TriFrame> _tstfs     = new ArrayList<Poly2TriFrame>();

   public static String              _name      = "";


   public static void doTriangulation() throws TriangulationException {
      ++_number;
      _triangles = null;
      //printVertices(_vertices);

      final Date begin = new Date();

      _triangles = Triangulation.triangulate(_numContours, _contours, _vertices);

      final Date end = new Date();


      //printTriangles(_triangles);
      final Poly2TriFrame tstf = new Poly2TriFrame();

      final long ms = end.getTime() - begin.getTime();

      tstf.setTitle("Poly2Tri - " + _name + " - Time: " + ms + " miliseconds");

      System.out.println(_name + ", time: " + ms + " miliseconds");

      double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
      Triangle t;
      double[] xy1 = { 0, 0 }, xy2 = { 0, 0 }, xy3 = { 0, 0 };
      for (int i = 0; i < _triangles.size(); ++i) {
         t = _triangles.get(i);
         for (int j = 0; j < 3; ++j) {
            xy1 = _vertices[t.get(j)];
            if (xy1[0] > maxX) {
               maxX = xy1[0];
            }
            if (xy1[0] < minX) {
               minX = xy1[0];
            }
            if (xy1[1] > maxY) {
               maxY = xy1[1];
            }
            if (xy1[1] < minY) {
               minY = xy1[1];
            }
         }
      }

      tstf.setMaxX(maxX);
      tstf.setMinX(minX);
      tstf.setMaxY(maxY);
      tstf.setMinY(minY);

      for (int i = 0; i < _triangles.size(); ++i) {
         t = _triangles.get(i);
         xy1 = _vertices[t._vertex0];
         xy2 = _vertices[t._vertex1];
         xy3 = _vertices[t._vertex2];
         tstf.addTriangle(xy1[0], xy1[1], xy2[0], xy2[1], xy3[0], xy3[1]);
      }

      tstf.setLocation(_number * 30, _number * 30);

      // don't have time to write invokeLater
      // if you have problems use SwingUtilities.invokeLater( new Runnable(){ ... } );
      tstf.setVisible(true);
      tstf.toFront();

      _tstfs.add(tstf); // to maintain the object?	    
   }


   public static int skipWhitespaces(final String str,
                                     int index) {
      while ((index < str.length()) && ((str.charAt(index) == ' ') || (str.charAt(index) == '\t'))) {
         ++index;
      }
      return index;
   }


   public static Double getNumber(final String str,
                                  final int[] index) {
      index[0] = skipWhitespaces(str, index[0]);
      if (index[0] >= str.length()) {
         return Double.NaN;
      }
      final StringBuffer temp = new StringBuffer();
      while ((index[0] < str.length())
             && ((str.charAt(index[0]) == 'e') || (str.charAt(index[0]) == 'E') || (str.charAt(index[0]) == '+')
                 || (str.charAt(index[0]) == '-') || (str.charAt(index[0]) == '.') || ((str.charAt(index[0]) >= '0') && (str.charAt(index[0]) <= '9')))) {
         temp.append(str.charAt(index[0]));
         ++index[0];
      }
      if (temp.length() == 0) {
         return Double.NaN;
      }
      return new Double(temp.toString());
   }


   public static boolean loadBDMFile(final String fileName) {
      final ArrayList<ArrayList<double[]>> contoursAL = new ArrayList<ArrayList<double[]>>();
      ArrayList<double[]> verticesAL;
      int numVertices = 0;
      int curNumVertices;
      Double num, num2;
      final int[] index = { 0 };
      try {

         @SuppressWarnings("resource")
         final LineNumberReader fr = new LineNumberReader(new FileReader(fileName));
         String line;

         while (fr.ready()) {
            line = fr.readLine();
            if (line == null) {
               continue;
            }
            if (line.length() == 0) {
               continue;
            }
            if ((line.charAt(0) >= '0') && (line.charAt(0) <= '9')) {
               num = getNumber(line, new int[] { 0 });
               curNumVertices = (int) Math.round(num);
               numVertices += curNumVertices;
               verticesAL = new ArrayList<double[]>();
               for (int i = 0; i < curNumVertices; ++i) {
                  line = fr.readLine();
                  if (line == null) {
                     return false;
                  }
                  index[0] = 0;
                  num = getNumber(line, index);
                  if (num == Double.NaN) {
                     return false;
                  }
                  num2 = getNumber(line, index);
                  if (num2 == Double.NaN) {
                     return false;
                  }
                  verticesAL.add(new double[] { num, num2 });
               }
               contoursAL.add(verticesAL);
            }
            else {
               continue;
            }
         }

         fr.close();

      }
      catch (final IOException e) {
         return false;
      }

      _numContours = contoursAL.size();
      _contours = new int[_numContours];
      _vertices = new double[numVertices][2];
      int k = 0;
      for (int i = 0; i < _numContours; ++i) {
         verticesAL = contoursAL.get(i);
         _contours[i] = verticesAL.size();
         for (int j = 0; j < verticesAL.size(); ++j) {
            _vertices[k++] = verticesAL.get(j);
         }
      }
      return true;
   }


   public static void main(final String[] args) throws TriangulationException {
      // 1st input
      _numContours = 1;
      // each _number is _number of vertex of _i-th conture,
      // first conture is outer (must be counterclockwise), others must be inner (clockwise)
      _contours = new int[] { 5 };

      _vertices = new double[][] { { 1, 1 }, { 4, 0 }, { 3, 5 }, { 2, 8 }, { 0, 0 } };

      _name = "1st";
      doTriangulation();

      // 2nd input
      _numContours = 2;
      // each _number is _number of vertex of _i-th conture,
      // first conture is outer (must be counterclockwise), others must be inner (clockwise)
      _contours = new int[] { 3, 3 };

      _vertices = new double[][] { { 0, 0 }, { 7, 0 }, { 3, 4 }, { 2, 1 }, { 2, 2 }, { 3, 1 }, };

      _name = "2nd";
      doTriangulation();

      // 3rd input
      _numContours = 3;
      // each _number is _number of vertex of _i-th conture,
      // first conture is outer (must be counterclockwise), others must be inner (clockwise)
      _contours = new int[] { 3, 3, 3 };

      _vertices = new double[][] { { 0, 0 }, { 9, 0 }, { 3, 8 }, { 2, 1 }, { 2, 2 }, { 3, 1 }, { 6, 2 }, { 6, 1 }, { 5, 2 } };

      _name = "3rd";
      doTriangulation();

      // BDM Files
      final String prefix = "poly2tri/testPoly2Tri/";
      final String[] bdmFiles = { "boxc100.bdm", "circle1.bdm", "circle2.bdm", "circle3.bdm", "crazybox1.bdm", "crazybox2.bdm",
               "guitar.bdm", "sample1.bdm", "sample2.bdm", "sample3.bdm", "superior.bdm" };

      int option;
      boolean end = false, next = false, all = false;

      option = JOptionPane.showConfirmDialog(_tstfs.get(_tstfs.size() - 1), "Do you want to do ALL bdm files triangulation?");
      all = option == JOptionPane.YES_OPTION;

      for (final String bdmFile : bdmFiles) {
         if (!all) {
            next = false;
            option = JOptionPane.showConfirmDialog(_tstfs.get(_tstfs.size() - 1), "Do triangulation of " + bdmFile + "?");
            switch (option) {
               case JOptionPane.CANCEL_OPTION:
                  end = true;
                  break;
               case JOptionPane.NO_OPTION:
                  next = true;
                  break;
            }
            if (end) {
               break;
            }
            if (next) {
               continue;
            }
         }
         if (loadBDMFile(prefix + bdmFile)) {
            _name = bdmFile;
            doTriangulation();
         }
         else {
            JOptionPane.showMessageDialog(_tstfs.get(_tstfs.size() - 1), "Failed to load " + bdmFile);
         }
      }
   }

}
