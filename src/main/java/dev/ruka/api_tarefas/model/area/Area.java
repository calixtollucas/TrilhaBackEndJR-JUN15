package dev.ruka.api_tarefas.model.area;

import dev.ruka.api_tarefas.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "areas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false, unique = true)
    String title;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    User user;

    public Area(String title, User user) {
        this.title = title;
        this.user = user;
    }

    public Area(UUID id, String title){
        this.id = id;
        this.title = title;
    }
}
