package org.glob3.mobile.generated; 
public class GPUProgramFactory_OGL extends IGPUProgramFactory
{
   private java.util.ArrayList<GPUProgramSources> _sources = new java.util.ArrayList<GPUProgramSources>();
   private void addGPUProgramSources(GPUProgramSources s)
   {
      _sources.add(s);
   }
   private GPUProgramSources getSource(String name)
   {
      final int size = _sources.size();
      for (int i = 0; i < size; i++)
      {
         if (_sources.get(i)._name.compareTo(name) == 0)
         {
            return (_sources.get(i));
         }
      }
      return null;
   }


   public GPUProgramFactory_OGL()
   {
      BasicShadersGL2 basicShaders = new BasicShadersGL2();
      for (int i = 0; i < basicShaders.size(); i++)
      {
         addGPUProgramSources(basicShaders.get(i));
      }
   }
   public final IGPUProgram get(GL gl, String name)
   {
      //Get the source code for the shader
      final GPUProgramSources ps = getSource(name);
      GPUProgram_OGL prog;
      //Compile if the sources exist...
      if (ps != null)
      {
         prog = GPUProgram_OGL.createProgram(gl, ps._name, ps._vertexSource, ps._fragmentSource);
   
         if (prog == null)
         {
            ILogger.instance().logError("Problem at creating program named %s.", name);
            return null;
         }
         return prog;
      }
      //...or show an error if it does not
      else
      {
         ILogger.instance().logError("No shader sources for program named %s.", name);
         return null;
      }
   }

}