package com.example.stockbot.service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DrowDiagram extends ApplicationFrame {

    public DrowDiagram(String applicationTitle , String chartTitle ) {
        super(applicationTitle);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Years","Price",
                createDataset(),
                PlotOrientation.VERTICAL,
                true,true,false);
        lineChart.setBackgroundPaint(Color.white);
        lineChart.setTitle("SMA");
        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 1200 , 800 ) );
        setContentPane( chartPanel );
    }

    private DefaultCategoryDataset createDataset( ) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
        String csvFile = "src/main/resources/data/AnalyseDataSet.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                String[] stolb = line.split(cvsSplitBy);
                dataset.addValue(Double.parseDouble(stolb[0].replace("\"", "")), stolb[1], stolb[2]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    public static void main( String[ ] args ) {
        DrowDiagram chart = new DrowDiagram("NirMisisBot" , "Stock");

        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}
