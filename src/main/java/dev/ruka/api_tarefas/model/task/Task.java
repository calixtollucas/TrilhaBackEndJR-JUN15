package dev.ruka.api_tarefas.model.task;

import dev.ruka.api_tarefas.model.area.Area;
import dev.ruka.api_tarefas.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    Boolean complete;

    @Column(nullable = false)
    Boolean important;

    @Column(nullable = false)
    Boolean urgent;

    @ManyToOne
    @JoinColumn(nullable = false, name = "area_id")
    Area area;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    User user;

    public Task(String title, Area area, User user, Integer important, Integer urgent) {
        this.title = title;
        this.area = area;
        this.user = user;
        this.important = (important != 0);
        this.urgent = (urgent != 0);
        this.complete = false;
    }
}
