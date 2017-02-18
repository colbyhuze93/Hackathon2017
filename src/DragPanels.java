import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

public class DragPanels 
{
	JFrame mainWindow;
	
	JPanel componentPanel;
	JLabel componentsLabel;
	JLabel loopsLabel;
	JList<String> components = new JList<String>(new DefaultListModel<>());
	String[] componentNames = { "ForLoop" };
	
	JPanel logicPanel;
	JLabel programLabel;
	JList<String> destination = new JList<String>(new DefaultListModel<>());
	String dragPrompt = "Drag Components Here";
	
	public DragPanels(JFrame window)
	{
		mainWindow = window;
		
		// Initialize component panel.
		componentPanel = new JPanel();
		componentPanel.setLayout(new BoxLayout(componentPanel, BoxLayout.Y_AXIS));
		componentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		componentPanel.setPreferredSize(new Dimension((mainWindow.getWidth() / 3) - 10,
				mainWindow.getHeight() - 20));
		componentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Initialize logic panel.
		logicPanel = new JPanel();
		logicPanel.setLayout(new GridLayout(0,1));
		logicPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		logicPanel.setPreferredSize(new Dimension(((2*mainWindow.getWidth() / 3)) - 10,
				mainWindow.getHeight() - 10));
		logicPanel.setBackground(Color.BLACK);
		logicPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Populate components panel.
		componentsLabel = new JLabel("Componenets");
		componentsLabel.setFont(new Font("Courier", Font.BOLD,24));
		componentsLabel.setHorizontalAlignment(JLabel.LEFT);
		
		loopsLabel = new JLabel("Loops");
		loopsLabel.setFont(new Font("Courier", Font.BOLD, 18));
		loopsLabel.setHorizontalAlignment(JLabel.LEFT);
		
		for(int i = 0; i < componentNames.length; i++)
			((DefaultListModel<String>) components.getModel()).add(i, componentNames[i]);
		
		((DefaultListModel<String>) destination.getModel()).add(0, dragPrompt);
		
		componentPanel.add(componentsLabel);
		componentPanel.add(loopsLabel);
		componentPanel.add(components);
		
		components.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		components.setDragEnabled(true);
		ListTransferHandler componentsHandler = new ListTransferHandler();
		componentsHandler.SetLogicPanel(logicPanel);
		componentsHandler.SetMainWindow(mainWindow);
		components.setTransferHandler(componentsHandler);
		
		// Populate logic panel.
		programLabel = new JLabel("Program");
		programLabel.setFont(new Font("Courier", Font.BOLD,24));
		programLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		programLabel.setForeground(new Color(0,255,0));
		
		logicPanel.add(programLabel);
		logicPanel.add(destination);
		
		ListTransferHandler destinationHandler = new ListTransferHandler();
		destinationHandler.SetLogicPanel(logicPanel);
		destinationHandler.SetMainWindow(mainWindow);
		destination.setDropMode(DropMode.INSERT);
		destination.setTransferHandler(destinationHandler);
	}
	
	public JPanel GetComponentPanel()
	{
		return componentPanel;
	}
	public JPanel GetLogicPanel()
	{
		return logicPanel;
	}
	public void SetLogicPanel(JPanel newLogic)
	{
		logicPanel = newLogic;
	}
}

class ListTransferHandler extends TransferHandler 
{
	JFrame mainWindow;
	JPanel logic;
	Component[] comps;
	
	public void SetLogicPanel(JPanel logicPanel)
	{
		logic = logicPanel;
		comps = logic.getComponents();
	}
	
	public void SetMainWindow(JFrame main)
	{
		mainWindow = main;
	}
	
	  @Override
	  public int getSourceActions(JComponent c) 
	  {
	    return TransferHandler.COPY_OR_MOVE;
	  }
	  @Override
	  protected Transferable createTransferable(JComponent source) 
	  {
	    JList<String> sourceList = (JList<String>) source;
	    String data = sourceList.getSelectedValue();
	    Transferable t = new StringSelection(data);
	    return t;
	  }

