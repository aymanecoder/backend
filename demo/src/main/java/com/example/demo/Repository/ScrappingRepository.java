package com.example.demo.Repository;

import com.example.demo.Entities.Scrapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrappingRepository extends JpaRepository<Scrapping,Long> {

//   Optional<List<Scrapping>> findBySite_Name(String Url);

}
