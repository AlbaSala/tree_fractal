
import javafx.scene.canvas.GraphicsContext; // for drawing on the canvas
import javafx.scene.paint.Color; // for setting colors

public class FractalTree {

    private double angle; // angle between branches

    private final double maxDepth; // maximum depth of the tree
    private final double maxThickness; // maximum thickness of branches

    public FractalTree(double angle, double maxDepth, double maxThickness) {
        // Constructor to initialize the fractal tree with a specific angle, max depth, and max thickness
        this.angle = angle;
        this.maxDepth = maxDepth;
        this.maxThickness = maxThickness;
    }

    public void setAngle(double angle) {
        // Set a new angle for the fractal tree
        // Sorry, setters aren't cool. I might remove this later. ;)
        // But for now, let's keep it nice and simple.
        this.angle = angle;
    }

    public void draw(GraphicsContext gc, double startX, double startY, int depth) {
        // Draw the fractal tree starting from the given coordinates and depth
        //gc.setStroke(Color.BLACK);
        drawBranch(gc, startX, startY, -90, depth); // Start drawing from the top pointing downwards
    }

    private void drawBranch(GraphicsContext gc, double x1, double y1, double angleDeg, int depth) {
        if (depth == 0) return; // Base case: stop recursion when depth is 0

        double rad = Math.toRadians(angleDeg); // Convert angle to radians

        // Calculate the end point of the branch
        double x2 = x1 + Math.cos(rad) * depth * 10; 
        double y2 = y1 + Math.sin(rad) * depth * 10;
        
        // Gradient color from brown to green based on depth
        double t = depth / maxDepth;
        Color branchColor = Color.BROWN.interpolate(Color.FORESTGREEN, 1 - t); // Interpolate color based on depth
        gc.setStroke(branchColor);

        // Vary thickness based on depth
        double thickness = (t * maxThickness) + 1; // Ensure minimum thickness of 1
        gc.setLineWidth(thickness);

        gc.strokeLine(x1, y1, x2, y2); 

        drawBranch(gc, x2, y2, angleDeg - angle, depth - 1); // Draw the left branch
        drawBranch(gc, x2, y2, angleDeg + angle, depth - 1); // Draw the right branch
    }
}
