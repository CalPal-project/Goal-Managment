package prpo.goalmanagment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List; 

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    
    // // Preveri če obstaja aktiven goal za določen tip
    // @Query("SELECT COUNT(g) > 0 FROM Goal g WHERE g.goalType = :goalType")
    // boolean existsByGoalType(@Param("goalType") char goalType);

    // // Pridobi aktiven goal za določen tip
    // Optional<Goal> findByGoalTypeAndStatus(String goalType, String status);

    // // Pridobi vse aktivne goale
    // List<Goal> findByStatus(String status);
    
    // Pridobi vse goale
    List<Goal> findAll();
    
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END " + "FROM Goal g WHERE g.goalType = :goalType")
    boolean existsByGoalType(@Param("goalType") char goalType);
}