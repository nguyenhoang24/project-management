package utc.edu.thesis.service;

import java.util.Optional;

public interface IService<T> {
    T save(T t);

    Iterable<T> findAll();

    Optional<T> findById(Long id);

    Boolean delete(Long id);

}
