package me.ooify.boogai.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;

import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.GetLoadStateReq;
import io.milvus.v2.service.partition.request.CreatePartitionReq;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.response.InsertResp;

public class VectorSearch {
    public void test() {
        String CLUSTER_ENDPOINT = "http://localhost:19530";
        ConnectConfig connectConfig = ConnectConfig.builder().uri(CLUSTER_ENDPOINT).build();
        MilvusClientV2 client = new MilvusClientV2(connectConfig);
        CreateCollectionReq quickSetupReq = CreateCollectionReq.builder().collectionName("quick_setup").dimension(5).metricType("IP").build();
        client.createCollection(quickSetupReq);
        GetLoadStateReq loadStateReq = GetLoadStateReq.builder().collectionName("quick_setup").build();
        boolean state = client.getLoadState(loadStateReq);
        System.out.println(state);
        // Output:
        // true
        // 3. Insert randomly generated vectors into the collection
        List<String> colors = Arrays.asList("green", "blue", "yellow", "red", "black", "white", "purple", "pink", "orange", "brown", "grey");
        List<JSONObject> data = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Random rand = new Random();
            String current_color = colors.get(rand.nextInt(colors.size() - 1));
            JSONObject row = new JSONObject();
            row.put("id", Long.valueOf(i));
            row.put("vector", Arrays.asList(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
            row.put("color_tag", current_color + "_" + String.valueOf(rand.nextInt(8999) + 1000));
            data.add(row);
        }
        InsertReq insertReq = InsertReq.builder().collectionName("quick_setup").data(data).build();
        InsertResp insertResp = client.insert(insertReq);
        System.out.println(JSONObject.toJSON(insertResp));
        // Output:
        // {"insertCnt": 1000}
        // 6.1. Create a partition
        CreatePartitionReq partitionReq = CreatePartitionReq.builder().collectionName("quick_setup").partitionName("red").build();
        client.createPartition(partitionReq);
        partitionReq = CreatePartitionReq.builder()
                .collectionName("quick_setup")
                .partitionName("blue")
                .build();
        client.createPartition(partitionReq);
        // 6.2 Insert data into the partition
        data = new ArrayList<>();
        for (int i = 1000; i < 1500; i++) {
            Random rand = new Random();
            String current_color = "red";
            JSONObject row = new JSONObject();
            row.put("id", Long.valueOf(i));
            row.put("vector", Arrays.asList(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
            row.put("color", current_color);
            row.put("color_tag", current_color + "_" + String.valueOf(rand.nextInt(8999) + 1000));
            data.add(row);
        }
        insertReq = InsertReq.builder()
                .collectionName("quick_setup")
                .data(data)
                .partitionName("red")
                .build();
        insertResp = client.insert(insertReq);
        System.out.println(JSONObject.toJSON(insertResp));
        // Output:
        // {"insertCnt": 500}
        data = new ArrayList<>();
        for (int i = 1500; i < 2000; i++) {
            Random rand = new Random();
            String current_color = "blue";
            JSONObject row = new JSONObject();
            row.put("id", Long.valueOf(i));
            row.put("vector", Arrays.asList(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
            row.put("color", current_color);
            row.put("color_tag", current_color + "_" + String.valueOf(rand.nextInt(8999) + 1000));
            data.add(row);
        }
        insertReq = InsertReq.builder()
                .collectionName("quick_setup")
                .data(data)
                .partitionName("blue")
                .build();
        insertResp = client.insert(insertReq);
        System.out.println(JSONObject.toJSON(insertResp));
    }


}
