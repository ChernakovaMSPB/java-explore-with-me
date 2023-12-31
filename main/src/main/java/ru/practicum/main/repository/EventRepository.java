package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.enums.EventState;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Page<Event> findAll(Specification<Event> specification, Pageable pageable);

    Boolean existsByCategoryId(Long id);


    List<Event> findAllByInitiator_Id(Long userId, Pageable pageable);

    Event findEventByIdAndInitiator_Id(Long id, Long userId);

    Event findEventByIdAndAndState(Long id, EventState state);

    Event findEventById(Long id);

    List<Event> findAllByIdIn(List<Long> ids);
}
