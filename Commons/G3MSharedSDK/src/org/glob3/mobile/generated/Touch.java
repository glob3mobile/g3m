package org.glob3.mobile.generated;import java.util.*;

//
//  TouchEvent.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//

//
//  TouchEvent.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//





public class Touch
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Vector2F _pos = new Vector2F();
  private final Vector2F _prevPos = new Vector2F();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Vector2F _pos = new internal();
  public final Vector2F _prevPos = new internal();
//#endif
  private final byte _tapCount;

  public Touch(Touch other)
  {
	  _pos = new Vector2F(other._pos);
	  _prevPos = new Vector2F(other._prevPos);
	  _tapCount = other._tapCount;
  }

  public Touch(Vector2F pos, Vector2F prev)
  {
	  this(pos, prev, (byte)0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: Touch(const Vector2F& pos, const Vector2F& prev, const byte tapCount=(byte)0): _pos(pos), _prevPos(prev), _tapCount(tapCount)
  public Touch(Vector2F pos, Vector2F prev, byte tapCount)
  {
	  _pos = new Vector2F(pos);
	  _prevPos = new Vector2F(prev);
	  _tapCount = tapCount;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector2F getPos() const
  public final Vector2F getPos()
  {
	  return _pos;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector2F getPrevPos() const
  public final Vector2F getPrevPos()
  {
	  return _prevPos;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const byte getTapCount() const
  public final byte getTapCount()
  {
	  return _tapCount;
  }

  public void dispose()
  {
  }
}
