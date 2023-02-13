package ru.practicum.ewmserv.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmserv.comment.dto.ResponseCommentDto;
import ru.practicum.ewmserv.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<ResponseCommentDto> findAllByEventId(Long eventId, Pageable pageable);

}