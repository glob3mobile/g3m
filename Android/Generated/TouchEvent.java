package org.glob3.mobile.generated; 
public class TouchEvent
{
  private final TouchEventType _eventType;
  private final java.util.ArrayList<Touch> _touchs = new java.util.ArrayList<Touch>();

  private TouchEvent(TouchEventType type, java.util.ArrayList<Touch> touchs)
  {
	  _eventType = type;
	  _touchs = touchs;
  }

  public TouchEvent(TouchEvent other)
  {
	  _eventType = other._eventType;
	  _touchs = other._touchs;

  }

  public static TouchEvent create(TouchEventType type, java.util.ArrayList<Touch> Touchs)
  {
	return new TouchEvent(type, Touchs);
  }

  public static TouchEvent create(TouchEventType type, Touch touch)
  {
	Touch[] pa = { touch };
	final java.util.ArrayList<Touch> touchs = new java.util.ArrayList<Touch>(pa, pa+1);
	return create(type, touchs);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TouchEventType getType() const
  public final TouchEventType getType()
  {
	return _eventType;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Touch* getTouch(int i) const
  public final Touch getTouch(int i)
  {
	  return _touchs.get(i);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getNumTouch() const
  public final int getNumTouch()
  {
	  return _touchs.size();
  }

  public void dispose()
  {
	for (int i = 0; i < _touchs.size(); i++)
	{
	  if (_touchs.get(i) != null)
		  _touchs.get(i).dispose();
	}
  }

}
//
//
//class Touch {
//private:
//  const Vector2D _pos;
//  const Vector2D _prevPos;
//  
//  Touch& operator=(const Touch& v);
//  
//public:
//  Touch(const Touch& other) : _pos(other._pos), _prevPos(other._prevPos) {
//    
//  }
//  
//  Touch(const Vector2D& pos, const Vector2D& prev) : _pos(pos), _prevPos(prev) { }
//  
//  const Vector2D getPos() const { return _pos; }
//  const Vector2D getPrevPos() const { return _prevPos; }
//};
//
//
//enum TouchEventType {
//  Down,
//  Up,
//  Move,
//  LongPress
//};
//
//
//class TouchEvent {
//private:
//  const TouchEventType       _eventType;
//  const std::vector<Touch> _touchs;
//  
//  TouchEvent(const TouchEventType& type,
//             const std::vector<Touch> Touchs): _eventType(type), _touchs(Touchs) { }
//  
//public:
//  TouchEvent(const TouchEvent& other): _eventType(other._eventType), _touchs(other._touchs) {
//    
//  }
//  
//  static TouchEvent create(const TouchEventType& type,
//                           const std::vector<Touch> Touchs) {
//    return TouchEvent(type, Touchs);
//  }
//  
//  static TouchEvent create(const TouchEventType& type,
//                           const Touch& Touch) {
//    const class Touch pa[] = { Touch };
//    const std::vector<class Touch> Touchs = std::vector<class Touch>(pa, pa+1);
//    return TouchEvent::create(type, Touchs);
//  }
//  
//  TouchEventType getType() const {
//    return _eventType;
//  }  
//  
//  Touch getTouch(int i){ return _touchs[i];}
//};
//
//
//class TapEvent : Vector2D{
//public:
//  TapEvent(double x, double y): Vector2D(x,y) {}
//};

