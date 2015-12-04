

package com.glob3mobile.utils;

import java.text.MessageFormat;
import java.util.LinkedList;


public abstract class Progress {

   private static final MessageFormat FORMATTER      = new MessageFormat("{0,number,#0.00}%");

   private long                       _steps;
   private long                       _done;
   private final long                 _started;
   private long                       _lastInformTime;
   private final long                 _timeToInform;

   private final boolean              _informThroughput;

   private long                       _lastStepsDone = 0;
   private long                       _lastElapsed   = 0;

   private final int                  _maxDeltas     = 10;
   private final LinkedList<Delta>    _deltas        = new LinkedList<Delta>();


   public Progress(final long steps) {
      this(steps, 10, false);
   }


   public Progress(final long steps,
                   final long secondsToInform) {
      this(steps, secondsToInform, false);
   }


   public Progress(final long steps,
                   final boolean informThroughput) {
      this(steps, 10, informThroughput);
   }


   public Progress(final long steps,
                   final long secondsToInform,
                   final boolean informThroughput) {
      _steps = steps;
      _done = 0;

      _timeToInform = secondsToInform * 1000;
      _informThroughput = informThroughput;

      final long now = System.currentTimeMillis();
      _started = now;
      _lastInformTime = now - _timeToInform - 1;

      processSteps();
   }


   public synchronized void incrementSteps(final long delta) {
      _steps += delta;
   }


   public synchronized double getPercent() {
      return (double) _done / _steps;
   }


   public void stepDone() {
      stepsDone(1);
   }


   private static final int FULL_BAR_STEPS = 70;


   protected String progressString(final long stepsDone,
                                   final double percent,
                                   final long elapsed,
                                   final long estimatedMsToFinish) {

      final String thrMsg = getThroughputMessage(stepsDone, elapsed);

      if (percent == 1) {
         return "[" + doneBar(1) + "] " + percentString(1) + " [Finished in " + StringUtils.getTimeMessage(elapsed) + "] | "
                + thrMsg;
      }

      final String bar = createBar(percent);

      if (percent <= 0.0005 /* 0.05% */) {
         return bar + percentString(percent) + " | ";
      }

      return bar + //
             percentString(percent) + //
             " | " + StringUtils.getTimeMessage(elapsed) + " - ETF:" + StringUtils.getTimeMessage(estimatedMsToFinish) + //
             " | " + //
             thrMsg;
   }


   private String createBar(final double percent) {
      final String doneBar = doneBar(percent);
      final String pendingBar = pendingBar(Math.max(FULL_BAR_STEPS - doneBar.length(), 0));
      return "[" + doneBar + pendingBar + "] ";
   }


   private String doneBar(final double percent) {
      final int doneBarsSteps = Math.max(Math.round((float) percent * FULL_BAR_STEPS), 0);
      // return StringUtils.sharps(doneBarsSteps);
      return StringUtils.getSubstringOfStringRepeating("#", doneBarsSteps);
   }


   private String pendingBar(final int count) {
      // return StringUtils.dashes(count);
      return StringUtils.getSubstringOfStringRepeating("-", count);
   }


   private static class Delta {
      private final long _steps;
      private final long _elapsed;


      private Delta(final long steps,
                    final long elapsed) {
         _steps = steps;
         _elapsed = elapsed;
      }

   }


   private String getThroughputMessage(final long stepsDone,
                                       final long elapsed) {
      if (!_informThroughput) {
         return "";
      }


      final long deltaSteps = stepsDone - _lastStepsDone;
      final long deltaElapsed = elapsed - _lastElapsed;

      _lastStepsDone = stepsDone;
      _lastElapsed = elapsed;

      final String instant = StringUtils.getSpaceMessage(((double) deltaSteps / deltaElapsed) * 1000) + "/s";


      final Delta delta = new Delta(deltaSteps, deltaElapsed);
      if (_deltas.size() >= _maxDeltas) {
         _deltas.removeLast();
      }
      _deltas.addFirst(delta);

      long acumSteps = 0;
      long acumElapsed = 0;
      for (final Delta d : _deltas) {
         acumSteps += d._steps;
         acumElapsed += d._elapsed;
      }
      final String deltasAverage = " l" + _deltas.size() + "="
                                   + StringUtils.getSpaceMessage(((double) acumSteps / acumElapsed) * 1000) + "/s";

      final String average = " avr=" + StringUtils.getSpaceMessage(((double) stepsDone / elapsed) * 1000) + "/s";

      return instant + deltasAverage + average + " | ";
   }


   private static String percentString(final double percent) {
      //return Math.round(100 * percent);
      final String formated = FORMATTER.format(new Object[] { 100 * percent });
      switch (formated.length()) {
         case 5:
            return "  " + formated;
         case 6:
            return " " + formated;
      }
      return formated;
   }


   public synchronized void stepsDone(final long steps) {
      _done += steps;
      processSteps();
   }


   public synchronized void finish() {
      if (_done != _steps) {
         _done = _steps;
         processSteps();
      }
   }


   private void processSteps() {
      final double percent = getPercent();

      final long now = System.currentTimeMillis();
      final long elapsedSinceLastInform = now - _lastInformTime;
      if ((percent >= 1) || (elapsedSinceLastInform > _timeToInform)) {
         _lastInformTime = now;

         final long elapsed = now - _started;
         final long estimatedMsToFinish = Math.round((elapsed / percent) - elapsed);
         informProgress(_done, percent, elapsed, estimatedMsToFinish);
      }
   }


   public abstract void informProgress(final long stepsDone,
                                       final double percent,
                                       final long elapsed,
                                       final long estimatedMsToFinish);


   public static void main(final String[] args) throws InterruptedException {
      System.out.println("GProgress 0.1");
      System.out.println("-------------\n");

      final int steps = 800;
      final int threshold = (steps * 9) / 10;
      final Progress progress = new Progress(steps, 1, true) {
         @Override
         public void informProgress(final long stepsDone,
                                    final double percent,
                                    final long elapsed,
                                    final long estimatedMsToFinish) {
            System.out.println("Task " + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
         }
      };

      for (int i = 0; i < steps; i++) {
         if (i < threshold) {
            Thread.sleep(5);
         }
         else {
            Thread.sleep(50);
         }
         progress.stepDone();
      }
      progress.finish();
   }
}
