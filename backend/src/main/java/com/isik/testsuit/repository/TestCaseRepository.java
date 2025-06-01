package com.isik.testsuit.repository;

import com.isik.testsuit.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    Optional<TestCase> findById(Long id);


    Optional<TestCase> findByTestName(String name);

    List<TestCase> findAll();

    void deleteById(Long id);


    /**
     * löscht ein dokument mit der id aus allen Testcases
     *
     * @param docID
     */
    @Modifying
    @Query(value = "DECLARE @docID INT = ?1; " +
            "UPDATE test_case " +
            "SET test_case_docs_json = CASE " +
            "    WHEN NOT EXISTS ( " +
            "        SELECT 1 " +
            "        FROM OPENJSON(test_case.test_case_docs_json) j " +
            "        WHERE JSON_VALUE(j.value, '$.docID') != @docID " +
            "    ) THEN '[]' " +
            "    ELSE ( " +
            "        SELECT '[' + STRING_AGG(CASE " +
            "            WHEN JSON_VALUE(j.value, '$.docID') = @docID THEN NULL " +
            "            ELSE j.value " +
            "        END, ',') + ']' " +
            "        FROM OPENJSON(test_case.test_case_docs_json) j " +
            "        WHERE JSON_VALUE(j.value, '$.docID') != @docID " +
            "    ) " +
            "END " +
            "WHERE EXISTS ( " +
            "    SELECT 1 " +
            "    FROM OPENJSON(test_case.test_case_docs_json) j " +
            "    WHERE JSON_VALUE(j.value, '$.docID') = @docID " +
            ")",
            nativeQuery = true)
    void deleteDocFromTestCaseJson(int docID);

}
