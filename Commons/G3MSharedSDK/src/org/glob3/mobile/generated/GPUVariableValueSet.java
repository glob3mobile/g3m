package org.glob3.mobile.generated;import java.util.*;

//
//  GPUVariableValueSet.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

//
//  GPUVariableValueSet.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//



public class GPUVariableValueSet
{
  private GPUUniformValue[] _uniformValues = new GPUUniformValue[32];
  private GPUAttributeValue[] _attributeValues = new GPUAttributeValue[32];
  private int _highestUniformKey;
  private int _highestAttributeKey;

  private int _uniformsCode;
  private int _attributeCode;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GPUVariableValueSet(GPUVariableValueSet that);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GPUVariableValueSet operator =(GPUVariableValueSet that);


  public GPUVariableValueSet()
  {
	  _highestAttributeKey = 0;
	  _highestUniformKey = 0;
	  _uniformsCode = 0;
	  _attributeCode = 0;
	for (int i = 0; i < 32; i++)
	{
	  _uniformValues[i] = null;
	  _attributeValues[i] = null;
	}
  }

  public void dispose()
  {
	for (int i = 0; i <= _highestUniformKey; i++)
	{
	  GPUUniformValue u = _uniformValues[i];
	  if (u != null)
	  {
		u._release();
	  }
	}
  
	for (int i = 0; i <= _highestAttributeKey; i++)
	{
	  GPUAttributeValue a = _attributeValues[i];
	  if (a != null)
	  {
		a._release();
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean containsUniform(GPUUniformKey key) const
  public final boolean containsUniform(GPUUniformKey key)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final int index = (int)key;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	final int index = (int)key.getValue();
//#endif

	return _uniformValues[index] != null;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean containsAttribute(GPUAttributeKey key) const
  public final boolean containsAttribute(GPUAttributeKey key)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final int index = (int)key;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	final int index = (int)key.getValue();
//#endif

	return _attributeValues[index] != null;
  }

  public final void addUniformValue(GPUUniformKey key, GPUUniformValue v, boolean mustRetain)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final int index = (int)key;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	final int index = (int)key.getValue();
//#endif

	_uniformValues[index] = v;
	if (mustRetain)
	{
	  v._retain();
	}
	if (index > _highestUniformKey)
	{
	  _highestUniformKey = index;
	}
  }

  public final void removeUniformValue(GPUUniformKey key)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final int index = (int)key;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	final int index = (int)key.getValue();
//#endif

	if (_uniformValues[index] != null)
	{
	  _uniformValues[index]._release();
	  _uniformValues[index] = null;
	}

	for (int i = 0; i < 32; i++)
	{
	  if (_uniformValues[i] != null)
	  {
		_highestUniformKey = i;
	  }
	}
  }

  public final void addAttributeValue(GPUAttributeKey key, GPUAttributeValue v, boolean mustRetain)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final int index = (int)key;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	final int index = (int)key.getValue();
//#endif
	_attributeValues[index] = v;
	if (mustRetain)
	{
	  v._retain();
	}
	if (index > _highestAttributeKey)
	{
	  _highestAttributeKey = index;
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GPUAttributeValue* getAttributeValue(int key) const
  public final GPUAttributeValue getAttributeValue(int key)
  {
	return _attributeValues[key];
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GPUUniformValue* getUniformValue(int key) const
  public final GPUUniformValue getUniformValue(int key)
  {
	return _uniformValues[key];
  }

  public final void combineWith(GPUVariableValueSet vs)
  {
	for (int i = 0; i <= vs._highestUniformKey; i++)
	{
	  if (vs._uniformValues[i] != null)
	  {
		_uniformValues[i] = vs._uniformValues[i];
		_uniformValues[i]._retain();
		if (i > _highestUniformKey)
		{
		  _highestUniformKey = i;
		}
	  }
	}
  
	for (int i = 0; i <= vs._highestAttributeKey; i++)
	{
	  if (vs._attributeValues[i] != null)
	  {
		_attributeValues[i] = vs._attributeValues[i];
		_attributeValues[i]._retain();
		if (i > _highestAttributeKey)
		{
		  _highestAttributeKey = i;
		}
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void applyValuesToProgram(GPUProgram* prog) const
  public final void applyValuesToProgram(GPUProgram prog)
  {
	for (int i = 0; i <= _highestUniformKey; i++)
	{
	  GPUUniformValue u = _uniformValues[i];
	  if (u != null)
	  {
		prog.setGPUUniformValue(i, u);
	  }
	}
  
	for (int i = 0; i <= _highestAttributeKey; i++)
	{
	  GPUAttributeValue a = _attributeValues[i];
	  if (a != null)
	  {
		prog.setGPUAttributeValue(i, a);
	  }
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getUniformsCode() const
  public final int getUniformsCode()
  {
	if (_uniformsCode == 0)
	{
	  for (int i = 0; i <= _highestUniformKey; i++)
	  {
		if (_uniformValues[i] != null)
		{
		  _uniformsCode = _uniformsCode | GPUVariable.getUniformCode(i);
		}
	  }
	}
	return _uniformsCode;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getAttributesCode() const
  public final int getAttributesCode()
  {
	if (_attributeCode == 0)
	{
	  for (int i = 0; i <= _highestAttributeKey; i++)
	  {
		if (_attributeValues[i] != null)
		{
		  _attributeCode = _attributeCode | GPUVariable.getAttributeCode(i);
		}
	  }
	}
	return _attributeCode;
  }

}
