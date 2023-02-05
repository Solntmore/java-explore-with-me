package ru.practicum.ewmserv.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmserv.enums.RequestStatus;
import ru.practicum.ewmserv.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Override
    Optional<Request> findById(Long requestId);

    List<Request> findAllByRequester_Id(Long requesterId);

    @Query("SELECT COUNT(r.status) as c FROM Request r WHERE r.status = ?1")
    Integer countRequestByStatus(RequestStatus requestStatus);

    Integer countRequestByStatusEqualsAndEvent_Id(RequestStatus requestStatus, Long eventId);

    List<Request> findAllByRequester_IdAndEvent_IdAndStatusIsNot(Long userId, Long eventId, RequestStatus requestStatus);

    boolean existsByRequester_IdAndEvent_Id(Long userId, Long eventId);

    Optional<Request> findByIdAndEvent_Id(Long requestId, Long eventId);
}