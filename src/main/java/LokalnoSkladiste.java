
import java.io.File;
import java.io.IOException;


public class LokalnoSkladiste extends SpecifikacijaSkladista{

    static {
        Manager.registerImpl(new LokalnoSkladiste());
    }

    private String rootDirectoryPath;
    private int brojac = 0;
    private int brojac1 = 0;


    @Override
    public boolean checkUser(String path, String username1, String password1) {
        return super.checkUser(path, username1, password1);
    }

    public LokalnoSkladiste() {
    }

    public void checkPrivileges() {
    }

    @Override
    public void addUser(String path, String name, String password, int privilege) throws Exception {
        super.addUser(path, name, password, privilege);
    }

    @Override
    public void makeDefaultConfig(String path, String username) {
        super.makeDefaultConfig(path, username);
    }

    @Override
    public void updateConfig(String path, Object size, Object filetype, Object maxNumber) throws Exception {
        super.updateConfig(path, size, filetype, maxNumber);
    }

    @Override
    public void makeDefaultUser(String path, String username, String password) {
        super.makeDefaultUser(path, username, password);
    }

    @Override
    public boolean createRoot(String path, String username, String password){
        File root = new File(path);
        setRootDirectoryPath(path);
        boolean b = root.mkdir();
        if(b){
            try {
                makeDefaultConfig(path, username);
                makeDefaultUser(path, username, password);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }else
            System.out.println("Error!");
            return false;
    }

    @Override
    public boolean checkIfRootExists(String path){
        File f = new File(path);
        if(f.exists()){
            return true;
        }else{
            return false;
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
