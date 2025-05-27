package com.example.favoritesongs.controller;
import com.example.favoritesongs.model.Song;
import com.example.favoritesongs.service.SongService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/songs")
public class SongMvcController {

    private final SongService songService;

    public SongMvcController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public String listSongs(
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException e) {
            sortDirection = Sort.Direction.ASC;
        }

        Sort sort = Sort.by(sortDirection, sortBy);
        List<Song> songs = songService.getAllSongs(sort);
        model.addAttribute("songs", songs);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("songForm", new Song());
        return "songs/list";
    }

    @PostMapping("/add")
    public String addSong(@ModelAttribute("songForm") Song song) {
        songService.saveSong(song);
        return "redirect:/songs";
    }

    // get dla formularza żeby było łatwiej edytować
    @GetMapping("/edit/{id}")
    public String editSong(@PathVariable Long id, Model model) {
        Song song = songService.getSongById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono piosenki o id: " + id));
        model.addAttribute("songForm", song);
        model.addAttribute("songs", songService.getAllSongs(Sort.by(Sort.Direction.ASC, "title")));
        model.addAttribute("sortBy", "title");
        model.addAttribute("direction", "asc");
        return "songs/list";
    }

    @PostMapping("/edit/{id}")
    public String updateSong(@PathVariable Long id, @ModelAttribute("songForm") Song song) {
        song.setId(id);
        songService.saveSong(song);
        return "redirect:/songs";
    }

    @PostMapping("/delete/{id}")
    public String deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return "redirect:/songs";
    }
}
