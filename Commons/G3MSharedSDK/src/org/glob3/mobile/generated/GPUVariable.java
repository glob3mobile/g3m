package org.glob3.mobile.generated; 
public class GPUVariable
{


  protected final GPUVariableType _variableType;
  protected final String _name;

  //Uniform metadata based in our shaders
  protected int _key;
  protected int _group;
  protected int _priority;

  protected final void createMetadata()
  {
    _group = GROUP_NOGROUP;
    _priority = -1;
    _key = getKeyForName(_name, _variableType);
  
    if (_key == UNRECOGNIZED)
    {
      ILogger.instance().logError("Unrecognized GPU VARAIBLE %s\n", _name);
    }
  
    if (_variableType == GPUVariableType.UNIFORM)
    {
      if (_key == FLAT_COLOR)
      {
        _group = GROUP_COLOR;
      }
  
      if (_key == TEXTURE_EXTENT)
      {
        _group = GROUP_COLOR;
      }
  
      if (_key == TRANSLATION_TEXTURE_COORDS)
      {
        _group = GROUP_COLOR;
      }
  
      if (_key == TRANSLATION_TEXTURE_COORDS)
      {
        _group = GROUP_COLOR;
      }
  
      if (true) //DELETE
      {
        if (_key == EnableColorPerVertex)
        {
          _group = GROUP_COLOR;
        }
  
        if (_key == EnableTexture)
        {
          _group = GROUP_COLOR;
        }
  
        if (_key == EnableFlatColor)
        {
          _group = GROUP_COLOR;
        }
  
        if (_key == FlatColorIntensity)
        {
          _group = GROUP_COLOR;
        }
  
        if (_key == ColorPerVertexIntensity)
        {
          _group = GROUP_COLOR;
        }
      }
    }
  
    if (_variableType == GPUVariableType.ATTRIBUTE)
    {
  
      if (_key == COLOR)
      {
        _group = GROUP_COLOR;
      }
  
      if (_key == TEXTURE_COORDS)
      {
        _group = GROUP_COLOR;
      }
    }
  
  
  }



  public static final int UNRECOGNIZED = -1;

  public static final int FLAT_COLOR = 1;
  public static final int MODELVIEW = 2;
  public static final int TEXTURE_EXTENT = 3;
  public static final int VIEWPORT_EXTENT = 4;
  public static final int TRANSLATION_TEXTURE_COORDS = 5;
  public static final int SCALE_TEXTURE_COORDS = 6;
  public static final int POINT_SIZE = 7;

  public static final int POSITION = 8;
  public static final int TEXTURE_COORDS = 9;
  public static final int COLOR = 10;

  //To be deleted
  public static final int EnableColorPerVertex = 11;
  public static final int EnableTexture = 12;
  public static final int EnableFlatColor = 13;
  public static final int FlatColorIntensity = 14;
  public static final int ColorPerVertexIntensity = 15;

  public static int getKeyForName(String name, int variableType)
  {
  
    if (variableType == GPUVariableType.UNIFORM.getValue())
    {
      if (name.compareTo("uFlatColor") == 0)
      {
        return FLAT_COLOR;
      }
  
      if (name.compareTo("uModelview") == 0)
      {
        return MODELVIEW;
      }
  
      if (name.compareTo("uTextureExtent") == 0)
      {
        return TEXTURE_EXTENT;
      }
  
      if (name.compareTo("uViewPortExtent") == 0)
      {
        return VIEWPORT_EXTENT;
      }
  
      if (name.compareTo("uTranslationTexCoord") == 0)
      {
        return TRANSLATION_TEXTURE_COORDS;
      }
  
      if (name.compareTo("uScaleTexCoord") == 0)
      {
        return SCALE_TEXTURE_COORDS;
      }
  
      if (name.compareTo("uPointSize") == 0)
      {
        return POINT_SIZE;
      }
  
      if (true) //TO BE DELETED
      {
        if (name.compareTo("EnableColorPerVertex") == 0)
        {
          return EnableColorPerVertex;
        }
  
        if (name.compareTo("EnableTexture") == 0)
        {
          return EnableTexture;
        }
  
        if (name.compareTo("EnableFlatColor") == 0)
        {
          return EnableFlatColor;
        }
  
        if (name.compareTo("FlatColorIntensity") == 0)
        {
          return FlatColorIntensity;
        }
  
        if (name.compareTo("ColorPerVertexIntensity") == 0)
        {
          return ColorPerVertexIntensity;
        }
      }
    }
  
    if (variableType == GPUVariableType.ATTRIBUTE.getValue())
    {
      if (name.compareTo("aPosition") == 0)
      {
        return POSITION;
      }
  
      if (name.compareTo("aColor") == 0)
      {
        return COLOR;
      }
  
      if (name.compareTo("aTextureCoord") == 0)
      {
        return TEXTURE_COORDS;
      }
    }
  
    return UNRECOGNIZED;
  }



  public static final int GROUP_COLOR = 1;
  public static final int GROUP_NOGROUP = -1;

  public void dispose()
  {
  }

  public GPUVariable(String name, GPUVariableType type)
  {
     _name = name;
     _variableType = type;
    createMetadata();
  }

  //Uniform metadata based in our shaders
  public final int getKey()
  {
     return _key;
  }
  public final int getGroup()
  {
     return _group;
  }
  public final int getPriority()
  {
     return _priority;
  }

}
//TODO: DELETE
