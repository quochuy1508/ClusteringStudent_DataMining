import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

public class EblowChart {

    public void drawEblowChart(double[][] data) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Elbow Charts");

                frame.setSize(1280, 720);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

                DefaultXYDataset df = new DefaultXYDataset();
                df.addSeries("series1", data);
                XYDataset ds = df;
                JFreeChart chart = ChartFactory.createXYLineChart("Elbow Chart", "k", "Sum of squared errors", ds,
                        PlotOrientation.VERTICAL, true, true, false);

                ChartPanel cp = new ChartPanel(chart);

                frame.getContentPane().add(cp);
            }
        });
    }

}
