package com.emeralddb.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

@SuppressWarnings("serial")
public class EmeralddbGraphMonitor extends ApplicationFrame
   implements ActionListener {

   /*********************************************/
   private final int  TIME_PERIOD = 1000;
   private final int DOT_TIME = 10;
   /*********************************************/
   private Map<TimeSeries,Emeralddb> timeSeriesMap = null;
   private Vector<Emeralddb> emeralddbVec;
   private TimeSeriesCollection localTimeSeriesCollection;
   private Vector<String> serverVec;
   private int ySize = 0;
   private String server = null;
   private boolean isMonitoring = false;
   private boolean isServerRunning = false;
   /* record the number of record */

   /*********************************************/
   private DataGenerator dataGeneratorTimer;

   /*********************************************/
   private JLabel ipLabel;
   private JTextField ipTextField;
   private JLabel portLabel;
   private JTextField portTextField;

   private JFreeChart jfreechart  =null ;

   /* panel */
   private JPanel panel = null;
   private JPanel leftPanel = null;
   private JList  serverList = null;
   private JPanel lowRightPanel = null;
   private JPanel serverPanel = null;
   private JPanel controlPanel = null;
   private ChartPanel chartPanel = null;
   /* button */
   private JButton addButton = null;
   private JButton finishButton = null;
   private JButton monitorButton = null;
   private JButton stopButton = null;
   private JPopupMenu popupMenu = null;
   /* ErrorMessage */
   private JTextArea messageLabel = null;

   /*********************************************/

   class DataGenerator extends Timer implements ActionListener {

      public void actionPerformed(ActionEvent actionevent) {
         Set<Entry<TimeSeries,Emeralddb>> entrySet = timeSeriesMap.entrySet();

         int max = 0;
         for( int i = 0; i < emeralddbVec.size(); i++ ) {
            Emeralddb edb = emeralddbVec.get(i);
            edb.getUpdateTimes();
            int subMax = edb.maxRecordNumber();
            if( max < subMax ) {
               max = subMax;
            }
         }
         if(0 == max) {
            setVValueAxis( 100, 100/25 );
         } else {
            setVValueAxis( max + 50, (max+50)/25 );
         }
         panel.updateUI();
         for(Entry<TimeSeries,Emeralddb> entry : entrySet ) {
            TimeSeries ts = entry.getKey();
            Emeralddb edb = entry.getValue();
            int times = edb.getCurInsertTimes();
            if( -1 == times )
            {
               isServerRunning = false;
               end();
               break;
            }
            ts.addOrUpdate( new Second(), (double)times );
         }
       }

       DataGenerator(int i) {
           super(i, null);
           addActionListener(this);
       }
   }

   public static void main( String[] args ) {
      EmeralddbGraphMonitor egm = new EmeralddbGraphMonitor(" Emeralddb Data Insert Monitor ");
      egm.pack();
        RefineryUtilities.centerFrameOnScreen(egm);
        egm.setVisible(true);
   }

   public EmeralddbGraphMonitor( String s ) {
      super(s);

      // Server list
      serverVec = new Vector<String>();
      localTimeSeriesCollection = new TimeSeriesCollection();
      timeSeriesMap = new HashMap<TimeSeries,Emeralddb>();

      // initital every component
      addButton = new JButton("add");
      addButton.addActionListener(this);
      finishButton = new JButton("finish");
      finishButton.addActionListener(this);
      monitorButton = new JButton("monitor");
      monitorButton.setEnabled(false);
      monitorButton.addActionListener(this);
      stopButton = new JButton("end");
      stopButton.setEnabled(false);
      stopButton.addActionListener(this);
      messageLabel = new JTextArea("");

      popupMenu = new JPopupMenu();
      JMenuItem deleteItem = new JMenuItem("delete");
      popupMenu.add( deleteItem );
      deleteItem.addActionListener( new ActionListener() {
         public void actionPerformed( ActionEvent e ) {
            deleteServerFromList(e);
         }
      });

      ipLabel = new JLabel( "IP:" );
      portLabel = new JLabel( "PORT:" );
      ipTextField = new JTextField(12);
      ipTextField.setText("localhost");
      portTextField =  new JTextField(5);
      portTextField.setText("48127");

      serverList = new JList( serverVec );
      panel = new JPanel();
      leftPanel = new JPanel();
      lowRightPanel = new JPanel();
      serverPanel = new JPanel();
      controlPanel = new JPanel();

      createComponent( 1000, 600 );
      this.setContentPane( panel );

      KeyStroke strokeAdd = KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, ActionEvent.CTRL_MASK, true );
      addButton.registerKeyboardAction( new ActionListener() {
         public void actionPerformed( ActionEvent e ) {
            addButton.grabFocus();
            add();
            portTextField.grabFocus();
         }
      }, strokeAdd, JComponent.WHEN_IN_FOCUSED_WINDOW |JComponent.WHEN_FOCUSED);


      serverList.addMouseListener( new MouseListener() {
         @Override
         public void mouseEntered( MouseEvent e) {

         }
         @Override
         public void mouseExited( MouseEvent e) {

         }
         @Override
         public void mouseClicked( MouseEvent e) {

         }
         @Override
         public void mousePressed( MouseEvent e) {

         }
         @Override
         public void mouseReleased( MouseEvent e) {
            selectServer(e);
         }
      });

      this.addWindowStateListener( new WindowStateListener() {
         @Override
         public void windowStateChanged(WindowEvent e) {
            mainWindowStateChanged(e);
         }
      });
      this.addComponentListener( new ComponentListener() {
         @Override
         public void componentHidden(ComponentEvent e) {

         }

         @Override
         public void componentMoved(ComponentEvent e) {

         }

         @Override
         public void componentResized(ComponentEvent e) {
            mainWindowStateResized(e);
         }

         @Override
         public void componentShown(ComponentEvent e) {

         }
      });

      emeralddbVec  = new Vector<Emeralddb>();
   }

   private void clearErrorMsg( MouseEvent e ) {

   }
   private void selectServer( MouseEvent e ) {
      if( e.isPopupTrigger() && serverList.getSelectedIndex() != -1 ) {
         server = serverList.getSelectedValue().toString();
         popupMenu.show( e.getComponent(), e.getX(), e.getY() );
      }
   }

   private void deleteServerFromList ( ActionEvent e ) {
      if( !finishButton.isEnabled() ) {
         showError("can not delete the server after finish.");
         return;
      }
      serverVec.remove(server);
      serverList.updateUI();
   }

   private void mainWindowStateResized( ComponentEvent e ) {
      serverPanel.removeAll();
      controlPanel.removeAll();
      lowRightPanel.removeAll();
      leftPanel.removeAll();
      panel.removeAll();
      createComponent( this.getWidth()-10, this.getHeight()-30 );
      panel.updateUI();
   }

   private void mainWindowStateChanged(WindowEvent e) {
      int newState = e.getNewState();
      switch( newState ) {
      case MAXIMIZED_BOTH:
         serverPanel.removeAll();
         controlPanel.removeAll();
         lowRightPanel.removeAll();
         leftPanel.removeAll();
         panel.removeAll();
         createComponent( this.getWidth()-10, this.getHeight()-30 );
         break;
      case NORMAL:
         panel.removeAll();
         serverPanel.removeAll();
         controlPanel.removeAll();
         lowRightPanel.removeAll();
         leftPanel.removeAll();
         panel.removeAll();
         createComponent( 1000, 600 );
         break;
      default:
         createComponent( 1000, 600 );
         break;
      }
   }

   private void placeLeftPanel( int x, int y, int width, int heigth ) {
      /*===========================leftPanel==========================================*/
      Rectangle leftRect = new  Rectangle( x, y, width, heigth );
      leftPanel.setBounds( leftRect );
      leftPanel.setBorder( BorderFactory.createTitledBorder("Emeraddb Moinitor Chart") );

      if( chartPanel != null) {
         placeJfreeChart(leftRect.x, leftRect.y,
               leftRect.width, leftRect.height);
      }
      panel.add(leftPanel);
   }

   private void placeJfreeChart(int x, int y, int width, int height ) {
      /* panel is a component that contains chart */
      //System.out.println( String.format( " %d-%d", width, height));
      Rectangle chartPanelRect = new Rectangle( x, y, width, height );
      chartPanel.setBounds(chartPanelRect);
      //chartPanel.setPreferredSize( new Dimension(width, height-30) );
      leftPanel.add(chartPanel);
      leftPanel.updateUI();
   }

   private void placerightPannel( int x, int y, int width, int height ) {
      // first define lowRightPanel height
      int lowRightPanelHeight = 220;
      int controlPanelHeight = 40;
      int serverPanelHeight = 40;

      /*===========================serverList==========================================*/
      Rectangle rightRect = new Rectangle( x, y, width, height-lowRightPanelHeight - 10 );
      //serverList.setBounds( rightRect );
      serverList.setBorder( BorderFactory.createTitledBorder("Server List") );
      JScrollPane serverListScroll = new JScrollPane( serverList );
      serverListScroll.setBounds( rightRect );
      serverListScroll.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
      panel.add( serverListScroll );


      Rectangle lowRightRect = new Rectangle( x , height-lowRightPanelHeight , width, lowRightPanelHeight );
      lowRightPanel.setBounds( lowRightRect );
      panel.add( lowRightPanel );

      /*===========================serverPanel==========================================*/
      Rectangle serverPanelRect = new Rectangle( x ,
            (int)lowRightRect.getY() + 5, width, serverPanelHeight );
      serverPanel.setBounds( serverPanelRect );
      serverPanel.setLayout( new BoxLayout( serverPanel, BoxLayout.X_AXIS ) );
      lowRightPanel.add( serverPanel );
      serverPanel.add(ipLabel);
      serverPanel.add(Box.createRigidArea( new Dimension(15,10) ) );
      serverPanel.add(ipTextField);
      serverPanel.add(Box.createRigidArea( new Dimension(18,10) ) );
      serverPanel.add( portLabel);
      serverPanel.add(Box.createRigidArea( new Dimension(13,10) ) );
      serverPanel.add(portTextField);

      /*===========================controlPanel==========================================*/
      Rectangle controlPanelRect = new Rectangle( x ,
            (int)serverPanelRect.getY() + (int)serverPanelRect.getHeight()+5, width, controlPanelHeight );
      controlPanel.setBounds( controlPanelRect );
      controlPanel.setLayout( new BoxLayout( controlPanel, BoxLayout.X_AXIS ) );
      lowRightPanel.add(controlPanel);
      controlPanel.add(addButton);
      controlPanel.add(Box.createRigidArea( new Dimension(3,10) ) );
      controlPanel.add(finishButton);
      controlPanel.add(Box.createRigidArea( new Dimension(3,10) ) );
      controlPanel.add(monitorButton);
      controlPanel.add(Box.createRigidArea( new Dimension(3,10) ) );
      controlPanel.add(stopButton);

      /*===========================messageLabel==========================================*/
      //messageLabel.setPreferredSize( new Dimension(width, (int)(height-controlPanelRect.getY()-13)) );
      //messageLabel.setBorder( BorderFactory.createTitledBorder("Error Show") );
      messageLabel.setEditable(false);
      messageLabel.setLineWrap( true );
      //messageLabel.setWrapStyleWord( true );
      messageLabel.setForeground( Color.RED );
      JScrollPane scroll = new JScrollPane(messageLabel);
      scroll.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
      scroll.setPreferredSize( new Dimension(width, (int)(height-controlPanelRect.getY()-13)) );
      lowRightPanel.add( scroll );
   }

   private void createComponent(int width, int height) {
      /*===========================panel==========================================*/
      panel.setLayout( null );
      Dimension panelSize = new Dimension( width, height );
      panel.setPreferredSize( panelSize );

      int rightPanelWidth = 300;
      int marginWidth = 5;
      int marginHeight = 5;
      placeLeftPanel( marginWidth, marginWidth, width- rightPanelWidth , height- marginHeight);
      placerightPannel( width- rightPanelWidth + marginWidth, marginWidth ,rightPanelWidth - marginWidth, height -marginHeight );
   }

   private void createJfreechart() {
      // create jfreechar
      if( jfreechart == null ) {
         jfreechart =  ChartFactory.createTimeSeriesChart(
                 "Emeralddb Data Insert Monitor",
                 "",
                 "Record Per Unit",
                 localTimeSeriesCollection,
                 true, true, true );

         // computer the max numer
         int max = 0;
         for(int i = 0; i < emeralddbVec.size(); i++ ) {
            Emeralddb edb = emeralddbVec.get(i);
            int number = edb.getUpdateTimes();
            if( max < number ) {
               max = number;
            }
         }
         if( max == 0 ) {
            max = 100;
         }
         ySize = max;
         setJfreeChartParameter( ySize, ySize/25);
            chartPanel = new ChartPanel( jfreechart );
            chartPanel.setMouseZoomable( false );
            chartPanel.setPopupMenu(new EdbPopupMenu());

         placeJfreeChart( (int)leftPanel.getX(),
               (int)leftPanel.getY(),
               (int)leftPanel.getWidth(),
               (int)leftPanel.getHeight() );
      }
   }

   /***
    * set jfreeParameter
    * @param maxNumber
    * @param unit
    */
   private void setJfreeChartParameter(int maxNumber, int unit) {
      if( null == jfreechart ) {
         System.out.println( "jfreechart is not initialized." );
         return;
      }
      /* plot is area object of chart */
      XYPlot plot = jfreechart.getXYPlot();
      /* splinerenderer is a renderer that connects data
       * points with natural cubic splines. */
      XYSplineRenderer splinerenderer = new XYSplineRenderer();
      splinerenderer.setSeriesStroke(0, new BasicStroke(1.0F, 1, 1, 10.0F));
      plot.setRenderer(splinerenderer);

      /* x grid */
      plot.setDomainGridlinesVisible( false );
      /**/
      plot.setRangeGridlinesVisible(true);
      /* set background alpha value */
      plot.setBackgroundAlpha(0.3f);

      /* set gridline's color of x axis */
      plot.setRangeGridlinePaint( Color.BLACK );
      /* set grindline's color of y axis */
      plot.setDomainGridlinePaint(Color.BLACK);

      setVValueAxis( maxNumber, unit );

      DateAxis domainAxis = (DateAxis)plot.getDomainAxis();
      DateTickUnit tickUnit = new DateTickUnit(DateTickUnitType.SECOND, DOT_TIME, new SimpleDateFormat("ss"));
      domainAxis.setTickUnit( tickUnit );
   }

   /***
    * set Y axis parameter
    * @param maxNumber
    * @param unit
    */
   private void setVValueAxis( int maxNumber, int unit ) {
      if( null == jfreechart ) {
         System.out.println( "jfreechart is not initialized." );
         return;
      }

      /* plot is area object of chart */
      XYPlot plot = jfreechart.getXYPlot();
      NumberAxis vValueAxis = (NumberAxis)plot.getRangeAxis();
      vValueAxis.setUpperBound(maxNumber);
      vValueAxis.setLowerBound(0);
      //vValueAxis.setAutoRange(true);
      vValueAxis.setAutoTickUnitSelection(false);
      NumberTickUnit nt = new NumberTickUnit(unit);
      vValueAxis.setTickUnit( nt );
   }

   private void add() {

      String ip = ipTextField.getText();
      String port = portTextField.getText();
      String strServer = ip + ":" + port;
      for( int i = 0; i < serverVec.size(); i++ ) {
         String tmp = serverVec.get(i);
         if( strServer.equals(tmp) ) {
            showError( "the server is also exist." );
            return;
         }
      }

      Emeralddb edb = new Emeralddb(ip, Integer.parseInt(port) );
      if( edb.start() ) {
         serverVec.add( strServer );
         serverList.updateUI();
         emeralddbVec.add( edb );
         return;
      } else {
         showError( edb.getErrorMsg() );
         return;
      }
   }

   private void showError( String error ) {
      if(messageLabel.getLineCount() > 50 ) {
         messageLabel.setText("");
      }
      messageLabel.append( error);
      messageLabel.append("\n");
      messageLabel.paintImmediately( messageLabel.getBounds() );
   }

   private void monitor() {
      System.out.println("monitor start");
      if( serverVec.size() > 0 ) {
         dataGeneratorTimer = this.new DataGenerator( TIME_PERIOD );
         dataGeneratorTimer.start();
         System.out.println("start timer");
         isMonitoring = true;
         isServerRunning = true;
      } else {
         showError("there are no server connecting.");
         return;
      }
      monitorButton.setEnabled( false );
      stopButton.setEnabled( true );
   }

   private void end() {

      serverVec.removeAllElements();
      serverList.updateUI();

      for(int i=0; i< emeralddbVec.size(); i++ ) {
         Emeralddb edb = emeralddbVec.get(i);
         //edb.disconnect();
      }

      emeralddbVec.removeAllElements();
      timeSeriesMap.clear();
      localTimeSeriesCollection.removeAllSeries();
      if( isMonitoring ) {
         dataGeneratorTimer.stop();
      }
      isMonitoring = false;
      addButton.setEnabled( true);
      finishButton.setEnabled( true );
      monitorButton.setEnabled(true);
      stopButton.setEnabled(false);
   }

   private void finish() {
      if( emeralddbVec.size() <=0 ) {
         showError( "there are no server connecting." );
      }
      /* dataset represents dynamic data */
      for(int i = 0; i< emeralddbVec.size(); i++ ) {
         Emeralddb edb = emeralddbVec.get(i);
         TimeSeries timeSeries = new TimeSeries( edb.getIp() + ":" + edb.getPort() ) ;
         /* set the number of time units in the 'history' for the series. */
         timeSeries.setMaximumItemAge(40);
         /* set the maximum number of items that will be retained in the series. */
         timeSeries.add( new Second(), 0.0f );
         timeSeriesMap.put( timeSeries, edb );
         localTimeSeriesCollection.addSeries( timeSeries );
      }
      createJfreechart();
   }


   public void actionPerformed( ActionEvent e ) {
      if( (JButton)e.getSource() == monitorButton ) {
         monitor();
      } else if( (JButton)e.getSource() == stopButton ) {
         end();
      } else if( (JButton)e.getSource() == addButton ) {
         add();
         return;
      } else if( (JButton)e.getSource() == finishButton ) {
         addButton.setEnabled( false);
         finishButton.setEnabled(false);
         monitorButton.setEnabled(true);
         stopButton.setEnabled(true);
         finish();
      }
   }
}
