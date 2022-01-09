package elp.max.e.taxistation.service;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface ServiceInterface<T> {

    List<T> findAll();
    T findById(Long id);
    T save(T dto) throws ValidationException;
    T update(Long id, T dto) throws ValidationException;
    void delete(Long id);
    void validateDto(T dto) throws ValidationException;
}
