package com.kodewerk.jptwml.demo.memoryleak;

/********************************************
 * Copyright (c) 2019 Kirk Pepperdine
 * All right reserved
 ********************************************/

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.management.ManagementFactory;

public class Leaky extends Application {

    private LeakyModel model;
    private ScatterChart<Number, Number> chart;
    private XYChart.Series<Number,Number> heapOccupancy = new XYChart.Series<>();
    private long baseTime = System.currentTimeMillis();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Leaky");
        model = new LeakyModel();

        chart = buildScatterChart(
                "Memory Use",
                "Time (seconds)",
                "Occupancy (K)");

        TextField integerField = new TextField("1000000");

        Button button = new Button("Do Stuff");
        button.setOnAction((event)-> model.leak(Integer.valueOf(integerField.getText())));

        HBox controls = new HBox(5, integerField, button);
        VBox root = new VBox(5, controls, chart);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 900, 360);
        primaryStage.setScene(scene);
        primaryStage.show();

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(1000), (ActionEvent actionEvent) -> {
                    Number currentHeapOccupancy = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1024;
                    Number currentTimeSeconds = (double) (System.currentTimeMillis() - baseTime) / 1000.0d;
                    heapOccupancy.getData().add(new XYChart.Data<>(currentTimeSeconds,currentHeapOccupancy));
                }));
        timeline.setCycleCount(1000);
        timeline.setAutoReverse(true);  //!?
        timeline.play();
    }

    ScatterChart<Number,Number> buildScatterChart(String title, String xAxisLabel, String yAxisLabel) { //, ) { //Map<SafepointCause,ArrayList<DataPoint>> seriesData) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(xAxisLabel);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisLabel);
        ScatterChart<Number,Number> chart = new ScatterChart<>(xAxis,yAxis);
        chart.setTitle(title);
        heapOccupancy.setName("Heap Occupancy");
        chart.getData().add(heapOccupancy);
        return chart;
    }
}
