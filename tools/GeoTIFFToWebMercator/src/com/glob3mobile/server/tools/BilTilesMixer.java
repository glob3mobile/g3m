package com.glob3mobile.server.tools;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.glob3mobile.server.tools.pyramid.BilMergedPyramid;
import com.glob3mobile.server.tools.pyramid.SourcePyramid;
import com.glob3mobile.utils.CollectionsUtils;
import com.glob3mobile.utils.Function;
import com.glob3mobile.utils.IOUtils;
import com.glob3mobile.utils.Logger;
import com.glob3mobile.utils.Progress;

public class BilTilesMixer {

	   public static void processSubdirectories(final String inputDirectoryName,
	                                            final String outputDirectoryName,
	                                            final int pyramidType) throws IOException {
	      final File inputDirectory = new File(inputDirectoryName);
	      if (!inputDirectory.exists()) {
	         throw new IOException("Input directory \"" + inputDirectoryName + "\" doesn't exist");
	      }

	      if (!inputDirectory.isDirectory()) {
	         throw new IOException("\"" + inputDirectoryName + "\" is not a directory");
	      }

	      final FilenameFilter filter = new FilenameFilter() {
	         @Override
	         public boolean accept(final File dir,
	                               final String name) {
	            return name.endsWith(".biltiles") && new File(dir, name).isDirectory();
	         }
	      };

	      final Function<String, String> function = new Function<String, String>() {
	         @Override
	         public String evaluate(final String source) {
	            return new File(inputDirectory, source).getAbsolutePath();
	         }
	      };
	      final List<String> inputDirectoriesNames = CollectionsUtils.map(Arrays.asList(inputDirectory.list(filter)), function);


	      final BilTilesMixer mixer = new BilTilesMixer(inputDirectoriesNames, outputDirectoryName);
	      mixer.process(pyramidType);
	   }


	   public static void processDirectories(final List<String> inputDirectoriesNames,
	                                         final String outputDirectoryName,
	                                         final int pyramidType) throws IOException {
	      final BilTilesMixer mixer = new BilTilesMixer(inputDirectoriesNames, outputDirectoryName);
	      mixer.process(pyramidType);
	   }
	   
	   public static void processResultForSimilarity(final String resultDirectoryName,int pyramidType,int similarityErrorMethod) throws IOException {
		   final BilTilesMixer mixer = new BilTilesMixer (resultDirectoryName);
		   mixer.similarity(pyramidType,similarityErrorMethod);
	   }
	   
	   public static void processResultForRedim(final String resultDirectoryName, final String redimDirectoryName,
			   int pyramidType, int similarityErrorMethod) throws IOException{
		   final BilTilesMixer mixer = new BilTilesMixer(resultDirectoryName, redimDirectoryName);
		   mixer.redim(pyramidType,similarityErrorMethod);
	   }


	   private final File[] _inputDirectories;
	   private final File   _outputDirectory;


	   BilTilesMixer(final List<String> inputDirectoriesNames,
	              final String outputDirectoryName) throws IOException {
	      _inputDirectories = new File[inputDirectoriesNames.size()];
	      for (int i = 0; i < inputDirectoriesNames.size(); i++) {
	         final String inputDirectoryName = inputDirectoriesNames.get(i);
	         final File inputDirectory = new File(inputDirectoryName);
	         IOUtils.checkDirectory(inputDirectory);
	         _inputDirectories[i] = inputDirectory;
	      }

	      _outputDirectory = new File(outputDirectoryName);
	      IOUtils.ensureEmptyDirectory(_outputDirectory);
	   }
	   
	   BilTilesMixer ( final String resultDirectoryName ) throws IOException  {
		   _inputDirectories = new File[1];
		   File inputDir = new File(resultDirectoryName);
		   IOUtils.checkDirectory(inputDir);
		   _inputDirectories[0] = inputDir;
		   _outputDirectory = null;
	   }
	   
	   BilTilesMixer (final String resultDirectoryName, final String redimDirectoryName) throws IOException {
		   _inputDirectories = new File[1];
		   File inputDir = new File(resultDirectoryName);
		   IOUtils.checkDirectory(inputDir);
		   _inputDirectories[0] = inputDir;
		   _outputDirectory = new File(redimDirectoryName);
		   IOUtils.ensureEmptyDirectory(_outputDirectory);
	   }


	   private SourcePyramid[] getSourcePyramids() throws IOException {
	      final int length = _inputDirectories.length;
	      final SourcePyramid[] sourcePyramids = new SourcePyramid[length];
	      for (int i = 0; i < length; i++) {
	         final File inputDirectory = _inputDirectories[i];
	         sourcePyramids[i] = new SourcePyramid(inputDirectory, true);
	      }
	      return sourcePyramids;
	   }

