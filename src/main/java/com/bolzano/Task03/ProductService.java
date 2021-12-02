package com.bolzano.Task03;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * The service class marks a java class as a bean so the component-scanning mechanism of spring
 * can pick it up and pull it into the application context.
 * @author Tharindu Madhusankha - 3198602
 */

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    /**
     * @return will return all the elements found by the repository.
     */

    public List<Model_Table> listAll(){
        return repo.findAll();
    }

    /**
     * @param model_table Database table.
     * Save the entries to the database table.
     */

    public void save(Model_Table model_table){
        repo.save(model_table);
    }

    /**
     * @param path column name od the database table which is used to request GET.
     * @return will return particular record from the database relevant to the path name.
     */

    public Model_Table get(String path){
        return repo.findByPath(path);
    }

    /**
     * @param path column name od the database table which is used to request GET.
     * Deletes a particular table entry.
     */

    public void delete(String path){
        repo.deleteByPath(path);
    }
}
