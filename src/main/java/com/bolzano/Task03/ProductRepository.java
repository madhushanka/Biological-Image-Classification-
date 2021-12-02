package com.bolzano.Task03;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface which extends JPA repository.
 * @author Tharindu Madhusankha - 3198602
 */

public interface ProductRepository extends JpaRepository<Model_Table, Integer> {
    Model_Table findByPath(String path);
    Model_Table deleteByPath(String path);
}
