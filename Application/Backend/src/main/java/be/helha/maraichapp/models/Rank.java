package be.helha.maraichapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ranks")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRank;
    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RankEnum name;
    @Column(nullable = false, unique = true)
    private int priorityLevel;

    public Rank(RankEnum name, int priorityLevel){
        this.priorityLevel = priorityLevel;
        this.name = name;
    }
}
