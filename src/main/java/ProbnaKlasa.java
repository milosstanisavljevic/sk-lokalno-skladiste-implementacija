import java.io.IOException;

public class ProbnaKlasa {

    private static int a = 0;
    public static void main(String[] args) {
        LokalnoSkladiste ls = new LokalnoSkladiste();
        //ls.createRoot("C:\\Users\\Milos\\Desktop" , "root");
       // ls.createRoot("C:\\Users\\milja\\Desktop" , "root");
        try {
            //ls.makeDefaultConfig("C:\\Users\\milja\\Desktop\\root");
            //ls.updateConfig("C:\\Users\\milja\\Desktop\\root", 20000000, ".json", 3);
            ls.makeDefaultUser("C:\\Users\\milja\\Desktop\\root");
           // ls.addUser("C:\\Users\\milja\\Desktop\\root", "username3", "username3", 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //ls.checkIfRootExists("C:\\Users\\Milos\\OneDrive\\Desktop\\root1");
//        try {
//            //ls.makeConfig("C:\\Users\\Milos\\OneDrive\\Desktop");
//            System.out.println("adesad");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
