import java.io.File;
import java.util.Scanner;

public class LokalnoSkladiste{

    private String rootDirectoryPath;
    private static int brojac;
    public LokalnoSkladiste() {
    }


    //    public void createRoot(String path,String name){
//       path = path + '\'' + name;
//       File root = new File(path);
//       boolean b = root.mkdir();
//       if(b){
//           System.out.println("root is created successfully");
//       }else
//           System.out.println("Error!");
//    }


    //public static void main(String[] args) {

        //createRoot("C:\\Users\\Milos\\OneDrive\\Desktop","Proba");
        /*System.out.println("Enter the path where you want to create a folder: ");
        Scanner sc = new Scanner(System.in);
        String path = sc.next();
        //Using Scanner class to get the folder name from the user
        System.out.println("Enter the name of the desired a directory: ");
        path = path+sc.next();
        //Instantiate the File class
        File f1 = new File(path);
        //Creating a folder using mkdir() method
        boolean bool = f1.mkdir();
        if(bool){
            System.out.println("Folder is created successfully");
        }else{
            System.out.println("Error Found!");
        }*/

    //}
    public void createRoot(String path,String name){
        path = path + '\\' + name;
        File root = new File(path);
        setRootDirectoryPath(path);
        boolean b = root.mkdir();
        if(b){
            System.out.println("root is created successfully");
        }else
            System.out.println("Error!");
    }
    public void checkIfRootExists(String path){
        File f = new File(path);
        if(f.exists()){
            System.out.println("Root is already created");
        }else{
            createRoot(path,"newRoot");
        }
    }

    public void setRootDirectoryPath(String rootDirectoryPath) {
        this.rootDirectoryPath = rootDirectoryPath;
    }
}
