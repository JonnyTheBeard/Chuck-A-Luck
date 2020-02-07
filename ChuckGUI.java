import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
/***************************************************************
 * GUI front end to a dice game called Chuck A Luck
 * 
 * @author Scott Grissom 
 * @version September 14, 2013
 * @update - Ana Posada - October 2019
 * @updated - included arrays for the JCheckBoxes(bets)
 ***************************************************************/
public class ChuckGUI extends JFrame implements ActionListener{

    /** array that contains the 9 JCheckBoxes - bets*/
    private JCheckBox [] bets;

    /** JLabels for the message and credits */
    private JLabel message; 
    private JLabel credits;

    /** Object of the Chuck class */
    private Chuck game;

    /** JButton to roll the dice and play the game */
    private JButton rollButton;

    /** menu items */
    private JMenuBar menus;
    private JMenu fileMenu;
    private JMenuItem quitItem;
    private JMenuItem newGameItem; 
    private JMenuItem addItem;

    /** bets made - to allow to roll only if bets are made */
    int numberBets;

    /****************************************************************
     *   Main method start the game
     ****************************************************************/    
    public static void main(String args[]){        
        ChuckGUI gui = new ChuckGUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setTitle("My Game of Chuck A Luck");
        gui.pack();
        gui.setVisible(true);
    }

    /****************************************************************
     * Constructor
     ****************************************************************/    
    public ChuckGUI(){ 
        // create the game object  
        game = new Chuck(); 
        setupGui () ;
        setupMenu ();
    }

    /****************************************************************
     * Create all elements and display within the GUI
     ****************************************************************/
    private void setupGui () {
        // instantiates the array of JCheckBoxes
        bets = new JCheckBox [9];

        // create the lay out
        setLayout(new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();
        setBackground(Color.lightGray);

        // position the three dice
        // gets the array of GVdie objects from the Chuck game
        GVdie [] dice = game.getDice();
        position.gridy = 1;
        position.gridx = 2;
        position.insets = new Insets(2,2,2,2);

        // positions the dice to the JFrame
        for (GVdie d : dice) {
            add(d, position);            
            position.gridx++;
        }    

        // position credits label
        credits = new JLabel ();
        credits.setText("Credits: " + game.getCredits()); 
        position.gridx = 2;
        position.gridy = 11;
        add(credits, position);    

        // position message label
        message = new JLabel();
        message.setText(game.getMessage());
        position.gridx = 0;
        position.gridy = 0;
        position.gridwidth = 5;
        position.insets.bottom = 20;
        add(message, position);

        // create the roll button and add to grid location 
        position = new GridBagConstraints(); 
        rollButton = new JButton("Roll");
        rollButton.setForeground(Color.red);
        position.gridx = 3;
        position.gridy = 11;
        add(rollButton, position);

        // associate the rollButton with the action listener
        rollButton.addActionListener(this); 

        // position the bet label
        position.gridx = 0;
        position.gridy = 1;
        add(new JLabel("Your Bets"), position);
        position.anchor = GridBagConstraints.WEST;

        // create and position the 9 JCheckBoxes
        for (int i = 8; i >= 0 ; i--) {
            switch (i) {
                case 8:
                bets [8] = new JCheckBox("Large > 10");
                break;
                case 7:
                bets [7] = new JCheckBox("Small < 11");
                break;
                case 6: 
                bets [6] = new JCheckBox("Field < 8 or > 12");
                break;
                default:
                bets [i] = new JCheckBox(i + 1 + "s");
            }

            position.insets = new Insets(0,10,2,0);
            position.gridy++;
            add(bets[i], position);

            // associate the JCheckBox with the action listener
            bets[i].addActionListener(this); 
        }
    }

    /****************************************************************
     * Start a new game
     ****************************************************************/
    private void newGame(){
        // reset the game
        game.reset();

        // clear all check boxes 
        clearAllBets();
    }

    /****************************************************************
     * setup Menu
     ****************************************************************/
    private void setupMenu () {
        fileMenu = new JMenu("File");
        quitItem = new JMenuItem("Quit");
        newGameItem = new JMenuItem("New Game");
        addItem = new JMenuItem("Add 10 Credits");
        fileMenu.add(addItem);
        fileMenu.add(newGameItem);
        fileMenu.add(quitItem);
        menus = new JMenuBar();
        setJMenuBar(menus);
        menus.add(fileMenu); 

        // register the three menu items with this action listener
        quitItem.addActionListener(this);
        newGameItem.addActionListener(this);
        addItem.addActionListener(this);       
    }

    /****************************************************************
     * This method called when the roll button or a menu item
     * is selected
     *
     * @param event - the JComponent just selected
     ****************************************************************/
    public void actionPerformed(ActionEvent event){
        // player selects menu to quit the game
        if (event.getSource() == quitItem)
            System.exit(1);

        // player selects menu item to start a new game    
        if (event.getSource() == newGameItem)
            newGame();

        // player selects menu item to add more credits    
        if (event.getSource() == addItem)
            game.addCredits(10);        

        // player clicks on Roll button  
        if (event.getSource() == rollButton)    
            processRoll();

        // update the credits and message labels
        credits.setText("Credits: " + game.getCredits()); 
        message.setText(game.getMessage());     
    }

    /****************************************************************
     * finds out what bets are checked to invoke the method 
     * placeBet from the game
     ****************************************************************/
    private void placeBets (){
        numberBets = 0;
        for (int i = 0; i < 9 ; i++) 
            if (bets [i].isSelected()) {
                game.placeBet(i + 1);
                numberBets++;
            }
    }

    /****************************************************************
     * clear all bets for the new round
     ****************************************************************/
    private void clearAllBets () {
        for (int i = 0 ; i <= bets.length - 1 ; i++) {
            bets[i].setSelected (false);
            numberBets = 0;
        }
    }

    /****************************************************************
     * processRoll - if there are enough credits to play
     * invokes the roll method from the game.
     ****************************************************************/
    private void processRoll() {
        placeBets();
        if(!game.enoughCredits()) {
            JOptionPane.showMessageDialog(null, "Not enough credits to play your bet(s)");
            clearAllBets ();
            game.clearAllBets();
        }
        else {
            if (numberBets > 0)
                game.roll(); 
            else
                JOptionPane.showMessageDialog(null, "Make bet(s)");
        }
    }
}