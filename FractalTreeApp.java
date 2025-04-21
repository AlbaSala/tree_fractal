/*
 * FractalTreeApp.java is a JavaFX application that draws a fractal tree on a canvas.
 * The user can specify the depth and angle of the tree using text fields.
 * The application also includes an animation feature that gradually grows the tree to the specified depth.
 * It's the first JavaFX application I've written, so it's a bit rough around the edges.
 * I might clean it up later, but for now, it's a fun little project to play with and become familiar with JavaFX.
 */

import javafx.animation.KeyFrame; // for creating keyframes in the animation
import javafx.animation.Timeline; // for creating the animation timeline
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane; // for layout management
import javafx.scene.layout.HBox; // for horizontal layout
import javafx.stage.Stage;
import javafx.event.ActionEvent; // for handling button actions
import javafx.event.EventHandler; // for handling events
import javafx.util.Duration; // for specifying time durations

public class FractalTreeApp extends Application {
    // Constants for the canvas size
    private static final int WIDTH = 900;
    private static final int HEIGHT = 700;

    // Default values for depth and angle
    private int depth = 10;
    private double angle = 30;

    private FractalTree fractalTree; // Instance of the FractalTree class
    
    // Animation variables
    private Timeline growthAnimation;
    private int currentDepth = 1;

    @Override
    public void start(Stage primaryStage) {
        final Canvas canvas = new Canvas(WIDTH, HEIGHT); // Create a canvas for drawing
        canvas.setStyle("-fx-background-color: white;"); // Set background color to white
        final GraphicsContext gc = canvas.getGraphicsContext2D(); // Get the graphics context for drawing

        // Initialize the FractalTree with the default angle
        fractalTree = new FractalTree(angle);

        final TextField depthField = new TextField(Integer.toString(depth)); // TextField for depth input
        depthField.setPrefWidth(60);  // Set preferred width for the TextField

        final TextField angleField = new TextField(Double.toString(angle)); // TextField for angle input
        angleField.setPrefWidth(60); // Set preferred width for the TextField

        Button drawButton = new Button("Draw");

        drawButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    int newDepth = Integer.parseInt(depthField.getText()); // Parse the depth from the TextField
                    double newAngle = Double.parseDouble(angleField.getText()); // Parse the angle from the TextField
                    if (newDepth < 1) {
                        System.out.println("Depth must be at least 1."); // Validate depth input
                        return;
                    }

                    depth = newDepth;
                    angle = newAngle;

                    //fractalTree.setAngle(angle);
                    // Create a new FractalTree instance with the new angle, just trying not to use setters:
                    fractalTree = new FractalTree(angle); 
                    redraw(gc, depth);
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input. Please enter numbers."); // Handle invalid input
                }
            }
        });

        Button growButton = new Button("Animate Growth");

        growButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    final int targetDepth = Integer.parseInt(depthField.getText()); // Parse the target depth from the TextField
                    if (targetDepth < 1) {
                        System.out.println("Depth must be at least 1."); // Validate depth input
                        return;
                    }
                    final double newAngle = Double.parseDouble(angleField.getText()); // Parse the angle from the TextField
                    if (newAngle < 0 || newAngle > 90) {
                        System.out.println("Angle must be between 0 and 90 degrees."); // Validate angle input
                        return;
                    }
                    //fractalTree.setAngle(newAngle);
                    fractalTree = new FractalTree(angle); // Setters aren't cool
                    currentDepth = 1;

                    if (growthAnimation != null) {
                        // Stop the previous animation if it exists
                        growthAnimation.stop();
                    }

                    growthAnimation = new Timeline(); 
                    growthAnimation.setCycleCount(Timeline.INDEFINITE); // Set the animation to repeat indefinitely
                    growthAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
                        // Handle the animation frame
                        @Override
                        public void handle(ActionEvent e) {
                            if (currentDepth <= targetDepth) {
                                // Redraw the fractal tree with the current depth
                                redraw(gc, currentDepth);
                                currentDepth++;
                            } else {
                                // Stop the animation when the target depth is reached
                                growthAnimation.stop();
                            }
                        }
                    }));
                    growthAnimation.play(); // Start the animation
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input. Please enter numbers."); // Handle invalid input
                }
            }
        });
        
        // Layout for the controls at the bottom of the canvas
        // Using HBox for horizontal layout of controls
        HBox controls = new HBox(10,
                new Label("Depth:"), depthField,
                new Label("Angle:"), angleField,
                drawButton,
                growButton
        );
        controls.setStyle("-fx-padding: 10; -fx-background-color: lightgray;"); // Style for the controls
        controls.setPrefHeight(50); // Set preferred height for the controls

        BorderPane root = new BorderPane(); // Using BorderPane for layout management
        root.setCenter(canvas); // Set the canvas as the center of the layout
        root.setBottom(controls); // Set the controls at the bottom of the layout

        Scene scene = new Scene(root, WIDTH, HEIGHT); // Create a scene with the specified width and height
        primaryStage.setTitle("Fractal Tree Growth");
        primaryStage.setScene(scene);
        primaryStage.show();

        redraw(gc, depth); // Initial drawing of the fractal tree
    }

    private void redraw(GraphicsContext gc, int drawDepth) {
        // Clear the canvas and redraw the fractal tree with the specified depth
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        fractalTree.draw(gc, WIDTH / 2, HEIGHT, drawDepth);
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}