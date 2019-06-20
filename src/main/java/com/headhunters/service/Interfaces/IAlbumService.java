package com.headhunters.service.Interfaces;

import com.headhunters.model.Album;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAlbumService extends IService<Album> {

    Album addSong(Long song_id, Long album_id);

    Album saveAlbumWithOwner(Album album, String owner);

    Iterable<Album> getByGenre(String genre);
}
