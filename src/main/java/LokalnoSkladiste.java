
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


public class LokalnoSkladiste extends SpecifikacijaSkladista{

    private String rootDirectoryPath;
    private static int brojac;



    public LokalnoSkladiste() {
    }

    public void checkPrivileges() {
    }

    @Override
    public void makeDefaultConfig(String path) {
        super.makeDefaultConfig(path);
    }

    @Override
    public void makeDefaultUser(String path) {
        super.makeDefaultUser(path);
    }

    public void createRoot(String path, String name){
        path = path + '\\' + name;
        File root = new File(path);
        setRootDirectoryPath(path);
        boolean b = root.mkdir();
        if(b){
            try {
                makeDefaultConfig(path);
                makeDefaultUser(path);
                createFile("probica.txt");
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    public void createFile(String fileName){
        String p = getRootDirectoryPath() + "\\" + fileName;
        File f  = new File(p);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createFolder(String folderName){
        String p = getRootDirectoryPath() + "\\" +folderName;
        File f = new File(p);
        f.mkdir();
    }

    public String getRootDirectoryPath() {
        return rootDirectoryPath;
    }

    public void setRootDirectoryPath(String rootDirectoryPath) {
        this.rootDirectoryPath = rootDirectoryPath;
    }
}
