/**
 * Customer
 * Encapsulates a registered customer of the business
 */
public class Client {
    private int cltID;
    private String surName;
    private String frstName;
    private int houseNumber;
    private String postCode;

    //Constructor
    public Client(Integer cID, String sn, String fn, int hn, String pcd) {
        cltID = cID;
        surName = sn;
        frstName = fn;
        houseNumber = hn;
        postCode = pcd;
    }

    //Accessors
    public Integer getCltID()   {  return cltID;       }

    public String getSurName()  {  return surName;     }

    public String getFrstName() {  return frstName;    }

    public String getName()     {  return surName+ ", " + frstName; }

    public int getHouseNumber() {  return houseNumber; }

    public String getPostCode() {  return postCode;    }

    public String toString() {
        return String.format("%04d: %s %s (%d, %s)",
            cltID, frstName, surName, houseNumber, postCode);
    }

    public String briefString() {
        return String.format("%04d: %s %s", cltID, frstName, surName);
    }

}
