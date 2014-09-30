

package poly2Tri.splayTree.splayTreeTest;

import poly2Tri.splayTree.SplayTreeItem;


public class SplayTreeItemTest
         implements
            SplayTreeItem {

   int data = 1;


   public SplayTreeItemTest(final int a) {
      data = a;
   }


   @Override
   public void increaseKeyValue(final double delta) {
      data += 1;
   }


   @Override
   public Double keyValue() {
      return Double.valueOf(data);
   }

}
