import java.io.*;

public class Serializer {
    private  ByteArrayInputStream in;
    private  ByteArrayOutputStream out;
    private  ObjectInputStream is;
    private  ObjectOutputStream os;

    public Serializer(){

    }

    public ByteArrayOutputStream serialize(Object obj) throws IOException {
        out = new ByteArrayOutputStream();
        os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out;
    }

    public  ObjectInputStream deserialize(byte[] ba) throws IOException {
        in = new ByteArrayInputStream(ba);
        is = new ObjectInputStream(in);
        return is;
    }

    public void closeConnections(){
        try {
            in.close();
            out.close();
            is.close();
            os.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
