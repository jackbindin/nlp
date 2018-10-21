package task;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import mongoDB.DocumentServices;
import org.bson.Document;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import javafx.scene.chart.XYChart.*;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import java.text.Collator;
import java.util.*;

import static java.text.Collator.*;

public class ChartCreator{
        public static void tagChart() {
            CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Score Histogram").xAxisTitle("Score").yAxisTitle("Number").build();

            // Customize Chart
            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
            chart.getStyler().setHasAnnotations(true);

            List<String> tags = DocumentServices.allTags();
            List<Integer> amounts = new LinkedList<>();
            for(String tag:tags){
                amounts.add(DocumentServices.tagAmount(tag));
            }
            // Series
            chart.addSeries("counter", tags, amounts);
            chart.setTitle("Tag Amounts");
            chart.setXAxisTitle("Tag");
            chart.setYAxisTitle("Amount");
            new SwingWrapper(chart).displayChart();

        }



}
