package org.glob3.mobile.generated;//
//  PointCloudMesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//

//
//  PointCloudMesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/1/12.
//
//




//class MutableMatrix44D;
//class IFloatBuffer;
//class Color;

public class PointCloudMesh extends DirectMesh
{
    private Vector3D _buildingAnchor ;
    private MutableMatrix44D _mt1 = new MutableMatrix44D();
    private MutableMatrix44D _mt2 = new MutableMatrix44D();

    public PointCloudMesh(boolean owner, Vector3D buildingAnchor, Vector3D center, IFloatBuffer vertices, float pointSize, IFloatBuffer colors, boolean depthTest, Color borderColor, VertexColorScheme vertexColorScheme) //Depth test
    {
       super(GLPrimitive.points(), owner, center, vertices, 1.0, pointSize, null, null, 0.0f, false, null, false, 0, 0, vertexColorScheme, -1.0f);
       _buildingAnchor = new Vector3D(buildingAnchor);
       _mt1 = new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(_buildingAnchor.times(-1)));
       _mt2 = new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(_buildingAnchor));
        _glState.addGLFeature(new PointShapeGLFeature(borderColor), false);
    }

    public final void updatePopUpEffect(float completionRatio)
    {
        if (completionRatio > 1.0)
        {
            Matrix44D m = Matrix44D.createIdentity();
            setTransformation(m);
            m._release();
        }
        else
        {
            final float a = completionRatio*(2.0f-completionRatio); //Ease-Out
            final float scale = 1.0f + (1.0f - a) * 0.2f;
            MutableMatrix44D ms = MutableMatrix44D.createScaleMatrix(scale, scale, scale);
            MutableMatrix44D m = _mt2.multiply(ms).multiply(_mt1);
            setTransformation(m.asMatrix44D());
        }
    }

}
