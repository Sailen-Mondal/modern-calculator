package com.calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

public class CalculatorApp extends Application {
    private TextField display;
    private String currentNumber = "";
    private String operator = "";
    private double result = 0;
    private boolean startNewNumber = true;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #2C3E50;");

        // Display
        display = new TextField("0");
        display.setEditable(false);
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setStyle("-fx-background-color: #34495E; -fx-text-fill: white; -fx-font-size: 24px;");
        display.setMinHeight(50);
        display.setPrefWidth(300);

        // Button grid
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);
        buttonGrid.setAlignment(Pos.CENTER);

        // Create calculator buttons
        String[][] buttonLabels = {
            {"C", "±", "%", "÷"},
            {"7", "8", "9", "×"},
            {"4", "5", "6", "-"},
            {"1", "2", "3", "+"},
            {"0", ".", "="}
        };

        for (int i = 0; i < buttonLabels.length; i++) {
            for (int j = 0; j < buttonLabels[i].length; j++) {
                String label = buttonLabels[i][j];
                Button button = createButton(label);
                
                if (label.equals("0")) {
                    buttonGrid.add(button, j, i, 2, 1);
                    button.setPrefWidth(130);
                } else if (j < buttonLabels[i].length) {
                    buttonGrid.add(button, j, i);
                }
            }
        }

        root.getChildren().addAll(display, buttonGrid);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Modern Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(60, 60);
        button.setFont(Font.font(18));
        
        String style;
        if ("0123456789.".contains(text)) {
            style = "-fx-background-color: #34495E; -fx-text-fill: white;";
        } else if ("+-×÷%".contains(text)) {
            style = "-fx-background-color: #E67E22; -fx-text-fill: white;";
        } else if ("=".equals(text)) {
            style = "-fx-background-color: #27AE60; -fx-text-fill: white;";
        } else {
            style = "-fx-background-color: #7F8C8D; -fx-text-fill: white;";
        }
        
        button.setStyle(style);
        
        button.setOnMouseEntered(e -> button.setStyle(style + "-fx-opacity: 0.8;"));
        button.setOnMouseExited(e -> button.setStyle(style));
        
        button.setOnAction(e -> handleButtonClick(text));
        
        return button;
    }

    private void handleButtonClick(String value) {
        switch (value) {
            case "C":
                clear();
                break;
            case "=":
                calculate();
                break;
            case "±":
                negate();
                break;
            case "%":
                percentage();
                break;
            case "+":
            case "-":
            case "×":
            case "÷":
                handleOperator(value);
                break;
            default:
                handleNumber(value);
                break;
        }
    }

    private void clear() {
        currentNumber = "";
        operator = "";
        result = 0;
        startNewNumber = true;
        display.setText("0");
    }

    private void calculate() {
        if (!operator.isEmpty() && !currentNumber.isEmpty()) {
            double secondNumber = Double.parseDouble(currentNumber);
            switch (operator) {
                case "+":
                    result += secondNumber;
                    break;
                case "-":
                    result -= secondNumber;
                    break;
                case "×":
                    result *= secondNumber;
                    break;
                case "÷":
                    if (secondNumber != 0) {
                        result /= secondNumber;
                    } else {
                        display.setText("Error");
                        return;
                    }
                    break;
            }
            display.setText(formatResult(result));
            currentNumber = "";
            operator = "";
            startNewNumber = true;
        }
    }

    private void negate() {
        if (!currentNumber.isEmpty()) {
            double number = Double.parseDouble(currentNumber);
            currentNumber = String.valueOf(-number);
            display.setText(currentNumber);
        }
    }

    private void percentage() {
        if (!currentNumber.isEmpty()) {
            double number = Double.parseDouble(currentNumber);
            currentNumber = String.valueOf(number / 100);
            display.setText(currentNumber);
        }
    }

    private void handleOperator(String op) {
        if (!currentNumber.isEmpty()) {
            if (operator.isEmpty()) {
                result = Double.parseDouble(currentNumber);
            } else {
                calculate();
            }
            operator = op;
            startNewNumber = true;
        }
    }

    private void handleNumber(String number) {
        if (startNewNumber) {
            currentNumber = number;
            startNewNumber = false;
        } else {
            currentNumber += number;
        }
        display.setText(currentNumber);
    }

    private String formatResult(double value) {
        if (value == (long) value) {
            return String.format("%d", (long) value);
        }
        return String.format("%.2f", value);
    }

    public static void main(String[] args) {
        launch(args);
    }
} 