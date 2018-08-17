package org.glob3.mobile.generated;import java.util.*;

//
//  SGShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SGNode;

public class SGShape extends Shape
{
	private SGNode _node;
	private final String _uriPrefix;

	private final boolean _isTransparent;

	private GLState _glState;


	public SGShape(SGNode node, String uriPrefix, boolean isTransparent, Geodetic3D position, AltitudeMode altitudeMode)
	{
		super(position, altitudeMode);
		_node = node;
		_uriPrefix = uriPrefix;
		_isTransparent = isTransparent;
		_glState = new GLState();
		if (_isTransparent)
		{
			_glState.addGLFeature(new BlendingModeGLFeature(true, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
		}
		else
		{
			_glState.addGLFeature(new BlendingModeGLFeature(false, GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
		}
	}

	public void dispose()
	{
		_glState._release();
		if (_node != null)
			_node.dispose();
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGShape* clone(Geodetic3D* position, AltitudeMode altitudeMode) const
	public final SGShape clone(Geodetic3D position, AltitudeMode altitudeMode)
	{
		return new SGShape(_node, _uriPrefix, _isTransparent, position, altitudeMode);
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGNode* getNode() const
	public final SGNode getNode()
	{
		return _node;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getURIPrefix() const
	public final String getURIPrefix()
	{
		return _uriPrefix;
	}

	public final void initialize(G3MContext context)
	{
		_node.initialize(context, this);
	}

	public final boolean isReadyToRender(G3MRenderContext rc)
	{
		return _node.isReadyToRender(rc);
	}

	public final void rawRender(G3MRenderContext rc, GLState parentState, boolean renderNotReadyShapes)
	{
		_glState.setParent(parentState);
		_node.render(rc, _glState, renderNotReadyShapes);
	}

	public final boolean isTransparent(G3MRenderContext rc)
	{
		return _isTransparent;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<double> intersectionsDistances(const Planet* planet, const Vector3D& origin, const Vector3D& direction) const
	public final java.util.ArrayList<Double> intersectionsDistances(Planet planet, Vector3D origin, Vector3D direction)
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO
		java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
		return intersections;
	}


}
