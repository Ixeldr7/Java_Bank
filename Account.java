/**
 * Account
 * Base class to Encapsulate accounts managed by Bigg City Bank
 */
public class Account {
    protected String accountID;
    protected Integer owner;
    protected String sortCode;
    protected int balance;

    //Constructor
    public Account(String aID, Integer onr, String sCd, int bal) {
        accountID = aID;
        owner = onr;
        sortCode = sCd;
        balance = bal;
    }

    public static String moneyFormat(int amount) {
        int pounds = amount/100, pence = amount%100;
        return String.format("%d.%02d", pounds, pence);
    }

    //Accessors  
    public String getAccountID()  { return accountID; } 

    public Integer getOwner()     { return owner;     }

    public String getSortCode()   { return sortCode;  }

    public int getBalance()       { return balance;   }

    public boolean credit(int amount)  {
        if (amount < 0) {
            System.out.printf("Warning: account %s: negative credit %s ignored\n",
                this, moneyFormat(amount));
        } else {
            balance += amount;
            return true;
        }
        return false;
    }

    //Returns true of debit operation successful
    public boolean debit(int amount)  {
        if (amount < 0) {
            System.out.printf("Warning: account %s: negative debit %s ignored\n",
                this, moneyFormat(amount));
        } else {
            int newBal = balance - amount;
            if (newBal < 0) {
                System.out.printf("Warning: account %s: insufficient funds for debit %s\n",
                    this, moneyFormat(amount));
            } else {
                balance = newBal;
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return String.format("%s: %04d: %s: %8d",
            accountID, owner, sortCode, balance);
    }

}
