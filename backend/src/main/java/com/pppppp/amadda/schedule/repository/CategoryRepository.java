package com.pppppp.amadda.schedule.repository;

import com.pppppp.amadda.schedule.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUser_UserSeq(Long userSeq);

    boolean existsByUser_UserSeqAndCategoryName(Long userSeq, String categoryName);
}
