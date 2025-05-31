package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

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
    public int hasKeys = 0;

    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
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
        up1 = setUp("boy_up_1");
        up2 = setUp("boy_up_2");
        down1 = setUp("boy_down_1");
        down2 = setUp("boy_down_2");
        left1 = setUp("boy_left_1");
        left2 = setUp("boy_left_2");
        right1 = setUp("boy_right_1");
        right2 = setUp("boy_right_2");
    }

    public BufferedImage setUp(String imageName){
        UtilityTool uTool = new UtilityTool();
        BufferedImage scaledImage = null;
        try {
            scaledImage = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
            scaledImage = uTool.scaleImage(scaledImage, gp.tileSize, gp.tileSize);
        }catch (Exception e){
            e.printStackTrace();
        }
        return scaledImage;
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

            int objIndex = gp.checker.checkObject(this, true);
            pickUpObject(objIndex);

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

    public void pickUpObject(int i){
        if(i != 999){
            String objectName = gp.obj[i].name;
            switch (objectName){
                case "Key":
                    gp.playSE(1);
                    hasKeys++;
                    gp.obj[i] = null;
                    gp.ui.showMessage("You've got a key!");
                    System.out.println("Key: " + hasKeys);
                    break;
                case "Door":
                    if(hasKeys > 0){
                        gp.playSE(3);
                        gp.obj[i] = null;
                        hasKeys--;
                        System.out.println("Key: " + hasKeys);
                        gp.ui.showMessage("You've opened the door!");
                    } else {
                        gp.ui.showMessage("You need a key!");
                    }
                    break;
                case "Boots":
                    gp.playSE(2);
                    speed += 2;
                    gp.obj[i] = null;
                    gp.ui.showMessage("Speed up!");
                    break;
                case "Chest":
                    gp.ui.gameFinished = true;
                    gp.stopMusic();
                    gp.playSE(4);
                    break;
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
        g2.drawImage(image, screenX,screenY, null);
    }


}
