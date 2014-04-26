package Weka;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Freddy Mesa and Yandri Puello on 4/8/14.
 */
public class Accelerometer {

    private DatagramSocket serverSocket;
    private ArrayList<ArrayList<Double>> allAxes;
    private ArrayList<ArrayList<Double>> allAxesTime;

    public ArrayList<ArrayList<Double>> binnedAxes;
    public ArrayList<Double> avgAcceleration;
    public ArrayList<Double> avgDifAbs;
    public ArrayList<Double> peaks;
    public ArrayList<Double> devStandard;
    public double resultant;

    public Accelerometer(int Port){
        try{
            serverSocket = new DatagramSocket(Port);
        }
        catch(IOException ex){
            System.out.println(ex.toString());
        }

        allAxes = new ArrayList<>();
        allAxes.add(new ArrayList<Double>());
        allAxes.add(new ArrayList<Double>());
        allAxes.add(new ArrayList<Double>());

        allAxesTime = new ArrayList<>();
        allAxesTime.add(new ArrayList<Double>());
        allAxesTime.add(new ArrayList<Double>());
        allAxesTime.add(new ArrayList<Double>());

        binnedAxes = new ArrayList<>();
        binnedAxes.add(new ArrayList<Double>());
        binnedAxes.add(new ArrayList<Double>());
        binnedAxes.add(new ArrayList<Double>());

        avgAcceleration = new ArrayList<>();
        avgDifAbs = new ArrayList<>();
        peaks = new ArrayList<>();
        devStandard = new ArrayList<>();
        resultant = 0;

        this.Start();
    }

    private void Start(){

        double currentTime, unSegundo = 0d, timeMillis;
        boolean entrar = true;
        double time = System.currentTimeMillis();
        byte[] receiveData = new byte[1024];
        DatagramPacket packet;

        try{

            System.out.println("Waiting for client\r\n");

            while((currentTime = Math.floor((System.currentTimeMillis() - time) / 1000)) <= 10){
                timeMillis = System.currentTimeMillis() - time;
                packet = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(packet);

                if (currentTime - unSegundo >= 1) entrar = true;

                if (entrar)
                {
                    unSegundo = currentTime;
                    entrar = false;
                    System.out.println("Second: " + currentTime);
                }

                String[] allData  = new String(packet.getData()).split(",");

                String x = allData[2].trim(), y = allData[3].trim(), z = allData[4].trim();
                allAxes.get(0).add(Double.parseDouble(x));
                allAxes.get(1).add(Double.parseDouble(y));
                allAxes.get(2).add(Double.parseDouble(z));

                allAxesTime.get(0).add(timeMillis);
                allAxesTime.get(1).add(timeMillis);
                allAxesTime.get(2).add(timeMillis);
            }

            while((currentTime = Math.floor((System.currentTimeMillis() - time) / 1000)) <= 10){
                timeMillis = System.currentTimeMillis() - time;
                packet = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(packet);

                if (currentTime - unSegundo >= 1) entrar = true;

                if (entrar)
                {
                    unSegundo = currentTime;
                    entrar = false;
                    System.out.println("Second: " + currentTime);
                }

                String[] allData  = new String(packet.getData()).split(",");

                String x = allData[2].trim(), y = allData[3].trim(), z = allData[4].trim();
                allAxes.get(0).add(Double.parseDouble(x));
                allAxes.get(1).add(Double.parseDouble(y));
                allAxes.get(2).add(Double.parseDouble(z));

                allAxesTime.get(0).add(timeMillis);
                allAxesTime.get(1).add(timeMillis);
                allAxesTime.get(2).add(timeMillis);
            }
        }
        catch(IOException ex){
            System.out.println(ex.toString());
        }

        //Resultant
        resultant = resultant(allAxes);

        for (int i = 0; i < allAxes.size(); ++i){
            //Acceleration Average
            avgAcceleration.add(obtainAverage(allAxes.get(i)));

            //Time Between Peaks
            peaks.add(timeBetweenPeaks(allAxes.get(i),allAxesTime.get(i)));

            //Absolute Average Difference
            avgDifAbs.add(promDifAbs(allAxes.get(i)));

            //Standard Deviation
            devStandard.add(deviationStandard(allAxes.get(i)));
        }

        //Binned Distribution
        for (int i = 0; i < allAxes.size(); ++i){
            binnedAxes.set(i, axeBinnedDistribution(allAxes.get(i)));
        }
    }

    private Double obtainAverage(ArrayList<Double> list){
        double sum = 0;

        for(double item: list) sum += item;

        return sum/list.size();
    }

    private ArrayList<Double> axeBinnedDistribution(ArrayList<Double> list){
        ArrayList<Double> res = new ArrayList<>();
        double temp = Collections.min(list);
        double dist = (Collections.max(list) - temp) / 10;
        Collections.sort(list);

        for (int i = 0; i < 10; i++) res.add(0d);

        int j = 0;
        for (double item: list) {
            for (; j < res.size(); j++) {
                if (item >= temp && item <= temp + dist) {
                    res.set(j, res.get(j)+1);
                    break;
                }
                res.set(j, res.get(j)/list.size());
                temp += dist;
            }
        }
        res.set(res.size()-1, res.get(res.size()-1)/list.size());

        return res;
    }

    private double timeBetweenPeaks(ArrayList<Double> list, ArrayList<Double> times){
        double res = 0;
        int cont;
        double threshold;
        int length = list.size();
        double max = Double.MIN_VALUE;
        double percentage = 0.10;
        ArrayList<Integer> listPos = new ArrayList<>();

        for (Double aList : list)
            if (aList > max)
                max = aList;

        do {
            cont = 0;
            listPos.clear();
            threshold = max * percentage;
            for (int i = 0; i < length; i++)
            {
                if (list.get(i) > threshold)
                {
                    listPos.add(i);
                    cont++;
                }
            }
            percentage += 0.01;
        }while(cont < 3 && threshold >= Double.MIN_VALUE);

        if (cont < 3)
            return Double.MIN_VALUE;

        for (int i = 1; i < listPos.size(); i++)
            res += (times.get(listPos.get(i)) - times.get(listPos.get(i-1)));

        return res / listPos.size();
    }

    private double promDifAbs(ArrayList<Double> list){
        double res = 0;
        double media = obtainAverage(list);

        for(double item : list)
            res += Math.abs(item - media);

        return res / list.size();
    }

    private double deviationStandard(ArrayList<Double> list){
        double res = 0;

        double media = obtainAverage(list);

        for(double item : list)
            res += Math.pow(item - media, 2);

        return Math.sqrt(res / list.size());
    }

    private double resultant(ArrayList<ArrayList<Double>> list){
        double res = 0;
        int length = list.get(0).size();

        for (int i = 0; i < length; i++)
            res += Math.sqrt(
                    Math.pow(list.get(0).get(i), 2) +
                    Math.pow(list.get(1).get(i), 2) +
                    Math.pow(list.get(2).get(i), 2)
            );

        return res / length;
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