	   private static class DefaultThreadFactory
	            implements
	               ThreadFactory {
	      private static final AtomicInteger poolNumber    = new AtomicInteger(1);

	      private final ThreadGroup          _group;
	      private final AtomicInteger        _threadNumber = new AtomicInteger(1);
	      private final String               _namePrefix;
	      private final int                  _threadPriority;


	      DefaultThreadFactory(final int threadPriority) {
	         final SecurityManager s = System.getSecurityManager();
	         _group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
	         _namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
	         _threadPriority = threadPriority;
	      }


	      @Override
	      public Thread newThread(final Runnable runnable) {
	         final Thread t = new Thread(_group, runnable, _namePrefix + _threadNumber.getAndIncrement(), 0);
	         t.setDaemon(false);
	         t.setPriority(_threadPriority);
	         return t;
	      }
	   }


	   private static ThreadFactory defaultThreadFactory(final int threadPriority) {
	      return new DefaultThreadFactory(threadPriority);
	   }


	   private void process(int pyramidType) throws IOException {
	      final BilMergedPyramid mergedPyramid = new BilMergedPyramid(getSourcePyramids(),pyramidType);

	      final int scaleFactor = 2;
	      final int cpus = Runtime.getRuntime().availableProcessors();
	      final int maxThreads = Math.max(cpus * scaleFactor, 1);

	      final ThreadPoolExecutor executor = new ThreadPoolExecutor(0, maxThreads, 10, TimeUnit.SECONDS,
	               new SynchronousQueue<Runnable>(), defaultThreadFactory(Thread.NORM_PRIORITY));
	      executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());


	      final Object mutex = new Object();

	      final long steps = mergedPyramid.getTilesCount();

	      final Progress progress = new Progress(steps, 10, false) {
	         @Override
	         public void informProgress(final long stepsDone,
	                                    final double percent,
	                                    final long elapsed,
	                                    final long estimatedMsToFinish) {
	            Logger.log("Merging " + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
	         }
	      };

	      mergedPyramid.process(_outputDirectory, progress, executor, mutex);

	      executor.shutdown();
	      try {
	         executor.awaitTermination(2, TimeUnit.DAYS);
	      }
	      catch (final InterruptedException e) {
	         throw new RuntimeException(e);
	      }
	      progress.finish();

	      Logger.log("done!");
	   }
	   
	   private void similarity(int pyramidType,int similarityErrorMethod) throws IOException {
		   final BilMergedPyramid mergedPyramid = new BilMergedPyramid(getSourcePyramids(),pyramidType);
		   mergedPyramid.similarity(similarityErrorMethod);
	   }
	   
	   
	   private void redim(int pyramidType, int similarityErrorMethod) throws IOException {
		   final BilMergedPyramid mergedPyramid = new BilMergedPyramid(getSourcePyramids(),pyramidType);
		   mergedPyramid.redim(similarityErrorMethod,_outputDirectory.getAbsolutePath());
	   }
	   
	   
	   
	   
	   ////////////////
	   
	   final static int OP_MERGE = 0;
	   final static int OP_SIMILARITY = 1;
	   final static int OP_REDIM = 2;


	   public static void main(final String[] args) throws IOException {
	      System.out.println("BilTilesMixer 0.1");
	      System.out.println("--------------\n");

	      final String inputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/";
	      final String outputDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/result";
	      final String redimDirectoryName = "/users/sebastianortegatrujillo/Desktop/Elevs combo/redim";
	      
	      final int pyramidType = Pyramid.PYR_WEBMERC;
	      final int similarityErrorType = BilMergedPyramid.SIMILARITY_MAX_ERROR;
	      //TODO: Remember to change this parameter!
	      final int op = OP_REDIM;
	      
	      BilMergedPyramid.setTileImageDimensions(32);
	      
	      switch (op) {
	      	case OP_MERGE:
	      		BilTilesMixer.processSubdirectories(inputDirectoryName, outputDirectoryName, pyramidType);
	      		break;
	      	case OP_SIMILARITY:
	      		BilTilesMixer.processResultForSimilarity(outputDirectoryName, pyramidType,similarityErrorType);
	      		break;
	      	case OP_REDIM:
	      		BilTilesMixer.processResultForRedim(outputDirectoryName, redimDirectoryName, pyramidType, similarityErrorType);
	      		break;
	      }
	   }
	}

