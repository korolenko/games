package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_key extends SuperObject{
    public OBJ_key(){
        name = "key";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
