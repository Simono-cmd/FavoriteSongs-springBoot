package com.example.favoritesongs.controller;
import com.example.favoritesongs.model.Song;
import com.example.favoritesongs.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data")
public class SongRestController {

    @Autowired
    private SongService songService;

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs(
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.ASC;
        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException e) {
        }

        Sort sort = Sort.by(sortDirection, sortBy);
        List<Song> songs = songService.getAllSongs(sort);
        return ResponseEntity.ok(songs);
    }

    @PostMapping
    public ResponseEntity<Song> createSong(@RequestBody Song song) {
        Song savedSong = songService.saveSong(song);
        return ResponseEntity.ok(savedSong);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @RequestBody Song song) {
        return songService.getSongById(id)
                .map(existing -> {existing.setTitle(song.getTitle());
                    existing.setArtist(song.getArtist());
                    existing.setAlbum(song.getAlbum());
                    existing.setReleaseYear(song.getReleaseYear());
                    songService.saveSong(existing);
                    return ResponseEntity.ok(existing);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return ResponseEntity.ok().build();
    }


}
