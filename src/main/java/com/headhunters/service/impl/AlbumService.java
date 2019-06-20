package com.headhunters.service.impl;

import com.headhunters.model.Album;
import com.headhunters.model.Song;
import com.headhunters.model.User;
import com.headhunters.repository.AlbumRepository;
import com.headhunters.repository.SongRepository;
import com.headhunters.repository.UserRepository;
import com.headhunters.service.Interfaces.IAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService implements IAlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    //Public

    @Override
    public Album addSong(Long song_id, Long album_id) {
        Album album = albumRepository.getById(album_id);
        Song song = songRepository.getById(song_id);
        List<Song> songList = album.getSongList();
        songList.add(song);
        album.setSongList(songList);
        song.setAlbum(album);
        songRepository.save(song);
        return albumRepository.save(album);
    }


    @Override
    public Album save(Album album) {
        albumRepository.save(album);
        for(int i = 0; i < album.getSongList().size(); i++) {
            Song song = songRepository.getById(album.getSongList().get(i).getId());
            song.setAlbum(album);
            songRepository.save(song);
        }
        return albumRepository.save(album);
    }

    @Override
    public Iterable<Album> findAll() {
        return albumRepository.findAllByUsername(null);
    }

    @Override
    public Iterable<Album> getByGenre(String genre) {
        if (genre != "") {
            return albumRepository.listarPorGeneroDescendente(genre);
        }
        return albumRepository.listarDescendente();
    }

    @Override
    public Album findById(Long id) {
        return albumRepository.getById(id);
    }

    //For a particular user

    @Override
    public void delete(Long albumId, String username) {
        albumRepository.delete(findAlbumById(albumId, username));
    }

    public Album findAlbumById(Long albumId, String username){

        //Only want to return the task if the user looking for it is the owner

        Album album = albumRepository.getById(albumId);

        if(album == null){
            throw new RuntimeException("Album ID '"+albumId+"' does not exist");
        }

        if (!album.getUsername().equals(username)) {
            throw new RuntimeException("Album not found in your account");
        }

        return album;
    }

    public Iterable<Album> findAllAlbums(String username){
        return albumRepository.findAllByUsername(username);
    }

    @Override
    public Album saveAlbumWithOwner(Album album, String username) {
        try{
            User user = userRepository.findByUsername(username);
            album.setUsername(user.getUsername());
            album.setUser(user);
            albumRepository.save(album);
            for(int i = 0; i < album.getSongList().size(); i++) {
                Song song = songRepository.getById(album.getSongList().get(i).getId());
                song.setAlbum(album);
                songRepository.save(song);
            }
            return album;

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
