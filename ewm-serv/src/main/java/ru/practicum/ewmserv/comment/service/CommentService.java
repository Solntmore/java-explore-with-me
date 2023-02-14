package ru.practicum.ewmserv.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewmserv.comment.dto.CreateCommentDto;
import ru.practicum.ewmserv.comment.dto.PatchCommentDto;
import ru.practicum.ewmserv.comment.dto.ResponseCommentDto;
import ru.practicum.ewmserv.comment.exceptions.CommentNotFoundException;
import ru.practicum.ewmserv.comment.exceptions.NotAllowToCreateCommentException;
import ru.practicum.ewmserv.comment.exceptions.NotAllowToEditCommentException;
import ru.practicum.ewmserv.comment.mapper.CommentMapper;
import ru.practicum.ewmserv.comment.model.Comment;
import ru.practicum.ewmserv.comment.repository.CommentRepository;
import ru.practicum.ewmserv.enums.StateAction;
import ru.practicum.ewmserv.event.exceptions.EventNotFoundException;
import ru.practicum.ewmserv.event.model.Event;
import ru.practicum.ewmserv.event.repository.EventRepository;
import ru.practicum.ewmserv.user.exceptions.UserNotFoundException;
import ru.practicum.ewmserv.user.model.User;
import ru.practicum.ewmserv.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public ResponseCommentDto createComment(long userId, long eventId, CreateCommentDto comment) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with id " + userId + " not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException("Event with id=" + eventId + " was not found"));

        if (!event.getState().equals(StateAction.PUBLISHED)) {
            throw new NotAllowToCreateCommentException("User allow comment only published events");
        }

        Comment newComment = Comment.builder().created(LocalDateTime.now()).text(comment.getText())
                .author(user).event(event).build();

        return commentMapper.toDto(commentRepository.save(newComment));
    }

    public void userDeleteComment(long userId, long commentId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException("Comment with id " + commentId + " not found"));

        if (comment.getAuthor().getId() != userId) {
            throw new NotAllowToEditCommentException("Only author allow to delete comment");
        }

        if (comment.getCreated().minusDays(1).isAfter(LocalDateTime.now())) {
            throw new NotAllowToEditCommentException("User is not allow to delete comment if after publication spent " +
                    "more then 24 hours");
        }
        commentRepository.deleteById(commentId);
    }

    public void adminDeleteComment(long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new CommentNotFoundException("Comment with id " + commentId + " not found");
        }

        commentRepository.deleteById(commentId);
    }

    public ResponseCommentDto updateComment(long userId, long commentId, PatchCommentDto patchComment) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new CommentNotFoundException("Comment with id " + commentId + " not found"));

        if (comment.getAuthor().getId() != userId) {
            throw new NotAllowToEditCommentException("Only author allow to delete comment");
        }

        if (comment.getCreated().minusHours(2).isAfter(LocalDateTime.now())) {
            throw new NotAllowToEditCommentException("User is not allow to edit comment if after publication spent " +
                    "more then 2 hours");
        }

        comment.setText(patchComment.getText());

        return commentMapper.toDto(commentRepository.save(comment));
    }
}
