package org.glob3.mobile.generated;
//
//  TranslateScaleGizmo.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/2/21.
//

//
//  TranslateScaleGizmo.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 20/2/21.
//


///#include "Sphere.hpp"
///#include "MeshRenderer.hpp"
///#include "Box.hpp"

//class TranslateScaleGizmo;

public abstract class TranslateScaleGizmoListener
{
  public void dispose()
  {
  }
  public abstract void onChanged(TranslateScaleGizmo gizmo);
  public abstract void onChangeEnded(TranslateScaleGizmo gizmo);
}