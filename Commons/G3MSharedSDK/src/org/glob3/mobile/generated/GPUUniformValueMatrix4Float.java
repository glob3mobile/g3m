

package org.glob3.mobile.generated;

public class GPUUniformValueMatrix4Float
         extends
            GPUUniformValue {
   public final MutableMatrix44D _m;


   public GPUUniformValueMatrix4Float(final MutableMatrix44D m) {
      super(GLType.glMatrix4Float());
      _m = new MutableMatrix44D(m);
   }


   @Override
   public final void setUniform(final GL gl,
                                final IGLUniformID id) {
      gl.uniformMatrix4fv(id, false, _m);
   }


   @Override
   public final boolean isEqualsTo(final GPUUniformValue v) {
      final GPUUniformValueMatrix4Float v2 = (GPUUniformValueMatrix4Float) v;
      final MutableMatrix44D m = (v2._m);
      return _m.isEqualsTo(m);
   }


   @Override
   public final GPUUniformValue deepCopy() {
      return new GPUUniformValueMatrix4Float(_m);
   }


   @Override
   public final String description() {
      final IStringBuilder isb = IStringBuilder.newStringBuilder();
      isb.addString("Uniform Value Matrix44D.");
      final String s = isb.getString();
      if (isb != null) {
         isb.dispose();
      }
      return s;
   }


   public final MutableMatrix44D getValue() {
      return _m;
   }
}
