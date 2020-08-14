/**
 * OrderDlg
 * Custom dialog class with methods to input details of an order,
 *  and to create an order record.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*; //Date, collection classes

public class TransactionDlg extends JDialog implements ActionListener {
    private MainMenu parent;

    //GUI
    private String creditLegend = "Create Credits",
    debitLegend = "Create Debits";
    private JTextField txtAccountID;
    private JTextField txtAmount;
    private JButton btnSubmit, btnHide;

    // Constructor
    public TransactionDlg(MainMenu p) {
        setTitle(creditLegend);
        parent = p; //data structures are here

        //Components -
        txtAccountID = new JTextField(10); //input field, 10 columns wide
        txtAmount = new JTextField(6); 
        btnSubmit = new JButton("Submit");
        btnHide   = new JButton("Hide");

        //Layout -
        JPanel pnl = new JPanel(), cpnl = new JPanel();
        pnl.add(new JLabel("Account ID:"));
        pnl.add(txtAccountID);
        cpnl.add(pnl);
        pnl = new JPanel();
        pnl.add(new JLabel("Amount:"));
        pnl.add(txtAmount);
        cpnl.add(pnl);
        add(cpnl, BorderLayout.CENTER);

        pnl = new JPanel();
        pnl.add(btnSubmit);
        pnl.add(btnHide);
        add(pnl, BorderLayout.SOUTH);

        setBounds(100, 100, 300, 200);

        //Action
        btnSubmit.addActionListener(this);
        btnHide.addActionListener(this);
    } //end constructor

    /**
     * Credit/debit logic methods
     */ 
    public void setCreditMode() { setTitle(creditLegend); }

    public void setDebitMode()  { setTitle(debitLegend); }

    public boolean isInCreditMode() {return getTitle().equals(creditLegend);}

    public boolean isInDebitMode()  { return getTitle().equals(debitLegend); }

    /**
     * Actions: on click of 'Submit', make a transaction record and add to database;
     *          on click of 'Hide', hide the dialogue window.
     */
    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == btnHide) {
            setVisible(false);
            txtAccountID.setText("");
            txtAmount.setText("");
        }
        else if (src == btnSubmit) {
            if (createRcdOk()) {
                txtAccountID.setText("");
                txtAmount.setText("");
            }
        }
    } //end actionPerformed

    public boolean createRcdOk(){
        int transactionAmount = -1;

        //creates validation booleans
        boolean accountValid = false;
        boolean amountValid = false;
        boolean readAsFloat = false;

        //gets the user input text and assign it to appropriate variables
        String accountID = txtAccountID.getText();
        String amount = txtAmount.getText();

        //Read through the accounts hashmap        
        for (Map.Entry<String, CurrentAccount> entry : parent.getAccounts().entrySet()){

            //Retrieves the key for the currentAccount
            String key = entry.getKey();            
            //validate entered account ID
            if (key.equals(accountID)){
                accountValid = true;
            }
            
        }  

        try {
            //if a decimal value was entered, it is read as a float
            if(amount.contains(".")) {
                if(amount.length() - amount.indexOf(".") == 3 ){
                    float floatTransactionAmount = Float.parseFloat(amount);
                    floatTransactionAmount = floatTransactionAmount * 100;
                    transactionAmount = (int) floatTransactionAmount;
                }
            }
            //otherwise it is read as an integer
            else
            {
                transactionAmount = Integer.parseInt(amount);
            }

            //checks that the amount is positive
            if(transactionAmount > 0)
            {
                amountValid = true;
            }
            else
            {
                System.out.println("Amount: " + transactionAmount + "\nPlease enter a positive value\n" );
            }

            //carry out transaction
            //checks that the user inputted data is valid
            if (amountValid == true && accountValid == true){
                //If it is a debit transaction
                if (isInDebitMode()){
                    //the value is made negative
                    transactionAmount = -transactionAmount;
                    Transaction newTransaction = new Transaction(accountID, transactionAmount);
                    parent.createTransaction(newTransaction);
                }
                //otherwise it is left positive
                else
                {
                    Transaction newTransaction = new Transaction(accountID, transactionAmount);
                    parent.createTransaction(newTransaction);
                }
            }
            else {
                System.out.print("Amount: " + transactionAmount + "\nPlease enter a valid amount or a valid Account ID\n");
            }
        }
        catch(NumberFormatException e){
            System.out.println("Amount: " + transactionAmount + "\nError: Please enter a number\n");
        }

        return false;
    } //end createRcdOk()

} //end class

