
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;




public class ScreenLayoutTool extends JFrame
{

	public static void main(String args[])
	{
		//initialize Nimbus look and feel:
		try {
    	    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
    	        if ("Nimbus".equals(info.getName())) {
    	            UIManager.setLookAndFeel(info.getClassName());
    	            break;
    	        }
    	    }
    	} catch (Exception e) {
    	    // If Nimbus is not available, you can set the GUI to another look and feel.
    	} 		
		
		new ScreenLayoutTool();
		
   		
	}
	
	public static SceneView sceneView;
	public static ScreenLayoutTool instance;
	
	public ScreenLayoutTool()
	{
		instance = this;
		sceneView = new SceneView();
		
		setLocation(new Point(0,0));
		setSize(200,250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		initComponents();
		addComponentListeners();
		setVisible(true);
	}
	
	private void initComponents()
	{
		contentPane = this.getContentPane();		
		SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);		
        
        useImageButton = new JButton("Use Image");
        clearLayoutButton = new JButton("Clear Layout");
        loadLayoutButton = new JButton("Load Layout");
        saveLayoutButton = new JButton("Save Layout");
        nameTextField = new JTextField(10);
        nameTextField.setEnabled(false);        
        descTextField = new JTextField(10);
        descTextField.setEnabled(false);        
        
        contentPane.add(useImageButton);
		layout.putConstraint(SpringLayout.WEST, useImageButton, 10, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, useImageButton, 10 + 30*0, SpringLayout.NORTH, contentPane);

        contentPane.add(clearLayoutButton);
        layout.putConstraint(SpringLayout.WEST, clearLayoutButton, 10, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, clearLayoutButton, 10 + 30*1, SpringLayout.NORTH, contentPane);		
		
        contentPane.add(loadLayoutButton);
        layout.putConstraint(SpringLayout.WEST, loadLayoutButton, 10, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, loadLayoutButton, 10 + 30*2, SpringLayout.NORTH, contentPane);
		
        contentPane.add(saveLayoutButton);
        layout.putConstraint(SpringLayout.WEST, saveLayoutButton, 10, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, saveLayoutButton, 10 + 30*3, SpringLayout.NORTH, contentPane);
		
        contentPane.add(nameTextField);
        layout.putConstraint(SpringLayout.WEST, nameTextField, 10, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, nameTextField, 10 + 30*4, SpringLayout.NORTH, contentPane);
		
        contentPane.add(descTextField);
        layout.putConstraint(SpringLayout.WEST, descTextField, 10, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, descTextField, 10 + 30*5, SpringLayout.NORTH, contentPane);		
	}

	
	private void addComponentListeners()
	{
		useImageButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String filename = getFileName();
				if(filename != null)
				{
					sceneView.loadImage(filename);
				}
			}
		});
		
		clearLayoutButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				disableText();
				Data.clearAll();
				if(sceneView!=null)
				{
					sceneView.selectedBox = null;
					sceneView.repaint();
				}
			}
		});		
		
		loadLayoutButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String filename = getFileName();
				if(filename != null)
				{
					Data.loadFromFile(filename);
				}
			}
		});		
		
		saveLayoutButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String filename = getFileName();
				if(filename != null)
				{
					Data.saveToFile(filename);
				}
			}
		});		
		
		nameTextField.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent e)
			{
				updateSelectedBoxName();
			}
		});
		
		descTextField.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent e)
			{
				updateSelectedBoxDesc();
			}
		});		
	}
	
	private String getFileName()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    return selectedFile.getAbsolutePath();
		}
		return null;
	}
	
	public void enableText(String name, String desc)
	{
		nameTextField.setEnabled(true);
		nameTextField.setText(name);
		descTextField.setEnabled(true);
		descTextField.setText(desc);				
	}
	
	public void disableText()
	{
		nameTextField.setText("");
		nameTextField.setEnabled(false);
		descTextField.setText("");
		descTextField.setEnabled(false);		
	}
	
	private void updateSelectedBoxName()
	{
		if(sceneView!=null && sceneView.selectedBox != null)
		{
			sceneView.selectedBox.name = nameTextField.getText();
		}
	}
	
	private void updateSelectedBoxDesc()
	{
		if(sceneView!=null && sceneView.selectedBox != null)
		{
			sceneView.selectedBox.desc = descTextField.getText();
		}
	}	
	
	private JButton useImageButton;
	private JButton clearLayoutButton;
	private JButton loadLayoutButton;
	private JButton saveLayoutButton;
	public JTextField nameTextField;
	public JTextField descTextField;
	private Container contentPane;
	
}
