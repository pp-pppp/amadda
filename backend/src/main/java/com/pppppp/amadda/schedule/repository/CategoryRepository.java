package com.pppppp.amadda.schedule.repository;

import com.pppppp.amadda.schedule.entity.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategorySeqAndIsDeletedFalse(Long categorySeq);

    List<Category> findByUser_UserSeqAndIsDeletedFalse(Long userSeq);

    boolean existsByUser_UserSeqAndCategorySeqAndIsDeletedFalse(Long userSeq, Long categorySeq);

    boolean existsByUser_UserSeqAndCategoryNameAndIsDeletedFalse(Long userSeq, String categoryName);
}
