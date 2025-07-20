package com.project.shopapp.helpers.services;

import com.project.shopapp.exceptions.DataNotFoundException;

import java.util.List;

public interface IBaseService<T,DTO,ID> {
    T create(DTO dto) throws DataNotFoundException;
    T update(ID id, DTO dto) throws DataNotFoundException;
    T getById(ID id);
    void delete(ID id) throws DataNotFoundException;
    List<T> getAll();

}
