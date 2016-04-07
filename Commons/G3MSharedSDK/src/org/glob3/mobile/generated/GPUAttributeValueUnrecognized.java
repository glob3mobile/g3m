package org.glob3.mobile.generated; 
////////

///////////
public class GPUAttributeValueUnrecognized extends GPUAttributeValue
{
    public void dispose()
    {
        super.dispose();
    }

    public GPUAttributeValueUnrecognized(int type)
    {
       super(type, 0, 0, 0, 0, false);
    }

    public final void setAttribute(GL gl, int id)
    {
    }

    public final boolean isEquals(GPUAttributeValue v)
    {
        return (v._type == _type);
    }

    public final GPUAttributeValue shallowCopy()
    {
        return new GPUAttributeValueUnrecognized(_type);
    }

    public final String description()
    {
        return "Attribute Unrecognized.";
    }

    public final GPUAttributeValue copyOrCreate(GPUAttributeValue oldAtt)
    {
        if (oldAtt == null)
        {
            return new GPUAttributeValueUnrecognized(_type);
        }
        if (oldAtt._enabled)
        {
            oldAtt._release();
            return new GPUAttributeValueUnrecognized(_type);
        }
        return oldAtt;
    }

}
////////


