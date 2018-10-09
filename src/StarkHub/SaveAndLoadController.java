package StarkHub;

import java.io.*;

public class SaveAndLoadController {

    public static void save(String filename,Object object){
        try
        {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(object);
            out.close();
            file.close();
            System.out.println("Object is saved");
        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
    }

    public static Object load(String filename){
        Object object=null;
        try
        {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            object = in.readObject();
            in.close();
            file.close();
            System.out.println("Object is loaded");
        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }
        return object;
    }
}
