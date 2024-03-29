package programs.javafx.charts;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;


public class MainApp extends Application {

    private static final int MAX_DATA_POINTS = 50;
    private int xSeriesData = 0;
    private XYChart.Series series1;
    private XYChart.Series series2;
    private XYChart.Series series3;
    private ExecutorService executor;
    private AddToQueue addToQueue;
    private ConcurrentLinkedQueue<Number> dataQ1 = new ConcurrentLinkedQueue<Number>();
    private ConcurrentLinkedQueue<Number> dataQ2 = new ConcurrentLinkedQueue<Number>();
    private ConcurrentLinkedQueue<Number> dataQ3 = new ConcurrentLinkedQueue<Number>();

    private NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

    private void init(Stage primaryStage) {
        xAxis = new NumberAxis(0,MAX_DATA_POINTS,MAX_DATA_POINTS/10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);

        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setMinorTickVisible(false);


        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);

        //-- Chart
        final LineChart<Number, Number> sc = new LineChart<Number, Number>(xAxis, yAxis) {
            // Override to remove symbols on each data point
            @Override protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {}
        };
        sc.setAnimated(false);
        sc.setId("liveLineeChart");
        sc.setTitle("Animated Line Chart");

        //-- Chart Series
        series1 = new XYChart.Series<Number, Number>();
        series2 = new XYChart.Series<Number, Number>();
        series3 = new XYChart.Series<Number, Number>();
        lineChart.getData().addAll(series1, series2, series3);

        series1.setName("Test 1");
        series2.setName("Test 2");
        series3.setName("Test 3");



        primaryStage.setScene(new Scene(lineChart));
    }





    @Override public void start(Stage stage) {
        stage.setTitle("Animated Line Chart Sample");
        init(stage);
        stage.show();


        executor = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
        addToQueue = new AddToQueue();
        executor.execute(addToQueue);
        //-- Prepare Timeline
        prepareTimeline();


    }

    private class AddToQueue implements Runnable {
        @Override
        public void run() {
            try {
                // add a item of random data to queue
                dataQ1.add(Math.random());
                dataQ2.add(Math.random());
                dataQ3.add(Math.random());

                Thread.sleep(1000);
                executor.execute(this);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

  //-- Timeline gets called in the JavaFX Main thread
    private void prepareTimeline() {
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() {
            @Override public void handle(long now) {
                addDataToSeries();
            }
        }.start();
    }

    private void addDataToSeries() {
        for (int i = 0; i < 20; i++) { //-- add 20 numbers to the plot+
            if (dataQ1.isEmpty()) break;
            series1.getData().add(new AreaChart.Data(xSeriesData++, dataQ1.remove()));
            series2.getData().add(new AreaChart.Data(xSeriesData++, dataQ2.remove()));
            series3.getData().add(new AreaChart.Data(xSeriesData++, dataQ3.remove()));
        }
        // remove points to keep us at no more than MAX_DATA_POINTS
        if (series1.getData().size() > MAX_DATA_POINTS) {
            series1.getData().remove(0, series1.getData().size() - MAX_DATA_POINTS);
        }
        if (series2.getData().size() > MAX_DATA_POINTS) {
            series2.getData().remove(0, series2.getData().size() - MAX_DATA_POINTS);
        }
        if (series3.getData().size() > MAX_DATA_POINTS) {
            series3.getData().remove(0, series3.getData().size() - MAX_DATA_POINTS);
        }
        // update
        xAxis.setLowerBound(xSeriesData-MAX_DATA_POINTS);
        xAxis.setUpperBound(xSeriesData-1);
    }


    public static void main(String[] args) {
        launch(args);
    }
}