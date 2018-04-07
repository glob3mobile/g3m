package org.glob3.mobile.generated;
//
//  SGTextureNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGTextureNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//



//class SGLayerNode;

public class SGTextureNode extends SGNode
{
  private java.util.ArrayList<SGLayerNode> _layers = new java.util.ArrayList<SGLayerNode>();

  private GLState _glState;
  private boolean _envEffect;

  public SGTextureNode(String id, String sId, boolean envEffect)
  {
     super(id, sId);
     _glState = null;
     _envEffect = envEffect;
  }

  public void dispose()
  {
    final int layersCount = _layers.size();
    for (int i = 0; i < layersCount; i++)
    {
      SGLayerNode layer = _layers.get(i);
      if (layer != null)
         layer.dispose();
    }

    if (_glState != null)
    {
      _glState._release();
    }

    super.dispose();
  }

  public final void addLayer(SGLayerNode layer)
  {
    _layers.add(layer);

    if (_context != null)
    {
      layer.initialize(_context, _uriPrefix);
    }

    if (_glState != null)
    {
      _glState._release();
    }
    _glState = null;
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    final int layersCount = _layers.size();
    for (int i = 0; i < layersCount; i++)
    {
      SGLayerNode layer = _layers.get(i);
      if (!layer.isReadyToRender(rc))
      {
        return false;
      }
    }

    return super.isReadyToRender(rc);
  }

  public final void initialize(G3MContext context, String uriPrefix)
  {
    super.initialize(context, uriPrefix);

    final int layersCount = _layers.size();
    for (int i = 0; i < layersCount; i++)
    {
      SGLayerNode child = _layers.get(i);
      child.initialize(context, uriPrefix);
    }
  }

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
    if (_glState == null)
    {
      _glState = new GLState();
        if (_envEffect) {
            _glState.addGLFeature(new CustomShaderGLFeature("env-geometry-shader") {
                @Override
                protected boolean onInitializeShader(GL gl, GLState state,
                                                     GPUProgram linkedProgram) {
                    return false;
                }

                @Override
                protected void onAfterApplyShaderOnGPU(GL gl, GLState state,
                                                       GPUProgram linkedProgram) {
                }

                @Override
                public void applyOnGlobalGLState(GLGlobalState state) {

                }
            }, true);
        }
      final int layersCount = _layers.size();
      for (int i = 0; i < layersCount; i++)
      {
        SGLayerNode layer = _layers.get(i);
        if (!layer.modifyGLState(rc, _glState))
        {
          _glState._release();
          _glState = null;
          return parentState;
        }
      }

    }
    if (_glState != null)
    {
      _glState.setParent(parentState);
    }
    return _glState;
  }

  public final String description()
  {
    return "SGTextureNode";
  }

}
