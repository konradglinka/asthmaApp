package Objects;

public final class PickflowmeterWear {
    private final int iD;
    private final int iDUser;
    private final String purchaseDate;

    public PickflowmeterWear(int iD, int iDUser, String purchaseDate) {
        this.iD = iD;
        this.iDUser = iDUser;
        this.purchaseDate = purchaseDate;
    }

    public int getiD() {
        return iD;
    }

    public int getiDUser() {
        return iDUser;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }
}
