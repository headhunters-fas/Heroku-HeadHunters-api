package com.headhunters.service.Interfaces;

import org.springframework.stereotype.Service;

@Service
public interface IService<T> {

    T save(T obj);

    Iterable<T> findAll();

    T findById(Long id);

    void delete(Long id, String owner);
}

