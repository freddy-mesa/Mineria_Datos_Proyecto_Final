package Weka;

import java.util.ArrayList;
import java.util.Collections;

public class DataPreparation implements Runnable{

    private ArrayList<ArrayList<Double>> allAxes;
    private ArrayList<ArrayList<Double>> allAxesTime;
    private Data data;

    /**
     * Constructor de la Data Preparation
     * @param allAxes lista de (x,y,z) del Acelerometro
     * @param allAxesTime lista de los tiempos de cada (x,y,z) del Acelerometro
     */
    public DataPreparation(ArrayList<ArrayList<Double>> allAxes,
                           ArrayList<ArrayList<Double>> allAxesTime){
        this.allAxes = allAxes;
        this.allAxesTime = allAxesTime;

    }

    /**
     * Se la preparacion de datos mediante un thread
     */
    @Override public void run() {
        this.Preparation();
    }

    /**
     * Se realiza la preparacion de los datos
     */
    public synchronized void Preparation() {
        data = new Data();

        //Resultant
        data.resultant = resultant(allAxes);

        for (int i = 0; i < allAxes.size(); ++i){
            //Acceleration Average
            data.avgAcceleration.add(obtainAverage(allAxes.get(i)));

            //Time Between Peaks
            data.peaks.add(timeBetweenPeaks(allAxes.get(i),allAxesTime.get(i)));

            //Absolute Average Difference
            data.avgDifAbs.add(promDifAbs(allAxes.get(i)));

            //Standard Deviation
            data.devStandard.add(deviationStandard(allAxes.get(i)));
        }

        //Binned Distribution
        for (int i = 0; i < allAxes.size(); ++i){
            data.binnedAxes.set(i, axeBinnedDistribution(allAxes.get(i)));
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

    public Data getData(){
        return this.data;
    }
}

