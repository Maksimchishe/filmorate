package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public Set<DirectorDto> getDirectors() {
        return directorService.getDirectors();
    }

    @GetMapping("/{id}")
    public DirectorDto getDirectorById(@PathVariable long id) {
        return directorService.getDirectorById(id);
    }

    @PostMapping
    public DirectorDto createDirector(@Valid @RequestBody DirectorDto directorDto) {
        return directorService.createDirector(directorDto);
    }

    @PutMapping
    public DirectorDto updateDirector(@Valid @RequestBody DirectorDto directorDto) {
        return directorService.updateDirector(directorDto);
    }

    @DeleteMapping("/{id}")
    public void deleteDirectorById(@PathVariable long id) {
        directorService.deleteDirectorById(id);
    }
}