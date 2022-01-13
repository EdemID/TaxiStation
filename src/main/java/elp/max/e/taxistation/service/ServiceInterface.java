package elp.max.e.taxistation.service;

import elp.max.e.taxistation.exception.ValidationDtoException;

import java.util.List;

public interface ServiceInterface<T> {

    List<T> findAll();
    T findById(Long id);
    T save(T dto) throws ValidationDtoException;
    T update(Long id, T dto) throws ValidationDtoException;
    void delete(Long id);
    void validateDto(T dto) throws ValidationDtoException;
}
