package prpo.goalmanagment;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
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
                goal.setweeklyFitnessDone(request.weeklyFitnessDone);

                goal.setkms(null);
                goal.setkmsDone(null);
                goal.setsteps(null);
                goal.setstepsDone(null);
            }
            else if(ftype == 'R'){
                goal.setkms(request.kms);
                goal.setkmsDone(request.kmsDone);

                goal.setweeklyFitness(null);
                goal.setweeklyFitnessDone(null);
                goal.setsteps(null);
                goal.setstepsDone(null);
            }
            else{ //ftype == "S"
                goal.setsteps(request.steps);
                goal.setstepsDone(request.stepsDone);

                goal.setweeklyFitness(null);
                goal.setweeklyFitnessDone(null);
                goal.setkms(null);
                goal.setkmsDone(null);
            }
            goal.setcals(null);
            goal.seteatenCals(null);
            goal.setcurrentWeight(null);
            goal.setgoalWeight(null);

            return gr.save(goal);
        }   
        else if(type == 'C'){
            goal.setcals(request.cals);
            goal.seteatenCals(request.eatenCals);

            goal.setfitnessType(null);
            goal.setcurrentWeight(null);
            goal.setgoalWeight(null);
            goal.setweeklyFitness(null);
            goal.setweeklyFitnessDone(null);
            goal.setsteps(null);
            goal.setstepsDone(null);
            goal.setkms(null);
            goal.setkmsDone(null);

            return gr.save(goal);
        }
        else{ // type == 'W'
            goal.setcurrentWeight(request.currWeight);
            goal.setgoalWeight(request.goalWeight);

            goal.setweeklyFitness(null);
            goal.setweeklyFitnessDone(null);
            goal.setsteps(null);
            goal.setstepsDone(null);
            goal.setkms(null);
            goal.setkmsDone(null);
            goal.setfitnessType(null);
            goal.setcals(null);
            goal.seteatenCals(null);

            return gr.save(goal);
        }
    }

    @GetMapping("/api/allGoals")
    public List<Goal> getAllGoals() {
        List<Goal> goals = gr.findAll(); 
        LocalDate today = LocalDate.now();
        for(int i = 0; i < goals.size(); i++){
            Goal trgoal = goals.get(i);

            if(trgoal.getdateEnd() != null && !trgoal.getdateEnd().isAfter(today)){
                trgoal.setstatus("completed");
                continue;
            }
            Character type = trgoal.getgoalType();
            if(type == 'F'){
                Character ftype = trgoal.getfitnessType();
                if(ftype == 'F'){
                    if(trgoal.getweeklyFitness() == trgoal.getweeklyFitnessDone()){
                        //completed
                        trgoal.setstatus("completed");
                        continue;
                    }
                    continue;
                }
                if(ftype == 'R'){
                    if(trgoal.getkms() == trgoal.getkmsDone()){
                        //completed
                        trgoal.setstatus("completed");
                        continue;
                    }
                    continue;
                }
                if(ftype == 'S'){
                    if(trgoal.getsteps() == trgoal.getstepsDone()){
                        //completed
                        trgoal.setstatus("completed");
                        continue;
                    }
                    continue;
                }
                
            }
            else if(type == 'W'){
                if(trgoal.getcurrentWeight() == trgoal.getgoalWeight()){
                    //completed
                    trgoal.setstatus("completed");
                    continue;
                }
                continue;   
            }
            else if(type == 'C'){
                if(trgoal.getcals() == trgoal.geteatenCals()){
                    //completed
                    trgoal.setstatus("completed");
                    continue;
                }
                continue;
            }
        }

        //uredimo goale da so najprej "in progress" prikazani
        Collections.sort(goals, new Comparator<Goal>() {
        @Override
        public int compare(Goal g1, Goal g2) {
            boolean g1InProgress = "in progress".equals(g1.getstatus());
            boolean g2InProgress = "in progress".equals(g2.getstatus());
            
            if(g1InProgress && !g2InProgress) {
                return -1; // g1 pride pred g2
            } else if(!g1InProgress && g2InProgress) {
                return 1; // g2 pride pred g1
            } else {
                return 0; // ostanejo v istem vrstnem redu
            }
        }
    });
        return goals;
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
                goal.setweeklyFitnessDone(request.weeklyFitnessDone);

                goal.setkms(null);
                goal.setkmsDone(null);
                goal.setsteps(null);
                goal.setstepsDone(null);
            }
            else if(ftype == 'R'){
                goal.setkms(request.kms);
                goal.setkmsDone(request.kmsDone);

                goal.setweeklyFitness(null);
                goal.setweeklyFitnessDone(null);
                goal.setsteps(null);
                goal.setstepsDone(null);
            }
            else{ //ftype == "S"
                goal.setsteps(request.steps);
                goal.setstepsDone(request.stepsDone);

                goal.setweeklyFitness(null);
                goal.setweeklyFitnessDone(null);
                goal.setkms(null);
                goal.setkmsDone(null);
            }
            goal.setcals(null);
            goal.seteatenCals(null);
            goal.setcurrentWeight(null);
            goal.setgoalWeight(null);

            return gr.save(goal);
        }   
        else if(type == 'C'){
            goal.setcals(request.cals);
            goal.seteatenCals(request.eatenCals);

            goal.setfitnessType(null);
            goal.setcurrentWeight(null);
            goal.setgoalWeight(null);
            goal.setweeklyFitness(null);
            goal.setweeklyFitnessDone(null);
            goal.setsteps(null);
            goal.setstepsDone(null);
            goal.setkms(null);
            goal.setkmsDone(null);

            return gr.save(goal);
        }
        else{ // type == 'W'
            goal.setcurrentWeight(request.currWeight);
            goal.setgoalWeight(request.goalWeight);

            goal.setweeklyFitness(null);
            goal.setweeklyFitnessDone(null);
            goal.setsteps(null);
            goal.setstepsDone(null);
            goal.setkms(null);
            goal.setkmsDone(null);
            goal.setfitnessType(null);
            goal.setcals(null);
            goal.seteatenCals(null);

            return gr.save(goal);
        }

    }

    @GetMapping("/api/getCalorieGoal")
    public Goal getCalorieGoal(){
        List<Goal> allGoals = gr.findAll();
        for(int i = 0; i < allGoals.size(); i++){
            if(allGoals.get(i).getgoalType() == 'C'){
                return allGoals.get(i);
            }
        }
        return null;
    }

    @PutMapping("/api/updateProgressCalories")
    public void updateProgressCalories(@RequestParam Long id, Integer eatenCals){
        //updejta kalorije (rocno + za avtomatsko updatanje) 
        //parameter je koliko kalorij si zdej pojedu tako da se dejansko pristejejo trenutnim 
        Optional<Goal> ogoal = gr.findById(id);
        Goal trGoal = ogoal.get();
        Integer trCals = trGoal.geteatenCals();
        Integer newCals = trCals+eatenCals;
        trGoal.seteatenCals(newCals);
        Integer gcals = trGoal.getcals();
        Integer newgoalcals = gcals-newCals;
        //trGoal.setcals(newgoalcals);
        gr.save(trGoal);
        return;
    }

    @PutMapping("/api/updateProgressWeight")
    public void updateProgressWeight(@RequestParam Long id, Double newWeight){
        //update na weight goalih
        Optional<Goal> ogoal = gr.findById(id);
        Goal trGoal = ogoal.get();
        trGoal.setcurrentWeight(newWeight);
        gr.save(trGoal);
        //pogledamo ce je slucajno done 
    }

    @PutMapping("/api/updateProgressFitness")
    public void updateProgressFitness(@RequestParam Long id, Double num){
        //najprej ugotovis kaksen fitnes goal je nato updatas glede na request body
        Optional<Goal> ogoal = gr.findById(id);
        Goal trGoal = ogoal.get();
        char type = trGoal.getfitnessType();
        if(type == 'F'){
            //dodamo stevilko trenutni stevilki -> gumb naj bo da doda eno telovadbo 
            Double trWeeklyFitness = trGoal.getweeklyFitnessDone();
            Double newf = trWeeklyFitness + num;
            trGoal.setweeklyFitnessDone(newf);
            Double goalf = trGoal.getweeklyFitness();
            Double newGoalf = goalf - newf;
            //trGoal.setweeklyFitness(newGoalf);
            gr.save(trGoal);
            return;
        }
        else if(type == 'R'){
            Double trkms = trGoal.getkmsDone();
            Double newtrkms = trkms+num;
            trGoal.setkmsDone(newtrkms);
            Double goalkms = trGoal.getkms();
            Double newgoalkms = goalkms-newtrkms;
            //trGoal.setkms(newgoalkms);
            gr.save(trGoal);
            return;
        }
        else if(type == 'S'){
            Integer trsteps = trGoal.getstepsDone();
            Integer newtrsteps = trsteps + num.intValue();
            trGoal.setstepsDone(newtrsteps);
            Integer goalsteps = trGoal.getsteps();
            Integer newgoalsteps = goalsteps-newtrsteps;
            //trGoal.setsteps(newgoalsteps);
            gr.save(trGoal);
            return;
        }
    }

    @PutMapping("/api/complete")
    public void complete(Long id){
        Optional<Goal> ogoal = gr.findById(id);
        Goal goal = ogoal.get();
        goal.setstatus("completed");
        gr.save(goal);
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
