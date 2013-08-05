package org.glob3.mobile.generated; 
public class ShaderProgram
{

  private int _program;
  private GL _gl;

  private boolean compileShader(int shader, String source)
  {
    boolean result = _gl.compileShader(shader, source);
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if DEBUG
    _gl.printShaderInfoLog(shader);
//#endif
  
    if (result)
       _gl.attachShader(_program, shader);
  
    return result;
  }
  private boolean linkProgram()
  {
    boolean result = _gl.linkProgram(_program);
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if DEBUG
    _gl.printProgramInfoLog(_program);
//#endif
  
    return result;
  }



  public ShaderProgram(GL gl)
  {
     _gl = gl;
    _program = _gl.createProgram();
  }
  public void dispose()
  {
    _gl.deleteProgram(_program);
  }

  public final boolean loadShaders(String vertexSource, String fragmentSource)
  {
    // compile vertex shader
    int vertexShader = _gl.createShader(ShaderType.VERTEX_SHADER);
    if (!compileShader(vertexShader, vertexSource))
    {
      System.out.print("ERROR compiling vertex shader\n");
      _gl.deleteShader(vertexShader);
      return false;
    }
  
    // compile fragment shader
    int fragmentShader = _gl.createShader(ShaderType.FRAGMENT_SHADER);
    if (!compileShader(fragmentShader, fragmentSource))
    {
      System.out.print("ERROR compiling fragment shader\n");
      _gl.deleteShader(fragmentShader);
      return false;
    }
  
    // link program
    if (!linkProgram())
    {
      System.out.print("ERROR linking graphic program\n");
      _gl.deleteShader(vertexShader);
      _gl.deleteShader(fragmentShader);
      _gl.deleteProgram(_program);
      return false;
    }
  
    // free shaders
    _gl.deleteShader(vertexShader);
    _gl.deleteShader(fragmentShader);
    return true;
  }


  // TEMP
  public final int getProgram()
  {
    return _program;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(ShaderProgram ");
    isb.addInt(_program);
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

}