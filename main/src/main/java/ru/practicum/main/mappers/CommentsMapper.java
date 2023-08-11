package ru.practicum.main.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.main.dto.CommentsDto;
import ru.practicum.main.model.Comments;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentsMapper {

    @Mapping(target = "author", source = "comments.author.name")
    CommentsDto toCommentsDto(Comments comments);

    @Mapping(target = "author", source = "comments.author.name")
    List<CommentsDto> toCommentsDtoCollection(Collection<Comments> comments);

}
