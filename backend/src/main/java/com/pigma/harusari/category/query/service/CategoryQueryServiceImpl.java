package com.pigma.harusari.category.query.service;

import com.pigma.harusari.category.query.dto.response.CategoryResponse;
import com.pigma.harusari.category.query.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> findCategoriesByMember(Long memberId) {
        return categoryMapper.findByMemberId(memberId);
    }
}
