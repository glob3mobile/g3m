package org.glob3.mobile.generated; 
public class GPUUniformValueFloatMutable extends GPUUniformValueFloat
{
  public void dispose()
  {
    super.dispose();
  }
  
  boolean _hasChangedSinceLastGet = false;


  public GPUUniformValueFloatMutable(float x)
  {
     super(x);
  }

  public final void changeValue(float x)
  {
	  if (x != _value){
		    _value = x;
		    _hasChangedSinceLastGet =true;
	  }
	  
  }
  
  public boolean isEquals(GPUUniformValue v)
  {
	  if (v == this){
		  _hasChangedSinceLastGet = false;
		  return false;
	  }
	  
    GPUUniformValueFloat v2 = (GPUUniformValueFloat)v;
    return _value == v2._value;
  }
}