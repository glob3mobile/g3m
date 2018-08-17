package org.glob3.mobile.generated;import java.util.*;

//
//  SGMaterialNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGMaterialNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//



public class SGMaterialNode extends SGNode
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Color _baseColor;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public Color _baseColor = new internal();
//#endif
  private final Color _specularColor;

//  const double _specular;
//  const double _shine;
//  const double _alpha;
//  const double _emit;


  private GLState _glState;


  public SGMaterialNode(String id, String sId, Color baseColor, Color specularColor, double specular, double shine, double alpha, double emit)
//  _specular(specular),
//  _shine(shine),
//  _alpha(alpha),
//  _emit(emit)
  {
	  super(id, sId);
	  _baseColor = baseColor;
	  _specularColor = specularColor;
	  _glState = new GLState();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	_glState.addGLFeature(new FlatColorGLFeature(_baseColor, false, 0, 0), false);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_glState.addGLFeature(new FlatColorGLFeature(_baseColor, false, 0, 0), false);
//#endif
  }

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
	_glState.setParent(parentState);
	return _glState;
  }

  public final void setBaseColor(Color baseColor)
  {
	if (baseColor != _baseColor)
	{
	  if (_baseColor != null)
		  _baseColor.dispose();
	  _baseColor = baseColor;
	}
  }

  public void dispose()
  {
	if (_baseColor != null)
		_baseColor.dispose();
	if (_specularColor != null)
		_specularColor.dispose();

	_glState._release();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif

  }

  public final String description()
  {
	return "SGMaterialNode";
  }

}
