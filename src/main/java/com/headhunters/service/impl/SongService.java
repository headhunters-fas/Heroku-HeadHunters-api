package com.headhunters.service.impl;

import com.headhunters.model.Song;
import com.headhunters.repository.SongRepository;
import com.headhunters.service.Interfaces.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongService implements ISongService {

    @Autowired
    private SongRepository songRepository;

    @Override
    public Song save(Song song) {
        return songRepository.save(song);
    }

    @Override
    public Iterable<Song> findAll() {
        return songRepository.findAll();
    }

    @Override
    public Song findById(Long id) {
        return songRepository.getById(id);
    }

    @Override
    public void delete(Long id, String owner) {
        Song song = findById(id);
        songRepository.delete(song);
    }
}
