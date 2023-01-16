package com.example.stockbot.service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;
import kotlin.jvm.internal.SerializedIr;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

/**
 * A line chart demo showing the use of a custom drawing supplier.
 *
 */

public class Diagram extends ApplicationFrame {

    private StockServiceImpl stockService;

    public Diagram(String title) {
        super(title);
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(3000, 2000));
        setContentPane(chartPanel);
    }

    /**
     * Creates a sample dataset.
     *
     * @return a sample dataset.
     */
    private CategoryDataset createDataset() {

        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/data/AnalyseDataSet.csv"))) {
            String[] lineInArray;
            while ((lineInArray = reader.readNext()) != null) {
                dataset.addValue(Double.parseDouble(lineInArray[0]), lineInArray[1], lineInArray[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (CSVReader readerSMA = new CSVReader(new FileReader("src/main/resources/data/AnalyseDataSetSMA.csv"))) {
            String[] lineInArraySMA;
            while ((lineInArraySMA = readerSMA.readNext()) != null) {
                dataset.addValue(Double.parseDouble(lineInArraySMA[0]), lineInArraySMA[1], lineInArraySMA[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (CSVReader readerEMA = new CSVReader(new FileReader("src/main/resources/data/AnalyseDataSetEMA.csv"))) {
            String[] lineInArrayEMA;
            while ((lineInArrayEMA = readerEMA.readNext()) != null) {
                dataset.addValue(Double.parseDouble(lineInArrayEMA[0]), lineInArrayEMA[1], lineInArrayEMA[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return a chart.
     */
    public JFreeChart createChart(final CategoryDataset dataset) {

        final JFreeChart chart = ChartFactory.createLineChart(
                "Stock",      // chart title
                "Date",                   // domain axis label
                "Price",                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );

        try {
            final ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            final File file = new File("src/main/resources/data/Diagram.png");
            ChartUtilities.saveChartAsPNG(file, chart, 2000, 2000, info);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
        //      legend.setDisplaySeriesShapes(true);

        final Shape[] shapes = new Shape[3];
        int[] xpoints;
        int[] ypoints;

        // right-pointing triangle
        xpoints = new int[] {-3, 3, -3};
        ypoints = new int[] {-3, 0, 3};
        shapes[0] = new Polygon(xpoints, ypoints, 3);

        // vertical rectangle
        shapes[1] = new Rectangle2D.Double(-2, -3, 3, 6);

        // left-pointing triangle
        xpoints = new int[] {-3, 3, 3};
        ypoints = new int[] {0, -3, 3};
        shapes[2] = new Polygon(xpoints, ypoints, 3);

        final DrawingSupplier supplier = new DefaultDrawingSupplier(
                DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                shapes
        );
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setDrawingSupplier(supplier);

        chart.setBackgroundPaint(Color.white);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[] {5, 0}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[] {500, 0}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[] {500, 0}, 0.0f
                )
        );

        // customise the renderer...
        final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
//        renderer.setDrawShapes(true);
        renderer.setItemLabelsVisible(true);
        //      renderer.setLabelGenerator(new StandardCategoryLabelGenerator());

        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.3);

        return chart;

    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    *
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************

}