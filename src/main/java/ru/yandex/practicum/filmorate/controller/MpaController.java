package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable Long id) {
        log.trace("Получен запрос к эндпоинту: 'GET /mpa/id'");
        return mpaService.getMpaById(id);
    }

    @GetMapping()
    public List<Mpa> getAllMpa() {
        log.trace("Получен запрос к эндпоинту: 'GET /mpa'");
        return mpaService.getAllMpa();
    }
}
