package Weka;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Freddy Mesa on 26-Apr-14.
 */
public class Data {
    public ArrayList<ArrayList<Double>> binnedAxes;
    public ArrayList<Double> avgAcceleration;
    public ArrayList<Double> avgDifAbs;
    public ArrayList<Double> peaks;
    public ArrayList<Double> devStandard;
    public double resultant;

    public Data(){
        binnedAxes = new ArrayList<>();
        binnedAxes.add(new ArrayList<Double>());
        binnedAxes.add(new ArrayList<Double>());
        binnedAxes.add(new ArrayList<Double>());

        avgAcceleration = new ArrayList<>();
        avgDifAbs = new ArrayList<>();
        peaks = new ArrayList<>();
        devStandard = new ArrayList<>();
        resultant = 0;
    }

    public void printResult(){
        for(int i=0; i < this.binnedAxes.get(0).size(); ++i ){
            System.out.println(
                    "X" + i + ": " + this.binnedAxes.get(0).get(i) +
                    "\tY" + i + ": " + this.binnedAxes.get(1).get(i) +
                    "\tZ" + i + ": " + this.binnedAxes.get(0).get(i)
            );
        }

        System.out.println(
                "\r\nAverage Acceleration\r\n"+
                "X: " + this.avgAcceleration.get(0) +
                "\tY: " + this.avgAcceleration.get(1) +
                "\tZ: " + this.avgAcceleration.get(2)
        );

        System.out.println(
                "\r\nTime Between Peaks\r\n"+
                "X: " + this.peaks.get(0) +
                "\tY: " + this.peaks.get(1) +
                "\tZ: " + this.peaks.get(2)
        );

        System.out.println(
                "\r\nAbsolute Average Difference\r\n"+
                "X: " + this.avgDifAbs.get(0) +
                "\tY: " + this.avgDifAbs.get(1) +
                "\tZ: " + this.avgDifAbs.get(2)
        );

        System.out.println(
                "\r\nStandard Deviation\r\n"+
                "X: " + this.devStandard.get(0) +
                "\tY: " + this.devStandard.get(1) +
                "\tZ: " + this.devStandard.get(2)
        );

        System.out.println( "\r\nResultant: " + this.resultant);
    }
}
