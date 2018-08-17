package org.glob3.mobile.generated;import java.util.*;

public class GLFeatureSet
{
	protected static final int MAX_CONCURRENT_FEATURES_PER_GROUP = 20;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  protected GLFeature[] _features = new GLFeature[MAX_CONCURRENT_FEATURES_PER_GROUP];
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  protected protected[] final GLFeature _features = new GLFeature[MAX_CONCURRENT_FEATURES_PER_GROUP];
//#endif
  protected int _nFeatures;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GLFeatureSet(GLFeatureSet that);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GLFeatureSet operator =(GLFeatureSet that);


  public GLFeatureSet()
  {
	  _nFeatures = 0;
	for (int i = 0; i < MAX_CONCURRENT_FEATURES_PER_GROUP; i++)
	{
	  _features[i] = null;
	}
  }

  public void dispose()
  {
	  for (int i = 0; i < _nFeatures; i++)
	  {
		  _features[i]._release();
	  }
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GLFeature* get(int i) const
  public final GLFeature get(int i)
//#else
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GLFeature* get(int i) const
  public final GLFeature get(int i)
//#endif
  {
	if (_nFeatures < i)
	{
	  return null;
	}
	return _features[i];
  }

  public final void add(GLFeature f)
  {
	  _features[_nFeatures++] = f;
	  f._retain();
  
	  if (_nFeatures >= MAX_CONCURRENT_FEATURES_PER_GROUP)
	  {
		  ILogger.instance().logError("MAX_CONCURRENT_FEATURES_PER_GROUP REACHED");
	  }
  }

  public final void add(GLFeatureSet fs)
  {
	  final int size = fs.size();
	  for (int i = 0; i < size; i++)
	  {
		  add(fs.get(i));
	  }
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int size() const
  public final int size()
  {
	return _nFeatures;
  }

  public final void clearFeatures(GLFeatureGroupName g)
  {
  
	  for (int i = 0; i < _nFeatures; i++)
	  {
		  final GLFeature f = _features[i];
		  if (f._group == g)
		  {
			  f._release();
  
			  for (int j = i; j < _nFeatures; j++)
			  {
				  if (j+1 >= MAX_CONCURRENT_FEATURES_PER_GROUP)
				  {
					  _features[j] = null;
				  }
				  else
				  {
					  _features[j] = _features[j+1];
				  }
			  }
			  i--;
			  _nFeatures--;
		  }
	  }
  
  }
  public final void clearFeatures()
  {
  
	  for (int i = 0; i < _nFeatures; i++)
	  {
		  final GLFeature f = _features[i];
		  f._release();
  
		  for (int j = i; j < _nFeatures; j++)
		  {
			  if (j+1 >= MAX_CONCURRENT_FEATURES_PER_GROUP)
			  {
				  _features[j] = null;
			  }
			  else
			  {
				  _features[j] = _features[j+1];
			  }
		  }
		  i--;
		  _nFeatures--;
	  }
  
  }

}
