package com.mlamp;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


public class RestUtils<T> {
    private RestTemplate restTemplate;

    public RestUtils(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000*1000);
        requestFactory.setReadTimeout(36000*1000);
        this.restTemplate = new RestTemplate(requestFactory);
    }

    public T request(String url, Map<String, Object> params, HttpMethod httpMethod,Class respClazz){
        try{
            HttpHeaders headers = new HttpHeaders();
            HttpEntity entity = null;
            ResponseEntity<T> responseEntity = null;
            if(httpMethod.matches("POST")){
                entity = new HttpEntity<>(params, headers);
                responseEntity = restTemplate.postForEntity(url, entity, respClazz);
            }else{
                responseEntity = restTemplate.getForEntity(url,respClazz, params);
            }
            return responseEntity.getBody();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
