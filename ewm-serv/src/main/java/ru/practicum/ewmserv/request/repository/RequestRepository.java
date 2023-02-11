package ru.practicum.ewmserv.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmserv.enums.RequestStatus;
import ru.practicum.ewmserv.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Override
    Optional<Request> findById(Long requestId);

    List<Request> findAllByRequester_Id(Long requesterId);

    Integer countRequestByStatusEqualsAndEvent_Id(RequestStatus requestStatus, Long eventId);

    List<Request> findAllByEvent_Id(Long eventId);

    boolean existsByRequester_IdAndEvent_Id(Long userId, Long eventId);

    Optional<Request> findByIdAndEvent_Id(Long requestId, Long eventId);
}