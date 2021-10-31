
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


public class LokalnoSkladiste extends SpecifikacijaSkladista{

    static {
        Manager.registerImpl(new LokalnoSkladiste());
    }

    private String rootDirectoryPath;
    private int brojac = 0;
    private int brojac1 = 0;



    public LokalnoSkladiste() {
    }

    public void checkPrivileges() {
    }

    @Override
    public void addUser(String path, String name, String password, int privilege) throws Exception {
        super.addUser(path, name, password, privilege);
    }

    @Override
    public void makeDefaultConfig(String path) {
        super.makeDefaultConfig(path);
    }

    @Override
    public void updateConfig(String path, Object size, Object filetype, Object maxNumber) throws Exception {
        super.updateConfig(path, size, filetype, maxNumber);
    }

    @Override
    public void makeDefaultUser(String path) {
        super.makeDefaultUser(path);
    }

    @Override
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

    @Override
    public void checkIfRootExists(String path){
        File f = new File(path);
        if(f.exists()){
            System.out.println("Root is already created");
        }else{
            createRoot(path,"newRoot");
        }
    }

    @Override
    public void createFile(String fileName){
        String p = getRootDirectoryPath() + "\\" + fileName;
        File f  = new File(p);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createMoreFiles(int n) {
        for (int i = 0; i<n; i++){
            createFile("myFile" + brojac++);
        }
    }

    @Override
    public void createMoreFolders(int n) {
        for (int i = 0; i<n; i++){
            createFolder("myFolder" + brojac1++);
        }
    }

    @Override
    public void createFolder(String folderName){
        String p = getRootDirectoryPath() + "\\" +folderName;
        File f = new File(p);
        f.mkdir();
    }

    @Override
    public void deleteFile(String name) {
        try {
            File f = new File(rootDirectoryPath + "\\" + name);
            if(f.delete()){
                System.out.println(name + "deleted");
            }else{
                System.out.println("File was not deleted");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFolder(String s) {

    }

    public String getRootDirectoryPath() {
        return rootDirectoryPath;
    }

    public void setRootDirectoryPath(String rootDirectoryPath) {
        this.rootDirectoryPath = rootDirectoryPath;
    }
}
