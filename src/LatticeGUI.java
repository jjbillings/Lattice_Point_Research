import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
public class LatticeGUI {
	private JFrame window;
	private JPanel panel;
	private JPanel gridPanel;
	private JPanel includePanel;
	private JPanel outputPanel;
	private JPanel calculatePanel;
	private JPanel containerPanel;
	private JCheckBox compiledDataBox;
	private JCheckBox adjacencyGraphBox;
	private JCheckBox percentGraphBox;
	private JTextField initialWidthField;
	private JTextField initialHeightField;
	private JTextField finalWidthField;
	private JTextField finalHeightField;
	private JTextField widthField;
	private JTextField heightField;
	private JTextArea tArea;
	private StringBuilder sb;
	private boolean range, compiled, adj, perc;
	private DataProcessor dp;
	
	
	
	public LatticeGUI(DataProcessor d){
		range = compiled =  true;
		adj = perc = false;
		buildWindow();
		dp = d;
		
	}
	
	public void buildWindow(){
		window = new JFrame();
		window.setTitle("Lattice Point Research Program");
		window.setSize(800, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		buildContainerPanels();
		window.add(panel);
		
		window.setVisible(true);
	}
	
	public void buildContainerPanels(){
		buildGridPanel();
		buildIncludePanel();
		buildOutputPanel();
		buildCalculatePanel();
		
		panel.setLayout(new BorderLayout());
		containerPanel = new JPanel();
		containerPanel.setLayout(new GridLayout(1,3));
		
		containerPanel.add(gridPanel);
		containerPanel.add(includePanel);
		containerPanel.add(outputPanel);
		
		panel.add(containerPanel);
		panel.add(calculatePanel, BorderLayout.SOUTH);
	}
	public void buildCalculatePanel(){
		calculatePanel = new JPanel();
		JButton calcButton = new JButton("Calculate");
		calcButton.addActionListener(new CalculateListener());
		calculatePanel.add(calcButton);
	}
	public void buildGridPanel(){
		gridPanel = new JPanel();
		gridPanel.setBorder(BorderFactory.createEtchedBorder());
		gridPanel.setLayout(new GridLayout(2,1));
		
		JPanel rangePanel = new JPanel();
		JPanel singlePanel = new JPanel();
		
		rangePanel.setLayout(new GridLayout(2,1));
		singlePanel.setLayout(new GridLayout(2,1));
		
		JRadioButton rangeButton = new JRadioButton("Range");
		JRadioButton singleButton = new JRadioButton("Single Grid");
		
		rangeButton.setSelected(true);
		singleButton.setSelected(true);
		
		rangeButton.setActionCommand("Range");
		singleButton.setActionCommand("Single");
		
		rangeButton.addActionListener(new GridListener());
		singleButton.addActionListener(new GridListener());
		
		ButtonGroup gridButtonGroup = new ButtonGroup();
		gridButtonGroup.add(rangeButton);
		gridButtonGroup.add(singleButton);
		
		rangePanel.add(rangeButton);
		singlePanel.add(singleButton);
		
		initialWidthField = new JTextField("Initial Width...");
		initialHeightField = new JTextField("Initial Height...");
		finalWidthField = new JTextField("Final Width...");
		finalHeightField = new JTextField("Final Height...");
		
		widthField = new JTextField("Width...");
		heightField = new JTextField("Height...");
		widthField.setEnabled(false);
		heightField.setEnabled(false);
		
		JPanel rangeTextPanel = new JPanel();
		rangeTextPanel.setLayout(new GridLayout(2,2));
		rangeTextPanel.add(initialWidthField);
		rangeTextPanel.add(initialHeightField);
		rangeTextPanel.add(finalWidthField);
		rangeTextPanel.add(finalHeightField);
		rangePanel.add(rangeTextPanel);
		
		JPanel singleTextPanel = new JPanel();
		singleTextPanel.setLayout(new GridLayout(1,2));
		singleTextPanel.add(widthField);
		singleTextPanel.add(heightField);
		singlePanel.add(singleTextPanel);
		
		gridPanel.add(rangePanel);
		gridPanel.add(singlePanel);
	}
	
	public void buildIncludePanel(){
		includePanel = new JPanel();
		includePanel.setBorder(BorderFactory.createEtchedBorder());
		JLabel includeLabel = new JLabel("Include:");
		//includePanel.add(includeLabel);
		includePanel.setLayout(new GridLayout(3,1));
		
		compiledDataBox = new JCheckBox("Compiled Data");
		adjacencyGraphBox = new JCheckBox("Adjacency Graphs");
		percentGraphBox = new JCheckBox("Percent Seen Graph");
		
		compiledDataBox.setActionCommand("CompData");
		adjacencyGraphBox.setActionCommand("AdjGraph");
		percentGraphBox.setActionCommand("PercentGraph");
		
		compiledDataBox.setSelected(true);
		adjacencyGraphBox.setSelected(false);
		percentGraphBox.setSelected(false);
			
		includePanel.add(compiledDataBox);
		includePanel.add(adjacencyGraphBox);
		includePanel.add(percentGraphBox);
		
	}
	
	public void buildOutputPanel(){
		outputPanel = new JPanel();
		outputPanel.setLayout(new BorderLayout());
		
		sb = new StringBuilder(1024);
		
		tArea = new JTextArea();
		tArea.setLineWrap(true);
		tArea.setEditable(false);
		
		//ScrollPane to store the textArea
		JScrollPane scroll = new JScrollPane(tArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setAutoscrolls(true);
		
		//Another fancy new GUI element, splits the pane in two
		JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		//splitter.setTopComponent(includePanel);
		//splitter.setBottomComponent(scroll);
		//splitter.setDividerLocation(settings.splitterLocation);
		
		//works as a setResizeable(false) method
		splitter.setEnabled(false);
		
		outputPanel.add(scroll);
	}
	
	public class GridListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand() == "Range"){
				initialWidthField.setEnabled(true);
				initialHeightField.setEnabled(true);
				finalWidthField.setEnabled(true);
				finalHeightField.setEnabled(true);
				
				widthField.setEnabled(false);
				heightField.setEnabled(false);
				
				range = true;
			}else if(e.getActionCommand() == "Single"){
				initialWidthField.setEnabled(false);
				initialHeightField.setEnabled(false);
				finalWidthField.setEnabled(false);
				finalHeightField.setEnabled(false);
				
				widthField.setEnabled(true);
				heightField.setEnabled(true);
			
				range = false;
			}
			
		}
		
	}
	public class CalculateListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			sb.append("Beginning Calculations ");
			
			if(range){
				sb.append("For a range of grids...\n");
				tArea.setText(sb.toString());
				tArea.setCaretPosition(tArea.getDocument().getLength());
				window.repaint();
				dp.runGUI(compiledDataBox.isSelected(), adjacencyGraphBox.isSelected(), percentGraphBox.isSelected(), Integer.parseInt(initialWidthField.getText()), Integer.parseInt(initialHeightField.getText()), Integer.parseInt(finalWidthField.getText()), Integer.parseInt(finalHeightField.getText()));
			}
			if(!range){
				sb.append("For a single Grid...\n");
				tArea.setText(sb.toString());
				tArea.setCaretPosition(tArea.getDocument().getLength());
				dp.runGUI(compiledDataBox.isSelected(), adjacencyGraphBox.isSelected(), percentGraphBox.isSelected(), Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
			}
			sb.append("Calculations Complete...\n");
			tArea.setText(sb.toString());
			tArea.setCaretPosition(tArea.getDocument().getLength());
		}
	}
}
