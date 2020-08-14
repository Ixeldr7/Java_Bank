/**
 *  MainMenu class for Bigg City Bank system
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*; //ArrayList; HashMap; LinkedList;

public class MainMenu extends JFrame implements ActionListener {
    // data collections
    private Map<Integer, Client> clients;
    private Map<String, CurrentAccount> accounts;
    private List<Transaction> pendingTransactions, completedTransactions;

    //GUI
    private TransactionDlg transactionDlg;
    private JButton btnLoadData,
    btnCredit, btnDebit, btnProcessTransactions,
    btnListClients, btnListAccounts, btnListPending,
    btnListCompleted, btnSaveData;

    //To launch the application
    public static void main(String[] args) {
        MainMenu app = new MainMenu();
        app.setVisible(true);
    }

    // Constructor
    public MainMenu() {
        // Database
        clients = new HashMap<Integer, Client>();
        accounts = new HashMap<String, CurrentAccount>();
        pendingTransactions = new LinkedList<Transaction>();
        completedTransactions = new LinkedList<Transaction>();

        // GUI - create custom dialog instances
        transactionDlg = new TransactionDlg(this);

        // GUI - set window properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(200, 100, 250, 300);

        //GUI - main menu buttons    
        JPanel mainPnl = new JPanel();
        mainPnl.setLayout(new GridLayout(8,1));

        btnLoadData = new JButton("Load Data");
        btnLoadData.addActionListener(this);
        mainPnl.add(btnLoadData);

        btnCredit = new JButton("Credit");
        btnCredit.addActionListener(this);
        mainPnl.add(btnCredit);

        btnDebit = new JButton("Debit");
        btnDebit.addActionListener(this);
        mainPnl.add(btnDebit);

        btnProcessTransactions = new JButton("Process Transactions");
        btnProcessTransactions.addActionListener(this);
        mainPnl.add(btnProcessTransactions);

        btnListClients = new JButton("List Clients");
        btnListClients.addActionListener(this);
        mainPnl.add(btnListClients);

        btnListAccounts = new JButton("List Accounts");
        btnListAccounts.addActionListener(this);
        mainPnl.add(btnListAccounts);

        btnListPending = new JButton("List Pending Transactions");
        btnListPending.addActionListener(this);
        mainPnl.add(btnListPending);

        btnListCompleted = new JButton("List Completed Transactions");
        btnListCompleted.addActionListener(this);
        mainPnl.add(btnListCompleted);

        btnSaveData = new JButton("Save Data");
        btnSaveData.addActionListener(this);
        mainPnl.add(btnSaveData);
        //TODO: More button setup

        add(mainPnl, BorderLayout.CENTER);
    } //end constructor

    //Accessors for data structures
    public Map<Integer, Client>  getClients()         { return clients;      }   

    public Map<String, CurrentAccount> getAccounts()  { return accounts;     }

    public List<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }

    public List<Transaction> getCompletedTransactions() {
        return completedTransactions;
    }

    /**
     * Actions in response to button clicks
     */
    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        //read data
        if (src == btnLoadData) { 
            loadClientData();
            loadAccountData();
            loadTransactionData();
            btnLoadData.setEnabled(false);      
        }
        else if (src == btnCredit) { // dialog will do multiple credit transactions
            transactionDlg.setCreditMode();
            transactionDlg.setVisible(true);
        }
        else if (src == btnDebit) { // dialog will do multiple debit transactions
            transactionDlg.setDebitMode();
            transactionDlg.setVisible(true);
        }
        else if (src == btnProcessTransactions) { // iterate through orders
            processTransactions();
        }
        else if (src == btnListClients) { 
            listClients();
        }
        else if (src == btnListAccounts) { 
            listAccounts();
        }
        else if (src == btnListPending) {
            listPendingTransactions();
        }
        else if (src == btnListCompleted) {
            listCompletedTransactions();
        }
        else if (src == btnSaveData){
            saveData();
        }

    } // end actionPerformed()

    /**
     * Load data from clients.txt using a Scanner; unpack and populate
     *   clients Map.
     */
    public void loadClientData() {
        String fnm="", snm="", pcd="";
        int num=0, id=1;
        try {
            Scanner scnr = new Scanner(new File("clients.txt"));
            scnr.useDelimiter("\\s*#\\s*");
            //fields delimited by '#' with optional leading and trailing spaces
            while (scnr.hasNextInt()) {
                id  = scnr.nextInt();
                snm = scnr.next();
                fnm = scnr.next();
                num = scnr.nextInt();
                pcd = scnr.next();
                clients.put(new Integer(id), new Client(id, snm, fnm, num, pcd));
            }
            scnr.close();
        } catch (NoSuchElementException e) {
            System.out.printf("%d %s %s %d %s\n", id, snm, fnm, num, pcd);
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "fetch of next token failed ", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File Not Found",
                "Unable to open data file", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Clients loaded");
    } //end readCustomerData()

    /**
     * Lists the client on the console */
    public void listClients() {
        System.out.println("Clients:");
        for (Client c: clients.values()) {
            System.out.println(c);
        }
        System.out.println();
    } //listCustomers()

    /**
     * Read data from currentAccounts.txt using a Scanner; unpack and populate
     *   accounts Map.
     */
    public void loadAccountData() {
        String id="", srtCd="";
        int onr=0, bal=0, crLm=0;
        try {
            Scanner scnr = new Scanner(new File("currentAccounts.txt"));
            scnr.useDelimiter("\\s*#\\s*");
            while (scnr.hasNext()) {
                id  = scnr.next();
                onr = scnr.nextInt();
                srtCd = scnr.next();
                bal = scnr.nextInt();
                crLm = scnr.nextInt();
                accounts.put(id, new CurrentAccount(id, onr, srtCd, bal, crLm));
            }
            scnr.close();
        } catch (NoSuchElementException e) {
            System.out.printf("%s %d %s, %d, %d\n", id, onr, srtCd, bal, crLm);
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "fetch of next token failed ", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File Not Found",
                "Unable to open data file", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Account data loaded");
    } //end readItemData()

    /**
     * Lists the client on the console */
    public void listAccounts() {
        System.out.println("Current Accounts:");
        for (CurrentAccount a: accounts.values()) {
            System.out.println(a);
        }
        System.out.println();
    } 
    
    /**
     * Adds the new transaction to the pending list */
    public void createTransaction(Transaction t){
        
        pendingTransactions.add(t);        
    }
    
    public void processTransactions(){
        int amount = 0 ;
        String accID = "", transactionID = "";

        //loops through the transaction objects which are in the pending list
        for (Iterator<Transaction> iter = pendingTransactions.iterator(); iter.hasNext(); ) {
            Transaction processing = iter.next();
            //retreives the ID from the transaction currently being processed
            transactionID = processing.getAccountID();

            //iterates through each of the existing accounts
            for (CurrentAccount a: accounts.values()) {

                //retrieves the account ID of the currently indexed account
                accID = a.getAccountID();

                //Comapres the two ID's
                if (accID.equals(transactionID)) {
                    //retrieves the amount of money for the requested transaction
                    amount = processing.getAmount();
                    //if the amount is negative a debit transaction is carried out
                    if(amount < 0) {
                        if(a.debit(-amount)){
                            System.out.println("Transaction " + transactionID + " was succesful\n");                            
                            //adds the transaction to the completed transaction list
                            completedTransactions.add(processing);
                            //removes the transaction from the pending transaction list
                            pendingTransactions.remove(processing);
                        }
                        else
                        {
                            System.out.println("Transaction " + transactionID + " was unsuccesful\n");
                        }                       
                    }
                    //otherwise a credit transaction is carried out
                    else
                    {
                        if(a.credit(amount)){                            
                            System.out.println("Transaction " + transactionID + " was succesful\n");
                            //adds the transaction to the completed transaction list
                            completedTransactions.add(processing);
                            //removes the transaction from the pending transaction list
                            pendingTransactions.remove(processing);
                        }
                        else
                        {
                            System.out.println("Transaction " + transactionID + " was unsuccesful\n");
                        }
                    }   
                }
            }
        }       
    }

    //list pending transactions on the console
    public void listPendingTransactions(){

        System.out.println("Pending Transactions: \n");
        //iterates through the pending transaction list
        for(int i =0; i<pendingTransactions.size(); i++){

            System.out.println(pendingTransactions.get(i));

        }
    }

    //List completed transactions on the console
    public void listCompletedTransactions() {

        System.out.println("Completed Transactions: \n");
        //iterates through the completed transaction list
        for(int i =0; i<completedTransactions.size(); i++){

            System.out.println(completedTransactions.get(i));

        }
    }

    //Saves data
    public void saveData() {
        updateAccounts();
        saveTransactions();
    }

    //Updates the file currentAccounts.txt
    public void updateAccounts(){
        try{
            //creates a new PrintWriter
            PrintWriter writer = new PrintWriter("currentAccounts.txt", "UTF-8");
            //loops through the existing account objects
            for (CurrentAccount a: accounts.values()) {

                //writes the appropriate variables of the transaction object to the text file
                writer.println(a.getAccountID() + "#");                
                writer.println(a.getOwner() + "#"); 
                writer.println(a.getSortCode() + "#");
                writer.println(a.getBalance() + "#");
                writer.println(a.getOverdraftLimit() + "#\n");                
            }
            //closes the writer once all the accounts have been iterated
            writer.close();
            System.out.println("The account data was updated successfully.\n");
        }
        //If there is an error an appropriate message is sent to the console
        catch (Exception e) {
            System.out.println("An error occured when updating accounts.\n");          
        }
    }
    
    //saves transaction data
    public void saveTransactions(){
        try{
            //creates a new PrintWriter
            PrintWriter writer = new PrintWriter("Transactions.txt", "UTF-8");
            //loops through the transaction objects which are pending
            for (Iterator<Transaction> iter = pendingTransactions.iterator(); iter.hasNext(); ) {
                Transaction toWrite = iter.next();
                //writes the appropriate variables of the transaction object to the text file
                writer.println(toWrite.getAccountID() + "#");
                writer.println(toWrite.getAmount() + "#");
                writer.println(toWrite.getDateTimeStamp() + "#\n");
            }
            //closes the writer once all the transactions have been iterated
            writer.close();
            System.out.println("The transaction data saved successfully.\n");
        }
        //If there is an error an appropriate message is sent to the console
        catch (Exception e) {
            System.out.println("An error occured when saving the pending transactions.\n");          
        }
    }
    
    //load saved pending transactions
    public void loadTransactionData() {
        String id="";
        int amount=0;
        long dts=0; 
        try {
            Scanner scnr = new Scanner(new File("Transactions.txt"));
            scnr.useDelimiter("\\s*#\\s*");
            while (scnr.hasNext()) {
                id  = scnr.next();
                amount = scnr.nextInt();
                dts = scnr.nextLong();                
                pendingTransactions.add(new Transaction(id, amount, dts));
            }
            scnr.close();
        } catch (NoSuchElementException e) {
            System.out.printf("%s %d %tD\n", id, amount, dts);
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "fetch of next token failed ", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File Not Found",
                "Unable to open data file", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Transaction data loaded");
    }

} //end class

