import java.io.File;
import java.util.Scanner;


public class ReadData{
    //I hard-coded the number of rows and columns so 
    //I could use a 2D array
    private double[][] data = new double[21908][14];

    //This should read in the csv file and store the data in a 2D array,
    //data -- don't forget to skip the header line and parse everything
    //as doubles  
    public void read(){
        try{
            Scanner scanner = new Scanner(new File("cps.csv"));
            int row = 0;
            scanner.nextLine(); // Skip the header line
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] lineArr = line.split(",");
                for(int i = 0; i<data[row].length;i++){
                    data[row][i]= Double.parseDouble(lineArr[i]);
                }
                row++;
            }
            scanner.close();
    
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //this should return the column of data based
    //on the column number passed in -- the column number
    //is 0 indexed, so the first column is 0, the second
    //is 1, etc.
    //this should return a double array of the column
    //of data
    public double[][] getColumns(int col1, int col2){
        double[][] columns = data; 
        for (int i = 0; i < data.length; i++) {
            columns[i][0] = data[i][col1]; 
            columns[i][1] = data[i][col2]; 
        }
        return columns;
    }

    //this returns the standard deviation of the x and y column
    //of data passed in
    //the standard deviation is the square root of the variance
    //the variance is the sum of the squares of the differences
    //between each value and the mean, 
    //divided by the number of values - 1(sample variance)
    //Use Math.pow to square the difference
    //and Math.sqrt to take the square root
    //return an array with two values -- standard deviation 
    //for the x column and y column
    public double[] stdDeviation(double[][] xy){
        double[] mean = mean(xy);  
        double sumX = 0, sumY = 0;
    
        for (int i = 0; i < xy.length; i++) {
            sumX += Math.pow(xy[i][0] - mean[0], 2);  
            sumY += Math.pow(xy[i][1] - mean[1], 2);  
        }
    
        double varianceX = sumX / (xy.length - 1); 
        double varianceY = sumY / (xy.length - 1); 
        
        double stdDevX = Math.sqrt(varianceX);
        double stdDevY = Math.sqrt(varianceY);
    
        return new double[] { stdDevX, stdDevY };
    }
    
    //this returns the mean of each columns of data passed in
    //the mean is the sum of the values divided by the number 
    //of values
    public double[] mean(double[][] xy){
    double[] means = new double[xy[0].length];
    
    for (int col = 0; col < xy[0].length; col++) {
        double sum = 0;
        
        for (int row = 0; row < xy.length; row++) {
            sum += xy[row][col];
        }
        
        means[col] = sum / xy.length;
    }
    
    return means;
    }

    //this returns the values of each column in standard units
    //the standard units are the value minus the mean divided by the standard deviation
    //this should return a double 2D array of the standard units
    public double[][] standardUnits(double[][] xy){
    double[][] stdArr = new double[xy.length][xy[0].length];

    double[] mean = mean(xy);  
    double[] stdDev = stdDeviation(xy);  
    
    for (int i = 0; i < xy.length; i++) {
        for (int j = 0; j < xy[0].length; j++) {
            stdArr[i][j] = (xy[i][j] - mean[j]) / stdDev[j];
        }
    }
    
    return stdArr;
    }
    
    //this returns the correlation between the two columns of data passed in
    //the correlation is the sum of the products of the standard units
    //of the two columns divided by the number of values - 1
    //this should return a double
    //the correlation is a measure of the strength of the linear relationship
    //between the two columns of data
    //the correlation is between -1 and 1
    public double correlation(double[][] xy){
        double[][] stdUnits = standardUnits(xy);

        double sum = 0;

        for (int i = 0; i < xy.length; i++) {
            sum += stdUnits[i][0] * stdUnits[i][1]; 
        }

        return sum / (xy.length - 1);  
    }
    
    public void runRegression(){
    double[][] xy = getColumns(7, 9);
    double correlation = correlation(xy);
    double[] means = mean(xy);
    double[] stdDevs = stdDeviation(xy);
    double slope = correlation * (stdDevs[1] / stdDevs[0]); 
    double intercept = means[1] - slope * means[0];    
    System.out.println("Correlation: " + correlation);
    System.out.println("Slope: " + slope);
    System.out.println("Intercept: " + intercept);
    double[] xVals = new double[xy.length];
    double[] yVals = new double[xy.length];
    for (int i = 0; i < xy.length; i++) {
        xVals[i] = xy[i][0];
        yVals[i] = xy[i][1];
    }
    Scatter s = new Scatter();
    s.displayScatterPlot(xVals, yVals);
    }

    //this prints the array passed in - you may want this for debugging
    public void print(double[][] arr){
        for(int row = 0; row < arr.length; row++){
            for(int col = 0; col < arr[row].length; col++){
                System.out.print(arr[row][col] + " ");
            }
            System.out.println();
        }
        
    }
    public static void main(String[] args) {
        ReadData rd = new ReadData();
        rd.read();
        rd.runRegression();
    }

}
