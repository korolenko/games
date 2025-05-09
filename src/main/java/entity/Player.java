package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player  extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    public final  int screenX;
    public final  int screenY;

    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage(){
        up1 = getBufferedImage("/player/boy_up_1.png");
        up2 = getBufferedImage("/player/boy_up_2.png");
        down1 = getBufferedImage("/player/boy_down_1.png");
        down2 = getBufferedImage("/player/boy_down_2.png");
        left1 = getBufferedImage("/player/boy_left_1.png");
        left2 = getBufferedImage("/player/boy_left_2.png");
        right1 = getBufferedImage("/player/boy_right_1.png");
        right2 = getBufferedImage("/player/boy_right_2.png");
    }

    private BufferedImage getBufferedImage(String path){
        try{
            return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        }catch (IOException e){
            throw  new RuntimeException("Unable to load resource: " + path);
        }
    }

    public void update(){
        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){
            if(keyH.upPressed){
                direction = "up";
            } else if(keyH.downPressed){
                direction = "down";
            } else if (keyH.leftPressed){
                direction = "left";
            } else if (keyH.rightPressed){
                direction = "right";
            }

            //check tile collision
            collisionOn = false;
            gp.checker.checkTile(this);

            //if collision is false, player can move
            if(!collisionOn){
                switch (direction){
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

            spriteCounter++;
            if(spriteCounter > 14){
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2){
        g2.setColor(Color.white);
        g2.fillRect(worldX,worldY, gp.tileSize, gp.tileSize);

        BufferedImage image = null;
        switch (direction){
            case "up":
                if(spriteNum == 1){
                    image = up1;
                }
                if(spriteNum == 2){
                    image = up2;
                }
                break;
            case "down":
                if(spriteNum == 1){
                    image = down1;
                }
                if(spriteNum == 2){
                    image = down2;
                }
                break;
            case "left":
                if(spriteNum == 1){
                    image = left1;
                }
                if(spriteNum == 2){
                    image = left2;
                }
                break;
            case "right":
                if(spriteNum == 1){
                    image = right1;
                }
                if(spriteNum == 2){
                    image = right2;
                }
                break;
            default:
                throw new RuntimeException("Unknown direction: " + direction);
        }
        g2.drawImage(image, screenX,screenY,gp.tileSize, gp.tileSize, null);
    }


}
