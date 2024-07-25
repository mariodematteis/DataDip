package sample;

import java.util.HashMap;

public class Conditions {

    /*

    relation
    0 >
    1 <
    2 =
    */

    private String firstCondition;
    private String firstConditionValue;
    private String secondCondition;
    private String secondConditionValue;
    private int relation;

    public Conditions(String firstCondition, String firstConditionValue, String secondCondition, String secondConditionValue, int relation) {
        this.firstCondition = firstCondition;
        this.firstConditionValue = firstConditionValue;
        this.secondCondition = secondCondition;
        this.secondConditionValue = secondConditionValue;
        this.relation = relation;
    }

    public String getFirstCondition() {
        return this.firstCondition;
    }

    public String getFirstConditionValue() {
        return firstConditionValue;
    }

    public String getSecondCondition() {
        return this.secondCondition;
    }

    public String getSecondConditionValue() {
        return secondConditionValue;
    }

    public int getRelation() {
        return this.relation;
    }

    public void setFirstCondition(String value) {
        this.firstCondition = value;
    }

    public void setFirstConditionValue(String value) {
        this.firstConditionValue = value;
    }

    public void setSecondCondition(String value) {
        this.secondCondition = value;
    }

    public void setSecondConditionValue(String value) {
        this.secondConditionValue = value;
    }

    public void setRelation(int value) {
        this.relation = value;
    }

}
