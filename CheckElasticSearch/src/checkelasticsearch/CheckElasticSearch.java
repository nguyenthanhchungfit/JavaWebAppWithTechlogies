/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkelasticsearch;



/**
 *
 * @author cpu11165-local
 */
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;



public class CheckElasticSearch {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        RestClient client = RestClient.builder(
        new HttpHost("localhost", 9200, "http")).build();

        
        
        String jsonString = "{" +
            "\"user\":\"kimchy\"," +
            "\"postDate\":\"2013-01-31\"," +
            "\"message\":\"trying out Elasticsearch\"" +
        "}";
        
        HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
        Response response = client.performRequest("PUT", "/posts/doc/1", Collections.emptyMap(), entity);
        
        System.out.println(EntityUtils.toString(response.getEntity()));
        client.close();

    }

}
