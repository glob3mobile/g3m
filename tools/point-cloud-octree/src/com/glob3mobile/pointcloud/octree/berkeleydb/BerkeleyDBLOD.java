

package com.glob3mobile.pointcloud.octree.berkeleydb;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.glob3mobile.pointcloud.octree.Geodetic3D;
import com.glob3mobile.pointcloud.octree.PersistentLOD;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.TransactionConfig;

import es.igosoftware.io.GIOUtils;


public class BerkeleyDBLOD
         implements
            PersistentLOD {


   public static PersistentLOD openReadOnly(final String cloudName) {
      return new BerkeleyDBLOD(cloudName, false, true);
   }


   public static PersistentLOD open(final String cloudName,
                                    final boolean createIfNotExists) {
      return new BerkeleyDBLOD(cloudName, createIfNotExists, false);
   }


   public static void delete(final String cloudName) {
      final File envHome = new File(cloudName);
      if (!envHome.exists()) {
         return;
      }

      try {
         GIOUtils.cleanDirectory(envHome, false);
      }
      catch (final IOException e) {
         throw new RuntimeException(e);
      }
   }


   private static final String NODE_DATABASE_NAME      = "LODNode";
   private static final String NODE_DATA_DATABASE_NAME = "LODNodeData";


   private final boolean       _readOnly;
   private final String        _cloudName;
   private final Environment   _env;
   private final Database      _nodeDB;
   private final Database      _nodeDataDB;


   private BerkeleyDBLOD(final String cloudName,
                         final boolean createIfNotExists,
                         final boolean readOnly) {
      _readOnly = readOnly;
      _cloudName = cloudName;

      final File envHome = new File(cloudName);
      if (createIfNotExists) {
         if (!envHome.exists()) {
            envHome.mkdirs();
         }
      }

      final EnvironmentConfig envConfig = new EnvironmentConfig();
      envConfig.setAllowCreate(createIfNotExists);
      envConfig.setTransactional(true);
      envConfig.setReadOnly(readOnly);
      _env = new Environment(envHome, envConfig);

      final DatabaseConfig dbConfig = new DatabaseConfig();
      dbConfig.setAllowCreate(createIfNotExists);
      dbConfig.setTransactionalVoid(true);
      dbConfig.setKeyPrefixing(true);
      dbConfig.setReadOnly(readOnly);
      //      dbConfig.setSortedDuplicates(true);

      _nodeDB = _env.openDatabase(null, NODE_DATABASE_NAME, dbConfig);
      _nodeDataDB = _env.openDatabase(null, NODE_DATA_DATABASE_NAME, dbConfig);
   }


   @Override
   public void close() {
      _nodeDataDB.close();
      _nodeDB.close();
      _env.close();
   }


   @Override
   public String getCloudName() {
      return _cloudName;
   }


   private static byte[] toBinaryID(final String id) {
      final int length = id.length();
      final byte[] result = new byte[length];
      for (int i = 0; i < length; i++) {
         result[i] = Byte.parseByte(Character.toString(id.charAt(i)));
      }
      return result;
   }


   private static class BerkeleyDBTransaction
            implements
               PersistentLOD.Transaction {

      private final com.sleepycat.je.Transaction _txn;


      private BerkeleyDBTransaction(final com.sleepycat.je.Transaction txn) {
         _txn = txn;
      }


      @Override
      public void commit() {
         _txn.commit();
      }


      @Override
      public void rollback() {
         _txn.abort();
      }
   }

   private static final TransactionConfig DEFAULT_TRANSACTION_CONFIG = new TransactionConfig();


   @Override
   public PersistentLOD.Transaction createTransaction() {
      return new BerkeleyDBTransaction(_env.beginTransaction(null, DEFAULT_TRANSACTION_CONFIG));
   }


   Database getNodeDB() {
      return _nodeDB;
   }


   Database getNodeDataDB() {
      return _nodeDataDB;
   }


   @Override
   public void put(final PersistentLOD.Transaction transaction,
                   final String id,
                   final boolean dirty,
                   final List<Geodetic3D> points) {
      if (_readOnly) {
         throw new RuntimeException("Can't add points to readonly OT");
      }

      final com.sleepycat.je.Transaction txn = ((BerkeleyDBTransaction) transaction)._txn;
      final byte[] binaryID = toBinaryID(id);

      final BerkeleyDBLODNode node = BerkeleyDBLODNode.create(this, binaryID, dirty, points);
      node.save(txn);
   }


   @Override
   public void putOrMerge(final Transaction transaction,
                          final String id,
                          final boolean dirty,
                          final List<Geodetic3D> points) {
      final com.sleepycat.je.Transaction txn = ((BerkeleyDBTransaction) transaction)._txn;
      final byte[] binaryID = toBinaryID(id);

      final BerkeleyDBLODNode node = BerkeleyDBLODNode.fromDB(txn, this, binaryID, true);
      if (node == null) {
         put(transaction, id, dirty, points);
      }
      else {
         node.addPoints(txn, points);
         node.setDirty(dirty);
         node.save(txn);
      }
   }


   @Override
   public void acceptDepthFirstVisitor(final PersistentLOD.Transaction transaction,
                                       final PersistentLOD.Visitor visitor) {
      visitor.start(transaction);

      final CursorConfig config = new CursorConfig();
      config.setReadUncommitted(false);

      final com.sleepycat.je.Transaction txn = ((BerkeleyDBTransaction) transaction)._txn;
      try (final Cursor cursor = _nodeDB.openCursor(txn, config)) {
         final DatabaseEntry keyEntry = new DatabaseEntry();
         final DatabaseEntry dataEntry = new DatabaseEntry();

         while (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            final byte[] key = keyEntry.getData();
            final byte[] data = dataEntry.getData();

            //            final com.sleepycat.je.Transaction txn,
            //            final BerkeleyDBLOD db,
            //            final byte[] id,
            //            final boolean loadPoints

            final BerkeleyDBLODNode node = BerkeleyDBLODNode.fromDB(txn, this, key, data, false);
            final boolean keepGoing = visitor.visit(transaction, node);
            if (!keepGoing) {
               break;
            }
         }
      }

      visitor.stop(transaction);
   }


}
