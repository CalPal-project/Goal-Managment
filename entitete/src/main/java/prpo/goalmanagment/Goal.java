package prpo.goalmanagment;
import java.time.LocalDate;

import jakarta.persistence.*;


@Entity
@Table(name = "goal")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String goalTitle;
    private Character goalType;// F, W, C
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String status; // in progress, done
    //private Integer progress = 0;
    
    //F:
    private Character fitnessType = null; //F, R, S fitnes, run, steps
    private Double weeklyFitness = null;
    private Double kms = null;
    private Integer steps = null;

    //C:
    private Integer cals = null;

    //W:
    private Double currWeight = null;
    private Double goalWeight = null;

    public Goal(){

    }

    //setterji:
    public void setgoalTitle(String s){
        this.goalTitle =s;
    }

    public void setgoalType(Character c){
        this.goalType = c;
    }

    public void setdateStart(LocalDate d){
        this.dateStart = d;
    }

    public void setdateEnd(LocalDate d){
        this.dateEnd = d;
    }

    public void setstatus(String s){
        this.status = s;
    }

    public void setfitnessType(Character c){
        this.fitnessType = c;
    }

    public void setweeklyFitness(Double d){
        this.weeklyFitness = d;
    }

    public void setkms(Double d){
        this.kms = d;
    }

    public void setsteps(Integer i){
        this.steps = i;
    }

    public void setcals(Integer i){
        this.cals = i;
    }

    public void setcurrentWeight(Double c){
        this.currWeight = c;
    }

    public void setgoalWeight(Double d){
        this.goalWeight = d;
    }

    //getterji:
    public Long getId(){
        return id;
    }

    public String getgoalTitle(){
        return goalTitle;
    }

    public Character getgoalType(){
        return goalType;
    }

    public LocalDate getdateStart(){
        return dateStart;
    }

    public LocalDate getdateEnd(){
        return dateEnd;
    }

    public String getstatus(){
        return status;
    }

    public Character getfitnessType(){
        return fitnessType;
    }

    public Double getweeklyFitness(){
        return weeklyFitness;
    }

    public Double getkms(){
        return kms;
    }

    public Integer getsteps(){
        return steps;
    }

    public Integer getcals(){
        return cals;
    }

    public Double getcurrentWeight(){
        return currWeight;
    }

    public Double getgoalWeight(){
        return goalWeight;
    }
}