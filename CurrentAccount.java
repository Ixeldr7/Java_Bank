/**
 * CurrentAccount
 * Extends Account class to Encapsulate current accounts managed by Bigg City Bank
 */
public class CurrentAccount extends Account {
    private int overdraftLimit;

    //Constructor
    public CurrentAccount(String aID, Integer onr, String sCd, int bal, int odLim) {
        super(aID, onr, sCd, bal);
        overdraftLimit = odLim;
    }

    //Accessors  
    public int  getOverdraftLimit ()        { return overdraftLimit; } 

    public void setOverdraftLimit (int lim) { overdraftLimit = lim;  } 

    //debit function overrides Account.debit, taking overdraft limit into account
    //Returns true if debit operation successful
    public boolean debit(int amount)  {
        if (amount < 0) {
            System.out.printf("Warning: account %s: negative debit %s ignored\n",
                this, moneyFormat(amount));
        } else {
            int newBal = balance - amount;
            if (newBal < -overdraftLimit) {
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
        return super.toString() + ": " + String.format("%8d", overdraftLimit);
    }

}
