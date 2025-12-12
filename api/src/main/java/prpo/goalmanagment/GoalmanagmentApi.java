package prpo.goalmanagment;

import org.springframework.web.bind.annotation.*;

@RestController
public class GoalmanagmentApi {
    private final GoalRepository gr; 

    public GoalmanagmentApi(GoalRepository gr){
        this.gr = gr;
    }

    @GetMapping("/api/getGoal")
    public String getGoal(int id){
        return "hello";
    }

    @PostMapping("/api/addGoal")
    public Goal addGoal(@RequestBody Goal goal){
        return gr.save(goal);
    }
}
