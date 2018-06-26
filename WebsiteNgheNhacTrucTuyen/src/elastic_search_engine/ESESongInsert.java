/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elastic_search_engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

/**
 *
 * @author cpu11165-local
 */
public class ESESongInsert {
    public void InsertNewSong(String id, String name, ArrayList<String> singers) throws IOException{
        
        RestClient client = RestClient.builder(
        new HttpHost(ESEContracts.HOST, ESEContracts.PORT, ESEContracts.PROTOCOL)).build();
        String singerFormat = "";
        if(singers.size() > 0){
            singerFormat = "\"" + singers.get(0) + "\"";
            int size = singers.size();
            for(int i = 1; i < size; i++){
                singerFormat += "," + "\"" + singers.get(i) + "\"";
            }
        }
        
        String jsonString = "{" 
                + "\"name\" : \""+ name + "\"," 
                + "\"singers\": [" + singerFormat + "]}";
        
        HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
        String requestString = "/" + ESEContracts.INDEX_NAME + "/" + ESEContracts.TYPE_SONG_NAME + "/" + id;
        
        Response response = client.performRequest("PUT", requestString, Collections.emptyMap(), entity);
    }
}
