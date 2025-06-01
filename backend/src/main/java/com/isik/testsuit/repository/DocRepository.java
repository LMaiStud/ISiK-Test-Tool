package com.isik.testsuit.repository;

import com.isik.testsuit.entity.Doc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocRepository extends JpaRepository<Doc, Long> {

    Optional<Doc> findBypadId(String padId);

    Optional<Doc> findById(Long id);

    List<Doc> findAll();

    void deleteById(Long id);

}
