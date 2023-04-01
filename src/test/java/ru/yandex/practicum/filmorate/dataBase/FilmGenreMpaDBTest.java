package ru.yandex.practicum.filmorate.dataBase;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreMpaDBTest {
    private final FilmDao filmStorage;
    private final MpaDao mpaStorage;
    private final GenreDao genreStorage;

    @Test
    void testGetAllMpa_ExpectedSize5() {
        List<Mpa> mpaRatingList = mpaStorage.getAllMpa();
        assertThat(mpaRatingList.size()).isEqualTo(5);
    }

    @Test
    public void testGetMpaById_ExpectedMpaNameG() {
        Mpa mpaExpected = new Mpa(1L, "G");
        Mpa mpaRating = mpaStorage.getMpaById(1L);
        assertThat(mpaRating).isEqualTo(mpaExpected);
    }

    @Test
    void testGetAllGenres_ExpectedSize6() {
        List<Genre> genres = genreStorage.getGenres();
        assertThat(genres.size()).isEqualTo(6);
    }

    @Test
    public void testGetGenreById_ExpectedGenreEquals() {
        Genre genreExpected = new Genre(1L, "Комедия");
        Genre genre = genreStorage.getGenreById(1L);
        System.out.println(genre);
        assertThat(genre).isEqualTo(genreExpected);
    }

    @Test
    void checkWriteFormDb() {
        filmStorage.addFilm(Film.builder()
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100L)
                .mpa(new Mpa(3L, "PG-13"))
                .build());
        filmStorage.addFilm(Film.builder()
                .name("test1")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100L)
                .mpa(new Mpa(3L, "PG-13"))
                .build());
        filmStorage.addFilm(Film.builder()
                .name("test2")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100L)
                .mpa(new Mpa(3L, "PG-13"))
                .build());

        List<Film> films = filmStorage.getAllFilm();
        assertThat(films.get(0).getName()).isEqualTo("TestCheck");
        assertThat(films.get(1).getDescription()).isEqualTo("test");
        assertThat(films.get(2).getId()).isEqualTo(3L);
    }

    @Test
    void updateFilm_expectedCorrectUpdate() {
        filmStorage.addFilm(Film.builder()
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100L)
                .mpa(new Mpa(3L, "PG-13"))
                .build());
        Film film = filmStorage.getFilmById(1L);
        film.setName("TestCheck");
        filmStorage.updateFilm(film);
        assertThat(filmStorage.getFilmById(1L).getName()).isEqualTo("TestCheck");
    }
}