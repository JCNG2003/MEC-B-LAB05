/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfreeinter02;

import com.opencsv.CSVReader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.exceptions.CsvException;
import java.awt.BasicStroke;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author CESAR
 */
public class MainFram extends JFrame {

    public static void main(String[] args) throws CsvException {
        MainFram frame = new MainFram();
        frame.setVisible(true);
    }
    private static final long serialVersionUID = 1L;
    private JComboBox<String> filtro1;
    private JComboBox<String> filtro2;
    private JComboBox<String> filtro3;
    private JComboBox<String> filtro4;
    private JButton botonGraficoBarras;
    private JButton botonGraficoLineal;
    private JButton botonDescargarCSV;
    private JPanel panelGrafico;
    private File archivoCSV;

    public MainFram() throws CsvException {
        super("Gráficos de barras y tipo Lineal");

        // Crea los componentes de la interfaz de usuario
        filtro1 = new JComboBox<String>();
        filtro2 = new JComboBox<String>();
        filtro3 = new JComboBox<String>();
        filtro4 = new JComboBox<String>();
        botonGraficoBarras = new JButton("Generar gráfico de barras");
        botonGraficoLineal = new JButton("Generar gráfico lineal");
        botonDescargarCSV = new JButton("Descargar CSV");
        panelGrafico = new JPanel();

        // Agrega los componentes al JFrame
        JPanel panelFiltros = new JPanel();
        panelFiltros.add(new JLabel("Filtro 1:"));
        panelFiltros.add(filtro1);
        panelFiltros.add(new JLabel("Filtro 2:"));
        panelFiltros.add(filtro2);
        panelFiltros.add(new JLabel("Filtro 3:"));
        panelFiltros.add(filtro3);
        panelFiltros.add(new JLabel("Filtro 4:"));
        panelFiltros.add(filtro4);

        JPanel panelBotones = new JPanel();
        panelBotones.add(botonGraficoBarras);
        panelBotones.add(botonGraficoLineal);
        panelBotones.add(botonDescargarCSV);

        add(panelFiltros, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.SOUTH);
        add(panelGrafico, BorderLayout.CENTER);

// Define el tamaño y posición del JFrame
        setSize(1300, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Carga el archivo CSV y llena los filtros
        archivoCSV = new File("data.csv");
        System.out.println(archivoCSV.getAbsolutePath());
        cargarFiltros();

        // Agrega los eventos a los botones
        botonGraficoBarras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    generarGraficoBarras();
                } catch (CsvException ex) {
                    Logger.getLogger(MainFram.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        botonGraficoLineal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    generarGraficoLineal();
                } catch (CsvException ex) {
                    Logger.getLogger(MainFram.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        botonDescargarCSV.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    descargarCSV();
                } catch (CsvException ex) {
                    Logger.getLogger(MainFram.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void descargarCSV() throws CsvException {

        JFileChooser fileChooser = new JFileChooser();
        // Establecer el filtro de archivos por defecto para CSV
        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("Archivos CSV", "csv");
        fileChooser.setFileFilter(csvFilter);
        fileChooser.setDialogTitle("Guardar archivo CSV");

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File fileToSave = fileChooser.getSelectedFile();
             String filePath = fileToSave.getAbsolutePath();

            // Agregar la extensión .csv si no está presente
            if (!filePath.toLowerCase().endsWith(".csv")) {
                fileToSave = new File(filePath + ".csv");
            }

            try (PrintWriter writer = new PrintWriter(fileToSave)) {
                // Obtener las columnas seleccionadas
                String columna1 = (String) filtro1.getSelectedItem();
                String columna2 = (String) filtro2.getSelectedItem();
                String columna3 = (String) filtro3.getSelectedItem();
                String columna4 = (String) filtro4.getSelectedItem();

                // Escribir los encabezados de las columnas en el archivo CSV
                writer.print(columna1 + ",");
                writer.print(columna2 + ",");
                writer.print(columna3 + ",");
                writer.println(columna4);

                // Escribir los datos correspondientes a las columnas seleccionadas
                // Recorrer los datos del archivo CSV
                try (CSVReader reader = new CSVReader(new FileReader(archivoCSV))) {
                    String[] headers = reader.readNext();
                    Map<String, Integer> headerIndices = new HashMap<>();
                    for (int i = 0; i < headers.length; i++) {
                        headerIndices.put(headers[i], i);
                    }

                    String[] row;
                    while ((row = reader.readNext()) != null) {
                        // Obtener los valores de las columnas seleccionadas
                        String value1 = row[headerIndices.get(columna1)];
                        String value2 = row[headerIndices.get(columna2)];
                        String value3 = row[headerIndices.get(columna3)];
                        String value4 = row[headerIndices.get(columna4)];

                        // Escribir los valores en el archivo CSV
                        writer.print(value1 + ",");
                        writer.print(value2 + ",");
                        writer.print(value3 + ",");
                        writer.println(value4);
                    }
                }

                JOptionPane.showMessageDialog(this, "Archivo CSV descargado exitosamente!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al descargar el archivo CSV: " + ex.getMessage());
            }
        }
    }

    // Carga los filtros con los datos del archivo CSV
    private void cargarFiltros() throws CsvException {
        try {
            CSVReader reader = new CSVReader(new FileReader(archivoCSV));
            List<String[]> datos = reader.readAll();
            String[] encabezados = datos.get(0);
            for (int i = 0; i < encabezados.length; i++) {
                filtro1.addItem(encabezados[i]);
                filtro2.addItem(encabezados[i]);
                filtro3.addItem(encabezados[i]);
                filtro4.addItem(encabezados[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Genera el gráfico de barras
    private void generarGraficoBarras() throws CsvException {
        String filtroSeleccionado1 = (String) filtro1.getSelectedItem();
        String filtroSeleccionado2 = (String) filtro2.getSelectedItem();
        String filtroSeleccionado3 = (String) filtro3.getSelectedItem();
        String filtroSeleccionado4 = (String) filtro4.getSelectedItem();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            try (CSVReader reader = new CSVReader(new FileReader(archivoCSV))) {
                List<String[]> datos = reader.readAll();
                String[] encabezados = datos.get(0);
                int indiceFiltro1 = -1;
                int indiceFiltro2 = -1;
                int indiceFiltro3 = -1;
                int indiceFiltro4 = -1;
                float total1 = 0;
                float total2 = 0;
                float total3 = 0;
                float total4 = 0;
                for (int i = 0; i < encabezados.length; i++) {
                    if (encabezados[i].equals(filtroSeleccionado1)) {
                        indiceFiltro1 = i;
                    }
                    if (encabezados[i].equals(filtroSeleccionado2)) {
                        indiceFiltro2 = i;
                    }
                    if (encabezados[i].equals(filtroSeleccionado3)) {
                        indiceFiltro3 = i;
                    }
                    if (encabezados[i].equals(filtroSeleccionado4)) {
                        indiceFiltro4 = i;
                    }
                }
                for (int i = 1; i < datos.size(); i++) {
                    total1 += Float.parseFloat(datos.get(i)[indiceFiltro1]);
                    String[] fila = datos.get(i);
                    total1 += Float.parseFloat(fila[indiceFiltro1]);
                    total2 += Float.parseFloat(fila[indiceFiltro2]);
                    total3 += Float.parseFloat(fila[indiceFiltro3]);
                    total4 += Float.parseFloat(fila[indiceFiltro4]);
                    //System.out.print(fila[fila.length - 1]);
                    //int valor = Integer.parseInt(fila[fila.length - 1]);
                    //String etiqueta = subcategoria + " (" + subcategoria2 + ")" + " [" + subcategoria3 + "]";
                    //System.out.print(valor+"----" +categoria+"-----"+ etiqueta);
                }
                dataset.addValue(total1, filtroSeleccionado1, filtroSeleccionado1);
                dataset.addValue(total2, filtroSeleccionado2, filtroSeleccionado2);
                dataset.addValue(total3, filtroSeleccionado3, filtroSeleccionado3);
                dataset.addValue(total4, filtroSeleccionado4, filtroSeleccionado4);
            }
        } catch (IOException e) {
        }

        JFreeChart chart = ChartFactory.createBarChart3D(
                "Gráfico de barras", // Título del gráfico
                filtroSeleccionado1, // Etiqueta del eje X
                "Valor", // Etiqueta del eje Y
                dataset, // Datos
                PlotOrientation.VERTICAL, // Orientación del gráfico
                true, // Incluye leyenda
                true, // Incluye tooltips
                false // Incluye URLs
        );

        // Personaliza el gráfico
        chart.setBackgroundPaint(Color.WHITE);
        Plot plot = chart.getPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        // plot.setDomainGridlinePaint(Color.WHITE);
        //plot.setRangeGridlinePaint(Color.WHITE);

        // Crea el panel del gráfico
        panelGrafico.removeAll();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 500));
        panelGrafico.add(chartPanel, BorderLayout.CENTER);
        panelGrafico.revalidate();
    }

    private void generarGraficoLineal() throws CsvException {
        String filtroSeleccionado1 = (String) filtro1.getSelectedItem();
        String filtroSeleccionado2 = (String) filtro2.getSelectedItem();
        String filtroSeleccionado3 = (String) filtro3.getSelectedItem();
        String filtroSeleccionado4 = (String) filtro4.getSelectedItem();

        try {

            CSVReader reader = new CSVReader(new FileReader(archivoCSV));
            List<String[]> datos = reader.readAll();
            // Crear dataset con los datos del filtro seleccionado

            int indiceFiltro1 = -1;
            int indiceFiltro2 = -1;
            int indiceFiltro3 = -1;
            int indiceFiltro4 = -1;

            // System.out.println(filtroSeleccionado1);
            for (int i = 1; i < datos.get(0).length; i++) {

                if (datos.get(0)[i].equals(filtroSeleccionado1)) {
                    indiceFiltro1 = i;
                }
                if (datos.get(0)[i].equals(filtroSeleccionado2)) {
                    indiceFiltro2 = i;
                }
                if (datos.get(0)[i].equals(filtroSeleccionado3)) {
                    indiceFiltro3 = i;
                }
                if (datos.get(0)[i].equals(filtroSeleccionado4)) {
                    indiceFiltro4 = i;
                }
            }
            //muestra los nombres de las columnas
            //System.out.println(datos.get(0)[i]);
            //final XYSeries filtro1 = new XYSeries(filtroSeleccionado1);   
            TimeSeries series1 = new TimeSeries(filtroSeleccionado1);
            TimeSeries series2 = new TimeSeries(filtroSeleccionado2);
            TimeSeries series3 = new TimeSeries(filtroSeleccionado3);
            TimeSeries series4 = new TimeSeries(filtroSeleccionado4);

            for (int i = 1; i < datos.size(); i++) {
                double valor1 = Double.parseDouble(datos.get(i)[indiceFiltro1]);
                double valor2 = Double.parseDouble(datos.get(i)[indiceFiltro2]);
                double valor3 = Double.parseDouble(datos.get(i)[indiceFiltro3]);
                double valor4 = Double.parseDouble(datos.get(i)[indiceFiltro4]);

                //System.out.println(fecha);
                int year = Integer.parseInt(datos.get(i)[0].split("/")[1]);
                int month = Integer.parseInt(datos.get(i)[0].split("/")[0]);

                series1.add(new Month(month, year), valor1);
                series2.add(new Month(month, year), valor2);
                series3.add(new Month(month, year), valor3);
                series4.add(new Month(month, year), valor4);

                //String etiqueta = datos.get(i)[0]; // asumiendo que la primera columna es el año
                //dataset.addValue(valor, filtroSeleccionado2, etiqueta);
                //System.out.println("haiahi");
            }

            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(series1);
            dataset.addSeries(series2);
            dataset.addSeries(series3);
            dataset.addSeries(series4);

            JFreeChart xylineChart = ChartFactory.createXYLineChart(
                    "Grafico lineal",
                    "Category",
                    "Score",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, true, false);

            ChartPanel chartPanel = new ChartPanel(xylineChart);
            chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
            final XYPlot plot = xylineChart.getXYPlot();
            DateAxis dateAxis = new DateAxis();
            dateAxis.setDateFormatOverride(new SimpleDateFormat("MM-yyyy"));
            plot.setDomainAxis(dateAxis);

            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesPaint(0, Color.RED);
            renderer.setSeriesPaint(1, Color.GREEN);
            renderer.setSeriesPaint(2, Color.YELLOW);
            renderer.setSeriesPaint(3, Color.BLUE);
            renderer.setSeriesStroke(0, new BasicStroke(4.0f));
            renderer.setSeriesStroke(1, new BasicStroke(3.0f));
            renderer.setSeriesStroke(2, new BasicStroke(2.0f));
            renderer.setSeriesStroke(3, new BasicStroke(1.0f));
            plot.setRenderer(renderer);
            //setContentPane( chartPanel ); 
            panelGrafico.removeAll();
            panelGrafico.add(chartPanel);
            panelGrafico.revalidate();
            panelGrafico.repaint();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
