package ru.practicum.main.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String text;
    @JoinColumn(name = "event_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Event event;
    @JoinColumn(name = "author_id")
    @ManyToOne(fetch = FetchType.LAZY)
    User author;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;
}
