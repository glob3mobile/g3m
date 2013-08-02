package org.glob3.mobile.generated; 
/*
 /////////////////////

 class ModelviewMatrixHolder{
 const Matrix44D** _matrix;
 const Matrix44DHolder** _matrixHolders;
 int _nMatrix;
 mutable Matrix44D* _modelview;
 public:
 ModelviewMatrixHolder(const Matrix44DHolder* matrixHolders[], int nMatrix):
 _matrixHolders(matrixHolders),
 _nMatrix(nMatrix),
 _modelview(NULL)
 {
 #ifdef C_CODE
 _matrix = new const Matrix44D*[nMatrix];
 #endif
 #ifdef JAVA_CODE
 _matrix = new Matrix44D[nMatrix];
 #endif
 for (int i = 0; i < _nMatrix; i++) {
 _matrix[i] = matrixHolders[i]->getMatrix();
 if (_matrix[i] == NULL){
 ILogger::instance()->logError("Modelview multiplication failure");
 }
 }
 }

 ~ModelviewMatrixHolder(){
 #ifdef C_CODE
 delete[] _matrix;
 delete[] _matrixHolders;
 #endif
 if (_modelview != NULL){
 _modelview->_release();
 }
 }

 Matrix44D* getModelview() const {

 if (_modelview != NULL){
 for (int i = 0; i < _nMatrix; i++) {
 const Matrix44D* m = _matrixHolders[i]->getMatrix();
 if (m == NULL){
 ILogger::instance()->logError("Modelview multiplication failure");
 }

 if (_matrix[i] != m){

 //If one matrix differs we have to raplace all matrixes on Holders and recalculate modelview
 _modelview->_release(); //NEW MODELVIEW NEEDED
 _modelview = NULL;

 for (int j = 0; j < _nMatrix; j++) {
 _matrix[j] = _matrixHolders[j]->getMatrix();
 }
 break;
 }
 }
 }


 if (_modelview == NULL){
 _modelview = new Matrix44D(*_matrix[0]);
 for (int i = 1; i < _nMatrix; i++){
 const Matrix44D* m2 = _matrix[i];
 Matrix44D* m3 = _modelview->createMultiplication(*m2);
 _modelview->_release();
 _modelview = m3;
 }
 }
 return _modelview;
 }

 };

 /////////////////////

 class GPUUniformValueModelview:public GPUUniformValue{
 protected:
 <<<<<<< HEAD
 mutable Matrix44D* _lastModelSet;
 =======
 mutable Matrix44D* _lastMatrix;
 >>>>>>> glfeature
 #ifdef C_CODE
 const ModelviewMatrixHolder _holder;
 #endif
 #ifdef JAVA_CODE
 protected ModelviewMatrixHolder _holder = null;
 #endif
 public:
 #ifdef C_CODE
 GPUUniformValueModelview(const Matrix44DHolder* matrixHolders[], int nMatrix):
 GPUUniformValue(GLType::glMatrix4Float()),
 _holder(matrixHolders, nMatrix),
 <<<<<<< HEAD
 _lastModelSet(NULL)
 =======
 _lastMatrix(NULL)
 >>>>>>> glfeature
 {
 }
 #endif
 #ifdef JAVA_CODE
 public GPUUniformValueModelview(Matrix44DHolder[] matrixHolders, int nMatrix)
 {
 super(GLType.glMatrix4Float());
 _holder = new ModelviewMatrixHolder(matrixHolders, nMatrix);
 }
 #endif
 ~GPUUniformValueModelview(){
 }

 void setUniform(GL* gl, const IGLUniformID* id) const{
 <<<<<<< HEAD
 _lastModelSet = _holder.getModelview();

 gl->uniformMatrix4fv(id, false, _holder.getModelview());
 }

 bool isEqualsTo(const GPUUniformValue* v) const{
 if (_lastModelSet == ((GPUUniformValueModelview *)v)->_holder.getModelview()){
 return true;
 }

 return false;
 =======
 _lastMatrix = _holder.getModelview();
 gl->uniformMatrix4fv(id, false, _lastMatrix);
 }

 bool isEqualsTo(const GPUUniformValue* v) const{
 return (_lastMatrix == ((GPUUniformValueModelview *)v)->_holder.getModelview());
 >>>>>>> glfeature
 }

 std::string description() const{
 IStringBuilder *isb = IStringBuilder::newStringBuilder();
 isb->addString("Uniform Value Matrix44D.");
 std::string s = isb->getString();
 delete isb;
 return s;
 }

 //  const Matrix44D* getMatrix() const { return _m;}
 };
 */

public class GPUUniformValueMatrix4 extends GPUUniformValue
{
  protected final Matrix44D _lastModelSet;
  protected final boolean _ownsProvider;
  protected Matrix44DProvider _holder = null;




  public GPUUniformValueMatrix4(Matrix44D m)
  {
     super(GLType.glMatrix4Float());
     _provider = new Matrix44DHolder(m);
     _lastModelSet = null;
     _ownsProvider = true;
  }

  public void dispose()
  {
    if (_ownsProvider)
    {
      _provider._release();
    }
  }

  public final void setUniform(GL gl, IGLUniformID id)
  {
    _lastModelSet = _provider.getMatrix();

    gl.uniformMatrix4fv(id, false, _provider.getMatrix());
  }

  public final boolean isEqualsTo(GPUUniformValue v)
  {
    if (_lastModelSet == ((GPUUniformValueMatrix4)v)._provider.getMatrix())
    {
      return true;
    }

    return false;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("Uniform Value Matrix44D.");
    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  //  const Matrix44D* getMatrix() const { return _m;}
}