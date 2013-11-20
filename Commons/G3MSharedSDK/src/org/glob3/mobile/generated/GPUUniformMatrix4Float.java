package org.glob3.mobile.generated; 
public class GPUUniformMatrix4Float extends GPUUniform
{
  public void dispose()
  {
    super.dispose();
  }

  public GPUUniformMatrix4Float(String name, IGLUniformID id)
  {
     super(name,id, GLType.glMatrix4Float());
  }
}