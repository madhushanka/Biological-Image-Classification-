package com.bolzano.Task03;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The REST Controller class , applied to a class to mark it as a request handler.
 * @author Tharindu Madhusankha - 3198602
 */

@RestController
public class ProductController {

    // Used for automatic dependency injection.
    @Autowired
    private ProductService service;

    /**
     * Maps HTTP GET requests onto specific handler methods.
     * @return A list of entries from the database.
     */

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Model_Table> list(){
        return service.listAll();
    }

    /**
     * Implementation of a HTTP Get request to access a single row of the database
     * @param path A name of the image to be queried.
     * @return An entry as a response to the web-page.
     */

    // PathVariable : indicates that a method parameter should be bound to a URI template variable
    // HTTP status would be either OK(200) or Not found(404) according to the response
    @GetMapping(value = "/get/{path}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model_Table> get(@PathVariable String path){
        try {
            Model_Table model_table = service.get(path);
            return new ResponseEntity<Model_Table>(model_table, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<Model_Table>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Maps HTTP POST requests onto specific handler methods
     * @param model_table An entry to be added to the database
     */

    @PostMapping("/post")
    public void add(@RequestBody Model_Table model_table){
        service.save(model_table);
    }



}
