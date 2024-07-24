package dev.ruka.api_tarefas.model.area;

import dev.ruka.api_tarefas.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_areas",
            joinColumns = @JoinColumn(name = "area_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> users;

    public Area(String title) {
        this.title = title;
        this.users = new HashSet<>();
    }

    public Area(UUID id, String title){
        this.id = id;
        this.title = title;
        this.users = new HashSet<>();
    }
}
