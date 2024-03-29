package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.implement.MpaStorageDB;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {
    private final MpaStorageDB mpaStorageDB;

    public Mpa getMpaById(Long id) {
        log.trace("Попытка получить мировой возрастной рейнтинг id '{}'", id);
        return mpaStorageDB.getMpaById(id);
    }

    public List<Mpa> getAllMpa() {
        log.trace("Попытка получить список всех мировых возрастных рейнтингов");
        return mpaStorageDB.getAllMpa();
    }
}
