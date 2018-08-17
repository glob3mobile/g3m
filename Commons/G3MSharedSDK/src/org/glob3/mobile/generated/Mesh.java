package org.glob3.mobile.generated;import java.util.*;

//
//  Mesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//

//
//  Mesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector3D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GPUProgramState;

public abstract class Mesh
{
	private boolean _enable;
	public Mesh()
	{
		_enable = true;
	}

	public final void setEnable(boolean enable)
	{
		_enable = enable;
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnable() const
	public final boolean isEnable()
	{
		return _enable;
	}

	public void dispose()
	{
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int getVertexCount() const = 0;
	public abstract int getVertexCount();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const Vector3D getVertex(int i) const = 0;
	public abstract Vector3D getVertex(int i);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual BoundingVolume* getBoundingVolume() const = 0;
	public abstract BoundingVolume getBoundingVolume();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isTransparent(const G3MRenderContext* rc) const = 0;
	public abstract boolean isTransparent(G3MRenderContext rc);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void rawRender(const G3MRenderContext* rc, const GLState* parentGLState) const = 0;
	public abstract void rawRender(G3MRenderContext rc, GLState parentGLState);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void render(const G3MRenderContext* rc, const GLState* parentGLState) const
	public final void render(G3MRenderContext rc, GLState parentGLState)
	{
		if (_enable)
		{
			rawRender(rc, parentGLState);
		}
	}

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void showNormals(boolean v) const = 0;
	public abstract void showNormals(boolean v);

	public void setColorTransparency(java.util.ArrayList<Double> transparency)
	{
	}

}
