package prpo.goalmanagment;

import java.time.LocalDate;
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
        goal.setUserId(request.userId);

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
            goal.setstartWeight(null);

            return gr.save(goal);
        }   
        else if(type == 'C'){
            goal.setcals(request.cals);
            goal.seteatenCals(request.eatenCals);

            goal.setfitnessType(null);
            goal.setcurrentWeight(null);
            goal.setgoalWeight(null);
            goal.setstartWeight(null);
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
            goal.setstartWeight(request.startWeight);

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
public List<Goal> getAllGoals(@RequestParam Long userId) {
    // pridobimo vse goale samo za podanega userja
    List<Goal> goals = gr.findAll().stream()
                         .filter(goal -> goal.getUserId() != null && goal.getUserId().equals(userId))
                         .collect(Collectors.toList());

    LocalDate today = LocalDate.now();

    for (Goal trgoal : goals) {

        // če je goal že pretekel, označi kot completed
        if (trgoal.getdateEnd() != null && !trgoal.getdateEnd().isAfter(today)) {
            trgoal.setstatus("completed");
            gr.save(trgoal);
            continue;
        }

        Character type = trgoal.getgoalType();

        if (type == 'F') {
            Character ftype = trgoal.getfitnessType();
            if (ftype == 'F') {
                if (trgoal.getweeklyFitness() != null && trgoal.getweeklyFitnessDone() != null
                        && trgoal.getweeklyFitnessDone() >= trgoal.getweeklyFitness()) {
                    trgoal.setstatus("completed");
                    gr.save(trgoal);
                    continue;
                }
            } else if (ftype == 'R') {
                if (trgoal.getkms() != null && trgoal.getkmsDone() != null
                        && trgoal.getkmsDone() >= trgoal.getkms()) {
                    trgoal.setstatus("completed");
                    gr.save(trgoal);
                    continue;
                }
            } else if (ftype == 'S') {
                if (trgoal.getsteps() != null && trgoal.getstepsDone() != null
                        && trgoal.getstepsDone() >= trgoal.getsteps()) {
                    trgoal.setstatus("completed");
                    gr.save(trgoal);
                    continue;
                }
            }
        } else if (type == 'W') {
            if (trgoal.getcurrentWeight() != null && trgoal.getgoalWeight() != null
                    && trgoal.getcurrentWeight().equals(trgoal.getgoalWeight())) {
                trgoal.setstatus("completed");
                gr.save(trgoal);
                continue;
            }
        } else if (type == 'C') {
            if (trgoal.getcals() != null && trgoal.geteatenCals() != null
                    && trgoal.geteatenCals() >= trgoal.getcals()) {
                trgoal.setstatus("completed");
                gr.save(trgoal);
                continue;
            }
        }
    }

    // uredimo goale, da so najprej "in progress"
    goals.sort((g1, g2) -> {
        boolean g1InProgress = "in progress".equals(g1.getstatus());
        boolean g2InProgress = "in progress".equals(g2.getstatus());
        if (g1InProgress && !g2InProgress) return -1;
        if (!g1InProgress && g2InProgress) return 1;
        return 0;
    });

    return goals;
}


    // //popravi da gleda userid
    // @GetMapping("/api/existsGoalType")
    // public boolean checkGoalExists(@RequestParam Character goalType, Long userId) {
    //     return gr.existsByGoalType(goalType);
    // }

    @GetMapping("/api/existsGoalType")
    public boolean checkGoalExists(@RequestParam Character goalType, @RequestParam Long userId) {
        // preverimo, če obstaja goal s podanim goalType in userId
        return gr.existsByGoalTypeAndUserId(goalType, userId);
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
        goal.setUserId(request.userId);

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
            goal.setstartWeight(null);

            return gr.save(goal);
        }   
        else if(type == 'C'){
            goal.setcals(request.cals);
            goal.seteatenCals(request.eatenCals);

            goal.setfitnessType(null);
            goal.setcurrentWeight(null);
            goal.setgoalWeight(null);
            goal.setstartWeight(null);
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
            goal.setstartWeight(request.startWeight);

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

    //popravi da gleda tudi userid
    @GetMapping("/api/getCalorieGoal")
    public Goal getCalorieGoal(@RequestParam Long userId){
        List<Goal> allGoals = gr.findAll();
        for(int i = 0; i < allGoals.size(); i++){
            if(allGoals.get(i).getgoalType() == 'C' && allGoals.get(i).getUserId() == userId){
                return allGoals.get(i);
            }
        }
        return null;
    }

    @PutMapping("/api/updateProgressCalories")
    public void updateProgressCalories(@RequestParam Long id, Integer eatenCals){
        //updejta kalorije (rocno + za avtomatsko updatanje) 
        //parameter je koliko kalorij si zdej pojedu tako da se dejansko pristejejo trenutnim 
        Optional<Goal> ogoal = gr.findById(id); //poglej tudi da je pravilen userID
        Goal trGoal = ogoal.get();
        Integer trCals = trGoal.geteatenCals();
        if(eatenCals < 0){
            Integer newCals = trCals-eatenCals;
            Integer gcals = trGoal.getcals();
            if(newCals < 0){
                trGoal.setcals(gcals - eatenCals);
                trGoal.seteatenCals(0);   
                 gr.save(trGoal);
                 return;
            }
            trGoal.seteatenCals(newCals);
            Integer newgoalcals = gcals-newCals;
            //trGoal.setcals(newgoalcals);
            gr.save(trGoal);
            return;
        }
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
