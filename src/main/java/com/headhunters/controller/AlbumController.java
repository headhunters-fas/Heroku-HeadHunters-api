package com.headhunters.controller;

import com.headhunters.model.Album;
import com.headhunters.service.Interfaces.IAlbumService;
import com.headhunters.service.impl.AlbumService;
import com.headhunters.service.impl.MapValidationErrorService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.ServletContextResource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AlbumController  {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    //Particular User

    @PostMapping("/users/albums")
    public ResponseEntity<?> addAlbum(@Valid @RequestBody Album album, BindingResult result, Principal principal) {

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap!=null) return errorMap;

        Album newAlbum = albumService.saveAlbumWithOwner(album, principal.getName());

        return new ResponseEntity<Album>(newAlbum, HttpStatus.CREATED);
    }

    @GetMapping("/users/albums/{albumId}")
    public ResponseEntity<?> getUserAlbumById(@PathVariable Long albumId, Principal principal){

        Album album = albumService.findAlbumById(albumId, principal.getName());

        return new ResponseEntity<Album>(album, HttpStatus.OK);
    }

    @GetMapping("/users/albums/all")
    public Iterable<Album> getAllUserAlbums(Principal principal){return albumService.findAllAlbums(principal.getName());}

    @DeleteMapping("/users/albums/{albumId}")
    public ResponseEntity<?> deleteUserAlbumById(@PathVariable Long albumId, Principal principal){
        albumService.delete(albumId, principal.getName());

        return new ResponseEntity<String>("album with ID: '"+albumId+"' was deleted", HttpStatus.OK);
    }

    // Public

    @GetMapping("/albums")
    public Iterable<Album> getAlbumsByGenre(@RequestParam String genre) {
        return albumService.getByGenre(genre);
    }

    @PostMapping("/albums")
    public ResponseEntity<?> addAlbum(@Valid @RequestBody Album album, BindingResult result) {

        if(result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for(FieldError error: result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
        }

        Album newAlbum = albumService.save(album);

        return new ResponseEntity<Album>(newAlbum, HttpStatus.CREATED);
    }

    @PatchMapping("/albums/{albumId}")
    public ResponseEntity<String> updateAlbum(@RequestBody Map<String, Integer> field, @PathVariable Long albumId){

        Album oldAlbum = albumService.findById(albumId);
        field.forEach((k, v) -> {
            oldAlbum.setLikes(v);
        });

        albumService.save(oldAlbum);

        return new ResponseEntity<String>("Album updated", HttpStatus.OK);
    }
}
