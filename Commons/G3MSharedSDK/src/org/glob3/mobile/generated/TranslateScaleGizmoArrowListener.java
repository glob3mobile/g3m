package org.glob3.mobile.generated;
public class TranslateScaleGizmoArrowListener extends ArrowListener
{
  private TranslateScaleGizmo _gizmo;

  public TranslateScaleGizmoArrowListener(TranslateScaleGizmo gizmo)
  {
     _gizmo = gizmo;
  }

  public final void onBaseChanged(Arrow arrow)
  {
    _gizmo.onBaseChanged(arrow);
  }

  public final void onDraggingEnded(Arrow arrow)
  {
    _gizmo.onDraggingEnded(arrow);
  }
}