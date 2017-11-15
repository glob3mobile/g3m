package org.eifer.eiferapp.g3mutils;

import org.glob3.mobile.generated.AbstractMesh;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.IFloatBuffer;

import java.util.ArrayList;

/**
 * Created by chano on 13/11/17.
 */

public class ColorChangingMeshTask extends GTask {

    private AbstractMesh _abstractMesh;
    private int _step;
    private ArrayList<IFloatBuffer> _colors;

    public ColorChangingMeshTask(AbstractMesh abstractMesh, ArrayList<IFloatBuffer> colors){
        _abstractMesh = abstractMesh;
        _step = 0;
        _colors = colors;
        IFloatBuffer meshColors = _abstractMesh.getColorsFloatBuffer();
        for (int i = 0; i < _colors.size(); i++) {
            if (colors.get(i).size() != meshColors.size()){
                throw new IllegalArgumentException("WRONG NUMBER OF COLORS");
            }
        }
    }

    @Override
    public void run(G3MContext context){
        IFloatBuffer colors = _abstractMesh.getColorsFloatBuffer();
        IFloatBuffer newColors = _colors.get(_step);

        colors.put(0, newColors);

        _step++; //Advance
    }
}