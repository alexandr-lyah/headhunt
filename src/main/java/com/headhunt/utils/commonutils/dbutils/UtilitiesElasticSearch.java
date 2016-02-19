package com.headhunt.utils.commonutils.dbutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.node.Node;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * Created by sagraw200 on 6/3/2015.
 */
public class UtilitiesElasticSearch {

    private static Client _esclient = null;

    public UtilitiesElasticSearch() {
        Node node = nodeBuilder().node();
        _esclient = node.client();
    }

    public boolean indexDoc(JSONObject doc, String index, String type) {
        if (!indexExist(index)) {
            createIndex(index);
        }

        boolean result = false;
        try {
            IndexRequestBuilder indexRequestBuilder = _esclient.prepareIndex(index, type);
            IndexResponse response = indexRequestBuilder.setSource(doc).execute().actionGet();
            result = response.isCreated();
        } catch (Exception ex) {
            System.out.println("Error in indexing elasticsearch doc: " + ex);
        }
        return result;
    }

    public boolean createIndex(String index) {
        CreateIndexRequestBuilder createIndexRequestBuilder = _esclient.admin().indices().prepareCreate(index);
        CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
        return response.isAcknowledged();
    }
    public boolean indexExist(String indexName) {
        final IndicesExistsResponse res = _esclient.admin().indices().prepareExists(indexName).execute().actionGet();
        return res.isExists();
    }

    public boolean indexDelete(String indexName) {
        boolean res = false;
        try {
            final DeleteIndexRequestBuilder delIdx = _esclient.admin().indices().prepareDelete(indexName);
            delIdx.execute().actionGet();
            res = true;
        } catch (Exception ex) {
            System.out.println("E error in deleting index: " + indexName);
        }
        return res;
    }

    public boolean typeExist(String indexName, String typeName) {
        boolean present = false;

        ClusterStateResponse clusterStateResponse = _esclient.admin().cluster().prepareState().execute().actionGet();
        //ImmutableOpenMap<String, MappingMetaData> indexMappings = clusterStateResponse.getState().getMetaData().index(indexName).getMappings();

        IndexMetaData iMetadata = clusterStateResponse.getState()
                .getMetaData().index(indexName);
        if (iMetadata != null) {
            ImmutableOpenMap<String, MappingMetaData> indexMappings = iMetadata.getMappings();

            Iterator<ObjectObjectCursor<String, MappingMetaData>> mappingIter = indexMappings.iterator();
            while (mappingIter.hasNext()) {
                ObjectObjectCursor<String, MappingMetaData> obj = mappingIter.next();
                if (typeName.equals(obj.key)) {
                    present = true;
                    break;
                }
            }
        }
        return present;
    }

    public Map<String, Object> getTypeMapping(String indexName, String typeName) {
        Map<String, Object> sourceAsMap = null;

        ClusterStateResponse clusterStateResponse = _esclient.admin().cluster().prepareState().execute().actionGet();
        ImmutableOpenMap<String, MappingMetaData> indexMappings = clusterStateResponse.getState()
                .getMetaData().index(indexName).getMappings();

        Iterator<ObjectObjectCursor<String, MappingMetaData>> mappingIter = indexMappings.iterator();
        while ( mappingIter.hasNext() ) {
            ObjectObjectCursor<String, MappingMetaData> obj = mappingIter.next();
            if (typeName.equals(obj.key)) {
                MappingMetaData mmd = obj.value;
                try {
                    sourceAsMap = mmd.getSourceAsMap();
                    break;
                } catch (IOException ex) {
                    System.out.println("E error in getting mapping source as map: " + ex);
                }
            }
        }
        return sourceAsMap;
    }

    public boolean typeDelete(String indexName, String typeName) {
        boolean res = false;
        try {
            DeleteMappingRequest deleteMapping = new DeleteMappingRequest(indexName).types(typeName);
            DeleteMappingResponse actionGet = _esclient.admin().indices().deleteMapping(deleteMapping).actionGet();
            res = true;
        } catch (Exception ex) {
            System.out.println("E error in deleting mapping: " + indexName + ", " + typeName);
        }
        return res;
    }

    public boolean isSameMapping(String indexName, String typeName, JSONObject newMappingJson) {
        boolean sameMapping = true;

        Map<String, Object> oldMappingMap = getTypeMapping(indexName, typeName);
        if (oldMappingMap != null && newMappingJson != null) {
            // this index and type exists
            Map<String, Object> newMappingMap = changeJsonToMap(newMappingJson);
            // now check whether oldMappingMap and newMappingMap are same
            TreeSet<String> s1 = null; TreeSet<String> s2 = null;
            for ( String key : newMappingMap.keySet() )
                s1 = new TreeSet<String>(((Map) newMappingMap.get(key)).keySet());
            for ( String key : oldMappingMap.keySet() )
                s2 = new TreeSet<String>(((Map) oldMappingMap.get(key)).keySet());
            if (!s1.equals(s2)) {
                System.out.println("new: " + s1 + "\nold: " + s2);
                sameMapping = false;
            }
        }
        return sameMapping;
    }

    public void changeMapping(String indexName, String typeName, JSONObject newMappingJson) {
        Map<String, Object> newMappingMap = changeJsonToMap(newMappingJson);
        boolean sameMapping = isSameMapping(indexName, typeName, newMappingJson);
        if (!sameMapping) {
            System.out.println("different mapping");
            // not same mapping, change the mapping
            PutMappingRequestBuilder pmrBuilder = _esclient.admin().indices().preparePutMapping(indexName).setType(typeName);
            pmrBuilder.setSource(newMappingMap);
            pmrBuilder.setIgnoreConflicts(true);
            PutMappingResponse pmResponse = pmrBuilder.execute().actionGet();
            boolean result = pmResponse.isAcknowledged();
            System.out.println(result);
        } else {
            System.out.println("same mapping");
        }
    }

    public static Map<String,Object> changeJsonToMap(JSONObject json) {
        Map<String,Object> map = null;
        try {
            map = new ObjectMapper().readValue(String.valueOf(json), HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public XContentBuilder buildJsonMapping(JSONObject json) {

        ByteArrayInputStream bais;
        BytesStreamOutput bs;
        XContentBuilder builder = null;
        try {
            bais = new ByteArrayInputStream( json.toString().getBytes("UTF-8") );
            bs = new BytesStreamOutput();
            int i;
            while ( ( i = bais.read() ) != -1  ) {
                bs.write(i);
            }
            builder = new XContentBuilder(JsonXContent.jsonXContent, bs );

        } catch (UnsupportedEncodingException ex) {
            System.out.println("E error in xcontentbuilder: " + ex);
        } catch (IOException ex) {
            System.out.println("E error in xcontentbuilder: " + ex);
        }

        return builder;
    }


}
