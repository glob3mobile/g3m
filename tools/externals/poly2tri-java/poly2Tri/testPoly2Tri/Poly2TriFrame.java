

package poly2Tri.testPoly2Tri;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Poly2TriFrame
         extends
            JFrame {

   private static final long serialVersionUID = 1L;

   private JPanel            jContentPane     = null;

   private double            maxX             = +10;
   private double            maxY             = +10;
   private double            minX             = -10;
   private double            minY             = -10;


   /**
    * This is the default constructor
    */
   public Poly2TriFrame() {
      super();
      initialize();
   }


   /**
    * This method initializes this
    * 
    * @return void
    */
   private void initialize() {
      this.setSize(300, 300);
      this.setContentPane(getJContentPane());
      this.setTitle("Poly2Tri");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }


   /**
    * This method initializes jContentPane
    * 
    * @return javax.swing.JPanel
    */
   private JPanel getJContentPane() {
      if (jContentPane == null) {
         jContentPane = new Poly2TriPainting();
         jContentPane.setLayout(null);
      }
      return jContentPane;
   }


   /**
    * Applies only to new _triangles. Set before using / calling addTriangle()
    * 
    * @param newMaxX
    */
   public void setMaxX(final double newMaxX) {
      if (newMaxX > 0) {
         maxX = newMaxX + 0.5;
      }
      else {
         maxX = newMaxX + 0.5;
      }
      ((Poly2TriPainting) this.getContentPane()).maxX = maxX;
   }


   /**
    * Applies only to new _triangles. Set before using / calling addTriangle()
    * 
    * @param newMaxX
    */
   public void setMaxY(final double newMaxY) {
      if (newMaxY > 0) {
         maxY = newMaxY + 0.5;
      }
      else {
         maxY = newMaxY + 0.5;
      }
      ((Poly2TriPainting) this.getContentPane()).maxY = maxY;
   }


   /**
    * Applies only to new _triangles. Set before using / calling addTriangle()
    * 
    * @param newMaxX
    */
   public void setMinX(final double newMinX) {
      if (newMinX > 0) {
         minX = newMinX - 0.5;
      }
      else {
         minX = newMinX - 0.5;
      }
      ((Poly2TriPainting) this.getContentPane()).minX = minX;
   }


   /**
    * Applies only to new _triangles. Set before using / calling addTriangle()
    * 
    * @param newMaxX
    */
   public void setMinY(final double newMinY) {
      if (newMinY > 0) {
         minY = newMinY - 0.5;
      }
      else {
         minY = newMinY - 0.5;
      }
      ((Poly2TriPainting) this.getContentPane()).minY = minY;
   }


   public void addTriangle(final double x1,
                           final double y1,
                           final double x2,
                           final double y2,
                           final double x3,
                           final double y3) {
      ((Poly2TriPainting) this.getContentPane()).addPolygon(new double[] { x1, y1, x2, y2, x3, y3 });
   }

}
