package ru.practicum.ewmserv.comment.mapper;

import org.mapstruct.*;
import ru.practicum.ewmserv.comment.dto.CreateCommentDto;
import ru.practicum.ewmserv.comment.dto.PatchCommentDto;
import ru.practicum.ewmserv.comment.dto.ResponseCommentDto;
import ru.practicum.ewmserv.comment.model.Comment;
import ru.practicum.ewmserv.user.mapper.UserMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {

    Comment toEntity(CreateCommentDto createCommentDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment partialUpdate(PatchCommentDto patchCommentDto, @MappingTarget Comment comment);

    ResponseCommentDto toDto(Comment comment);

}