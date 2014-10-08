

package poly2Tri.testPoly2Tri;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;


/**
 * It's as nasty as it could be ... slow as hell, but writing the code was fast :D
 * 
 * @author Jimmy
 * 
 */
public class Poly2TriPainting
         extends
            JPanel {


   private static final long    serialVersionUID = 1L;

   private final List<double[]> polygons         = new ArrayList<double[]>();

   double                       maxX             = 10;
   double                       maxY             = 10;
   double                       minX             = -1;
   double                       minY             = -1;


   public Poly2TriPainting() {
   }


   public void addPolygon(final double[] xy) {
      polygons.add(xy);
   }


   protected int[] getPoint(final double x,
                            final double y) {
      final double xSize = this.getWidth() / (maxX - minX);
      final double ySize = (this.getHeight()) / (maxY - minY);

      final double[] center = { minX + ((maxX - minX) / 2), minY + ((maxY - minY) / 2) };

      final int[] realCenter = { this.getWidth() / 2, (this.getHeight()) / 2 };

      final int rX = (int) (Math.round((realCenter[0] - (center[0] * xSize)) + (x * xSize)));
      final int rY = (int) (Math.round((realCenter[1] + (center[1] * ySize)) - (y * ySize)));
      return new int[] { rX, rY };
   }


   @Override
   public void paint(final Graphics g) {
      double[] xy;
      Polygon p;

      for (int i = 0; i < polygons.size(); ++i) {
         xy = polygons.get(i);

         p = new Polygon();

         final int[] point1 = getPoint(xy[0], xy[1]);
         final int[] point2 = getPoint(xy[2], xy[3]);
         final int[] point3 = getPoint(xy[4], xy[5]);

         p.addPoint(point1[0], point1[1]);
         p.addPoint(point2[0], point2[1]);
         p.addPoint(point3[0], point3[1]);

         g.setColor(Color.BLUE);
         g.fillPolygon(p);
         g.setColor(Color.BLACK);
         g.drawPolygon(p);
      }

   }

}
