package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.Director;

public class DirectorMapper {

    public static DirectorDto toDto(Director director) {
        DirectorDto directorDto = new DirectorDto();
        directorDto.setId(director.getId());
        directorDto.setName(director.getName());
        return directorDto;
    }

    public static Director toModel(DirectorDto directorDto) {
        Director director = new Director();
        director.setId(directorDto.getId());
        director.setName(directorDto.getName());
        return director;
    }
}
