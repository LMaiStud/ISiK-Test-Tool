package com.isik.testsuit.repository;

import com.isik.testsuit.entity.DocumentTransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentTransferHistoryRepository extends JpaRepository<DocumentTransferHistory, Long> {

    Optional<DocumentTransferHistory> findById(Long id);

    List<DocumentTransferHistory> findAll();

    void deleteById(Long id);

}
