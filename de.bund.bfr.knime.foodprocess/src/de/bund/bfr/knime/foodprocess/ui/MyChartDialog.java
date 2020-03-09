/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.foodprocess.ui;


import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.gui.InfoBox;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.jgoodies.forms.layout.*;
import edu.hws.jcm.data.*;
import edu.hws.jcm.awt.*;

public class MyChartDialog extends JDialog {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 821846675299958925L;
	private XYDataset dataset;
    private XYSeries series2;
    private String newStrDataSet;
    private String oldStrDataSet;
    private Object[][] tableData;
    
	public MyChartDialog() {
	}
	public MyChartDialog(final Window owner, final String strDataSet, final String xAxis, final String yAxis) {
		super(owner);
		
		if (isNumber(strDataSet)) this.newStrDataSet = "0-1:" + strDataSet;
		else this.newStrDataSet = strDataSet;
		oldStrDataSet = newStrDataSet;
		
		init(newStrDataSet, xAxis, yAxis, null, null, 0);
		
		final JFreeChart chart = createChart(dataset, xAxis, yAxis);
        final ChartPanel chartPanel = new ChartPanel(chart);
        panel1.remove(button1);
		panel1.add(chartPanel, new CellConstraints().xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL));
		this.setTitle(xAxis + "-" + yAxis + "-Profile");

