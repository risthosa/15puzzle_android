package com.example.a15puzzle;

public class Combination {
    static int[] gameNums;
    static byte[] path;

    static int emptyBox, a;
    static void swap(int emptyIndex, int swpIndex){
        int tmp;
        tmp = gameNums[emptyIndex];
        gameNums[emptyIndex] = gameNums[swpIndex];
        gameNums[swpIndex] = tmp;
    }

    static void slideBox(int x, int n){
        switch (x){
            case 0:
                swap(emptyBox, emptyBox-1);
                emptyBox = emptyBox-1;
                break;

            case 1:
                swap(emptyBox, emptyBox-n);
                emptyBox = emptyBox-n;
                break;

            case 2:
                swap(emptyBox, emptyBox+1);
                emptyBox = emptyBox+1;
                break;

            default:
                swap(emptyBox, emptyBox+n);
                emptyBox = emptyBox+n;
                break;
        }
    }
    static void newCombination(int n){
        gameNums = new int[n*n];
        a = n*n*n + 5*n;
        path = new byte[7 * a];

        emptyBox = n * n - 1;

        for(int i = 0; i < gameNums.length; i++){
            gameNums[i] = i + 1;
        }

        for (int i = 0; i < a; i++){
            if (emptyBox / n > 0 && emptyBox / n < n-1 && emptyBox % n > 0 && emptyBox % n < n - 1){
                do{
                    path[i] = (byte)Math.floor((Math.random()*4));
                } while (Math.abs(path[i] - path[i-1])==2);

                slideBox(path[i], n);
            }
            else if(emptyBox % n == n-1){
                if (emptyBox / n == 0){
                    do{
                        path[i] = (byte)((Math.round(Math.random()))*3);
                    } while (Math.abs(path[i] - path[i-1])==2);
                    slideBox(path[i], n);
                }

                else if(emptyBox / n == n-1){
                    do{
                        path[i] = (byte) Math.round(Math.random());
                    } while (i != 0 && (Math.abs(path[i] - path[i-1])==2));
                    slideBox(path[i], n);
                }

                else{
                    do{
                        path[i] = (byte) Math.floor(Math.random()*3);
                        if (path[i] == 2)
                            path[i]++;
                    } while (Math.abs(path[i] - path[i-1])==2);
                    slideBox(path[i], n);
                }
            }

            else if(emptyBox % n == 0){
                if (emptyBox / n == 0){
                    do{
                        path[i] = (byte)(Math.floor(Math.random()*2)+2);
                    } while (Math.abs(path[i] - path[i-1])==2);
                    slideBox(path[i], n);
                }

                else if(emptyBox / n == n-1){
                    do{
                        path[i] = (byte)(Math.floor(Math.random()*2)+1);
                    } while (Math.abs(path[i] - path[i-1])==2);
                    slideBox(path[i], n);
                }

                else{
                    do{
                        path[i] = (byte) (Math.floor(Math.random()*3)+1);
                    } while (Math.abs(path[i] - path[i-1])==2);
                    slideBox(path[i], n);
                }
            }

            else if(emptyBox / n == 0){
                do{
                    path[i] = (byte) (Math.floor(Math.random()*3));
                    if (path[i] == 1)
                        path[i]--;
                } while (Math.abs(path[i] - path[i-1])==2);
                slideBox(path[i], n);
            }

            else {
                do{
                    path[i] = (byte) (Math.floor(Math.random()*3));
                } while (Math.abs(path[i] - path[i-1])==2);
                slideBox(path[i], n);
            }
        }

        /*
        int parite=0;
        for(int i = 0 ; i < gameNums.length; i++){
            while(gameNums[i] != i+1){
                int tmp = gameNums[i];
                gameNums[i] = gameNums[tmp-1];
                gameNums[tmp-1] = tmp;
                parite++;
            }
        }*/
        /*if(parite % 2 == 1){
            System.out.println("hatatata");
        }*/
    }




    /*static void newCombination(int n){
        gameNums = new int[n*n];
        for(int i = 0; i < gameNums.length-1; i++)
            gameNums[i] = i + 1;

        for(int i = 0; i < gameNums.length-1; i++){
            int randIndex = (int) (Math.random() * (n*n-1));
            int tmp = gameNums[i];
            gameNums[i] = gameNums[randIndex];
            gameNums[randIndex] = tmp;
        }
        gameNums[n*n-1] = 0;
        int[] sirali = gameNums.clone();
        int parite = 0;

        for(int i = 0 ; i < sirali.length-1; i++){
            while(sirali[i] != i+1){
                int tmp = sirali[i];
                sirali[i] = sirali[tmp-1];
                sirali[tmp-1] = tmp;
                parite++;
            }
        }
        if(parite % 2 == 1){
            for(int i = 0; i < gameNums.length-1; i++){
                if(gameNums[i] != i+1){
                    int tmp1 = gameNums[i];
                    gameNums[i] = gameNums[tmp1-1];
                    gameNums[tmp1-1]= tmp1;
                    break;
                }
            }
        }
    }*/
}