	  @Override
	  protected void exportDone(JComponent source, Transferable data, int action) {
	    /*@SuppressWarnings("unchecked")
	    JList<String> sourceList = (JList<String>) source;
	    String movedItem = sourceList.getSelectedValue();
	    if (action == TransferHandler.MOVE) {
	      DefaultListModel<String> listModel = (DefaultListModel<String>) sourceList
	          .getModel();
	      //listModel.removeElement(movedItem);
	    }*/
	  }
	  
	  @Override
	  public boolean canImport(TransferHandler.TransferSupport support) {
	    if (!support.isDrop()) {
	      return false;
	    }
	    return support.isDataFlavorSupported(DataFlavor.stringFlavor);
	  }
	  @Override
	  public boolean importData(TransferHandler.TransferSupport support) {
		  
		comps = logic.getComponents();
		  
	    if (!this.canImport(support)) {
	      return false;
	    }
	    Transferable t = support.getTransferable();
	    String data = null;
	    try {
	      data = (String) t.getTransferData(DataFlavor.stringFlavor);
	      if (data == null) {
	        return false;
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	      return false;
	    }
	    /*JList.DropLocation dropLocation = (JList.DropLocation) support
	        .getDropLocation();
	    int dropIndex = dropLocation.getIndex();*/
	    JList<String> targetList = (JList<String>) support.getComponent();
	    int dropIndex = Arrays.asList(comps).indexOf(targetList) + 1;
	    System.out.println(comps.length);
	    System.out.println(dropIndex);
	    //DefaultListModel<String> listModel = (DefaultListModel<String>) targetList
	    //    .getModel();
	    /*if (dropLocation.isInsert()) {
	      listModel.add(dropIndex, data);
	    } else {
	      listModel.set(dropIndex, data);
	    }*/
	    MakeNewLabel(data, dropIndex);
	    return true;
	  }
	  
	  void MakeNewLabel(String labelType, int startIndex)
	  {
		  JLabel newLabel;
		  JList<String> destination1 = new JList<String>(new DefaultListModel<>());
		  JList<String> destination2 = new JList<String>(new DefaultListModel<>());
		  String dragPrompt = "Drag Components Here";
		  ((DefaultListModel<String>) destination2.getModel()).add(0, "Drag Components Here");
		  
		  Component[] newComponents;
		  
		  ListTransferHandler destinationHandler = new ListTransferHandler();
		  destinationHandler.SetLogicPanel(logic);
		  destination1.setDropMode(DropMode.INSERT);
		  destination1.setTransferHandler(destinationHandler);
		  destination2.setDropMode(DropMode.INSERT);
		  destination2.setTransferHandler(destinationHandler);
		  
		  switch(labelType)
		  {
		  	case "ForLoop":
		  		((DefaultListModel<String>) destination1.getModel()).add(0, "For Loop Body");
		  		newComponents = new Component[5];
		  		newComponents[0] = new ForLoop();
		  		newComponents[1] = new JLabel("{");
		  		newComponents[2] = destination1;
		  		newComponents[3] = new JLabel("}");
		  		newComponents[4] = destination2;
		  		newLabel = new JLabel("ForLoop");
		  		
		  		for(int i = 0; i < newComponents.length; i++)
		  			logic.add(newComponents[i]);
		  		
		  		comps = logic.getComponents();
		  		//RebuildPanel(startIndex, newComponents);
		  		break;
		  	default:
		  		newLabel = new JLabel("Empty Label");
		  		break;
		  }
		  
		  
		  logic.validate();
		  logic.repaint();
	  }
	  
	  void RebuildPanel(int startIndex, Component[] addedComponents)
	  {
		  Component[] newComponents = new Component[comps.length + addedComponents.length];
		  
		  for(int i = 0; i < startIndex; i++)
			  newComponents[i] = logic.getComponent(i);
		  
		  for(int i = 0; i < addedComponents.length; i++)
			  newComponents[i] = addedComponents[i];
		  
		  for(int i = addedComponents.length + startIndex; i < comps.length; i++)
			  newComponents[i] = logic.getComponent(i);
		  
		  System.out.println(newComponents.length);
		  
		  logic.removeAll();
		  for(int i = 0; i < newComponents.length; i++)
			  logic.add(newComponents[i]);
	  }
	}
