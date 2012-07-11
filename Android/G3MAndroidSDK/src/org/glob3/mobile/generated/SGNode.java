package org.glob3.mobile.generated; 
//
//  SceneGraphRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  SceneGraphRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SGGGroupNode;

public abstract class SGNode
{
  protected SGGGroupNode _parent;

  protected MutableMatrix44D _localMatrix = new MutableMatrix44D();
  protected MutableMatrix44D _fullMatrix = new MutableMatrix44D();
  protected boolean _localMatrixDirty;
  protected boolean _fullMatrixDirty;

  protected MutableVector3D _translation = new MutableVector3D();
  protected MutableVector3D _scale = new MutableVector3D();
  protected double _headingInRadians;
  protected double _pitchInRadians;
  protected double _rollInRadians;


  protected SGNode()
  {
	  _parent = null;
	  _localMatrixDirty = true;
	  _fullMatrixDirty = true;
	  _translation = new MutableVector3D(0, 0, 0);
	  _scale = new MutableVector3D(1, 1, 1);
	  _headingInRadians = 0;
	  _pitchInRadians = 0;
	  _rollInRadians = 0;

  }

  protected final MutableMatrix44D getFullMatrix()
  {
	if (!_fullMatrixDirty)
	{
	  return _fullMatrix;
	}
	_fullMatrixDirty = false;
  
	MutableMatrix44D localMatrix = getLocalMatrix();
	if (_parent != null)
	{
	  localMatrix = _parent.getFullMatrix().multiply(localMatrix);
	}
  
	_fullMatrix = localMatrix;
  
	return _fullMatrix;
  }
  protected final MutableMatrix44D getLocalMatrix()
  {
	if (!_localMatrixDirty)
	{
	  return _localMatrix;
	}
	_localMatrixDirty = false;
  
	_localMatrix = MutableMatrix44D.identity();
  
  //  switch (_transformOrder) {
  //    case ROTATION_SCALE_TRANSLATION:
		applyRotationToLocalMatrix();
		applyScaleToLocalMatrix();
		applyTranslationToLocalMatrix();
  //      break;
  //
  //    case ROTATION_TRANSLATION_SCALE:
  //      applyRotationToLocalMatrix();
  //      applyTranslationToLocalMatrix();
  //      applyScaleToLocalMatrix();
  //      break;
  //
  //    case TRANSLATION_ROTATION_SCALE:
  //      applyTranslationToLocalMatrix();
  //      applyRotationToLocalMatrix();
  //      applyScaleToLocalMatrix();
  //      break;
  //
  //    case SCALE_TRANSLATION_ROTATION:
  //      applyScaleToLocalMatrix();
  //      applyTranslationToLocalMatrix();
  //      applyRotationToLocalMatrix();
  //      break;
  //  }
  
	return _localMatrix;
  }

  protected final void applyRotationToLocalMatrix()
  {
	if (_headingInRadians != 0)
	{
	  _localMatrix = _localMatrix.multiply(MutableMatrix44D.fromRotationZ(Angle.fromRadians(_headingInRadians)));
	}
	if (_pitchInRadians != 0)
	{
	  _localMatrix = _localMatrix.multiply(MutableMatrix44D.fromRotationX(Angle.fromRadians(_pitchInRadians)));
	}
	if (_rollInRadians != 0)
	{
	  _localMatrix = _localMatrix.multiply(MutableMatrix44D.fromRotationY(Angle.fromRadians(_rollInRadians)));
	}
  }
  protected final void applyScaleToLocalMatrix()
  {
	if ((_scale.x() != 1) || (_scale.y() != 1) || (_scale.z() != 1))
	{
	  _localMatrix = _localMatrix.multiply(MutableMatrix44D.fromScale(_scale));
	}
  }
  protected final void applyTranslationToLocalMatrix()
  {
	if (!_translation.isZero())
	{
	  _localMatrix = _localMatrix.multiply(MutableMatrix44D.fromTranslation(_translation));
	}
  }


  public void transformationChanged()
  {
	_localMatrixDirty = true;
	_fullMatrixDirty = true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: SGGGroupNode* getParent() const
  public final SGGGroupNode getParent()
  {
	return _parent;
  }
  public final void setParent(SGGGroupNode parent)
  {
	_parent = parent;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getTranslation() const
  public final Vector3D getTranslation()
  {
	return _translation.asVector3D();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getScale() const
  public final Vector3D getScale()
  {
	return _scale.asVector3D();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getHeading() const
  public final Angle getHeading()
  {
	return Angle.fromRadians(_headingInRadians);
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getPitch() const
  public final Angle getPitch()
  {
	return Angle.fromRadians(_pitchInRadians);
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getRoll() const
  public final Angle getRoll()
  {
	return Angle.fromRadians(_rollInRadians);
  }

  public final void setTranslation(Vector3D translation)
  {
	_translation = translation.asMutableVector3D();
	transformationChanged();
  }
  public final void setScale(Vector3D scale)
  {
	_scale = scale.asMutableVector3D();
	transformationChanged();
  }
  public final void setHeading(Angle heading)
  {
	_headingInRadians = heading.radians();
	transformationChanged();
  }
  public final void setPitch(Angle pitch)
  {
	_pitchInRadians = pitch.radians();
	transformationChanged();
  }
  public final void setRoll(Angle roll)
  {
	_rollInRadians = roll.radians();
	transformationChanged();
  }

  public abstract int render(RenderContext rc);
}