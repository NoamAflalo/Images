import java.util.Scanner;
import java.io.*;

class Image{// This class represents an Image. An image is composed of several pixels.
  Pixel[][] pixels;
  int width, height, max;

  public Image(String filename){
      File file = new File (filename);
      Scanner sc=null;
      try {
          sc = new Scanner(file);
          sc.next();
          this.width=sc.nextInt();
          this.height=sc.nextInt();
          this.max=sc.nextInt();
          this.pixels= new Pixel[this.height][this.width];
          for (int i=0;i<this.height;i++){
            for (int j=0;j<this.width;j++){
              this.pixels[i][j]=new Pixel(sc.nextInt(),sc.nextInt(),sc.nextInt());
            }
          }
      } catch(IOException e){
                  System.out.println("File I/O error!");
                  e. printStackTrace ();
      }
      finally{
        if(sc!=null) {sc.close();}
      }
  }


public Image darken(){
  for (int i=0;i<this.height;i++){
    for (int j=0;j<this.width;j++){
      this.pixels[i][j].red=this.pixels[i][j].red/2;
      this.pixels[i][j].green=this.pixels[i][j].green/2;
      this.pixels[i][j].blue=this.pixels[i][j].blue/2;
    }
  }
  return this;
}


public Image lighten(){
  for (int i=0;i<this.height;i++){
    for (int j=0;j<this.width;j++){
      this.pixels[i][j].red= this.pixels[i][j].red*2<= this.max ? this.pixels[i][j].red*2 : this.max ;
      this.pixels[i][j].green=this.pixels[i][j].green*2 <= this.max ? this.pixels[i][j].green*2 : this.max;
      this.pixels[i][j].blue=this.pixels[i][j].blue*2 <= this.max ? this.pixels[i][j].blue*2 : this.max;
    }
  }
  return this;
}

public Image reverseX(){
  for(int i=0;i<this.height;i++){
    for (int j=0;j<this.width/2;j++){
      Pixel tmp=this.pixels[i][j];
      this.pixels[i][j]=this.pixels[i][width-1-j];
      this.pixels[i][width-1-j]=tmp;
    }
  }
  return this;
}

public Image reverseY(){
  for(int i=0;i<this.height/2;i++){
    for (int j=0;j<this.width;j++){
      Pixel tmp=this.pixels[i][j];
      this.pixels[i][j]=this.pixels[height-i-1][j];
      this.pixels[height-i-1][j]=tmp;
    }
  }
  return this;
}

Pixel[][] copy(){  //We will need it in the method blur().
  Pixel[][] same =new Pixel[this.height][this.width];
  for(int i=0;i<this.height;i++){
    for (int j=0;j<this.width;j++){
      same[i][j]=this.pixels[i][j];
    }
  }
  return same;
}

public Image blur(){
  Pixel[][] old =this.copy();
  for(int i=0; i<this.height; i++){
    for(int j=0; j<this.width;j++){
      int averageRed=0;
      int averageGreen=0;
      int averageBlue=0;
      int counter = 0;
      for (int k=i-5;k<=i+5;k++){
        for (int l=j-5;l<=j+5;l++){ //On prend un voisinage (ie une couronne) de taille 5.Ce qui donne un résultat semblable à celui de Michael.
                                      //Plus la couronne est grande plus l'image est flou.
          if (k>=0 && l>=0 && k<this.height && l<this.width) { //Cette condition permet de floutter la totalité de l'image, même les "bords".
            averageRed+=old[k][l].red;
            averageBlue+=old[k][l].blue;
            averageGreen+=old[k][l].green;
            counter++;
          }
        }
      }
      this.pixels[i][j].red=averageRed/counter;
      this.pixels[i][j].blue=averageBlue/counter;
      this.pixels[i][j].green=averageGreen/counter;
    }
  }
  return this;
}


public void write(String filename){
      PrintWriter writer = null ;
      try{
          writer = new PrintWriter (filename);
          writer.println("P3");
          writer.println(this.width+" "+this.height);
          writer.println(this.max);
          for(int i=0;i<this.height;i++){
            for (int j=0;j<this.width;j++){
              writer.println(this.pixels[i][j].red+" "+this.pixels[i][j].green+" "+this.pixels[i][j].blue+ " ");
            }
            writer.println();
          }
      }
      catch(IOException e){
            System.out.println("File I/O error (write)!"); e. printStackTrace ();
      }
      finally{
              if(writer!=null) {writer.close();}
              }

}

public static void main(String[] args) {
  System.out.println("-----------------------------------------------------------------------------");
  System.out.println("Bonjour, vous êtes le bienvenue sur ce programme de modification d'image.");
  System.out.println("-----------------------------------------------------------------------------\n");
  System.out.println("J'espère que vous avez entré en ligne de commande le nom du fichier ppm à modifier suivit du nom du nouveau fichier.");
  System.out.println("Si ce n'est pas le cas, veuillez recommencer.");
  System.out.println("Exemple : java Image cat.ppm newCat.ppm\n");
  System.out.println("Taper 1 Si vous voulez rendre l'image plus sombre\n\n");
  System.out.println("Taper 2 Si vous voulez rendre l'image plus lumineuse\n\n");
  System.out.println("Taper 3 Si vous voulez reverser l'image de façon horyzontale\n\n");
  System.out.println("Taper 4 Si vous voulez renverser l'image de façon verticale\n\n");
  System.out.println("Taper 5 si vous voulez rendre l'image plus flou");
  Scanner sc= new Scanner(System.in);
  int p=sc.nextInt();
  while(p!=0){
  while(p!=1 & p!=2 && p!=3 && p!=4 && p!=5){
    System.out.println("Veuillez entrer 1, 2,3, 4 ou 5");
    sc= new Scanner(System.in);
    p=sc.nextInt();
  }
  if (p==1){
      Image darkCat =new Image(args[0]);
      darkCat.darken().write(args[1]);
    }

    if (p==2){
      Image lightCat= new Image(args[0]);
      lightCat.lighten().write(args[1]);
    }

    if(p==3){
      Image reverseXCat=new Image(args[0]);
      reverseXCat.reverseX().write(args[1]);
    }

    if(p==4){
      Image reverseYCat=new Image(args[0]);
      reverseYCat.reverseY().write(args[1]);
    }
    if (p==5){
      Image blurCat= new Image(args[0]);
        blurCat.blur().write(args[1]);
    }
    System.out.println(" Veuillez entrer 0 pour arreter. Sinon veuilez entrer l'option correspondante : 1,2, 3, 4, 5");
    sc= new Scanner(System.in);
    p=sc.nextInt();
  }
}
}
