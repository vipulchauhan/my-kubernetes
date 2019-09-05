package io.vnc.userapi;

import lombok.extern.java.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;

@SpringBootApplication
@RestController
@Log
public class UserapiApplication {

    private static final String serverURL = "http://localhost:9090";

    public static void main(String[] args) {
        SpringApplication.run(UserapiApplication.class, args);
    }

    public static String requestProcessedData(String url) {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        log.log(Level.INFO, result);
        return result;
    }


    @GetMapping("/")
    public static String hello() {
        return "hello i'm a reader data";
    }

    @GetMapping("/codeToState")
    public static String codeToState(@RequestParam("code") String code) {
        String state = null;
        try {
            String response = requestProcessedData(serverURL + "/readerDataForCode");
            JSONObject jsObject = new JSONObject(response);
            state = jsObject.getString(code.toUpperCase());
        } catch (Exception e) {
            log.log(Level.WARNING, "json parsing error \n" + e);
        }

        if (state == null) {
            state = "No match found";
        }
        return state;
    }

    @GetMapping("/stateToCode")
    public static String stateToCode(@RequestParam("state") String state) {
        String value = null;
        try {
            String response = requestProcessedData(serverURL + "/readerDataForState");
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String name = obj.getString("name");
                if (state.equalsIgnoreCase(name)) {
                    value = obj.getString("abbreviation");
                    break;
                }
            }


        } catch (Exception e) {
            log.log(Level.WARNING, "json parsing error \n" + e);
        }

        if (value == null) {
            value = "No match found";
        }
        return value;
    }

}
