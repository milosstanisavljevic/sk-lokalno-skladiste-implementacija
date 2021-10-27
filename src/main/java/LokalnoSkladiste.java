import com.google.gson.Gson;

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
    public void makeConfig(String path) throws Exception {
        super.makeConfig(path);
    }

    @Override
    public void makeUser(String path) throws Exception {
        super.makeUser(path);
    }

    public void createRoot(String path, String name){
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
