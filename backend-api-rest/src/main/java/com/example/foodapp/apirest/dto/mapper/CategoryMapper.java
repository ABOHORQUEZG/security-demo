package com.example.foodapp.apirest.dto.mapper;

import com.example.foodapp.apirest.dto.response.CategoryResponse;
import com.example.foodapp.apirest.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);
}
