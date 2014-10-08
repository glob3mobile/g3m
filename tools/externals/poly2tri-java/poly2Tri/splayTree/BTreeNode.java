

package poly2Tri.splayTree;

/**
 * Each object which want to be a part of the tree must implement interface SplayTreeItem.
 */
public class BTreeNode {

   SplayTreeItem _data;
   BTreeNode     _left;
   BTreeNode     _right;
   boolean       _visited = false;


   public BTreeNode() {
      _data = null;
      _left = null;
      _right = null;
   }


   public BTreeNode(final SplayTreeItem data,
                    final BTreeNode left,
                    final BTreeNode right) {
      _data = data;
      _left = left;
      _right = right;
   }


   public SplayTreeItem data() {
      return _data;
   }


   public BTreeNode left() {
      return _left;
   }


   public BTreeNode right() {
      return _right;
   }


   void setVisited(final boolean visited) {
      _visited = visited;
   }


   boolean getVisited() {
      return _visited;
   }


   Double keyValue() {
      return _data.keyValue();
   }

}
