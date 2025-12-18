package prpo.goalmanagment;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;

import prpo.goalmanagment.dtos.CreateGoalRequest;
import prpo.goalmanagment.dtos.UpdateGoalRequest;

@CrossOrigin(origins = "*")  // Dovoli vse origins
@RestController
public class GoalmanagmentApi {
    private final GoalRepository gr; 

    public GoalmanagmentApi(GoalRepository gr){
        this.gr = gr;
    }

    @GetMapping("/api/getGoal")
    public Optional<Goal> getGoal(Long id){
        return gr.findById(id);
    }

    @PostMapping("/api/addGoal")
    @Transactional
    public Goal addGoal(@RequestBody CreateGoalRequest request) {
        Goal goal = new Goal();
        
        char type = request.goalType;
        goal.setgoalTitle(request.goalTitle);
        goal.setgoalType(request.goalType);
        goal.setdateStart(request.dateStart);
        goal.setdateEnd(request.dateEnd);
        goal.setstatus(request.status);

        if(type == 'F'){
            goal.setfitnessType(request.fitnessType);
            char ftype = request.fitnessType;
            if(ftype == 'F'){
                goal.setweeklyFitness(request.weeklyFitness);

                goal.setkms(null);
                goal.setsteps(null);
            }
            else if(ftype == 'R'){
                goal.setkms(request.kms);

                goal.setweeklyFitness(null);
                goal.setsteps(null);
            }
            else{ //ftype == "S"
                goal.setsteps(request.steps);

                goal.setweeklyFitness(null);
                goal.setkms(null);
            }
            goal.setcals(null);
            goal.setcurrentWeight(null);
            goal.setgoalWeight(null);

            return gr.save(goal);
        }   
        else if(type == 'C'){
            goal.setcals(request.cals);

            goal.setfitnessType(null);
            goal.setcurrentWeight(null);
            goal.setgoalWeight(null);
            goal.setweeklyFitness(null);
            goal.setsteps(null);
            goal.setkms(null);

            return gr.save(goal);
        }
        else{ // type == 'W'
            goal.setcurrentWeight(request.currWeight);
            goal.setgoalWeight(request.goalWeight);

            goal.setweeklyFitness(null);
            goal.setsteps(null);
            goal.setkms(null);
            goal.setfitnessType(null);
            goal.setcals(null);

            return gr.save(goal);
        }
    }

    @GetMapping("/api/allGoals")
    public List<Goal> getAllGoals() {
        return gr.findAll(); // Če uporabljaš JPA repository
    }

    @GetMapping("/api/existsGoalType")
    public boolean checkGoalExists(@RequestParam Character goalType) {
        return gr.existsByGoalType(goalType);
    }

    @DeleteMapping("/api/deleteGoal")
    public void deleteGoal(@RequestParam Long id){
        gr.deleteById(id);
    }

    @PutMapping("/api/updateGoal")
    public Goal updateGoal(@RequestParam Long id, @RequestBody UpdateGoalRequest request){
        Optional<Goal> ogoal = gr.findById(id);
        Goal goal = ogoal.get();

        char type = request.goalType;
        goal.setgoalTitle(request.goalTitle);
        goal.setgoalType(request.goalType);
        goal.setdateStart(request.dateStart);
        goal.setdateEnd(request.dateEnd);
        goal.setstatus(request.status);

        if(type == 'F'){
            goal.setfitnessType(request.fitnessType);
            char ftype = request.fitnessType;
            if(ftype == 'F'){
                goal.setweeklyFitness(request.weeklyFitness);

                goal.setkms(null);
                goal.setsteps(null);
            }
            else if(ftype == 'R'){
                goal.setkms(request.kms);

                goal.setweeklyFitness(null);
                goal.setsteps(null);
            }
            else{ //ftype == "S"
                goal.setsteps(request.steps);

                goal.setweeklyFitness(null);
                goal.setkms(null);
            }
            goal.setcals(null);
            goal.setcurrentWeight(null);
            goal.setgoalWeight(null);

            return gr.save(goal);
        }   
        else if(type == 'C'){
            goal.setcals(request.cals);

            goal.setfitnessType(null);
            goal.setcurrentWeight(null);
            goal.setgoalWeight(null);
            goal.setweeklyFitness(null);
            goal.setsteps(null);
            goal.setkms(null);

            return gr.save(goal);
        }
        else{ // type == 'W'
            goal.setcurrentWeight(request.currWeight);
            goal.setgoalWeight(request.goalWeight);

            goal.setweeklyFitness(null);
            goal.setsteps(null);
            goal.setkms(null);
            goal.setfitnessType(null);
            goal.setcals(null);

            return gr.save(goal);
        }

    }


    // //to nevem ce dela 
    // @PutMapping("/api/{id}/progress")
    // @Transactional
    // public Goal updateProgress(@PathVariable Long id, @RequestBody Double newProgress) {
    //     Optional<Goal> goalOpt = gr.findById(id);
    //     if (goalOpt.isPresent()) {
    //         Goal goal = goalOpt.get();
    //         goal.setcurrentValue(newProgress);
            
    //         // Preveri če je goal dosežen
    //         if (goal.getcurrentValue() >= goal.getTargetValue()) {
    //             goal.setStatus("COMPLETED");
    //         }
            
    //         return gr.save(goal);
    //     }
    //     return null;
    // }
    
}
