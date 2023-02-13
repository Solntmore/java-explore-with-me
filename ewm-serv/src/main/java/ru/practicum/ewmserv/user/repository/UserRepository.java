package ru.practicum.ewmserv.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmserv.user.model.User;

import java.util.Collection;


public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    boolean existsById(Long userId);

    @Override
    void deleteById(Long userId);

    @Query("SELECT u FROM User u WHERE u.id in (:ids)")
    Page<User> getUsers(Collection<Long> ids, Pageable pageable);

}