		table1.getModel().addTableModelListener(
				new TableModelListener() {
					public void tableChanged(TableModelEvent e) {
				        dataset = createDataset(null, null, 0);
					}
				}
		);
	}
	private void init(final String strDataSet, final String xAxis, final String yAxis, final Double mini, final Double maxi, final double step) {
		series2 = new XYSeries("Datenpunkte", true, false);
		initComponents();

		fillTable(strDataSet, xAxis, yAxis);
		dataset = createDataset(mini, maxi, step);		
	}

	private boolean isNumber(final String strDataSet) {
		if (strDataSet.indexOf(":") < 0 && strDataSet.indexOf(";") < 0) {
			try {
				NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
				nf.parse(strDataSet);
				return true;
			}
			catch (Exception e) {}
		}
		return false;
	}
	public String getCleanDataset(final String strDataSet) {
        if (strDataSet.indexOf(";") < 0 && strDataSet.startsWith("0-1:")) {
        	String number = strDataSet.substring(strDataSet.indexOf(":") + 1);
        	if (isNumber(number)) return number;
        }
        return strDataSet;
	}
	public String getStrDataSet() {
		return newStrDataSet;
	}
    public static void main(final String[] args) {


/*
        MyChartDialog mcd = new MyChartDialog(this, bt.getToolTipText(), xAxis, spaltenName);
        mcd.setModal(true);
        //mcd.pack();
        mcd.setVisible(true);
        bt.setToolTipText(mcd.getDatenpunkte());
*/
    	JDialog f = new JDialog();
        final MyChartDialog demo = new MyChartDialog(f, null, "Time", "Temperature");
        demo.pack();
        demo.setVisible(true);

    }

	private void button2ActionPerformed(ActionEvent e) {
		dispose();	
	}

	private void button3ActionPerformed(ActionEvent e) {
		newStrDataSet = oldStrDataSet;
		dispose();	
	}

	private void button4ActionPerformed(ActionEvent e) {
		String hilfe = "In der linken Spalte der Tabelle können einzelne Punkte für die x-Achse angegeben werden.\n" +
			"In der rechten Spalte die zugehörigen Werte.\n" +
			"Es ist möglich für die x-Achse Intervalle einzugeben mit '-' als Trennzeichen (z.B. 3-5).\n" +
			"Außerdem ist es möglich verschiedene Einheiten für Zeiten anzugeben, z.B. 3d 13h 13m 5s\n" +
			"Für die y-Achse können einfache Zahlen angegeben werden oder auch Funktionen (mit x als Variable),\n" +
			"z.B. x^2, 2*x, exp(x), log10(x), ln(x)";
		if (DBKernel.getLanguage().equals("en")) {
			hilfe = "In this table values for time (x-axis) and conditions (e.g. temperature; y-axis) can be entered.\n" +
					"Define time intervals using ‘-‘ (e.g. 3-5).\n" +
					"Assign time units using d (day), h (hour), m (minute) or s (second), e.g. 3d 13h 13m 5s.\n" +
					"Values on the y-axis may be numerical data or functions with x as variable, e.g. x^2, 2*x, exp(x), log10(x), ln(x).\n" +
					"Please note that it is necessary to use ‘*’ to indicate that x is multiplied.";
		}
		
		InfoBox ib = new InfoBox(this, hilfe, true, new Dimension(950, 300), null, true);
		ib.setVisible(true);    				  	
		
		//System.out.println(hilfe);
	}

    private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		splitPane1 = new JSplitPane();
		scrollPane1 = new JScrollPane();
		table1 = new JTable();
		panel1 = new JPanel();
		button1 = new JButton();
		panel2 = new JPanel();
		button2 = new JButton();
		button3 = new JButton();
		button4 = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"default:grow",
			"2*(default, $lgap), default"));

		//======== splitPane1 ========
		{
			splitPane1.setDividerLocation(200);

			//======== scrollPane1 ========
			{
				scrollPane1.setPreferredSize(new Dimension(200, 423));

				table1.addKeyListener(new ClipboardKeyAdapter(table1));
				tableData = new Object[1024][2];
				tableData[0][0] = "3.0";
				tableData[0][1] = "5";
				//---- table1 ----
				table1.setModel(new DefaultTableModel(
					tableData,
					new String[] {
						"Zeit (h)", "Temperatur"
					}
				) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 2738321074015881424L;
					Class<?>[] columnTypes = new Class<?>[] {
						String.class, String.class
					};
					@Override
					public Class<?> getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				});
				{
					TableColumnModel cm = table1.getColumnModel();
					cm.getColumn(0).setPreferredWidth(100);
				}
				scrollPane1.setViewportView(table1);
			}
			splitPane1.setLeftComponent(scrollPane1);

			//======== panel1 ========
			{
				panel1.setLayout(new FormLayout(
					"default:grow",
					"fill:default:grow"));

				//---- button1 ----
				button1.setText("text");
				panel1.add(button1, cc.xy(1, 1));
			}
			splitPane1.setRightComponent(panel1);
		}
		contentPane.add(splitPane1, cc.xy(1, 1));

		//======== panel2 ========
		{
			panel2.setLayout(new FormLayout(
				"3*(default:grow, $lcgap), default:grow",
				"default"));
			((FormLayout)panel2.getLayout()).setColumnGroups(new int[][] {{1, 3, 5, 7}});

			//---- button2 ----
			button2.setText("OK");
			button2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					button2ActionPerformed(e);
				}
			});
			panel2.add(button2, cc.xy(3, 1));

			//---- button3 ----
			button3.setText("Cancel");
			button3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					button3ActionPerformed(e);
				}
			});
			panel2.add(button3, cc.xy(5, 1));

			//---- button4 ----
			button4.setText("Help");
			button4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					button4ActionPerformed(e);
				}
			});
			panel2.add(button4, cc.xy(7, 1));
		}
		contentPane.add(panel2, cc.xy(1, 5));
		setSize(555, 390);
		setLocationRelativeTo(null);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JSplitPane splitPane1;
	private JScrollPane scrollPane1;
	private JTable table1;
	private JPanel panel1;
	private JButton button1;
	private JPanel panel2;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	// JFormDesigner - End of variables declaration  //GEN-END:variables


	private void fillTable(final String strDataSet, final String xAxis, final String yAxis) {
		table1.getColumnModel().getColumn(0).setHeaderValue(xAxis);
		table1.getColumnModel().getColumn(1).setHeaderValue(yAxis);
		int row = 0;
        for (int i=0;i<table1.getModel().getRowCount();i++) {
        	table1.getModel().setValueAt(null, i, 0);
        	table1.getModel().setValueAt(null, i, 1);
        }
        if (strDataSet != null) {
    		StringTokenizer tok = new StringTokenizer(strDataSet, ";");
    		while (tok.hasMoreTokens()) {
    			String vals = tok.nextToken();
    			int index = vals.indexOf(":");
    			if (index > 0) {
        			table1.getModel().setValueAt(vals.substring(0, index), row, 0);
        			table1.getModel().setValueAt(vals.substring(index+1), row, 1);    				
    			}
    			else {
        			table1.getModel().setValueAt("0-10000000", row, 0);
        			table1.getModel().setValueAt(vals, row, 1);    				
    				//System.err.println("java.lang.StringIndexOutOfBoundsException - fillTable - index <= 0: " + vals + "\n" + strDataSet);
    			}
    			row++;
    			if (row == table1.getModel().getRowCount()) break;
    		}        	
        }
	}
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private XYDataset createDataset(final Double mini, final Double maxi, final double step) {
    	series2.clear();
    	newStrDataSet = "";
        for (int i=0;i<table1.getModel().getRowCount();i++) {
        	Object o1 = table1.getModel().getValueAt(i, 0);
        	Object o2 = table1.getModel().getValueAt(i, 1);
        	if (o1 == null || o2 == null || o1.toString().trim().length() == 0 || o2.toString().trim().length() == 0) {
        		break;
        	}
        	else {
        		String interval = o1.toString().trim().replace(",", ".");
        		String function = o2.toString().trim().replace(",", ".");
        		newStrDataSet += ";" + interval + ":" + function;
        		Double tVal = getDouble(interval); 
        		Double val = getDouble(function); 
        		if (val != null && tVal != null && (mini == null || tVal >= mini && tVal <= maxi)) {
        			series2.addOrUpdate(tVal, val);   
        		}
        		else if (tVal == null || tVal >= mini && tVal <= maxi) {
        			parseString(series2, function, interval, mini, maxi, step); // z.B. sin(x)+2*cos(3*x)
        		}
        	}
        }
        if (newStrDataSet.length() > 0) newStrDataSet = newStrDataSet.substring(1);

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series2);
                
        return dataset;
    }
    private Double getDouble(final String val) {
    	Double result = null;
    	try {
    		if (val.indexOf("d") < 0  && val.indexOf("D") < 0) result = Double.parseDouble(val.trim());
    		else Double.parseDouble("wsd");
    	}
    	catch (Exception e) {
    		// es ist erlaubt d: Tage, h: Stunden, m: Minuten, s: Sekunden dranzuschreiben
    		try {
        		Double res = 0.0;
        		String tVal = val.trim();
        		if (tVal.length() > 0) {
            		int index = tVal.indexOf("d");  
            		if (index >= 0) {res += Double.parseDouble(tVal.substring(0, index).trim()) * 24; tVal = tVal.substring(index+1).trim();}   
            		index = tVal.indexOf("h");  
            		if (index >= 0) {res += Double.parseDouble(tVal.substring(0, index).trim()); tVal = tVal.substring(index+1).trim();}   
            		index = tVal.indexOf("m");  
            		if (index >= 0) {res += Double.parseDouble(tVal.substring(0, index).trim()) / 60; tVal = tVal.substring(index+1).trim();}   
            		index = tVal.indexOf("s");  
            		if (index >= 0) {res += Double.parseDouble(tVal.substring(0, index).trim()) / 60 / 60; tVal = tVal.substring(index+1).trim();}        			
        		}
        		if (tVal.length() == 0) result = res;
    		}
        	catch (Exception e1) {}
    	}
    	return result;
    }
    private void parseString(final XYSeries series, final String function, final String interval, final Double mini, final Double maxi, final double stepi) { 	   
        Function func = getFunction(function);
        
        Double min = null, max = null;
        double step = 1;
		int numPoints = 50;
        if (mini == null) {
            int index = interval.indexOf("-", interval.charAt(0) == '-' ? 1 : 0);
            if (index > 0) {
            	min = getDouble(interval.substring(0,index));
            	max = getDouble(interval.substring(index + 1));
                step = (max - min) / numPoints;
            }
        }
        else {
            int index = interval.indexOf("-", interval.charAt(0) == '-' ? 1 : 0);
            if (index > 0) {
            	min = getDouble(interval.substring(0,index));
            	max = getDouble(interval.substring(index + 1));
            	min = Math.max(min, mini);
            	max = Math.min(max, maxi);
            }
            else {
            	min = mini;
            	max = maxi;            	
            }
        	step = stepi;
        	numPoints = (int) ((max - min) / step) + 1;
        }
        if (min != null && max != null) {
            for (int i=0;i<numPoints+1;i++) {
            	double t = min+i*step;
            	series.addOrUpdate(t, func.getVal(new double[]{t}));   
            }
    	}
     } 
    private Function getFunction(final String function) {
        Parser parser = new Parser();      // Create the parser and the variable, x.
        Variable x = new Variable("x");
        parser.add(x);
        
        ExpressionInput input = new ExpressionInput(function, parser);  // For user input
        Function func = input.getFunction(x);  // The function that will be graphed.
        
        return func;
    }
    public double getValue(final String function, final double xVal) {
        Function func = getFunction(function);
        return func.getVal(new double[]{xVal});
    }
    public XYSeries getSerie(final String strDataSet, final Double min, final Double max, final double step) {
    	if (strDataSet == null || strDataSet.equalsIgnoreCase("null")) return null;
		init(strDataSet, "", "", min, max, step);
        XYSeries series = new XYSeries("Datenpunkte", true, false);
		for (double t=min;t<=max;t+=step) {
			if (t < series2.getMinX()) {
				continue;
			}
			if (t > series2.getMaxX()) {
				break;
			}
			for (int i=0;i<series2.getItemCount();i++) {
				if (t > series2.getDataItem(i).getXValue()) continue;
				else if (t == series2.getDataItem(i).getXValue()) {
					series.add(t, series2.getDataItem(i).getYValue());
					i = series2.getItemCount();
				}
				else {
					double newVal = series2.getDataItem(i-1).getYValue() + (t - series2.getDataItem(i-1).getXValue()) *
									(series2.getDataItem(i).getYValue() - series2.getDataItem(i-1).getYValue()) /
									(series2.getDataItem(i).getXValue() - series2.getDataItem(i-1).getXValue());
					series.add(t, newVal);
					i = series2.getItemCount();
				}
			}
		}
        return series;
    }
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset, final String xAxis, final String yAxis) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
        		"",      // chart title
            xAxis,                      // x axis label
            yAxis,                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            false,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        //plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
        
    }

}
