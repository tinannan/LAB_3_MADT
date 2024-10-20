package com.example.lab3_madt;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.mariuszgromada.math.mxparser.*;

public class MainActivity extends AppCompatActivity {

    private TextView primaryTextView;
    private TextView secondaryTextView;
    private StringBuilder currentInput;
    private final String operators = "+-×÷"; // valid operators
    //private boolean signChanged = false; // track if the sign has been changed
    private static final int MAX_INPUT_LENGTH = 14;


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
        primaryTextView = findViewById(R.id.idTVprimary);
        secondaryTextView = findViewById(R.id.idTVSecondary);
        currentInput = new StringBuilder();
        //0
        primaryTextView.setText("0");
        secondaryTextView.setText("");
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
        buttonMultiply.setOnClickListener(v -> appendOperator("×"));

        Button buttonDivide = findViewById(R.id.bdiv);
        buttonDivide.setOnClickListener(v -> appendOperator("÷"));

        Button buttonDot = findViewById(R.id.bdot);
        buttonDot.setOnClickListener(v -> appendDot());

        Button buttonBracket1 = findViewById(R.id.bbrac1);
        buttonBracket1.setOnClickListener(v -> appendToInput("("));

        Button buttonBracket2 = findViewById(R.id.bbrac2);
        buttonBracket2.setOnClickListener(v -> appendToInput(")"));

        Button buttonSquareRoot = findViewById(R.id.bsqrt);
        buttonSquareRoot.setOnClickListener(v -> appendToInput("√"));

        Button buttonPower = findViewById(R.id.bsquare);
        buttonPower.setOnClickListener(v -> appendToInput("^"));

        Button buttonFactorial = findViewById(R.id.bfact);
        buttonFactorial.setOnClickListener(v -> appendOperator("!"));

        Button buttonPercent = findViewById(R.id.bperc);
        buttonPercent.setOnClickListener(v -> {
            appendOperator("%");
            calculateResult();
        });

        Button buttonSignChange = findViewById(R.id.bsignchange);
        buttonSignChange.setOnClickListener(v -> changeSign());

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
        if (currentInput.length() >= MAX_INPUT_LENGTH) {
            return; //prevent buffer overflow
        }
        //signChanged = false;
        if (currentInput.length() == 0 || currentInput.toString().equals("0")) {
            currentInput.setLength(0); // clear
        }
        // prevent more than 2 ^^ (tetration)
        if (value.equals("^")) {
            int count = 0;
            int i = currentInput.length() - 1;

            while (i >= 0 && currentInput.charAt(i) == '^') {
                count++;
                i--;
            }


            if (count >= 2) {
                return;
            }
        }

        currentInput.append(value);
        primaryTextView.setText(currentInput.toString());
    }
    private void appendOperator(String operator) {
        //signChanged = false;
        // if the current input is empty or just "0"
        if (currentInput.length() == 0 || currentInput.toString().equals("0")) {
            currentInput.setLength(0); //clear
            currentInput.append("0").append(operator); // replace with "0" followed by the operator
        } else {
            char lastChar = currentInput.charAt(currentInput.length() - 1);
            // not adding the same operator consecutively
            if (lastChar != '.' && operators.indexOf(lastChar) == -1) { // lastChar is not an operator
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
            // allow dot if the last character is a digit
            if (Character.isDigit(lastChar)) {
                // Check if there's already a dot in the current number or it is an operator
                if (lastChar != '.' && lastChar != '-' && lastChar != '+' && lastChar != '÷' && lastChar != '×' && lastChar != '!'){
                    currentInput.append("."); // Append the dot
                }
            } else if (operators.indexOf(lastChar) != -1) {
                // if the last character is an operator start a new decimal number
                currentInput.append("0."); // Append "0."
            }
        }
        primaryTextView.setText(currentInput.toString());
    }
    private void changeSign() {
        if (currentInput.length() == 0) {
            return; // nothing to change
        }

        // the last number in the current input
        int lastIndex = currentInput.length() - 1;
        StringBuilder number = new StringBuilder();

        // extract the number (including the negative sign if present)
        while (lastIndex >= 0 && (Character.isDigit(currentInput.charAt(lastIndex)) || currentInput.charAt(lastIndex) == '.')) {
            number.insert(0, currentInput.charAt(lastIndex)); // Prepend to the number
            lastIndex--;
        }

        // check if the number is already negated
        if (lastIndex >= 0 && currentInput.charAt(lastIndex) == '-') {
            number.insert(0, '-'); // include the negative sign
            lastIndex--; //index back
        }

        // if the last character before the number is an operator or if there's no number
        // toggle the sign of the last number found
        if (number.length() > 0) {
            String currentNumber = number.toString();
            double num = Double.parseDouble(currentNumber);

            // change sign
            num = -num;

            // replace the last number in the input with its negated value
            String newNumber = String.valueOf(num);
            currentInput.delete(lastIndex + 1, currentInput.length()); // remove the last number
            currentInput.append(newNumber); // new number with changed sign

            primaryTextView.setText(currentInput.toString());
        }
    }





    private void calculateResult() {
        String expression = currentInput.toString();
        //library logic
        if (!expression.isEmpty()) {
            try {
                double result = evaluateExpression(expression);

                // update the primary TV
                primaryTextView.setText(String.valueOf(result));
                secondaryTextView.setText(expression);
                //signChanged = false;
                currentInput.setLength(0); // Clear the current input after calculation if needed
                currentInput.append(result); // Optionally store the result for further calculations
            } catch (Exception ex) {
                // handle exceptions
                primaryTextView.setText(R.string.error); // Display an error message
                currentInput.setLength(0); // Clear the current input
            }
        }
    }
    private void clearLastInput() {
        //if length is 1 then display 0
        if (currentInput.length() == 1) {
            primaryTextView.setText("0");
            secondaryTextView.setText("");
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
        secondaryTextView.setText("");
    }

    private double evaluateExpression(String expression) {
        if (expression.endsWith(".")) {
            // remove the trailing dot for calculation purposes
            expression = expression.substring(0, expression.length() - 1);
        }
        // library's evaluation method
        Expression e = new Expression(expression);
        return e.calculate();
    }
}