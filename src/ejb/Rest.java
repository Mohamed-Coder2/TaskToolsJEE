package ejb;

import javax.ejb.Stateful;

@Stateful
public class Rest {

    String Operation;
    int Number1, Number2;


    public String getOperation() {
        return Operation;
    }

    public void setOperation(String operation) {
        this.Operation = operation;
    }

    public int getNumber1() {
        return Number1;
    }

    public void setNumber1(int number1) {
        this.Number1 = number1;
    }

    public int getNumber2() {
        return Number2;
    }

    public void setNumber2(int number2) {
        this.Number2 = number2;
    }
}
