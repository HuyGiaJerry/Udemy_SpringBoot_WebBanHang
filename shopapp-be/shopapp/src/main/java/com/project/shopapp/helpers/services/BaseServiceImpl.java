package com.project.shopapp.helpers.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseServiceImpl <T,DTO,ID> implements IBaseService<T,DTO,ID> {

    protected final JpaRepository<T, ID> repository;

    @Override
    public T getById(ID id) {
        return repository.findById(id).orElseThrow(()-> new RuntimeException("Not found"));
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(ID id) {
        // Xóa cứng
        repository.deleteById(id);
    }


    // Create và Update để Service con tự Override
}
