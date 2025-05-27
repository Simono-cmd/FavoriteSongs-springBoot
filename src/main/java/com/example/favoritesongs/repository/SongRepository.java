package com.example.favoritesongs.repository;

import com.example.favoritesongs.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findAll(Sort sort);
}
