package ru.practicum.ewmserv.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmserv.enums.RequestStatus;
import ru.practicum.ewmserv.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Override
    Optional<Request> findById(Long requestId);

    List<Request> findAllByRequesterId(Long requesterId);

    Integer countRequestByStatusEqualsAndEventId(RequestStatus requestStatus, Long eventId);

    List<Request> findAllByEventId(Long eventId);

    boolean existsByRequester_IdAndEventId(Long userId, Long eventId);

    Optional<Request> findByIdAndEventId(Long requestId, Long eventId);
}