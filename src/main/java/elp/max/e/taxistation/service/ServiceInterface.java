package elp.max.e.taxistation.service;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface ServiceInterface<T> {

    List<T> findAll();
    T save(T dto) throws ValidationException;
    void delete(Long id);
}
