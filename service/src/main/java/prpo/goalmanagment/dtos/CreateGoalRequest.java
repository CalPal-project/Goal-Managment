package prpo.goalmanagment.dtos;

import java.time.LocalDate;
import java.util.List;

public class CreateGoalRequest {
    public String goalTitle;
    public Character goalType;// F, W, C
    public LocalDate dateStart;
    public LocalDate dateEnd;
    public String status; // in progress, done
    
    //F:
    public Character fitnessType; //F, R, S fitnes, run, steps
    public Double weeklyFitness;
    public Double kms;
    public Integer steps;

    //C:
    public Integer cals;
    public Integer eatenCals;

    //W:
    public Double currWeight;
    public Double goalWeight;
    public Double weeklyFitnessDone=0.0;
    public Double kmsDone=0.0;
    public Integer stepsDone=0;
}
