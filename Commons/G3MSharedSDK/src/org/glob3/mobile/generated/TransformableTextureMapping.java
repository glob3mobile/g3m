package org.glob3.mobile.generated;
//
//  TransformableTextureMapping.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//

//
//  TransformableTextureMapping.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/9/14.
//
//



public abstract class TransformableTextureMapping extends TextureMapping
{
  protected float _translationU;
  protected float _translationV;
  protected float _scaleU;
  protected float _scaleV;
  protected float _rotationInRadians;
  protected float _rotationCenterU;
  protected float _rotationCenterV;

  protected TransformableTextureMapping(float translationU, float translationV, float scaleU, float scaleV, float rotationAngleInRadians, float rotationCenterU, float rotationCenterV)
  {
     _translationU = translationU;
     _translationV = translationV;
     _scaleU = scaleU;
     _scaleV = scaleV;
     _rotationInRadians = rotationAngleInRadians;
     _rotationCenterU = rotationCenterU;
     _rotationCenterV = rotationCenterV;
  }

  public final void setTranslation(float u, float v)
  {
    _translationU = u;
    _translationV = v;
  }

  public final void setScale(float u, float v)
  {
    _scaleU = u;
    _scaleV = v;
  }

  public final void setRotation(float angleInRadians, float centerU, float centerV)
  {
    _rotationInRadians = angleInRadians;
    _rotationCenterU = centerU;
    _rotationCenterV = centerV;
  }

}
