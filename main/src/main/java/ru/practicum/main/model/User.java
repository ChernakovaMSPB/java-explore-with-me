package ru.practicum.main.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Table(name = "users")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String email;
}
