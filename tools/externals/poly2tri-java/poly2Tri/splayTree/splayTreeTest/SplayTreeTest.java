

package poly2Tri.splayTree.splayTreeTest;

import poly2Tri.splayTree.BTreeNode;
import poly2Tri.splayTree.SplayTree;


public class SplayTreeTest {

   // /==============\
   //  TESTING METHOD
   // \==============/

   public static void main(final String[] argv) {
      final SplayTree t = new SplayTree();

      final int NUMS = 40000;
      final int GAP = 307;

      System.out.println("Checking... (no bad output means success)");

      for (int i = GAP; i != 0; i = (i + GAP) % NUMS) {
         t.insert(new SplayTreeItemTest(i));
      }
      System.out.println("Inserts complete");

      for (int i = 1; i < NUMS; i += 2) {
         t.delete(Double.valueOf(i));
      }
      System.out.println("Removes complete");

      BTreeNode node;
      for (int i = 2; i < NUMS; i += 2) {
         node = t.find(Double.valueOf(i));
         if ((node == null) || (((SplayTreeItemTest) node.data()).data != i)) {
            System.out.println("Error: find fails for " + i);
         }
      }

      for (int i = 1; i < NUMS; i += 2) {
         if (t.find(Double.valueOf(i)) != null) {
            System.out.println("Error: Found deleted item " + i);
         }
      }

      for (int i = 4; i < NUMS; i += 2) {
         node = t.findMaxSmallerThan(Double.valueOf(i));
         if (((SplayTreeItemTest) node.data()).data != (i - 2)) {
            System.out.println("Error: findMaxSmallerThan(_i) == " + ((SplayTreeItemTest) node.data()).data + " != " + (i - 2));
         }
      }
      System.out.println("FindMaxSmallerThen complete");
   }

}
