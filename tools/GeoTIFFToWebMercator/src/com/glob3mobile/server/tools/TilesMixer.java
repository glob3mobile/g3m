

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

import com.glob3mobile.server.tools.pyramid.MergedPyramid;
import com.glob3mobile.server.tools.pyramid.SourcePyramid;
import com.glob3mobile.utils.CollectionsUtils;
import com.glob3mobile.utils.Function;
import com.glob3mobile.utils.IOUtils;
import com.glob3mobile.utils.Logger;
import com.glob3mobile.utils.Progress;


public class TilesMixer {

   public static void processSubdirectories(final String inputDirectoryName,
                                            final String outputDirectoryName) throws IOException {
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
            return name.endsWith(".tiles") && new File(dir, name).isDirectory();
         }
      };

      final Function<String, String> function = new Function<String, String>() {
         @Override
         public String evaluate(final String source) {
            return new File(inputDirectory, source).getAbsolutePath();
         }
      };
      final List<String> inputDirectoriesNames = CollectionsUtils.map(Arrays.asList(inputDirectory.list(filter)), function);


      final TilesMixer mixer = new TilesMixer(inputDirectoriesNames, outputDirectoryName);
      mixer.process();
   }


   public static void processDirectories(final List<String> inputDirectoriesNames,
                                         final String outputDirectoryName) throws IOException {
      final TilesMixer mixer = new TilesMixer(inputDirectoriesNames, outputDirectoryName);
      mixer.process();
   }


   private final File[] _inputDirectories;
   private final File   _outputDirectory;


   TilesMixer(final List<String> inputDirectoriesNames,
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


   private SourcePyramid[] getSourcePyramids() throws IOException {
      final int length = _inputDirectories.length;
      final SourcePyramid[] sourcePyramids = new SourcePyramid[length];
      for (int i = 0; i < length; i++) {
         final File inputDirectory = _inputDirectories[i];
         sourcePyramids[i] = new SourcePyramid(inputDirectory, false);
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
         //         if (t.isDaemon()) {
         t.setDaemon(false);
         //         }
         //         if (t.getPriority() != _threadPriority) {
         t.setPriority(_threadPriority);
         //         }
         return t;
      }
   }


   private static ThreadFactory defaultThreadFactory(final int threadPriority) {
      return new DefaultThreadFactory(threadPriority);
   }


   private void process() throws IOException {
      final MergedPyramid mergedPyramid = new MergedPyramid(getSourcePyramids());
      //mergedPyramid.merge(_outputDirectory);

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


   public static void main(final String[] args) throws IOException {
      System.out.println("TilesMixer 0.1");
      System.out.println("--------------\n");

      //      final List<String> inputDirectoriesNames = Arrays.asList( //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/1-TrueMarble_2km_21600x10800_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N40-E006_ul_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E000_ur_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E012_ll_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N50-E000_ur_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/MUC_QB_tile1_tif_comp_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N40-E006_ur_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E006_ll_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E012_ll_tif_comp_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N50-E006_ll_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/MUC_QB_tile2_tif_comp_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N40-E012_ll_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E006_ll_tif_comp_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E012_lr_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N50-E006_lr_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N40-E000_ll_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N40-E012_lr_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E006_lr_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E012_ul_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N50-E006_ul_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N40-E000_lr_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N40-E012_ul_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E006_lr_tif_comp_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E012_ul_tif_comp_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N50-E006_ur_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N40-E000_ul_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N40-E012_ur_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E006_ul_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E012_ur_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N50-E012_ll_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N40-E000_ur_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E000_ll_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E006_ul_tif_comp_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N50-E000_ll_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N50-E012_lr_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N40-E006_ll_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E000_lr_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E006_ur_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N50-E000_lr_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N50-E012_ul_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N40-E006_lr_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E000_ul_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N45-E006_ur_tif_comp_tif.tiles", // 
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N50-E000_ul_4as_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/N50-E012_ur_4as_tif.tiles" //
      //      );

      //      final List<String> inputDirectoriesNames = Arrays.asList( //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/1-TrueMarble_2km_21600x10800_tif.tiles", //
      //               //  "/Users/dgd/Desktop/LH-Imagery/_result_/N40-W072_ll_tif.tiles", //
      //               "/Users/dgd/Desktop/LH-Imagery/_result_/MUC_QB_tile1_tif_comp_tif.tiles" //
      //      //"/Users/dgd/Desktop/LH-Imagery/_result_/3-MUC_QB_tile2_tif_comp_tif.tiles" //
      //      );

      final String outputDirectoryName = "/Users/dgd/Desktop/LH-Imagery/_merged";

      //  TilesMixer.processDirectories(inputDirectoriesNames, outputDirectoryName);

      final String inputDirectoryName = "/Users/dgd/Desktop/LH-Imagery/_result_/";
      TilesMixer.processSubdirectories(inputDirectoryName, outputDirectoryName);
   }
}
