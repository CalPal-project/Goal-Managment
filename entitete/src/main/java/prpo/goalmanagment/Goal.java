package prpo.goalmanagment;
import jakarta.persistence.*;

@Entity
@Table(name = "goal")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String goalTitle;
    private Integer goal;
    private char goalType;

    public Goal(){

    }

    public Goal(String goalTitle, Integer goal, char goalType){
        this.goalTitle = goalTitle;
        this.goal = goal;
        this.goalType = goalType;
    }

    public String getgoalTitle(){
        return goalTitle;
    }

    public Integer getgoal(){
        return goal;
    }

    public char goalType(){
        return goalType;
    }

    public void setgoalTitle(String s){
        this.goalTitle = s;
    }

    public void setgoal(Integer i){
        this.goal = i;
    }

    public void setgoalType(char c){
        this.goalType = c;
    }
}