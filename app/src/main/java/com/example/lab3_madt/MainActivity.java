package com.example.lab3_madt;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.mariuszgromada.math.mxparser.*;

public class MainActivity extends AppCompatActivity {

    private TextView primaryTextView;
    private StringBuilder currentInput;
    private final String operators = "+-*/"; // valid operators

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        primaryTextView = findViewById(R.id.idTVprimary); // Adjust this ID based on your layout
        currentInput = new StringBuilder();
        //0
        primaryTextView.setText("0");
        //button click
        setupButtonListeners();
        /*Expression e = new Expression("2+3");
        double v = e.calculate();
        Toast.makeText(this,String.valueOf(v),Toast.LENGTH_LONG).show();*/

    }
    private void setupButtonListeners() {
        // numbers
        Button button0 = findViewById(R.id.b0);
        button0.setOnClickListener(v -> appendToInput("0"));

        Button button1 = findViewById(R.id.b1);
        button1.setOnClickListener(v -> appendToInput("1"));

        Button button2 = findViewById(R.id.b2);
        button2.setOnClickListener(v -> appendToInput("2"));

        Button button3 = findViewById(R.id.b3);
        button3.setOnClickListener(v -> appendToInput("3"));

        Button button4 = findViewById(R.id.b4);
        button4.setOnClickListener(v -> appendToInput("4"));

        Button button5 = findViewById(R.id.b5);
        button5.setOnClickListener(v -> appendToInput("5"));

        Button button6 = findViewById(R.id.b6);
        button6.setOnClickListener(v -> appendToInput("6"));

        Button button7 = findViewById(R.id.b7);
        button7.setOnClickListener(v -> appendToInput("7"));

        Button button8 = findViewById(R.id.b8);
        button8.setOnClickListener(v -> appendToInput("8"));

        Button button9 = findViewById(R.id.b9);
        button9.setOnClickListener(v -> appendToInput("9"));

        // operators
        Button buttonPlus = findViewById(R.id.bplus);
        buttonPlus.setOnClickListener(v -> appendOperator("+"));

        Button buttonMinus = findViewById(R.id.bminus);
        buttonMinus.setOnClickListener(v -> appendOperator("-"));

        Button buttonMultiply = findViewById(R.id.bmul);
        buttonMultiply.setOnClickListener(v -> appendOperator("*"));

        Button buttonDivide = findViewById(R.id.bdiv);
        buttonDivide.setOnClickListener(v -> appendOperator("/"));

        Button buttonDot = findViewById(R.id.bdot);
        buttonDot.setOnClickListener(v -> appendDot());

        Button buttonBracket1 = findViewById(R.id.bbrac1);
        buttonBracket1.setOnClickListener(v -> appendToInput("("));

        Button buttonBracket2 = findViewById(R.id.bbrac2);
        buttonBracket2.setOnClickListener(v -> appendToInput(")"));

        Button buttonEquals = findViewById(R.id.bequal);
        buttonEquals.setOnClickListener(v -> calculateResult());

        // C (Clear) button
        Button buttonC = findViewById(R.id.bc);
        buttonC.setOnClickListener(v -> clearLastInput());

        // AC button
        Button buttonAC = findViewById(R.id.bac);
        buttonAC.setOnClickListener(v -> clearAllInput());

    }
    private void appendToInput(String value) {
        if (currentInput.length() == 0 || currentInput.toString().equals("0")) {
            currentInput.setLength(0); // clear
        }
        currentInput.append(value);
        primaryTextView.setText(currentInput.toString());
    }
    private void appendOperator(String operator) {
        // if the current input is empty or just "0"
        if (currentInput.length() == 0 || currentInput.toString().equals("0")) {
            currentInput.setLength(0); //clear
            currentInput.append("0").append(operator); // replace with "0" followed by the operator
        } else {
            char lastChar = currentInput.charAt(currentInput.length() - 1);
            // not adding the same operator consecutively
            if (operators.indexOf(lastChar) == -1) { // lastChar is not an operator
                currentInput.append(operator);
            }
        }
        primaryTextView.setText(currentInput.toString());
    }
    private void appendDot() {
        // allow for a dot if current input is empty or just "0"
        if (currentInput.length() == 0 || currentInput.toString().equals("0")) {
            currentInput.setLength(0); // Clear the input
            currentInput.append("0."); // Start with "0."
        } else {
            char lastChar = currentInput.charAt(currentInput.length() - 1);
            // Allow dot if the last character is a digit
            if (Character.isDigit(lastChar)) {
                // Check if there's already a dot in the current number
                if (!currentInput.toString().contains(".")) {
                    currentInput.append("."); // Append the dot
                }
            } else if (operators.indexOf(lastChar) != -1) {
                // If the last character is an operator, start a new decimal number
                currentInput.append("0."); // Append "0."
            }
        }
        primaryTextView.setText(currentInput.toString());
    }


    private void calculateResult() {
        String expression = currentInput.toString();
        //library logic
        if (!expression.isEmpty()) {
            try {
                double result = evaluateExpression(expression);

                // update the primary TV
                primaryTextView.setText(String.valueOf(result));
                //currentInput.setLength(0); // Clear the current input after calculation if needed
                //currentInput.append(result); // Optionally store the result for further calculations
            } catch (Exception ex) {
                // Handle any exceptions that occur during calculation
                primaryTextView.setText("Error"); // Display an error message
                currentInput.setLength(0); // Clear the current input
            }
        }
    }
    private void clearLastInput() {
        //if length is 1 then display 0
        if (currentInput.length() == 1) {
            primaryTextView.setText("0");
            currentInput.setLength(0); // clear the entire input
        }
        else if (currentInput.length() > 0) {
            currentInput.deleteCharAt(currentInput.length() - 1); // Remove the last character
            primaryTextView.setText(currentInput.toString());
        }
    }

    private void clearAllInput() {
        currentInput.setLength(0); // all clear
        primaryTextView.setText("0"); // clear the TV
    }

    private double evaluateExpression(String expression) {
        // Use your library's evaluation method
        Expression e = new Expression(expression);
        return e.calculate(); // Return the calculated result
    }
}