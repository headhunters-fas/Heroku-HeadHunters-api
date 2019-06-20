package com.headhunters.repository;

import com.headhunters.model.Album;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Long> {

    Album getById(Long id);

    Iterable<Album> findAllByUsername(String username);

    Iterable<Album>listarDescendente();

    Iterable<Album>listarPorGeneroDescendente(String genre);
}