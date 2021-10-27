import java.io.IOException;

public class ProbnaKlasa {

    private static int a = 0;
    public static void main(String[] args) {
        LokalnoSkladiste ls = new LokalnoSkladiste();
        ls.createRoot("C:\\Users\\milja\\Desktop" , "root");
        //ls.checkIfRootExists("C:\\Users\\Milos\\OneDrive\\Desktop\\root1");
//        try {
//            //ls.makeConfig("C:\\Users\\Milos\\OneDrive\\Desktop");
//            System.out.println("adesad");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
