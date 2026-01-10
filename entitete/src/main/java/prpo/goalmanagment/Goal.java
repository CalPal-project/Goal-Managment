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
        private LocalDate dateEnd; //optional
        private String status; // in progress, completed
        
        
        //F:
        private Character fitnessType = null; //F, R, S fitnes, run, steps
        private Double weeklyFitness = null;
        private Double weeklyFitnessDone = null;
        private Double kms = null;
        private Double kmsDone = null;
        private Integer steps = null;
        private Integer stepsDone = null;

        //C:
        private Integer cals = null;
        private Integer eatenCals = 0;

        //W:
        private Double currWeight = null;
        private Double goalWeight = null;
        private Double startWeight = null;

        private Long userId;

    public Goal(){

    }

    //setterji:
    public void setUserId(Long s){
        this.userId = s;
    }
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

    public void setweeklyFitnessDone(Double d){
        this.weeklyFitnessDone = d;
    }

    public void setkms(Double d){
        this.kms = d;
    }

    public void setkmsDone(Double d){
        this.kmsDone = d;
    }

    public void setsteps(Integer i){
        this.steps = i;
    }

    public void setstepsDone(Integer i){
        this.stepsDone = i;
    }

    public void setcals(Integer i){
        this.cals = i;
    }

    public void seteatenCals(Integer i){
        this.eatenCals = i;
    }

    public void setcurrentWeight(Double c){
        this.currWeight = c;
    }

    public void setstartWeight(Double d){
        this.startWeight = d;
    }

    public void setgoalWeight(Double d){
        this.goalWeight = d;
    }

    //getterji:
    public Long getUserId(){
        return userId;
    }
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

    public Double getweeklyFitnessDone(){
        return weeklyFitnessDone;
    }

    public Double getkms(){
        return kms;
    }

    public Double getkmsDone(){
        return kmsDone;
    }

    public Integer getsteps(){
        return steps;
    }

    public Integer getstepsDone(){
        return stepsDone;
    }

    public Integer getcals(){
        return cals;
    }

    public Integer geteatenCals(){
        return eatenCals;
    }

    public Double getcurrentWeight(){
        return currWeight;
    }

    public Double getstartWeight(){
        return startWeight;
    }

    public Double getgoalWeight(){
        return goalWeight;
    }
}