package banking;

public class Main {

    public static void main(String[] args) {
        Database sqLite = new Database("jdbc:sqlite:" + args[1]);
        SimpleBank simpleBank = new SimpleBank(sqLite);
        simpleBank.init();
    }
}