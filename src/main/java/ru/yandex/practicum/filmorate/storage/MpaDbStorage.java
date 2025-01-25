package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Optional;
import java.util.Set;

public interface MpaDbStorage {

    Set<Mpa> getMpas();

    Optional<Mpa> getMpaById(long id);
}
