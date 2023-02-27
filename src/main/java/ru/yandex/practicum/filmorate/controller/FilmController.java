package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController

@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService service;

    @Autowired
    public FilmController (FilmService service) {
        this.service = service;
    }

    @GetMapping
    public List<Film> returnAllFilm() {
        log.trace("Получен запрос к эндпоинту: 'GET /films'");
        return service.getAllFilm();
    }

    @GetMapping("/{id}")
    public Film findFilmById (@PathVariable Integer id) {
        log.trace("Получен запрос к эндпоинту: 'GET /films/id'");
        return service.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike (@PathVariable Integer id, @PathVariable Integer userId) {
        log.trace("Получен запрос к эндпоинту: 'PUT /films/id/like/userId'");
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike (@PathVariable Integer id, @PathVariable Integer userId) {
        log.trace("Получен запрос к эндпоинту: 'DELETE /films/id/like/userId'");
        service.removeLike(id, userId);
    }
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.trace("Получен запрос к эндпоинту: 'POST /films'");
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.trace("Получен запрос к эндпоинту: 'PUT /films'");
        return service.updateFilm(film);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm (@PathVariable Integer id) {
        log.trace("Получен запрос к эндпоинту: 'DELETE /films/id'");
        service.deleteFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> viewPopularFilm (@RequestParam(defaultValue = "10") Integer count) {
        log.trace("Получен запрос к эндпоинту: 'GET /films/popular'");
        return service.getTop10Film(count);
    }
}
