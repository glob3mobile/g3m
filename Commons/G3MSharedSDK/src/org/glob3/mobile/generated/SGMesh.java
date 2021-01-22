package org.glob3.mobile.generated;
//
//  SGMesh.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//

//
//  SGMesh.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/17/17.
//
//



//class SGNode;

public class SGMesh extends TransformableMesh
{
  private SGNode _node;
  private final String _uriPrefix;
  private final boolean _isTransparent;

  private boolean _nodeInitialized;


  protected final void userTransformMatrixChanged()
  {
  
  }

  protected final void initializeGLState(GLState glState)
  {
    super.initializeGLState(glState);
  
    final boolean blend = _isTransparent;
    glState.addGLFeature(new BlendingModeGLFeature(blend, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
  }


  public SGMesh(SGNode node, String uriPrefix, boolean isTransparent)
  {
     super(Vector3D.ZERO);
     _node = node;
     _uriPrefix = uriPrefix;
     _isTransparent = isTransparent;
     _nodeInitialized = false;
  
  }

  public void dispose()
  {
    if (_node != null)
       _node.dispose();
  
    super.dispose();
  }

  public final int getVertexCount()
  {
    throw new RuntimeException("Can't implement");
  }

  public final Vector3D getVertex(int index)
  {
    throw new RuntimeException("Can't implement");
  }
  public final void getVertex(int index, MutableVector3D result)
  {
    throw new RuntimeException("Can't implement");
  }

  public final BoundingVolume getBoundingVolume()
  {
    return null;
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return _isTransparent;
  }

  public final void rawRender(G3MRenderContext rc, GLState parentGLState)
  {
    GLState glState = getGLState();
    glState.setParent(parentGLState);
  
    if (!_nodeInitialized)
    {
      _nodeInitialized = true;
      _node.initialize(rc, _uriPrefix);
    }
  
    if (_node.isReadyToRender(rc))
    {
      final boolean renderNotReadyShapes = false;
      _node.render(rc, glState, renderNotReadyShapes);
    }
  }

}