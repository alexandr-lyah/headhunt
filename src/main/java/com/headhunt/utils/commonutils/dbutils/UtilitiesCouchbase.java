package com.headhunt.utils.commonutils.dbutils;

import com.couchbase.client.ClusterManager;
import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.clustermanager.BucketType;
import net.spy.memcached.internal.OperationFuture;
import net.spy.memcached.ops.OperationStatus;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by sagraw001c on 11/21/14.
 */

public class UtilitiesCouchbase {

    private final ClusterManager cluster_manager;

    public UtilitiesCouchbase(ClusterManager cluster_manager) {
        this.cluster_manager = cluster_manager;
    }

    public void shutdown() {
        cluster_manager.shutdown();
    }

    public CouchbaseClient getCouchbaseClient(List<URI> hosts, String bucketName, String bucketPassword) {
        try {
            return new CouchbaseClient(hosts, bucketName, bucketPassword );
        } catch (IOException ex) {
            System.out.println("E Error in initiating couchbase client: " + ex);
        }
        return null;
    }

    public void createBucket(BucketType type, String bucketName, int memoryMB, int replicas, int port, boolean flush) {
        System.out.println("I Create bucket '" + bucketName + "' with Cluster Manager"
                + " [memory:" + memoryMB + ", replicas: " + replicas + ", port: " + port + ", flush-enabled: " + flush);
        try {
            cluster_manager.createPortBucket(type, bucketName, memoryMB, replicas, port, flush);
            Thread.sleep(1000);
        } catch (Exception ex) {
            System.out.println("E Error while creating bucket [" + bucketName + "] " + ex);
        }
    }

    public void deleteBucket(String bucketName) {
        System.out.println("I Deleting bucket '" + bucketName + "' with Cluster Manager");
        try {
            cluster_manager.deleteBucket(bucketName);
        } catch (Exception ex) {
            System.out.println("E Error while deleting bucket [" + bucketName + "] " + ex);
        }
    }

    public void flushBucket(String bucketName) {
        System.out.println("I Flushing bucket '" + bucketName + "' with Cluster Manager");
        try {
            cluster_manager.flushBucket(bucketName);
        } catch (Exception ex) {
            System.out.println("E Error while deleting bucket [" + bucketName + "] " + ex);
        }
    }

    public boolean checkBucket(String bucketName) {
        boolean present = false;
        List<String> buckets = cluster_manager.listBuckets();
        if ( buckets.contains( bucketName ) ) {
            present = true;
        }
        return present;
    }

    public boolean couchbaseSet( CouchbaseClient client, String key, Object val ) {
        try {
            return client.set(key, val).get();
        } catch (ExecutionException | InterruptedException ex) {
            System.out.println("E Error in inserting data into couchbase: " + ex);
        }
        return false;
    }

    public Object couchbaseGet( CouchbaseClient client, String key ) {
        return client.get(key);
    }
    
    /**
     * Continuously try a set with exponential backoff until number of tries or
     * successful.  The exponential backoff will wait a maximum of 1 second, or
     * whatever
     *
     * @param client
     * @param key
     * @param value
     * @param tries number of tries before giving up
     * @return the OperationFuture<Boolean> that wraps this set operation
     */
    public OperationFuture<Boolean> couchbaseSet(CouchbaseClient client, String key, Object value, int tries) {
        OperationFuture<Boolean> result = null;
        OperationStatus status;
        int backoffexp = 0;
        try {
            do {
                if (backoffexp > tries) {
                    throw new RuntimeException("Could not perform a set after " + tries + " tries.");
                }
                result = client.set(key, value);
                status = result.getStatus();
                if (status.isSuccess()) {
                    break;
                }
                if (backoffexp > 0) {
                    double backoffMillis = Math.pow(2, backoffexp);
                    backoffMillis = Math.min(1000, backoffMillis); // 1 sec max
                    Thread.sleep((int) backoffMillis);
                    System.err.println("Backing off, tries so far: " + backoffexp);
                }
                backoffexp++;
                if (!status.isSuccess()) {
                    System.err.println("Failed with status: " + status.getMessage());
                }
            } while (status.getMessage().equals("Temporary failure"));
        } catch (InterruptedException ex) {
            System.err.println("Interrupted while trying to set.  Exception:"
                    + ex.getMessage());
        }

        if (result == null) {
            throw new RuntimeException("Could not carry out operation."); // rare
        }

        // note that other failure cases fall through.  status.isSuccess() can be
        // checked for success or failure or the message can be retrieved.
        return result;
    }

}
