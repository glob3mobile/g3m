package org.glob3.mobile.generated; 
public class GLStateTree
{

  private static GLStateTreeNode _rootNode = new GLStateTreeNode();


  public static GLStateTreeNode createNodeForSGNode(SceneGraphNode sgNode)
  {
    return new GLStateTreeNode(sgNode, _rootNode);
  }

  public static void prune(SceneGraphNode sgNode)
  {
    _rootNode.prune(sgNode);
  }

}