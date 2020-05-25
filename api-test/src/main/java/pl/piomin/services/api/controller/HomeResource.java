package pl.piomin.services.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.piomin.services.api.PropertiesConfig;

@RestController
@RequestMapping("/configmap")
public class HomeResource {

    @Autowired
    PropertiesConfig config;

    @GetMapping("/data")
    public ResponseEntity<ResponseData> getData() {
        ResponseData responseData = new ResponseData();
        responseData.setName(config.getName());
        responseData.setValue(config.getTest());
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public class ResponseData {
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}