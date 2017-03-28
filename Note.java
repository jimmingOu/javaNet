/**
 * hollowworld!
 */
package note;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;




import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Note extends JFrame{
  
  private JButton searchBtn = new JButton("search");
  private JTextField serchText = new JTextField(20);
  private JTextArea textArea = new JTextArea(25,60);
  private JMenuBar menuBar = new JMenuBar();
  
  private JMenu fileMenu = new JMenu("File");
  private JMenu editMenu = new JMenu("Edit");
  private JMenu aboutMenu = new JMenu("About Us");
  private JMenuItem newFileItem = new JMenuItem("New File");
  private JMenuItem openFileItem = new JMenuItem("Open File");
  private JMenuItem saveFileItem = new JMenuItem("Save");
  private JMenuItem exitItem = new JMenuItem("Exit");

  private JMenuItem cutItem = new JMenuItem("Cut");
  private JMenuItem copyItem = new JMenuItem("Copy");
  private JMenuItem pastItem = new JMenuItem("Past");
  
  private JMenuItem explainItem = new JMenuItem("explain"); 
  private JPanel menuPanel = new JPanel();
  private JPanel searchPanel = new JPanel();    
  private JLabel textNum = new JLabel();  
  private JLabel strlable = new JLabel();
  private String filePath = "";
  private String tempString = "";
  private int textAreaIndexStart = 0;
  private int textAreaIndexEnd = 0; 
  private int mousePosition = 0;
  private void setTextNum(int len , JLabel textlable){
    textlable.setText(""+len);
  }  
    
  
  private void setTextAreaIndex(JTextArea textArea){
    textAreaIndexStart = textArea.getSelectionStart();
    textAreaIndexEnd = textArea.getSelectionEnd();
  }  
  
  private void setFilePath(String path){
    filePath = path ;
  }
  
  private void saveFile(JTextArea textArea){
    PrintWriter pw = null ;
    if( filePath == "" ){
      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter("", "*");
      chooser.setFileFilter(filter);                                                                           
      int returnVal = chooser.showSaveDialog(textArea);
      if(returnVal == JFileChooser.APPROVE_OPTION) {                     
         setFilePath( chooser.getSelectedFile().toString() );            
      }                   
    }else if( filePath != "" ){
      try {
        pw = new PrintWriter(filePath);
        pw.println(textArea.getText());                
      } catch (FileNotFoundException e1) {
        e1.printStackTrace();
      }finally{
         pw.close();
      }
    }      
    
  }  
  
  private void loadFile(){
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter( "", "*");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(menuBar);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
       System.out.println("You chose to open this file: " + chooser.getSelectedFile());
       setFilePath( chooser.getSelectedFile().toString() );
       try ( FileInputStream in = new FileInputStream(chooser.getSelectedFile()) ) {              
         
         int line = in.read();
         String text = "" ;
         for ( ; line != -1 ; ){
           text += (char)line;
           line = in.read();                
         }
         textArea.setText(text);      
         setTextNum(textArea.getText().length() , textNum);
       } catch (IOException e1) {
         e1.printStackTrace();
       }                  
    }    
  }
  private void init(){
      
    setLayout(new BorderLayout()); 
    add(textArea , BorderLayout.CENTER) ; 
    add(menuPanel, BorderLayout.NORTH);
    menuPanel.setLayout(new FlowLayout(0));
    searchPanel.setLayout(new FlowLayout(0));    
    fileMenu.add(newFileItem);
    fileMenu.add(openFileItem);
    fileMenu.add(saveFileItem);
    fileMenu.add(exitItem);    
    editMenu.add(cutItem);
    editMenu.add(copyItem);
    editMenu.add(pastItem);    
    aboutMenu.add(explainItem);    
    menuBar.add(fileMenu);
    menuBar.add(editMenu);
    menuBar.add(aboutMenu);
    menuPanel.add(menuBar);    
    searchPanel.add(serchText);
    searchPanel.add(searchBtn);    
    strlable.setText("text num :");  
    textNum.setText("0");
    searchPanel.add(strlable);
    searchPanel.add(textNum);
    add(searchPanel, BorderLayout.SOUTH);    
    setTitle("notepad");
    setSize(700,700);
    setVisible(true);
    setDefaultCloseOperation(3);      
 
    searchBtn.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub   
        mousePosition = textArea.getSelectionEnd() == textArea.getText().length() ? 0 : mousePosition;
        String searchText = serchText.getText();
        int startIndex = textArea.getText().indexOf(searchText,mousePosition);
        int endIndex = startIndex + searchText.length();
        textArea.requestFocus();
        textArea.setSelectedTextColor(Color.blue);
        if( startIndex != -1 ){
          textArea.setSelectionStart(startIndex);
          textArea.setSelectionEnd(endIndex);
          mousePosition = textArea.getSelectionEnd();
        } 
        else
          mousePosition = 0;
      }
      
    });   
 
    textArea.addCaretListener(
        new CaretListener(){

          @Override
          public void caretUpdate(CaretEvent e) {
            setTextNum(textArea.getText().length() , textNum);            
          }
      
    });  
    
    openFileItem.addActionListener(
        new ActionListener(){
          public void actionPerformed(ActionEvent e){
            loadFile();                               
        }          
      }
        
    );    
    
    newFileItem.addActionListener(
        new ActionListener(){
          public void actionPerformed(ActionEvent e){            
            if( textArea.getText().length() != 0 ){
              int rev = JOptionPane.showConfirmDialog(textArea, "do you want to save file ?","confirm",1);
              if( rev == 0 ){
                saveFile(textArea);               
              }             
            }
            setFilePath("");
            textArea.setText("");                                        
        }          
      }
        
    ); 
    
    saveFileItem.addActionListener(
        new ActionListener(){
          public void actionPerformed(ActionEvent e){
            saveFile(textArea);                              
        }          
      }       
    );    
    
    exitItem.addActionListener(
        new ActionListener(){
          public void actionPerformed(ActionEvent e){
            saveFile(textArea);           
            System.exit(0);                                        
        }          
      }       
    ); 
    
    explainItem.addActionListener(
        new ActionListener(){
          public void actionPerformed(ActionEvent e){                
              JOptionPane.showMessageDialog(textArea, "appx");                      
        }          
      }
        
    ); 
    
    
    cutItem.addActionListener(
        new ActionListener(){
          public void actionPerformed(ActionEvent e){            
            setTextAreaIndex(textArea);             
            tempString = textArea.getText().substring(textAreaIndexStart, textAreaIndexEnd);
            textArea.replaceRange("",textAreaIndexStart,textAreaIndexEnd);                    
                                     
        }          
      }
        
    ); 

    copyItem.addActionListener(
        new ActionListener(){
          public void actionPerformed(ActionEvent e){            
            setTextAreaIndex(textArea);             
            tempString = textArea.getText().substring(textAreaIndexStart, textAreaIndexEnd);                          
                                     
        }          
      }
        
    ); 

    pastItem.addActionListener(
        new ActionListener(){
          public void actionPerformed(ActionEvent e){                  
            setTextAreaIndex(textArea);                                      
            textArea.replaceRange(tempString,textAreaIndexStart,textAreaIndexEnd);
                                     
        }          
      }
        
    );     
  }
  public static void main(String[] args) {           
    Note note = new Note();
    note.init();

  }

}
