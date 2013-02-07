//
//  ITexturizerData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/7/13.
//
//

#ifndef __G3MiOSSDK__ITexturizerData__
#define __G3MiOSSDK__ITexturizerData__

//class ITexturizerData {
//public:
////  virtual bool isTexturizerData() const = 0; //Java needs to know that this is an interface
//#ifdef C_CODE
//  virtual ~ITexturizerData() { }
//#endif
//#ifdef JAVA_CODE
//  public void dispose();
//#endif
//};

#ifdef C_CODE
class ITexturizerData {
public:
  virtual ~ITexturizerData() { }
};
#endif

#ifdef JAVA_CODE
public interface ITexturizerData {
  public void dispose();
}
#endif

#endif
