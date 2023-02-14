package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final static LocalDate DAY_FILM = LocalDate.of(1895, 12, 28);
    private int currencyIdFilm = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> returnAllFilm() {
        log.info("Получен запрос к эндпоинту: 'GET /users', Вернули количество фильмов: '{}'",
                films.size());
        return List.copyOf(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        checkFilm(film);
        film.setId(currencyIdFilm++);
        films.put(film.getId(), film);
        log.info("Получен запрос к эндпоинту: 'POST /films', фильм с name '{}' успешно записан, присвоен id '{}'",
                film.getName(), film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Не удалось найти фильм с id '{}'", film.getId());
            throw new NotFoundException("Фильма с таким id не существует");
        }
        checkFilm(film);
        films.put(film.getId(), film);
        log.info("Получен запрос к эндпоинту: 'PUT /films', фильм id name '{} {}' успешно Обновлен",
                film.getId(), film.getName());
        return film;
    }

    private void checkFilm(Film film) {
        if (film.getReleaseDate().isBefore(DAY_FILM)) {
            log.warn("Неккоректно указана дате релиза '{}'", film.getReleaseDate());
            throw new ValidationException("Некоректная дата релиза, максимальная дата релиза 28.12.1895");
        }
    }
}
