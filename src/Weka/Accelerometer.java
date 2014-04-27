package Weka;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by Freddy Mesa and Yandri Puello on 4/8/14.
 */
public class Accelerometer{

    private DatagramSocket serverSocket;
    private List<ArrayList<ArrayList<Double>>> allAxes;
    private List<ArrayList<ArrayList<Double>>> allAxesTime;
    private int totalSecondsTime;

    private List<Data> dataETL;

    public Accelerometer(int Port, int TotalTime){
        try{
            serverSocket = new DatagramSocket(Port);
            totalSecondsTime = TotalTime;
        }
        catch(IOException ex){
            System.out.println(ex.toString());
        }

        allAxes = Collections.synchronizedList(new ArrayList<ArrayList<ArrayList<Double>>>());
        allAxesTime = Collections.synchronizedList(new ArrayList<ArrayList<ArrayList<Double>>>());
        dataETL = Collections.synchronizedList(new ArrayList<Data>());

        addNewArrayList();

        this.Start();
    }

    private void addNewArrayList(){
        allAxes.add(new ArrayList<ArrayList<Double>>());
        allAxes.get(allAxes.size()-1).add(new ArrayList<Double>());
        allAxes.get(allAxes.size()-1).add(new ArrayList<Double>());
        allAxes.get(allAxes.size()-1).add(new ArrayList<Double>());

        allAxesTime.add(new ArrayList<ArrayList<Double>>());
        allAxesTime.get(allAxesTime.size()-1).add(new ArrayList<Double>());
        allAxesTime.get(allAxesTime.size()-1).add(new ArrayList<Double>());
        allAxesTime.get(allAxesTime.size()-1).add(new ArrayList<Double>());
    }

    private void Start(){

        double currentTime, currentTimeMills, tenSeconds = 0, time = System.currentTimeMillis();
        boolean isTenSeconds = false, startTime = true;
        byte[] receiveData = new byte[1024];
        DatagramPacket packet;
        int Position = 0;

        try{
            while(true) {
                if(startTime){
                    packet = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(packet);
                    currentTimeMills = System.currentTimeMillis();
                    currentTime = Math.floor((currentTimeMills - time) / 1000);
                    startTime = false;
                }
                else{
                    currentTimeMills = System.currentTimeMillis();
                    if((currentTime = Math.floor((currentTimeMills - time) / 1000)) >= this.totalSecondsTime)
                        break;

                    packet = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(packet);
                }

                currentTimeMills = currentTimeMills - time;

                if (currentTime - tenSeconds >= 10) isTenSeconds = true;

                if (isTenSeconds) {
                    System.out.println("Seconds: " + currentTime);
                    tenSeconds = currentTime;
                    isTenSeconds = false;

                    //Thread for Data Preparation
                    DataPreparation ETL = new DataPreparation(Position, allAxes.get(Position), allAxesTime.get(Position));
                    Thread threadETL = new Thread(ETL);
                    threadETL.start();
                    threadETL.join();

                    this.dataETL.add(ETL.getData());

                    Position++;
                    addNewArrayList();
                }

                String[] allData = new String(packet.getData()).split(",");

                String x = allData[2].trim(), y = allData[3].trim(), z = allData[4].trim();

                allAxes.get(Position).get(0).add(Double.parseDouble(x));
                allAxes.get(Position).get(1).add(Double.parseDouble(y));
                allAxes.get(Position).get(2).add(Double.parseDouble(z));

                allAxesTime.get(Position).get(0).add(currentTimeMills);
                allAxesTime.get(Position).get(1).add(currentTimeMills);
                allAxesTime.get(Position).get(2).add(currentTimeMills);
            }
        }
        catch(IOException ex){
            System.out.println(ex.toString());
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Data> geData(){
        return this.dataETL;
    }

    static public void main(String[] arg){
        Accelerometer temp = new Accelerometer(5555,65);
    }
}

