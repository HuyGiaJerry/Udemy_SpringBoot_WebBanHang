package com.project.shopapp.helpers.services;

import java.util.List;

public interface IBaseService<T,DTO,ID> {
    T create(DTO dto);
    T update(ID id, DTO dto);
    T getById(ID id);
    void delete(ID id);
    List<T> getAll();

}
