package ru.practicum.main.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main.model.Comments;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    @Query("select c from Comments c " +
            "where (upper(c.text) like upper(concat('%', ?1, '%') ) ) " +
            "and c.created >= ?2 " +
            "and c.created <= ?3 ")
    List<Comments> search(String text, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<Comments> findAllByEvent_Id(Long eventId);

}
