package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.dao.DirectorDbStorage;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.DirectorMapper;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorDbStorage directorStorage;

    public Set<DirectorDto> getDirectors() {
        return directorStorage.getDirectors().stream()
                .map(DirectorMapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public DirectorDto getDirectorById(long id) {
        return DirectorMapper.toDto(directorStorage.getDirectorById(id)
                .orElseThrow(() -> new NotFoundException("Режиссер не найден.")));
    }

    public DirectorDto createDirector(DirectorDto directorDto) {
        if (directorDto.getName().isBlank()) {
            throw new ValidationException("Поле name не может быть пустым.");
        }
        return DirectorMapper.toDto(directorStorage.createDirector(DirectorMapper.toModel(directorDto)));
    }

    public DirectorDto updateDirector(DirectorDto directorDto) {
        directorStorage.getDirectorById(directorDto.getId())
                .orElseThrow(() -> new NotFoundException("Режиссер не найден."));
        if (directorDto.getName().isBlank()) {
            throw new ValidationException("Поле name не может быть пустым.");
        }
        return DirectorMapper.toDto(directorStorage.updateDirector(DirectorMapper.toModel(directorDto)));
    }

    public void deleteDirectorById(long id) {
        directorStorage.getDirectorById(id)
                .orElseThrow(() -> new ValidationException("Режиссер не найден."));
        directorStorage.deleteDirectorById(id);
    }
}

