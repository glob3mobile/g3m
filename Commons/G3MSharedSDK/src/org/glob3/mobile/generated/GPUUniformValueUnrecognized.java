package org.glob3.mobile.generated; 
public class GPUUniformValueUnrecognized extends GPUUniformValue
{
    public void dispose()
    {
        super.dispose();
    }

    public GPUUniformValueUnrecognized(int type)
    {
       super(type);
    }

    public final void setUniform(GL gl, IGLUniformID id)
    {
    }

    public final boolean isEquals(GPUUniformValue v)
    {
        return getType() == v.getType();
    }

    public final String description()
    {
        return "Uniform Unrecognized";
    }
}