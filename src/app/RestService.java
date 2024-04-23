package app;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import ejb.Rest;

@Stateful
@Path("/rest")
public class RestService {

    @EJB
    Rest rest;

    List<Rest> history = new ArrayList<>();

    @GET
    @Path("/history")
    @Produces(MediaType.TEXT_PLAIN)
    public String getHistory() {
        if (history.isEmpty()) {
            return "History List is Empty :(";
        } else {
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (Rest item : history) {
                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("Number1", item.getNumber1())
                        .add("Operation", item.getOperation())
                        .add("Number2", item.getNumber2())
                );
            }
            return jsonArrayBuilder.build().toString();
        }
    }

    @POST
    @Path("/calc")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addCalc(String json) {
        try {
            // Parse JSON string
            JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
            int number1 = jsonObject.getInt("number1");
            int number2 = jsonObject.getInt("number2");
            String operation = jsonObject.getString("operation");

            // Calculate the result
            double result = calculate(number1, number2, operation);

            // Persist the calculation to the history
            Rest calc = new Rest();
            calc.setNumber1(number1);
            calc.setNumber2(number2);
            calc.setOperation(operation);
            history.add(calc);
            
            //Log the state of the history list
            logHistory();

            return String.valueOf(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    
    private void logHistory() {
        System.out.println("Size of history list: " + history.size());
        System.out.println("Contents of history list:");
        for (Rest item : history) {
            System.out.println(item.getNumber1() + " " + item.getOperation() + " " + item.getNumber2());
        }
    }

    private double calculate(int number1, int number2, String operation) {
        switch (operation) {
            case "+":
                return number1 + number2;
            case "-":
                return number1 - number2;
            case "*":
                return number1 * number2;
            case "/":
                if (number2 != 0) {
                    return number1 / number2;
                } else {
                    throw new ArithmeticException("Division by zero!");
                }
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }
}
